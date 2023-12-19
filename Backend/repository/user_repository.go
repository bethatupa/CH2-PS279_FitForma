package repository

import (
	"context"
	"math/rand"
	"net/http"
	"strconv"
	"time"

	"cloud.google.com/go/firestore"
	"github.com/bethatupa/CH2-PS279_FitForma/helper"
	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/bethatupa/CH2-PS279_FitForma/model/web"
	"github.com/labstack/echo/v4"
	"golang.org/x/crypto/bcrypt"
	"google.golang.org/api/iterator"
)

const collectionName string = "users"

type UserRepository interface {
	Save(ctx context.Context, user *entity.User) (*entity.User, error)
	Authenticate(ctx context.Context, req *entity.LoginPayload) error
	FindAll(ctx context.Context) (*[]entity.User, error)
	FindById(ctx context.Context, id string) (*entity.User, error)
}

type repo struct {
	projectID string
	client    *firestore.Client
}

func NewUserRepository(ctx context.Context, projectId string) (UserRepository, error) {
	client, err := firestore.NewClient(ctx, projectId)
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Failed to create a Firestore client")
	}
	return &repo{
		client: client,
	}, nil
}

func (u *repo) Save(ctx context.Context, user *entity.User) (*entity.User, error) {
	emailExists, err := helper.ValidateEmailExists(ctx, u.client, collectionName, user.Email)
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Error checking email existence:", err)
	}

	if emailExists {
		return nil, echo.NewHTTPError(http.StatusBadRequest, web.ValidationMessage{Message: "Email has been used."})
	}

	password, err := helper.HashAndSalted([]byte(user.Password))
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusBadRequest, bcrypt.ErrPasswordTooLong.Error())
	}
	_, _, err = u.client.Collection(collectionName).Add(ctx, map[string]interface{}{
		"ID":        rand.Intn(10000),
		"Email":     user.Email,
		"Password":  string(password),
		"Country":   user.Country,
		"CreatedAt": time.Now(),
	})
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Failed to adding a new user")
	}
	return user, nil
}

func (u *repo) Authenticate(ctx context.Context, req *entity.LoginPayload) error {
	docRef := u.client.Collection(collectionName).Where("Email", "==", req.Email).Documents(ctx)
	defer docRef.Stop()

	var userExists bool
	for {
		docSnap, err := docRef.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			return echo.NewHTTPError(http.StatusInternalServerError, err.Error())
		}
		if docSnap.Exists() {
			userExists = true
			hashedPassword, ok := docSnap.Data()["Password"].(string)
			if !ok {
				return echo.NewHTTPError(http.StatusInternalServerError, "Password field is not a string type")
			}
			err := bcrypt.CompareHashAndPassword([]byte(hashedPassword), []byte(req.Password))
			if err == nil {
				return nil
			}
		}
	}
	if !userExists {
		return echo.NewHTTPError(http.StatusBadRequest, "User not found.")
	}

	return echo.NewHTTPError(http.StatusUnauthorized, "Invalid email or password.")
}

func (u *repo) FindAll(ctx context.Context) (*[]entity.User, error) {
	var users []entity.User
	iter := u.client.Collection("users").Documents(ctx)
	defer iter.Stop()
	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			if err != nil {
				return nil, echo.NewHTTPError(http.StatusInternalServerError, err.Error())
			}
		}
		var user entity.User
		if err := doc.DataTo(&user); err != nil {
			return nil, echo.NewHTTPError(http.StatusInternalServerError, err.Error())
		}
		users = append(users, user)
	}
	return &users, nil
}

func (u *repo) FindById(ctx context.Context, id string) (*entity.User, error) {
	userId, _ := strconv.Atoi(id)
	iter := u.client.Collection(collectionName).Where("ID", "==", userId).Limit(1).Documents(ctx)
	doc, err := iter.Next()
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusNotFound, "User not found.")
	}
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, err.Error())
	}
	var user entity.User
	if err := doc.DataTo(&user); err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, err.Error())
	}

	return &user, nil
}

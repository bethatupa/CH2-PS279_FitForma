package repository

import (
	"context"
	"net/http"
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
	Save(user *entity.User) (*entity.User, error)
	Authenticate(req *entity.LoginPayload) error
}

type repo struct {
	projectID string
}

func NewUserRepository(projectId string) UserRepository {
	return &repo{
		projectID: projectId,
	}
}

func (u *repo) Save(user *entity.User) (*entity.User, error) {
	ctx := context.Background()
	client, err := firestore.NewClient(ctx, u.projectID)
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Failed to create a Firestore client")
	}
	defer client.Close()

	emailExists, err := helper.ValidateEmailExists(ctx, client, collectionName, user.Email)
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
	_, _, err = client.Collection(collectionName).Add(ctx, map[string]interface{}{
		"Email":     user.Email,
		"Password":  password,
		"Country":   user.Country,
		"CreatedAt": time.Now(),
	})
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Failed to adding a new user")
	}
	return user, nil
}

func (u *repo) Authenticate(req *entity.LoginPayload) error {
	ctx := context.Background()
	client, err := firestore.NewClient(ctx, u.projectID)
	if err != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "Failed to create a Firestore client")
	}
	defer client.Close()

	docRef := client.Collection(collectionName).Where("Email", "==", req.Email).Documents(ctx)
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
			hashedPassword, ok := docSnap.Data()["Password"].([]uint8)
			if !ok {
				return echo.NewHTTPError(http.StatusInternalServerError, "Password field is not a []uint8 type")
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

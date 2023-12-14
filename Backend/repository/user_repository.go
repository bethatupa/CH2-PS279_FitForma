package repository

import (
	"cloud.google.com/go/firestore"
	"context"
	"github.com/bethatupa/CH2-PS279_FitForma/helper"
	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/labstack/echo/v4"
	"golang.org/x/crypto/bcrypt"
	"net/http"
	"time"
)

const (
	projectID      string = "fitforma-prod"
	collectionName string = "users"
)

type UserRepository interface {
	Save(user *entity.User) (*entity.User, error)
}

type repo struct{}

func NewUserRepository() UserRepository {
	return &repo{}
}

func (u *repo) Save(user *entity.User) (*entity.User, error) {
	ctx := context.Background()
	client, err := firestore.NewClient(ctx, projectID)
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Failed to create a Firestore client : %v", err)
	}
	defer client.Close()
	password, err := helper.HashAndSalted([]byte(user.Password))
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, bcrypt.ErrPasswordTooLong.Error())
	}
	_, _, err = client.Collection(collectionName).Add(ctx, map[string]interface{}{
		"Email":     user.Email,
		"Password":  password,
		"Country":   user.Country,
		"CreatedAt": time.Now(),
	})
	if err != nil {
		return nil, echo.NewHTTPError(http.StatusInternalServerError, "Failed to adding a new user: %v", err)
	}
	return user, nil
}

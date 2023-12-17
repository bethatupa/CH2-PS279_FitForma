package app

import (
	"math/rand"
	"net/http"
	"time"

	"github.com/bethatupa/CH2-PS279_FitForma/helper"
	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/bethatupa/CH2-PS279_FitForma/model/web"
	"github.com/bethatupa/CH2-PS279_FitForma/repository"
	"github.com/go-playground/validator"
	"github.com/golang-jwt/jwt/v5"
	"github.com/labstack/echo/v4"
)

type RouterInterface interface {
	CreateUser(c echo.Context) error
	Authenticate(c echo.Context) error
}

type Router struct {
	repo repository.UserRepository
	key  []byte
	v    *validator.Validate
}

func NewRouter(validator *validator.Validate, repo repository.UserRepository, secretKey []byte) RouterInterface {
	return &Router{
		v:    validator,
		repo: repo,
		key:  secretKey,
	}

}

func (r *Router) CreateUser(c echo.Context) error {
	req := web.UserRequest{}
	user := entity.User{
		Email:    req.Email,
		Password: req.Country,
		Country:  req.Password,
	}
	err := c.Bind(&user)
	if err != nil {
		return c.JSON(http.StatusInternalServerError,
			web.ApiError{StatusCode: http.StatusInternalServerError, Error: "Error unmarshalling data:"})
	}
	if err := r.v.Struct(user); err != nil {
		exception, ok := err.(validator.ValidationErrors)
		errorResponse := make([]web.ValidationMessage, len(exception))
		if ok {
			for i, fe := range exception {
				errorResponse[i] = web.ValidationMessage{Message: helper.WriteMsgForTag(fe.Field(), fe.Tag())}
			}
		}
		return c.JSON(http.StatusBadRequest, web.ApiError{StatusCode: http.StatusBadRequest, Error: errorResponse})
	}
	user.ID = rand.Int()
	_, err = r.repo.Save(&user)
	if err != nil {
		return c.JSON(http.StatusBadRequest, web.ApiError{
			StatusCode: http.StatusBadRequest,
			Error:      err,
		})
	}
	return c.JSON(http.StatusOK, web.ApiResponse{
		StatusCode: http.StatusOK,
		Message:    "Succesfully created user",
		Data: web.UserCreatedResponse{
			ID:        user.ID,
			CreatedAt: time.Now(),
		},
	})
}

func (r *Router) Authenticate(c echo.Context) error {
	req := web.LoginRequest{}
	loginPayload := &entity.LoginPayload{
		Email:    req.Email,
		Password: req.Password,
	}
	err := c.Bind(&loginPayload)
	if err != nil {
		return c.JSON(http.StatusInternalServerError,
			web.ApiError{StatusCode: http.StatusInternalServerError, Error: "Error unmarshalling data:"})
	}
	err = r.repo.Authenticate(loginPayload)
	if err != nil {
		return c.JSON(http.StatusBadRequest, web.ApiError{StatusCode: http.StatusBadRequest, Error: err})
	}

	var token entity.JwtTokenPayload
	lifeTimeToken := 1

	token.RegisteredClaims = jwt.RegisteredClaims{
		Issuer:    "fitforma",
		IssuedAt:  jwt.NewNumericDate(time.Now()),
		ExpiresAt: jwt.NewNumericDate(time.Now().Add(time.Hour * time.Duration(lifeTimeToken))),
	}

	token.Email = loginPayload.Email
	_token := jwt.NewWithClaims(jwt.SigningMethodHS256, token)
	tokenResult, _ := _token.SignedString(r.key)
	return c.JSON(http.StatusOK, web.LoginResponse{
		StatusCode: http.StatusOK,
		Message:    "Login sucesfully",
		Data: struct {
			AccessToken string `json:"accessToken"`
		}{
			AccessToken: tokenResult,
		},
	})
}
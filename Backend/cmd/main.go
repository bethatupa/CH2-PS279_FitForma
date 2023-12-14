package main

import (
	"math/rand"
	"net/http"
	"time"

	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/bethatupa/CH2-PS279_FitForma/model/web"
	"github.com/bethatupa/CH2-PS279_FitForma/repository"
	"github.com/go-playground/validator"
	"github.com/labstack/echo/v4"
)

func main() {
	e := echo.New()
	v := validator.New()
	var repo repository.UserRepository = repository.NewUserRepository()

	e.POST("/api/v1/users", func(c echo.Context) error {
		req := web.UserRequest{}
		user := entity.User{
			Email:    req.Email,
			Password: req.Country,
			Country:  req.Password,
		}
		err := c.Bind(&user)
		if err != nil {
			return c.JSON(http.StatusInternalServerError,
				web.ApiError{StatusCode: http.StatusBadRequest, Error: "Error unmarshalling data:"})
		}
		if err := v.Struct(user); err != nil {
			return c.JSON(http.StatusBadRequest, web.ApiError{StatusCode: http.StatusBadRequest, Error: err.Error()})
		}
		user.ID = rand.Int()
		_, err = repo.Save(&user)
		if err != nil {
			return c.JSON(http.StatusInternalServerError, web.ApiError{
				StatusCode: http.StatusInternalServerError,
				Error:      err})
		}
		return c.JSON(http.StatusOK, web.ApiResponse{
			StatusCode: http.StatusOK,
			Message:    "Succesfully created user",
			Data: web.UserCreatedResponse{
				ID:        user.ID,
				CreatedAt: time.Now(),
			},
		})
	})
	e.Logger.Fatal(e.Start(":1234"))
}

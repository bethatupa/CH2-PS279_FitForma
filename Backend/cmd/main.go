package main

import (
	"encoding/json"
	"math/rand"
	"net/http"
	"time"

	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/bethatupa/CH2-PS279_FitForma/model/web"
	"github.com/bethatupa/CH2-PS279_FitForma/repository"
	"github.com/labstack/echo/v4"
)

func main() {
	e := echo.New()
	var repo repository.UserRepository = repository.NewUserRepository()

	e.POST("/api/v1/users", func(c echo.Context) error {
		var user entity.User
		err := json.NewDecoder(c.Request().Body).Decode(&user)
		if err != nil {
			return c.JSON(http.StatusInternalServerError, []byte(`{"error": "Error unmarshaling data"}`))
		}
		user.ID = rand.Int()
		_, err = repo.Save(&user)
		if err != nil {
			return c.JSON(http.StatusInternalServerError, err.Error())
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

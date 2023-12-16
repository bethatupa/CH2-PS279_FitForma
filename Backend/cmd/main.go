package main

import (
	"log"
	"os"

	"github.com/bethatupa/CH2-PS279_FitForma/app"
	"github.com/bethatupa/CH2-PS279_FitForma/repository"
	"github.com/go-playground/validator"
	"github.com/labstack/echo/v4"
)

var SECRET_KEY []byte

func main() {
	secretKey, ok := os.LookupEnv("MY_SECRET_KEY")
	if !ok {
		log.Fatal("Set your secret key")
	}
	SECRET_KEY = []byte(secretKey)

	projectId, ok := os.LookupEnv("PROJECT_ID")
	if !ok {
		log.Fatal("Set your firestore project id")
	}

	e := echo.New()
	v := validator.New()
	var repo repository.UserRepository = repository.NewUserRepository(projectId)
	router := app.NewRouter(v, repo, SECRET_KEY)

	e.POST("/api/v1/users", router.CreateUser)
	e.POST("/api/v1/auth", router.Authenticate)
	e.Logger.Fatal(e.Start(":1234"))
}

package main

import (
	"context"
	"log"
	"os"

	"github.com/bethatupa/CH2-PS279_FitForma/app"
	"github.com/bethatupa/CH2-PS279_FitForma/repository"
	"github.com/go-playground/validator"
	"github.com/labstack/echo/v4"
)

var SECRET_KEY []byte

func main() {
	e := echo.New()
	v := validator.New()
	ctx := context.Background()
	secretKey, ok := os.LookupEnv("MY_SECRET_KEY")
	if !ok {
		log.Fatal("Set your secret key")
	}
	SECRET_KEY = []byte(secretKey)
	projectId, ok := os.LookupEnv("PROJECT_ID")
	if !ok {
		log.Fatal("Set your firestore project id")
	}
	repo, err := repository.NewUserRepository(ctx, projectId)
	if err != nil {
		log.Fatalf("error firestore : %v", err)
	}

	router := app.NewRouter(ctx, v, repo, SECRET_KEY)

	e.POST("/api/v1/users", router.CreateUser)
	e.POST("/api/v1/auth", router.Authenticate)
	e.GET("/api/v1/users", router.GetAllUsers)
	e.Logger.Fatal(e.Start(":1234"))
}

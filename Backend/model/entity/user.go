package entity

import (
	"time"
)

type User struct {
	ID        int
	Email     string `validate:"required,email" json:"email"`
	Password  string `validate:"required" json:"password"`
	Country   string `validate:"required" json:"country"`
	CreatedAt time.Time
}

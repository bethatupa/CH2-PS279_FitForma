package entity

import (
	"time"
)

type User struct {
	ID        int       `json:"id"`
	Email     string    `json:"email"`
	Password  string    `json:"password"`
	Country   string    `json:"country"`
	CreatedAt time.Time `json:"createdAt"`
}

package web

import "time"

type UserCreatedResponse struct {
	ID        int       `json:"id"`
	CreatedAt time.Time `json:"createdAt"`
}

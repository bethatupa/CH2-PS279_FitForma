package web

import "time"

type UserCreatedResponse struct {
	CreatedAt time.Time `json:"createdAt"`
}

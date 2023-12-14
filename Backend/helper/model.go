package helper

import (
	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/bethatupa/CH2-PS279_FitForma/model/web"
)

func EntityToCreateUser(user entity.User) *web.UserCreatedResponse {
	return &web.UserCreatedResponse{
		ID:        user.ID,
		CreatedAt: user.CreatedAt,
	}
}

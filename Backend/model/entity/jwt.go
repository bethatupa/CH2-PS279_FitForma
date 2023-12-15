package entity

import "github.com/golang-jwt/jwt/v5"

type JwtTokenPayload struct {
	Email string `json:"email"`
	jwt.RegisteredClaims
}

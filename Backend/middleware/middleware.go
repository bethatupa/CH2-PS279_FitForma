package middleware

import (
	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/golang-jwt/jwt/v5"
	echojwt "github.com/labstack/echo-jwt/v4"
	"github.com/labstack/echo/middleware"
	"github.com/labstack/echo/v4"
)

func JWTMiddleware(secretKey []byte) echo.MiddlewareFunc {
	middleware.ErrJWTMissing.Code = 401
	middleware.ErrJWTMissing.Message = "Unauthorized"

	return echojwt.WithConfig(echojwt.Config{
		NewClaimsFunc: func(c echo.Context) jwt.Claims {
			return new(entity.JwtTokenPayload)
		},
		SigningKey: secretKey,
	})

}

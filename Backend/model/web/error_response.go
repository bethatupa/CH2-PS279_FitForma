package web

type ApiError struct {
	StatusCode int `json:"statusCode"`
	Error      any `json:"error"`
}


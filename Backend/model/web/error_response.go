package web

type ValidationMessage struct {
	Message any `json:"message"`
}

type ApiError struct {
	StatusCode int `json:"statusCode"`
	Error      any `json:"error"`
}

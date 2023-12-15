package helper

func WriteMsgForTag(field string, tag string) string {
	switch tag {
	case "required":
		return field + " is required"
	case "email":
		return "Email is not valid"
	}
	return ""
}

package helper

import (
	"context"

	"cloud.google.com/go/firestore"
	"github.com/bethatupa/CH2-PS279_FitForma/model/entity"
	"github.com/bethatupa/CH2-PS279_FitForma/model/web"
	"google.golang.org/api/iterator"
)

func EntityToCreateUser(user entity.User) *web.UserCreatedResponse {
	return &web.UserCreatedResponse{
		ID:        user.ID,
		CreatedAt: user.CreatedAt,
	}
}

func ValidateEmailExists(ctx context.Context, client *firestore.Client, collectionName, email string) (bool, error) {
	iter := client.Collection(collectionName).Where("Email", "==", email).Documents(ctx)
	defer iter.Stop()

	for {
		_, err := iter.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			return false, err
		}
		return true, nil
	}

	return false, nil
}

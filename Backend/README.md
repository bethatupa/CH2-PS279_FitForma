# FitForma Authentication
Rest API for Registration,authenticiation Fitforma Apps

## Endpoints

### Create User 
```bash
POST api/v1/users
Request e.g 
    {
        "email":string,
        "password":string,
        "country":string,
    }
```
### Get Access Token
```bash
POST api/v1/auth
Request e.g 
    {
        "email":string,
        "password":string,
    }

Response e.g
    {
        "statusCode":int,
        "message":string,
        "data":{
            "accessToken":string
        }
    }

```

# FitForma Authentication
Simple Restful API for Fitforma Apps


## Setup Environtment

```bash
export MY_SECRET_KEY = ""
export PROJECT_ID=""
```

## Endpoints

### Create User 
```bash
POST api/v1/users
Request e.g 
  {                               
      "email":string,
      "password":string,
      "country":string
  }                            
```
### Get Users            
```bash                    
GET api/v1/users          
Response e.g                
    {                      
        "statusCode":int,
        "message":string,
        "data":[
          "id":int,
          "email":string,
          "password":string,
          "country":string,
          "createdAt":timestamp
        ]
    }                      
```                        
### Get User By Id
```bash
GET api/v1/users/id
Response e.g 
  {
      "statusCode":int,
      "message":string,
      "data":{
        "id":int,
        "email":"string",
        "password":string,
        "country":string,
        "createdAt":timestamp
      }
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
## Database Design
![1702884951](https://github.com/bethatupa/CH2-PS279_FitForma/assets/93138224/46034e2e-0ff9-4bb1-a749-9c216f4e34fb)



# FitForma Authentication
Rest API for Registration,authenticiation Fitforma Apps


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
      "statusCode":int,        
      "message":string,        
      "data":{                 
        "id":int,
        "email":string,
        "password":string,
        "country":string,
        "createdAt":timestamp
      }                        
  }                            
```
### Get users            
```bash                    
GET api/v1/users          
Response e.g                
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

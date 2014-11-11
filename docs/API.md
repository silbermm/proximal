
### Getting Started
The api is exposed via REST and JSON. To make a call to one of the endpoints you'll need an HTTP client. To call the endpoint, just use the appropriate method i.e (GET,POST,PUT,DELETE), the url and the body (if required). The web service will send back a response code (200, 401) and a JSON response.

### Making Authenticated Calls
To call an endpoint where authentication is required you will need an auth token. Use the `login` endpoint to recieve that token and then add a header to you api call named `X-Auth-Token` with the token as a value. 

### Available EndPoints

#### Authentication Methods
	
##### Login
Method: `POST`
URL   : `/api/authenticate/userpass`
BODY  : `username={users email}&password={users password}`
EXAMPLE of RETURNED JSON:
```
{
	"token":"a9c8d56cca45cc0195b812f3fcf009af9695fb62bdb3c1ca50e3f5078bbe67d167f90ee1958fe9c13c4c27b32c35cceb722e5e3704e3f607f3f829d1fc004cddb8c2b7ea74be7745217b3c80de0f6362e067a1b973645cf0ed7d7ccc85167a6c739e85f487efcd7e3b38812679c6c862f7f7073b953e7b81a1a448622fd400c2",
	"expiresOn":"2014-10-17T10:12:14.116-04:00"
}
```


#### Standards 
| METHOD | URL | BODY | RESPONSE CODES | JSON EXPECTED |
| ------ | --- | ---- | -------------- | ------------- |
| GET    | 

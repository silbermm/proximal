[<< Back to API Docs](API.md)

# Authentication Methods

###[Login](#login)

#### Method
POST

#### URL
`/api/authenticate/userpass`

#### BODY
`username=username&password=password`

#### RETURN CODES
200 if the login was successful
401 if the username/password combination was incorrect

#### RETURNED JSON OBJECT
```
{
  "token": "",
  "expiresOn": ""
}
```

#### Example of returned Json
```
{
	"token":"a9c8d56cca45cc0195b812f3fcf009af9695fb62bdb3c1ca50e3f5078bbe67d167f90ee1958fe9c13c4c27b32c35cceb722e5e3704e3f607f3f829d1fc004cddb8c2b7ea74be7745217b3c80de0f6362e067a1b973645cf0ed7d7ccc85167a6c739e85f487efcd7e3b38812679c6c862f7f7073b953e7b81a1a448622fd400c2",
	"expiresOn":"2014-10-17T10:12:14.116-04:00"
}
```

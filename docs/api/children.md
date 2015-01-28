[<-- Back to API Docs](API.md)

#Children Methods

###[List all Children](#list-all-children)
List all of the children for the currently logged in user

#### METHOD
`GET`

#### URL
`/api/v1/children`

#### RETURNS
A list of children JSON Objects
```json
[
  {
    "id":22,
    "firstName":"FirstName",
    "lastName":"LastName",
    "birthDate":1207281600
    }
]
```

###[Get Specific Child](#get-specific-child)
Find the specific child for the logged in user 

#### METHOD
`GET`

#### URL
Replace {id} with the id of the child
`/api/v1/children/{id}`

#### RETURNS
The specific child object
```json
{
  "id":22,
  "firstName":"FirstName",
  "lastName":"LastName",
  "birthDate":1207281600
}
```
###[Add a Child](#add-a-child)
Add a new child to the currently logged in user

#### METHOD
`POST`

#### URL
`/api/v1/children`

#### BODY
Send a child JSON object as the BODY
```json
{
  "firstName":"asdfasdfasdf",
  "lastName":"asdfasdf",
  "birthDate":1415163600
}
```

#### RETURNS
The child json object that was created
```json
{  
    "id": 23,
    "firstName":"asdfasdfasdf",
    "lastName":"asdfasdf",
    "birthDate":1415163600
}
```

###[Delete a Child](#delete-a-child)
Delete a child for the currently logged in user

#### METHOD
`DELETE`

#### URL
Replace {id} with the actual id of the child to delete
`/api/v1/children/{id}`

#### RETURN CODE
`200 OK`

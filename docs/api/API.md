
### Getting Started
The api is exposed via REST and JSON. To make a call to one of the endpoints you'll need an HTTP client. To call the endpoint, just use the appropriate method i.e (GET,POST,PUT,DELETE), the url and the body (if required). The web service will send back a response code (200, 401) and a JSON response.

### Making Authenticated Calls
To call an endpoint where authentication is required you will need an auth token. Use the `login` endpoint to recieve that token and then add a header to you api call named `X-Auth-Token` with the token as a value. 

### Available EndPoints
	
#### Authentication

| DESCRIPTION                 | METHOD  | URL                        | AUTH REQUIRED? | DETAILS |
| -----------                 | ------- | ---                        | -------------- | ------- | 
| Login In                    | POST    | /api/authenticate/userpass | NO             | [View Details](authenticate.md#login)
| Get logged in users profile | GET     | /api/v1/profile            | YES            | [View Details](authenticate.md#profile)


#### Children
| DESCRIPTION                           | METHOD  | URL                   | AUTH REQUIRED? | DETAILS 										 |
| -----------                           | ------- | ---                   | -------------- | -------                                         | 
| Get all children for logged in user   | GET     | /api/v1/children      | YES            |  [View Details](children.md#list-all-children)  | 
| Get specific child for logged in user | GET     | /api/v1/children/{id} | YES            |  [View Details](children.md#get-specific-child) |
| Add a child to the logged in user     | POST    | /api/v1/children/add  | YES            |  [View Details](children.md#add-a-child)        |
| Delete a child from the logged in user| DELETE  | /api/v1/children/{id} | YES            |  [View Details](children.md#delete-a-child)     |

#### Standards

| DESCRIPTION                 | METHOD  | URL                                                       | AUTH REQUIRED? | DETAILS |
| -----------                 | ------- | ---                                                       | -------------- | ------- | 
| Get all available standards | GET     | /api/v1/standards                                         | NO             | [View Details](standards.md#list-all-standards)  
| Get a specific standard     | GET     | /api/v1/standards/{id}                                    | NO             | [View Details](standards.md#get-a-specific-standard)
| Create a standard           | POST    | /api/v1/standards                                         | YES            | [View Details](standards.md#create-a-standard)
| Update a standard           | PUT     | /api/v1/standards/{id}                                    | YES            | [View Details](standards.md#update-a-standard)
| Delete a standard           | DELETE  | /api/v1/standards/{id}                                    | YES            | [View Details](standards.md#delete-a-standard)
| Get all statements in a standard | GET | /api/v1/standards/{standardid}statements                 | NO             | [View Details](standards.md#get-all-statements-in-a-standard) |
| Create a statement in a standard | POST | /api/v1/standards/{standardid}/statements               | YES            | [View Details](standards.md#create-a-statement) |
| Update a statement in a standard | PUT  | /api/v1/standards/{standardid}/statements/{statementid} | YES            | [View Details](standards.md#update-a-statement) |
| Delete a statement in a standard | DELETE | /api/v1/standards/{standardid/statements/{statementid}| YES            | [View Details](standards.md#delete-a-statement) |

#### Questions
| DESCRIPTION                 | METHOD  | URL                                                       | AUTH REQUIRED? | DETAILS |
| -----------                 | ------- | ---                                                       | -------------- | ------- | 
| Get all Questions           | GET     | /api/v1/questions                                         | NO             | [View Details](questions.md#list-all-questions)
| Get a Specific Question     | GET     | /api/v1/questions/{id}                                    | NO             | [View Details](questions.md#get-a-specific-question)
| Get all Questions for a statement | GET | ???                                                     | NO             | [View Details](questions.md#get-all-questions-for-a-statement)

#### Assessments
| DESCRIPTION					   | METHOD | URL 								| AUTH REQUIRED? |  |
| -----------					   | ------ | ---								| -------------- |  |
| Start a new Assessment           | POST   | /api/v1/assessments				| YES			 | [View Details](assessments.md#new-dynamic-assessment)
| Rate a Question on an assessment | PUT	| /api/v1/assessments/{assessmentid}| YES			 | [View Details](assessments.md#score-a-question)











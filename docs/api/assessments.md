[<< Back to API Docs](API.md)

#Assessment Methods

### [New Dynamic Assessment](#new-dynamic-assessment)

#### METHOD
`POST`

#### URL
`/api/v1/assessments`

#### BODY
Send a childId and standardId as a JSON object as the BODY (the child and standard must already exist in the system)
```json
{
  "childId" : 340,	
  "standardId" : 4569
}
```
#### RETURNS
A JSON object with two keys, "assessment" and "question". The "assesment" key is the assessment object that was created. The "question" object is the first question in the assessment.
Example:
```json
{
	'assessment' : {
		'id' : 1,
		'beginDate' : 1422220207510,
	},
	'question' : {
		'id': 1,
		'text': 'What is the first letter of the alphabet?',
		'picture' : 'somelongstringofnumbersrepreseningthebase64picture',
		'typeId' : 4,
		'answer' : "A"
	}
}
```

### [Score a Question](#score-a-question)

#### METHOD
`PUT`

#### URL
`/api/v1/assessments/:assessmentId`

#### BODY
Send the Scored Question as a JSON object
```json
{
	'studentId': 23,
	'questionId': 570,
	'score' : 5,
	'timestamp': 1422220207510
}
``` 

#### RETURNS
The next question to answer
```json
{
	'assessment' : {
		'id' : 1,
		'beginDate' : 1422220207510,
	},
	'question' : {
		'id': 1,
		'text': 'What is the first letter of the alphabet?',
		'picture' : 'somelongstringofnumbersrepreseningthebase64picture',
		'typeId' : 4,
		'answer' : "A"
	}
}
```
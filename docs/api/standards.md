[<-- Back to API Docs](API.md)

#Standards Methods

###[List All Standards](#all)
List all of the publically available standards that the system knows about 

#### METHOD
GET

#### URL
`/api/v1/standards`

#### RETURN CODE
200 if the request was successful

#### RETURNS
A List of Standards Objects

```json
[
  {"id":1,
   "title":"Standard Name",
   "description":"Some long description",
   "publicationStatus":"published",
   "subject":"subject name",
   "language":"English",
   "source":"url to a place where the standard is documented",
   "dateValid":1262322000000,
   "repositoryDate":1299560400000,
   "rights":"© Copyright 2010",
   "manifest":"http://asn.jesandco.org/resources/D10003FB/manifest.json",
   "identifier":"http://purl.org/ASN/resources/D10003FB"
  },
  {
    "id":2,
    "title":"Common Core State Standards for English Language Arts & Literacy in History/Social Studies, Science, and Technical Subjects",
    "description":"The Common Core State Standards for English Language Arts & Literacy in History/Social Studies, Science, and Technical Subjects (\"the Standards\") are the culmination of an extended, broad-based effort to fulfill the charge issued by the states to create the next generation of K—12 standards in order to help ensure that all students are college and career ready in literacy no later than the end of high school.",
    "publicationStatus":"published",
    "subject":"english",
    "language":"English",
    "source":"http://www.corestandards.org/assets/CCSSI_ELA%20Standards.pdf",
    "dateValid":1262322000000,
    "repositoryDate":1299560400000,
    "rights":"© Copyright 2010. National Governors Association Center for Best Practices and Council of Chief State School Officers. All rights reserved.",
    "manifest":"http://asn.jesandco.org/resources/D10003FC/manifest.json",
    "identifier":"http://purl.org/ASN/resources/D10003FC"
  }
]
```

###[Get a Specific Standard](#specific)
Get a specific publically available standard based on the id

#### METHOD
GET

#### URL
`/api/v1/standards/{id}` 

#### RETURN CODE
200 if the request was successful

#### RETURNS
A Standards Objects
```json
{
  "id":2,
  "title":"Common Core State Standards for English Language Arts & Literacy in History/Social Studies, Science, and Technical Subjects",
  "description":"The Common Core State Standards for English Language Arts & Literacy in History/Social Studies, Science, and Technical Subjects (\"the Standards\") are the culmination of an extended, broad-based effort to fulfill the charge issued by the states to create the next generation of K—12 standards in order to help ensure that all students are college and career ready in literacy no later than the end of high school.",
  "publicationStatus":"published",
  "subject":"english",
  "language":"English",
  "source":"http://www.corestandards.org/assets/CCSSI_ELA%20Standards.pdf",
  "dateValid":1262322000000,
  "repositoryDate":1299560400000,
  "rights":"© Copyright 2010. National Governors Association Center for Best Practices and Council of Chief State School Officers. All rights reserved.",
  "manifest":"http://asn.jesandco.org/resources/D10003FC/manifest.json",
  "identifier":"http://purl.org/ASN/resources/D10003FC"
}
```

###[Create a Standard](#create-a-standard)
Create a publicially available standard

#### METHOD
`POST`

#### URL
`/api/v1/standards`

#### BODY
Send a *Standards* JSON object along as the body. The id should be empty since it will be auto generated.

```json
{
  "id": null,
  "title":"The title",
  "description":"The Description",
  "publicationStatus":"published", //leave emtpy if not published
  "subject":"English",
  "language":"English",
  "source":"http://www.my.standards/standard.pdf",
  "dateValid":1262322000000,
  "repositoryDate":1299560400000,
  "rights":"© Copyright 2010. Creative Commons",
  "manifest":"",
  "identifier":""
}
```
#### RETURNS
The object that was created with the id that was generated

```json
{
  "id": 29809,
  "title":"The title",
  "description":"The Description",
  "publicationStatus":"published", //leave emtpy if not published
  "subject":"English",
  "language":"English",
  "source":"http://www.my.standards/standard.pdf",
  "dateValid":1262322000000,
  "repositoryDate":1299560400000,
  "rights":"© Copyright 2010. Creative Commons",
  "manifest":"",
  "identifier":""
}
```

###[Update a Standard](#update-a-standard)
Update an already created *Standard* 

#### METHOD
`PUT`

#### URL
The {id} should be replaced with an actual id of the standard you are updating
`/api/v1/standards/{id}

#### BODY
Send the whole *Standards* Json object that you want to update with the updated fields as the Body

```json
{
  "id": 29809,
  "title":"The NEW title",
  "description":"The NEW Description",
  "publicationStatus":null, //leave emtpy if not published
  "subject":"English",
  "language":"English",
  "source":"http://www.my.standards/standard.pdf",
  "dateValid":1262322000000,
  "repositoryDate":1299560400000,
  "rights":"© Copyright 2010. Creative Commons",
  "manifest":"",
  "identifier":""
}
```
#### RETURNS
The updated Standards Object
```json
{
  "id": 29809,
  "title":"The NEW title",
  "description":"The NEW Description",
  "publicationStatus":null, //leave emtpy if not published
  "subject":"English",
  "language":"English",
  "source":"http://www.my.standards/standard.pdf",
  "dateValid":1262322000000,
  "repositoryDate":1299560400000,
  "rights":"© Copyright 2010. Creative Commons",
  "manifest":"",
  "identifier":""
}

```

###[Delete a Standard](#delete-a-standard)

#### METHOD
`DELETE`

#### URL
Replace {id} with the actual id of the standard you wish to delete
`/api/v1/standards/{id}`

#### RETURN CODE
`200 OK`


###[Get all Statements in a Standard](#)

#### METHOD
`GET`

#### URL
Replace {standardid} with the id of the standard that the standards belong too
`/api/v1/standards/{standardid}/statements`

#### RETURNS
A list of objects that include the statement and education levels 

```json

```

###[Create a Statement](#)

#### METHOD
`POST`

#### URL
Replace {standardid} with the id of the standard that this statement belongs too
`/api/v1/standards/{standardid}/statements`

#### BODY

#### RETURNS

###[Update a Statement](#)

#### METHOD
`PUT`

#### URL
 Replace {standardid} with the id of the standard that this statement belongs too 
`/api/v1/standards/{standardid}/statements/statementid`

#### BODY

#### RETURNS


###[Delete a Statement](#)

#### METHOD
`DELETE`

#### URL

#### RETURN CODE
`200 OK`

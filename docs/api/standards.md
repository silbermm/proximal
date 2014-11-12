[<< Back to API Docs](API.md)

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

```
[
  {"id":1,
   "title":"Common Core State Standards for Mathematics",
   "description":"These Standards define what students should understand and be able to do in their study of mathematics. Asking a student to understand something means asking a teacher to assess whether the student has understood it. But what does mathematical understanding look like? One hallmark of mathematical understanding is the ability to justify, in a way appropriate to the student's mathematical maturity, why a particular mathematical statement is true or where a mathematical rule comes from. There is a world of difference between a student who can summon a mnemonic device to expand a product such as (a + b)(x + y) and a student who can explain where the mnemonic comes from. The student who can explain the rule understands the mathematics, and may have a better chance to succeed at a less familiar task such as expanding (a + b + c)(x + y). Mathematical understanding and procedural skill are equally important, and both are assessable using mathematical tasks of sufficient richness.",
   "publicationStatus":"published",
   "subject":"math",
   "language":"English",
   "source":"http://www.corestandards.org/assets/CCSSI_Math%20Standards.pdf",
   "dateValid":1262322000000,
   "repositoryDate":1299560400000,
   "rights":"© Copyright 2010. National Governors Association Center for Best Practices and Council of Chief State School Officers. All rights reserved.",
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
```
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



# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "Person" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"firstName" VARCHAR NOT NULL,"lastName" VARCHAR,"birthDate" TIMESTAMP,"educationLevelId" BIGINT,"uid" BIGINT);
create unique index "idx_uid" on "Person" ("uid");
create table "Relationship" ("personId" BIGINT NOT NULL,"otherId" BIGINT NOT NULL,"typeId" BIGINT NOT NULL);
create table "RelationshipType" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"type" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create table "SecureUser" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"providerId" VARCHAR NOT NULL,"userId" VARCHAR NOT NULL,"firstName" VARCHAR,"lastName" VARCHAR,"fullName" VARCHAR,"email" VARCHAR,"avatarUrl" VARCHAR,"authMethod" VARCHAR NOT NULL,"token" VARCHAR,"secret" VARCHAR,"accessToken" VARCHAR,"tokenType" VARCHAR,"expiresIn" INTEGER,"refreshToken" VARCHAR,"hasher" VARCHAR,"password" VARCHAR,"salt" VARCHAR);
create table "Token" ("uuid" VARCHAR NOT NULL,"email" VARCHAR NOT NULL,"creationTime" TIMESTAMP NOT NULL,"expirationTime" TIMESTAMP NOT NULL,"isSignUp" BOOLEAN NOT NULL);
create table "attendence" ("personId" BIGINT NOT NULL,"schoolId" BIGINT NOT NULL,"startDate" TIMESTAMP,"endDate" TIMESTAMP,"grade" BIGINT);
create table "dummies" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create table "education_levels" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"value" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create unique index "idx_value" on "education_levels" ("value");
create table "person_roles" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"person_id" BIGINT NOT NULL,"role_id" BIGINT NOT NULL);
create table "questions" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"text" Text NOT NULL,"picture" BLOB,"type_id" BIGINT,"answer" Text);
create table "questions_with_statements" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"question_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);
create table "roles" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create table "school" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"streetNumber" VARCHAR NOT NULL,"street" VARCHAR NOT NULL,"city" VARCHAR NOT NULL,"state" VARCHAR,"district" VARCHAR,"public" INTEGER);
create table "scores" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"studentId" BIGINT NOT NULL,"questionId" BIGINT NOT NULL,"compentency" INTEGER NOT NULL,"timestamp" BIGINT DEFAULT 1420996644143 NOT NULL);
create table "standard_levels" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"standard_id" BIGINT NOT NULL);
create table "standards" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"organization_id" BIGINT,"title" VARCHAR NOT NULL,"description" Text NOT NULL,"publication_status" VARCHAR NOT NULL,"subject" VARCHAR NOT NULL,"language" VARCHAR,"source" VARCHAR,"date_valid" TIMESTAMP,"repository_date" TIMESTAMP,"rights" VARCHAR,"manifest" VARCHAR,"identifier" VARCHAR);
create table "statement_levels" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);
create table "statements" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"standardId" BIGINT,"asn_uri" VARCHAR,"subject" VARCHAR,"notation" VARCHAR,"alternate_notation" VARCHAR,"label" VARCHAR,"description" Text,"alternate_description" Text,"exactMatch" VARCHAR,"identifier" VARCHAR,"language" VARCHAR);
alter table "Person" add constraint "educationLevel" foreign key("educationLevelId") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "person_roles" add constraint "person_id" foreign key("person_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "person_roles" add constraint "role_id" foreign key("role_id") references "roles"("id") on update NO ACTION on delete NO ACTION;
alter table "questions_with_statements" add constraint "question_fk" foreign key("question_id") references "questions"("id") on update NO ACTION on delete NO ACTION;
alter table "questions_with_statements" add constraint "statement_fk" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;
alter table "scores" add constraint "question" foreign key("questionId") references "questions"("id") on update NO ACTION on delete NO ACTION;
alter table "scores" add constraint "student" foreign key("studentId") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "standard_levels" add constraint "education_level_standard" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "standard_levels" add constraint "standard" foreign key("standard_id") references "standards"("id") on update NO ACTION on delete NO ACTION;
alter table "statement_levels" add constraint "education_level_statement" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "statement_levels" add constraint "statement" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;
alter table "statements" add constraint "STANDARD_FK" foreign key("standardId") references "standards"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "statements" drop constraint "STANDARD_FK";
alter table "statement_levels" drop constraint "education_level_statement";
alter table "statement_levels" drop constraint "statement";
alter table "standard_levels" drop constraint "education_level_standard";
alter table "standard_levels" drop constraint "standard";
alter table "scores" drop constraint "question";
alter table "scores" drop constraint "student";
alter table "questions_with_statements" drop constraint "question_fk";
alter table "questions_with_statements" drop constraint "statement_fk";
alter table "person_roles" drop constraint "person_id";
alter table "person_roles" drop constraint "role_id";
alter table "Person" drop constraint "educationLevel";
drop table "statements";
drop table "statement_levels";
drop table "standards";
drop table "standard_levels";
drop table "scores";
drop table "school";
drop table "roles";
drop table "questions_with_statements";
drop table "questions";
drop table "person_roles";
drop table "education_levels";
drop table "dummies";
drop table "attendence";
drop table "Token";
drop table "SecureUser";
drop table "RelationshipType";
drop table "Relationship";
drop table "Person";


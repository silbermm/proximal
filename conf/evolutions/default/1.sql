# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "Person" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"firstName" VARCHAR(254) NOT NULL,"lastName" VARCHAR(254),"birthDate" TIMESTAMP,"educationLevelId" BIGINT,"uid" BIGINT);
create unique index "idx_uid" on "Person" ("uid");
create table "Relationship" ("personId" BIGINT NOT NULL,"otherId" BIGINT NOT NULL,"typeId" BIGINT NOT NULL);
create table "RelationshipType" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"type" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);
create table "SecureUser" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"providerId" VARCHAR(254) NOT NULL,"userId" VARCHAR(254) NOT NULL,"firstName" VARCHAR(254),"lastName" VARCHAR(254),"fullName" VARCHAR(254),"email" VARCHAR(254),"avatarUrl" VARCHAR(254),"authMethod" VARCHAR(254) NOT NULL,"token" VARCHAR(254),"secret" VARCHAR(254),"accessToken" VARCHAR(254),"tokenType" VARCHAR(254),"expiresIn" INTEGER,"refreshToken" VARCHAR(254),"hasher" VARCHAR(254),"password" VARCHAR(254),"salt" VARCHAR(254));
create table "Token" ("uuid" VARCHAR(254) NOT NULL,"email" VARCHAR(254) NOT NULL,"creationTime" TIMESTAMP NOT NULL,"expirationTime" TIMESTAMP NOT NULL,"isSignUp" BOOLEAN NOT NULL);
create table "attendence" ("personId" BIGINT NOT NULL,"schoolId" BIGINT NOT NULL,"startDate" TIMESTAMP,"endDate" TIMESTAMP,"grade" BIGINT);
create table "dummies" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);
create table "education_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"value" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);
create unique index "idx_value" on "education_levels" ("value");
create table "person_roles" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"person_id" BIGINT NOT NULL,"role_id" BIGINT NOT NULL);
create table "questions" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"text" Text NOT NULL,"picture" BYTEA,"type_id" BIGINT);
create table "questions_with_statements" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"question_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);
create table "roles" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);
create table "school" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"streetNumber" VARCHAR(254) NOT NULL,"street" VARCHAR(254) NOT NULL,"city" VARCHAR(254) NOT NULL,"state" VARCHAR(254),"district" VARCHAR(254),"public" INTEGER);
create table "standard_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"standard_id" BIGINT NOT NULL);
create table "standards" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"organization_id" BIGINT,"title" VARCHAR(254) NOT NULL,"description" Text NOT NULL,"publication_status" VARCHAR(254) NOT NULL,"subject" VARCHAR(254) NOT NULL,"language" VARCHAR(254),"source" VARCHAR(254),"date_valid" TIMESTAMP,"repository_date" TIMESTAMP,"rights" VARCHAR(254),"manifest" VARCHAR(254),"identifier" VARCHAR(254));
create table "statement_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);
create table "statements" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"standardId" BIGINT,"asn_uri" VARCHAR(254),"subject" VARCHAR(254),"notation" VARCHAR(254),"alternate_notation" VARCHAR(254),"label" VARCHAR(254),"description" Text,"alternate_description" Text,"exactMatch" VARCHAR(254),"identifier" VARCHAR(254),"language" VARCHAR(254));
alter table "Person" add constraint "educationLevel" foreign key("educationLevelId") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "person_roles" add constraint "person_id" foreign key("person_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "person_roles" add constraint "role_id" foreign key("role_id") references "roles"("id") on update NO ACTION on delete NO ACTION;
alter table "questions_with_statements" add constraint "question_fk" foreign key("question_id") references "questions"("id") on update NO ACTION on delete NO ACTION;
alter table "questions_with_statements" add constraint "statement_fk" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;
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
alter table "questions_with_statements" drop constraint "question_fk";
alter table "questions_with_statements" drop constraint "statement_fk";
alter table "person_roles" drop constraint "person_id";
alter table "person_roles" drop constraint "role_id";
alter table "Person" drop constraint "educationLevel";
drop table "statements";
drop table "statement_levels";
drop table "standards";
drop table "standard_levels";
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


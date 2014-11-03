# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "Person" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"firstName" VARCHAR(254) NOT NULL,"lastName" VARCHAR(254),"birthDate" TIMESTAMP,"uid" BIGINT);
create unique index "idx_uid" on "Person" ("uid");
create table "Relationship" ("personId" BIGINT NOT NULL,"otherId" BIGINT NOT NULL,"typeId" BIGINT NOT NULL);
create table "RelationshipType" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"type" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);
create table "SecureUser" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"userId" VARCHAR(254) NOT NULL,"providerId" VARCHAR(254) NOT NULL,"firstName" VARCHAR(254),"lastName" VARCHAR(254),"fullName" VARCHAR(254),"email" VARCHAR(254),"avatarUrl" VARCHAR(254),"authMethod" VARCHAR(254) NOT NULL,"token" VARCHAR(254),"secret" VARCHAR(254),"accessToken" VARCHAR(254),"tokenType" VARCHAR(254),"expiresIn" INTEGER,"refreshToken" VARCHAR(254),"hasher" VARCHAR(254),"password" VARCHAR(254),"salt" VARCHAR(254));
create table "Token" ("uuid" VARCHAR(254) NOT NULL,"email" VARCHAR(254) NOT NULL,"creationTime" TIMESTAMP NOT NULL,"expirationTime" TIMESTAMP NOT NULL,"isSignUp" BOOLEAN NOT NULL);
create table "attendence" ("personId" BIGINT NOT NULL,"schoolId" BIGINT NOT NULL,"startDate" TIMESTAMP,"endDate" TIMESTAMP,"grade" BIGINT);
create table "education_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"description" VARCHAR(254) NOT NULL);
create unique index "idx_description" on "education_levels" ("description");
create table "school" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"streetNumber" VARCHAR(254) NOT NULL,"street" VARCHAR(254) NOT NULL,"city" VARCHAR(254) NOT NULL,"state" VARCHAR(254),"district" VARCHAR(254),"public" INTEGER);
create table "standard_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"standard_id" BIGINT NOT NULL);
create table "standards" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"organization_id" BIGINT,"title" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL,"publication_status" VARCHAR(254) NOT NULL,"subject" VARCHAR(254) NOT NULL,"language" VARCHAR(254),"source" VARCHAR(254),"date_valid" TIMESTAMP,"repository_date" TIMESTAMP,"rights" VARCHAR(254),"manifest" VARCHAR(254));
alter table "standard_levels" add constraint "education_level" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "standard_levels" add constraint "standard" foreign key("standard_id") references "standards"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "standard_levels" drop constraint "education_level";
alter table "standard_levels" drop constraint "standard";
drop table "standards";
drop table "standard_levels";
drop table "school";
drop table "education_levels";
drop table "attendence";
drop table "Token";
drop table "SecureUser";
drop table "RelationshipType";
drop table "Relationship";
drop table "Person";


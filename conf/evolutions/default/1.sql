# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "Person" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"firstName" VARCHAR NOT NULL,"lastName" VARCHAR,"birthDate" TIMESTAMP,"educationLevelId" BIGINT,"uid" BIGINT);
create unique index "idx_uid" on "Person" ("uid");
create table "Relationship" ("personId" BIGINT NOT NULL,"otherId" BIGINT NOT NULL,"typeId" BIGINT NOT NULL);
create table "RelationshipType" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"type" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create table "SecureUser" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"providerId" VARCHAR NOT NULL,"userId" VARCHAR NOT NULL,"firstName" VARCHAR,"lastName" VARCHAR,"fullName" VARCHAR,"email" VARCHAR,"avatarUrl" VARCHAR,"authMethod" VARCHAR NOT NULL,"token" VARCHAR,"secret" VARCHAR,"accessToken" VARCHAR,"tokenType" VARCHAR,"expiresIn" INTEGER,"refreshToken" VARCHAR,"hasher" VARCHAR,"password" VARCHAR,"salt" VARCHAR);
create table "Token" ("uuid" VARCHAR NOT NULL,"email" VARCHAR NOT NULL,"creationTime" TIMESTAMP NOT NULL,"expirationTime" TIMESTAMP NOT NULL,"isSignUp" BOOLEAN NOT NULL);
create table "act" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"type" VARCHAR NOT NULL,"action" VARCHAR,"progress" VARCHAR,"resourceId" BIGINT);
create table "activities" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"creator" BIGINT NOT NULL,"date" BIGINT NOT NULL,"description" VARCHAR,"rights" VARCHAR,"source" VARCHAR,"subject" VARCHAR,"title" VARCHAR,"category" VARCHAR,"resource_id" BIGINT);
create table "activity_acts" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"activity_id" BIGINT NOT NULL,"act_id" BIGINT NOT NULL);
create table "activity_sets" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"activity_id" BIGINT NOT NULL,"set_id" BIGINT NOT NULL,"sequence" BIGINT);
create table "activity_statements" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"activityId" BIGINT NOT NULL,"statementId" BIGINT NOT NULL);
create table "assesments" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"studentId" BIGINT NOT NULL,"startDate" BIGINT NOT NULL,"endDate" BIGINT);
create table "assesments_questions_scores" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"assesment_id" BIGINT NOT NULL,"question_score_id" BIGINT NOT NULL);
create table "assessment_history" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"assessment_id" BIGINT NOT NULL,"activity_id" BIGINT NOT NULL,"score_id" BIGINT NOT NULL);
create table "attempts" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"activity_id" BIGINT NOT NULL,"student_id" BIGINT NOT NULL,"timestamp" BIGINT NOT NULL,"score" BIGINT NOT NULL);
create table "attendence" ("personId" BIGINT NOT NULL,"schoolId" BIGINT NOT NULL,"startDate" TIMESTAMP,"endDate" TIMESTAMP,"grade" BIGINT);
create table "dummies" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create table "education_levels" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"value" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create unique index "idx_value" on "education_levels" ("value");
create table "homework" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"activity_id" BIGINT,"student_id" BIGINT,"teacher_id" BIGINT,"status" VARCHAR NOT NULL,"date_given" BIGINT NOT NULL,"date_due" BIGINT);
create table "person_roles" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"person_id" BIGINT NOT NULL,"role_id" BIGINT NOT NULL);
create table "question_uploads" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"question_id" BIGINT NOT NULL,"upload_id" BIGINT NOT NULL);
create table "questions" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"text" Text NOT NULL,"type_id" BIGINT,"answer" Text,"resource_id" BIGINT);
create table "questions_with_statements" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"question_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);
create table "resources" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"title" VARCHAR,"description" Text,"type" VARCHAR,"creator" BIGINT,"created_on" BIGINT);
create table "roles" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR NOT NULL);
create table "school" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"streetNumber" VARCHAR NOT NULL,"street" VARCHAR NOT NULL,"city" VARCHAR NOT NULL,"state" VARCHAR,"district" VARCHAR,"public" INTEGER);
create table "scores" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"studentId" BIGINT NOT NULL,"questionId" BIGINT,"actid" BIGINT,"activity_id" BIGINT,"compentency" BIGINT,"timestamp" BIGINT NOT NULL);
create table "sets" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"title" VARCHAR,"description" VARCHAR);
create table "standard_levels" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"standard_id" BIGINT NOT NULL);
create table "standards" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"title" VARCHAR NOT NULL,"description" Text NOT NULL,"subject" VARCHAR NOT NULL);
create table "statement_levels" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);
create table "statements" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"sequence" BIGINT,"standardId" BIGINT,"subject" VARCHAR,"notation" VARCHAR,"description" Text);
create table "study" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"activity_id" BIGINT NOT NULL,"student_id" BIGINT NOT NULL,"status" VARCHAR,"date_started" BIGINT NOT NULL);
create table "uploads" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"content" Text NOT NULL,"content_type" VARCHAR,"filename" VARCHAR);
alter table "Person" add constraint "educationLevel" foreign key("educationLevelId") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "activities" add constraint "activities_person_fk" foreign key("creator") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "activities" add constraint "activities_resource_fk" foreign key("resource_id") references "resources"("id") on update NO ACTION on delete NO ACTION;
alter table "activity_acts" add constraint "activityact_act_fk" foreign key("act_id") references "act"("id") on update NO ACTION on delete NO ACTION;
alter table "activity_acts" add constraint "activityact_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "activity_sets" add constraint "activityset_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "activity_sets" add constraint "activityset_set_fk" foreign key("set_id") references "sets"("id") on update NO ACTION on delete NO ACTION;
alter table "activity_statements" add constraint "activities_activities_fk" foreign key("activityId") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "activity_statements" add constraint "statements_activities_fk" foreign key("statementId") references "statements"("id") on update NO ACTION on delete NO ACTION;
alter table "assesments" add constraint "assessment_student_fk" foreign key("studentId") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "assesments_questions_scores" add constraint "assesment_question_assesment_fk" foreign key("assesment_id") references "assesments"("id") on update NO ACTION on delete NO ACTION;
alter table "assesments_questions_scores" add constraint "assesment_question_score_fk" foreign key("question_score_id") references "scores"("id") on update NO ACTION on delete NO ACTION;
alter table "assessment_history" add constraint "history_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "assessment_history" add constraint "history_assessment_fk" foreign key("assessment_id") references "assesments"("id") on update NO ACTION on delete NO ACTION;
alter table "assessment_history" add constraint "history_score_fk" foreign key("score_id") references "scores"("id") on update NO ACTION on delete NO ACTION;
alter table "attempts" add constraint "attempts_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "attempts" add constraint "attempts_student_fk" foreign key("student_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "homework" add constraint "homework_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "homework" add constraint "homework_student_fk" foreign key("student_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "homework" add constraint "homework_teacher_fk" foreign key("teacher_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "person_roles" add constraint "person_id" foreign key("person_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "person_roles" add constraint "role_id" foreign key("role_id") references "roles"("id") on update NO ACTION on delete NO ACTION;
alter table "question_uploads" add constraint "questionupload_question_fk" foreign key("question_id") references "questions"("id") on update NO ACTION on delete NO ACTION;
alter table "question_uploads" add constraint "questionupload_upload_fk" foreign key("upload_id") references "uploads"("id") on update NO ACTION on delete NO ACTION;
alter table "questions" add constraint "questions_resource_fk" foreign key("resource_id") references "resources"("id") on update NO ACTION on delete NO ACTION;
alter table "questions_with_statements" add constraint "question_fk" foreign key("question_id") references "questions"("id") on update NO ACTION on delete NO ACTION;
alter table "questions_with_statements" add constraint "statement_fk" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;
alter table "resources" add constraint "resources_person_fk" foreign key("creator") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "scores" add constraint "scores_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;
alter table "scores" add constraint "scores_acts_fk" foreign key("actid") references "act"("id") on update NO ACTION on delete NO ACTION;
alter table "scores" add constraint "scores_question_fk" foreign key("questionId") references "questions"("id") on update NO ACTION on delete NO ACTION;
alter table "scores" add constraint "scores_student_fk" foreign key("studentId") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "standard_levels" add constraint "education_level_standard" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "standard_levels" add constraint "standard" foreign key("standard_id") references "standards"("id") on update NO ACTION on delete NO ACTION;
alter table "statement_levels" add constraint "education_level_statement" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;
alter table "statement_levels" add constraint "statement" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;
alter table "statements" add constraint "STANDARD_FK" foreign key("standardId") references "standards"("id") on update NO ACTION on delete NO ACTION;
alter table "study" add constraint "student_study_activity_fk" foreign key("student_id") references "Person"("id") on update NO ACTION on delete NO ACTION;
alter table "study" add constraint "study_activity_fk" foreign key("activity_id") references "activities"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "study" drop constraint "student_study_activity_fk";
alter table "study" drop constraint "study_activity_fk";
alter table "statements" drop constraint "STANDARD_FK";
alter table "statement_levels" drop constraint "education_level_statement";
alter table "statement_levels" drop constraint "statement";
alter table "standard_levels" drop constraint "education_level_standard";
alter table "standard_levels" drop constraint "standard";
alter table "scores" drop constraint "scores_activity_fk";
alter table "scores" drop constraint "scores_acts_fk";
alter table "scores" drop constraint "scores_question_fk";
alter table "scores" drop constraint "scores_student_fk";
alter table "resources" drop constraint "resources_person_fk";
alter table "questions_with_statements" drop constraint "question_fk";
alter table "questions_with_statements" drop constraint "statement_fk";
alter table "questions" drop constraint "questions_resource_fk";
alter table "question_uploads" drop constraint "questionupload_question_fk";
alter table "question_uploads" drop constraint "questionupload_upload_fk";
alter table "person_roles" drop constraint "person_id";
alter table "person_roles" drop constraint "role_id";
alter table "homework" drop constraint "homework_activity_fk";
alter table "homework" drop constraint "homework_student_fk";
alter table "homework" drop constraint "homework_teacher_fk";
alter table "attempts" drop constraint "attempts_activity_fk";
alter table "attempts" drop constraint "attempts_student_fk";
alter table "assessment_history" drop constraint "history_activity_fk";
alter table "assessment_history" drop constraint "history_assessment_fk";
alter table "assessment_history" drop constraint "history_score_fk";
alter table "assesments_questions_scores" drop constraint "assesment_question_assesment_fk";
alter table "assesments_questions_scores" drop constraint "assesment_question_score_fk";
alter table "assesments" drop constraint "assessment_student_fk";
alter table "activity_statements" drop constraint "activities_activities_fk";
alter table "activity_statements" drop constraint "statements_activities_fk";
alter table "activity_sets" drop constraint "activityset_activity_fk";
alter table "activity_sets" drop constraint "activityset_set_fk";
alter table "activity_acts" drop constraint "activityact_act_fk";
alter table "activity_acts" drop constraint "activityact_activity_fk";
alter table "activities" drop constraint "activities_person_fk";
alter table "activities" drop constraint "activities_resource_fk";
alter table "Person" drop constraint "educationLevel";
drop table "uploads";
drop table "study";
drop table "statements";
drop table "statement_levels";
drop table "standards";
drop table "standard_levels";
drop table "sets";
drop table "scores";
drop table "school";
drop table "roles";
drop table "resources";
drop table "questions_with_statements";
drop table "questions";
drop table "question_uploads";
drop table "person_roles";
drop table "homework";
drop table "education_levels";
drop table "dummies";
drop table "attendence";
drop table "attempts";
drop table "assessment_history";
drop table "assesments_questions_scores";
drop table "assesments";
drop table "activity_statements";
drop table "activity_sets";
drop table "activity_acts";
drop table "activities";
drop table "act";
drop table "Token";
drop table "SecureUser";
drop table "RelationshipType";
drop table "Relationship";
drop table "Person";


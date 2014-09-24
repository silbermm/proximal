# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "COCKTAIL" ("ID" BIGINT NOT NULL,"NAME" VARCHAR NOT NULL);

# --- !Downs

drop table "COCKTAIL";


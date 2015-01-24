--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: Person; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE "Person" (
    id bigint NOT NULL,
    "firstName" character varying(254) NOT NULL,
    "lastName" character varying(254),
    "birthDate" timestamp without time zone,
    uid bigint,
    "educationLevelId" bigint
);


ALTER TABLE "Person" OWNER TO silbermm;

--
-- Name: Person_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE "Person_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "Person_id_seq" OWNER TO silbermm;

--
-- Name: Person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE "Person_id_seq" OWNED BY "Person".id;


--
-- Name: Relationship; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE "Relationship" (
    "personId" bigint NOT NULL,
    "otherId" bigint NOT NULL,
    "typeId" bigint NOT NULL
);


ALTER TABLE "Relationship" OWNER TO silbermm;

--
-- Name: RelationshipType; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE "RelationshipType" (
    id bigint NOT NULL,
    type character varying(254) NOT NULL,
    description character varying(254) NOT NULL
);


ALTER TABLE "RelationshipType" OWNER TO silbermm;

--
-- Name: RelationshipType_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE "RelationshipType_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "RelationshipType_id_seq" OWNER TO silbermm;

--
-- Name: RelationshipType_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE "RelationshipType_id_seq" OWNED BY "RelationshipType".id;


--
-- Name: SecureUser; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE "SecureUser" (
    id bigint NOT NULL,
    "providerId" character varying(254) NOT NULL,
    "userId" character varying(254) NOT NULL,
    "firstName" character varying(254),
    "lastName" character varying(254),
    "fullName" character varying(254),
    email character varying(254),
    "avatarUrl" character varying(254),
    "authMethod" character varying(254) NOT NULL,
    token character varying(254),
    secret character varying(254),
    "accessToken" character varying(254),
    "tokenType" character varying(254),
    "expiresIn" integer,
    "refreshToken" character varying(254),
    hasher character varying(254),
    password character varying(254),
    salt character varying(254)
);


ALTER TABLE "SecureUser" OWNER TO silbermm;

--
-- Name: SecureUser_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE "SecureUser_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "SecureUser_id_seq" OWNER TO silbermm;

--
-- Name: SecureUser_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE "SecureUser_id_seq" OWNED BY "SecureUser".id;


--
-- Name: Token; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE "Token" (
    uuid character varying(254) NOT NULL,
    email character varying(254) NOT NULL,
    "creationTime" timestamp without time zone NOT NULL,
    "expirationTime" timestamp without time zone NOT NULL,
    "isSignUp" boolean NOT NULL
);


ALTER TABLE "Token" OWNER TO silbermm;

--
-- Name: attendence; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE attendence (
    "personId" bigint NOT NULL,
    "schoolId" bigint NOT NULL,
    "startDate" timestamp without time zone,
    "endDate" timestamp without time zone,
    grade bigint
);


ALTER TABLE attendence OWNER TO silbermm;

--
-- Name: dummies; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE dummies (
    id bigint NOT NULL,
    name character varying(254) NOT NULL,
    description character varying(254) NOT NULL
);


ALTER TABLE dummies OWNER TO silbermm;

--
-- Name: dummies_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE dummies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE dummies_id_seq OWNER TO silbermm;

--
-- Name: dummies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE dummies_id_seq OWNED BY dummies.id;


--
-- Name: education_levels; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE education_levels (
    id bigint NOT NULL,
    value character varying(254) NOT NULL,
    description character varying(254) NOT NULL
);


ALTER TABLE education_levels OWNER TO silbermm;

--
-- Name: education_levels_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE education_levels_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE education_levels_id_seq OWNER TO silbermm;

--
-- Name: education_levels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE education_levels_id_seq OWNED BY education_levels.id;


--
-- Name: person_roles; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE person_roles (
    id bigint NOT NULL,
    person_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE person_roles OWNER TO silbermm;

--
-- Name: person_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE person_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE person_roles_id_seq OWNER TO silbermm;

--
-- Name: person_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE person_roles_id_seq OWNED BY person_roles.id;


--
-- Name: play_evolutions; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE play_evolutions (
    id integer NOT NULL,
    hash character varying(255) NOT NULL,
    applied_at timestamp without time zone NOT NULL,
    apply_script text,
    revert_script text,
    state character varying(255),
    last_problem text
);


ALTER TABLE play_evolutions OWNER TO silbermm;

--
-- Name: question_scores; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE question_scores (
    id bigint NOT NULL,
    "studentId" bigint NOT NULL,
    "questionId" bigint NOT NULL,
    "timestamp" bigint NOT NULL
);


ALTER TABLE question_scores OWNER TO silbermm;

--
-- Name: question_scores_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE question_scores_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE question_scores_id_seq OWNER TO silbermm;

--
-- Name: question_scores_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE question_scores_id_seq OWNED BY question_scores.id;


--
-- Name: questions; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE questions (
    id bigint NOT NULL,
    text text NOT NULL,
    picture bytea,
    type_id bigint,
    answer text
);


ALTER TABLE questions OWNER TO silbermm;

--
-- Name: questions_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE questions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE questions_id_seq OWNER TO silbermm;

--
-- Name: questions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE questions_id_seq OWNED BY questions.id;


--
-- Name: questions_with_statements; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE questions_with_statements (
    id bigint NOT NULL,
    question_id bigint NOT NULL,
    statement_id bigint NOT NULL
);


ALTER TABLE questions_with_statements OWNER TO silbermm;

--
-- Name: questions_with_statements_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE questions_with_statements_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE questions_with_statements_id_seq OWNER TO silbermm;

--
-- Name: questions_with_statements_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE questions_with_statements_id_seq OWNED BY questions_with_statements.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE roles (
    id bigint NOT NULL,
    name character varying(254) NOT NULL,
    description character varying(254) NOT NULL
);


ALTER TABLE roles OWNER TO silbermm;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE roles_id_seq OWNER TO silbermm;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


--
-- Name: school; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE school (
    id bigint NOT NULL,
    name character varying(254) NOT NULL,
    "streetNumber" character varying(254) NOT NULL,
    street character varying(254) NOT NULL,
    city character varying(254) NOT NULL,
    state character varying(254),
    district character varying(254),
    public integer
);


ALTER TABLE school OWNER TO silbermm;

--
-- Name: school_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE school_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE school_id_seq OWNER TO silbermm;

--
-- Name: school_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE school_id_seq OWNED BY school.id;


--
-- Name: scores; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE scores (
    id bigint NOT NULL,
    "studentId" bigint NOT NULL,
    "questionId" bigint NOT NULL,
    compentency integer NOT NULL,
    "timestamp" bigint DEFAULT 1419816066564::bigint NOT NULL
);


ALTER TABLE scores OWNER TO silbermm;

--
-- Name: scores_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE scores_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE scores_id_seq OWNER TO silbermm;

--
-- Name: scores_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE scores_id_seq OWNED BY scores.id;


--
-- Name: standard_levels; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE standard_levels (
    id bigint NOT NULL,
    education_level_id bigint NOT NULL,
    standard_id bigint NOT NULL
);


ALTER TABLE standard_levels OWNER TO silbermm;

--
-- Name: standard_levels_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE standard_levels_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE standard_levels_id_seq OWNER TO silbermm;

--
-- Name: standard_levels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE standard_levels_id_seq OWNED BY standard_levels.id;


--
-- Name: standards; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE standards (
    id bigint NOT NULL,
    organization_id bigint,
    title character varying(254) NOT NULL,
    description text NOT NULL,
    publication_status character varying(254) NOT NULL,
    subject character varying(254) NOT NULL,
    language character varying(254),
    source character varying(254),
    date_valid timestamp without time zone,
    repository_date timestamp without time zone,
    rights character varying(254),
    manifest character varying(254),
    identifier character varying(254)
);


ALTER TABLE standards OWNER TO silbermm;

--
-- Name: standards_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE standards_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE standards_id_seq OWNER TO silbermm;

--
-- Name: standards_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE standards_id_seq OWNED BY standards.id;


--
-- Name: statement_levels; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE statement_levels (
    id bigint NOT NULL,
    education_level_id bigint NOT NULL,
    statement_id bigint NOT NULL
);


ALTER TABLE statement_levels OWNER TO silbermm;

--
-- Name: statement_levels_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE statement_levels_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE statement_levels_id_seq OWNER TO silbermm;

--
-- Name: statement_levels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE statement_levels_id_seq OWNED BY statement_levels.id;


--
-- Name: statements; Type: TABLE; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE TABLE statements (
    id bigint NOT NULL,
    "standardId" bigint,
    asn_uri character varying(254),
    subject character varying(254),
    notation character varying(254),
    alternate_notation character varying(254),
    label character varying(254),
    description text,
    alternate_description text,
    "exactMatch" character varying(254),
    identifier character varying(254),
    language character varying(254)
);


ALTER TABLE statements OWNER TO silbermm;

--
-- Name: statements_id_seq; Type: SEQUENCE; Schema: public; Owner: silbermm
--

CREATE SEQUENCE statements_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE statements_id_seq OWNER TO silbermm;

--
-- Name: statements_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: silbermm
--

ALTER SEQUENCE statements_id_seq OWNED BY statements.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY "Person" ALTER COLUMN id SET DEFAULT nextval('"Person_id_seq"'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY "RelationshipType" ALTER COLUMN id SET DEFAULT nextval('"RelationshipType_id_seq"'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY "SecureUser" ALTER COLUMN id SET DEFAULT nextval('"SecureUser_id_seq"'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY dummies ALTER COLUMN id SET DEFAULT nextval('dummies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY education_levels ALTER COLUMN id SET DEFAULT nextval('education_levels_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY person_roles ALTER COLUMN id SET DEFAULT nextval('person_roles_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY question_scores ALTER COLUMN id SET DEFAULT nextval('question_scores_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY questions ALTER COLUMN id SET DEFAULT nextval('questions_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY questions_with_statements ALTER COLUMN id SET DEFAULT nextval('questions_with_statements_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY school ALTER COLUMN id SET DEFAULT nextval('school_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY scores ALTER COLUMN id SET DEFAULT nextval('scores_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY standard_levels ALTER COLUMN id SET DEFAULT nextval('standard_levels_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY standards ALTER COLUMN id SET DEFAULT nextval('standards_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY statement_levels ALTER COLUMN id SET DEFAULT nextval('statement_levels_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY statements ALTER COLUMN id SET DEFAULT nextval('statements_id_seq'::regclass);


--
-- Data for Name: Person; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY "Person" (id, "firstName", "lastName", "birthDate", uid, "educationLevelId") FROM stdin;
69	Miles	Silbernagel	1970-01-14 18:19:55.2	\N	2
70	mlmmomom	omomomomom	1970-01-17 05:02:13.2	\N	2
71	asdfasdfasdfasdfasdf	sadfasdfasdfasdfasdf	1970-01-17 04:59:20.4	\N	2
72	aaaaa	aaaaaa	1970-01-17 05:31:01.2	\N	6
73	Slesie	Silbernagel	1970-01-04 11:52:37.2	\N	1
74	Matt Jr.	Silbernagel	1969-04-27 12:16:37.2	\N	12
75	another	test!	1970-01-04 11:52:37.2	\N	7
76	one	last	1970-01-04 11:23:49.2	\N	2
77	asdfasdfasdfsadf	sadfasdfasdfsafd	1970-01-04 11:52:37.2	\N	2
78	cmon	man	1969-12-06 05:23:49.2	\N	1
68	Matt	Silbernagel	\N	1	\N
\.


--
-- Name: Person_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('"Person_id_seq"', 78, true);


--
-- Data for Name: Relationship; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY "Relationship" ("personId", "otherId", "typeId") FROM stdin;
21	22	1
21	23	1
40	41	1
68	69	1
68	70	1
68	71	1
68	72	1
68	73	1
68	74	1
68	75	1
68	76	1
68	77	1
68	78	1
\.


--
-- Data for Name: RelationshipType; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY "RelationshipType" (id, type, description) FROM stdin;
1	child	A child of a parent that is registered in the system
\.


--
-- Name: RelationshipType_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('"RelationshipType_id_seq"', 1, true);


--
-- Data for Name: SecureUser; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY "SecureUser" (id, "providerId", "userId", "firstName", "lastName", "fullName", email, "avatarUrl", "authMethod", token, secret, "accessToken", "tokenType", "expiresIn", "refreshToken", hasher, password, salt) FROM stdin;
1	google	107327837380848557656	Matt	Silbernagel	Matt Silbernagel	silbermm@gmail.com	https://lh6.googleusercontent.com/-xDa8bFMdNUE/AAAAAAAAAAI/AAAAAAAACPQ/xcD4KE2w6Y8/photo.jpg?sz=50	oauth2	\N	\N	ya29.wwB00Ek3DggU5kB7uuoeviYuLXLoXV06GIxDGSoHLfEHq8S4mkCGUq2w	Bearer	3599	\N	\N	\N	\N
\.


--
-- Name: SecureUser_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('"SecureUser_id_seq"', 1, true);


--
-- Data for Name: Token; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY "Token" (uuid, email, "creationTime", "expirationTime", "isSignUp") FROM stdin;
\.


--
-- Data for Name: attendence; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY attendence ("personId", "schoolId", "startDate", "endDate", grade) FROM stdin;
\.


--
-- Data for Name: dummies; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY dummies (id, name, description) FROM stdin;
1	title	whatever
2	title2	whatever2
\.


--
-- Name: dummies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('dummies_id_seq', 2, true);


--
-- Data for Name: education_levels; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY education_levels (id, value, description) FROM stdin;
1	k	Kindergarden
2	1	1st Grade
3	2	2nd Grade
4	3	3rd Grade
5	4	4th Grade
6	5	5th Grade
7	6	6th Grade
8	7	7th Grade
9	8	8th Grade
10	9	9th Grade
11	10	10th Grade
12	11	11th Grade
13	12	12th Grade
\.


--
-- Name: education_levels_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('education_levels_id_seq', 13, true);


--
-- Data for Name: person_roles; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY person_roles (id, person_id, role_id) FROM stdin;
2	68	1
\.


--
-- Name: person_roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('person_roles_id_seq', 2, true);


--
-- Data for Name: play_evolutions; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY play_evolutions (id, hash, applied_at, apply_script, revert_script, state, last_problem) FROM stdin;
1	0f6ca56d9690a4eeefed0b3337aa4b342b4dacbe	2014-12-28 00:00:00	create table "Person" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"firstName" VARCHAR(254) NOT NULL,"lastName" VARCHAR(254),"birthDate" TIMESTAMP,"educationLevelId" BIGINT,"uid" BIGINT);\ncreate unique index "idx_uid" on "Person" ("uid");\ncreate table "Relationship" ("personId" BIGINT NOT NULL,"otherId" BIGINT NOT NULL,"typeId" BIGINT NOT NULL);\ncreate table "RelationshipType" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"type" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);\ncreate table "SecureUser" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"providerId" VARCHAR(254) NOT NULL,"userId" VARCHAR(254) NOT NULL,"firstName" VARCHAR(254),"lastName" VARCHAR(254),"fullName" VARCHAR(254),"email" VARCHAR(254),"avatarUrl" VARCHAR(254),"authMethod" VARCHAR(254) NOT NULL,"token" VARCHAR(254),"secret" VARCHAR(254),"accessToken" VARCHAR(254),"tokenType" VARCHAR(254),"expiresIn" INTEGER,"refreshToken" VARCHAR(254),"hasher" VARCHAR(254),"password" VARCHAR(254),"salt" VARCHAR(254));\ncreate table "Token" ("uuid" VARCHAR(254) NOT NULL,"email" VARCHAR(254) NOT NULL,"creationTime" TIMESTAMP NOT NULL,"expirationTime" TIMESTAMP NOT NULL,"isSignUp" BOOLEAN NOT NULL);\ncreate table "answers" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"text" Text NOT NULL);\ncreate table "attendence" ("personId" BIGINT NOT NULL,"schoolId" BIGINT NOT NULL,"startDate" TIMESTAMP,"endDate" TIMESTAMP,"grade" BIGINT);\ncreate table "dummies" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);\ncreate table "education_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"value" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);\ncreate unique index "idx_value" on "education_levels" ("value");\ncreate table "person_roles" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"person_id" BIGINT NOT NULL,"role_id" BIGINT NOT NULL);\ncreate table "questions" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"text" Text NOT NULL,"picture" BYTEA,"type_id" BIGINT,"answer_id" BIGINT);\ncreate table "questions_with_statements" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"question_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);\ncreate table "roles" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL);\ncreate table "school" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"streetNumber" VARCHAR(254) NOT NULL,"street" VARCHAR(254) NOT NULL,"city" VARCHAR(254) NOT NULL,"state" VARCHAR(254),"district" VARCHAR(254),"public" INTEGER);\ncreate table "scores" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"studentId" BIGINT NOT NULL,"questionId" BIGINT NOT NULL,"compentency" INTEGER NOT NULL,"timestamp" BIGINT DEFAULT 1419826722281 NOT NULL);\ncreate table "standard_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"standard_id" BIGINT NOT NULL);\ncreate table "standards" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"organization_id" BIGINT,"title" VARCHAR(254) NOT NULL,"description" Text NOT NULL,"publication_status" VARCHAR(254) NOT NULL,"subject" VARCHAR(254) NOT NULL,"language" VARCHAR(254),"source" VARCHAR(254),"date_valid" TIMESTAMP,"repository_date" TIMESTAMP,"rights" VARCHAR(254),"manifest" VARCHAR(254),"identifier" VARCHAR(254));\ncreate table "statement_levels" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"education_level_id" BIGINT NOT NULL,"statement_id" BIGINT NOT NULL);\ncreate table "statements" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"standardId" BIGINT,"asn_uri" VARCHAR(254),"subject" VARCHAR(254),"notation" VARCHAR(254),"alternate_notation" VARCHAR(254),"label" VARCHAR(254),"description" Text,"alternate_description" Text,"exactMatch" VARCHAR(254),"identifier" VARCHAR(254),"language" VARCHAR(254));\nalter table "Person" add constraint "educationLevel" foreign key("educationLevelId") references "education_levels"("id") on update NO ACTION on delete NO ACTION;\nalter table "person_roles" add constraint "person_id" foreign key("person_id") references "Person"("id") on update NO ACTION on delete NO ACTION;\nalter table "person_roles" add constraint "role_id" foreign key("role_id") references "roles"("id") on update NO ACTION on delete NO ACTION;\nalter table "questions" add constraint "answer" foreign key("answer_id") references "answers"("id") on update NO ACTION on delete NO ACTION;\nalter table "questions_with_statements" add constraint "question_fk" foreign key("question_id") references "questions"("id") on update NO ACTION on delete NO ACTION;\nalter table "questions_with_statements" add constraint "statement_fk" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;\nalter table "scores" add constraint "question" foreign key("questionId") references "questions"("id") on update NO ACTION on delete NO ACTION;\nalter table "scores" add constraint "student" foreign key("studentId") references "Person"("id") on update NO ACTION on delete NO ACTION;\nalter table "standard_levels" add constraint "education_level_standard" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;\nalter table "standard_levels" add constraint "standard" foreign key("standard_id") references "standards"("id") on update NO ACTION on delete NO ACTION;\nalter table "statement_levels" add constraint "education_level_statement" foreign key("education_level_id") references "education_levels"("id") on update NO ACTION on delete NO ACTION;\nalter table "statement_levels" add constraint "statement" foreign key("statement_id") references "statements"("id") on update NO ACTION on delete NO ACTION;\nalter table "statements" add constraint "STANDARD_FK" foreign key("standardId") references "standards"("id") on update NO ACTION on delete NO ACTION;	alter table "statements" drop constraint "STANDARD_FK";\nalter table "statement_levels" drop constraint "education_level_statement";\nalter table "statement_levels" drop constraint "statement";\nalter table "standard_levels" drop constraint "education_level_standard";\nalter table "standard_levels" drop constraint "standard";\nalter table "scores" drop constraint "question";\nalter table "scores" drop constraint "student";\nalter table "questions_with_statements" drop constraint "question_fk";\nalter table "questions_with_statements" drop constraint "statement_fk";\nalter table "questions" drop constraint "answer";\nalter table "person_roles" drop constraint "person_id";\nalter table "person_roles" drop constraint "role_id";\nalter table "Person" drop constraint "educationLevel";\ndrop table "statements";\ndrop table "statement_levels";\ndrop table "standards";\ndrop table "standard_levels";\ndrop table "scores";\ndrop table "school";\ndrop table "roles";\ndrop table "questions_with_statements";\ndrop table "questions";\ndrop table "person_roles";\ndrop table "education_levels";\ndrop table "dummies";\ndrop table "attendence";\ndrop table "answers";\ndrop table "Token";\ndrop table "SecureUser";\ndrop table "RelationshipType";\ndrop table "Relationship";\ndrop table "Person";	applied	ERROR: relation "Person" already exists [ERROR:0, SQLSTATE:42P07]
\.


--
-- Data for Name: question_scores; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY question_scores (id, "studentId", "questionId", "timestamp") FROM stdin;
\.


--
-- Name: question_scores_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('question_scores_id_seq', 1, false);


--
-- Data for Name: questions; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY questions (id, text, picture, type_id, answer) FROM stdin;
17	asdfasdfasdfasdfasdfasdfasdf	\N	\N	\N
18	asdfasdfasdfasdfasdfasdfasdfasdfasdfasdf	\N	\N	\N
19	asdfasdfasdfasdfasdfasdfasdfasdfasdf\nasdfasdfasdfasdf\nasdfasdfa\n\n\nasdfasdfasdfasdfasdfasdfasdfa\nasdfasdf	\N	\N	\N
20	sdfasdf	\N	\N	\N
21	sadfasdf	\N	\N	\N
22	fff	\N	\N	\N
23	sdfasdf	\\x89504e470d0a1a0a0000000d494844520000001e0000001e08060000003b30aea20000000473424954080808087c086488000000097048597300000dd700000dd70142289b780000001974455874536f667477617265007777772e696e6b73636170652e6f72679bee3c1a000008c3494441544889ad576b6c53e7197ebe7375ecd489e3c421096d2628e55670d7c2d0b6aa6ba936a13204da960d4dedaac220c5245542b3651b052f4b955042b984d46df2638c1f5d294163289a34b2745a19852e014a0294a28550dbc190c6b7e3cb39b1cfe5db0f62639348fbd3f7d7399fbef77ddefbfb7e84528a697202d808602980027c8d14080494fefefe6b972e5d3ad6d9d9390c00641af815002d866170e7ce9d43341a85aaaa33041886014551108d46914ea76118062c160b8a8b8b6132996605258480e338582c16f8fd7eedd34f3f757b3c9e23241289388b8b8bfb060606b8d1d1d1198c9aa621168b211e8f83520a86615294521f21e4162144a19416534a1f370cc32e08024a4b4b21080208213364b12c0b9fcfa74992b48e8b46a31b43a1d0aca0e1701892248165d938c3301f300cf33ec77137dd6eb7f1a061cdcdcd8b745d5febf7fb375b2c9622bbdd0e4110f22ee9ba0e93c9c4298ab291fbf2cb2f97cab23cc3ca502884542a6588a278505194773b3a3a92c3c3c3df743a9d2f01580c60110061646464726868e892d56afdebce9d3bf76ddebcb91bc0f6743a5d67b7db198bc592673dcff320842c25274f9efc473a9d5e1a0e87b3a0131313d0342d0ca0aebdbdfd5fb89770bb003c0300b76fdfc6ad5bb72049120441c8c6786868e8e2952b57f61c3870e093a6a6a6a7354df33cf2c823a505050559f04422816030788d5355159aa6e5b957d7f538c771eb1c0e4700c0db007e76f3e64de6e38f3f862ccb605976460c198681d96c7ecae9747eb86ddbb6ced5ab57bf7df6ecd91fdfb973e7d4dcb9738b4551bc1f1742c0e5324b92846432099ee77fed703802f5f5f547003cbb7bf76e4355d5580ea389522a104260180628a5c82496d96c6656ae5cd9f0d1471f3d515151f1e2e4e4e42bb158ec439bcd2670dc7db8ec97a6698846a3e079fe8337df7cf314804e00cfbef7de7b505575acbdbdfd995c255b5a5a4c8944c24a087948d775ab6118dfbb7bf76e7d45454581c964c2a38f3efaacdfef6fe8eaeadaef72b98eda6cb62d791eca7cc46231504a2764597e6364646415809f9c387102b989373a3aba04c02e59965bb66cd9d2bc61c3861f2d59b264896118918e8e8e83b22cefd0751dbaaec36ab5c266b335d6d5d57d9b61984e499212b921cd02cbb20c42c8e9fdfbf72bcb972f6fbd7af52a82c1605e1c8787871f03b06d7c7c7c8b2449b5959595bbd7ac59d3ed7038061a1a1a161e3a74e814002fcbb2300c03369b8d1545f1775d5d5d21bfdf7f2437ce1c70af234d27d9e9bd7bf72e6c6a6a7afce2c58b789006070799f2f2725cbd7a35ef3c1e8f9b45517c1ac08db973e7ded534ad5a922488a2089ee79f6c6c6cac1045b1bfb0b0b05e92a4fb16c7e371b02c9bb45aad9f2c5bb66cb5d7eb85a228338059966572ff33f54e290521e4bf3d3d3d7c4d4dcdc2dc3ba2281242c80b53535323b22c67853200904ea701e092dbed4e731cb7cae7f3cd0005804824c2f4f5f5c1ebf5c2e7f321100818c964324408e98c4422e7b76eddda04a03891486479044100c330dfe9eeee5683c1e0e54c37cb66b5aeeb3a004c4e4e8a0505b30fa73973e69c088542a7ec763b0560b8ddee6cb6b85cae9a63c78ed5a9aa8a6951192f81e33833001416164a19d91c80bc66904aa5d8dc24c8a5b56bd77e63c58a153f0804028c2449a4bfbf3f188d46c7c7c6c66e7b3c9edefafafa499bcdf62787c391add9e93a3701405151113263980380692d5800f0f97cca82050b66058e46a3cb00ece6791e369b0d656565282c2cc4850b17e072b9ba3c1e4f9bcbe5ea78f8e1877766ac5655152ccb460060f1e2c52599c4e4329ae8baee6c6969e1262626fe397ffefcefcf063c3030004110f0f9e79fe79d534a61b1587ed9d3d3d3c130ccf98a8a0a8c8f8f673c08c330865a5a5aaccdcdcd4f9e3f7f1ec07472e9ba0e4110acb1586c8520087f9765d9c8d94cfe2f4992048ee3e89d3b7768555555a2baba1a00a0280a745d472a95facfa2458b9e8bc7e3dcd4d4d47d8ba735c6d4d4d4ea83070fb63536365e983f7ffeb7727b2b706fb29c3d7b16b9593b5d4a6059f6cf6eb75b330ce3f9cca40b87c3e038eec25b6fbd75696c6c6c532010c8f265ebb2a4a40494d29fbef6da6bd6542a75201a8d1ad365962555556f44a3d1fd84900394d243b8d7cf0f13425ee679de0da09c6198c6cf3efb0c9224415555504a3b0e1f3e6c9d376fde73636363595959930821a8aaaa7278bdde9d1e8fa7b9aeae6e2fc330bfb1582c4826930080eeeeee2f007c91ab4c6f6f2f0ba0a0a6a6660380dffafdfec22b57ae201c0e8365d9a36d6d6dff06d0a3288a2db705e7f9d26ab5a2acacecc5eddbb79f78e79d770ed7d7d73f515656b666f9f2e5f38686866eac5cb99299f61299e6e56a6a6ab2fc8383833873e60c262727c1f3fc69a7d3f906805f00f8615f5f5f5e7de7011b8681b2b232924ea7bb6b6b6b7f5e5959e99224e957c964726b381c7ee8f2e5cb60180686716fe5ca15944aa5100c06a1691a1504e128c7717f58bf7efd63007e3f3030804cdcb3c00faeb18410545757cf319bcd7f191f1f7fc9e3f1b43634349c0a87c3fb42a1d0e30cc3401004701c074a295455453a9d06cbb26018e606cbb2eed6d6d633005e00b0e7faf5eba6dcd86694e6befaea2ba5b4b4340f5cd775949797173b1c8e938d8d8d7f1445f1ed82828235b22c2f2484ac48a5524fa9aaba801092a0944a2ccb8eeabaded7d6d676bdb7b7b704c0bb00d60f0e0e627878380b0620b3af2be4d5575f6d5fb56ad5cb99faca25966551545404c33026bd5eeffbe974fa6fbb76edbaf6e0bd7dfbf659366ddaf4dd929292e700ac035072fcf8f119eea594c2eff703c051525b5bebacaaaaea2b2a2ae21edc833364329950555585eaea6a88a2785b1084db822084ed76bb02a012c09300f848248273e7ce211008e4c53f031a0c069148243442c83a4229c58e1d3b5e6159b6c56432712693092ccbceaa0021043ccfc36c36c362b14014c5ec93666a6a0ab3753bc330a0695a66be6b1cc7b95b5b5b8f64de4e78fdf5d79da2286ea4942e25847cad8f364288c2b2ecb5743a7d6ccf9e3dc300f03f917548b5a54d49050000000049454e44ae426082	\N	\N
24	asdfasdf	\N	\N	\N
25	with Answer	\N	\N	"B"
11	asdfasdfasdfasdfasdfasdfasdf	\\x89504e470d0a1a0a0000000d494844520000008d0000005a08060000008565d53200000006624b474400ff00ff00ffa0bda793000000097048597300000b1300000b1301009a9c180000000774494d4507dc0709000b16712089f40000200049444154785eecbd779c655775e7fb5d6b9f706fddcad539a9d56ab5a44601094908040a48209b684080c1e3f76c70c2f6387d66b0f1bc79c603b6c179ec8fc133d826d93048186421822c100804126ae5d44add6a758e95eba673f65eef8f7d6e5575ab25e1193fcfd062f5e776ddbaf7841d7e7b85df5afb949899f143f9fec400e90d57881f98802820cf7cde0926f243d0fccbc46c011e862102cf27c00024cf75c0f35dcc0c1121aa99088fde329bff785efb3c3fc0f343d03c87880851191b88c72811310407221802e610512add73c243e787a05924518354f667de680b16002d31ebe2a4c05b171f4a12ad219263926221455010c32a937534788c134513fd1034f3629809261e682364986508865a8b76793f87c6bf4d9e75b1b260a639c1f0e02a242ca7af6f03f5fa19981fc14481c5f0e881e5c4000cfcd0113e4a8285081a030d098647f41045b89327f77c81204fe0ac03014c3c165234f463364a5e3f9715cbae224dcf40491073201e28896bf3c4599f274e4ffe5544d090403044ba049e60dfa1af3039f33534dd85c814588aaa2360880ae2760309d3cd7d0c34eb8c0c9c04f48324311cefd9b91347d1fc10340b628809580b7147986b3e40a7bc8389e9afe1b4839847ca0666353c866180e210902e89cc7064e27e06fa1f25d3f5604b3112841c544e208fe6796d9ea20f238b66d2c23433ed3b99ed7c9b2387b6902513843083498249072c03cbc1b5085222a1860b20da244842b71820cb37b16ac92b19a8bd1c95e5982420829a9e30a8791e6b1a41c430030b20ce08ec66e7beeb29f81e493645b77428099e6e15767b8c365817a34411022912147305e626681777f1e49e43ac5e56b06cf8d5980d57fecd73b5e707479ec7a0318c80a098805997d2ef033b1c436a40c40101bc025a6925039f0129a5019488245026084a2242a93bd87be82b885fc9d2d1cbb0e0c09d3806ea790d1aa12498020e91c0de833b986d8f13922e8a548eac20f3167c817b8994ced19f9b59f5591f65d8cb54713b4be58520cb39510003cf73d040a85204104249ab98a3a00471f82010dcd3ce7af6a90f20820f0e714da65adb999a3dc8407d3927922bfc3c064dd430a060829394d52b4fe2c0a32956e684501253059e803eedeca31295665560ad88460d56b66bd019245bddc0f098f5ccdb0fbe3c4f41d35bf515688080d2ec189d4e8acb73cad03bcc138ec3e8cefb37701468d0003a474dd671f6a63793a6fd287efe3e27829c383df917490f003affab9031da7706037da7538441da3ea163295d9f538484c21c5d73744de99aa313944e90f8328d9fa3cc851add6298e9e90efbf6ed402c039279337822c80f791a8110a22f22d6c1740fb73df1719edcff3dd2bc446863c799f0107481ec95687a4ca0eb0bfa6c092f587739272d792535dd84ba0c931327fffd3c354f10799aea5df5c62ce1e0c42c9e8c6e7074bb1d440dcc61d5f1bd3566a10716c12c20a2042b50811046f1ad75d492756029275ac1c4f3183451168aacc0cb24f73cfa25f636b7d0d2d938d5de01e9c209a1ca396920048f4a5438164a1445dacaf2e1356c3ae902841ca4007144ffe9c490e7296816d6febcaf612092333ab68e6d1377d14e7282084858e0662c32c8aad1070af38013c04825a75eac64fdea8b51378c584aacea3b9102ee13dca7e94dd4f13a2854449c587584e1bdd209b3ec98b88307b6dfc674fb10fb0eed845a09d6a13f1d61a031ca64b19f99b2c59a91d35937ba8e3befbe8dc6e0082f7ac1e5accc4f65fdb2cd24d68784589415fded13053227386800c27121c342106d61c14fa9520b88c7d3a6e5a77872f776a6cb39bc9fe0bc0d2f237723dcf4d8e7f8f6e35fe5ea97fe1ce70c5e48b33c802475fadd1a544a84aa8a6ff1cd4e20d09cf0e64908545553d03325060bc5e054e599002582c72328fdd4dd0067ae3fa9b2301da448c0a78ce56710a6bf43238c92e908593600d285e02189f75bd0724565a67ee8d3fcc088584c3602841033dbf19748c889188a11273ac12c2321967d2a014289484969052ec940baa876e8b60a3a45939202d514137004b00c706082612029270c155cc9096e9e0c425cef46000a9058e6203e8b00d222bee812c20cdecf50962d4268025d8440b01a852424e2103c13618e9d13873865ec5496f76da0b0419ce448f02001c8c062e59e682fb43f715ce11f00d03cbd79b610cf3ced3059fc95085801b4313a18b3789b25d82c45f7099acd83743ae3b48a2394e52c41a6810e120c87e24c8012d3394ae943bd9111684942a903e441a8bb06e9c026f2fa3a06f355045943aa03883430cb91f9707b519436dfbc1f4c10fd1f0e9ac5f14fa83e13c2229fc4e6bf33c4329412e8002d8c16edd62eda9d1d743a0768750fd0ea4ee07d13d3399c3af2ac867339a0204935c916fd13026605e627e9962d8c39cce6c015e44982f82e8851869cb2ac230ce0dc2aea7daba8d73632d07f26b93b89d286300c2706a1861888064cdc0fa4eef93f1c34b0a04d8c989106c42f50ff15a842f088cc203249b3bb8389c9ad4c4c6f0799c139a3d137423d5f4e2d5d46bdb61cc72a44625e286a82c51ac1e801d54281c87465c26669770e33357388b9e60ebae56e4c8e206e1ce79a98f7287d583243698eb2d8c070e362568c5d8c93cd882d41b420e011eaa8fc60a6fefed541d363581733adffb36218620144abe807cc0278418241d2063d42e10f323db78b567b0fed761bb194befa30fd8d416af94924c99a48fb9313fd0d8d0e6aa5c9048bc41dbd08eb18ef23f4fef33108128f3149c9415aad27986d3e48abfd04ddce1e1239842652413da5f460611963433fc292e15721ba9c2035cc6a24a2ff2ae3f4af2dcfd5a6ef0b34cf759163bf7faee38f27c73fa772604d230f278648076c1698a3d519e7f0f80e669b07185b32c450ff6a521d4564086118421f86274800711577d2d322d5a636b3689930c27ca85c85e610ef1bebf8e8154020016f092038da88cd624cd09c7b8243e35f66aeb58d343f043289aa07a9519439993b8fb52b7f89c49d1d1d64fbfeb2df8bc7e6fb1ddbe31df76ce72e86c1735d5f82059b4fa6f55c88eafdfc20b1705111899aa477a8516defb1851b0bf3aa77f1259f4d6cf1f9c7c8fc3e6969e2fd11665b3b69772610df477f6339fd8d956019f81c104c1d2655c84b0b110f56075b303f8bf549afff26811eedbff00a401b4c62b6bb2a0135d30826ebd5e394a81488ee627ce66bec1fbf1e64171a02624ad039bcd5507b09a7aefd05527726667d8856f79e6f4df5fea80f8cb068fc8f2722327f91dea98bc7b4376f518e9e15ebfd6795c97f2ed09899190133e2091677187a75388b556886c65a5a0ba87810450c8228228a58176f820545a9b66ab8e87fa81cddc063a5d729b38011f0a527718b1c440904f3a806a6a6f6d2296718185c42e286495d829010bca0e23029314908287e91be50160f62896a897987930a00c4dd02422f645e38cb00a38c57338dc00b8078440caba8ae4049dc5129a84c53148ff2d49e2fd0e53608d3a042a0839190db256c5cf31e24590d92c671ea2dd0403c76d1c28c7481d2db6d25805a4010a2a28c8b2a28103c2aca7c5edd7c04833a8e26187b8185019e600e8f9098c545fa2cc049cc0c2c60021e870b46b3dd619a0cccc8f178110a126ad6a13f154494c27b6643426942cd3a044d70e618aa2738aa95211607fa59802b22841028024cb5bb884bf0658883e88d3cf1e499a7337384bebe2186b335f8e0c02259174c089ad06c171c9c9c61e7fe163b0f4d32d36c52744b520df4d533468797b07cb49fa52339430335866b29b55080085e254e82284fe33bcd22a80898b5306b012dd484404f33d5101904926ad50e93b97359b7aae4b1dd3b296d0eb52ed800469b4e7894e9d907191e5e8979010d74ad8b6a46d76066bac5fe992687263b1c3c3c4eb7d3a10c8ea49653af65ac1c6db0742861b0de60b89e936b2011018b1b6b6667baf84409288ae1aa051928e741d7d3c3f1b917251d4dc19465b9e0a417601c5fe64748cd030e8fe3139fbb897fbcf5115ae928ea4b0c431556e4737cf03fbe8b35ab96b0e5feadfcee473e4b2b5b869ae1829033cb7bdefd262e3d7723798858affcd7671511982be037ffe093ec38d2a52d759c05b2d0e4577fe212ae7cd939d41b19a973149620a18dd312d3411ed933cb67bf7433b7dffd18bb8e94ccb44bda1e7cb06ac24b9c331251ea89a351730cf6c17f7ef76bb8ea82537150b5d15783786c4423a8393045758e6dbbbe4a33dc1e437b73040df8ee28a7ad7f23b56c33460641c16ae4d926d62e7b1d8feefc042edb0fc1c535244798987d90e1c12b510b9176d41a776cdbc735377c9bfbb63ec5e1f192d9a6108a14ef4b82b631515ceac813a12f738cf4e59c34667cf803bfc468dd909090a8f2d16b6fe6c6dbeea3ad839824387c34d140d42c4a10a1140102898f8019c95a7ce28f7e99b1fab3cf58d2b38566bd5201e5475fff3a3ef4b93b99a08ec3901048c31caf7ccb45ac5bb58424042e3eff0c4e5a7f0a5fb87d175efa51c9482df0be8f7c8e8f7fe8573975a80ec1c059e5c41ebf2151d3c1605d79cdab2ee3d73e742d33cee17c9b17ac6d70c94bce2727e0f0507a9c4be868ceeee9c07fbfe6cb7cf2fadb98b541bcd4d024a1f0015589d1b919680d8fd031e898e3c88ca1536df64db5401242e84613da6bded3aca92104cc1c8494a54b07d8baf3514c0e2124987a7c39cac1f1933869c56968887e530882b084a1fe4ba8e7df63b61c474c51c9f1cc30e777e2dd346539c293fb66f9e38f7d91ebbff918ed740c4dea55f86eb84408ce6192612278496897c294771c68956cdff53873a60cd321c15196c6d56fbc826bbf7e2f4f4eb7e992a39404c97108ce0acc0245502c33bcb4494b454d599635697a65c40c7d16dc2440741a2b7b979867cd48c2c5179dc70ddfd9364f7c8919afb9e22272eb10579ff19357bf8a1b6eff6b44538287d2f5f3c8ee09defb879fe66fdeff2e06059c3d3b680010c1f992f3372d6778b08fe93981728ecb2f7c21c31991680b102ca114e5e607f6f03b7ff1391edf3381a463842068d164c84d71ce591b3863e37a968c0ca2a9a3d5f2ecde37c17d0f3dcae33b76912683781fb590025af90ef1c1443c4d2d8a182681b833c1e12da553289a5a7470cd309d62c7de2dac59fe2328fd20bef22112600943439b99387017ea02a11044fbe888a3a0c647bf782b1ffef4b73834eb48b2b5a4a1c4cf8eb3a41fced9bc968d1b563230384859264c4d4eb26dc71eee7f643b332d83be11cc8ccc39d4120c23718165fdc2e653d6f2e45d0711e7102951f3f8e60cd29964a89133303842219ea9e6119acd2390f69336c0c973cc159040b4d905869340123a64f471eea963dcf0ad8730978124f40f0eb26cf920413b608a4960f9ca0659aa94de539336de6a7819e3f6fbf7f3277ffb4ffca79f7d3df5cabf33acb203c7442ebd06ba9281a11afd430d8ab992543a9c7dfa723a10733eced10ccae76fb98fdffcd03fd09465d41bcbe9cc8cb33469f38e1fbb8c9f7adba5ac1e4d487c896af47990682dda7615f73f71840f7fec8bdcb6e521d2506d4c0956d14055087e8caa3104b3041350e97278fa29bab410af500c605a60c924c1ed63dfc4fdac1a5e8d4889681cd7e81d0c51f8140b1360394e07690c6ce4af3ffd2d7eefefbecb4cdf6a92dcd3e81ce1e411cfcffcc4ab79d32bcf67388b5a2eb62bd201258ef139e19a1b6ee1a3d77f9b66b383743c9a249806020569ea58b17a29dc7b8420821258d63fcdaffdda1b79d9e69359369053af1bde9476171ede79904f7ff166eebff34eb284e368dba325a17279120262829113e8b26468040b4a290eac20a5497f0ea10a3935409f2a59aacc044fd7725c3050cfb4a67ce22bf771eee6b3b8fae51b28c21c8e1a714f7309d5601e2521234d8c3ec948b4c417ca487d982c044cda78e9e34bb73ec97ffad3eb68654bc16acc4cede4caf34ee2fdef7e33e7ac1fc5502c54debf194ea24fa5060d315e72ea2817fdfe4fb1e59eade4a9442d81c6924da30a188e69970904100914ec66d7812db44a4f9aac64b07f25e3534f021e09931c18bf9fe5c32fc5d960e51bb58184391ba759d4503788263334dba3aceadbccdf5df75dcafa182242bf4df0736f3e8f5f7ceb952c1fec032c724854442931144e819575e3d7df76196f79edc57cfef39f234b0b4acd111c620115a1bf51c34207712966cacad161def68a5319111f632d1390129f38969dbe8c4b4eff71b63e7111696883d47836d1c5de744f04c369868a43545115b244482ad5158f97ea38011124f1382d9060a80cd20c7dfcd607ff96bb774e536a0aa1241268c783b0010ea78ec425f1087138e710021d0f773fb18fdffed0c7683148494a126679c5b91bf8e81ffc029bd68e118245ad215699db2a64965ef81a7d27c538efac8d9cb1e9e438195a71378bea6b168bd0a3014bf61e798ca9f661ba9290ea2636ad7e1dadd96594c5309d02f61ed9813149f01e7ca40d8c290e1cda4fe913bc41a7c8304ee7aefb9503ed1c13a1eea7f885b75dc92fffe4552c1faca1169b6356f99aa2182e2e8a9efb15024bfb1cef7cc75ba8a5319e5113b44a85a8083dc240004c08919eaeae5bdd0080809ab179c35afa6a192cc2c2f1e438fe4e455659248642ef157a3570bd332af42388c0f0a0e34567ad46dbb35837a7b40653c900fffe0f3ec5815901514c7a7ba717a97f5bd085661058b8a70f01af306b191ff8f075cce8206dfa4855d9bcb6c1477ee79d8c862e4928f09244f28d057e0661fe7d4fb3f910485c429a248beecf71da34ff1b04a3649a4776ddc9accd32dbc959317a1163f51733d2ff425aed3e3a5232ded9c3b6035b485ca59d2839d2decade434f52f8369da2cbd4ec202b57bf916b6ed8cb6ce843ca36eff891f3f88d77bc8c2599c77c777eca2221d77b1f7faa2aaa8aa8922264016acee1ac877b5be88a5444ddb3f8286a0b3c9602ba307acf7cced1bf2edc30de0c20de58a537ec56ada0f8528ddb3886f294df7fefd59c754a1f35eb60a6344979704f8bf7ffd597e84a4a37049c400871628ea2b585c89d497ca1e02d1646dd7cc72ebebb75826652c32490768ef01b3ff52a96e6464a49e2a2e5efb19e8b5f3d06b7d7fa5ebf54ddfc31c732d1bdcf7aafe0ba4cb4f6b07f6e3b736509612d6b57bd081864edeab3996b2b53dd16733ac9834f7d97ae4c831a25536c79e44666ed10b36587d9d6106b4f7a33dfb8abcd371fd88dcb335eb07e8cf7fecc95f451a021a01ab3ecc7ebcb516db518e164a99b5f205145555fd39bbe857ecf4f69af9fc6fc670a3855546491063abe1c4b4a3c4de438ef164b4f23259dc0b2baf1dbfffe358c366671cea38912b4c6e76eba9f3fb9e65b745d826f77a37aab1ab63039f4dc0782c49789d0053ef417ff48375d8677e0fc04179ebe94575e785a754e8e9154ccf3713a3b3f88d5802e1a54906ad416f7cd8e7e275048976d071f66bc75906e21ac1e3b9f7e5d838594154b4e477419a58dd00e39bb269f62f7c4e3846486c70fdcc393871fa6ed0a663b03d4eb57b077cf8bf8e087bf49d1d7c0f949aebeea852cab411a3ca675cc8e2d0b7d3aa02322e2e40a0124be824492f6a8e3798699eb753bc4574f29082c2ce46790a783e6186d1d24461f41ec6953d21b54541049c85de0e5679dc47bdefd5aacb51b6746e93da131c67fbbf6ebdcf6d00e429ac407226ab4cff32a4b0218280109129f1cef8c2f7d672b7ba63ac482ef047c8777bde995f4038973047520823bae1db66a20e6d71d3d5f4c88fe8f5840ccc7f44930cc7a714a74440d70e2b967fbed34adc08a3136aeba080b3990a0da60747833b37303345b09de8c2777dfc79e897bb879cbf51c0e33cccce6ac18b99cbcfe7a3ef017dfc1b2f5602983499b575d742a5a96f86ac1207acc240b0b8364cc3b3506f30f654208f43c988ac41323087823fa74d5c28c4e7b07a95c00a58c37f6c4be5b2f59f1ccf234d02cd0cb82a9101c940a5e7adf2f0cbed10395d0490433a51e1c575f7e3a3ff7d69790179e245542e638d26df0db7ff439764e7b0a153a4541843bd1b3122318a8065412440d2f2537dfb38d76bd4ea6b32009434323bcf8ac0d8805528dbc42e4f28ef695200ea654c051222fe3082c84b13d4031ffceb0c80d5b3cce24f0c8c1fb786a661b53a5a3ee4e63ac7e5234230299e4ac5d7636edd93a653765b8b681be95c37cead64fb2df0ed329fad9347a0997bde897f87ffff806f6341382421694b563839cb67c0c67354a12c43c6a70bc6dc071662daa8779fcf4f8b5cab79cf717e37171fe04af0e9384664899b3944e70b48363969459948e455700f32005c76ab763e5590bcb85ca96563f0de8857ff17be6bfa7a7db42cc57fde2db5fcdc30f1ee2f66d07e8c830211de2c9c9497ee1773fc1a73ff84e867387595985bb4aa8cc8b68a81ee6e098ed78b6ed3a4ca939ce3c8979d62f1f61a8cf11ac77ff5e4b8e854c6f01c441ec41a2f74d4ffb0471f3dfc40c130b1a508c2e2dfef9a16f30e7049a291b369c455fdac02c66af333fc869cbcfe786ce0dac3b6d33ab473771cdf76e61466718ecd678d39957f3a2f52f65ebde2e53d325922848892f0ace7be139b8ea79388b27ea7890a1d724a84c90e145507c9502120a49887a205e4b17cdcd9ef136bffe87d7924a89f3298aa7a0c0c473d9b96b79d7eb5e4146070dba3097cf204f03cd0224409d823a2c04d4f5f2b055e7aa83b472d090408fc04ba564455fe08f7febffe2277ff3cfd83ed3a4958cd0d631eedd3dc75f7de69ff9b59f7a350312504a62d96374d410030d04a7b40acf642be03583d0c14960f9683f4975ec73d95ea85a6b86101f19e225210019bed23d802f49221943d0a49a1da1a4c3aed9dd3c30be95b6158c16639c71d2854848091ad381624a9e8ef08a57be9527661ee633dffe3c894f593bb482375ff1564eed3f1fa735f6eed9c95cd3b0a104912ee6bb6c3a654d85cd675fd98bbf0d06a585f9452c08c1022a4a62016f1eab82135117a32de050d7f1cf0f1ec27b506aa474f05690228c8d36f128842c3e2a2e797657f7e99a46167eaac4e7d1898253b7f0f5a2c912d5f9bdcd10f911154f4ac9c61575fecbafbd855ffdc37fe080752824a520e76fbf7827cb57afe19daf3a1b6761fe7202880ba091cfe97aa1d9eee25c8e502040a35ec329f391c4b30347e8398b66c24cc773d7134f51b87a041e2065877337ae64388b19603587891282d155cfdd3b1e602acc12baca69eb2f64797d7dcc12201494945997c7261fe7eb77deca83bb1f6660748c57bee86decbe6b1ff9dc467c5f46a270f8c834ae9e11d44848485cc6602daef8aa1cf059fab1009c50999e4347c6796ccf38521b423441cb82a1b4c319272f03e2f83815542586e212906296b4e856e6b04b700a9691859404104b9eab19c03398a779648bc422a1d03353d073832cb69d5e28e8b447db093d06075f70d9b9ebf9776fbc8cff7acdb72119441dccb182bffce48d9cb96e8c8b4f5b49620666f3d73431700e6f90b8589aa30170090117d5b3f5ee155bbc08c6f3ef8ce85863465021a9e57cf11b5bf8ec8ddfa3535b829ac7358ff0993f7f0f2fdfb40c57915c5834536d29b867dbfdb48b36fd3ac6859b2fc1ca8440173161d6cff2e507afe3c6bb6f22ab0df0f20b5ec5daa197f3677f7d2b8f3f7280567894f7bc7d0cb5126f05a61e538190a03de77b911e3936f2e9f5215a8bcac00a78c0abf1bb7ff53f78e890a3c9000d6972c18ac067ffeb7f888a42e84d1022c2c9a38e5f7ac79b18b102b594a9d2b3af39c77df73ec440982407104fd012916376881e23cf081a4550257a99e8d1dac5168e1317350d2238738896949220389c74c983e757df7a31870e4ff1d99b1fa2a88d211893a18fdff99b2ff3b7efff59d6e66d7233c472b03c66a92525490c4b059132e655b28cd9ee2cc1e2c089f41434c4693ebaa302506d961333724adef3cb3fce37eedec1ceee106d4de260b9489695e611ac4a87943c39fd188f1cb98f72b8ceda641da736d6a28967bc3bc3cd8fdec22df7de4c2ec26bce7b3d679ff2521e7a709cdff8cd6b989031647039b7dcb79d9f7fe3852c4995be81113ada2095844c0a42563231374b414ee2ba28e97c04b4183bc72e8684982c5e3d3ac64fbeed75bce7e3b7613ea34bc24c3dc534a6128ce8049bc6053cd6a7bcf9e25359a2719c7af7eabeee7c260f4f445f4e04c421cf51cf725cd00854cc63e500aba0da0b6a8fee864a1c70e77a2b6251b7259644e665c97b7ffa956cddbe97070e7a0a32ba52e791bd53bce70ffe968fff97772165811248c4c89c5222346a3586ea192a1ee7523cca81f1b91865d1334d014ce6238e63fb6af4fc1f03ef69b884f3ced9c49e2d070912eb4fd4aa8cb739a878928e18fff4dd9bd07a9dc159c799e79c45272bb8fe817fe2c63bbeca70df28efb8f46a4e5fbd81ba357036022b87712e23cb324a4dd8be673fdb0eb4195dd7cfd2a535328dd96f0170058f6fdf8b721e6038e9e94b8beae4a88e1cdd37052c182fd8b09a7aa278834c12541cf301bb0aea227bac16700aa1f45812400c9398ae494360e9e820660113a1f727899e4d9e51078980738a3a87aac3e962fea0071f89dfbbe8701d2b8158265aa7cbf2ccf8835f7f1b636e1a95029394321de1de27a7f8f3ff710b1d97628053c149205525cf6bac1ba993aaa092e0827068a2c3fec998458ea58c1e13c38e494ff444a0e232aa3e01ab962fc549ac5053a4caa554ec84185eba4c360f3228393f72d6e5bcf5fcd76373c627aefb0c93334ddef563efe4ff79fb6ff3d2d517b3c496d10803645e397ddd102fd8b0022ddb7853667cce17bffd308872c6c92b18cbbb24044c33b2bcc1bd5bb7d3868a2f0938f331e7f4ec73362f63030dfad3845414273aaf8590b89817520ebdf939fac22184ca618edf7d9fb77d26d0f438937833e75c34190b5fcf4f848ae05cd5a0e3dd55145370946c5e35c09ffec71f67c026a84917e7eab4dc289fbaf11ebefec041da6942a109599ae244a8a972fe692ba9a5114c9913a65a9edb1eda414794a2c75d84a733a1c78a99a1120b371b798a248a26099a25144100a55dced196390a69e102ac4956d37ea44b5fa79f2b2f78153ffbda9fe5c75ffc76ce1bbc883e3f08a460354c534212d99f9f79cb1524c5148953c807f9eaad0f70b0e318c9e0fc4d2b71ea9124c55cc2c1163cbc7b82205915eac7fe3c5b577acebf8a504f943c0175497486135769df4a3389e29224ce8ff4a2d385abf722df1e5c84defb053feb78a2c77eddf3572218dcbc99d2f962ef455d122ad3e44892a75bbae84d185e6b7425c38582cb4e1fe35dafbb80519d43cb024e22e3b40000200049444154dc00b332c4fb3f7c2df7ed9c86da103e28994b701678cde5e75274a75117489d27a9f5f3c9eb6f623c40210b8f357baeb01520bae8814485e034da7ca778310ecd4c323533c3133b7772fbd6c7b9f1ee6df881b3718d9772c1c62b58ea9632a2756a486453b1a8e1d4008b7e172597be70151b960f9250629a7260b2cdb76e7f980cb8fa475f865060ce208159ede7e6db1fa02bb1ccf6d8356c8bfeeffd988f16451009b89448e2258ea03d6a36ba173deba0a2c7580ae6afb1c0ff2ed09c98d04befc0220855bf6b0f57bd9f5e2211668e589cac09b102ade7d32cdcc220961668024ee340521169b6003143117124023981775ffd322e7fc17a32dfa69b283ec938d275fce1c7bfc46437e01387b9804ae0a4e13a6fbce2c59878da04488dc70fcdf2e5db1ea11d0cacc408913cb792f8f7957cf5b388efad02558ce65155cc2996a44892d0ee76a9d7ea0c364658bfea541a23a7f0679fbb8b0ffcc3ed7cf9b66d345c466e499c52f59469091a9f2a21d6639c2135230b9e2b5f7a3689b5912c451bfd7cf93bf730531a176c5ac5a92b8710f58434c727fd7cf1b607d9df8efb1d2c40694689c7cc232154947b20d6211518253d76383aac0e9f2478e708aa0b1050c112872409e2921879ca0200aa801535412c20e28975d286a7c01328896905a9ca4e7a5a4a215e2866978d52e387a533ba99c3a71921c963e901ccab22ab9a1e5c42a94aa98ac71009f3da0a8b5b5c52333220d514d13e06157ee7ddafe79c0da3789d85ccd14907b8675787ed078f106a3945e2413c098e9f7bcd0b191d4809794699a6ccf5ade46faebf8b7d931d3c420891fa0f02b1eb55588acce7ce90e82c1722749cc6ba5b72cc1c037d198d34a32fcbe84b33aef9f21676359730ddc979e9d96ba9d75cacded4142123938c44d2c86ba8ab9cdb04911467c2c5e7aca73fef52aa51a439f71d6ef3e4c42ccb1a196fbc68237529e96a4229c61353f0818f7d99896e35dd21545a33ee2028310a8402e83dcfd8884b22c2d5457a4215738e500d7da8b44fe91c5e1ddea5f19caa8c402a4de2ab451e9965432d801558f08b945c8949494f2268047a84bb54a5045e6258e6054a5582d36a35cf9f1b41a31251ac71bfd1515b56e67fca822db55827d39fc37ff8a9cb599dcc5202edfa3065da4799f553b81c7ae6ce8c9347fbf877af7801797b9cb66b30e346786c32f02b7ff555ee9d4c68694a6a5dccb41a02c1700412bc08d6cb006b1cecd259659e1493187d04a24ade36d1e22bf7eec0f2015c77821fbbf45c442230ad7232e7e598f7163c99c4a866f96006043aaec6fe167ce7a1dda8839f7ec34b3867cd00a99fc56a293e5fc22df71ee0cf3f7d3313e21075b8a0111a5a12b4a0d45822325f885581270898c6851a8be395ded3764c95a08a57c5576c7e4f39201124221e5f298be848282128a5f6d1b1945dbb7681054cc3226e6cb11135c0e6833e5ade63598e4f13ca2ca1ac506c46b56a23880a20a4095ded7d5fe9c0aa71c793d4a564aee0e5a78cf09eb7bf8a064d0a3c3ec9e826395d97135c4a101011fa29f8b937bc982bce5e4316da4896d1ce87f8debe2e3fffa75fe05b3b2699b6581e915881f32d126b93840e89ef908422969af838a4b941291257a7c6be084ac71c5fbf6f07fbfc1005c2a5e79cc2e94b1b15e61723e4e9128745110b8cd59457bef41cf04dba2e65daf573fded5b992ca1cf4afee857dfc82923099dd065460718cfd7f1f1efece457fee6cb3c38d3615285ae2866461a3c355f908502173a886f22a143424947856e9ae39d8b5503aaf3e02fcdf0aa94ce513847294617a11047214ad01869650104a58de388098fb6db7cfce1fdbcfd7d7fc96307273071f1593e21990f36925e870dab9ec70288a315e2be18730eb340a9d02a002798451559187851ba40b30cd1da569aca9e0130d00b0b0db52e575fb8811d07c7f9c8d71fa2488708ea6886b881cd4b549c89940c50f0bb3fff7af493dfe4c6fb9fa29b0f53a40db6cd76f895bfbc9e8b4f5fc91b2edacc79a7ad65599e9310282556232b4207980b295bf7cc71e7f629429a1288a515e21c9ec00cca97efd945511f4367f6f27fbfe1c78002becf3dd720204a6225afbdec3c3efab57be9268a699d2d4f1de0a6470e73f5994b58db17f8e02fbe81f7fcf71b78726a8a563a4ad72de1ab8f4c72ff1f7e9a4bcfdec86b2e3c8bb3d78eb2a462f6a3ae7094286de0a9234d6e79e8298e481fdd44c842276a1a895ab355f8b8309c821a3be78cdfffd437583f92313232cad2911146eb3956040ecf4cb3fdf011b61e98e0e13d077862c2b3d639ce3cefece85755da5b2b6df0b490270e8de3f11d7b71594e99268841b353323ed164ed921a04c31261ff9136058639c76cbbc34ccbd06c9105eb45348bc65b300882770e099e3e845ff8d1f37970df116e79e430a67d64f50128e263c702d1fba72c59554ff8bd77bd82d1cf7d872f6cd9c6acf4d149eb1ca0c1754f947ce9b13b58967f97cd6b863869f928432323d444189f6cf2e853fbd873e000fbe6603a1b81ac868418a696c14850eed9769807f64ee37594554339eb570e102c706c59d4f1240606717031cfeaa18c53d6ade09e8344f00fade0a35fbc8d4b37bc9a95b9e7a2b50dfee1bd6fe3373f722d5bf6ef65361dc0c200bbdbc35c7be70c5fb8f506560e67ac5a3ac486d52b184a8556b764dfe434db0e4cb07b7c8ed09963b2ef1442969295018d3b7c29110e1e9e44d31cd2b8116e9f0ef08f0f4e530b2d821c80e0c1b7f18990684a191c85f463e93af2f230afbdf83c9638509ba39084404e5eb91e09441b65e610f174cdb3b39bf0bd477731d018a413324a57a3dd99e6b6079fe2ac4b4ec385c00c09d7ddf630dd2423c9a22dfcd2966dacbff4541ad6250982887b1ab969484c80a2a091de5feae04fde7915fff9ef6fe2ab8fced0d2947a929257bc0aa6a8cb480d5638e3fd3f7e31579c73329ffafa9d7c77c721669225f8b49f0e397bf0ec7daa0d3b76616167bc9f389ca624b28c50533297e2a5a00842664d6aea79b45df2a7fff41d9aa4e499d2ec084f8eb759b3323dca8f7b6611204008044998f6d021902419e2724c0ab64e4ff11737dfcb7b5f7b018d6ec1babe948ffcca5bf9d29647f9ecad8f71ffde3934efc7abd2ec5fc213a5f0d43ee3b6a77610f78a2be252707542ad1fab79d02c6ad3b24d6e5da60d5a1dcfa3fbc6915a3f894acca9058fa40d4a6954d59680c4fddf1e89597294103c635987abaf38abda5897e2703176ae26d2bdef7def7b9f198880129f6cf989cfdfc4ee0347e84a8b3ed7a121b30cd65b1cdcf930179eb191d146ce37ef7994ebbe7223c38d8cfe628e659971f0d17b386fe31a960f372277200e3d06353dee699e50aa52157517b8e8cc93193f709823070f9117735cf9e2cd6c1c6d2c682ca26f9161ac1d6970c5456770c1999bb0c9fdf8c3bb49424edd0b8da44e5d6bd45d03cb84907bf21cd2c4534bbad46d86d536c9996b9770f54b4ee5a5a72ee3139ffe3c4f3cb58fa10c86986109d3ec7de80e5efea217d2c8b2a3807f5ca9fcb8600144f9c4355fe0b1271e67c839c6acc598cc31a42df63ffa00f5a2c58b4e3f85443c3981d3d62ee1aa8b3773c6ba554ccf1d61a6351e37e32546a2469aa548364892e6b8c49139255765994db29271ce1a81d79fbd8ab75d7216a72e1be2a31fbb863d070e93298c688ba54c32664d46b5c9b0cc312cb38ce81cc332c7a83619a93e1b9619466486f3d78df1b64b36510f05a82348dc48d84b628a597c5e83192825c18c03cd92494b49d394bc8c79992e9e1c63408d46dd31d5f1845a86b7583dd735c80d1aa14b9e18250e70a4cf31dc0b35c225aa4657726e7b6a9c071ed9ce65e79dc6e6a503d1075a24126276d88ba3d488c2232dcf238726d87bf808e3d373cc744a3a05284aaaca602d67b891b36c7890e5a303ac1ca993275007a46c5124359a1594b57a753b25b54419a880fd5c1288f52c1a0273de28b25a2cecaa0015777c415a78fac4a36ab1804ae25f76c12574108eb48c1dfba6383031c3e4f41cad4e97ae041209f4658ec1468dd1c1062383fdac1e1d62492e64c422d51084561968278e36902a24d5fd7ba3b878348f1ad9ea171760848284b89fca8b56b1db51a0898eb0c6209adea33a82411662d963a10ec3c54751a8115db3de3925a17ae0b25a19f91e6244129da6671e70ab58a6b8193d463f411c0145ad8b4a467c9ac3d1e718c4a78260551809c1ba80e0abf37bbc8610701677908a556d3603557a5a3710cb30d52aa6c7042fd1594e58a0f67be039760743d512029ec402412c0e7670d17f103075f89eaab55833e484f80c9b2aec95109f851334c550bc59155ac73d600e43ad8c734216734ea10404d38a21afce818a77a100148e01fe33595d23417d0912f09213243e0f432bd024003d02d950b0b88dd3594987f87401134781c6c116434de7ef681569169f9702260e23320af1ba474f787523e6ff948dc516c4eef4ea771d3dc2a9d7cdb8587aff47de20e64d626ecb1b249603c43d4018511b199e1041552124205546d82275200eb5401262d9a4af9efd1b75657c3ac47c432a1164013832dfc0d85b4bf012f95b2554a52301c19358ec99c962eec421066286d7842015db0bd579f1f7781b2548064062256246a9b1a44c2da64944c059a4f9620b33622694a3faf14c4bd900d1089e20713ce358c57392c5d759584f0a18993908021a775752add058a3d1030c044b71d26b642fedb5d0e8c58db32adc96a0043c5d8d85cc0e23b132d6ec7a8757a194948410a97f1302825703095819b550bc9645e068d42426012fe0515c101253c40b22f1afa97835bc0a4697828212c8c51029284d51529c38822f118dc553de3b92f95a5ec34b88d9691116d79f384b282de07155853f048d7e4e62710c83044a8a2a2a13d414098a4919974c992208a5c41a5ed158d1eb42e4999cc53c9554a05f4838c6b6c5b48d54231fc74dcc8e037c081c470c62eaa8aa022002aea7a892deea8d97afa6587a84171166f357720befa53ac77aa5053137a5f48eafecdf71e0edad2409194161f7ec4eb6efdf416e35c4c19a656b59595b49cdd7e826d1b317a2df5348c9fd7befa5280ace3de902124fcc8b68343f12125ad2e4e19df733175a744519ae0fb27e6415a3d95212afc424a3a76b81ed3bef61bc3b492bcdc8bc2065c1603ec23927bd10d70d900a139d191edefd10cb8757b366643d09198904f64eee65dfe1439cb1ee34f2244311ba5270db8e2d94c92c584adeae71eeda4df465fd24560713ba04b63c7e27851668306a43391b4637322ca3b15e4c2402cd790e1507d8baf36126a7e6082a6c5a7132a7adda18a902924adb2eca0a567326d55c59f5dec1c28c1f23c7a513a4021ed12c51591ba922263d1a7e0b2b66e15339e6e731ef9ff65678c663e73fa94c96c254678a1bbe7e3d07db53dc7d603bbff7853f66cbc12df8c49395295970a829a5164cdb3e6eb8edeff9d40d7fc7aec9dd74d457b63e4588455a4d097cedc15bb9f19e1be986c3fcf3edd7f1995baea1692d0a8de64ecd6887160f6f7f82f1ee3437ddf515eed87137d3ad16870f1dc17b8fb9a815f35a83ebeff806ffed2b1f6467b99542021ea51d5a6c79e88ee869aa5693a77cfbe16f72ebfddf60a2799027f76ca3e99b20515b1a803afef9ceaf71f793773357ce72ed77aee533775cc36ce2292d477d8a3861d226f9d84d7fc7ad0f7c97352b56b074a0c68103bb8198e72ba22741153b03b268a4e569ffffcbe419ae5501ef59b7b0fcff2715562dd0dfc8c8ebc6cbce389b3449f993fdf770c77ddfe2fc15e7d2670d8c2e8494901807e60e30ba643d79a3c91d5bbfc9aa975c4d08914710041702b98b5b7f970e2ce78a0dafa2bf7f88eb6efa222debd027fdc4ea3461500778cbe53f419b399ed8bf8d53d76ee4159baea446bc1e168f4b48491a8aa470dd37ff9e9fbef2b7189621f24c181aab9154d7539404cfea640943f5d5bce1f4d7919f3e84b40394613e240b14344613962c1fe092cd97d2cc67b8e5dbdf24bc748acc0fe124c31398ea4eb2f3c84eae7cd9659cb6e6146a364066790cc15430296380704c90f06f21fff677a4e79c557e4c2858d2c869842ef54e93a18e72f69ad34934212480742ba7d978e8813bb860e3595c75f1c51c38f220c14f23f3d957433490e3599a286389d02cc779ec9efb79d1c617d0905a550610fd82cc2bda157294652e658964d448897b35a3df2402099ea5ce78f5f96f420be1963baf21e824a9160ca4551ad17abab380c1c3ec6b3ec4b71ef8220fefbc9b4ed622a425480f849ec1cc339877999edbc3fec71fe78a335fccb064382dc1c5bf50b7345fce051bcfe5be2db7f19dbbbf42a7338e508005d424eee2a05708f16f2bff5b40d3537342a086b134cf996a1de49bf77e970bcf7b19979c7539a9cfe2a048425061a2b987e9f107f0ad47983ebc954ce63834be1dd580a9c72450688cb746060259b69f7fbce54f59b92ae70d175c451e1c1ae3507c55758f1a468b81be92beb4a8c2ca98adb2ea9f52b22e4d599b6ee6ea2bdfc1f891077974c7f7a86b60384b7094f3a6c7f08c6429cbfbfbc88124288e94d2145f553e3a4a96e419740e70ed4d7fcec9eb46b8eafcab50df8751a7536d4fee0f0dde7af19b79eda597b3e7f03d7ce2a6f771d7ee1b28d3194a0d149a112c4699ffd6f2bfc53c398aa8562da14fbb0cd73a8cd49773c5856793ced7a954134c42a0cdfefd0fb36ed54a560d6ca6ebfb09eb8d479fba9993966e406d35102a82b04e5f6d863e1be2cc0d17f1f813df20b40f93e46b31098896a8c53fb5630a2919c3499bfea4407b5a06213a92254a499217a432c78ada295c79e19bb9ebfeeb1818d84c23abc5b6a240812330920afdfdebb8e8b437137c861a1412b735f7ae3c90c192a151369cfb0276edb91b5fcee26d1541bb2416302d50a01186b860e5959cb1f43cb6eef926f76dfd27968f0cb162e05216dcd1ff199fe57f4dfecd358d00f18958315c37ebd268c47c502e75041777380844ae44707698c3871fe3fc332e67fdf2f358b76c33ff5f7b671e255575e7f1cfabf7ead5de0bbdd04043374b034d08db10440445408e0b8e5b22319a19249e31478d66c6714bce249ee4cc249ac91c13478f33415033461315610c0a08c8da04018106ba9b6e9aa6bb81debbaabaaa6b79f5debbf3c7abad17c49c6348cc99ef1ff5debdf777effdd5effeeef6bbf7ddfba5f17388f7f7118cb6808825bb126b89c1e790f1a9397c69d41594959451d3b805ddd600720421ac4b33acd3cf2d63a6535150641391b40b591d49aa38045e8f03bb4dc72624c6154c65f2b8591cafd98bac7660934260d3927dae03a7ecc4212730cc001040d7bbb1090d19eb420d1913b7ace0b4b9a91c7b150579f91ca9fd0dc2de805d9690925b3ccce48104180edc3637534a8bc8f7ba5084756a994c026bbbd9e5c7673ae6fef347a63e6b460fc1500705b9e548920bc81abb0b0930d1459040b09382fc52104e2cf3633f3d81569c0e1f5e57318864a329e904c24d08536144ee584cc274743793e32bc0e31889102a9294e2c03286f943cda876171e6709d654534af22880043dfe66727cc5a88a0f3031cc083dc12654bb8b5cef5832f72d08fca1462429814b2d40c28616d3707b8a906d49031b097a02ad381c2a6e5709bae1a7bbb795bcdc91b8eca300194932d18d38916818afc709924128da453412a3b8603c927093b220a7a6c697137f06a5c9d86d92d3fe4fa3406454283be0336170fa2977b6fff0ef59f966e599b68f65f390cdd325f84b1b902f453fd87f983cb265f44709e573c09f4169fe1f5f740c19d35c4c872e976e0dcee7e26ec16562298decbc3f1bdd30619788fb4540baa51149cba224c1397f98a69e303d311daf43a134cfc3e4422fb68b98a23f6ff4c7348e5d0830b9248f42b79af6b738149c6a0f32c2e7a6c89309bb1c104250db1ea4bcd087db2e0ff0073879be17afc74579befb62490010896bd47685314c6bedaad8e7645cdea7c7f94b42a6a5912c857969e77196bcb4936776d5b3afa98b97abea79ecf7d5c4f46197b6fe24d00d8387d61fe478771480feb8c691d65e24201a89b2e2e5dd3cbbe7b4b54fe532a2abdbcfc21777f0d1597fd2c7644d5523a9ed126f7d72860da7ba2e9e401209dde05bafefe3d5c3cdac3bd0c08a5fede2995d0d991d757fe148db6924e040432b3ff8e8348f2e9dce135757a489b4848e923ee846108c26d00c931ca78a43b176cd4734ddfa6e184128aee373da712a32420824492296d009c5755459c6e754304d938866e076d8516c12ba6110d54d3caaf551be2c49e9f1dd99b65eea021a15c53ebc6e17cfad98c1f8d185d82488c413d6579e0882711dafc38ecb2ea707b59a6ed0174ba43b050178543b1e35d352680983b869e27358abe009c320ae0b3c0e6b7922a11bc44d4171611e6b6e9fc395a5b9007474f7b1ab35c0dfe926361b3855199b24a1eb06bdb104398364906240b55bbbc5bf7ffd4c46bb64d6ffa186ef6caf67f5dc325c8a84cd66c36db7e2f56b96ccecb2446f248e4bb5134fe8f89c2a512d81aa28204c82711db75dc1eb5006e4178e2588e806beb45c92e5a1e9f4693a0e45c6e7b0b6564892556103d1048a2c93e7b2331c32c63d21d870a28d821c370fcc9f98f20204aadd6244d775fe754b357b5a83b8140953b2f1ef7f3b87a9851e5eab3ac5d19e18b1789cf3c1285e8f9b97effc0a052e3b479bda79eac35a54454698825f7ef50a8a1c70f76b55fce2aeab9890e3201a8972d79b8779f5ee2b7126e52b01bb4e9de3e92d2751dd4eb6d69ee767b7ce657f5327a7c206d38abc6c3e7e962d67fc44e21aeda118aac3c18b77cca52ccf45edb92e9ef8e004d86cb40422f89c7634dde43f6ebf82abc6e6a6ff7a435b370f6eaa61f3fdd7e2b441474f806fbc5dcd7bf75d4dae2a73beb397c77634f2eb9573d974b29551c5f94cf2d87870c3114e8713ac7a633fff72e36c1c8acc8e9a6676d59c2518d5f079ddacf9da57287065d950d353324ba1416672a10fddb414eff96dd5b8478ce0912bc72349124fadff9865b327b1a2a288a7371dc16f487404233cf7d5f96c3fdac049bf46a03f8a66426f34c13f2d9dce2d534702f0d2ce93acafebc0e3b0134918fcf086592c189b4775733b4f6daec5ae5aa74c3c7fc73cc6e4386868ebe1b1f7ab31b1d11bd1b86dd6041e5d3471a0d293a534866972211c6374ae079f9adcd69035bf9424895fedaea1aa23ca96fb17234b12afec39c1eab70eb1ff81c5d86d50d5e2e7c0c3cb702b70fb7f6de3b7273b78606e29cfedaca3a4309f75b7cf0221d0933beec2718d54afe773aab406fb31b2669002583ca59499871a593c672ab7565ac2482412b4856208c06997d9d3e2e7d03f2ec72d4bdcf3f2765e3f769e27168ee7b1f78e72e5b4097cff9a89ecaf6be6ef379e64f743cb28f1a869410801e38af2884622bc79bc9d55334ba86eede1f8855edeadeb64d58c5134740428f4b971da15faa271c2719da231052c2dcfc7d727b1eef69998a6e07d21680d2538f0f0121404cb7eb1996d8dbdac9c3e92e1200404c3119efcb08e7f983f8902978a5b9509c4325f33c6348dbeb87536a14d40436f94030f2e01608b6972a0ad8f83df598a5d82add567b8f7ad032cf8e71bf9e0937a7e79e81cdbbe7d2da3bd2a1b0e9fe6aed7f672f8d1ebf9f9f61ac69414f0dfb7cc04213084358e5cfddb8f59b9601a0fcd2ba3b9c3cf0d6bf7704d4531734b7c03f84e8f692449c2adc804231ac343b0f6500b4f2f9f6edd3900dc396f32e1508863dd114c215832b514b72201120bc615d01d8e0370cfdc720e369c63c5da3dfccf91d6b49d21bbfb16e99fa13004248ccc982a7b066298826595a5b8654bd3964f2aa6379a00209a30f03aad7a71e5d4b118b1188d7e6b9c94aa399224f0b9546e9a5ccc9afdf580e0d93d8d7cf7aa49bcb0bb0e809fed6be2db0b2625f3ce40378575af145605d34dc115138ad35b5c678dce21a425180e12a0eb3ad56d41be77e31c7e7c5d2530747695edd20c831533cad3ee846972db8c72ec9235185f34a59432afc2c6da76de3dd9c6fd8b2a19edb5260bb7ce2c23df0eef9e6c67d5bc0954d5b572f3babdfce6e8396c3689c6f66e9a421a7bebcff3b5d7f6f2f8d61a72dd0e7634f63018e996c666b3b1b07c04ef9f39cdde96000bc7e50d213684404f0909cbcaad26c71f0250e54c1326106923d6b22f977370f218de3cd8c8b3dbaa39d619e6a7cba7a60819f4f2474100aa2d93af292cde6459e6be79e379f2c363c4a2515adb7b983c6e24734a7c6459f148356babe74d60dd9a2a5ed859833d2797ef2d99c2eb47b6f2c2be7a7247e433b3e8d2b39bac9e0748667311c39b00ec76854515a3d2ee14e545cd1e804bcdb2920850e554af208110c92348ac0d58a9b2b208ac0f004c04d74e2fe760c568de38d8c84fb61ee3444f84bb2b72c871bb78f39e050cc6e0ee69809de6ee79155c3f3e9fef6e38c48ec62ec2f1049d7d11de3c7c86f3218d955f1ec5b31fd512d2ac358f8d471ac9c9f531adc085618a01b31921488f893e6ee9415664ee5d38951f2daea0d9df8f4d92f0a932ff5b7b01804dc75be8b7f65f23490c9849b8551bc73bfa48958804a42cb61262d02c2a5357ef9855c6588fc2e6da362ac78ee4f99b67e2526c2049430aa6746401cbc7faf8d147f53c766d257687935b26e6f36f3bea7878d1e4ace4330274ab0a0d5d4162ba8921520b13d9b08e24b1a2a59ea902266dc21022a33023dc0e76377660006d3d413e690f5bf4c93803d896c0308de4d80876d45d2026d9b975da48ae9b58c4cb55a76809c600d878ac999090b97e62215567bb911585d50b2bf9c1e249347687983aa690310ec1239b4ed09dec6de25a82ae707c80c240564b23844092155e58b980357b6b79f07707889a169795a34670c3f4713cb2740667def998593f7f1fb72c33c2e762ddd7e78361d01c8cd11a8d275333b9108e1388f401121b0f9fe61baf77e1b1cbe47a1cacbd733e12f0f8e2a97c6bfd51d6eeabe5b6e9a58cf628d4f7462877d9f03aed7cd2d2cd9271b9dc73450537bdfa079abac3fcfaced9348712e4291aba6952d3de474b20759520d475f7d3615af76f7e723ec0c2f1c5b4c5740e9cede495030de47a3d6c58bd106fd6b1a7a99af4c39b66d3f8f611968fcf07e0f1eb6670247084bf29f102108fc5e88999d4778558367e04b7cc1cc7b3554ddcb0761f1fdcbb80f6509c73911809c3c42e4b4474c1b1f37e98539ad51d426ba79fb6b0c6d6fa4eee9b3386ec32b9796639af1f3b4fe5339b9832329ff965f9d47404d12b8b09e90675177a81b224e3e0b02bfc74d361de6ee8c6262bbcf2f5f978141b0f2e9d8e85497198000001a5494441543faab1f4c56d781d320ec5cec6d5575396e7e2a5ed47f9e61b3df8541bf91e376b57ce4356ecbcb36a110fbd7388f9cf6d21c765c7a3aabcbd6a1143202e0243d74563579fe80cc586847504fac5d99ef030b184304d73a03bf9ec08f68bb3bd993829ff48342e5afcfde26248a5d7d91711b184f1a974d939fffe609d18f5e3f74444cff836b5758b514faf173b9bfdc3c617428878421fe04e2474eb7d50faa9f0582c2e7afbe343d21a4c9b2d97a89610715d17a1b826868729ce748784314896e9d0a4ff331f1c123fd973460821c4e9ae3ea119663a3c15b53714150d5da1acb8d6b32330a83cb2f2eaea8b88d35d7d69dac1653a64ed29e51cdc2409048861fc07f57743fc068c1f32e1295cea23b4c1e90f74a7da97a161817084fb7ff731d55dfd8cca75611a265d618d6fceafe0c96b3236a8e1904a273bbde1f8003e357cb8b0e1d391d2221a4e9e8391a279e2ed2afa5d79fce74dd386845d2aee707e978a9bc25fe58265f69fef0945381b88a2c832538a7c389306aeec82fa22c2344c8e5ef06322317b4cbe7559db65c25fa5d2c0a56bdc1719976cddffc4f83ff0cf87d21d928ef40000000049454e44ae426082	\N	\N
27	Name my Cat	\N	\N	Nelly II
\.


--
-- Name: questions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('questions_id_seq', 29, true);


--
-- Data for Name: questions_with_statements; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY questions_with_statements (id, question_id, statement_id) FROM stdin;
2	11	7
7	22	3
8	25	6
\.


--
-- Name: questions_with_statements_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('questions_with_statements_id_seq', 9, true);


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY roles (id, name, description) FROM stdin;
1	admin	administrator for the system
\.


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('roles_id_seq', 1, true);


--
-- Data for Name: school; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY school (id, name, "streetNumber", street, city, state, district, public) FROM stdin;
\.


--
-- Name: school_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('school_id_seq', 1, false);


--
-- Data for Name: scores; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY scores (id, "studentId", "questionId", compentency, "timestamp") FROM stdin;
\.


--
-- Name: scores_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('scores_id_seq', 1, false);


--
-- Data for Name: standard_levels; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY standard_levels (id, education_level_id, standard_id) FROM stdin;
1	1	1
2	2	1
3	3	1
4	4	1
5	5	1
6	6	1
7	7	1
8	8	1
9	9	1
10	10	1
11	11	1
12	12	1
13	13	1
14	1	2
15	2	2
16	3	2
17	4	2
18	5	2
19	6	2
20	7	2
21	8	2
22	9	2
23	10	2
24	11	2
25	12	2
26	13	2
\.


--
-- Name: standard_levels_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('standard_levels_id_seq', 26, true);


--
-- Data for Name: standards; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY standards (id, organization_id, title, description, publication_status, subject, language, source, date_valid, repository_date, rights, manifest, identifier) FROM stdin;
1	\N	Common Core State Standards for Mathematics	These Standards define what students should understand and be able to do in their study of mathematics. Asking a student to understand something means asking a teacher to assess whether the student has understood it. But what does mathematical understanding look like? One hallmark of mathematical understanding is the ability to justify, in a way appropriate to the student's mathematical maturity, why a particular mathematical statement is true or where a mathematical rule comes from. There is a world of difference between a student who can summon a mnemonic device to expand a product such as (a + b)(x + y) and a student who can explain where the mnemonic comes from. The student who can explain the rule understands the mathematics, and may have a better chance to succeed at a less familiar task such as expanding (a + b + c)(x + y). Mathematical understanding and procedural skill are equally important, and both are assessable using mathematical tasks of sufficient richness.	published	math	English	http://www.corestandards.org/assets/CCSSI_Math%20Standards.pdf	2010-01-01 00:00:00	2011-03-08 00:00:00	© Copyright 2010. National Governors Association Center for Best Practices and Council of Chief State School Officers. All rights reserved.	http://asn.jesandco.org/resources/D10003FB/manifest.json	http://purl.org/ASN/resources/D10003FB
2	\N	Common Core State Standards for English Language Arts & Literacy in History/Social Studies, Science, and Technical Subjects	The Common Core State Standards for English Language Arts & Literacy in History/Social Studies, Science, and Technical Subjects ("the Standards") are the culmination of an extended, broad-based effort to fulfill the charge issued by the states to create the next generation of K—12 standards in order to help ensure that all students are college and career ready in literacy no later than the end of high school.	published	english	English	http://www.corestandards.org/assets/CCSSI_ELA%20Standards.pdf	2010-01-01 00:00:00	2011-03-08 00:00:00	© Copyright 2010. National Governors Association Center for Best Practices and Council of Chief State School Officers. All rights reserved.	http://asn.jesandco.org/resources/D10003FC/manifest.json	http://purl.org/ASN/resources/D10003FC
\.


--
-- Name: standards_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('standards_id_seq', 2, true);


--
-- Data for Name: statement_levels; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY statement_levels (id, education_level_id, statement_id) FROM stdin;
1	1	1
2	2	1
3	3	1
4	4	1
5	5	1
6	6	1
7	7	1
8	1	2
9	2	2
10	1	3
11	2	3
12	3	3
13	4	3
14	5	3
15	6	3
16	7	3
17	8	3
18	9	3
19	10	3
20	11	3
21	12	3
22	13	3
23	1	4
24	2	4
25	3	4
26	4	4
27	5	4
28	6	4
29	7	4
30	8	4
31	9	4
32	10	4
33	11	4
34	12	4
35	13	4
36	1	5
37	2	5
38	3	5
39	4	5
40	5	5
41	6	5
42	7	5
43	8	5
44	9	5
45	10	5
46	11	5
47	12	5
48	13	5
49	1	6
50	2	6
51	3	6
52	4	6
53	5	6
54	6	6
55	7	6
56	8	6
57	9	6
58	10	6
59	11	6
60	12	6
61	13	6
62	1	7
63	2	7
64	3	7
65	4	7
66	5	7
67	6	7
68	7	7
69	8	7
70	9	7
71	10	7
72	11	7
73	12	7
74	13	7
75	1	8
76	2	8
77	3	8
78	4	8
79	5	8
80	6	8
81	7	8
82	8	8
83	9	8
84	10	8
85	11	8
86	12	8
87	13	8
88	1	9
89	2	9
90	3	9
91	4	9
92	5	9
93	6	9
94	7	9
95	8	9
96	9	9
97	10	9
98	11	9
99	12	9
100	13	9
101	1	10
102	1	11
103	1	12
104	1	13
105	1	14
106	1	15
107	1	16
108	1	17
109	1	18
110	1	19
111	1	20
112	1	21
113	1	22
114	1	23
115	1	24
116	1	25
117	2	26
118	2	27
119	2	28
120	3	29
121	3	30
122	2	31
123	2	32
124	2	33
125	2	34
126	2	35
127	2	36
128	3	37
129	3	38
130	2	39
131	2	40
132	2	41
133	3	42
134	3	43
135	3	44
136	4	45
137	4	46
138	4	47
139	4	48
140	4	49
141	4	50
142	4	51
143	4	52
144	4	53
145	4	54
146	4	55
147	4	56
148	4	57
149	1	58
150	2	58
151	3	58
152	4	58
153	5	58
154	6	58
155	7	58
156	8	58
157	9	58
158	10	58
159	11	58
160	12	58
161	13	58
162	1	59
163	2	59
164	3	59
165	4	59
166	5	59
167	6	59
168	7	59
169	8	59
170	9	59
171	10	59
172	11	59
173	12	59
174	13	59
175	1	60
176	2	60
177	3	60
178	4	60
179	5	60
180	6	60
181	7	60
182	8	60
183	9	60
184	10	60
185	11	60
186	12	60
187	13	60
188	1	61
189	2	61
190	3	61
191	4	61
192	5	61
193	6	61
194	7	61
195	8	61
196	9	61
197	10	61
198	11	61
199	12	61
200	13	61
201	1	62
202	2	62
203	3	62
204	4	62
205	5	62
206	6	62
207	7	62
208	8	62
209	9	62
210	10	62
211	11	62
212	12	62
213	13	62
214	1	63
215	2	63
216	3	63
217	4	63
218	5	63
219	6	63
220	7	63
221	8	63
222	9	63
223	10	63
224	11	63
225	12	63
226	13	63
227	1	64
228	2	64
229	3	64
230	4	64
231	5	64
232	6	64
233	7	64
234	8	64
235	9	64
236	10	64
237	11	64
238	12	64
239	13	64
240	1	65
241	2	65
242	3	65
243	4	65
244	5	65
245	6	65
246	7	65
247	8	65
248	9	65
249	10	65
250	11	65
251	12	65
252	13	65
253	1	66
254	2	66
255	3	66
256	4	66
257	5	66
258	6	66
259	7	66
260	8	66
261	9	66
262	10	66
263	11	66
264	12	66
265	13	66
266	1	67
267	2	67
268	3	67
269	4	67
270	5	67
271	6	67
272	7	67
273	8	67
274	9	67
275	10	67
276	11	67
277	12	67
278	13	67
279	1	68
280	1	69
281	1	70
282	2	71
283	2	72
284	2	73
285	3	74
286	3	75
287	3	76
288	4	77
289	4	78
290	4	79
291	1	80
292	1	81
\.


--
-- Name: statement_levels_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('statement_levels_id_seq', 292, true);


--
-- Data for Name: statements; Type: TABLE DATA; Schema: public; Owner: silbermm
--

COPY statements (id, "standardId", asn_uri, subject, notation, alternate_notation, label, description, alternate_description, "exactMatch", identifier, language) FROM stdin;
1	\N	http://asn.jesandco.org/resources/S2366906	Math	CCSS.Math.Practice.MP1	MP.1	\N	Make sense of problems and persevere in solving them	asf	\N	asd	\N
2	1	http://asn.jesandco.org/resources/S2366907	Math	CCSS.Math.Practice.MP2	MP.2	\N	Reason abstractly and quantitatively.	Mathematically proficient students make sense of quantities and their relationships in problem situations. They bring two complementary abilities to bear on problems involving quantitative relationships: the ability to decontextualize—to abstract a given situation and represent it symbolically and manipulate the representing symbols as if they have a life of their own, without necessarily attending to their referents—and the ability to contextualize, to pause as needed during the manipulation process in order to probe into the referents for the symbols involved. Quantitative reasoning entails habits of creating a coherent representation of the problem at hand; considering the units involved; attending to the meaning of quantities, not just how to compute them; and knowing and flexibly using different properties of operations and objects.	\N	kjh	\N
3	1	http://asn.jesandco.org/resources/S2366906	Math	CCSS.Math.Practice.MP1	MP.1	\N	Make sense of problems and persevere in solving them.	\N	\N	http://asn.jesandco.org/resources/S2366906	\N
4	1	http://asn.jesandco.org/resources/S2366908	math	CCSS.Math.Practice.MP3	MP.3	\N	Construct viable arguments and critique the reasoning of others.	Mathematically proficient students understand and use stated assumptions, definitions, and previously established results in constructing arguments. They make conjectures and build a logical progression of statements to explore the truth of their conjectures. They are able to analyze situations by breaking them into cases, and can recognize and use counterexamples. They justify their conclusions, communicate them to others, and respond to the arguments of others. They reason inductively about data, making plausible arguments that take into account the context from which the data arose. Mathematically proficient students are also able to compare the effectiveness of two plausible arguments, distinguish correct logic or reasoning from that which is flawed, and—if there is a flaw in an argument—explain what it is. Elementary students can construct arguments using concrete referents such as objects, drawings, diagrams, and actions. Such arguments can make sense and be correct, even though they are not generalized or made formal until later grades. Later, students learn to determine domains to which an argument applies. Students at all grades can listen or read the arguments of others, decide whether they make sense, and ask useful questions to clarify or improve the arguments.	\N	http://asn.jesandco.org/resources/S2366908	\N
5	1	http://asn.jesandco.org/resources/S2366909	math	CCSS.Math.Practice.MP4	MP.4	\N	Model with mathematics.	Mathematically proficient students can apply the mathematics they know to solve problems arising in everyday life, society, and the workplace. In early grades, this might be as simple as writing an addition equation to describe a situation. In middle grades, a student might apply proportional reasoning to plan a school event or analyze a problem in the community. By high school, a student might use geometry to solve a design problem or use a function to describe how one quantity of interest depends on another. Mathematically proficient students who can apply what they know are comfortable making assumptions and approximations to simplify a complicated situation, realizing that these may need revision later. They are able to identify important quantities in a practical situation and map their relationships using such tools as diagrams, two-way tables, graphs, flowcharts and formulas. They can analyze those relationships mathematically to draw conclusions. They routinely interpret their mathematical results in the context of the situation and reflect on whether the results make sense, possibly improving the model if it has not served its purpose.	\N	http://asn.jesandco.org/resources/S2366909	\N
6	1	http://asn.jesandco.org/resources/S2366910	math	CCSS.Math.Practice.MP5	MP.5	\N	Use appropriate tools strategically.	Mathematically proficient students consider the available tools when solving a mathematical problem. These tools might include pencil and paper, concrete models, a ruler, a protractor, a calculator, a spreadsheet, a computer algebra system, a statistical package, or dynamic geometry software. Proficient students are sufficiently familiar with tools appropriate for their grade or course to make sound decisions about when each of these tools might be helpful, recognizing both the insight to be gained and their limitations. For example, mathematically proficient high school students analyze graphs of functions and solutions generated using a graphing calculator. They detect possible errors by strategically using estimation and other mathematical knowledge. When making mathematical models, they know that technology can enable them to visualize the results of varying assumptions, explore consequences, and compare predictions with data. Mathematically proficient students at various grade levels are able to identify relevant external mathematical resources, such as digital content located on a website, and use them to pose or solve problems. They are able to use technological tools to explore and deepen their understanding of concepts.	\N	http://asn.jesandco.org/resources/S2366910	\N
7	1	http://asn.jesandco.org/resources/S2366911	math	CCSS.Math.Practice.MP6	MP.6	\N	Attend to precision.	\N	\N	http://asn.jesandco.org/resources/S2366911	\N
8	1	http://asn.jesandco.org/resources/S2366912	math	CCSS.Math.Practice.MP7	MP.7	\N	Look for and make use of structure.	\N	\N	http://asn.jesandco.org/resources/S2366912	\N
9	1	http://asn.jesandco.org/resources/S2366913	math	CCSS.Math.Practice.MP8	MP.8	\N	Look for and express regularity in repeated reasoning.	\N	\N	http://asn.jesandco.org/resources/S2366913	\N
10	1	http://asn.jesandco.org/resources/S114340E	math	CCSS.Math.Content.K.CC.A	\N	\N	Know number names and the count sequence.	\N	\N	http://purl.org/ASN/resources/S114340E	\N
11	1	http://asn.jesandco.org/resources/S1143417	math	CCSS.Math.Content.K.CC.A.1	K.CC.1	\N	Count to 100 by ones and by tens.	\N	\N	http://purl.org/ASN/resources/S1143417	\N
12	1	http://asn.jesandco.org/resources/S1143418	math	CCSS.Math.Content.K.CC.A.2	K.CC.2	\N	Count forward beginning from a given number within the known sequence (instead of having to begin at 1).	\N	\N	http://purl.org/ASN/resources/S1143418	\N
13	1	http://asn.jesandco.org/resources/S1143419	math	CCSS.Math.Content.K.CC.A.3	K.CC.3	\N	Write numbers from 0 to 20. Represent a number of objects with a written numeral 0-20 (with 0 representing a count of no objects).	\N	\N	http://purl.org/ASN/resources/S1143419	\N
14	1	http://asn.jesandco.org/resources/S114340F	math	CCSS.Math.Content.K.CC.B	\N	\N	Count to tell the number of objects	\N	\N	http://purl.org/ASN/resources/S114340F	\N
15	1	http://asn.jesandco.org/resources/S114341A	math	CCSS.Math.Content.K.CC.B.4	K.CC.4	\N	Understand the relationship between numbers and quantities; connect counting to cardinality.	\N	\N	http://purl.org/ASN/resources/S114341A	\N
16	1	http://asn.jesandco.org/resources/S114341B	math	CCSS.Math.Content.K.CC.B.5	K.CC.5	CCSS.Math.Content.K.CC.B.5	Count to answer "how many?" questions about as many as 20 things arranged in a line, a rectangular array, or a circle, or as many as 10 things in a scattered configuration; given a number from 1—20, count out that many objects.	\N	\N	http://purl.org/ASN/resources/S114341B	\N
17	1	http://asn.jesandco.org/resources/S1143410	math	CCSS.Math.Content.K.CC.C		\N	Compare numbers.	\N	\N	http://purl.org/ASN/resources/S1143410	\N
18	1	http://asn.jesandco.org/resources/S114341C	math	CCSS.Math.Content.K.CC.C.6	K.CC.6	\N	Identify whether the number of objects in one group is greater than, less than, or equal to the number of objects in another group, e.g., by using matching and counting strategies.	\N	\N	http://purl.org/ASN/resources/S114341C	\N
19	1	http://asn.jesandco.org/resources/S114341D	math	CCSS.Math.Content.K.CC.C.7	K.CC.7	\N	Compare two numbers between 1 and 10 presented as written numerals.	\N	\N	http://purl.org/ASN/resources/S114341D	\N
20	1	http://asn.jesandco.org/resources/S1143411	math	CCSS.Math.Content.K.OA.A	\N	\N	Understand addition as putting together and adding to, and understand subtraction as taking apart and taking from.	\N	\N	http://purl.org/ASN/resources/S1143411	\N
21	1	\N	math	CCSS.Math.Content.K.OA.A.1	K.OA.1	\N	Represent addition and subtraction with objects, fingers, mental images, drawings, sounds (e.g., claps), acting out situations, verbal explanations, expressions, or equations.	\N	\N	http://asn.jesandco.org/resources/S114341E	\N
22	1	http://asn.jesandco.org/resources/S114341F	math	CCSS.Math.Content.K.OA.A.2	K.OA.2	\N	Solve addition and subtraction word problems, and add and subtract within 10, e.g., by using objects or drawings to represent the problem.	\N	\N	http://purl.org/ASN/resources/S114341F	\N
23	1	http://asn.jesandco.org/resources/S1143420	math	CCSS.Math.Content.K.OA.A.3	K.OA.3	\N	Decompose numbers less than or equal to 10 into pairs in more than one way, e.g., by using objects or drawings, and record each decomposition by a drawing or equation (e.g., 5 = 2 + 3 and 5 = 4 + 1).	\N	\N	http://purl.org/ASN/resources/S1143420	\N
24	1	http://asn.jesandco.org/resources/S1143421	math	CCSS.Math.Content.K.OA.A.4	K.OA.A.4	\N	For any number from 1 to 9, find the number that makes 10 when added to the given number, e.g., by using objects or drawings, and record the answer with a drawing or equation.	\N	\N	http://purl.org/ASN/resources/S1143421	\N
25	1	http://asn.jesandco.org/resources/S1143422	math	CCSS.Math.Content.K.OA.A.5	K.OA.5	\N	Fluently add and subtract within 5	\N	\N	http://purl.org/ASN/resources/S1143422	\N
26	1	http://asn.jesandco.org/resources/S114342D	math	CCSS.Math.Content.1.OA.A	\N	\N	Represent and solve problems involving addition and subtraction	\N	\N	http://purl.org/ASN/resources/S114342D	\N
27	1	http://asn.jesandco.org/resources/S1143438	math	CCSS.Math.Content.1.OA.A.1	1.OA.1	\N	Use addition and subtraction within 20 to solve word problems involving situations of adding to, taking from, putting together, taking apart, and comparing, with unknowns in all positions, e.g., by using objects, drawings, and equations with a symbol for the unknown number to represent the problem.	\N	\N	http://purl.org/ASN/resources/S1143438	\N
28	1	http://asn.jesandco.org/resources/S1143439	math	CCSS.Math.Content.1.OA.A.2	1.OA.2	\N	Solve word problems that call for addition of three whole numbers whose sum is less than or equal to 20, e.g., by using objects, drawings, and equations with a symbol for the unknown number to represent the problem.	\N	\N	http://purl.org/ASN/resources/S1143439	\N
29	1	http://asn.jesandco.org/resources/S2390245	math	CCSS.Math.Content.2.OA.A	Cluster	\N	Represent and solve problems involving addition and subtraction.	\N	\N	\N	\N
30	1	http://asn.jesandco.org/resources/S1143453	math	CCSS.Math.Content.2.OA.A.1	2.OA.1	\N	Use addition and subtraction within 100 to solve one- and two-step word problems involving situations of adding to, taking from, putting together, taking apart, and comparing, with unknowns in all positions, e.g., by using drawings and equations with a symbol for the unknown number to represent the problem		\N	http://purl.org/ASN/resources/S1143453	\N
31	1	http://asn.jesandco.org/resources/S114342E	math	CCSS.Math.Content.1.OA.B	Cluster	\N	Understand and apply properties of operations and the relationship between addition and subtraction	\N	\N	http://purl.org/ASN/resources/S114342E	\N
32	1	http://asn.jesandco.org/resources/S114343A	math	CCSS.Math.Content.1.OA.B.3	1.OA.3	\N	Apply properties of operations as strategies to add and subtract		\N	http://purl.org/ASN/resources/S114343A	\N
33	1	http://asn.jesandco.org/resources/S114343B	math	CCSS.Math.Content.1.OA.B.4	1.OA.4	\N	Understand subtraction as an unknown-addend problem	\N	\N	http://purl.org/ASN/resources/S114343B	\N
34	1	http://asn.jesandco.org/resources/S114342F	math	CCSS.Math.Content.1.OA.C	Cluster	\N	Add and subtract within 20	\N	\N	http://purl.org/ASN/resources/S114342F	\N
35	1	http://asn.jesandco.org/resources/S114343C	math	CCSS.Math.Content.1.OA.C.5	1.OA.5	\N	Relate counting to addition and subtraction (e.g., by counting on 2 to add 2).	\N	\N	http://purl.org/ASN/resources/S114343C	\N
36	1	http://asn.jesandco.org/resources/S114343D	math	CCSS.Math.Content.1.OA.C.6	1.OA.6	\N	Description\nen-US:\tAdd and subtract within 20, demonstrating fluency for addition and subtraction within 10. Use strategies such as counting on; making ten (e.g., 8 + 6 = 8 + 2 + 4 = 10 + 4 = 14); decomposing a number leading to a ten (e.g., 13 - 4 = 13 - 3 - 1 = 10 - 1 = 9); using the relationship between addition and subtraction (e.g., knowing that 8 + 4 = 12, one knows 12 - 8 = 4); and creating equivalent but easier or known sums (e.g., adding 6 + 7 by creating the known equivalent 6 + 6 + 1 = 12 + 1 = 13)	\N	\N	http://purl.org/ASN/resources/S114343D	\N
37	1	http://asn.jesandco.org/resources/S2390246	math	CCSS.Math.Content.2.OA.B	Cluster	\N	Add and subtract within 20.	\N	\N	\N	\N
38	1	http://asn.jesandco.org/resources/S1143454	math	CCSS.Math.Content.2.OA.B.2	2.OA.2	\N	Fluently add and subtract within 20 using mental strategies. By end of Grade 2, know from memory all sums of two one-digit numbers	\N	\N	http://purl.org/ASN/resources/S1143454	\N
39	1	http://asn.jesandco.org/resources/S1143430	math	CCSS.Math.Content.1.OA.D	Cluster	\N	Work with addition and subtraction equations	\N	\N	http://purl.org/ASN/resources/S1143430	\N
40	1	http://asn.jesandco.org/resources/S114343E	math	CCSS.Math.Content.1.OA.D.7	1.OA.7	\N	Understand the meaning of the equal sign, and determine if equations involving addition and subtraction are true or false	\N	\N	http://purl.org/ASN/resources/S114343E	\N
41	1	http://asn.jesandco.org/resources/S114343F	math	CCSS.Math.Content.1.OA.D.8	1.OA.8	\N	Determine the unknown whole number in an addition or subtraction equation relating three whole numbers	\N	\N	http://purl.org/ASN/resources/S114343F	\N
42	1	http://asn.jesandco.org/resources/S114344F	math	CCSS.Math.Content.2.OA.C	Cluster	\N	Work with equal groups of objects to gain foundations for multiplication	\N	\N	http://purl.org/ASN/resources/S114344F	\N
43	1	http://asn.jesandco.org/resources/S1143455	math	CCSS.Math.Content.2.OA.C.3	2.OA.3	\N	Determine whether a group of objects (up to 20) has an odd or even number of members, e.g., by pairing objects or counting them by 2s; write an equation to express an even number as a sum of two equal addends	\N	\N	http://purl.org/ASN/resources/S1143455	\N
44	1	http://asn.jesandco.org/resources/S1143456	math	CCSS.Math.Content.2.OA.C.4	2.OA.4	\N	Use addition to find the total number of objects arranged in rectangular arrays with up to 5 rows and up to 5 columns; write an equation to express the total as a sum of equal addends	\N	\N	http://purl.org/ASN/resources/S1143456	\N
45	1	http://asn.jesandco.org/resources/S114346D	math	CCSS.Math.Content.3.OA.A	Cluster	\N	Represent and solve problems involving multiplication and division		\N	http://purl.org/ASN/resources/S114346D	\N
46	1	http://asn.jesandco.org/resources/S1143477	math	CCSS.Math.Content.3.OA.A.1	3.OA.1	\N	Interpret products of whole numbers, e.g., interpret 5 × 7 as the total number of objects in 5 groups of 7 objects each	\N	\N	http://purl.org/ASN/resources/S1143477	\N
47	1	http://asn.jesandco.org/resources/S1143478	math	CCSS.Math.Content.3.OA.A.2	3.OA.2	\N	Interpret whole-number quotients of whole numbers, e.g., interpret 56 ÷ 8 as the number of objects in each share when 56 objects are partitioned equally into 8 shares, or as a number of shares when 56 objects are partitioned into equal shares of 8 objects each	\N	\N	http://purl.org/ASN/resources/S1143478	\N
48	1	http://asn.jesandco.org/resources/S1143479	math	CCSS.Math.Content.3.OA.A.3	3.OA.3	\N	Use multiplication and division within 100 to solve word problems in situations involving equal groups, arrays, and measurement quantities, e.g., by using drawings and equations with a symbol for the unknown number to represent the problem	\N	\N	http://purl.org/ASN/resources/S1143479	\N
49	1	http://asn.jesandco.org/resources/S114347A	math	CCSS.Math.Content.3.OA.A.4	3.OA.4	\N	Determine the unknown whole number in a multiplication or division equation relating three whole numbers	\N	\N	http://purl.org/ASN/resources/S114347A	\N
50	1	http://asn.jesandco.org/resources/S114346E	math	CCSS.Math.Content.3.OA.B	Cluster	\N	Understand properties of multiplication and the relationship between multiplication and division	\N	\N	http://purl.org/ASN/resources/S114346E	\N
51	1	http://asn.jesandco.org/resources/S114347B	math	CCSS.Math.Content.3.OA.B.5	3.OA.5	\N	Apply properties of operations as strategies to multiply and divide	\N	\N	http://purl.org/ASN/resources/S114347B	\N
52	1	http://asn.jesandco.org/resources/S114347C	math	CCSS.Math.Content.3.OA.B.6	\N	\N	Understand division as an unknown-factor problem	\N	\N	http://purl.org/ASN/resources/S114347C	\N
53	1	http://asn.jesandco.org/resources/S114346F	math	CCSS.Math.Content.3.OA.C	Cluster	\N	Multiply and divide within 100	\N	\N	http://purl.org/ASN/resources/S114346F	\N
54	1	http://asn.jesandco.org/resources/S114347D	math	CCSS.Math.Content.3.OA.C.7	3.OA.7	\N	Fluently multiply and divide within 100, using strategies such as the relationship between multiplication and division (e.g., knowing that 8 × 5 = 40, one knows 40 ÷ 5 = 8) or properties of operations. By the end of Grade 3, know from memory all products of two one-digit numbers	\N	\N	http://purl.org/ASN/resources/S114347D	\N
55	1	http://asn.jesandco.org/resources/S1143470	math	CCSS.Math.Content.3.OA.D	Cluster	\N	Solve problems involving the four operations, and identify and explain patterns in arithmetic	\N	\N	http://purl.org/ASN/resources/S1143470	\N
56	1	http://asn.jesandco.org/resources/S114347E	math	CCSS.Math.Content.3.OA.D.8	3.OA.8	\N	Solve two-step word problems using the four operations. Represent these problems using equations with a letter standing for the unknown quantity. Assess the reasonableness of answers using mental computation and estimation strategies including rounding	\N	\N	http://purl.org/ASN/resources/S114347E	\N
57	1	http://asn.jesandco.org/resources/S114347F	math	CCSS.Math.Content.3.OA.D.9	3.OA.9	\N	Identify arithmetic patterns (including patterns in the addition table or multiplication table), and explain them using properties of operations	\N	\N	http://purl.org/ASN/resources/S114347F	\N
58	2	http://asn.jesandco.org/resources/S114376D	english	CCSS.ELA-Literacy.CCRA.R.1	CCR.R.1	\N	Read closely to determine what the text says explicitly and to make logical inferences from it; cite specific textual evidence when writing or speaking to support conclusions drawn from the text	\N	\N	http://purl.org/ASN/resources/S114376D	\N
59	2	http://asn.jesandco.org/resources/S114376E	english	CCSS.ELA-Literacy.CCRA.R.2	CCR.R.2	\N	Determine central ideas or themes of a text and analyze their development; summarize the key supporting details and ideas	\N	\N	http://purl.org/ASN/resources/S114376E	\N
60	2	http://asn.jesandco.org/resources/S114376F	english	CCSS.ELA-Literacy.CCRA.R.3	CCR.R.3	\N	Analyze how and why individuals, events, and ideas develop and interact over the course of a text	\N	\N	http://purl.org/ASN/resources/S114376F	\N
61	2	http://asn.jesandco.org/resources/S1143770	english	CCSS.ELA-Literacy.CCRA.R.4	CCR.R.4	\N	Interpret words and phrases as they are used in a text, including determining technical, connotative, and figurative meanings, and analyze how specific word choices shape meaning or tone	\N	\N	http://purl.org/ASN/resources/S1143770	\N
62	2	http://asn.jesandco.org/resources/S1143771	english	CCSS.ELA-Literacy.CCRA.R.5	CCR.R.5	\N	Analyze the structure of texts, including how specific sentences, paragraphs, and larger portions of the text (e.g., a section, chapter, scene, or stanza) relate to each other and the whole	\N	\N	http://purl.org/ASN/resources/S1143771	\N
63	2	http://asn.jesandco.org/resources/S1143772	english	CCSS.ELA-Literacy.CCRA.R.6	CCR.R.6	\N	Assess how point of view or purpose shapes the content and style of a text	\N	\N	http://purl.org/ASN/resources/S1143772	\N
64	2	http://asn.jesandco.org/resources/S1143773	english	CCSS.ELA-Literacy.CCRA.R.7	CCR.R.7	\N	Integrate and evaluate content presented in diverse media and formats, including visually and quantitatively, as well as in words.	\N	\N	http://purl.org/ASN/resources/S1143773	\N
65	2	http://asn.jesandco.org/resources/S1143774	english	CCSS.ELA-Literacy.CCRA.R.8	CCR.R.8	\N	Delineate and evaluate the argument and specific claims in a text, including the validity of the reasoning as well as the relevance and sufficiency of the evidence	\N	\N	http://purl.org/ASN/resources/S1143774	\N
66	2	http://asn.jesandco.org/resources/S1143775	english	CCSS.ELA-Literacy.CCRA.R.9	CCR.R.9	\N	Analyze how two or more texts address similar themes or topics in order to build knowledge or to compare the approaches the authors take	\N	\N	http://purl.org/ASN/resources/S1143775	\N
67	2	http://asn.jesandco.org/resources/S1143776	english	CCSS.ELA-Literacy.CCRA.R.10	CCR.R.10	\N	Read and comprehend complex literary and informational texts independently and proficiently	\N	\N	http://purl.org/ASN/resources/S1143776	\N
68	2	http://asn.jesandco.org/resources/S11436B2	english	CCSS.ELA-Literacy.RL.K.1	RL.K.1	\N	With prompting and support, ask and answer questions about key details in a text	\N	\N	http://purl.org/ASN/resources/S11436B2	\N
69	2	http://asn.jesandco.org/resources/S11436B3	english	CCSS.ELA-Literacy.RL.K.2	RL.K.2	\N	With prompting and support, retell familiar stories, including key details	\N	\N	http://purl.org/ASN/resources/S11436B3	\N
70	2	http://asn.jesandco.org/resources/S11436B4	english	CCSS.ELA-Literacy.RL.K.3	RL.K.3	\N	With prompting and support, identify characters, settings, and major events in a story	\N	\N	http://purl.org/ASN/resources/S11436B4	\N
71	2	http://asn.jesandco.org/resources/S11436B5	english	CCSS.ELA-Literacy.RL.1.1	RL.1.1	\N	Ask and answer questions about key details in a text		\N	http://purl.org/ASN/resources/S11436B5	\N
72	2	http://asn.jesandco.org/resources/S11436B6	english	CCSS.ELA-Literacy.RL.1.2	RL.1.2	\N	Retell stories, including key details, and demonstrate understanding of their central message or lesson	\N	\N	http://purl.org/ASN/resources/S11436B6	\N
73	2	http://asn.jesandco.org/resources/S11436B7	english	CCSS.ELA-Literacy.RL.1.3	RL.1.3	\N	Describe characters, settings, and major events in a story, using key details		\N	http://purl.org/ASN/resources/S11436B7	\N
74	2	http://asn.jesandco.org/resources/S11436B8	english	CCSS.ELA-Literacy.RL.2.1	RL.2.1	\N	Ask and answer such questions as who, what, where, when, why, and how to demonstrate understanding of key details in a text	\N	\N	http://purl.org/ASN/resources/S11436B8	\N
75	2	http://asn.jesandco.org/resources/S11436B9	english	CCSS.ELA-Literacy.RL.2.2	RL.2.2	\N	Recount stories, including fables and folktales from diverse cultures, and determine their central message, lesson, or moral	\N	\N	http://purl.org/ASN/resources/S11436B9	\N
76	2	http://asn.jesandco.org/resources/S11436BA	english	CCSS.ELA-Literacy.RL.2.3	RL.2.3	\N	Describe how characters in a story respond to major events and challenges	\N	\N	http://purl.org/ASN/resources/S11436BA	\N
77	2	http://asn.jesandco.org/resources/S11436BB	english	CCSS.ELA-Literacy.RL.3.1	RL.3.1	\N	Ask and answer questions to demonstrate understanding of a text, referring explicitly to the text as the basis for the answers	\N	\N	http://purl.org/ASN/resources/S11436BB	\N
78	2	http://asn.jesandco.org/resources/S11436BC	english	CCSS.ELA-Literacy.RL.3.2	RL.3.2	\N	Recount stories, including fables, folktales, and myths from diverse cultures; determine the central message, lesson, or moral and explain how it is conveyed through key details in the text	\N	\N	http://purl.org/ASN/resources/S11436BC	\N
79	2	http://asn.jesandco.org/resources/S11436BD	english	CCSS.ELA-Literacy.RL.3.3	RL.3.3	\N	Describe characters in a story (e.g., their traits, motivations, or feelings) and explain how their actions contribute to the sequence of events	\N	\N	http://purl.org/ASN/resources/S11436BD	\N
80	2	asdfas	english	asdf	asdf	\N	asdf	asdf	\N	sadf	\N
81	2	aa	english	aa	aa	\N	aa	aa	\N	aa	\N
\.


--
-- Name: statements_id_seq; Type: SEQUENCE SET; Schema: public; Owner: silbermm
--

SELECT pg_catalog.setval('statements_id_seq', 81, true);


--
-- Name: Person_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY "Person"
    ADD CONSTRAINT "Person_pkey" PRIMARY KEY (id);


--
-- Name: RelationshipType_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY "RelationshipType"
    ADD CONSTRAINT "RelationshipType_pkey" PRIMARY KEY (id);


--
-- Name: SecureUser_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY "SecureUser"
    ADD CONSTRAINT "SecureUser_pkey" PRIMARY KEY (id);


--
-- Name: dummies_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY dummies
    ADD CONSTRAINT dummies_pkey PRIMARY KEY (id);


--
-- Name: education_levels_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY education_levels
    ADD CONSTRAINT education_levels_pkey PRIMARY KEY (id);


--
-- Name: person_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY person_roles
    ADD CONSTRAINT person_roles_pkey PRIMARY KEY (id);


--
-- Name: play_evolutions_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY play_evolutions
    ADD CONSTRAINT play_evolutions_pkey PRIMARY KEY (id);


--
-- Name: question_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY question_scores
    ADD CONSTRAINT question_scores_pkey PRIMARY KEY (id);


--
-- Name: questions_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: questions_with_statements_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY questions_with_statements
    ADD CONSTRAINT questions_with_statements_pkey PRIMARY KEY (id);


--
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: school_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY school
    ADD CONSTRAINT school_pkey PRIMARY KEY (id);


--
-- Name: scores_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT scores_pkey PRIMARY KEY (id);


--
-- Name: standard_levels_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY standard_levels
    ADD CONSTRAINT standard_levels_pkey PRIMARY KEY (id);


--
-- Name: standards_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY standards
    ADD CONSTRAINT standards_pkey PRIMARY KEY (id);


--
-- Name: statement_levels_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY statement_levels
    ADD CONSTRAINT statement_levels_pkey PRIMARY KEY (id);


--
-- Name: statements_pkey; Type: CONSTRAINT; Schema: public; Owner: silbermm; Tablespace: 
--

ALTER TABLE ONLY statements
    ADD CONSTRAINT statements_pkey PRIMARY KEY (id);


--
-- Name: idx_uid; Type: INDEX; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE UNIQUE INDEX idx_uid ON "Person" USING btree (uid);


--
-- Name: idx_value; Type: INDEX; Schema: public; Owner: silbermm; Tablespace: 
--

CREATE UNIQUE INDEX idx_value ON education_levels USING btree (value);


--
-- Name: educationLevel; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY "Person"
    ADD CONSTRAINT "educationLevel" FOREIGN KEY ("educationLevelId") REFERENCES education_levels(id);


--
-- Name: education_level; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY standard_levels
    ADD CONSTRAINT education_level FOREIGN KEY (education_level_id) REFERENCES education_levels(id);


--
-- Name: education_level; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY statement_levels
    ADD CONSTRAINT education_level FOREIGN KEY (education_level_id) REFERENCES education_levels(id);


--
-- Name: person_id; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY person_roles
    ADD CONSTRAINT person_id FOREIGN KEY (person_id) REFERENCES "Person"(id);


--
-- Name: question; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT question FOREIGN KEY ("questionId") REFERENCES questions(id);


--
-- Name: question_fk; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY questions_with_statements
    ADD CONSTRAINT question_fk FOREIGN KEY (question_id) REFERENCES questions(id);


--
-- Name: questionscores_question_fk; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY question_scores
    ADD CONSTRAINT questionscores_question_fk FOREIGN KEY ("questionId") REFERENCES questions(id);


--
-- Name: questionscores_student_fk; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY question_scores
    ADD CONSTRAINT questionscores_student_fk FOREIGN KEY ("studentId") REFERENCES "Person"(id);


--
-- Name: role_id; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY person_roles
    ADD CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES roles(id);


--
-- Name: standard; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY standard_levels
    ADD CONSTRAINT standard FOREIGN KEY (standard_id) REFERENCES standards(id);


--
-- Name: statement; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY statement_levels
    ADD CONSTRAINT statement FOREIGN KEY (statement_id) REFERENCES statements(id);


--
-- Name: statement_fk; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY questions_with_statements
    ADD CONSTRAINT statement_fk FOREIGN KEY (statement_id) REFERENCES statements(id);


--
-- Name: student; Type: FK CONSTRAINT; Schema: public; Owner: silbermm
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT student FOREIGN KEY ("studentId") REFERENCES "Person"(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


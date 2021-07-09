CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;

CREATE TYPE accesslevel AS ENUM (
    'READ',
    'EDIT',
    'ADMIN'
);

CREATE TYPE visibility AS ENUM (
    'PUBLIC',
    'PRIVATE'
);

CREATE TABLE account (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    role text NOT NULL,
    CONSTRAINT account_username_length CHECK (((char_length(username) > 0) AND (char_length(username) <= 50)))
);

CREATE TABLE document (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    project_id uuid NOT NULL,
    name text NOT NULL,
    content text NOT NULL,
    CONSTRAINT document_name_length CHECK (((char_length(name) > 0) AND (char_length(name) <= 50)))
);

CREATE TABLE idmapping (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    externalid text NOT NULL
);

CREATE TABLE project (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name text NOT NULL,
    owner_id uuid NOT NULL,
    visibility visibility DEFAULT 'PUBLIC'::visibility NOT NULL,
    CONSTRAINT project_name_length CHECK (((char_length(name) > 0) AND (char_length(name) <= 50)))
);

CREATE TABLE representation (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    project_id uuid NOT NULL,
    targetobjectid text NOT NULL,
    label text NOT NULL,
    contenttype text NOT NULL,
    content text NOT NULL,
    CONSTRAINT representation_label_length CHECK (((char_length(label) > 0) AND (char_length(label) <= 50)))
);

ALTER TABLE ONLY account
    ADD CONSTRAINT account_username_must_be_different UNIQUE (username);

ALTER TABLE ONLY idmapping
    ADD CONSTRAINT idmapping_externalid_unique UNIQUE (externalid);

ALTER TABLE ONLY account
    ADD CONSTRAINT pk_account_id PRIMARY KEY (id);

ALTER TABLE ONLY document
    ADD CONSTRAINT pk_document_id PRIMARY KEY (id);

ALTER TABLE ONLY idmapping
    ADD CONSTRAINT pk_idmapping_id PRIMARY KEY (id);

ALTER TABLE ONLY project
    ADD CONSTRAINT pk_project_id PRIMARY KEY (id);

ALTER TABLE ONLY representation
    ADD CONSTRAINT pk_representation_id PRIMARY KEY (id);

ALTER TABLE ONLY document
    ADD CONSTRAINT fk_document_project_id_id2 FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE;

ALTER TABLE ONLY project
    ADD CONSTRAINT fk_project_owner_id_id FOREIGN KEY (owner_id) REFERENCES account(id);

ALTER TABLE ONLY representation
    ADD CONSTRAINT fk_representation_project_id_id FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE;


CREATE TABLE CustomImage (
	id UUID DEFAULT gen_random_uuid() NOT NULL,
	label TEXT NOT NULL,
	filename TEXT NOT NULL,
	content BYTEA NOT NULL,
	CONSTRAINT pk_customimage_id PRIMARY KEY (id)
);
 
-- password is "012345678910" encrypted using Spring's BCryptPasswordEncoder
INSERT INTO Account
VALUES ('7d345191-8c47-4387-aac9-6e125d7cee60', 'system', '$2a$10$T99K83wmZ7BBwuh7JrZ8o.1n.J30ciaRPfUZzo2yLEWQ/cXBtFVPK', 'ADMIN'); 

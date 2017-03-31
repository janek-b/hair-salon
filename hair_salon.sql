--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

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
-- Name: appointments; Type: TABLE; Schema: public; Owner: janek
--

CREATE TABLE appointments (
    id integer NOT NULL,
    clientid integer,
    stylistid integer,
    appdate date,
    apptime time without time zone
);


ALTER TABLE appointments OWNER TO janek;

--
-- Name: appointments_id_seq; Type: SEQUENCE; Schema: public; Owner: janek
--

CREATE SEQUENCE appointments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE appointments_id_seq OWNER TO janek;

--
-- Name: appointments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: janek
--

ALTER SEQUENCE appointments_id_seq OWNED BY appointments.id;


--
-- Name: clients; Type: TABLE; Schema: public; Owner: janek
--

CREATE TABLE clients (
    id integer NOT NULL,
    name character varying,
    stylistid integer
);


ALTER TABLE clients OWNER TO janek;

--
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: janek
--

CREATE SEQUENCE clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clients_id_seq OWNER TO janek;

--
-- Name: clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: janek
--

ALTER SEQUENCE clients_id_seq OWNED BY clients.id;


--
-- Name: stylists; Type: TABLE; Schema: public; Owner: janek
--

CREATE TABLE stylists (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE stylists OWNER TO janek;

--
-- Name: stylists_id_seq; Type: SEQUENCE; Schema: public; Owner: janek
--

CREATE SEQUENCE stylists_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stylists_id_seq OWNER TO janek;

--
-- Name: stylists_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: janek
--

ALTER SEQUENCE stylists_id_seq OWNED BY stylists.id;


--
-- Name: appointments id; Type: DEFAULT; Schema: public; Owner: janek
--

ALTER TABLE ONLY appointments ALTER COLUMN id SET DEFAULT nextval('appointments_id_seq'::regclass);


--
-- Name: clients id; Type: DEFAULT; Schema: public; Owner: janek
--

ALTER TABLE ONLY clients ALTER COLUMN id SET DEFAULT nextval('clients_id_seq'::regclass);


--
-- Name: stylists id; Type: DEFAULT; Schema: public; Owner: janek
--

ALTER TABLE ONLY stylists ALTER COLUMN id SET DEFAULT nextval('stylists_id_seq'::regclass);


--
-- Data for Name: appointments; Type: TABLE DATA; Schema: public; Owner: janek
--

COPY appointments (id, clientid, stylistid, appdate, apptime) FROM stdin;
1	1	1	2017-04-13	15:30:00
2	2	2	2017-03-31	16:00:00
3	1	1	2017-04-01	15:30:00
4	3	3	2017-03-17	17:30:00
\.


--
-- Name: appointments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: janek
--

SELECT pg_catalog.setval('appointments_id_seq', 4, true);


--
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: janek
--

COPY clients (id, name, stylistid) FROM stdin;
1	Joe	1
3	Matt	2
\.


--
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: janek
--

SELECT pg_catalog.setval('clients_id_seq', 3, true);


--
-- Data for Name: stylists; Type: TABLE DATA; Schema: public; Owner: janek
--

COPY stylists (id, name) FROM stdin;
2	Susan
1	Becky
\.


--
-- Name: stylists_id_seq; Type: SEQUENCE SET; Schema: public; Owner: janek
--

SELECT pg_catalog.setval('stylists_id_seq', 3, true);


--
-- Name: appointments appointments_pkey; Type: CONSTRAINT; Schema: public; Owner: janek
--

ALTER TABLE ONLY appointments
    ADD CONSTRAINT appointments_pkey PRIMARY KEY (id);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: janek
--

ALTER TABLE ONLY clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- Name: stylists stylists_pkey; Type: CONSTRAINT; Schema: public; Owner: janek
--

ALTER TABLE ONLY stylists
    ADD CONSTRAINT stylists_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--


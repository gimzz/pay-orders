--
-- PostgreSQL database dump
--

-- Dumped from database version 15.12 (Debian 15.12-1.pgdg120+1)
-- Dumped by pg_dump version 15.12 (Debian 15.12-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: system; Type: SCHEMA; Schema: -; Owner: admin
--

CREATE SCHEMA system;


ALTER SCHEMA system OWNER TO admin;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categorias; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.categorias (
    id_categoria integer NOT NULL,
    nombre_categoria character varying(100) NOT NULL
);


ALTER TABLE system.categorias OWNER TO admin;

--
-- Name: categorias_id_categoria_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.categorias_id_categoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.categorias_id_categoria_seq OWNER TO admin;

--
-- Name: categorias_id_categoria_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.categorias_id_categoria_seq OWNED BY system.categorias.id_categoria;


--
-- Name: clientes; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.clientes (
    id integer NOT NULL,
    cedula character varying(20) NOT NULL,
    nombre character varying(50),
    apellido character varying(50),
    telefono character varying(20),
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE system.clientes OWNER TO admin;

--
-- Name: clientes_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.clientes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.clientes_id_seq OWNER TO admin;

--
-- Name: clientes_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.clientes_id_seq OWNED BY system.clientes.id;


--
-- Name: detalle_pedido; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.detalle_pedido (
    id integer NOT NULL,
    id_pedido integer NOT NULL,
    id_producto integer NOT NULL,
    cantidad integer NOT NULL,
    precio_unitario_usd numeric(10,2) NOT NULL,
    subtotal_usd numeric(10,2) GENERATED ALWAYS AS (((cantidad)::numeric * precio_unitario_usd)) STORED,
    CONSTRAINT detalle_pedido_cantidad_check CHECK ((cantidad > 0))
);


ALTER TABLE system.detalle_pedido OWNER TO admin;

--
-- Name: detalle_pedido_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.detalle_pedido_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.detalle_pedido_id_seq OWNER TO admin;

--
-- Name: detalle_pedido_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.detalle_pedido_id_seq OWNED BY system.detalle_pedido.id;


--
-- Name: materia_prima; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.materia_prima (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    unidad_medida character varying(50),
    stock_actual integer DEFAULT 0 NOT NULL,
    stock_minimo integer DEFAULT 0 NOT NULL
);


ALTER TABLE system.materia_prima OWNER TO admin;

--
-- Name: materia_prima_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.materia_prima_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.materia_prima_id_seq OWNER TO admin;

--
-- Name: materia_prima_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.materia_prima_id_seq OWNED BY system.materia_prima.id;


--
-- Name: metodos_pago; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.metodos_pago (
    id integer NOT NULL,
    descripcion character varying(50),
    activo boolean DEFAULT true
);


ALTER TABLE system.metodos_pago OWNER TO admin;

--
-- Name: metodos_pago_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.metodos_pago_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.metodos_pago_id_seq OWNER TO admin;

--
-- Name: metodos_pago_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.metodos_pago_id_seq OWNED BY system.metodos_pago.id;


--
-- Name: movimientos_materia_prima; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.movimientos_materia_prima (
    id integer NOT NULL,
    id_materia_prima integer NOT NULL,
    tipo_movimiento character varying(50) NOT NULL,
    cantidad integer NOT NULL,
    motivo text,
    fecha_movimiento timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE system.movimientos_materia_prima OWNER TO admin;

--
-- Name: movimientos_materia_prima_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.movimientos_materia_prima_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.movimientos_materia_prima_id_seq OWNER TO admin;

--
-- Name: movimientos_materia_prima_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.movimientos_materia_prima_id_seq OWNED BY system.movimientos_materia_prima.id;


--
-- Name: movimientosinventario; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.movimientosinventario (
    id integer NOT NULL,
    id_producto integer,
    tipo_movimiento character varying(50) NOT NULL,
    cantidad integer NOT NULL,
    fecha_movimiento timestamp without time zone DEFAULT now() NOT NULL,
    motivo text
);


ALTER TABLE system.movimientosinventario OWNER TO admin;

--
-- Name: movimientosinventario_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.movimientosinventario_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.movimientosinventario_id_seq OWNER TO admin;

--
-- Name: movimientosinventario_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.movimientosinventario_id_seq OWNED BY system.movimientosinventario.id;


--
-- Name: pagos_pedido; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.pagos_pedido (
    id integer NOT NULL,
    id_pedido integer NOT NULL,
    id_metodo_pago integer NOT NULL,
    tipo_moneda public.moneda_enum NOT NULL,
    monto numeric(10,2) NOT NULL,
    fecha_pago timestamp without time zone
);


ALTER TABLE system.pagos_pedido OWNER TO admin;

--
-- Name: pagos_pedido_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.pagos_pedido_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.pagos_pedido_id_seq OWNER TO admin;

--
-- Name: pagos_pedido_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.pagos_pedido_id_seq OWNED BY system.pagos_pedido.id;


--
-- Name: pedidos; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.pedidos (
    id integer NOT NULL,
    cliente_id integer NOT NULL,
    fecha timestamp without time zone,
    total_usd numeric(10,2) NOT NULL,
    tasa_cambio_aplicada numeric(10,4) NOT NULL,
    entregado boolean DEFAULT false,
    pagado boolean DEFAULT false
);


ALTER TABLE system.pedidos OWNER TO admin;

--
-- Name: pedidos_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.pedidos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.pedidos_id_seq OWNER TO admin;

--
-- Name: pedidos_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.pedidos_id_seq OWNED BY system.pedidos.id;


--
-- Name: productos; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.productos (
    id integer NOT NULL,
    nombre character varying(50),
    precio_usd numeric(10,2) NOT NULL,
    stock_actual integer DEFAULT 0 NOT NULL,
    stock_minimo integer DEFAULT 5 NOT NULL,
    activo boolean,
    id_categoria integer
);


ALTER TABLE system.productos OWNER TO admin;

--
-- Name: productos_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.productos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.productos_id_seq OWNER TO admin;

--
-- Name: productos_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.productos_id_seq OWNED BY system.productos.id;


--
-- Name: tasa_cambio; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.tasa_cambio (
    id integer NOT NULL,
    fecha timestamp without time zone,
    moneda_origen character varying(3) DEFAULT 'USD'::character varying NOT NULL,
    moneda_destino character varying(3) DEFAULT 'VES'::character varying NOT NULL,
    valor numeric(10,4) NOT NULL
);


ALTER TABLE system.tasa_cambio OWNER TO admin;

--
-- Name: tasa_cambio_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.tasa_cambio_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.tasa_cambio_id_seq OWNER TO admin;

--
-- Name: tasa_cambio_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.tasa_cambio_id_seq OWNED BY system.tasa_cambio.id;


--
-- Name: usuarios; Type: TABLE; Schema: system; Owner: admin
--

CREATE TABLE system.usuarios (
    id integer NOT NULL,
    nombre_usuario character varying(50) NOT NULL,
    password_usuario character varying(255) NOT NULL,
    rol public.rol_enum NOT NULL,
    activo boolean DEFAULT true NOT NULL,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE system.usuarios OWNER TO admin;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: system; Owner: admin
--

CREATE SEQUENCE system.usuarios_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system.usuarios_id_seq OWNER TO admin;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: system; Owner: admin
--

ALTER SEQUENCE system.usuarios_id_seq OWNED BY system.usuarios.id;


--
-- Name: categorias id_categoria; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.categorias ALTER COLUMN id_categoria SET DEFAULT nextval('system.categorias_id_categoria_seq'::regclass);


--
-- Name: clientes id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.clientes ALTER COLUMN id SET DEFAULT nextval('system.clientes_id_seq'::regclass);


--
-- Name: detalle_pedido id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.detalle_pedido ALTER COLUMN id SET DEFAULT nextval('system.detalle_pedido_id_seq'::regclass);


--
-- Name: materia_prima id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.materia_prima ALTER COLUMN id SET DEFAULT nextval('system.materia_prima_id_seq'::regclass);


--
-- Name: metodos_pago id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.metodos_pago ALTER COLUMN id SET DEFAULT nextval('system.metodos_pago_id_seq'::regclass);


--
-- Name: movimientos_materia_prima id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.movimientos_materia_prima ALTER COLUMN id SET DEFAULT nextval('system.movimientos_materia_prima_id_seq'::regclass);


--
-- Name: movimientosinventario id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.movimientosinventario ALTER COLUMN id SET DEFAULT nextval('system.movimientosinventario_id_seq'::regclass);


--
-- Name: pagos_pedido id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pagos_pedido ALTER COLUMN id SET DEFAULT nextval('system.pagos_pedido_id_seq'::regclass);


--
-- Name: pedidos id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pedidos ALTER COLUMN id SET DEFAULT nextval('system.pedidos_id_seq'::regclass);


--
-- Name: productos id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.productos ALTER COLUMN id SET DEFAULT nextval('system.productos_id_seq'::regclass);


--
-- Name: tasa_cambio id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.tasa_cambio ALTER COLUMN id SET DEFAULT nextval('system.tasa_cambio_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.usuarios ALTER COLUMN id SET DEFAULT nextval('system.usuarios_id_seq'::regclass);


--
-- Data for Name: categorias; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.categorias (id_categoria, nombre_categoria) FROM stdin;
\.


--
-- Data for Name: clientes; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.clientes (id, cedula, nombre, apellido, telefono, fecha_registro) FROM stdin;
1	123456789	Juan	Pérez	+58424567890	2025-08-07 15:48:32.841304
2	9621311	Pedro	Parra	+584125203932	2025-08-07 15:50:26.454209
3	22181478	Luis	Parra	+584129659	2025-08-07 15:51:29.382924
15	28454282	Leticia	Parra	04245110421	\N
\.


--
-- Data for Name: detalle_pedido; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.detalle_pedido (id, id_pedido, id_producto, cantidad, precio_unitario_usd) FROM stdin;
\.


--
-- Data for Name: materia_prima; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.materia_prima (id, nombre, descripcion, unidad_medida, stock_actual, stock_minimo) FROM stdin;
10	carne	carne mechada	kg	30	5
11	bulto	siete bultos de harina\npan	kilo	7	1
\.


--
-- Data for Name: metodos_pago; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.metodos_pago (id, descripcion, activo) FROM stdin;
1	pago movil	t
2	binance	t
3	zinli	t
4	zelle	t
\.


--
-- Data for Name: movimientos_materia_prima; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.movimientos_materia_prima (id, id_materia_prima, tipo_movimiento, cantidad, motivo, fecha_movimiento) FROM stdin;
13	10	ENTRADA	30	Stock inicial	2025-08-24 18:20:42.841828
14	10	SALIDA	10	se cva askljdsadjklasdjkldasf	2025-08-24 18:21:28.50415
15	11	ENTRADA	7	Stock inicial	2025-08-24 19:05:10.735498
16	11	SALIDA	1	asdffdsadfadsfads	2025-08-24 19:05:37.06417
\.


--
-- Data for Name: movimientosinventario; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.movimientosinventario (id, id_producto, tipo_movimiento, cantidad, fecha_movimiento, motivo) FROM stdin;
\.


--
-- Data for Name: pagos_pedido; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.pagos_pedido (id, id_pedido, id_metodo_pago, tipo_moneda, monto, fecha_pago) FROM stdin;
\.


--
-- Data for Name: pedidos; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.pedidos (id, cliente_id, fecha, total_usd, tasa_cambio_aplicada, entregado, pagado) FROM stdin;
\.


--
-- Data for Name: productos; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.productos (id, nombre, precio_usd, stock_actual, stock_minimo, activo, id_categoria) FROM stdin;
\.


--
-- Data for Name: tasa_cambio; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.tasa_cambio (id, fecha, moneda_origen, moneda_destino, valor) FROM stdin;
1	2025-08-11 00:00:00	VES	USD	130.0000
2	2025-08-01 00:00:00	VES	USD	150.0000
3	2025-02-20 00:00:00	VES	USD	200.0000
7	2025-08-24 00:00:00	VES	EUR	300.0000
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: system; Owner: admin
--

COPY system.usuarios (id, nombre_usuario, password_usuario, rol, activo, fecha_creacion) FROM stdin;
3	gimz	12345	ENCARGADO	t	2025-08-02 21:44:33.554947
1	system	Coco2405	ADMIN	t	2025-08-02 18:19:42.268233
12	giova	123456	ENCARGADO	t	2025-08-14 14:58:30.362338
\.


--
-- Name: categorias_id_categoria_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.categorias_id_categoria_seq', 1, false);


--
-- Name: clientes_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.clientes_id_seq', 15, true);


--
-- Name: detalle_pedido_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.detalle_pedido_id_seq', 116, true);


--
-- Name: materia_prima_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.materia_prima_id_seq', 11, true);


--
-- Name: metodos_pago_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.metodos_pago_id_seq', 4, true);


--
-- Name: movimientos_materia_prima_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.movimientos_materia_prima_id_seq', 16, true);


--
-- Name: movimientosinventario_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.movimientosinventario_id_seq', 1, false);


--
-- Name: pagos_pedido_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.pagos_pedido_id_seq', 20, true);


--
-- Name: pedidos_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.pedidos_id_seq', 50, true);


--
-- Name: productos_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.productos_id_seq', 15, true);


--
-- Name: tasa_cambio_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.tasa_cambio_id_seq', 7, true);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: system; Owner: admin
--

SELECT pg_catalog.setval('system.usuarios_id_seq', 18, true);


--
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id_categoria);


--
-- Name: clientes clientes_cedula_key; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.clientes
    ADD CONSTRAINT clientes_cedula_key UNIQUE (cedula);


--
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (id);


--
-- Name: detalle_pedido detalle_pedido_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.detalle_pedido
    ADD CONSTRAINT detalle_pedido_pkey PRIMARY KEY (id);


--
-- Name: materia_prima materia_prima_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.materia_prima
    ADD CONSTRAINT materia_prima_pkey PRIMARY KEY (id);


--
-- Name: metodos_pago metodos_pago_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.metodos_pago
    ADD CONSTRAINT metodos_pago_pkey PRIMARY KEY (id);


--
-- Name: movimientos_materia_prima movimientos_materia_prima_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.movimientos_materia_prima
    ADD CONSTRAINT movimientos_materia_prima_pkey PRIMARY KEY (id);


--
-- Name: movimientosinventario movimientosinventario_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.movimientosinventario
    ADD CONSTRAINT movimientosinventario_pkey PRIMARY KEY (id);


--
-- Name: pagos_pedido pagos_pedido_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pagos_pedido
    ADD CONSTRAINT pagos_pedido_pkey PRIMARY KEY (id);


--
-- Name: pedidos pedidos_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pedidos
    ADD CONSTRAINT pedidos_pkey PRIMARY KEY (id);


--
-- Name: productos productos_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (id);


--
-- Name: tasa_cambio tasa_cambio_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.tasa_cambio
    ADD CONSTRAINT tasa_cambio_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_nombre_usuario_key; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.usuarios
    ADD CONSTRAINT usuarios_nombre_usuario_key UNIQUE (nombre_usuario);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: pedidos fk_cliente; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pedidos
    ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES system.clientes(id) ON DELETE CASCADE;


--
-- Name: pagos_pedido fk_metodo_pago; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pagos_pedido
    ADD CONSTRAINT fk_metodo_pago FOREIGN KEY (id_metodo_pago) REFERENCES system.metodos_pago(id) ON DELETE CASCADE;


--
-- Name: pagos_pedido fk_pedido; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.pagos_pedido
    ADD CONSTRAINT fk_pedido FOREIGN KEY (id_pedido) REFERENCES system.pedidos(id) ON DELETE CASCADE;


--
-- Name: detalle_pedido fk_pedido; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.detalle_pedido
    ADD CONSTRAINT fk_pedido FOREIGN KEY (id_pedido) REFERENCES system.pedidos(id) ON DELETE CASCADE;


--
-- Name: detalle_pedido fk_producto; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.detalle_pedido
    ADD CONSTRAINT fk_producto FOREIGN KEY (id_producto) REFERENCES system.productos(id) ON DELETE CASCADE;


--
-- Name: movimientos_materia_prima movimientos_materia_prima_id_materia_prima_fkey; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.movimientos_materia_prima
    ADD CONSTRAINT movimientos_materia_prima_id_materia_prima_fkey FOREIGN KEY (id_materia_prima) REFERENCES system.materia_prima(id) ON DELETE CASCADE;


--
-- Name: movimientosinventario movimientosinventario_id_producto_fkey; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.movimientosinventario
    ADD CONSTRAINT movimientosinventario_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES system.productos(id);


--
-- Name: productos productos_id_categoria_fkey; Type: FK CONSTRAINT; Schema: system; Owner: admin
--

ALTER TABLE ONLY system.productos
    ADD CONSTRAINT productos_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES system.categorias(id_categoria);


--
-- PostgreSQL database dump complete
--


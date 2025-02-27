CREATE TABLE IF NOT EXISTS business_partner
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    partner_number character varying(30),
    name character varying(80),
    country_code character varying(2)  NOT NULL,
    city character varying(80) NOT NULL,
    currency character varying(3)  NOT NULL,
    provider_id integer,
    last_modified timestamp without time zone,
    tenant_id VARCHAR,
    switch_off_exit boolean DEFAULT false,
    type varchar(40) DEFAULT 'DEFAULT',
    DELETED_AT TIMESTAMP,
    CONSTRAINT pk_business_partner PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS person
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    business_partner_id character(36)  NOT NULL,
    first_name character varying(80)  NOT NULL,
    sur_name character varying(80)  NOT NULL,
    salutation character varying(20)  NOT NULL,
    personal_title character varying(80) ,
    email character varying(100)  NOT NULL,
    phone character varying(100) ,
    state character varying(80) ,
    city character varying(80) ,
    zip_code character varying(10) ,
    street character varying(80),
    street_number character varying(10) ,
    country_code character varying(2) ,
    CONSTRAINT pk_person PRIMARY KEY (id),
    CONSTRAINT fk_person_business_partner FOREIGN KEY (business_partner_id)
        REFERENCES  business_partner (id)
);

CREATE TABLE IF NOT EXISTS connection_point
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    business_partner_id character(36)  NOT NULL,
    type character varying(20)  NOT NULL,
    name character varying(100)  NOT NULL,
    location_id character varying(100) ,
    facility_id character varying(10) ,
    cell_id character varying(10) ,
    operator_id character varying(10) ,
    carpark_type character varying(20) ,
    last_modified timestamp without time zone,
    order_number character varying(50) ,
    technical_place character varying(50) ,
    activated_at date,
    other character varying(255) ,
    with_leave_loop boolean DEFAULT false,
    tenant_name character varying(100)  DEFAULT NULL::character varying,
    geometry_path character varying(255)  DEFAULT NULL::character varying,
    keyclaok_inbound_user boolean DEFAULT true,
    DELETED_AT TIMESTAMP,
    CONSTRAINT pk_connectionp PRIMARY KEY (id),
    CONSTRAINT fk_connectionp_bp FOREIGN KEY (business_partner_id) REFERENCES business_partner (id)

);

CREATE TABLE IF NOT EXISTS connectivity
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    connection_point_id character(36)  NOT NULL,
    port integer NOT NULL,
    host character varying(100)  ,
    base_url character varying(200)  ,
    request_format character varying(100) ,
    response_format character varying(100) ,
    user_name character varying(100) ,
    auth_type character varying(100)  ,
    pwd_credentials character varying(100) ,
    api_key character varying(128) ,
    oacc_endpoint character varying(255) ,
    oacc_client_id character varying(64) ,
    oacc_client_credentials character varying(128) ,
    inb_user_name character varying(100) ,
    inb_rate_profile character varying(128) ,
    CONSTRAINT pk_connectivity PRIMARY KEY (id),
    CONSTRAINT fk_connectivity_cp FOREIGN KEY (connection_point_id) REFERENCES connection_point (id)

);

CREATE TABLE IF NOT EXISTS service
(
    id character(36)  NOT NULL,
    name character varying(100)  NOT NULL,
    sub_name character varying(100)  NOT NULL,
    CONSTRAINT pk_servicepk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS connectionp_service
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    service_id character(36)  NOT NULL,
    connection_point_id character(36)  NOT NULL,
    endpoint_role character varying(20) ,
    service_name character varying(100)  DEFAULT NULL::character varying,
    CONSTRAINT pk_connectionp_service PRIMARY KEY (id),
    CONSTRAINT fk_cpservice_cp FOREIGN KEY (connection_point_id) REFERENCES connection_point (id) ,
    CONSTRAINT fk_cpservice_svc FOREIGN KEY (service_id) REFERENCES service (id)

);


CREATE TABLE IF NOT EXISTS sensor
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    serial_number character varying(36)  NOT NULL,
    lane_number character varying(10)  NOT NULL,
    direction character varying(10)  NOT NULL,
    location_id character varying(100)  NOT NULL,
    connection_point_id character varying(36)  NOT NULL,
    tenant_name character varying ,
    device_name character varying(36)  DEFAULT NULL,
    api_key character varying(128) DEFAULT NULL,
    DELETED_AT TIMESTAMP,
    CONSTRAINT pk_sensor PRIMARY KEY (id),
    CONSTRAINT fk_sensor_cp FOREIGN KEY (connection_point_id) REFERENCES connection_point (id)
);


CREATE TABLE IF NOT EXISTS contract
(
    id character(36)  NOT NULL,
    revision bigint NOT NULL,
    service_id character(36)  NOT NULL,
    supplier_id character(36)  NOT NULL,
    consumer_id character(36)  NOT NULL,
    priority_level character varying(50)  NOT NULL,
    supplier_license_type character varying(50)  NOT NULL,
    supplier_price double precision NOT NULL,
    supplier_currency character varying(3)  NOT NULL,
    supplier_connection_point_selection character varying(50)  NOT NULL,
    consumer_license_type character varying(50)  NOT NULL,
    consumer_price double precision NOT NULL,
    consumer_currency character varying(3)  NOT NULL,
    consumer_connection_point_selection character varying(50)  NOT NULL,
    contract_start date NOT NULL,
    contract_end date,
    service_name character varying ,
    DELETED_AT TIMESTAMP,
    CONSTRAINT pk_contract PRIMARY KEY (id),
    CONSTRAINT fk_contract_supp FOREIGN KEY (supplier_id) REFERENCES business_partner (id),
    CONSTRAINT fk_contract_cons FOREIGN KEY (consumer_id) REFERENCES business_partner (id),
    CONSTRAINT fk_contract_svc FOREIGN KEY (service_id) REFERENCES service (id)

);

CREATE TABLE IF NOT EXISTS consumer
(
    contract_id character(36)  NOT NULL,
    connection_point_id character(36)  NOT NULL,
    CONSTRAINT fk_consumer_contract FOREIGN KEY (contract_id) REFERENCES contract (id) ,
    CONSTRAINT fk_consumer_cp FOREIGN KEY (connection_point_id) REFERENCES connection_point (id)
);

CREATE TABLE IF NOT EXISTS supplier
(
    contract_id character(36)  NOT NULL,
    connection_point_id character(36)  NOT NULL,
    CONSTRAINT fk_supplier_contract FOREIGN KEY (contract_id)  REFERENCES contract (id) ,
    CONSTRAINT fk_supplier_cp FOREIGN KEY (connection_point_id) REFERENCES connection_point (id)
);


INSERT INTO service(id, name, sub_name)
VALUES ('f0ddd6b9-4789-4393-96e8-a35eb9678c0a', 'Online Authorization', 'onlineAuth');
INSERT INTO service(id, name, sub_name)
VALUES ('6da0c851-57bc-455f-8c0e-6a2497b5d9b9', 'Evopark visitor Management', 'deviceControlCommandsGeometry');
INSERT INTO service(id, name, sub_name)
VALUES ('34ce19aa-78c0-4151-8181-396968476154', 'Capacity Management', 'capacity-manager');
INSERT INTO service(id, name, sub_name)
VALUES ('54ce19aa-78c0-4151-8181-396968476153', 'Customer/Contracts', 'customers-contracts');
INSERT INTO service(id, name, sub_name)
VALUES ('64ce19aa-78c0-4151-8181-396968476152', 'Car Park Information', 'cpi-admin');
INSERT INTO service(id, name, sub_name)
VALUES ('79ce19aa-78c0-4151-8181-396968478152', 'Product Administration Management', 'pam-service');
INSERT INTO service(id, name, sub_name)
VALUES ('36ce19aa-68c0-4151-8281-396968476152', 'Tariff Engine', 'tariff-engine-service');

--CREATE INDEX ix_partner_admin_bpid on partner_admin(business_partner_id);
--CREATE INDEX ix_person_bpid on person(business_partner_id);

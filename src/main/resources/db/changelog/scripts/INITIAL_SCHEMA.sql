--liquibase formatted sql

--changeset poc:1
create table car_brand (
    id   bigserial    NOT NULL,
    name varchar(255) NOT NULL,
    constraint car_brand_pk primary key (id),
    constraint car_brand_name_uk unique (name)
);

--changeset poc:2
create table tb_model (
    name         varchar(255) NOT NULL,
    car_brand_id bigint       NOT NULL,
    constraint tb_model_pk primary key (name, car_brand_id),
    constraint tb_model_car_brand_fk foreign key (car_brand_id) references car_brand (id)
);

--changeset poc:3
create table tb_car (
    year         int8           NOT NULL,
    model_name   varchar(255)   NOT NULL,
    car_brand_id bigint         NOT NULL,
    base_price   numeric(19, 2) NOT NULL,
    constraint tb_car_pk primary key (year, car_brand_id, model_name),
    constraint tb_model_car_brand_fk foreign key (car_brand_id, model_name) references tb_model (car_brand_id, name)
);

--changeset poc:4
create table auditory (
    id        bigserial  NOT NULL,
    operation varchar(6) not null,
    constraint auditory_pk primary key (id)
);

create table auditory_value (
    id             bigserial     not null,
    auditory_id    bigint        NULL,
    column_name    varchar(255)  NOT NULL,
    previous_value varchar(4000) NULL,
    current_value  varchar(4000) NOT NULL,
    constraint auditory_value_pk primary key (id),
    constraint auditory_value_fk foreign key (auditory_id) references auditory (id)
);


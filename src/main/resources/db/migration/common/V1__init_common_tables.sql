create table tenants
(
    id              varchar(255) not null primary key,
    created_at      timestamp(6) not null,
    updated_at      timestamp(6),
    admin_fullname  varchar(255) not null,
    admin_email     varchar(255) not null unique,
    admin_username  varchar(255) not null unique,
    admin_password  varchar(255) not null,
    company_code    varchar(255) not null unique,
    company_name    varchar(255) not null,
    email           varchar(255) not null unique,
    status          varchar(255) not null
                    constraint tenants_status_check
                        check ((status)::text = ANY
                            ((ARRAY['PENDING'::character varying,'ACTIVE'::character varying, 'SUSPENDED'::character varying,'INACTIVE'::character varying])))
);

create table users
(
    id         varchar(255) not null primary key,
    created_at timestamp(6) not null,
    created_by varchar(255) not null,
    updated_at timestamp(6),
    updated_by varchar(255),
    deleted    boolean      not null,
    enabled    boolean      not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null unique,
    password        varchar(255) not null,
    username        varchar(255) not null unique,
    role            varchar(255) not null,
                    constraint tenants_role_check
                        check ((role)::text = ANY
                            ((ARRAY['ROLE_PLATFORM_ADMIN'::character varying,'ROLE_COMPANY_ADMIN'::character varying, 'ROLE_ADMINISTRATOR'::character varying,'ROLE_USER'::character varying, 'ROLE_SALES_OPERATOR'::character varying]))),
    tenant_id  varchar(255)
        constraint fk_user_tenant_id references tenants
)

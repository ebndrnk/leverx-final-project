create table IF NOT EXISTS public.app_user
(
    id        bigint       not null
        primary key,
    email     varchar(50)  not null,
    firstname varchar(50)  not null,
    lastname  varchar(50)  not null,
    password  varchar(100) not null,
    role      varchar(255) not null
        constraint app_user_role_check
            check ((role)::text = ANY
        ((ARRAY ['ROLE_SELLER'::character varying, 'ROLE_ADMIN'::character varying])::text[])),
    username  varchar(50)  not null
        constraint uk3k4cplvh82srueuttfkwnylq0
            unique
);
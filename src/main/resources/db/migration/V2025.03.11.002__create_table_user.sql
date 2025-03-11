create table if not exists public.app_user
(
    id                 bigint generated by default as identity
    primary key,
    created_dttm       timestamp(6) default CURRENT_TIMESTAMP not null,
    updated_at         timestamp(6) default CURRENT_TIMESTAMP not null,
    version            bigint       default 1                 not null
    constraint app_user_version_check
    check (version > 0)
    constraint app_user_version_check1
    check (version >= 1),
    email              varchar(50)                            not null,
    is_email_confirmed boolean                                not null,
    password           varchar(100)                           not null,
    role               varchar(255)                           not null
    constraint app_user_role_check
    check ((role)::text = ANY
((ARRAY ['ROLE_SELLER'::character varying, 'ROLE_ADMIN'::character varying])::text[])),
    username           varchar(50)                            not null
    constraint uk3k4cplvh82srueuttfkwnylq0
    unique,
    profile_id         bigint
    constraint uknv8hg42pwanrqg8k89um582st
    unique
    constraint fk4k8ioximnm5upkt9s068b0g3h
    references profile
    );

comment on column app_user.id is 'Первичный ключ';

comment on column app_user.created_dttm is 'В формате ISO 8601: YYYY-MM-DD hh:mm:ss.000000';

comment on column app_user.updated_at is 'В формате ISO 8601: YYYY-MM-DD hh:mm:ss.000000';

comment on column app_user.version is 'Целое число с большим диапазоном от -9223372036854775808 до +9223372036854775807';
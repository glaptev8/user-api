CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


create table wallet_types
(
    id            serial
        primary key,
    created_at    timestamp default now() not null,
    modified_at   timestamp,
    name          varchar(32)             not null,
    currency_code varchar(3)              not null,
    status        varchar(18)             not null,
    archived_at   timestamp,
    profile_type  varchar(15),
    creator       varchar(255),
    modifier      varchar(255)
);

create table wallets
(
    id             bigserial
        primary key,
    created_at     timestamp default now()              not null,
    modified_at    timestamp,
    name           varchar(32)                          not null,
    wallet_type_id bigint                               not null
        constraint fk_wallets_wallet_types
        references wallet_types,
    profile_uid    uuid                                 not null,
    status         varchar(30)                          not null,
    balance        integer   default 0.0                not null,
    archived_at    timestamp,
    wallet_uid     uuid      default uuid_generate_v4() not null
        constraint wallet_uid_unique
        unique
);

create index wallets_id_uidx
    on wallets (id);

create table merchant_fee_rules
(
    id                   bigserial
        primary key,
    created_at           timestamp  default now()                    not null,
    modified_at          timestamp,
    transaction_type     varchar(20)                                 not null,
    percentage           numeric    default 0.0                      not null,
    fixed_amount         integer    default 0.0                      not null,
    option               varchar(3),
    archived_at          timestamp,
    wallet_uid           uuid
        references wallets (wallet_uid)
            on delete cascade,
    is_all_wallets       boolean    default false,
    wallet_type_id       integer    default '-1'::integer            not null,
    payment_method_id    integer    default '-1'::integer            not null,
    amount_from          integer,
    amount_to            integer,
    highest_priority     boolean    default false,
    fee_currency         varchar(4) default 'USD'::character varying not null,
    fee_rule_type        varchar(24),
    start_date           timestamp,
    end_date             timestamp,
    status               varchar(10),
    creator_profile_uid  uuid,
    modifier_profile_uid uuid
);

create table rolling_reserve
(
    id             bigserial
        primary key,
    created_at     timestamp default now() not null,
    modified_at    timestamp,
    wallet_uid     uuid                    not null
        references wallets (wallet_uid)
            on delete cascade,
    blocked_amount integer                 not null,
    lockout_period integer                 not null,
    archived_at    timestamp
);
create table individual_fee_rules
(
    id                   bigserial
        primary key,
    created_at           timestamp  default now()                    not null,
    modified_at          timestamp,
    is_all_wallet_types  boolean                                     not null,
    percentage           numeric    default 0.0                      not null,
    fixed_amount         integer    default 0.0                      not null,
    option               varchar(3),
    archived_at          timestamp,
    transaction_type     varchar(20),
    wallet_type_id       integer    default '-1'::integer            not null,
    payment_method_id    integer    default '-1'::integer            not null,
    amount_from          integer,
    amount_to            integer,
    highest_priority     boolean    default false,
    fee_currency         varchar(4) default 'USD'::character varying not null,
    fee_rule_type        varchar(24),
    start_date           timestamp,
    end_date             timestamp,
    status               varchar(10),
    creator_profile_uid  uuid,
    modifier_profile_uid uuid
);
create table transaction_rolling_reserve
(
    transaction_uid    uuid                    not null
        primary key,
    created_at         timestamp default now() not null,
    expires_at         timestamp               not null,
    wallet_uid         uuid                    not null
        references wallets (wallet_uid),
    rolling_reserve_id bigint                  not null
        references rolling_reserve,
    frozen_balance     numeric   default 0.0   not null
);

create index transaction_rolling_reserve_wallet_uid_idx
    on transaction_rolling_reserve (wallet_uid);

create table frozen_transactions
(
    transaction_uid uuid                    not null
        primary key,
    created_at      timestamp default now() not null,
    wallet_uid      uuid                    not null
        references wallets (wallet_uid),
    frozen_balance  integer   default 0.0   not null
);

create index frozen_transactions_wallet_uid_idx
    on frozen_transactions (wallet_uid);

create table transaction_attributes
(
    id              serial
        primary key,
    created_at      timestamp default now() not null,
    modified_at     timestamp,
    transaction_uid uuid                    not null,
    key             text                    not null,
    value           text
);

create table wallet_status_history
(
    id                      bigserial
        primary key,
    created_at              timestamp default now() not null,
    wallet_uid              uuid                    not null
        references wallets (wallet_uid)
            on delete cascade,
    changed_by_user_uid     uuid,
    changed_by_profile_type varchar(20)             not null,
    reason                  varchar(50)             not null,
    from_status             varchar(24),
    comment                 varchar(512),
    to_status               varchar(24)
);

create index wallets_status_history_id_uidx
    on wallet_status_history (id);

create table wallet_types_status_history
(
    id                      bigserial
        primary key,
    created_at              timestamp default now() not null,
    wallet_type_id          bigint
        references wallet_types,
    changed_by_user_uid     uuid                    not null,
    changed_by_profile_type varchar(20)             not null,
    reason                  varchar(50)             not null,
    from_status             varchar(24),
    comment                 varchar(512),
    to_status               varchar(24)
);

create index wallet_types_status_history_id_uidx
    on wallet_types_status_history (id);

create table partner_payment_requests
(
    uid                  uuid      default uuid_generate_v4() not null
        primary key,
    created_at           timestamp default now()              not null,
    modified_at          timestamp default now()              not null,
    metadata             text                                 not null,
    merchant_profile_uid uuid                                 not null,
    currency_code        varchar(3)                           not null,
    amount               integer,
    type                 varchar(32)                          not null,
    notification_url     varchar(1024),
    test_mode            boolean   default true,
    return_url           varchar(1024)
);

create table payment_details
(
    id                          bigserial
        primary key,
    organization_id             bigint                                    not null,
    bank_id                     bigint                                    not null,
    status_id                   bigint                                    not null,
    currency_id                 bigint                                    not null,
    amount                      numeric(20, 10)                           not null,
    description                 varchar(1000),
    name                        varchar(2000)                             not null,
    invoice_id                  varchar(1000),
    account_id                  varchar(1000),
    created                     timestamp(0) with time zone default now() not null,
    updated                     timestamp(0) with time zone default now() not null,
    version                     integer                     default 1     not null,
    bank_response_http_status   integer,
    bank_response_body          text,
    operation_type_id           bigint                                    not null,
    reason_id                   bigint,
    bank_transaction_id         varchar(255),
    card_type_id                bigint,
    card_first_six              varchar(2000),
    card_last_four              varchar(2000),
    card_expiry_year            varchar(2000),
    card_expiry_month           varchar(2000),
    payment_transaction_id      bigint,
    amount_after_refunds        numeric(20, 10),
    payment_ip                  varchar(255),
    identifier                  varchar(255)                              not null,
    token                       varchar(255),
    bin_id                      bigint,
    organization_route_bank_id  bigint                                    not null,
    bank_error                  varchar(4000),
    test_three_ds_result        boolean,
    bank_md                     text,
    bank_pa_res_url             text,
    bank_three_ds_redirect_url  varchar(4000),
    success_redirect_url        varchar(4000),
    failure_redirect_url        varchar(4000),
    in_progress                 boolean                     default true  not null,
    completed                   timestamp(0) with time zone,
    bank_response_data          varchar(4000),
    bank_acs_url                varchar(4000),
    bank_method                 varchar(1000),
    bank_pa_req                 text,
    pending_redirect_url        varchar(4000),
    method_id                   bigint                                    not null,
    alternative_payment_type_id bigint,
    payment_country_id          bigint,
    payment_person_identifier   varchar(1000),
    payment_phone               varchar(1000),
    payment_address             varchar(1000),
    payment_city                varchar(1000),
    payment_first_name          varchar(1000),
    payment_last_name           varchar(1000),
    payment_birth_date          date,
    organization_mid_id         bigint,
    payment_data                varchar(4000),
    merchant_fee                numeric(20, 10),
    client_fee                  numeric(20, 10),
    hold_amount                 numeric(20, 10),
    hold_release_date           timestamp with time zone,
    bank_term_url               text,
    bank_temporary_data         text,
    bank_data                   text,
    authentication_requested    boolean,
    payment_state               varchar(1000),
    payment_postal_code         varchar(1000),
    bank_arn                    varchar(1000),
    bank_eci                    varchar(1000),
    bank_response_code          varchar(1000)
);

create table crypto_wallets (
    uid         uuid      default uuid_generate_v4() not null
        primary key,
    created_at  timestamp default now()              not null,
    modified_at timestamp,
    ticker      varchar(8)                           not null,
    address     varchar(64)                          not null,
    network     varchar(16),
    profile_uid uuid                                 not null,
    status      varchar(32)                          not null,
    wallet_uid  uuid
        references wallets(wallet_uid)
);

create table payment_requests
(
    uid                      uuid      default uuid_generate_v4() not null
        primary key,
    created_at               timestamp default now()              not null,
    modified_at              timestamp,
    profile_uid              uuid                                 not null,
    wallet_uid               uuid                                 not null
        references wallets (wallet_uid),
    amount_gross              integer   default 0.0                not null,
    fee                      integer   default 0.0                not null,
    status                   varchar,
    percentage               numeric   default 0.0                not null,
    fixed_amount             integer   default 0.0                not null,
    option                   varchar(3)                           not null,
    scale                    integer                              not null,
    comment                  varchar(256),
    crypto_callback_id       bigint,
    amount_in_crypto         numeric,
    provider_transaction_uid uuid,
    provider_transaction_id  varchar(255),
    payment_method_id        bigint,
    crypto_wallet_uid        uuid
        references crypto_wallets
);

create table transactions
(
    uid                      uuid      default uuid_generate_v4() not null
        primary key,
    created_at               timestamp default now()              not null,
    modified_at              timestamp,
    linked_transaction       uuid,
    profile_uid              uuid                                 not null,
    wallet_uid               uuid                                 not null
        references wallets (wallet_uid),
    wallet_name              varchar(32)                          not null,
    balance_operation_amount integer   default 0.0                not null,
    raw_amount               integer   default 0.0                not null,
    fee                      integer   default 0.0                not null,
    amount_in_usd            integer   default 0.0                not null,
    type                     varchar(32)                          not null,
    state                    varchar(32)                          not null,
    payment_request_uid      uuid                                 not null
        references payment_requests
            on delete cascade,
    currency_code            varchar(3)                           not null,
    refund_fee               bigint    default 0                  not null
);

create table top_up_requests
(
    uid                 uuid      default uuid_generate_v4() not null
        primary key,
    created_at          timestamp default now()              not null,
    provider            varchar                              not null,
    payment_request_uid uuid                                 not null
        references payment_requests
            on delete cascade
);

create table withdrawal_requests
(
    uid                 uuid      default uuid_generate_v4() not null
        primary key,
    created_at          timestamp default now()              not null,
    provider            varchar                              not null,
    token               varchar                              not null,
    payment_request_uid uuid                                 not null
        references payment_requests
            on delete cascade
);
create table transfer_requests
(
    uid                      uuid      default uuid_generate_v4() not null
        primary key,
    created_at               timestamp default now()              not null,
    system_rate              varchar                              not null,
    payment_request_uid_from uuid                                 not null
        references payment_requests
            on delete cascade,
    payment_request_uid_to   uuid                                 not null
        references payment_requests
            on delete cascade
);

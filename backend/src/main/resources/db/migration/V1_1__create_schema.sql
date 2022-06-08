create TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    auth_token character varying(255),
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    phone character varying(255),
    social_account_type character varying(255),
    username character varying(255),
    contact_phone_number character varying(255),
    mobile_money_number character varying(255),
    physical_address character varying(255),
    clothing_recommendations character varying(255),
    item_queue_tracker bigint DEFAULT 0,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS body_measurements (
    id SERIAL PRIMARY KEY,
    user_id bigint,
    chest_cm integer,
    hips_cm integer,
    waist_cm integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS clothing_sizes (
    id SERIAL PRIMARY KEY,
    eu integer,
    international character varying(255),
    uk integer,
    us integer,
    user_id bigint NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    description character varying(255) NOT NULL,
    image_url character varying(255),
    price bigint,
    size_international character varying(255),
    size_number bigint,
    target character varying(255),
    size_type character varying(255),
    sold boolean,
    brand character varying(255),
    external_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS item_actions (
    id SERIAL PRIMARY KEY,
    action integer NOT NULL,
    count integer NOT NULL,
    item_id bigint NOT NULL,
    user_id bigint NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS item_properties (
    id SERIAL PRIMARY KEY,
    item_id bigint NOT NULL,
    tag_property_id bigint NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    merchant_request_id character varying(255),
    state character varying(255),
    user_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS order_item (
    id SERIAL PRIMARY KEY,
    item_id bigint NOT NULL,
    order_id bigint NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS tags (
    id SERIAL PRIMARY KEY,
    value character varying(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS tag_properties (
    id SERIAL PRIMARY KEY,
    value character varying(255) NOT NULL,
    tag_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

create TABLE IF NOT EXISTS transaction_details (
    id SERIAL PRIMARY KEY,
    amount double precision NOT NULL,
    merchant_request_id character varying(255) NOT NULL,
    mpesa_receipt_number character varying(255) NOT NULL,
    phone_number character varying(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);

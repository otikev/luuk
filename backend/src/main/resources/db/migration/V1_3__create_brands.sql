create TABLE IF NOT EXISTS brands (
    id bigserial NOT NULL,
    description character varying(255) NOT NULL,
    awareness character varying(255) NOT NULL,
    perception character varying(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    CONSTRAINT brands_pkey PRIMARY KEY (id),
    CONSTRAINT unique_brands_description UNIQUE (description)
);
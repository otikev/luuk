alter table users drop COLUMN IF EXISTS measurements_id;
alter table users drop COLUMN IF EXISTS body_measurements_id;
alter table users drop COLUMN IF EXISTS clothing_size_id;
alter table users drop COLUMN IF EXISTS social_id;
alter table users drop COLUMN IF EXISTS phone;
alter table users drop COLUMN IF EXISTS gender;
alter table body_measurements drop COLUMN IF EXISTS chest;
alter table body_measurements drop COLUMN IF EXISTS hips;
alter table body_measurements drop COLUMN IF EXISTS waist;
alter table body_measurements drop COLUMN IF EXISTS size_international;
alter table body_measurements drop COLUMN IF EXISTS size_number;
alter table body_measurements drop COLUMN IF EXISTS size_eu;
alter table body_measurements drop COLUMN IF EXISTS size_uk;
alter table body_measurements drop COLUMN IF EXISTS size_us;
drop table IF EXISTS body_measurement;
drop table IF EXISTS clothing_size;

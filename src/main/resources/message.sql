create table message
(
	id bigint not null
		constraint message_pkey
			primary key,
	text varchar(255)
);

alter table message owner to postgres;


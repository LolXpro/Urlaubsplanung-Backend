drop table if exists urlaub;
drop table if exists urlaub_seq;
drop table if exists employee;
drop table if exists employee_seq;


create table urlaub
(
    id          bigint       not null primary key AUTO_INCREMENT,
    description varchar(255) null,
    end_date    date         not null,
    start_date  date         not null,
    status      smallint     not null,
    type        smallint     not null,
    username    varchar(255) not null
);

create table urlaub_seq
(
    next_val      bigint      not null
);

insert into urlaub_seq VALUES (0);

create table employee
(
    id                  bigint        not null primary key AUTO_INCREMENT,
    sonderurlaubstage   int           not null,
    urlaubstage         int           not null,
    username            varchar(255)  not null
);

create table employee_seq
(
    next_val      bigint      not null
);

insert into employee_seq VALUES (0);

insert into employee (sonderurlaubstage, urlaubstage, username)
values
    (2, 28, 'link'),
    (2, 28, 'zelda'),
    (2, 28, 'mario'),
    (2, 28, 'luigi'),
    (2, 28, 'demo-admin');
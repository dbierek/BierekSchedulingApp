create table user (
    "userId" int,
    "userName" varchar(255),
    "password" varchar(255),
    "active" int,
    "createDate" Timestamp,
    "createdBy" varchar(255),
    "lastUpdate" Timestamp,
    "lastUpdateBy" varchar(255)
);

create table Customer (
    "customerId" int,
    "customerName" varchar(255),
    "addressId" int,
    "active" int,
    "createDate" Timestamp,
    "createdBy" varchar(255),
    "lastUpdate" Timestamp,
    "lastUpdateBy" varchar(255)
);

create table Country (
    "countryId" int,
    "country" varchar(255),
    "createDate" Timestamp,
    "createdBy" varchar(255),
    "lastUpdate" Timestamp,
    "lastUpdateBy" varchar(255)
);

create table City (
    "cityId" int,
    "city" varchar(255),
    "countryId" int,
    "createDate" Timestamp,
    "createdBy" varchar(255),
    "lastUpdate" Timestamp,
    "lastUpdateBy" varchar(255)
);

create table Appointment (
    "appointmentId" int,
    "customerId" int,
    "userId" int,
    "title" varchar(255),
    "description" varchar(255),
    "location" varchar(255),
    "contact" varchar(255),
    "type" varchar(255),
    "url" varchar(255),
    "start" Timestamp,
    "end" Timestamp,
    "createDate" Timestamp,
    "createdBy" varchar(255),
    "lastUpdate" Timestamp,
    "lastUpdateBy" varchar(255)
);

create table Address (
    "addressId" int,
    "address" varchar(255),
    "address2" varchar(255),
    "cityId" int,
    "postalCode" varchar(255),
    "phone" varchar(255),
    "createDate" Timestamp,
    "createdBy" varchar(255),
    "lastUpdate" Timestamp,
    "lastUpdateBy" varchar(255)
);
insert into user values(1, 'admin', 'admin', 1, CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');
insert into user values(2, 'tester', 'tester', 1, CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');
insert into country values(1, 'USA', CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');
insert into city values(1, 'Napa', 1, CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');
insert into address values(1, '1000 Main St', '', 1, '94558', '7075555555', CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');
insert into customer values(1, 'Danny', 1, 1, CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');
insert into appointment values(1, 1, 1, 'Code Demonstration', 'Demonstration of all the code', 'Conference Room 1', 'admin', 'Demonstration', 'dannybierek.com', '2021-08-02 10:30:00', '2021-08-02 11:00:00', CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 'admin');

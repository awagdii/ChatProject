


create table UserInfo (
E_mail varchar2(50),
password varchar2(30),
firstName varchar2(30),
lastName varchar2(30),
gender varchar2(30),
status 	number(1,0),
statusMode varchar2(30),
CONSTRAINT User_id_pk PRIMARY KEY(E_mail));

create table ContactList (
userMail varchar2(50),
friendMail varchar2(50),
friendshipDate date,
CONSTRAINT ContactList_mail_pk PRIMARY KEY(userMail,friendMail),
CONSTRAINT contactList_userMail_fk foreign key(userMail) references UserInfo(E_mail));

create table ChatGroup (
groupId number(2,0),
groupName varchar2(50),
creationDate date,
CONSTRAINT ChatGroup_groupId_pk PRIMARY KEY(groupId));

create table GroupMembers (
groupId number(2,0),
userMail varchar2(50),
CONSTRAINT GroupMemebrs_groupId_mail_pk PRIMARY KEY(groupId,userMail),
CONSTRAINT GroupMembers_userMail_fk foreign key(userMail) references UserInfo(E_mail),
CONSTRAINT GroupMembers_groupId_fk foreign key(groupId) references ChatGroup(groupId));


alter table UserInfo
rename column E_mail to eMail;

alter table ContactList 
add Constraint contactList_friendMail_fk foreign key (friendMail)references UserInfo(eMail);


alter table UserInfo
rename column statusMode to country;

insert into Userinfo values('nessma@yahoo.com','1345','nessma','atef','female',1,'egypt');

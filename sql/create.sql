mysql> use test;
Database changed
mysql> create table Address
    -> (Address_ID varchar(50) primary key,
    -> Line_1 varchar(40),
    -> Line_2 varchar(40),
    -> Line_3 varchar(40),
    -> City varchar(20),
    -> Pincode varchar(10));
Query OK, 0 rows affected (0.81 sec)

mysql> create table profile
    -> ( profile_id varchar(50) primary key,
    -> Date_created date,
    -> Date_updated date,
    -> Designation varchar(50),
    -> Job_Qual varchar(100),
    -> Certificates varchar(200),
    -> Languages varchar(100),
    -> Patents varchar(100),
    -> Skill_Set varchar(100),
    -> Additional_Info varchar(200));
Query OK, 0 rows affected (0.29 sec)


mysql> create table Members
    -> (Member_Id varchar(50) primary key,
    -> Address_ID varchar(50),
    -> foreign key (Address_ID) REFERENCES Address(Address_ID), 
    -> profile_id varchar(50),
    -> foreign key (profile_id) REFERENCES profile(profile_id),
    -> Maritial_Status_Code varchar(20),
    -> Date_Joined date,
    -> Date_of_Birth date,
    -> Email_Address varchar(50),
    -> Email_Password varchar(50),
    -> First_Name varchar(50),
    -> Middle_Name varchar(50),
    -> Last_Name varchar(50),
    -> Gender varchar(50));
Query OK, 0 rows affected (0.41 sec)

mysql> create table Recommendations
    -> ( Member_Recommending_ID varchar(50),
    -> Member_Being_Recommended_ID varchar(50),
    -> Date_of_Recommendation date,
    -> Other_Details varchar(50),
    -> primary key(Member_Recommending_ID,Member_Being_Recommended_ID));
Query OK, 0 rows affected (0.41 sec)


mysql> create table authentication
    -> ( Member_Id varchar(50),
    ->   Password varchar(50),
    ->   Designation varchar(20));
Query OK, 0 rows affected (0.12 sec)

mysql> alter table authentication
    -> add foreign key (Member_Id)
    -> references Members(Member_Id);
Query OK, 0 rows affected (0.38 sec)
Records: 0  Duplicates: 0  Warnings: 0


mysql> notee;

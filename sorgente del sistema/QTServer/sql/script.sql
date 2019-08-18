DROP DATABASE IF EXISTS MapDB;
CREATE DATABASE MapDB;
DROP USER 'MapUser'@'localhost';
flush privileges;
CREATE USER 'MapUser'@'localhost' IDENTIFIED BY 'map';
GRANT CREATE, SELECT, INSERT, DELETE ON MapDB.* TO MapUser@localhost;

CREATE TABLE MapDB.playtennis(
  outlook varchar(10),
  temperature float(5,2),
  umidity varchar(10),
  wind varchar(10),
  play varchar(10));

insert into MapDB.playtennis values
  ('sunny',30.3,'high','weak','no'),
  ('sunny',30.3,'high','strong','no'),
  ('overcast',30.0,'high','weak','yes'),
  ('rain',13.0,'high','weak','yes'),
  ('rain',0.0,'normal','weak','yes'),
  ('rain',0.0,'normal','strong','no'),
  ('overcast',0.1,'normal','strong','yes'),
  ('sunny',13.0,'high','weak','no'),
  ('sunny',0.1,'normal','weak','yes'),
  ('rain',12.0,'normal','weak','yes'),
  ('sunny',12.5,'normal','strong','yes'),
  ('overcast',12.5,'high','strong','yes'),
  ('overcast',29.21,'normal','weak','yes'),
  ('rain',12.5,'high','strong','no');

CREATE TABLE MapDB.emptyTable(
  outlook varchar(10),
  temperature float(5,2));

CREATE TABLE MapDB.gopicnic(
  outlook varchar(10),
  temperature float(5,2),
  umidity varchar(10),
  wind varchar(10),
  picnic varchar(10));

insert into MapDB.gopicnic values
  ('sunny',30.3,'high','weak','yes'),
  ('sunny',30.3,'high','strong','no'),
  ('overcast',30.0,'high','weak','yes'),
  ('rain',13.0,'high','weak','no'),
  ('rain',0.0,'normal','weak','no'),
  ('rain',0.0,'normal','strong','no'),
  ('overcast',0.1,'normal','strong','no'),
  ('sunny',20.0,'high','weak','yes'),
  ('sunny',0.1,'normal','weak','no'),
  ('rain',18.0,'normal','weak','no'),
  ('sunny',12.5,'normal','strong','no'),
  ('overcast',12.5,'high','strong','no'),
  ('overcast',29.21,'normal','weak','yes'),
  ('rain',12.5,'high','strong','no'),
  ('sunny',40,'low','weak','no'),
  ('overcast',23,'low','weak','yes'),
  ('rain',40,'high','strong','no');

CREATE TABLE MapDB.test(
  discreto varchar(10),
  continuo float(5,2));

insert into MapDB.test values
  ('primo',30.3),
  ('secondo',31.2);

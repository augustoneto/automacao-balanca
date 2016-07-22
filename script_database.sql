create database automacao;

use automacao;

create table pedido_peso (
  id int(9) auto_increment primary key,
  cod_pedido int(9),
  cod_produto int(9),
  peso varchar(10),
  unidade varchar(2),
  operador varchar(50),
  data_gravacao datetime
);


-- HSQLDB

create table pedido_peso (
  id integer identity primary key,
  cod_pedido integer,
  cod_produto integer,
  peso varchar(10),
  unidade varchar(2),
  operador varchar(50),
  data_gravacao datetime
);
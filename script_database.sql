create database automacao;

use database automacao;

create table pedido_peso (
  id int(9) auto_increment primary key,
  num_pedido int(9),
  num_nf int(9),
  peso decimal(5, 2),
  unidade varchar(2),
  operador varchar(50),
  data_gravacao datetime
);
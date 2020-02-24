DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario
(
  id varchar(36) not null primary key,
  nome varchar(255) not null,
  cpf varchar(255) not null,
  datanascimento varchar(255) not null,
  senha varchar(255)
);

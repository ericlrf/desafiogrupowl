package com.apress.todo.domain;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Entidade persistente que representa um modelo de domínio para um Usuário, "
		+ "com os campos: id (gerado pelo próprio sistema); "
		+ "nome (não permite caractere numérico); "
		+ "cpf(não permite máscara de CPF inválida); "
		+ "datanascimento (não permite data superior à data atual e nem menor que 01/01/1900); "
		+ "senha (gerado pelo próprio sistema)")
@Data
public class Usuario {
	@ApiModelProperty(required = false, position = 0, hidden = false, allowEmptyValue = true, example = "4b39821f-a312-4c25-8edc-e5473509a9b3", 
			value = "ID do usuário, obrigatório apenas para Atualizar(PUT) ou Deletar(DELETE) um usuário")
	@NotNull
	private String id;
	@ApiModelProperty(required = true, position = 1, example = "José da Silva",
			value = "Nome do usuário, não permite caractere numérico")
	@NotNull
	@NotBlank
	private String nome; // add validation: Não permitir caracter numérico
	@ApiModelProperty(required = true, position = 2, example = "095.820.164-26",
			value = "Número de CPF do usuário, não permite máscara de CPF inválida")
	@NotNull
	@NotBlank
	private String cpf; // add validation: Validar máscara de CPF
	@ApiModelProperty(required = true, position = 3, example = "01/04/1984",
			value = "Data de nascimento do usuário, não permite data superior à data atual e nem menor que 01/01/1900")
	@NotNull
	@NotBlank
	private String datanascimento; // add validation: Não permitir data superior a data atual e nem menor que 01/01/1900
	@ApiModelProperty(required = false, hidden = true)
	private String senha;
	
	public Usuario() {
		// funcao p/ gerar campo senha aqui:
		// 3 primeiros dígitos do cpf + mês de nascimento
		// criptografado em SHA-1
		this.id = UUID.randomUUID().toString();
		this.senha = "123456"; // substituir por funcao p/ gerar senha
	}
	
	public Usuario(String nome, String cpf, String datanascimento) {
		this();
		this.nome = nome;
		this.cpf = cpf;
		this.datanascimento = datanascimento;
	}

}

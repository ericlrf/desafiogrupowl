package com.apress.todo.domain;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Usuario {
	@NotNull
	private String id;
	@NotNull
	@NotBlank
	private String nome; // add validation: Não permitir caracter numérico
	@NotNull
	@NotBlank
	private String cpf; // add validation: Validar máscara de CPF
	@NotNull
	@NotBlank
	private String datanascimento; // add validation: Não permitir data superior a data atual e nem menor que 01/01/1900
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

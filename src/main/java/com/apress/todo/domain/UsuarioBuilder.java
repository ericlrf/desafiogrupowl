package com.apress.todo.domain;

public class UsuarioBuilder {
	private static UsuarioBuilder instance = new UsuarioBuilder();
	private String id = null;
	private String nome = "";
	private String cpf = "";
	private String datanascimento = "";
	private String senha = "";
	
	private UsuarioBuilder(){}
	
	public static UsuarioBuilder create() {
		return instance;
	}
	
	public UsuarioBuilder withInformation(String nome, String cpf, String datanascimento, String senha) {
		this.nome = nome;
		this.cpf = cpf;
		this.datanascimento = datanascimento;
		this.senha = senha;
		return instance;
	}
	
	public UsuarioBuilder withId(String id) {
		this.id = id;
		return instance;
	}
	
	public Usuario build() {
		Usuario result = new Usuario(this.nome, this.cpf, this.datanascimento);
		if(id != null) {
			result.setId(id);
		}
		return result;
	}

}

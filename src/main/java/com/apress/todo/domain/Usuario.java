package com.apress.todo.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	// //
	@ApiModelProperty(required = true, position = 1, example = "José da Silva",
			value = "Nome do usuário, não permite caractere numérico")
	@NotNull(message = "Precisa informar o campo nome")
	@NotBlank(message = "O campo nome não deve estar vazio ou apenas com espaços em branco")
	private String nome; // add validation: Não permitir caracter numérico
	// //
	@ApiModelProperty(required = true, position = 2, example = "727.746.914-35",
			value = "Número de CPF do usuário, não permite máscara de CPF inválida")
	@NotNull
	@NotBlank
	@CPF(message = "CPF inválida ou sem pontuação (sugestão: 727.746.914-35)")
	private String cpf; // add validation: Validar máscara de CPF
	// //
	@ApiModelProperty(required = true, position = 3, example = "01/04/1984",
			value = "Data de nascimento do usuário, não permite data superior à data atual e nem menor que 01/01/1900")
	@NotNull
	@Past
	@JsonFormat(pattern = "dd/MM/yyyy")// descobrir como personalizar messagem de erro JSON, dica: @RestControllerAdvice
	private Date datanascimento; // descobrir como recusar datas anteriores a 01/01/1900, dica: docs/4.1.x/spring-framework-reference/html/validation.html
	// //
	@ApiModelProperty(required = false, hidden = true)
	private String senha;
	
	public Usuario() {
		this.id = UUID.randomUUID().toString();
		this.senha = "";
	}
	
	public Usuario(String nome, String cpf, Date datanascimento) {
		this();
		this.nome = nome;
		this.cpf = cpf;
		this.datanascimento = datanascimento;
		this.senha = this.getSenhaHash(cpf, datanascimento);
	}
	
	public String getSenhaHash(String cpf, Date datanascimento) {
		// funcao p/ gerar campo senha aqui:
		// 3 primeiros dígitos do cpf + mês de nascimento
		// criptografado em SHA-1
		String valor = "";
		String sha1 = "";
		valor = cpf.substring(0, 3);
		valor = valor.concat(String.valueOf(datanascimento.getMonth()+1));
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(valor.getBytes("utf8"));
			sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sha1;
	}

}

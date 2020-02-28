package com.apress.todo.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apress.todo.domain.Usuario;
import com.apress.todo.domain.UsuarioBuilder;
import com.apress.todo.repository.CommonRepository;
import com.apress.todo.repository.UsuarioRepository;
import com.apress.todo.validation.ToDoValidationError;
import com.apress.todo.validation.ToDoValidationErrorBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "CRUD do usuario", 
description = "operações CRUD para usuario", 
consumes = "application/json", 
protocols = "http")
@RestController
@RequestMapping("/api")
public class UsuarioController {
	private CommonRepository<Usuario> usuarioRepository;
	
	@Autowired
	public UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	//						//
	// GET lista de users	//
	//						//
	
	@ApiOperation(value = "Retornar uma lista de usuários cadastrados", 
			notes = "Consulta o banco de dados e retorna todos os registros da tabela usuario em formato JSON", 
			produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de usuários obtida com sucesso", 
					response = Usuario.class, 
					responseContainer = "List"),
			@ApiResponse(code = 401, message = "Não autorizado"),
			@ApiResponse(code = 403, message = "Proibido"),
			@ApiResponse(code = 404, message = "Não encontrado")
	})
	@GetMapping("/usuario")
	public ResponseEntity<Iterable<Usuario>> getUsuarios(){
		return ResponseEntity.ok(usuarioRepository.findAll());
	}
	
	//					//
	// GET {id} user	//
	//					//
	
	@ApiOperation(value = "Retornar um único usuário utilizando seu ID cadastrado", 
			notes = "Consulta o banco de dados e retorna um registro da tabela usuario, que corresponde ao ID informado, em formato JSON", 
			produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuário obtido com sucesso", 
					response = Usuario.class),
			@ApiResponse(code = 401, message = "Não autorizado"),
			@ApiResponse(code = 403, message = "Proibido"),
			@ApiResponse(code = 404, message = "Não encontrado")
	})
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> getUsuarioById(
			@ApiParam(value = "ID de um usuário já cadastrado")
			@PathVariable String id){
		return ResponseEntity.ok(usuarioRepository.findById(id));
	}
	
	//					//
	// POST/PUT user	//
	//					//	
	
	@ApiOperation(value = "Cadastrar(POST) / Atualizar(PUT) um usuário", 
			notes = "Cadastra no banco de dados (método POST) um novo registro na tabela usuario de acordo com os dados informados (não incluir o ID) em formato JSON"
					+ " / Atualiza no banco de dados (método PUT) um registro já existente na tabela usuario de acordo com os dados informados (incluir o ID) em formato JSON", 
			consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resposta recebida (pode conter mensagem de erros)"),
			@ApiResponse(code = 201, message = "Usuário cadastrado/atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro de validação, campo(s) inválido(s)"),
			@ApiResponse(code = 401, message = "Não autorizado"),
			@ApiResponse(code = 403, message = "Proibido"),
			@ApiResponse(code = 404, message = "Não encontrado")
	})
	@RequestMapping(value="/usuario", method = {RequestMethod.POST,RequestMethod.PUT})
	public ResponseEntity<?> createUsuario(
			@ApiParam(value = "Objeto usuário em formato JSON")
			@Valid @RequestBody Usuario usuario, 
			@ApiIgnore
			Errors errors){
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
		}
		Usuario result = usuarioRepository.save(usuario);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	//					//
	// DELETE {id} user	//
	//					//
	
	@ApiOperation(value = "Deletar um único usuário utilizando seu ID cadastrado", 
			notes = "Consulta o banco de dados e remove um registro da tabela usuario que corresponda ao ID informado")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resposta recebida"),
			@ApiResponse(code = 401, message = "Não autorizado"),
			@ApiResponse(code = 403, message = "Proibido"),
			@ApiResponse(code = 204, message = "Usuário removido com sucesso/sem registro encontrado")
	})
	@DeleteMapping("/usuario/{id}")
	public ResponseEntity<Usuario> deleteUsuario(
			@ApiParam(value = "ID de um usuário já cadastrado")
			@PathVariable String id){
		usuarioRepository.delete(UsuarioBuilder.create().withId(id).build());
		return ResponseEntity.noContent().build();
	}
	
	//					//
	// DELETE user	//
	//					//

	@ApiOperation(value = "Deletar um usuário cadastrado", 
			notes = "Consulta o banco de dados e remove um registro da tabela usuario que corresponda ao usuário informado (incluindo seu campo ID)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resposta recebida"),
			@ApiResponse(code = 401, message = "Não autorizado"),
			@ApiResponse(code = 403, message = "Proibido"),
			@ApiResponse(code = 204, message = "Usuário removido com sucesso/sem registro encontrado")
	})
	@DeleteMapping("/usuario")
	public ResponseEntity<Usuario> deleteUsuario(
			@ApiParam(value = "Objeto usuário em formato JSON")
			@RequestBody Usuario usuario){
		usuarioRepository.delete(usuario);
		return ResponseEntity.noContent().build();
	}
	
	@ExceptionHandler
	public ToDoValidationError handleException(Exception exception) {
		return new ToDoValidationError(exception.getMessage());
	}

}

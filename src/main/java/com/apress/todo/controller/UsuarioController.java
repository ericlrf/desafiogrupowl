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

@RestController
@RequestMapping("/api")
public class UsuarioController {
	private CommonRepository<Usuario> usuarioRepository;
	
	@Autowired
	public UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@GetMapping("/usuario")
	public ResponseEntity<Iterable<Usuario>> getUsuarios(){
		return ResponseEntity.ok(usuarioRepository.findAll());
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable String id){
		return ResponseEntity.ok(usuarioRepository.findById(id));
	}
	
	@RequestMapping(value="/usuario", method = {RequestMethod.POST,RequestMethod.PUT})
	public ResponseEntity<?> createUsuario(@Valid @RequestBody Usuario usuario, Errors errors){
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
		}
		Usuario result = usuarioRepository.save(usuario);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/usuario/{id}")
	public ResponseEntity<Usuario> deleteUsuario(@PathVariable String id){
		usuarioRepository.delete(UsuarioBuilder.create().withId(id).build());
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/todo")
	public ResponseEntity<Usuario> deleteUsuario(@RequestBody Usuario usuario){
		usuarioRepository.delete(usuario);
		return ResponseEntity.noContent().build();
	}
	
	@ExceptionHandler
	public ToDoValidationError handleException(Exception exception) {
		return new ToDoValidationError(exception.getMessage());
	}

}

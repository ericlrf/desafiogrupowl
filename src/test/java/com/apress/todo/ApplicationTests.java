package com.apress.todo;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import com.apress.todo.domain.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTests {
	static Validator validator;
	
	@BeforeClass
	public static void setupClass() {
		validator = ValidationUtils.buildValidator(); //? criar classe
	}

	@Test
	public void validarSenha() {
		SimpleDateFormat simpleDateObject = new SimpleDateFormat("dd/MM/yyyy");
		Usuario user1 = null;
		Date d1;
		try {
			d1 = simpleDateObject.parse("14/02/1995");
			user1 = new Usuario("Luiz Rodrigues", "727.746.914-35", d1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//'7272' hash SHA1: '428940C109A3159ED1C15915971C3958E15B8EA5'
		
		assertEquals("428940C109A3159ED1C15915971C3958E15B8EA5", user1.getSenha().toUpperCase());
		
	}
	
//	@Test
	public void validarCpf() {
		Usuario cadastro = gerarUserMock();
		cadastro.setCpf("123"); // lombok annotation na classe Usuario
		
		Set<ConstraintViolation<Usuario>> constraintViolations = validator.validate(cadastro);
		assertEquals("CPF inválido", constraintViolations.iterator().next().getMessage());
	}
	
	Usuario gerarUserMock() {
		Usuario user = null;
		SimpleDateFormat simpleDateObject = new SimpleDateFormat("dd/MM/yyyy");
		simpleDateObject.setLenient(false);
		Date dateObject;
		try {
			dateObject = simpleDateObject.parse("14/02/1995");
			user = new Usuario("Hebert Francisco", "069.180.426-26", dateObject);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return user;
	}

}

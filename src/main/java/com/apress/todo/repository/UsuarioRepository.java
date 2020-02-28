package com.apress.todo.repository;

import java.sql.ResultSet;
import java.util.*;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.apress.todo.domain.Usuario;

@Repository
public class UsuarioRepository  implements CommonRepository<Usuario>{
	
    private static final String SQL_INSERT = "insert into usuario (id, nome, cpf, datanascimento, senha) values (:id,:nome,:cpf,:datanascimento,:senha)";
    private static final String SQL_QUERY_FIND_ALL = "select id, nome, cpf, datanascimento, senha from usuario";
    private static final String SQL_QUERY_FIND_BY_ID = SQL_QUERY_FIND_ALL + " where id = :id";
    private static final String SQL_UPDATE = "update usuario set id = :id, nome = :nome, cpf = :cpf, datanascimento = :datanascimento, senha = :senha where id = :id";
    private static final String SQL_DELETE = "delete from usuario where id = :id";
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    
    public UsuarioRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    	this.jdbcTemplate = jdbcTemplate;
    }
    
    private RowMapper<Usuario> usuarioRowMapper = (ResultSet rs, int rowNum) -> {
    	Usuario usuario = new Usuario();
    	usuario.setId(rs.getString("id"));
    	usuario.setNome(rs.getString("nome"));
    	usuario.setCpf(rs.getString("cpf"));
    	usuario.setDatanascimento(rs.getDate("datanascimento"));
    	usuario.setSenha(rs.getString("senha"));
    	return usuario;
    };

	@Override
	public Usuario save(final Usuario domain) {
		Usuario result = findById(domain.getId());
		if (result != null) {
			result.setNome(domain.getNome());
			result.setCpf(domain.getCpf());
			result.setDatanascimento(domain.getDatanascimento());
			result.setSenha(domain.getSenha());
			return upsert(result, SQL_UPDATE);
		}
		return upsert(domain, SQL_INSERT);
	}
	
	private Usuario upsert(final Usuario usuario, final String sql) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", usuario.getId());
		namedParameters.put("nome", usuario.getNome());
		namedParameters.put("cpf", usuario.getCpf());
		namedParameters.put("datanascimento", usuario.getDatanascimento());
		namedParameters.put("senha", usuario.getSenhaHash(usuario.getCpf(), usuario.getDatanascimento()));
		
		this.jdbcTemplate.update(sql, namedParameters);
		return findById(usuario.getId());
	}

	@Override
	public Iterable<Usuario> save(Collection<Usuario> domains) {
		domains.forEach(this::save);
		return findAll();
	}

	@Override
	public void delete(final Usuario domain) {
		Map<String, String> namedParameters = Collections.singletonMap("id", domain.getId());
		this.jdbcTemplate.update(SQL_DELETE, namedParameters);
	}

	@Override
	public Usuario findById(String id) {
		try {
			Map<String, String> namedParameters = Collections.singletonMap("id", id);
			return this.jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_ID, namedParameters, usuarioRowMapper);
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	@Override
	public Iterable<Usuario> findAll() {
		return this.jdbcTemplate.query(SQL_QUERY_FIND_ALL, usuarioRowMapper);
	}
    
    
}

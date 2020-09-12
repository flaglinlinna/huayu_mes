package com.app.query.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class JdbcDao extends NamedParameterJdbcTemplate {
	protected DataSource dataSource;

	public JdbcDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		super(dataSource);
		this.jdbcTemplate = jdbcTemplate;
	}

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public List<Map<String, Object>> find(String sql, SQLParameter<Object> parameters) {
		return jdbcTemplate.queryForList(sql, parameters.toArray(new Object[]{}));
	}

}

package com.pharmondev.springtwofactor.security.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    NamedParameterJdbcTemplate template;

    @Autowired
    public UserDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /**
     * Retrieves the TOTP Secret for a given user.
     *
     * @param username The username of the user to retrieve the TOTP token for.
     * @return The TOTP Secret for the user.
     */
    public String getUserTotpToken( String username){

        String sql = "SELECT totp_token FROM users WHERE username = :username";
        SqlParameterSource paramSource = new MapSqlParameterSource("username", username);
        return template.queryForObject(sql, paramSource, String.class);

    }

    private JdbcTemplate jdbcTemplate;



    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

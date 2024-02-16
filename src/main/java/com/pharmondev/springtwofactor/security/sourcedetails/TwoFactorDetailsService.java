package com.pharmondev.springtwofactor.security.sourcedetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The TwoFactorDetailsService class implements the UserDetailsService interface and provides
 * a method to load user details by username from the database.
 */
@Component
public class TwoFactorDetailsService implements UserDetailsService {

    private JdbcTemplate jdbcTemplate;

    /**
     * Loads user details by username from the database.
     *
     * @param username The username of the user to load.
     * @return The User object representing the loaded user details.
     * @throws UsernameNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userQuery = "SELECT username, password, enabled FROM users WHERE username = ?";
        String authorQuery = "SELECT authority FROM authorities WHERE username = ?";
        List<GrantedAuthority>  grantedAuthorities = new ArrayList<>();
        try {
            List<String> authorities = jdbcTemplate.queryForList(authorQuery, new Object[]{username}, String.class);
            for(String authority : authorities) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority));
            }
            User user = jdbcTemplate.queryForObject(userQuery, new Object[]{username},
                    (rs, rowNum) -> new User(

                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBoolean("enabled"),
                            true, true, true,
                            grantedAuthorities));
            return user;

        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User not found: " + username, e);
        }
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
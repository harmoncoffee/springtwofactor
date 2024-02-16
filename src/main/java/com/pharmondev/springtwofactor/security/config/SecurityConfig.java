package com.pharmondev.springtwofactor.security.config;

import com.pharmondev.springtwofactor.security.authenticaton.TwoFactorAuthenticationProvider;
import com.pharmondev.springtwofactor.security.dao.UserDao;
import com.pharmondev.springtwofactor.security.sourcedetails.TwoFactorDetailSource;
import com.pharmondev.springtwofactor.security.sourcedetails.TwoFactorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDao userTotpDao;

    private TwoFactorDetailsService service;

    /**
     * Returns a SecurityFilterChain object configured with the provided HttpSecurity and TwoFactorDetailSource.
     *
     * @param http                   the HttpSecurity object to configure the security filter chain
     * @param twoFactorDetailSource  the TwoFactorDetailSource object used for authentication details
     * @return the configured SecurityFilterChain object
     * @throws Exception             if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Autowired TwoFactorDetailSource twoFactorDetailSource) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form
                .authenticationDetailsSource(twoFactorDetailSource)
                .loginPage("/login")
                .permitAll()
            )
            .logout((logout) -> {
                logout
                .logoutSuccessUrl("/login")
                .permitAll();
                logout.deleteCookies("JSESSIONID");
            }
            );

        return http.build();
    }
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Creates and returns a DaoAuthenticationProvider object configured with a TwoFactorAuthenticationProvider.
     * The TwoFactorAuthenticationProvider is responsible for authenticating users with two-factor authentication.
     *
     * @return the configured DaoAuthenticationProvider object
     *
     * @see TwoFactorAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        TwoFactorAuthenticationProvider authProvider = new TwoFactorAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setUserDao(userTotpDao);
        authProvider.setService(service);
        return authProvider;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userTotpDao = userDao;
    }

    @Autowired
    public void setService(TwoFactorDetailsService service) {
        this.service = service;
    }

    /**
     * Returns a SpringTemplateEngine object configured with the provided ITemplateResolver.
     *
     * @param templateResolver the ITemplateResolver object to set as the template resolver for the SpringTemplateEngine
     * @return the configured SpringTemplateEngine object
     */
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(templateResolver);
        springTemplateEngine.addDialect(new SpringSecurityDialect());
        return springTemplateEngine;
    }
// onetime generation of generic user

//    @Bean
//    UserDetailsManager users(@Autowired DataSource dataSource) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//                .roles("USER", "ADMIN")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        users.createUser(user);
//        users.createUser(admin);
//        return users;
//    }
}
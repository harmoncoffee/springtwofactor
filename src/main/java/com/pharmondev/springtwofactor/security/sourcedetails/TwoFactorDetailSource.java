package com.pharmondev.springtwofactor.security.sourcedetails;
//In spring 6 we need to use jakarta not javax
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * The TwoFactorDetailSource class implements the AuthenticationDetailsSource interface and provides
 * a method to build WebAuthenticationDetails based on HttpServletRequest. It is used for two-factor
 * authentication in the authentication process.
 */
@Component
public class TwoFactorDetailSource implements
        AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new TwoFactorDetails(context);
    }
}

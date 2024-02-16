package com.pharmondev.springtwofactor.security.sourcedetails;

//import jakarta.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * The TwoFactorDetails class extends the WebAuthenticationDetails class and represents the details
 * related to a user's two-factor authentication.
 */
public class TwoFactorDetails extends WebAuthenticationDetails {

    private String verificationCode;
    private String username;
    public TwoFactorDetails(HttpServletRequest request) {
        super(request);
        verificationCode = request.getParameter("code");
        username = request.getParameter("username");
    }

    public String getVerificationCode() {
        return verificationCode;
    }
    public String getUsername() {
        return username;
    }
}

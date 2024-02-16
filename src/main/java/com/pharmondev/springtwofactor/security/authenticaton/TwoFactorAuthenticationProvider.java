package com.pharmondev.springtwofactor.security.authenticaton;

import com.pharmondev.springtwofactor.security.dao.UserDao;
import com.pharmondev.springtwofactor.security.sourcedetails.TwoFactorDetails;
import com.pharmondev.springtwofactor.security.sourcedetails.TwoFactorDetailsService;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

public class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {
    // User Data Access Object
    private UserDao userTotpDao;

    // Service for dealing with Two-Factor authentication details
    private TwoFactorDetailsService service;
    // Method to authenticate users with two-factor authentication

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        // Extract verification code from authentication details

        String verificationCode
                = ((TwoFactorDetails) auth.getDetails())
                .getVerificationCode();
        // Load user details by username
        User user = (User) service.loadUserByUsername(auth.getName());

        String username
                = user.getUsername();
        // If user does not exist throw exception
        if ((user == null)) {
            throw new BadCredentialsException("Invalid username or password");
        }
        // Retrieve user's secret token
        String secret = userTotpDao.getUserTotpToken(username);
        //Validate Token
        Totp totp = new Totp(secret);
        if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
            throw new BadCredentialsException("Invalid verfication code");
        }
        // Perform usual authentication
        Authentication result = super.authenticate(auth);
        // Return authentication token
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }
    // Method for code validation
    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    // Check if authentication provider supports UsernamePasswordAuthenticationToken
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public void setUserDao(UserDao userDao) {
        this.userTotpDao = userDao;
    }

    public void setService(TwoFactorDetailsService service) {
        this.service = service;
    }
}

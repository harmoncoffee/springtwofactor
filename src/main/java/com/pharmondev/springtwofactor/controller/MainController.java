package com.pharmondev.springtwofactor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
public class MainController {
    //Note that this is not used yet from the UI. But the goal in future iterations would be to reference this for generating a token automatically.
    //And having a way for the user to view the QR code
    //For demo purposes this is not functional but to show intent.
    public static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public String generateQRUrl(String username, String secret) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format(
                        "otpauth://totp/pharmondev:%s?secret=%s&issuer=pharmondev",
                        username, secret),
                "UTF-8");
    }

    @GetMapping("/getQRUrl/{username}/{secret}/")
    public String getQrUrl(@PathVariable String username,@PathVariable String secret) throws UnsupportedEncodingException {
        return generateQRUrl(username,secret);
    }

}

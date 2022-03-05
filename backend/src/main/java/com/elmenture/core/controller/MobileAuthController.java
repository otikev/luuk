package com.elmenture.core.controller;

import com.elmenture.core.SignInResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class MobileAuthController {
    String CLIENT_ID = "323086391588-rvm5c6492ngk4c8aclr8q7l08tqv555n.apps.googleusercontent.com";

    @PostMapping("/tokensignin")
    public SignInResponse tokenSignin(@Valid @RequestParam MultiValueMap<String,String> idTokenString) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setIssuer("https://accounts.google.com")
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        SignInResponse response = new SignInResponse();
        try {
            String token = idTokenString.get("idToken").get(0);
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                // Use or store profile information
                // ...

                response.success = true;
            } else {
                System.out.println("Invalid ID token.");
                response.success = false;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SignInResponse();
    }
}

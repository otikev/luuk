package com.elmenture.core.controller;

import com.elmenture.core.SignInResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.json.JSONObject;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class MobileAuthController {
    String GOOGLE_CLIENT_ID = "323086391588-rvm5c6492ngk4c8aclr8q7l08tqv555n.apps.googleusercontent.com";

    @PostMapping("/facebooksignin")
    public SignInResponse facebookTokenSignin(@Valid @RequestParam MultiValueMap<String, String> idTokenString) {
        try {
            String userToken = idTokenString.get("userToken").get(0);
            SignInResponse response = new SignInResponse();
            String clientId = "684178072610069";
            String clientSecret = "f1abeabfe94faa3c28c4a5083758fb82";
            String url = "https://graph.facebook.com/oauth/access_token?client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";

            RestTemplate restTemplate = new RestTemplate();
            String resultAppToken = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = new JSONObject(resultAppToken);
            String accessToken = String.valueOf(jsonObject.get("access_token"));

            String url2 = "https://graph.facebook.com/debug_token?input_token=" + userToken + "&access_token=" + accessToken;
            String resultUserId = restTemplate.getForObject(url2, String.class);
            JSONObject json = new JSONObject(resultUserId);
            JSONObject metadata = (JSONObject) json.get("metadata");
            String userId = String.valueOf(metadata.get("user_id"));
            if (userId != null && !userId.isEmpty()) {
                response.success = true;
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SignInResponse();
    }

    @PostMapping("/googlesignin")
    public SignInResponse googleTokenSignin(@Valid @RequestParam MultiValueMap<String, String> idTokenString) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setIssuer("https://accounts.google.com")
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
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

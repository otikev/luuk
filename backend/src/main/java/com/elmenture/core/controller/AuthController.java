package com.elmenture.core.controller;

import com.elmenture.core.SignInResponse;
import com.elmenture.core.model.User;
import com.elmenture.core.repository.UserRepository;
import com.elmenture.core.utils.Properties;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

import static com.elmenture.core.SocialAccountType.FACEBOOK;
import static com.elmenture.core.SocialAccountType.GOOGLE;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/facebooksignin")
    public ResponseEntity<SignInResponse> facebookTokenSignin(@Valid @RequestParam MultiValueMap<String, String> idTokenString) {
        String userToken = idTokenString.get("userToken").get(0);
        SignInResponse response = new SignInResponse();
        String url = "https://graph.facebook.com/oauth/access_token?client_id=" + Properties.facebookAppId + "&client_secret=" + Properties.facebookAppSecret + "&grant_type=client_credentials";

        RestTemplate restTemplate = new RestTemplate();
        String resultAppToken = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = new JSONObject(resultAppToken);
        String accessToken = String.valueOf(jsonObject.get("access_token"));

        String url2 = "https://graph.facebook.com/debug_token?input_token=" + userToken + "&access_token=" + accessToken;
        String resultUserId = restTemplate.getForObject(url2, String.class);
        JSONObject json = new JSONObject(resultUserId);
        String userId = json.getJSONObject("data").getString("user_id");
        if (userId != null && !userId.isEmpty()) {
            User user = userRepository.findByUsernameAndSocialAccountType(userId, FACEBOOK.value());

            if (user == null) {
                response.isNewAccount = true;
                user = new User();
                //user.setFirstName(String.valueOf(idTokenString.get("firstName")));
                //user.setLastName(String.valueOf(idTokenString.get("lastName")));
                user.setUsername(userId);
                user.setSocialAccountType(FACEBOOK.value());
                //boolean emailVerified = payload.getEmailVerified();
            }
            String auth = createSession(user);
            response.authToken = auth;
            response.success = true;
        } else {
            System.out.println("Invalid user token.");
            response.success = false;
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/googlesignin")
    public ResponseEntity<SignInResponse> googleTokenSignin(@Valid @RequestParam MultiValueMap<String, String> idTokenString) throws GeneralSecurityException, IOException {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setIssuer("https://accounts.google.com")
                .setAudience(Collections.singletonList(Properties.googleClientId))
                .build();
        SignInResponse response = new SignInResponse();
        String token = idTokenString.get("idToken").get(0);
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            User user = userRepository.findByUsernameAndSocialAccountType(userId, GOOGLE.value());
            if (user == null) {
                response.isNewAccount = true;
                user = new User();
                user.setEmail(payload.getEmail());
                user.setFirstName(String.valueOf(payload.get("given_name")));
                user.setLastName(String.valueOf(payload.get("family_name")));
                user.setUsername(payload.getSubject());
                user.setSocialAccountType(GOOGLE.value());
                //boolean emailVerified = payload.getEmailVerified();
            }
            String auth = createSession(user);
            response.authToken = auth;
            response.success = true;
        } else {
            System.out.println("Invalid ID token.");
            response.success = false;
        }

        return ResponseEntity.ok(response);
    }

    private String createSession(User user) {
        UUID uuid = UUID.randomUUID();
        user.setAuthToken(uuid.toString());
        userRepository.save(user);
        return user.getAuthToken();
    }
}

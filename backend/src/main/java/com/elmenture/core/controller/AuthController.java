package com.elmenture.core.controller;

import com.elmenture.core.model.TagProperty;
import com.elmenture.core.model.User;
import com.elmenture.core.payload.*;
import com.elmenture.core.repository.TagPropertyRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

import static com.elmenture.core.utils.SocialAccountType.FACEBOOK;
import static com.elmenture.core.utils.SocialAccountType.GOOGLE;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController{

    //FIXME: Hardcoding for now. This will eventually be replaced with user roles functionality
    private String[] STAFF = {
            "oti.kevin@gmail.com",
            "njihiamuchai@gmail.com",
            "aycewhispero@gmail.com",
            "kellen.kinyua@gmail.com"
    };

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagPropertyRepository tagPropertyRepository;


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
                response.setNewAccount(true);
                user = new User();
                //user.setFirstName(String.valueOf(idTokenString.get("firstName")));
                //user.setLastName(String.valueOf(idTokenString.get("lastName")));
                user.setUsername(userId);
                user.setSocialAccountType(FACEBOOK.value());
                //boolean emailVerified = payload.getEmailVerified();
            }
            response = createSigninResponse(response, user);
        } else {
            System.out.println("Invalid user token.");
            response.setSuccess(false);
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
                response.setNewAccount(true);
                user = new User();
                user.setEmail(payload.getEmail());
                user.setFirstName(String.valueOf(payload.get("given_name")));
                user.setLastName(String.valueOf(payload.get("family_name")));
                user.setUsername(payload.getSubject());
                user.setSocialAccountType(GOOGLE.value());
                //boolean emailVerified = payload.getEmailVerified();
            }

            response = createSigninResponse(response, user);
        } else {
            System.out.println("Invalid ID token.");
            response.setSuccess(false);
        }

        System.out.println("Signin success!!");
        return ResponseEntity.ok(response);
    }

    SignInResponse createSigninResponse(SignInResponse response, User user) {
        UserMeasurementsDto actualMeasurements = new UserMeasurementsDto();

        BodyMeasurementsDto bodyMeasurementsDto = new BodyMeasurementsDto();
        if(user.getBodyMeasurement() != null){
            bodyMeasurementsDto.setChest_cm(user.getBodyMeasurement().getChest_cm());
            bodyMeasurementsDto.setWaist_cm(user.getBodyMeasurement().getWaist_cm());
            bodyMeasurementsDto.setHips_cm(user.getBodyMeasurement().getHips_cm());
        }
        actualMeasurements.setBodyMeasurements(bodyMeasurementsDto);


        ClothingSizeDto clothingSizeDto = new ClothingSizeDto();
        if(user.getClothingSize() != null){
            clothingSizeDto.setInternational(user.getClothingSize().getInternational());
            clothingSizeDto.setUs(user.getClothingSize().getUs());
            clothingSizeDto.setUk(user.getClothingSize().getUk());
            clothingSizeDto.setEu(user.getClothingSize().getEu());
        }
        actualMeasurements.setClothingSizes(clothingSizeDto);

        SignInResponse.FemaleSize femaleSize = new SignInResponse.FemaleSize();
        femaleSize.setDress(getDressSizesForUser(user));

        String auth = createSession(user);
        response.setSessionKey(auth);
        response.setSuccess(true);
        response.setActualMeasurements(actualMeasurements);
        response.setFemaleSize(femaleSize);
        response.setContactPhoneNumber(user.getContactPhoneNumber());
        response.setPhysicalAddress(user.getPhysicalAddress());
        response.setClothingRecommendations(user.getClothingRecommendations());
        response.setName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        if (isStaff(user)) {
            response.setStaff(true);
            response.setS3AccessKeyId(Properties.amazonS3AccessKeyId);
            response.setS3SecretKeyId(Properties.amazonS3SecretKeyId);
        }

        List<TagProperty> tagProperties = tagPropertyRepository.findAll();

        List<TagPropertyDto> tagPropertiesList = new ArrayList<>();
        for(TagProperty tagProperty : tagProperties){
            TagPropertyDto dto = new TagPropertyDto();
            dto.setId(tagProperty.getId());
            dto.setValue(tagProperty.getValue());
            tagPropertiesList.add(dto);
        }
        response.setTagProperties(tagPropertiesList);
        return response;
    }

    private boolean isStaff(User user) {
        List<String> staffList = new ArrayList<>(Arrays.asList(STAFF));
        return staffList.contains(user.getEmail());
    }

    private String createSession(User user) {
        user.setAuthToken(UUID.randomUUID().toString());
        userRepository.save(user);

        String sessionKey = UUID.randomUUID().toString().replace("-", "") + ":" + user.getUsername() + ":" + user.getAuthToken();
        String base64String = Base64.getEncoder().encodeToString(sessionKey.getBytes(StandardCharsets.UTF_8));
        return base64String;
    }
}
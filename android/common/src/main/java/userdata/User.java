package userdata;

import java.util.List;

import models.SignInResponse;
import models.TagProperty;

public class User {
    String[] tags;
    public SignInResponse userDetails;
    public String sessionKey;

    private static User user = new User();

    public static User getCurrent() {
        return User.user;
    }

    public void setUserDetails(SignInResponse userDetails) {
        this.userDetails = userDetails;
        this.sessionKey = userDetails.getSessionKey();
        tags = new String[userDetails.getTagProperties().size()];
        int i = 0;
        for (TagProperty tagProperty : userDetails.getTagProperties()) {
            tags[i] = tagProperty.getValue();
            i++;
        }
    }

    public static boolean hasSignedInUser() {
        return user.userDetails != null;
    }

    public void logout() {
        userDetails = null;
    }

    public List<TagProperty> getTagProperties() {
        return userDetails.getTagProperties();
    }

    public TagProperty getTagProperty(Long id) {
        for (TagProperty tagProperty : userDetails.getTagProperties()) {
            if (tagProperty.getId().longValue() == id.longValue()) {
                return tagProperty;
            }
        }
        return null;
    }

    public TagProperty getTagProperty(String s) {
        for (TagProperty tagProperty : userDetails.getTagProperties()) {
            if (tagProperty.getValue().equalsIgnoreCase(s)) {
                return tagProperty;
            }
        }
        return null;
    }

    public String[] getTags() {
        return tags;
    }
}
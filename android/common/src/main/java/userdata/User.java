package userdata;

import java.util.List;

import models.SignInResponse;
import models.TagProperty;

public class User {

    public SignInResponse userDetails;

    private static User user = new User();

    public static User getCurrent() {
        return User.user;
    }

    public void setUserDetails(SignInResponse userDetails) {
        this.userDetails = userDetails;
    }

    public static boolean hasSignedInUser() {
        return user.userDetails != null;
    }

    public void logout() {
        userDetails = null;
    }

    public List<TagProperty> getTags(){
        return userDetails.getTagProperties();
    }
}

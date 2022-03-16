package userdata;

import com.luuk.common.models.SignInResponse;

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
}

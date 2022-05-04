package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecureUtils {
    private static String SECRET_KEY = "PpjUOmijJ7s7asn6iVTbHeoQ";
    private static String INIT_VECTOR = "OXw4umKgQxDelzpN";

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getUserSessionKey(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("LUUK_PREFERENCES", Context.MODE_PRIVATE);
        String val = sharedPref.getString("session_key", null);
        if (val == null) {
            return null;
        }
        String decrypted = decrypt(val);
        return decrypted;
    }

    public static void setUserSessionKey(Context context, String value) {
        String encrypted = null;
        if (value != null) {
            encrypted = encrypt(value);
        }

        SharedPreferences sharedPref = context.getSharedPreferences("LUUK_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("session_key", encrypted);
        editor.apply();
    }
}
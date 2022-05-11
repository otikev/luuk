package utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogUtils {

    private static final String LOG_PREFIX = "LUUK-";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 30;
    public SimpleDateFormat dateFormat;
    private String prefixedTag;

    public LogUtils(Class aClass) {
        this.dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        this.prefixedTag = makeLogTag(aClass.getSimpleName());
    }

    private static String makeLogTag(String str) {
        return str.length() > 30 - LOG_PREFIX_LENGTH ? LOG_PREFIX + str.substring(0, 30 - LOG_PREFIX_LENGTH - 1) : LOG_PREFIX + str;
    }

    public void d(String message) {
        try {
            Log.d(this.prefixedTag, message);
        } catch (Exception var3) {
            this.e(var3);
        }
    }

    public void v(String message) {
        try {
            Log.v(this.prefixedTag, message);
        } catch (Exception var3) {
            this.e(var3);
        }
    }

    public void i(String message) {
        try {
            Log.i(this.prefixedTag, message);
        } catch (Exception var3) {
            this.e(var3);
        }
    }

    public void w(String message) {
        try {
            Log.w(this.prefixedTag, message);
        } catch (Exception var3) {
            this.e(var3);
        }
    }

    public void e(Throwable cause) {
        e(cause.getMessage(), cause);
    }

    public void e(String message, Throwable cause) {
        Log.e(this.prefixedTag, message, cause);
    }
}

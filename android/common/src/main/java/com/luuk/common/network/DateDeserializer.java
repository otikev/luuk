package com.luuk.common.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.format.DateTimeFormat;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;


public class DateDeserializer implements JsonDeserializer<Date> {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String[] DATE_FORMATS = {"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", "yyyy-MM-dd'T'HH:mm:ssZ", "E, dd MMM yyyy HH:mm:ss 'GMT'", "yyyy-MM-dd"};

    @Override
    public Date deserialize(final JsonElement jsonElement, final Type typeOF,
                            final JsonDeserializationContext context) throws JsonParseException {
        for (final String format : DATE_FORMATS) {
            try {
                return date(jsonElement.getAsString(), format);
            } catch (Exception ignored) {
            }
        }
        throw new JsonParseException(
                "Unparsable date: \"" + jsonElement.getAsString() + "\". Supported formats: " +
                        Arrays.toString(DATE_FORMATS));
    }

    private Date date(final String date, final String format) {
        return DateTimeFormat.forPattern(format).parseDateTime(date).toDate();
    }
}
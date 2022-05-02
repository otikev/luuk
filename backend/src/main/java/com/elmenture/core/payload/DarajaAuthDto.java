package com.elmenture.core.payload;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DarajaAuthDto {
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("expires_in")
    String expiresIn;
}

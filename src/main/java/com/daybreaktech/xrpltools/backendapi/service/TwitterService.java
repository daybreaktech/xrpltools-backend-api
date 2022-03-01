package com.daybreaktech.xrpltools.backendapi.service;

import org.apache.commons.codec.binary.Base64;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TwitterService {

    @Value("${twitter.auth.secretKey}")
    private String mySecretKey;

    public String validateCrc(String crcToken) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(mySecretKey.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(crcToken.getBytes("UTF-8")));
        Map<String, String> response = new HashMap<>(1);
        response.put("response_token", "sha256="+hash);
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.toString();
    }

}

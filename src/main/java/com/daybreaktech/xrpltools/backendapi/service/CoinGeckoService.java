package com.daybreaktech.xrpltools.backendapi.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


@Service
public class CoinGeckoService {

    Logger logger = LoggerFactory.getLogger(CoinGeckoService.class);

    public String getCurrentXrpPrice() {
        String price = "";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(
                    "https://api.coingecko.com/api/v3/simple/price?ids=ripple&vs_currencies=usd",
                    String.class);

            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            BigDecimal bigDecimal = new BigDecimal(jsonObject.getAsJsonObject("ripple").get("usd").getAsString());
            price = "$" + bigDecimal.setScale(2, BigDecimal.ROUND_UP).toString();
        } catch (Exception e) {
            logger.error("Error parsing getCurrentXrpPrice" + e);
        }

        return price;
    }

}

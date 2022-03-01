package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("${v1API}/webhook/twitter")
@CrossOrigin("*")
public class TwitterWebhookController {

    @Autowired
    private TwitterService twitterService;

    @GetMapping
    public ResponseEntity validate(@RequestParam("crc_token") String token)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("CRC Token received: " + token);
        return ResponseEntity.ok(twitterService.validateCrc(token));
    }

    @PostMapping
    public void consumeEvent(@RequestBody String json) {
        System.out.println("Event sent: " + json);
    }

}

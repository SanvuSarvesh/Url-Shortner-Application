package com.example.UrlShortner.controller;

import com.example.UrlShortner.common.BaseResponse;
import com.example.UrlShortner.common.UrlRequest;
import com.example.UrlShortner.common.UrlResponse;
import com.example.UrlShortner.model.Url;
import com.example.UrlShortner.service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UrlController {

    @Autowired
    private UrlService urlService;

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlRequest urlRequest) {
        Long startTime = System.currentTimeMillis();
        logger.info("Inside the UrlController : generateShortLink Method.");
        BaseResponse baseResponse = urlService.generateShortLink(urlRequest);
        logger.info("Inside the UrlController : generateShortLink Method, time taken by the api is : {}",startTime);
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        Long startTime = System.currentTimeMillis();
        logger.info("Inside the UrlController : generateShortLink Method.");
        BaseResponse baseResponse = urlService.getEncodedUrl(shortLink);
        Url urlInfo = (Url)baseResponse.getPayLoad();
        String url = urlInfo.getOriginalUrl();
        response.sendRedirect(url);
        logger.info("Inside the UrlController : redirectToOriginalUrl Method, time taken by the api is : {}",startTime);
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

}

package com.example.UrlShortner.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlResponse {

    private String originalUrl;

    private String shortLink;

    private LocalDateTime expirationDate;

}

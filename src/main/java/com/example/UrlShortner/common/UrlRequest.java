package com.example.UrlShortner.common;

import lombok.Data;

@Data
public class UrlRequest {

    private String url;
    private String expirationDate;  //optional

}

package com.example.UrlShortner.service;

import com.example.UrlShortner.common.BaseResponse;
import com.example.UrlShortner.common.UrlRequest;
import com.example.UrlShortner.model.Url;

public interface UrlService {

    BaseResponse generateShortLink(UrlRequest urlRequest);

    BaseResponse persistShortLink(Url url);

    BaseResponse getEncodedUrl(String url);

    BaseResponse  deleteShortLink(Url url);

}

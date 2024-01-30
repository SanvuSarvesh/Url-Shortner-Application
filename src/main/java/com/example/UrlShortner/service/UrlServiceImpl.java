package com.example.UrlShortner.service;

import com.example.UrlShortner.common.BaseResponse;
import com.example.UrlShortner.common.UrlRequest;
import com.example.UrlShortner.controller.UrlController;
import com.example.UrlShortner.exception.controller.UrlExceptions;
import com.example.UrlShortner.model.Url;
import com.example.UrlShortner.repository.UrlRepository;
import com.google.common.hash.Hashing;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Service
public class UrlServiceImpl implements UrlService{

    @Autowired
    private UrlRepository urlRepository;

    private static final Logger logger = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Override
    public BaseResponse generateShortLink(UrlRequest urlRequest) {
        logger.info("Inside the UrlController : generateShortLink Method.");
        BaseResponse baseResponse = new BaseResponse();
        String errorMessage = "";
        if(StringUtils.isNotEmpty(urlRequest.getUrl())) {
            String encodedUrl = encodeUrl(urlRequest.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreatedAt(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlRequest.getUrl());
            urlToPersist.setShortUrl(encodedUrl);
            urlToPersist.setExpireOn(getExpirationDate(urlRequest.getExpirationDate(),urlToPersist.getCreatedAt()));
            BaseResponse urlToRet = persistShortLink(urlToPersist);
            if(urlToRet != null){
                baseResponse.setStatus(HttpStatus.OK.toString());
                baseResponse.setSuccess(true);
                baseResponse.setPayLoad(urlToRet);
                baseResponse.setMessage("Short URL is created.");
                return baseResponse;
            }
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
            baseResponse.setSuccess(false);
            baseResponse.setMessage("There's some problem while creating short url, please try again.");
            logger.info("Inside the UrlServiceImpl : generateShortLink Method, time taken by the api is : {}",errorMessage);
            return baseResponse;
        }
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
        baseResponse.setSuccess(false);
        baseResponse.setMessage("There's some problem while creating short url, please try again.");
        logger.info("Inside the UrlServiceImpl : generateShortLink Method, time taken by the api is : {}",errorMessage);
        return baseResponse;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime createdAt) {
        if(StringUtils.isBlank(expirationDate)) {
            return createdAt.plusSeconds(600);
        }
        return LocalDateTime.parse(expirationDate);
    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodedUrl;
    }

    @Override
    public BaseResponse persistShortLink(Url url) {
        BaseResponse baseResponse = new BaseResponse();
        Url urlToRet = urlRepository.save(url);
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setSuccess(true);
        baseResponse.setPayLoad(urlToRet);
        baseResponse.setMessage("Short URL is created.");
        return baseResponse;
    }

    @Override
    public BaseResponse getEncodedUrl(String shortUrl) {
        BaseResponse baseResponse = new BaseResponse();
        Optional<Url> urlToRet = urlRepository.findByShortUrl(shortUrl);
        if(urlToRet.isEmpty()){
            logger.info("Oops!!! shortUrl doesn't exist.");
            throw new UrlExceptions("Given url is either expired or invalid, try generating new one",HttpStatus.NOT_FOUND);
        }
        Url url = urlToRet.get();
        if(url.getExpireOn().isBefore(LocalDateTime.now())){
            deleteShortLink(url);
            logger.info("Oops!!! Given Url is expired.");
            throw new UrlExceptions("Given url is expired, try generating new one.",HttpStatus.BAD_REQUEST);
        }
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setSuccess(true);
        baseResponse.setPayLoad(url);
        baseResponse.setMessage("Encoded shortUrl is fetched.");
        return baseResponse;
    }

    @Override
    public BaseResponse deleteShortLink(Url url) {
        BaseResponse baseResponse = new BaseResponse();
        urlRepository.delete(url);
        baseResponse.setStatus(HttpStatus.OK.toString());
        baseResponse.setSuccess(true);
        baseResponse.setMessage("Url is deleted.");
        return baseResponse;
    }
}

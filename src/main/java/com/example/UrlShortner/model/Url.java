package com.example.UrlShortner.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "url_info")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String originalUrl;

    private String shortUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expireOn;

}

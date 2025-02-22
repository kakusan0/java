package com.example.demo.entity.post;

import lombok.Data;

@Data
public class ZipCode {
    private Long id;
    private String zipCode;
    private String prefectureKana;
    private String cityKana;
    private String townAreaKana;
    private String prefecture;
    private String city;
    private String townArea;
}
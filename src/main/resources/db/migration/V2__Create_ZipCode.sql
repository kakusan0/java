CREATE TABLE ZipCode
(
    id              INT AUTO_INCREMENT
        PRIMARY KEY,
    zip_code        VARCHAR(7)  NULL,
    prefecture_kana VARCHAR(7)  NULL,
    city_kana       VARCHAR(23) NULL,
    town_area_kana  VARCHAR(42) NULL,
    prefecture      VARCHAR(5)  NULL,
    city            VARCHAR(11) NULL,
    town_area       VARCHAR(37) NULL
) COLLATE = utf8mb4_unicode_ci;
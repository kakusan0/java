UPDATE ZipCode
SET zip_code = LPAD(zip_code, 7, '0')
WHERE LENGTH(zip_code) < 7;
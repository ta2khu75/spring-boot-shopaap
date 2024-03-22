DECLARE @columnCount INT
DECLARE @alterStatement NVARCHAR(MAX)

SELECT @columnCount = COUNT(*)
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'tokens'
AND TABLE_SCHEMA = 'ShopApp'
AND COLUMN_NAME = 'refresh_token';

SET @alterStatement = 
    CASE 
        WHEN @columnCount = 0 THEN 'ALTER TABLE tokens ADD refresh_token VARCHAR(255) DEFAULT '''';'
        ELSE ''
    END;

EXEC sp_executesql @alterStatement;

SELECT @columnCount = COUNT(*)
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'tokens'
AND TABLE_SCHEMA = 'ShopApp'
AND COLUMN_NAME = 'refresh_expiration_data';

SET @alterStatement = 
    CASE 
        WHEN @columnCount = 0 THEN 'ALTER TABLE tokens ADD refresh_expiration_data DATETIME;'
        ELSE ''
    END;

EXEC sp_executesql @alterStatement;

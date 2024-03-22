DECLARE @columnCount INT;

-- Check if the column exists
SELECT @columnCount = COUNT(*)
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'tokens'
AND TABLE_SCHEMA = 'shopapp'
AND COLUMN_NAME = 'is_mobile';

-- If the column does not exist, add it
DECLARE @alterStatement NVARCHAR(MAX);
SET @alterStatement = 
    CASE WHEN @columnCount = 0 THEN 
        'ALTER TABLE tokens ADD is_mobile TINYINT DEFAULT 0;'
    ELSE 
        ''
    END;

-- Execute the ALTER TABLE statement if necessary
IF @alterStatement <> ''
BEGIN
    EXEC sp_executesql @alterStatement;
END;

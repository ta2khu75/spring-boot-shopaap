ALTER TABLE categories 
ALTER COLUMN name NVARCHAR(50);
ALTER TABLE categories 
ADD CONSTRAINT unique_name UNIQUE (name);

ALTER TABLE products DROP CONSTRAINT CHK_PriceGreaterThanZero;
ALTER TABLE products ALTER COLUMN price DECIMAL(10,2);
ALTER TABLE products ADD CONSTRAINT CHK_PriceGreaterThanZero CHECK (price > 0);

ALTER TABLE products 
ALTER COLUMN thumbnail NVARCHAR(255);

ALTER TABLE users 
ALTER COLUMN password VARCHAR(60);
ALTER TABLE users 
ADD CONSTRAINT df_role_id DEFAULT 1 FOR role_id;

ALTER TABLE products DROP CONSTRAINT CHK_PriceGreaterThanZero;
ALTER TABLE products ALTER COLUMN price DECIMAL(10,2);

ALTER TABLE order_details 
ADD CONSTRAINT df_number_of_products DEFAULT 1 FOR number_of_products;
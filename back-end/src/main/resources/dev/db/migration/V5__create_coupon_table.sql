IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'coupons')
BEGIN
    CREATE TABLE coupons (
        id INT PRIMARY KEY IDENTITY,
        code NVARCHAR(50) NOT NULL,
        active BIT NOT NULL DEFAULT 1
    );
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'coupon_id' AND Object_ID = Object_ID(N'orders'))
BEGIN
    ALTER TABLE orders
    ADD coupon_id INT,
    CONSTRAINT fk_orders_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id);
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = N'coupon_id' AND Object_ID = Object_ID(N'order_details'))
BEGIN
    ALTER TABLE order_details
    ADD coupon_id INT,
    CONSTRAINT fk_order_details_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id);
END

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'coupon_conditions')
BEGIN
    CREATE TABLE coupon_conditions (
        id INT PRIMARY KEY IDENTITY,
        coupon_id INT NOT NULL,
        attribute NVARCHAR(255) NOT NULL,
        operator NVARCHAR(10) NOT NULL,
        value NVARCHAR(255) NOT NULL,
        discount_amount DECIMAL(5, 2) NOT NULL,
        FOREIGN KEY (coupon_id) REFERENCES coupons (id)
    );
END

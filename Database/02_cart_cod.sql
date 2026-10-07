USE DB_BookStore;
GO

IF OBJECT_ID('dbo.orders', 'U') IS NULL
BEGIN
    CREATE TABLE orders
    (
        order_id INT IDENTITY(1,1) PRIMARY KEY,
        userid INT NOT NULL,
        customer_name NVARCHAR(100) NOT NULL,
        phone VARCHAR(20) NOT NULL,
        shipping_address NVARCHAR(300) NOT NULL,
        note NVARCHAR(500) NULL,
        payment_method VARCHAR(20) NOT NULL DEFAULT 'COD',
        payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
        order_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
        total_amount DECIMAL(18,2) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_orders_users FOREIGN KEY (userid) REFERENCES users(id),
        CONSTRAINT CK_orders_payment_method CHECK (payment_method IN ('COD')),
        CONSTRAINT CK_orders_payment_status CHECK (payment_status IN ('UNPAID','PAID')),
        CONSTRAINT CK_orders_status CHECK (order_status IN ('PENDING','CONFIRMED','SHIPPING','COMPLETED','CANCELLED'))
    );
END;
GO

IF OBJECT_ID('dbo.order_items', 'U') IS NULL
BEGIN
    CREATE TABLE order_items
    (
        order_item_id INT IDENTITY(1,1) PRIMARY KEY,
        order_id INT NOT NULL,
        bookid INT NOT NULL,
        quantity INT NOT NULL,
        unit_price DECIMAL(18,2) NOT NULL,
        subtotal DECIMAL(18,2) NOT NULL,
        CONSTRAINT FK_order_items_orders FOREIGN KEY (order_id) REFERENCES orders(order_id),
        CONSTRAINT FK_order_items_books FOREIGN KEY (bookid) REFERENCES books(bookid),
        CONSTRAINT CK_order_items_quantity CHECK (quantity > 0)
    );
END;
GO

SELECT * FROM orders ORDER BY order_id DESC;
SELECT * FROM order_items ORDER BY order_item_id DESC;
GO

CREATE DATABASE DB_BookStore;
GO

USE DB_BookStore;
GO

CREATE TABLE users
(
    id INT IDENTITY(1,1) PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    fullname NVARCHAR(50) NULL,
    phone INT NULL,
    passwd VARCHAR(32) NOT NULL,
    signup_date DATETIME NULL,
    last_login DATETIME NULL,
    is_admin BIT NULL
);
GO

CREATE TABLE author
(
    author_id INT IDENTITY(1,1) PRIMARY KEY,
    author_name VARCHAR(100) NULL,
    date_of_birth DATE NULL
);
GO

CREATE TABLE books
(
    bookid INT IDENTITY(1,1) PRIMARY KEY,
    isbn INT NULL,
    title VARCHAR(200) NULL,
    publisher VARCHAR(100) NULL,
    price DECIMAL(6,2) NULL,
    description TEXT NULL,
    publish_date DATE NULL,
    cover_image VARCHAR(100) NULL,
    quantity INT NULL
);
GO

CREATE TABLE book_author
(
    bookid INT NOT NULL,
    author_id INT NOT NULL,

    CONSTRAINT PK_book_author PRIMARY KEY (bookid, author_id),

    CONSTRAINT FK_book_author_books
        FOREIGN KEY (bookid)
        REFERENCES books(bookid),

    CONSTRAINT FK_book_author_author
        FOREIGN KEY (author_id)
        REFERENCES author(author_id)
);
GO

CREATE TABLE rating
(
    userid INT NOT NULL,
    bookid INT NOT NULL,
    rating TINYINT NULL,
    review_text TEXT NULL,

    CONSTRAINT PK_rating PRIMARY KEY (userid, bookid),

    CONSTRAINT FK_rating_users
        FOREIGN KEY (userid)
        REFERENCES users(id),

    CONSTRAINT FK_rating_books
        FOREIGN KEY (bookid)
        REFERENCES books(bookid),

    CONSTRAINT CK_rating_value
        CHECK (rating BETWEEN 1 AND 5)
);
GO



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
GO

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
GO

SELECT * FROM users;
SELECT * FROM author;
SELECT * FROM books;
SELECT * FROM book_author;
SELECT * FROM rating;
GO

SELECT
    a.author_name,
    b.bookid,
    b.isbn,
    b.title,
    b.publisher,
    b.price,
    b.publish_date,
    b.cover_image,
    b.quantity
FROM author a
JOIN book_author ba ON a.author_id = ba.author_id
JOIN books b ON ba.bookid = b.bookid
ORDER BY a.author_id, b.bookid;
GO

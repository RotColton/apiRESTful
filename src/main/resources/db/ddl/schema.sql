CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price FLOAT NOT NULL CHECK (price > 0),
    category VARCHAR(255) NOT NULL
);

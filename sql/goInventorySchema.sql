CREATE DATABASE IF NOT EXISTS goinventory
DEFAULT CHARACTER SET 'utf8'
DEFAULT COLLATE 'utf8_general_ci';
USE goinventory;

DROP TABLE users;

CREATE TABLE IF NOT EXISTS users (
u_id INT AUTO_INCREMENT PRIMARY KEY,
name varchar(30) NOT NULL,
surname varchar(30)NOT NULL UNIQUE,
email varchar(50) UNIQUE NOT NULL,
password varchar(30) NOT NULL,
possition varchar(10) NOT NULL
);

INSERT INTO users(name,surname,email,password,possition) VALUES ('defaultUser','user','usergoinventory@gmail.com','12345','user');
SELECT * FROM users;

CREATE TABLE IF NOT EXISTS products (
p_id INT AUTO_INCREMENT PRIMARY KEY,
name varchar(30) NOT NULL DEFAULT 'UNNAMED PRODUCT',
quantity INT NOT NULL DEFAULT 0,
sellingPrice INT NOT NULL DEFAULT 0
);

SELECT * FROM products;

DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS orders (
o_id INT AUTO_INCREMENT PRIMARY KEY,
p_id INT NOT NULL,
foreign key (p_id) references products(p_id),
quantity INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orderhistory (
o_id INT AUTO_INCREMENT PRIMARY KEY,
product varchar(30) NOT NULL DEFAULT 'UNNAMED PRODUCT',
quantity INT NOT NULL DEFAULT 0
);

SELECT * FROM orders;
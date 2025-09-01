-- MySQL Database Schema for Don Mac Kiosk System
-- Execute these SQL commands in your MySQL database

-- Create database (if it doesn't exist)
CREATE DATABASE IF NOT EXISTS donmac_kiosk;
USE donmac_kiosk;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'manager', 'cashier', 'clerk') DEFAULT 'cashier',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category VARCHAR(50) NOT NULL,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('pending', 'completed', 'cancelled') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Insert sample admin user
INSERT INTO users (first_name, last_name, username, password, role) VALUES 
('Admin', 'User', 'admin', 'admin123', 'admin');

-- Insert sample categories
INSERT INTO categories (name, created_by) VALUES 
('Drinks', 'admin'),
('Specials', 'admin');

-- Insert sample products
INSERT INTO products (name, description, price, stock, category, image_path) VALUES 
('Iced Caramel Macchiato', 'Sweet caramel with espresso and milk over ice', 39.00, 100, 'Drinks', '/ui/Images/product_images/iced_caramel.png'),
('Spanish Latte', 'Rich espresso with condensed milk', 39.00, 100, 'Drinks', '/ui/Images/product_images/spanish_latte.png'),
('Black Forest', 'Chocolatey coffee delight', 39.00, 100, 'Drinks', '/ui/Images/product_images/black_forest.png'),
('Donya Berry', 'Berry-infused coffee creation', 39.00, 100, 'Drinks', '/ui/Images/product_images/donya_berry.png'),
('Don Matchatos', 'Matcha-based specialty drink', 39.00, 100, 'Drinks', '/ui/Images/product_images/don_matchatos.png'),
('Matcha Berry', 'Refreshing matcha with berries', 39.00, 100, 'Drinks', '/ui/Images/product_images/matcha_berry.png'),
('Oreo Coffee', 'Coffee with Oreo cookie goodness', 39.00, 100, 'Drinks', '/ui/Images/product_images/ore_coffee.png'),
('Don Darko', 'Dark chocolate coffee blend', 39.00, 100, 'Drinks', '/ui/Images/product_images/don_darko.png'),
('Pure Ube', 'Ube-flavored specialty drink', 39.00, 100, 'Specials', '/ui/Images/product_images/pure_ube.png'),
('Hot Caramel', 'Warm caramel coffee delight', 39.00, 100, 'Specials', '/ui/Images/product_images/hot_caramel.png'),
('Hot Darko', 'Hot version of our Don Darko', 39.00, 100, 'Specials', '/ui/Images/product_images/hot_darko.png'),
('Don Barako', 'Strong Filipino barako coffee', 39.00, 100, 'Specials', '/ui/Images/product_images/don_barako.png'),
('Don Pistachio', 'Pistachio-infused coffee creation', 39.00, 100, 'Specials', '/ui/Images/product_images/pure_pistachio.png');
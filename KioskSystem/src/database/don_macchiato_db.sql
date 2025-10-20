-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 20, 2025 at 09:31 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `don_macchiato_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_users_tb`
--

CREATE TABLE `admin_users_tb` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` date DEFAULT current_timestamp(),
  `last_login` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_users_tb`
--

INSERT INTO `admin_users_tb` (`admin_id`, `username`, `password`, `created_at`, `last_login`) VALUES
(1, 'admin', 'admin123', '2025-10-20', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `customer_order_tb`
--

CREATE TABLE `customer_order_tb` (
  `order_id` int(11) NOT NULL,
  `order_datetime` datetime DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `payment_method_id` int(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer_order_tb`
--

INSERT INTO `customer_order_tb` (`order_id`, `order_datetime`, `total_amount`, `payment_method_id`) VALUES
(1, '2025-10-11 20:24:37', 78.00, 1),
(2, '2025-10-11 20:25:21', 78.00, 2),
(3, '2025-10-11 20:25:48', 39.00, 1),
(4, '2025-10-16 19:36:29', 39.00, 1),
(5, '2025-10-16 20:50:56', 78.00, 1),
(6, '2025-10-18 09:46:26', 39.00, 1),
(7, '2025-10-18 09:49:44', 39.00, 1),
(8, '2025-10-18 09:50:33', 39.00, 2),
(9, '2025-10-18 10:56:42', 78.00, 2),
(10, '2025-10-18 12:16:25', 78.00, 1),
(11, '2025-10-18 12:21:02', 78.00, 1),
(12, '2025-10-19 18:55:19', 117.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `order_item_tb`
--

CREATE TABLE `order_item_tb` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_item_tb`
--

INSERT INTO `order_item_tb` (`order_item_id`, `order_id`, `product_id`, `quantity`, `subtotal`) VALUES
(1, 1, 3, 1, 39.00),
(2, 1, 4, 1, 39.00),
(3, 2, 9, 1, 39.00),
(4, 2, 5, 1, 39.00),
(5, 3, 9, 1, 39.00),
(6, 4, 13, 1, 39.00),
(7, 5, 3, 2, 78.00),
(8, 6, 8, 1, 39.00),
(9, 7, 3, 1, 39.00),
(10, 8, 1, 1, 39.00),
(11, 9, 5, 2, 78.00),
(12, 10, 1, 2, 78.00),
(13, 11, 5, 2, 78.00),
(14, 12, 9, 1, 39.00),
(15, 12, 13, 1, 39.00),
(16, 12, 8, 1, 39.00);

-- --------------------------------------------------------

--
-- Table structure for table `payment_method_tb`
--

CREATE TABLE `payment_method_tb` (
  `payment_method_id` int(11) NOT NULL,
  `method_name` varchar(50) DEFAULT NULL,
  `is_available` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment_method_tb`
--

INSERT INTO `payment_method_tb` (`payment_method_id`, `method_name`, `is_available`) VALUES
(1, 'Cash', 1),
(2, 'GCash', 1);

-- --------------------------------------------------------

--
-- Table structure for table `product_tb`
--

CREATE TABLE `product_tb` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_available` tinyint(4) DEFAULT NULL,
  `image_filename` varchar(255) NOT NULL DEFAULT 'default',
  `archived` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_tb`
--

INSERT INTO `product_tb` (`product_id`, `product_name`, `price`, `description`, `is_available`, `image_filename`, `archived`) VALUES
(1, 'Black Forest', 39.00, 'Rich chocolate cake with cherries and cream', 0, 'black_forest.png', 0),
(2, 'Don Barako', 39.00, 'Strong Filipino barako coffee blend', 0, 'don_barako.png', 0),
(3, 'Don Darko', 39.00, 'Dark roasted coffee with chocolate notes', 0, 'don_darko.png', 0),
(4, 'Don Matchatos', 39.00, 'Unique matcha and chocolate fusion drink', 0, 'don_matchatos.png', 0),
(5, 'Donya Berry', 39.00, 'Refreshing berry-infused coffee creation', 0, 'donya_berry.png', 0),
(6, 'Hot Caramel', 39.00, 'Warm caramel coffee delight', 0, 'hot_caramel.png', 0),
(7, 'Hot Darko', 39.00, 'Hot version of our signature dark roast', 0, 'hot_darko.png', 0),
(8, 'Iced Caramel', 39.00, 'Chilled caramel coffee refreshment', 0, 'iced_caramel.png', 0),
(9, 'Matcha Berry', 39.00, 'Matcha green tea with mixed berries', 0, 'matcha_berry.png', 0),
(10, 'Oreo Coffee', 39.00, 'Coffee blended with Oreo cookie crumbs', 0, 'oreo_coffee.png', 0),
(11, 'Pure Pistachio', 39.00, 'Creamy pistachio-flavored coffee', 0, 'pure_pistachio.png', 0),
(12, 'Pure Ube', 39.00, 'Traditional Filipino ube purple yam coffee', 0, 'pure_ube.png', 0),
(13, 'Spanish Latte', 39.00, 'Sweet condensed milk latte Spanish style', 1, 'spanish_latte.png', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_users_tb`
--
ALTER TABLE `admin_users_tb`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `customer_order_tb`
--
ALTER TABLE `customer_order_tb`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `payment_method_id` (`payment_method_id`);

--
-- Indexes for table `order_item_tb`
--
ALTER TABLE `order_item_tb`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `payment_method_tb`
--
ALTER TABLE `payment_method_tb`
  ADD PRIMARY KEY (`payment_method_id`),
  ADD UNIQUE KEY `method_name` (`method_name`);

--
-- Indexes for table `product_tb`
--
ALTER TABLE `product_tb`
  ADD PRIMARY KEY (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_users_tb`
--
ALTER TABLE `admin_users_tb`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `customer_order_tb`
--
ALTER TABLE `customer_order_tb`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `order_item_tb`
--
ALTER TABLE `order_item_tb`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `product_tb`
--
ALTER TABLE `product_tb`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer_order_tb`
--
ALTER TABLE `customer_order_tb`
  ADD CONSTRAINT `customer_order_tb_ibfk_1` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method_tb` (`payment_method_id`);

--
-- Constraints for table `order_item_tb`
--
ALTER TABLE `order_item_tb`
  ADD CONSTRAINT `order_item_tb_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `customer_order_tb` (`order_id`),
  ADD CONSTRAINT `order_item_tb_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product_tb` (`product_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

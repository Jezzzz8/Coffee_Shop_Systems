-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 02, 2025 at 03:35 PM
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
(2, '2025-10-02 19:04:59', 78.00, 1),
(3, '2025-10-02 19:07:01', 78.00, 1),
(4, '2025-10-02 19:41:08', 78.00, 1),
(5, '2025-10-02 19:55:07', 78.00, 2),
(6, '2025-10-02 20:08:51', 78.00, 1),
(7, '2025-10-02 20:15:57', 78.00, 2),
(8, '2025-10-02 20:16:24', 117.00, 1),
(9, '2025-10-02 20:34:57', 78.00, 1),
(10, '2025-10-02 20:58:03', 78.00, 1),
(11, '2025-10-02 21:05:10', 78.00, 1),
(12, '2025-10-02 21:05:51', 78.00, 1),
(13, '2025-10-02 21:06:52', 78.00, 1);

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
(1, 3, 12, 2, 78.00),
(2, 4, 9, 2, 78.00),
(3, 5, 9, 2, 78.00),
(4, 6, 9, 2, 78.00),
(5, 7, 9, 2, 78.00),
(6, 8, 9, 3, 117.00),
(7, 9, 12, 2, 78.00),
(8, 10, 1, 2, 78.00),
(9, 11, 8, 2, 78.00),
(10, 12, 8, 2, 78.00),
(11, 13, 13, 2, 78.00);

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
(2, 'GCash', 0);

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
  `image_filename` varchar(255) NOT NULL DEFAULT 'default'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_tb`
--

INSERT INTO `product_tb` (`product_id`, `product_name`, `price`, `description`, `is_available`, `image_filename`) VALUES
(1, 'Black Forest', 39.00, 'Rich chocolate cake with cherries and cream', 1, 'black_forest.png'),
(2, 'Don Barako', 39.00, 'Strong Filipino barako coffee blend', 1, 'don_barako.png'),
(3, 'Don Darko', 39.00, 'Dark roasted coffee with chocolate notes', 1, 'don_darko.png'),
(4, 'Don Matchatos', 39.00, 'Unique matcha and chocolate fusion drink', 1, 'don_matchatos.png'),
(5, 'Donya Berry', 39.00, 'Refreshing berry-infused coffee creation', 1, 'donya_berry.png'),
(6, 'Hot Caramel', 39.00, 'Warm caramel coffee delight', 1, 'hot_caramel.png'),
(7, 'Hot Darko', 39.00, 'Hot version of our signature dark roast', 1, 'hot_darko.png'),
(8, 'Iced Caramel', 39.00, 'Chilled caramel coffee refreshment', 1, 'iced_caramel.png'),
(9, 'Matcha Berry', 39.00, 'Matcha green tea with mixed berries', 1, 'matcha_berry.png'),
(10, 'Oreo Coffee', 39.00, 'Coffee blended with Oreo cookie crumbs', 0, 'oreo_coffee.png'),
(11, 'Pure Pistachio', 39.00, 'Creamy pistachio-flavored coffee', 0, 'pure_pistachio.png'),
(12, 'Pure Ube', 39.00, 'Traditional Filipino ube purple yam coffee', 0, 'pure_ube.png'),
(13, 'Spanish Latte', 39.00, 'Sweet condensed milk latte Spanish style', 1, 'spanish_latte.png');

--
-- Indexes for dumped tables
--

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
-- AUTO_INCREMENT for table `customer_order_tb`
--
ALTER TABLE `customer_order_tb`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `order_item_tb`
--
ALTER TABLE `order_item_tb`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `product_tb`
--
ALTER TABLE `product_tb`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

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

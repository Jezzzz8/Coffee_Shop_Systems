-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 08, 2025 at 07:31 PM
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
-- Database: `donmac_db`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_new_order_proc` (IN `p_customer_id` INT, IN `p_cashier_id` INT, IN `p_payment_method` ENUM('Cash','Card','E-Wallet'))   BEGIN
    INSERT INTO order_tb (customer_id, cashier_id, payment_method, total_amount)
    VALUES (p_customer_id, p_cashier_id, p_payment_method, 0);
    
    SELECT LAST_INSERT_ID() AS new_order_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `add_order_item_proc` (IN `p_order_id` INT, IN `p_product_id` INT, IN `p_quantity` INT)   BEGIN
    DECLARE v_product_price DECIMAL(10, 2);
    DECLARE v_subtotal DECIMAL(10, 2);
    DECLARE v_current_stock INT;
    
    -- Get product price
    SELECT price INTO v_product_price FROM product_tb WHERE product_id = p_product_id;
    
    -- Calculate subtotal
    SET v_subtotal = v_product_price * p_quantity;
    
    -- Check stock availability
    SELECT stock_quantity INTO v_current_stock FROM inventory_tb WHERE product_id = p_product_id;
    
    IF v_current_stock < p_quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insufficient stock available';
    END IF;
    
    -- Insert order item
    INSERT INTO order_item_tb (order_id, product_id, quantity, subtotal)
    VALUES (p_order_id, p_product_id, p_quantity, v_subtotal);
    
    -- Update order total amount
    UPDATE order_tb 
    SET total_amount = total_amount + v_subtotal 
    WHERE order_id = p_order_id;
    
    -- Update inventory
    UPDATE inventory_tb 
    SET stock_quantity = stock_quantity - p_quantity 
    WHERE product_id = p_product_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `get_daily_sales_report_proc` (IN `p_report_date` DATE)   BEGIN
    SELECT 
        o.order_id,
        o.order_date,
        c.first_name,
        c.last_name,
        cash.cashier_name,
        o.total_amount,
        o.payment_method,
        o.status
    FROM order_tb o
    JOIN customer_tb c ON o.customer_id = c.customer_id
    JOIN cashier_tb cash ON o.cashier_id = cash.cashier_id
    WHERE DATE(o.order_date) = p_report_date
    ORDER BY o.order_date DESC;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `get_low_stock_products_proc` (IN `p_threshold` INT)   BEGIN
    SELECT 
        p.product_id,
        p.product_name,
        p.category,
        i.stock_quantity,
        p.price
    FROM product_tb p
    JOIN inventory_tb i ON p.product_id = i.product_id
    WHERE i.stock_quantity <= p_threshold
    ORDER BY i.stock_quantity ASC;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_product_stock_proc` (IN `p_product_id` INT, IN `p_quantity` INT)   BEGIN
    UPDATE inventory_tb 
    SET stock_quantity = stock_quantity + p_quantity 
    WHERE product_id = p_product_id;
END$$

DELIMITER ;

--
-- Dumping data for table `category_tb`
--

INSERT INTO `category_tb` (`category_id`, `category_name`, `created_by`, `created_at`) VALUES
(2, 'Coffee', 'admin', '2025-09-06 14:58:06'),
(3, 'Dessert', 'admin', '2025-09-06 14:58:15'),
(4, 'Refreshment', 'admin', '2025-09-06 14:58:24'),
(5, 'Pastry', 'admin', '2025-09-06 14:58:33');

--
-- Dumping data for table `customer_tb`
--

INSERT INTO `customer_tb` (`customer_id`, `first_name`, `last_name`, `contact_no`, `email`, `created_at`) VALUES
(3, 'Mike', 'Johnson', '09127778899', 'mike.johnson@email.com', '2025-09-05 12:55:01'),
(4, 'Walk-in', 'Customer', '000-000-0000', 'walkin@donmac.com', '2025-09-07 13:03:57');

--
-- Dumping data for table `inventory_tb`
--

INSERT INTO `inventory_tb` (`inventory_id`, `product_id`, `stock_quantity`, `last_updated`) VALUES
(8, 8, 0, '2025-09-08 09:49:53'),
(9, 9, 0, '2025-09-08 15:26:36'),
(10, 10, 0, '2025-09-08 16:10:03'),
(11, 11, 0, '2025-09-08 09:49:53'),
(12, 12, 0, '2025-09-08 09:49:53'),
(13, 13, 0, '2025-09-08 09:49:53'),
(14, 14, 0, '2025-09-08 09:49:53'),
(15, 15, 0, '2025-09-08 09:49:53'),
(16, 16, 29, '2025-09-08 17:25:11'),
(17, 17, 0, '2025-09-08 09:49:53'),
(18, 18, 0, '2025-09-08 09:49:53'),
(19, 19, 0, '2025-09-08 09:49:53');

--
-- Dumping data for table `order_item_tb`
--

INSERT INTO `order_item_tb` (`order_item_id`, `order_id`, `product_id`, `quantity`, `subtotal`) VALUES
(23, 1, 16, 8, 312.00),
(24, 8, 16, 3, 117.00),
(25, 9, 16, 6, 234.00);

--
-- Dumping data for table `order_tb`
--

INSERT INTO `order_tb` (`order_id`, `customer_id`, `cashier_id`, `order_date`, `total_amount`, `payment_method`, `status`) VALUES
(1, 4, NULL, '2025-09-08 17:09:25', 312.00, 'Cash', 'Pending'),
(8, 4, NULL, '2025-09-08 17:24:57', 117.00, 'Cash', 'Pending'),
(9, 4, NULL, '2025-09-08 17:25:14', 234.00, 'Cash', 'Pending');

--
-- Dumping data for table `product_tb`
--

INSERT INTO `product_tb` (`product_id`, `product_name`, `category`, `price`, `description`, `image_path`, `is_active`, `created_at`) VALUES
(8, 'Iced Caramel Macchiato', 'Coffee', 39.00, 'Iced coffee with caramel and milk', '\\ui\\Images\\product_images\\iced_caramel.png', 1, '2025-09-08 09:49:53'),
(9, 'Spanish Latte', 'Coffee', 39.00, 'Sweet Spanish-style latte', '\\ui\\Images\\product_images\\spanish_latte.png', 1, '2025-09-08 09:49:53'),
(10, 'Pure Ube', 'Refreshment', 39.00, 'Pure ube flavor drink', '\\ui\\Images\\product_images\\pure_ube.png', 1, '2025-09-08 09:49:53'),
(11, 'Donya Berry', 'Coffee', 39.00, 'Refreshing berry blend', '\\ui\\Images\\product_images\\donya_berry.png', 1, '2025-09-08 09:49:53'),
(12, 'Don Matchattos', 'Coffee', 39.00, 'Signature Matcha blend', '\\ui\\Images\\product_images\\don_matchatos.png', 1, '2025-09-08 09:49:53'),
(13, 'Matcha Berry', 'Coffee', 39.00, 'Matcha green tea with berries', '\\ui\\Images\\product_images\\matcha_berry.png', 1, '2025-09-08 09:49:53'),
(14, 'Oreo Coffee', 'Coffee', 39.00, 'Coffee with Oreo crumbs', '\\ui\\Images\\product_images\\ore_coffee.png', 1, '2025-09-08 09:49:53'),
(15, 'Don Darko', 'Coffee', 39.00, 'Dark chocolate coffee', '\\ui\\Images\\product_images\\don_darko.png', 1, '2025-09-08 09:49:53'),
(16, 'Black Forest', 'Coffee', 39.00, 'Black forest flavor drink', '\\ui\\Images\\product_images\\black_forest.png', 1, '2025-09-08 09:49:53'),
(17, 'Hot Caramel', 'Coffee', 39.00, 'Hot caramel coffee', '\\ui\\Images\\product_images\\hot_caramel.png', 1, '2025-09-08 09:49:53'),
(18, 'Hot Darko', 'Coffee', 39.00, 'Hot dark chocolate coffee', '\\ui\\Images\\product_images\\hot_darko.png', 1, '2025-09-08 09:49:53'),
(19, 'Don Barako', 'Coffee', 39.00, 'Strong local barako coffee', '\\ui\\Images\\product_images\\don_barako.png', 1, '2025-09-08 09:49:53');

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `username`, `password`, `role`, `created_at`) VALUES
(1, 'Jess', 'Tadang', 'admin', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=', 'admin', '2025-09-05 13:23:26'),
(2, 'Jess', 'Tads', 'jesstads', 'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', 'cashier', '2025-09-05 17:51:46'),
(4, 'Ian', 'Calingasan', 'iancalibangon', 'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', 'clerk', '2025-09-07 05:16:44'),
(7, 'System', 'Kiosk', 'kiosk', '73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8=', 'cashier', '2025-09-07 15:55:29');

-- --------------------------------------------------------

--
-- Structure for view `order_details_vw`
--
DROP TABLE IF EXISTS `order_details_vw`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `order_details_vw`  AS SELECT `o`.`order_id` AS `order_id`, `o`.`order_date` AS `order_date`, `o`.`total_amount` AS `total_amount`, `o`.`payment_method` AS `payment_method`, `o`.`status` AS `status`, `c`.`first_name` AS `first_name`, `c`.`last_name` AS `last_name`, `c`.`contact_no` AS `contact_no`, `c`.`email` AS `email`, `cash`.`cashier_name` AS `cashier_name`, `cash`.`shift_schedule` AS `shift_schedule` FROM ((`order_tb` `o` join `customer_tb` `c` on(`o`.`customer_id` = `c`.`customer_id`)) join `cashier_tb` `cash` on(`o`.`cashier_id` = `cash`.`cashier_id`)) ;

-- --------------------------------------------------------

--
-- Structure for view `order_items_detailed_vw`
--
DROP TABLE IF EXISTS `order_items_detailed_vw`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `order_items_detailed_vw`  AS SELECT `oi`.`order_item_id` AS `order_item_id`, `oi`.`order_id` AS `order_id`, `oi`.`quantity` AS `quantity`, `oi`.`subtotal` AS `subtotal`, `p`.`product_name` AS `product_name`, `p`.`category` AS `category`, `p`.`price` AS `price`, `o`.`order_date` AS `order_date`, `c`.`first_name` AS `first_name`, `c`.`last_name` AS `last_name` FROM (((`order_item_tb` `oi` join `product_tb` `p` on(`oi`.`product_id` = `p`.`product_id`)) join `order_tb` `o` on(`oi`.`order_id` = `o`.`order_id`)) join `customer_tb` `c` on(`o`.`customer_id` = `c`.`customer_id`)) ;

-- --------------------------------------------------------

--
-- Structure for view `product_inventory_vw`
--
DROP TABLE IF EXISTS `product_inventory_vw`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `product_inventory_vw`  AS SELECT `p`.`product_id` AS `product_id`, `p`.`product_name` AS `product_name`, `p`.`category` AS `category`, `p`.`price` AS `price`, `p`.`description` AS `description`, `i`.`stock_quantity` AS `stock_quantity`, `i`.`last_updated` AS `last_updated` FROM (`product_tb` `p` left join `inventory_tb` `i` on(`p`.`product_id` = `i`.`product_id`)) ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

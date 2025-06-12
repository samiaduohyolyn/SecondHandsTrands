-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2025-06-11 22:23:11
-- 服务器版本： 5.7.26
-- PHP 版本： 7.3.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `second_hand_trading`
--

-- --------------------------------------------------------

--
-- 表的结构 `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `price` decimal(10,2) NOT NULL,
  `category` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` enum('在售','已售出','已下架') COLLATE utf8_unicode_ci DEFAULT '在售',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `products`
--

INSERT INTO `products` (`product_id`, `user_id`, `title`, `description`, `price`, `category`, `status`, `create_time`) VALUES
(1, 1, 'Java编程思想 第四版', '几乎全新，仅翻阅几次', '50.00', '图书', '在售', '2025-06-11 13:30:55'),
(2, 1, 'Apple MacBook Air 13寸', '2020款，8+256', '4500.00', '电子产品', '在售', '2025-06-11 13:32:57'),
(3, 1, '线性代数及其应用', '教材，有少量笔记，封面轻微磨损', '25.00', '图书', '已售出', '2025-06-11 13:34:56'),
(4, 2, '哈利波特全集（英文原版）', '全套7册，保存完好，适合英语学习', '120.00', '图书', '在售', '2025-06-11 13:34:56'),
(5, 3, '小米手环7', '几乎全新，所有功能正常，配件齐全', '100.00', '电子产品', '已售出', '2025-06-11 13:34:56'),
(6, 4, 'Beats Solo3 无线耳机', '黑色，电池续航良好，原装正品', '600.00', '电子产品', '在售', '2025-06-11 13:34:56'),
(7, 5, '宜家书桌', '尺寸80*50cm，木质，组装完好', '150.00', '生活用品', '已售出', '2025-06-11 13:34:56'),
(8, 5, '全自动洗衣机', '5公斤，海尔品牌，使用1年，功能正常', '300.00', '生活用品', '已下架', '2025-06-11 13:34:56'),
(9, 6, '迪卡侬山地自行车', '26寸，21速，适合校园骑行', '500.00', '运动户外', '在售', '2025-06-11 13:34:56'),
(10, 6, '瑜伽垫', '防滑材质，几乎全新，仅使用过几次', '30.00', '运动户外', '在售', '2025-06-11 13:34:56');

-- --------------------------------------------------------

--
-- 表的结构 `product_images`
--

CREATE TABLE `product_images` (
  `image_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `image_path` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `is_main` tinyint(1) DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `product_images`
--

INSERT INTO `product_images` (`image_id`, `product_id`, `image_path`, `is_main`) VALUES
(1, 1, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2024-12-23 131307.png', 1),
(2, 2, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-01-07 131344.png', 1),
(3, 4, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2024-12-24 233144.png', 1),
(5, 2, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-01-06 221016.png', 1),
(7, 3, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2024-12-23 131307.png', 1),
(9, 5, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-04-22 180406.png', 1),
(10, 6, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-04-22 180451.png', 1),
(11, 7, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-04-22 180406.png', 1),
(12, 8, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-04-22 180533.png', 1),
(13, 9, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-04-22 180328.png', 1),
(14, 10, 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-04-22 180032.png', 1);

-- --------------------------------------------------------

--
-- 表的结构 `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `buyer_id` int(11) NOT NULL,
  `seller_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `transaction_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(10,2) NOT NULL,
  `status` enum('待付款','已付款','已完成','已取消') COLLATE utf8_unicode_ci DEFAULT '待付款'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `buyer_id`, `seller_id`, `product_id`, `transaction_time`, `amount`, `status`) VALUES
(1, 2, 1, 3, '2025-06-11 13:42:49', '25.00', '待付款'),
(2, 8, 5, 7, '2025-06-11 14:18:32', '150.00', '待付款');

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `campus` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `phone`, `campus`, `avatar`, `create_time`) VALUES
(1, 'sa', 'sa', 'sa', 'sa', 'sa', 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2024-12-24 235118.png', '2025-06-11 13:27:59'),
(2, 'sa1', 'sa1', 'sa1', 'sa1', 'sa1', NULL, '2025-06-11 13:40:47'),
(3, 'sa2', 'sa2', 'sa2', 'sa2', 'sa2', NULL, '2025-06-11 13:48:38'),
(4, 'sa3', 'sa3', 'sa3', 'sa3', 'sa3', NULL, '2025-06-11 13:48:51'),
(5, 'sa4', 'sa4', 'sa4', 'sa4', 'sa4', NULL, '2025-06-11 13:49:11'),
(6, 'sa5', 'sa5', 'sa5', 'sa5', 'sa5', NULL, '2025-06-11 13:49:24'),
(7, 'sa6', 'sa6', 'sa6', 'sa6', 'sa6', NULL, '2025-06-11 13:49:38'),
(8, 'sa7', 'sa7', 'sa7', 'sa7', 'sa7', 'C:\\Users\\16247\\Pictures\\Screenshots\\屏幕截图 2025-05-26 145516.png', '2025-06-11 14:18:16');

--
-- 转储表的索引
--

--
-- 表的索引 `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `user_id` (`user_id`);

--
-- 表的索引 `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `product_id` (`product_id`);

--
-- 表的索引 `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `buyer_id` (`buyer_id`),
  ADD KEY `seller_id` (`seller_id`),
  ADD KEY `product_id` (`product_id`);

--
-- 表的索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- 使用表AUTO_INCREMENT `product_images`
--
ALTER TABLE `product_images`
  MODIFY `image_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用表AUTO_INCREMENT `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

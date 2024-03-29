INSERT INTO orders (order_no, member_id, products_name, total_amount, type, status, created_at)
VALUES ('202112130001', 1, '여성 원피스', 100000, 'SINGLE', 'COMPLETED', '2021-12-13 00:00:00'),
       ('202112140001', 1, '남성 티셔츠', 200000, 'CART', 'PENDING', '2021-12-14 00:00:00'),
       ('202112150001', 1, '신발', 300000, 'GIFT', 'COMPLETED', '2021-12-15 00:00:00'),
       ('202112160001', 1, '가방', 400000, 'AUCTION', 'PENDING', '2021-12-16 00:00:00'),
       ('202112170001', 1, '스니커즈', 150000, 'SINGLE', 'COMPLETED', '2021-12-17 00:00:00'),
       ('202112180001', 1, '여성 원피스', 250000, 'CART', 'PENDING', '2021-12-18 00:00:00'),
       ('202112190001', 1, '남성 셔츠', 180000, 'GIFT', 'COMPLETED', '2021-12-19 00:00:00'),
       ('202112200001', 1, '백팩', 320000, 'AUCTION', 'PENDING', '2021-12-20 00:00:00'),
       ('202112210001', 1, '여성 스커트', 120000, 'SINGLE', 'COMPLETED', '2021-12-21 00:00:00');

INSERT INTO order_detail (order_id, order_no, order_detail_no, product_id, product_size_id, coupon_info_id,
                          product_name,
                          product_quantity, product_size, product_gender, product_img_url, order_price, coupon_name,
                          coupon_discount_price, status)
VALUES (1, '202112130001', 'ORDER_DETAIL_NO1', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (1, '202112130001', 'ORDER_DETAIL_NO2', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (1, '202112130001', 'ORDER_DETAIL_NO3', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (1, '202112130001', 'ORDER_DETAIL_NO4', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (1, '202112130001', 'ORDER_DETAIL_NO5', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (2, '202112140001', 'ORDER_DETAIL_NO6', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (2, '202112140001', 'ORDER_DETAIL_NO7', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (2, '202112140001', 'ORDER_DETAIL_NO8', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (2, '202112140001', 'ORDER_DETAIL_NO9', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (2, '202112140001', 'ORDER_DETAIL_NO10', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (3, '202112150001', 'ORDER_DETAIL_NO11', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (3, '202112150001', 'ORDER_DETAIL_NO12', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (3, '202112150001', 'ORDER_DETAIL_NO13', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (3, '202112150001', 'ORDER_DETAIL_NO14', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (3, '202112150001', 'ORDER_DETAIL_NO15', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (3, '202112150002', 'ORDER_DETAIL_NO16', 6, 3, 6, '여성 블라우스', 1, 'S', '여성', 'https://example.com/image6.jpg',
        120000, '쿠폰 F',
        6000, 'BEFORE_DELIVERY'),
       (3, '202112150002', 'ORDER_DETAIL_NO17', 7, 4, 7, '남성 청바지', 1, '34', '남성', 'https://example.com/image7.jpg',
        180000, '쿠폰 G',
        9000, 'BEFORE_DELIVERY'),
       (3, '202112150002', 'ORDER_DETAIL_NO18', 8, 2, 8, '운동화', 2, 'M', '남성', 'https://example.com/image8.jpg', 250000,
        '쿠폰 H', 12500,
        'BEFORE_DELIVERY'),
       (3, '202112150002', 'ORDER_DETAIL_NO19', 9, 1, 9, '여성 가디건', 1, 'L', '여성', 'https://example.com/image9.jpg',
        90000, '쿠폰 I', 4500,
        'BEFORE_DELIVERY'),
       (3, '202112150002', 'ORDER_DETAIL_NO20', 10, 5, 10, '팔찌', 2, 'Free Size', '여성',
        'https://example.com/image10.jpg', 50000, '쿠폰 J',
        2500, 'BEFORE_DELIVERY'),
       (4, '202112160001', 'ORDER_DETAIL_NO21', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (4, '202112160001', 'ORDER_DETAIL_NO22', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (4, '202112160001', 'ORDER_DETAIL_NO23', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (4, '202112160001', 'ORDER_DETAIL_NO24', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (4, '202112160001', 'ORDER_DETAIL_NO25', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (5, '202112170001', 'ORDER_DETAIL_NO26', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (5, '202112170001', 'ORDER_DETAIL_NO27', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (5, '202112170001', 'ORDER_DETAIL_NO28', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (5, '202112170001', 'ORDER_DETAIL_NO29', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (5, '202112170001', 'ORDER_DETAIL_NO30', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (6, '202112180001', 'ORDER_DETAIL_NO31', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (6, '202112180001', 'ORDER_DETAIL_NO32', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (6, '202112180001', 'ORDER_DETAIL_NO33', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (6, '202112180001', 'ORDER_DETAIL_NO34', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (6, '202112180001', 'ORDER_DETAIL_NO35', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (7, '202112190001', 'ORDER_DETAIL_NO36', 1, 1, 1, '여성 원피스', 1, 'M', '여성', 'https://example.com/image1.jpg',
        100000, '쿠폰 A', 5000,
        'BEFORE_DELIVERY'),
       (7, '202112190001', 'ORDER_DETAIL_NO37', 2, 2, 2, '남성 티셔츠', 2, 'L', '남성', 'https://example.com/image2.jpg',
        200000, '쿠폰 B',
        10000, 'BEFORE_DELIVERY'),
       (7, '202112190001', 'ORDER_DETAIL_NO38', 3, 3, 3, '신발', 1, '250', '남성', 'https://example.com/image3.jpg', 300000,
        '쿠폰 C', 15000,
        'BEFORE_DELIVERY'),
       (7, '202112190001', 'ORDER_DETAIL_NO39', 4, 4, 4, '가방', 1, 'Free Size', '여성', 'https://example.com/image4.jpg',
        400000, '쿠폰 D',
        20000, 'BEFORE_DELIVERY'),
       (7, '202112190001', 'ORDER_DETAIL_NO40', 5, 5, 5, '스니커즈', 2, 'L', '여성', 'https://example.com/image5.jpg', 150000,
        '쿠폰 E', 7500,
        'BEFORE_DELIVERY'),
       (8, '202112200001', 'ORDER_DETAIL_NO41', 6, 3, 6, '여성 블라우스', 1, 'S', '여성', 'https://example.com/image6.jpg',
        120000, '쿠폰 F',
        6000, 'BEFORE_DELIVERY'),
       (8, '202112200001', 'ORDER_DETAIL_NO42', 7, 4, 7, '남성 청바지', 1, '34', '남성', 'https://example.com/image7.jpg',
        180000, '쿠폰 G',
        9000, 'BEFORE_DELIVERY'),
       (8, '202112200001', 'ORDER_DETAIL_NO43', 8, 2, 8, '운동화', 2, 'M', '남성', 'https://example.com/image8.jpg', 250000,
        '쿠폰 H', 12500,
        'BEFORE_DELIVERY'),
       (8, '202112200001', 'ORDER_DETAIL_NO44', 9, 1, 9, '여성 가디건', 1, 'L', '여성', 'https://example.com/image9.jpg',
        90000, '쿠폰 I', 4500,
        'BEFORE_DELIVERY'),
       (8, '202112200001', 'ORDER_DETAIL_NO45', 10, 5, 10, '팔찌', 2, 'Free Size', '여성',
        'https://example.com/image10.jpg', 50000, '쿠폰 J',
        2500, 'BEFORE_DELIVERY'),
       (9, '202112210001', 'ORDER_DETAIL_NO46', 11, 2, 11, '여성 스커트', 1, 'S', '여성', 'https://example.com/image11.jpg',
        80000, '쿠폰 K',
        4000, 'BEFORE_DELIVERY'),
       (9, '202112210001', 'ORDER_DETAIL_NO47', 12, 3, 12, '남성 재킷', 1, 'XL', '남성', 'https://example.com/image12.jpg',
        300000, '쿠폰 L',
        15000, 'BEFORE_DELIVERY'),
       (9, '202112210001', 'ORDER_DETAIL_NO48', 13, 4, 13, '가방', 1, 'Free Size', '여성',
        'https://example.com/image13.jpg', 500000,
        '쿠폰 M', 25000, 'BEFORE_DELIVERY'),
       (9, '202112210001', 'ORDER_DETAIL_NO49', 14, 1, 14, '남성 셔츠', 2, 'L', '남성', 'https://example.com/image14.jpg',
        150000, '쿠폰 N',
        7500, 'BEFORE_DELIVERY'),
       (9, '202112210001', 'ORDER_DETAIL_NO50', 15, 5, 15, '스니커즈', 2, 'M', '남성', 'https://example.com/image15.jpg',
        200000, '쿠폰 O',
        10000, 'BEFORE_DELIVERY');


INSERT INTO delivery
(id, order_id, status, receiver, post_code, road_address, detail_address, phone_number)
VALUES
    (1, 1, 'BEFORE_DELIVERY', '홍길동', '12345', '서울시 강남구 테헤란로', '101호', '010-1234-5678'),
    (2, 2, 'DELIVERY_PREPARE', '이순신', '67890', '서울시 서초구 강남대로', '202호', '010-9876-5432'),
    (3, 3, 'DELIVERING', '유관순', '11223', '서울시 종로구 종로', '303호', '010-5678-1234'),
    (4, 4, 'COMPLETE_DELIVERY', '강감찬', '44556', '서울시 중구 명동', '404호', '010-4321-9876'),
    (5, 5, 'BEFORE_DELIVERY', '신사임당', '77889', '서울시 용산구 이태원로', '505호', '010-8765-4321'),
    (6, 6, 'DELIVERY_PREPARE', '세종대왕', '99011', '서울시 동작구 신대방', '606호', '010-3456-7890'),
    (7, 7, 'DELIVERING', '장영실', '22334', '서울시 광진구 아차산로', '707호', '010-7890-1234'),
    (8, 8, 'COMPLETE_DELIVERY', '이황', '44555', '서울시 성동구 왕십리로', '808호', '010-2345-6789'),
    (9, 9, 'BEFORE_DELIVERY', '이이', '66677', '서울시 도봉구 도봉로', '909호', '010-6789-2345');

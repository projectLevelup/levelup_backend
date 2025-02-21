-- 유저 데이터 삽입
INSERT INTO user (id, email, nick_name, password, img_url, role, phone_number, is_deleted, created_at, updated_at)
VALUES (1, 'user1@example.com', 'UserOne', 'password1', 'https://example.com/user1.jpg', 'USER', '010-1234-5678', false,
        NOW(), NOW()),
       (2, 'user2@example.com', 'UserTwo', 'password2', 'https://example.com/user2.jpg', 'ADMIN', '010-8765-4321',
        false, NOW(), NOW()),
       (3, 'user3@example.com', 'UserThree', 'password3', NULL, 'USER', '010-1111-2222', false, NOW(), NOW());

-- 게임 데이터 삽입
INSERT INTO game (id, name, img_url, genre, user_id, is_deleted, created_at, updated_at)
VALUES (1, 'Game One', 'https://example.com/game1.jpg', 'RPG', 1, false, NOW(), NOW()),
       (2, 'Game Two', 'https://example.com/game2.jpg', 'FPS', 2, false, NOW(), NOW());

-- 제품 데이터 삽입
INSERT INTO product (id, user_id, game_id, product_name, contents, price, amount, status, img_url,
                     is_deleted, created_at, updated_at)
VALUES (1, 1, 1, 'Sword of Power', 'A legendary sword.', 100000, 10, 'ACTIVE', 'https://example.com/sword.jpg', false,
        NOW(), NOW()),
       (2, 2, 2, 'Shield of Valor', 'An indestructible shield.', 50000, 5, 'ACTIVE', 'https://example.com/shield.jpg',
        false, NOW(), NOW());

-- 주문 데이터 삽입
INSERT INTO orders (id, status, total_price, user_id, product_id, is_deleted, created_at, updated_at)
VALUES (1, 'COMPLETED', 100000, 1, 1, false, NOW(), NOW()),
       (2, 'PENDING', 50000, 2, 2, false, NOW(), NOW());

-- 리뷰 데이터 삽입
INSERT INTO review (id, contents, star_score, product_id, user_id, orders_id, is_deleted, created_at, updated_at)
VALUES (1, '정말 좋은 상품입니다!', 5, 1, 1, 1, false, NOW(), NOW()),
       (2, '보통이에요.', 3, 2, 2, 2, false, NOW(), NOW()),
       (3, '별로였습니다.', 2, 2, 3, 2, false, NOW(), NOW());
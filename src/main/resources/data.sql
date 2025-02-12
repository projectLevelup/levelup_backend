-- 유저 데이터 삽입
INSERT INTO user (id, created_at, updated_at, is_deleted, email, nick_name, img_url, password, role, phone_number)
VALUES
    (1, NOW(), NOW(), 0, 'user1@example.com', '닉네임1', 'http://example.com/user1.jpg', 'hashedpassword1', 'USER', '010-1234-5678'),
    (2, NOW(), NOW(), 0, 'user2@example.com', '닉네임2', 'http://example.com/user2.jpg', 'hashedpassword2', 'ADMIN', '010-8765-4321');

-- 게임 데이터 삽입
INSERT INTO game (id, created_at, updated_at, is_deleted, name, img_url, genre, user_id)
VALUES
    (1, NOW(), NOW(), 0, '게임1', 'http://example.com/game1.jpg', 'RPG', 1),
    (2, NOW(), NOW(), 0, '게임2', 'http://example.com/game2.jpg', 'FPS', 2),
    (3, NOW(), NOW(), 0, '게임3', 'http://example.com/game3.jpg', 'MOBA', 1);

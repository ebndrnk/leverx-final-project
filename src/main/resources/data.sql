DELETE FROM rating WHERE id BETWEEN 1 AND 6;
DELETE FROM comment WHERE id BETWEEN 1 AND 6;
DELETE FROM game_object WHERE id BETWEEN 1 AND 6;
DELETE FROM anonymous_user WHERE id BETWEEN 1 AND 5;

DELETE FROM app_user WHERE id IN (2,3,4,5,6,7,8,9,10);
DELETE FROM profile WHERE id BETWEEN 2 AND 10;

DELETE FROM app_user WHERE email = 'ebndrnk@gmail.com';
INSERT INTO app_user ( email, password, role, username, is_email_confirmed)
VALUES ('ebndrnk@gmail.com', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_ADMIN', 'admin_ebndrnk', true);


INSERT INTO profile (username, firstname, lastname, email, is_confirmed_by_admin, rating) VALUES
   ('user1', 'Alice', 'Smith', 'user1@mail.com', true, 7),
   ('user2', 'Bob', 'Johnson', 'user2@mail.com', false, 5),
   ('user3', 'Carol', 'Williams', 'user3@mail.com', true, 8),
   ('user4', 'David', 'Brown', 'user4@mail.com', true, 6),
   ('user5', 'Eve', 'Jones', 'user5@mail.com', false, 9),
   ('user6', 'Frank', 'Garcia', 'user6@mail.com', true, 4),
   ('user7', 'Grace', 'Miller', 'user7@mail.com', true, 7),
   ('user8', 'Henry', 'Davis', 'user8@mail.com', false, 5),
   ( 'user9', 'Ivy', 'Rodriguez', 'user9@mail.com', true, 8),
   ( 'user10', 'Jack', 'Martinez', 'user10@mail.com', true, 6);

INSERT INTO app_user (username, password, email, role, is_email_confirmed, profile_id) VALUES
   ('user1', '$2a$10$FakeHash1', 'user1@mail.com', 'ROLE_SELLER', true, 2),
   ('user2', '$2a$10$FakeHash2', 'user2@mail.com', 'ROLE_SELLER', true, 3),
   ('user3', '$2a$10$FakeHash3', 'user3@mail.com', 'ROLE_SELLER', true, 4),
   ('user4', '$2a$10$FakeHash4', 'user4@mail.com', 'ROLE_SELLER', true, 5),
   ('user5', '$2a$10$FakeHash5', 'user5@mail.com', 'ROLE_SELLER', true, 6),
   ('user6', '$2a$10$FakeHash6', 'user6@mail.com', 'ROLE_SELLER', true, 7),
   ('user7', '$2a$10$FakeHash7', 'user7@mail.com', 'ROLE_SELLER', true, 8),
   ('user8', '$2a$10$FakeHash8', 'user8@mail.com', 'ROLE_SELLER', true, 9),
   ( 'user9', '$2a$10$FakeHash9', 'user9@mail.com', 'ROLE_SELLER', true, 10);

INSERT INTO anonymous_user (anonymous_id) VALUES
   ('anon_001'),
   ('anon_002'),
   ('anon_003'),
   ('anon_004'),
   ('anon_005');

INSERT INTO game_object (title, text, price, seller_id) VALUES
   ('Cyberpunk 2077', 'Open-world RPG', 59.99, 5),
   ('The Witcher 3', 'Fantasy RPG', 39.99, 5),
   ('Red Dead 2', 'Western RPG', 49.99, 7),
   ('GTA V', 'Action Game', 29.99, 7),
   ('Minecraft', 'Sandbox Game', 19.99, 9),
   ('Skyrim', 'Fantasy RPG', 14.99, 9);

INSERT INTO comment (message, author_id, seller_id, approved) VALUES
   ('Great seller!', 1, 5, true),
   ('Fast delivery', 2, 5, true),
   ('Good prices', 3, 7, false),
   ('Highly recommend', 4, 7, true),
   ('Awesome games', 5, 9, true),
   ('Friendly support', 1, 9, false);

INSERT INTO rating (mark, author_id, seller_id) VALUES
   (9, 1, 5),
   (8, 2, 5),
   (7, 3, 7),
   (10, 4, 7),
   (9, 5, 9),
   (8, 1, 9);
-- Insert 5 regular users (confirmed by admin, ROLE_SELLER)
INSERT INTO app_user (email, firstname, lastname, password, role, username, is_email_confirmed, is_confirmed_by_admin)
VALUES
    ('user1@example.com', 'User1', 'Lastname1', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'user1', true, true),
    ('user2@example.com', 'User2', 'Lastname2', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'user2', true, true),
    ('user3@example.com', 'User3', 'Lastname3', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'user3', true, true),
    ('user4@example.com', 'User4', 'Lastname4', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'user4', true, true),
    ('user5@example.com', 'User5', 'Lastname5', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'user5', true, true);

-- Insert 5 seller users (not confirmed by admin, ROLE_SELLER)
INSERT INTO app_user (email, firstname, lastname, password, role, username, is_email_confirmed, is_confirmed_by_admin)
VALUES
    ('seller1@example.com', 'Seller1', 'Lastname1', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'seller1', true, false),
    ('seller2@example.com', 'Seller2', 'Lastname2', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'seller2', true, false),
    ('seller3@example.com', 'Seller3', 'Lastname3', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'seller3', true, false),
    ('seller4@example.com', 'Seller4', 'Lastname4', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'seller4', true, false),
    ('seller5@example.com', 'Seller5', 'Lastname5', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_SELLER', 'seller5', true, false)

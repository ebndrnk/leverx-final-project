delete from app_user where email = 'ebndrnk@gmail.com';

INSERT INTO app_user (email, password, role, username, is_email_confirmed)
VALUES ('ebndrnk@gmail.com', '$2a$10$E38pY6zLNVAnXGfDmZkmiOBVuZO1q.x1s.gaD8HVfqpwDLu3Qao2C', 'ROLE_ADMIN', 'admin_ebndrnk', true);

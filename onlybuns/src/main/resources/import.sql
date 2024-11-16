-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123

INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('user', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Marko', 'Markovic', 'user@example.com', true, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('admin', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'admin@example.com', true, '2017-10-01 18:57:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('user1', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user1@example.com', true, '2017-10-01 18:57:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('user2', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user2@example.com', true, '2017-10-01 18:57:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('user3', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user3@example.com', true, '2017-10-01 18:57:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('user4', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user4@example.com', true, '2017-10-01 18:57:58.508-07');

INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 1); -- admin-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 1); -- user-u dodeljujemo rolu USER

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('123 Elm Street, Springfield', '2024-11-09 14:30:00', 'Beautiful sunset view', 'uploads/1_sunset.jpg', 37.7749, -122.4194, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('456 Maple Avenue, Metropolis', '2024-11-09 15:45:00', 'Cute rabbit in the park', 'uploads/2_rabbit.jpg', 40.7128, -74.0060, 2);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('789 Pine Road, Gotham', '2024-11-09 16:00:00', 'Amazing mountain hike', 'uploads/3_mountains.jpg', 34.0522, -118.2437, 1);


INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('Wow, this view is stunning!', '2024-11-09 15:00:00', 2, 1);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('Aww, the rabbit is adorable!', '2024-11-09 16:00:00', 1, 2);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('Looks like a great hike; I should try it!', '2024-11-09 17:00:00', 2, 3);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('This view is totally worth the climb!', '2024-11-09 17:45:00', 1, 3);

INSERT INTO USER_FOLLOWING (follower_id, following_id) VALUES (1, 1);
-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123

INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('user', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Marko', 'Markovic', 'user@example.com', true, '2017-10-01 21:58:58.508-07', 0, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('admin', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'admin@example.com', true, '2017-10-01 18:57:58.508-07', 0, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('relja', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Relja', 'Vranjes', 'reljavranjes02@gmail.com', true, '2017-10-01 18:57:58.508-07', 0, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('user1', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user1@example.com', true, '2017-10-01 18:57:58.508-07', 0, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('user2', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user2@example.com', true, '2017-10-01 18:57:58.508-07', 0, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('user3', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user3@example.com', true, '2017-10-01 18:57:58.508-07', 0, '2017-10-01 21:58:58.508-07');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, registration_date) VALUES ('user4', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user4@example.com', true, '2017-10-01 18:57:58.508-07', 0, '2017-10-01 21:58:58.508-07');


INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 1); -- admin-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 1); -- user-u dodeljujemo rolu USER

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('123 Elm Street, Springfield', '2023-10-09 14:30:00', 'Beautiful sunset view', 'uploads\1_rabbit.jpg', 37.7749, -122.4194, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('456 Maple Avenue, Metropolis', '2023-12-09 15:45:00', 'Cute rabbit in the park', 'uploads\2_rabbit.jpg', 40.7128, -74.0060, 2);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('789 Pine Road, Gotham', '2024-10-09 16:00:00', 'Amazing mountain hike', 'uploads\3_rabbit.jpg', 34.0522, -118.2437, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('101 Oak Street, Springfield', '2024-10-25 10:00:00', 'Lovely garden with flowers', 'uploads\4_rabbit.jpg', 37.7749, -122.4194, 1);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('202 Birch Lane, Metropolis', '2024-12-27 12:30:00', 'Sunny day at the park', 'uploads\5_rabbit.jpg', 40.7128, -74.0060, 2);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('303 Cedar Way, Gotham', '2024-11-28 14:45:00', 'Snowy mountain retreat', 'uploads\6_rabbit.jpg', 34.0522, -118.2437, 1);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('404 Elm Avenue, Springfield', '2024-05-29 11:15:00', 'Stunning city skyline', 'uploads\7_rabbit.jpg', 37.7749, -122.4194, 3);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('505 Pine Boulevard, Metropolis', '2024-01-30 09:30:00', 'Beautiful beach sunset', 'uploads\8_rabbit.jpg', 40.7128, -74.0060, 2);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('606 Maple Lane, Gotham', '2024-01-26 16:20:00', 'Relaxing forest walk', 'uploads\9_rabbit.jpg', 34.0522, -118.2437, 4);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('707 Birch Street, Springfield', '2024-11-25 18:10:00', 'Bright flowers blooming', 'uploads\10_rabbit.jpg', 37.7749, -122.4194, 1);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('808 Cedar Road, Metropolis', '2024-12-08 14:50:00', 'Charming river view', 'uploads\11_rabbit.jpg', 40.7128, -74.0060, 2);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('909 Elm Boulevard, Gotham', '2024-11-28 10:30:00', 'Morning coffee spot', 'uploads\12_rabbit.jpg', 34.0522, -118.2437, 4);

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('1000 Pine Avenue, Springfield', '2024-11-29 19:45:00', 'Cozy cabin in the woods', 'uploads\13_rabbit.jpg', 37.7749, -122.4194, 3);


INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('Wow, this view is stunning!', '2024-11-09 15:00:00', 2, 1);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('Aww, the rabbit is adorable!', '2024-11-08 16:00:00', 1, 2);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('Looks like a great hike; I should try it!', '2024-11-09 17:00:00', 2, 3);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('This view is totally worth the climb!', '2024-12-09 17:45:00', 4, 4);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('This view is totally worth the climb!', '2024-10-09 17:45:00', 6, 5);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('This view is totally worth the climb!', '2024-08-01 17:45:00', 5, 6);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('This view is totally worth the climb!', '2024-08-09 17:45:00', 5, 7);
INSERT INTO COMMENTS (content, created_at, user_id, post_id) VALUES ('This view is totally worth the climb!', '2024-01-09 17:45:00', 3, 8);

INSERT INTO POST_LIKES (liked_at, post_id, user_id) VALUES ('2024-11-09 15:00:00', 1, 2);

INSERT INTO USER_FOLLOWING (follower_id, following_id) VALUES (1,2);
INSERT INTO USER_FOLLOWING (follower_id, following_id) VALUES (2,1);

INSERT INTO CHAT_ROOMS (chat_admin_id, created_at, name) VALUES (1, '"2025-02-28 22:38:06', 'olelole');

INSERT INTO CHAT_USERS (join_time, chat_room_id, user_id) VALUES ('2025-02-28 22:41:08', 1, 2);
INSERT INTO CHAT_USERS (join_time, chat_room_id, user_id) VALUES ('2025-02-28 22:41:03', 1, 1);
INSERT INTO CHAT_USERS (join_time, chat_room_id, user_id) VALUES ('2025-02-28 22:41:14', 1, 4);

INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('1', '2025-02-28 22:41:04', 'user', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('2', '2025-02-28 22:41:05', 'user', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('3', '2025-02-28 22:41:06', 'user', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('4', '2025-02-28 22:41:07', 'user', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('5', '2025-02-28 22:41:08', 'user', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('6', '2025-02-28 22:41:09', 'admin', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('7', '2025-02-28 22:41:10', 'admin', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('8', '2025-02-28 22:41:11', 'admin', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('9', '2025-02-28 22:41:12', 'admin', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('10', '2025-02-28 22:41:13', 'admin', 1);
INSERT INTO MESSAGES (content, date, username, chat_room_id) VALUES ('11', '2025-02-28 22:41:14', 'admin', 1);

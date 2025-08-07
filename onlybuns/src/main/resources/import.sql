-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123

INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (1, 'Main Street', '101', 'New York', 40.712776, -74.005974);
INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (2, 'Broadway', '202', 'New York', 40.713055, -74.007228);
INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (3, 'Wall Street', '303', 'New York', 40.707491, -74.011276);
INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (4, 'Market Street', '401', 'San Francisco', 37.774929, -122.419416);
INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (5, 'Mission Street', '501', 'San Francisco', 37.776846, -122.416502);
INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (6, 'High Street', '601', 'Los Angeles', 34.052235, -118.243683);
INSERT INTO address (id, street, street_number, city, latitude, longitude) VALUES (7, 'Ocean Avenue', '701', 'Miami', 25.761681, -80.191788);

INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('user', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Marko', 'Markovic', 'user@example.com', true, '2017-10-01 21:58:58.508-07', 0, 1, '2017-10-01 21:58:58.508-07', 1);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('admin', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'admin@example.com', true, '2017-10-01 18:57:58.508-07', 0, 2, '2017-10-01 21:58:58.508-07', 1);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('relja', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Relja', 'Vranjes', 'reljavranjes02@gmail.com', true, '2017-10-01 18:57:58.508-07', 0, 3, '2017-10-01 21:58:58.508-07', 0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('user1', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user1@example.com', true, '2017-10-01 18:57:58.508-07', 0, 4, '2017-10-01 21:58:58.508-07', 0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('user2', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user2@example.com', true, '2017-10-01 18:57:58.508-07', 0, 5, '2017-10-01 21:58:58.508-07', 0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('user3', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user3@example.com', true, '2017-10-01 18:57:58.508-07', 0, 6, '2017-10-01 21:58:58.508-07', 0);
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date, version, address_id, registration_date, followers_count) VALUES ('user4', '$2a$10$dpZUUh0IuhiyL5a5/ltkLOU2NZzsafOy3aQWmngM1x3hfzndIbsOm', 'Nikola', 'Nikolic', 'user4@example.com', true, '2017-10-01 18:57:58.508-07', 0, 7, '2017-10-01 21:58:58.508-07', 0);

INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 1); -- admin-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 1); -- user-u dodeljujemo rolu USER

INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('123 5th Avenue, New York, NY', '2023-11-09 14:30:00', 'Beautiful sunset view', 'uploads/1_rabbit.jpg', 40.741895, -73.989308, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('456 Madison Avenue, New York, NY', '2024-11-09 15:45:00', 'Cute rabbit in the park', 'uploads/2_rabbit.jpg', 40.758896, -73.985130, 2);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('789 Broadway, New York, NY', '2024-11-09 16:00:00', 'Amazing mountain hike', 'uploads/3_rabbit.jpg', 40.712776, -74.005974, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('101 Main Street, Brooklyn, NY', '2024-11-25 10:00:00', 'Lovely garden with flowers', 'uploads/4_rabbit.jpg', 40.702659, -73.987378, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('202 Central Park West, New York, NY', '2024-11-27 12:30:00', 'Sunny day at the park', 'uploads/5_rabbit.jpg', 40.781324, -73.973988, 2);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('303 Hollywood Boulevard, Los Angeles, CA', '2024-11-28 14:45:00', 'Snowy mountain retreat', 'uploads/6_rabbit.jpg', 34.092809, -118.328661, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('404 Fulton Street, Brooklyn, NY', '2024-11-29 11:15:00', 'Stunning city skyline', 'uploads/7_rabbit.jpg', 40.689247, -73.989631, 3);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('505 Beach Avenue, Miami, FL', '2024-11-30 09:30:00', 'Beautiful beach sunset', 'uploads/8_rabbit.jpg', 25.761681, -80.191788, 2);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('606 Grand Avenue, Jersey City, NJ', '2024-11-26 16:20:00', 'Relaxing forest walk', 'uploads/9_rabbit.jpg', 40.717754, -74.043143, 4);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('707 Forest Lane, Albany, NY', '2024-11-25 18:10:00', 'Bright flowers blooming', 'uploads/10_rabbit.jpg', 42.652579, -73.756232, 1);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('808 River Road, Hoboken, NJ', '2024-12-08 14:50:00', 'Charming river view', 'uploads/11_rabbit.jpg', 40.745255, -74.027475, 2);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('909 Broadway, San Francisco, CA', '2024-11-28 10:30:00', 'Morning coffee spot', 'uploads/12_rabbit.jpg', 37.798589, -122.405904, 4);
INSERT INTO POSTS (address, created_at, description, image_path, latitude, longitude, user_id) VALUES ('1000 Pine Street, Boston, MA', '2024-11-29 19:45:00', 'Cozy cabin in the woods', 'uploads/13_rabbit.jpg', 42.360081, -71.058884, 3);

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

/* ROLES */
insert into public.role(role)
values ('ROLE_ADMIN'),
       ('ROLE_CLIENT'),
       ('ROLE_RESTAURANT');

/* ADMIN USERS */
insert into public.user_base(role, username, password, email)
values (1, 'admin-test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'admin-test@email.com');
insert into public.user_admin
values (1);

/* CLIENT USERS */
insert into public.user_base(role, username, password, email)
values (2, 'client-test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W', 'client-test@email.com');
insert into public.user_client
values (2);

insert into public.user_base(role, username, password, email)
values (2, 'recovery-test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W',
        'recovery-test@email.com');
insert into public.user_client
values (3);

/* RESTAURANT USERS */
insert into public.user_base(role, username, password, email)
values (3, 'restaurant-test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W',
        'restaurant-test@email.com');
insert into public.user_restaurant(id, verified)
values (4, true);

insert into public.user_base(role, username, password, email)
values (3, 'restaurant-test-1', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W',
        'restaurant-test-1@email.com');
insert into public.user_restaurant(id, verified)
values (5, false);

insert into public.user_base(role, username, password, email)
values (3, 'restaurant-test-2', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W',
        'restaurant-test-2@email.com');
insert into public.user_restaurant(id, verified)
values (6, false);

/* TOKENS */
insert into public.password_recovery_token(token, "user")
values ('token-test', 4);

/* ADDRESSES */
insert into public.address(street, zip_code, city, country)
values ('Ulicowa 12', '23-023', 'Gdansk', 'Polska');

/* RESTAURANTS */
insert into public.restaurant(name, description, capacity, address, phone_number, owner)
values ('Burgerownia', 'Burgerowania desc', 15, 1, 123123123, 4),
       ('Pizzeria', 'Pizzeria desc', 15, 1, 123321123, 4),
       ('PizzerioBurgerownia', 'PizzerioBurgerownia desc', 15, 1, 321123123, 4),
       ('NoVerifiedTest', 'NoVerifiedTest desc', 15, 1, 321126123, 5);

/* TAGS */
insert into public.tag(value)
values ('burger'),
       ('pizza'),
       ('sushi'),
       ('wege');
insert into public.restaurant_tags(restaurant_id, tags_id)
values (1, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (4, 2),
       (4, 4);

/* RESTAURANT IMAGES */
INSERT INTO public.restaurant_image (main, name, path, size, type, restaurant)
VALUES (true, '475905.jpg', 'C:\workspace\images\475905.jpg', 214541, 'image/jpeg', 1);
INSERT INTO public.restaurant_image (main, name, path, size, type, restaurant)
VALUES (false, 'divinity-original-sin-2-mage-video-game-1320.jpg',
        'C:\workspace\images\divinity-original-sin-2-mage-video-game-1320.jpg', 1010008, 'image/jpeg', 1);
INSERT INTO public.restaurant_image (main, name, path, size, type, restaurant)
VALUES (true, '816923.png', 'C:\workspace\images\816923.png', 1601550, 'image/png', 2);
INSERT INTO public.restaurant_image (main, name, path, size, type, restaurant)
VALUES (false,
        'Konachan.com - 117037 brown_hair flowers forest keishi long_hair original photoshop scenic sky space stars tree.png',
        'C:\workspace\images\Konachan.com - 117037 brown_hair flowers forest keishi long_hair original photoshop scenic sky space stars tree.png',
        8288608, 'image/png', 2);
INSERT INTO public.restaurant_image (main, name, path, size, type, restaurant)
VALUES (false, '739010.jpg', 'C:\workspace\images\739010.jpg', 1036375, 'image/jpeg', 3);
INSERT INTO public.restaurant_image (main, name, path, size, type, restaurant)
VALUES (true, '74htem6e65g51.png', 'C:\workspace\images\74htem6e65g51.png', 6067425, 'image/png', 3);

/* REVIEWS */
insert into public.review("user", restaurant, content, rate)
values (2, 1, 'Test1', 'THREE'),
       (3, 1, 'Test2', 'FOUR'),
       (2, 2, 'Test3', 'TWO');

/* REVIEW REPORTS */
insert into public.review_report(restaurant, review)
values (1, 1),
       (1, 2);

/* CHAT_ROOMS */
insert into public.chat_room(client, restaurant)
values (2, 4),
       (3, 4);

/* CHAT MESSAGES */
insert into public.chat_message(content, status, timestamp, author, chat_room, recipient)
values ('Test client is sending message to restaurant', 'RECEIVED', '2020-01-03'::timestamp, 2, 1, 4),
       ('Test restaurant is sending message to client', 'RECEIVED', '2020-01-04'::timestamp, 4, 1, 2),
       ('Test recovery test is sending message to restaurant', 'RECEIVED', '2020-01-04'::timestamp, 3, 2, 4),
       ('Test restaurant is sending message to recovery test', 'RECEIVED', '2020-01-05'::timestamp, 4, 2, 3),
       ('Test recovery test is sending second message to restaurant', 'DELIVERED', '2020-01-06'::timestamp, 3, 2, 4);

/* MENU */
insert into public.single_menu_dish(name, description, price, type)
values ('Dish 1', 'Test dish 1', 2.50, 'TEA'),
       ('Dish 2', 'Test dish 2', 123.123, 'SALAD'),
       ('Dish 3', 'Test dish 3', 5.65, 'MAIN'),
       ('Dish 4', 'Test dish 4', 17.23, 'BURGER'),
       ('Dish 5', 'Test dish 5', 11.99, 'COFFEE'),
       ('Dish 6', 'Test dish 6', 7.55, 'DESSERT'),
       ('Dish 7', 'Test dish 7', 4.35, 'DRINK'),
       ('Dish 8', 'Test dish 8', 1.27, 'SANDWICH'),
       ('Dish 9', 'Test dish 9', 8.97, 'SNACK'),
       ('Dish 10', 'Test dish 10', 4.66, 'SOUP'),
       ('Dish 11', 'Test dish 11', 32.12, 'SOUP'),
       ('Dish 12', 'Test dish 12', 76.32, 'PIZZA'),
       ('Dish 13', 'Test dish 13', 2.89, 'PIZZA'),
       ('Dish 14', 'Test dish 14', 12.63, 'PIZZA');

insert into public.restaurant_menu(restaurant_id, dish_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14);

/* OPEN HOURS */
insert into public.open_hour(open_time, day_of_week, close_time, restaurant)
values ('15:00'::time, 1, '19:00'::time, 1),
       ('15:00'::time, 1, '19:00'::time, 2),
       ('11:00'::time, 1, '19:00'::time, 3),
       ('12:00'::time, 3, '19:00'::time, 1),
       ('12:00'::time, 4, '14:00'::time, 2),
       ('15:00'::time, 5, '19:00'::time, 3),
       ('15:00'::time, 2, '19:00'::time, 1),
       ('15:00'::time, 2, '19:00'::time, 2),
       ('15:00'::time, 2, '19:00'::time, 3);

/* RESERVATION */
insert into public.reservation(persons_count, reservation_date, reservation_time, status, client, restaurant)
values (2, '01-02-2021'::date, '12:30'::time, 'ACCEPTED', 2, 1),
       (2, '02-02-2021'::date, '12:30'::time, 'ACCEPTED', 2, 2),
       (2, '03-02-2021'::date, '12:30'::time, 'REJECTED', 2, 1),
       (2, '04-02-2021'::date, '12:30'::time, 'CANCELED', 3, 2),
       (2, '05-02-2021'::date, '12:30'::time, 'ACCEPTED', 3, 3);
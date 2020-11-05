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
insert into public.user_restaurant(id)
values (4);

/* TOKENS */
insert into public.password_recovery_token(token, "user")
values ('token-test', 4);

/* ADDRESSES */
insert into public.address(street, zip_code, city, country)
values ('Ulicowa 12', '23-023', 'Gdańsk', 'Polska');

/* RESTAURANTS */
insert into public.restaurant(name, address, phone_number, owner)
values ('Burgerownia', 1, 123123123, 4),
       ('Pizzeria', 1, 123321123, 4),
       ('PizzerioBurgerownia', 1, 321123123, 4);

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
       (3, 4);

/* REVIEWS */
insert into public.review("user", restaurant, content, rate)
values (2, 1, 'Test1', 'THREE'),
       (3, 1, 'Test2', 'FOUR'),
       (2, 2, 'Test3', 'TWO');
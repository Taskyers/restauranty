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
values ('Burgerownia', 1, 123123123, 4);

/* REVIEWS */
insert into public.review("user", restaurant, content, rate)
values (1, 1, 'Test1', 'THREE'),
       (1, 1, 'Test2', 'ONE');

/* CHAT_ROOMS */
insert into public.chat_room(client,restaurant)
values (2,4),(3,4);
/* CHAT MESSAGES */
insert into public.chat_message(content,status,timestamp,author, chat_room, recipient)
values ('Test client is sending message to restaurant','RECEIVED','2020-01-03'::timestamp,2,1,4),
       ('Test restaurant is sending message to client','RECEIVED','2020-01-04'::timestamp,4,1,2),
       ('Test recovery test is sending message to restaurant','RECEIVED','2020-01-04'::timestamp,3,2,4),
        ('Test restaurant is sending message to recovery test','RECEIVED','2020-01-05'::timestamp,4,2,3),
          ('Test recovery test is sending second message to restaurant','DELIVERED','2020-01-06'::timestamp,3,2,4);
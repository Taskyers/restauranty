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

/* RESTAURANT USERS */
insert into public.user_base(role, username, password, email)
values (3, 'restaurant-test', '$2a$10$0k1y57DwGGZ8iKY5jpd6fum./qxDxq24lGsi8ChagpXgEHHVV0V6W',
        'restaurant-test@email.com');
insert into public.user_restaurant
values (3);
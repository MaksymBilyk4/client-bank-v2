INSERT INTO public.accounts (id, number, currency, balance, customer_id)
VALUES (1, 'fds4-1412-1', 'USD', 592, 1),
       (2, 'fds-fdgjj1', 'EUR', 1053.1, 1),
       (3, '432dsgfdsg', 'USD', 0, 2);

INSERT INTO public.customers (id, name, email, age, employer_id, account_id)
VALUES (1, 'Max', 'd@gmail.com', 15, 1, 1),
       (2, 'Den', 'dsfds@gmail.com', 16, 2, 1);

INSERT INTO public.employers (id, name, address, customer_id)
VALUES (1, 'first', 'wp', 1),
       (2, 'second', 'gg', 2);
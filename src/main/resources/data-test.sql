INSERT INTO TASK_CATEGORIES (name, description)
VALUES ('other', 'A category for everything.'),
       ('finance', 'my activities related to money and business.'),
       ('wellness', 'Need to rest sometimes.');

INSERT INTO TASKS (name, description, deadline, category_id)
VALUES ('order pizza', 'order 5 pizzas for tonight.', '2022-05-01T18:59:00+02:00', 1),
       ('repair car', 'bring the car to the garage', '2022-04-30T07:59:00+02:00', 1),
       ('check budget', 'connect to the bank website and check transactions of last month.',
        '2022-04-28T09:00:00+02:00', 2),
       ('yoga', 'yoga session on zoom', '2022-05-03T12:00:00+02:00', 3);
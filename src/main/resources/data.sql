-- users 
INSERT INTO users (username, password, phone, balance)
VALUES ('young', 'young0216', 426660916, 0)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, phone, balance)
VALUES ('kittynini', 'kittynini0425', 13380970745, 1000)
ON CONFLICT (username) DO NOTHING;

-- transactions 
INSERT INTO transactions (transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'test1',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'EXPENSE',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

INSERT INTO transactions (transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'test2',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'EXPENSE',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

INSERT INTO transactions (transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'test3',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'INCOME',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

INSERT INTO transactions (transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'test4',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'INCOME',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

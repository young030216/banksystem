-- users 
INSERT INTO users (username, password, phone, balance)
VALUES ('young', 'young0216', 426660916, 0)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, phone, balance)
VALUES ('kittynini', 'kittynini0425', 13380970745, 1000)
ON CONFLICT (username) DO NOTHING;

-- transactions 
INSERT INTO transactions
(transfer_id, transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'TX100',
    'test1',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'EXPENSE',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

INSERT INTO transactions
(transfer_id, transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'TX100',
    'test2',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'INCOME',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

INSERT INTO transactions
(transfer_id, transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'TX200',
    'test3',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    500,
    '2025-10-01 18:25:00',
    'EXPENSE',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

INSERT INTO transactions
(transfer_id, transaction_id, from_user, to_user, amount, timestamp, status, description)
VALUES (
    'TX200',
    'test4',
    (SELECT id FROM users WHERE username='young'),
    (SELECT id FROM users WHERE username='kittynini'),
    400, 
    '2025-10-01 18:25:00',
    'INCOME',
    'testing'
)
ON CONFLICT (transaction_id) DO NOTHING;

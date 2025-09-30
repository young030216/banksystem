INSERT INTO users (username, password, phone, balance) VALUES ('young', 'young0216', 426660916, 0) ON DUPLICATE KEY UPDATE username=username;

INSERT INTO users (username, password, phone, balance) VALUES ('kittynini', 'kittynini0425', 13380970745, 1000) ON DUPLICATE KEY UPDATE username=username;
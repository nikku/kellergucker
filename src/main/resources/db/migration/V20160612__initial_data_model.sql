CREATE TABLE users (
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  created TIMESTAMP NOT NULL
);

CREATE TABLE stocks (
  symbol VARCHAR(255) PRIMARY KEY,
  date VARCHAR(255),
  open real,
  high real,
  low real,
  close real,
  volume real,
  adjusted_close real
);

CREATE TABLE user_triggers (
  user_id VARCHAR(255) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
  stock_symbol VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  buy real NOT NULL,
  sell real NOT NULL,
  created TIMESTAMP NOT NULL,
  PRIMARY KEY (user_id, stock_symbol)
);
CREATE TABLE historic_stocks (
  symbol VARCHAR(255),
  date VARCHAR(255),
  open real,
  high real,
  low real,
  close real,
  volume real,
  adjusted_close real,
  PRIMARY KEY (symbol, date)
);
CREATE TABLE historic_stocks (
  symbol VARCHAR(255),
  date VARCHAR(255),
  open numeric(12, 3),
  high numeric(12, 3),
  low numeric(12, 3),
  close numeric(12, 3),
  volume numeric(12, 3),
  adjusted_close numeric(12, 3),
  PRIMARY KEY (symbol, date)
);
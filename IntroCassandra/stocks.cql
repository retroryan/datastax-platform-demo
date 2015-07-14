CREATE KEYSPACE stocks WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

use stocks;

CREATE TABLE stock_summary (
    symbol text,
    dateOffset int,
    open decimal,
    close decimal,
    low decimal,
    high decimal,
    totQuantity int,
    PRIMARY KEY (symbol, dateOffset)
) WITH CLUSTERING ORDER BY (dateOffset ASC)
    and gc_grace_seconds = 0;

CREATE TABLE stock_ticks_date (
  tradeId uuid,
  symbol text,
  dateOffset int,
  tradeDate timestamp,
  price decimal,
  quantity int,
  PRIMARY KEY ((symbol, dateOffset), tradeDate)
) WITH CLUSTERING ORDER BY (tradeDate DESC)
    and gc_grace_seconds = 0;

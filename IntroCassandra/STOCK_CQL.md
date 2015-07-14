## STOCK CQL Demo Scripts

This is an example of using CQL to create a basic Cassandra Data Model and then import data into the tables.

* Create the stocks keyspace and tables
  * cqlsh localhost 9042 -f stocks.cql

* In a cqlsh import the data

`COPY stock_ticks_date (tradeId, symbol, dateOffset, tradeDate, price, quantity)  FROM 'stock_ticks_date.csv';`

`COPY stock_summary (symbol, dateOffset, open, close, low, high,totQuantity)  FROM 'stock_summary.csv';`

```
use stocks;

describe tables;

describe table stock_ticks_date;

select * from stock_summary;

select * from stock_ticks_date where symbol='ORCL' and dateoffset=16633
    and tradeDate > '2015-07-17 23:11:45+0000' and tradeDate < '2015-07-17 23:38:18+0000';

SELECT symbol, dateoffset, token(symbol, dateoffset) FROM stocks.stock_ticks_date;    
```

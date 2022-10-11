-- register a MySQL table 'users' in Flink SQL
CREATE TABLE MyUserTable
(
    id   BIGINT,
    name STRING,
    PRIMARY KEY (id) NOT ENFORCED
) WITH (
      'connector' = 'jdbc',
      'url' = 'jdbc:mysql://localhost:3306/tiezhu',
      'table-name' = 'test_one',
      'username' = 'root',
      'password' = 'admin123'
      );

create table sink
(
    name varchar,
    aa   DECIMAL(10, 0)
) with (
      'connector' = 'print'
      );

insert into sink
SELECT name,
       max(INT_VAL) as aa
FROM DIM_HOUR_M
GROUP BY name;
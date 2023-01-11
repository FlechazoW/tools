CREATE TABLE source
(
    `id`      int,
    `name`    varchar,
   `-name`   varchar,
    `name-`   varchar,
    `-name-`  varchar,
    `:name`   varchar,
    PRIMARY KEY (`id`) NOT ENFORCED
) WITH (
      'connector' = 'jdbc',
      'url' = 'jdbc:mysql://op1:3306/tiezhu',
      'table-name' = 'test-one',
      'username' = 'root',
      'password' = 'admin123'
      )
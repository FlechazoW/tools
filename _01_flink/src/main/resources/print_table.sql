CREATE TABLE sink
(
    id       int,
    `name`   varchar,
    `-name`  varchar,
    `name-`  varchar,
    `-name-` varchar,
    `:name`  varchar
) with (
      'connector' = 'print'
      )
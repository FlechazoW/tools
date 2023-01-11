insert into sink
select id,
       name,
       -name,
       name-,
       -name-,
       :name
from source;
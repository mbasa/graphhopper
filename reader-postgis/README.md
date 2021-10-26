# graphhopper-reader-postgis


#### Creating a GraphHopper Graph from a PostGIS Table


* Uncomment the PostGIS parameters in the config.yml and set the 
appropriate values. The PostGIS reader will only be used if all 
the PostGIS parameters are set.

```
  ##### PostGIS Connection Parameters #####
  
  db.host: localhost
  db.port: 5432
  db.database: pgr
  db.table: graphhopper
  db.schema: public
  db.user: mbasa
  db.passwd: passwd

```
 
* Create a Table or View in the specifed database and schema that 
has these necessary columns (refer to OSM documentation for the proper 
values of these columns) : 

``` 
osm_id, maxspeed, oneway, fclass, name, geom 
```

For example:

``` 
 create view graphhopper 
   (osm_id,maxspeed,oneway,fclass,name,geom ) 
   as select id,0,'B'::text,'tertiary'::text,name,geom from phil;
```


#### Turn Restrictions

* For Turn Restrictions information, add the following fields
 into the view: `restriction::text` and `restriction_to::integer`


* The `restriction` field should contain one of the following recognized 
OSM turn restrictions:

```
 no_left_turn
 no_right_turn
 no_straight_on
 no_u_turn
 no_entry
 only_right_turn
 only_left_turn
 only_straight_on
```

* The `restriction_to` field should contain the **osm_id** 
of the road the restriction will be based upon.


#### Starting the GraphHopper Server 

* To start the GraphHopper server do:

``` 
java -jar web/target/graphhopper-web-4.0.jar server config.yml
```

* If the data in PostgreSQL changes and the graph has to be updated, 
just delete the created graph directory and restart GraphHopper 
using the above method.


#### Reference

For more information refer to [this blog on the postgis-reader](https://georepublic.info/ja/blog/2018/graphhopper-with-postgis-data-reader/).


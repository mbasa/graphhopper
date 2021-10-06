# graphhopper-reader-postgis

To create a GraphHopper Graph from a PostGIS table, do:

* Uncomment the PostGIS parameters in the config.yml and set the appropriate values. The PostGIS reader will only be used if all the PostGIS parameters are set.

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
 
* Create a Table or View in the specifed database and schema that has these necessary columns (refer to OSM documentation for the proper values of these columns) : 

``` 
osm_id, maxspeed, oneway, fclass, name, geom 
```

For example:

``` 
 create view graphhopper 
   (osm_id,maxspeed,oneway,fclass,name,geom ) 
   as select id,0,'B'::text,'tertiary'::text,name,geom from phil;
```

* Start the GraphHopper server :

``` 
java -jar web/target/graphhopper-web-4.0.jar server config.yml
```

* If the data in PostgreSQL changes and the graph has to be updated, just delete the created graph directory and restart GraphHopper using the above method.



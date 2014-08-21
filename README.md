tile-joiner
===========

Renders a Google Maps tiles to a BufferedImage in java and thence to a PNG for instance.

Status: *pre-alpha*

How to build
-------------
To compiles, tests and builds the artifact locally:

 ```bash
 git clone https://github.com/davidmoten/tile-joiner.git
 cd tile-joiner
 mvn clean install
 ```
Usage
----------
You can generate an image using maven or direct from java. 

### Generate an image using maven
 
To create an image joined and cropped from google maps tiles:

```
mvn exec:java -Dlat1=-35 -Dlon1=149 -Dlon2=150 -Dwidth=1200 -Dheight=800 -Dmap.type=m
```
 
The image wil be a Google Maps Hybrid map and is created in ```target/map.png``` by default.
 
### Generate an image using java
See [ImageMakerMain.java](src/main/java/com/github/davidmoten/tj/ImageMakerMain.java).

Map types
-----------

* ```m``` = roads
* ```s``` = satellite
* ```y``` = hybrid
* ```h``` = roads overlay
* ```t``` = terrain

Tile cache
=============
Tiled images will be cached in ```System.getProperty("java.io.tmpdir")``` and start with 
the prefix *tile-joiner-* if you'd like to clean them out to force refresh for instance.
 
 
 

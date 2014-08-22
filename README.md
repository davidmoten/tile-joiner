tile-joiner
===========

Renders Google Maps tiles to a [BufferedImage](http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html) 
in java and thence to a PNG for instance.

Status: *pre-alpha*

<img src="https://raw.githubusercontent.com/davidmoten/tile-joiner/master/src/docs/map.png"/>

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
mvn exec:java -DtopLat=16 -DleftLon=67 -DrightLon=179 -Dwidth=800 -Dheight=500 \
  -Dmap.type=s -Dimage.format=PNG -Doutput.file=target/map.png
```

The image wil be a Google Maps Satellite map and is created in ```target/map.png``` by default.

The parameters above are also the defaults. The same result will be produced by 

```
mvn exec:java
```
 
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
 
 
 

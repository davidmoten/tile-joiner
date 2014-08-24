tile-joiner
===========

Renders service map tiles from Google Maps, ArcGIS, OpenStreetMaps to a [BufferedImage](http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html) 
in java and thence to a PNG for instance.

Status: *pre-alpha*

<img src="https://raw.githubusercontent.com/davidmoten/tile-joiner/master/src/docs/map.png"/>

Features
---------------
* Calculates optimal zoom level
* Handles 180/-180 longitude boundary
* Caches tiles to the local file system

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
mvn exec:java -DtopLat=25 -DleftLon=50 -DrightLon=-140 -Dwidth=800 -Dheight=500 \
  -Dmap.type=s -Dimage.format=PNG -Doutput.file=target/map.png
```

The image wil be a Google Maps Satellite map and is created in ```target/map.png``` by default.

The parameters above are also the defaults. The same result will be produced by 

```
mvn exec:java
```
 
### Generate an image using java
Add this dependency to your pom.xml:

```xml
<dependency>
  <groupId>com.github.davidmoten</groupId>
  <artifactId>tile-joiner</artifactId>
  <version>0.1-SNAPSHOT</version>
</dependency>
```

Then download, cache, and join tiles and write the image to a file like this:

```java
ImageCreator.builder()
            .topLat(25)
            .leftLon(50)
			.rightLon(-140)
            .width(800)
            .height(500)
			.outputFile("target/map.png")
            .imageFormat("PNG")
            .service(MapService.GOOGLE)
            .mapType(MapType.SATELLITE)
			.create();
```

See also [ImageCreatorMain.java](src/main/java/com/github/davidmoten/tj/ImageCreatorMain.java).



Map types
-----------

Google Maps:
* ```m``` = roads
* ```s``` = satellite
* ```y``` = hybrid
* ```h``` = roads overlay
* ```t``` = terrain

Tile cache
=============
Tiled images will be cached in ```System.getProperty("java.io.tmpdir")``` and start with 
the prefix *tile-joiner-* if you'd like to clean them out to force refresh for instance.
 
 
 

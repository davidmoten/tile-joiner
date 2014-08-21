tile-joiner
===========

Renders a group of (Google Maps) tiles to a BufferedImage in java and thence to a PNG for instance.

Status: *alpha*

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

 ###Generate an image using maven
  To create an image joined and cropped from google maps tiles:
 
 ```bash
 mvn exec:java -Dlat1=-35 -Dlon1=149 -Dlat2=-37 -Dlon2=150 -Dwidth=1200 -Dheight=800
 ```
 
 The image will be placed in ```target/map.png```.
 
 ###Generate an image using java
 See [ImageMakerMain.java](src/main/java/com/github/davidmoten/tj/ImageMakerMain.java).
 
 Tile cache
 =============
 Tiled images will be cached in ```System.getProperty("java.io.tmpdir")``` and start with 
 the prefix *tile-joiner-* if you'd like to clean them out to force refresh for instance.
 
 
 

tile-joiner
===========

Renders a group of (Google Maps) tiles to a BufferedImage in java and thence to a PNG for instance.

How to build
=============
To compiles, tests and builds the artifact locally:

 ```bash
 git clone https://github.com/davidmoten/tile-joiner.git
 cd tile-joiner
 mvn clean install
 ```
 
 To create an image joined and cropped from google maps tiles:
 
 ```bash
 mvn exec:java -Dlat1=-35 -Dlon1=149 -Dlat2=-37 -Dlon2=150 -Dwidth=1200 -Dheight=800
 ```
 
 The image will be placed in ```target/map.png```.
 
 
 

#!/bin/bash
set -e
mvn site
cd ../davidmoten.github.io
git pull
mkdir -p tile-joiner
cp -r ../tile-joiner/target/site/* tile-joiner/
git add .
git commit -am "update site reports"
git push

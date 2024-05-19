#!/bin/bash
./gradlew build
docker build -t exchanger_scanner .
docker run --name exchanger_scanner exchanger_scanner
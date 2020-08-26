#!/bin/bash

docker build -t gttp:latest .

docker run --rm --entrypoint cat gttp /home/user/gttp/gttp > gttp

chmod +x gttp
#!/bin/bash
set -e   # dừng nếu có lỗi

docker build --platform=linux/amd64 -t appabove-be-app:latest .
docker save -o app.tar appabove-be-app:latest
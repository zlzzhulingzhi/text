#!/bin/bash


echo "==================== 正式环境 部署开始 ===================="
echo "nginx 服务"
docker-compose up -d nginx
echo "执行完成"




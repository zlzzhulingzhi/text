#!/bin/bash

echo "==================== 正式环境 部署开始 ===================="
echo "停用所有服务容器"
docker-compose down --rmi all
echo "es索引服务 xxljob定时器"
docker-compose up -d es1  xxljob
echo "执行完成"



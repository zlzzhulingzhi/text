#!/bin/bash

echo "==================== 正式环境 部署开始 ===================="
echo "停用所有服务容器"
docker-compose down --rmi all
docker system prune -f
docker volume prune -f
echo "准备构建 teach-auth teach-cert union-auth train-screen teach-gateway"
docker-compose up -d teach-auth teach-cert union-auth train-screen teach-gateway
echo "执行完成"




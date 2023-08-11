#!/bin/bash

echo "==================== 正式环境 部署开始 ===================="
echo "停用所有服务容器"
docker-compose down --rmi all
docker system prune -f
docker volume prune -f
echo "准备构建 teach-basic teach-organization teach-course-standard union-admin train-basic train-logistics"
docker-compose up -d teach-basic teach-organization teach-course-standard union-admin train-basic train-logistics
echo "执行完成"




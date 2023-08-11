#!/bin/bash

echo "==================== 正式环境 部署开始 ===================="
echo "停用所有服务容器"
docker-compose down --rmi all
docker system prune -f
docker volume prune -f
echo "准备构建 teach-question teach-exam-admin teach-exam-consume teach-exam-answer train-allowance"
docker-compose up -d teach-question teach-exam-admin teach-exam-consume teach-exam-answer train-allowance
echo "执行完成"




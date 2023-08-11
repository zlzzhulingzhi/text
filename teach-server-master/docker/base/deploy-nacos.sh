#!/bin/bash


nacos1(){
  echo "==================== 正式环境部署开始 ===================="
  echo "准备构建 1.Nacos1"
  docker-compose up -d nacos1
  echo "执行完成"
}


nacos2(){
  echo "==================== 正式环境部署开始 ===================="
  echo "准备构建 1.Nacos2 "
  docker-compose up -d nacos2
  echo "执行完成"
}

nacos3(){
  echo "==================== 正式环境Naco3 部署开始 ===================="
  echo "准备构建 1.Nacos3"
  docker-compose up -d nacos3
  echo "执行完成"
}

case "$1" in
"nacos1")
	nacos1
;;
"nacos2")
	nacos2
;;
"nacos3")
	nacos3
;;

esac



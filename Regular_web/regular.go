package main

import (
	"./cmd"
	"./dao"
)

func main() {
	//初始化数据库链接
	dao.DB = dao.Init()
	defer dao.DB.Close()
	//创建用户表
	dao.CreateUserTab()
	//创建群组表
	dao.CreateGroupTab()
	//创建规划表
	dao.CreateProjectTab()
	//创建图书表
	dao.CreateBooksTab()
	//创建消息表
	dao.CreateMessageTab()
	//创建音乐文件索引表
	dao.CreateMusicTab()
	//开启socket
	go cmd.Im()
	//进入路由配置
	cmd.Entrance()
}

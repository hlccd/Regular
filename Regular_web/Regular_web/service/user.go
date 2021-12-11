package service

import (
	"../dao"
	"../models"
	"fmt"
	"github.com/gin-gonic/gin"
	"strconv"
)

/**
Login
登录服务
登录成功返回用户id和用户名
登录失败返回0和""
*/

func Login(c *gin.Context) (uid int64, name, info string) {
	uidS := c.Query("uid")
	uid, _ = strconv.ParseInt(uidS, 10, 64)
	password := c.Query("password")
	b, name, info := models.Login(uid, password)
	if !b {
		return 0, "", info
	}
	return uid, name, info
}

/**
Register
注册服务
注册成功返回用户id和用户名
注册失败返回0和""
*/

func Register(c *gin.Context) (uid int64, name, info string) {
	password := c.Query("password")
	name = c.Query("name")
	b, uid, info := models.Register(password, name)
	if !b {
		return 0, "", info
	}
	return uid, name, info
}

/**
GainUserMessage
获取用户详细信息
失败则返回全为默认值的User类型
*/

func GainUserMessage(c *gin.Context) ( user dao.User, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	b, user, info := models.GainUserMessage(operator)
	if !b {
		return dao.User{}, info
	}
	return user, info
}

func FriendList(c *gin.Context)(list []dao.IdName) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return dao.FriendList(operator)
}

func SearchUsers(c *gin.Context) (users []dao.IdName, info string) {
	searchS:= c.Query("search")
	b, users, info := dao.SearchUsers(searchS)
	if !b {
		return nil, info
	}
	return users, info
}

func ChangeUserName(c *gin.Context) (uid int64, name, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	name = c.Query("name")
	b, info := models.ChangeUserName(operator, name)
	if !b {
		return 0, "", info
	}
	return operator, name, info
}

func ChangePassword(c *gin.Context) (uid int64, password, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	password = c.Query("passage")
	b, info := models.ChangePassword(operator, password)
	if !b {
		return 0, "", info
	}
	return operator, password, info
}

func ChangePhone(c *gin.Context) (uid int64, phone int64, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	phoneS := c.Query("phone")
	phone, _ = strconv.ParseInt(fmt.Sprintf("%v", phoneS), 10, 64)
	b, info := models.ChangePhone(operator, phone)
	if !b {
		return 0, 0, info
	}
	return operator, phone, info
}

func ChangeEmail(c *gin.Context) (uid int64, email, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	email = c.Query("email")
	b, info := models.ChangeEmail(operator, email)
	if !b {
		return 0, "", info
	}
	return operator, email, info
}

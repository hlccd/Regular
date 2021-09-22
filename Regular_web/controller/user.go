package controller

import (
	"../service"
	"github.com/gin-gonic/gin"
	"github.com/hlccd/util/jwt"
	"github.com/hlccd/util/response"
)

//反馈的message结构体
type userMes struct {
	Uid   int64       `json:"uid"`
	Data  interface{} `json:"data"` //特色数据,根据不同功能提供不同的数据
	Token string      `json:"token"`
}

/**
Login
实现登录
根据服务层返回的用户id和用户名判断是否实现登录
*/

func Login(c *gin.Context) {
	uid, name, info := service.Login(c)
	if uid > 0 {
		token := jwt.CreateToken(uid, nil)
		response.Ok(c, userMes{
			Uid:   uid,
			Data:  name,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}

/**
Register
实现注册
根据服务层返回的用户id和用户名判断是否完成注册
*/

func Register(c *gin.Context) {
	uid, name, info := service.Register(c)
	if uid > 0 {
		token := jwt.CreateToken(uid, nil)
		response.Ok(c, userMes{
			Uid:   uid,
			Data:  name,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}

func GainUserMessage(c *gin.Context) {
	user, info := service.GainUserMessage(c)
	if user.U_id > 0 {
		token := jwt.CreateToken(user.U_id, nil)
		response.Ok(c, userMes{
			Uid:   user.U_id,
			Data:  user,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}

func FriendList(c *gin.Context)  {
	list:= service.FriendList(c)
	if list!=nil {
		response.Ok(c, list)
	} else {
		response.Error(c,"error", nil)
	}
}
func SearchUsers(c *gin.Context) {
	users, info := service.SearchUsers(c)
	if users != nil {
		response.Ok(c, users)
	} else {
		response.Error(c, info, nil)
	}
}

func ChangeUserName(c *gin.Context) {
	uid, name, info := service.ChangeUserName(c)
	if uid > 0 {
		token := jwt.CreateToken(uid, nil)
		response.Ok(c, userMes{
			Uid:   uid,
			Data:  name,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}
func ChangePassword(c *gin.Context) {
	uid, password, info := service.ChangePassword(c)
	if uid > 0 {
		token := jwt.CreateToken(uid, nil)
		response.Ok(c, userMes{
			Uid:   uid,
			Data:  password,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}
func ChangePhone(c *gin.Context) {
	uid, phone, info := service.ChangePhone(c)
	if uid > 0 {
		token := jwt.CreateToken(uid, nil)
		response.Ok(c, userMes{
			Uid:   uid,
			Data:  phone,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}
func ChangeEmail(c *gin.Context) {
	uid, email, info := service.ChangeEmail(c)
	if uid > 0 {
		token := jwt.CreateToken(uid, nil)
		response.Ok(c, userMes{
			Uid:   uid,
			Data:  email,
			Token: token,
		})
	} else {
		response.Error(c, info, nil)
	}
}

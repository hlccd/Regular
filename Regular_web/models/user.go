package models

import (
	"../dao"
)

//获取用户信息

func GainUserMessage(uid int64) (b bool, user dao.User, info string) {
	return dao.GainUserMessage(uid)
}

//验证用户登录

func Login(uid int64, password string) (b bool, name string, info string) {
	return dao.Login(uid, password)
}

//用户注册

func Register(password, name string) (b bool, uid int64, info string) {
	return dao.Register(password, name)
}

//用户改名

func ChangeUserName(uid int64, name string) (b bool, info string) {
	return dao.ChangeUserName(uid, name)
}

//用户改密

func ChangePassword(uid int64, password string) (b bool, info string) {
	return dao.ChangePassword(uid, password)
}

//用户改手机号

func ChangePhone(uid, phone int64) (b bool, info string) {
	return dao.ChangePhone(uid, phone)
}

//用户改电子邮箱

func ChangeEmail(uid int64, email string) (b bool, info string) {
	return dao.ChangeEmail(uid, email)
}

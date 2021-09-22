package models

import (
	"../dao"
)

//获取群成员列表

func FindMember(gid int64) (list []int64) {
	return dao.FindMember(gid)
}

//获取群组gid列表

func FindGroups() (list []dao.GroupName) {
	return dao.FindGroups()
}

//获取某用户在某群组的权限

func GetPower(gid, uid int64) (power int) {
	return dao.GetPower(gid, uid)
}

//创建群组

func Establish(uid int64, name string) (b bool, gid int64, info string) {
	return dao.Establish(uid, name)
}

//群组改名

func ChangeGroupName(gid int64, name string) (b bool, info string) {
	return dao.ChangeGroupName(gid, name)
}

//加入群组

func Join(uid, gid int64) (b bool, info string) {
	return dao.Join(uid, gid)
}

//踢出群组

func Kick(uid, gid int64) (b bool, info string) {
	return dao.Kick(uid, gid)
}

//更换群主

func ChangeMaster(gid, master int64) (b bool, info string) {
	return dao.ChangeMaster(gid, master)
}

//设置管理员

func SetupAdministrator(gid, uid int64) (b bool, info string) {
	return dao.SetupAdministrator(gid, uid)
}

//取消管理员

func CancelAdministrator(gid, uid int64) (b bool, info string) {
	return dao.CancelAdministrator(gid, uid)
}

//注销群组

func CancelGroup(gid int64) (b bool, info string) {
	return dao.CancelGroup(gid)
}

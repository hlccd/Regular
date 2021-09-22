package models

import (
	"../dao"
)

//查看个人习惯记录

func FindHabit(uid int64) (list []dao.Habit) {
	return dao.FindHabit(uid)
}

//查看个人任务记录

func FindUserTask(uid int64) (list []dao.Task) {
	return dao.FindUserTask(uid)
}

//查看群组任务记录

func FindGroupTask(gid int64) (list []dao.Task) {
	return dao.FindGroupTask(gid)
}

//查看执行记录

func FindImplement(uid int64) (list []dao.Implement) {
	return dao.FindImplement(uid)
}

//新增习惯记录

func IncreaseHabit(uid int64, name, label, remark string, difficulty byte) (b bool, info string) {
	return dao.IncreaseHabit(uid, name, label, remark, difficulty)
}

//完成一次习惯

func DoneHabit(uid, begin, duration, end int64, name string, difficulty byte, PN bool) (b bool, info string) {
	return dao.DoneHabit(uid, begin, duration, end, name, difficulty, PN)
}

//增加一项个人任务

func IncreaseUserTask(uid int64, name, label, remark string, difficulty byte) (b bool, info string) {
	return dao.IncreaseUserTask(uid, name, label, remark, difficulty)
}

//完成一项个人任务

func DoneUserTask(uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	return dao.DoneUserTask(uid, begin, duration, end, name, difficulty)
}

//增加一项团体任务

func IncreaseGroupTask(gid int64, name, label, remark string, difficulty byte) (b bool, info string) {
	return dao.IncreaseGroupTask(gid, name, label, remark, difficulty)
}

//完成一项团体任务

func DoneGroupTask(gid, uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	return dao.DoneGroupTask(gid, uid, begin, duration, end, name, difficulty)
}

//增加一条执行记录

func DoneImplement(uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	return dao.DoneImplement(uid, begin, duration, end, name, difficulty)
}
package service

import (
	"../dao"
	"../models"
	"fmt"
	"github.com/gin-gonic/gin"
	"strconv"
)

func FindHabit(c *gin.Context) (list []dao.Habit) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return models.FindHabit(operator)
}
func FindUserTask(c *gin.Context) (list []dao.Task) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return models.FindUserTask(operator)
}
func FindGroupTask(c *gin.Context) (list []dao.Task) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return models.FindGroupTask(operator)
}
func FindImplement(c *gin.Context) (list []dao.Implement) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return models.FindImplement(operator)
}
func IncreaseHabit(c *gin.Context) (b bool, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	name := c.Query("name")
	label := c.Query("label")
	remark := c.Query("remark")
	difficultyS := c.Query("difficulty")
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	return models.IncreaseHabit(operator, name, label, remark, byte(difficulty))
}
func DoneHabit(c *gin.Context) (b bool, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	beginS := c.Query("begin")
	begin, _ := strconv.ParseInt(beginS, 10, 64)
	durationS := c.Query("duration")
	duration, _ := strconv.ParseInt(durationS, 10, 64)
	endS := c.Query("end")
	end, _ := strconv.ParseInt(endS, 10, 64)
	name := c.Query("name")
	difficultyS := c.Query("difficulty")
	PNS := c.Query("PN")
	PN := true
	if PNS == "0" {
		PN = false
	}
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	return models.DoneHabit(operator, begin, duration, end, name, byte(difficulty), PN)
}

func IncreaseUserTask(c *gin.Context) (b bool, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	name := c.Query("name")
	label := c.Query("label")
	remark := c.Query("remark")
	difficultyS := c.Query("difficulty")
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	return models.IncreaseUserTask(operator, name, label, remark, byte(difficulty))
}
func DoneUserTask(c *gin.Context) (b bool, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	beginS := c.Query("begin")
	begin, _ := strconv.ParseInt(beginS, 10, 64)
	durationS := c.Query("duration")
	duration, _ := strconv.ParseInt(durationS, 10, 64)
	endS := c.Query("end")
	end, _ := strconv.ParseInt(endS, 10, 64)
	name := c.Query("name")
	difficultyS := c.Query("difficulty")
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	return models.DoneUserTask(operator, begin, duration, end, name, byte(difficulty))
}
func IncreaseGroupTask(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	name := c.Query("name")
	label := c.Query("label")
	remark := c.Query("remark")
	difficultyS := c.Query("difficulty")
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	if dao.GetPower(gid,operator)>0 {
		return models.IncreaseGroupTask(gid, name, label, remark, byte(difficulty))
	}
	return false,"unconditional"
}
func DoneGroupTask(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	beginS := c.Query("begin")
	begin, _ := strconv.ParseInt(beginS, 10, 64)
	durationS := c.Query("duration")
	duration, _ := strconv.ParseInt(durationS, 10, 64)
	endS := c.Query("end")
	end, _ := strconv.ParseInt(endS, 10, 64)
	name := c.Query("name")
	difficultyS := c.Query("difficulty")
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	if dao.GetPower(gid, operator) >= 0 {
		return models.DoneGroupTask(gid, operator, begin, duration, end, name, byte(difficulty))
	}
	return false,"unconditional"
}

func DoneImplement(c *gin.Context) (b bool, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	beginS := c.Query("begin")
	begin, _ := strconv.ParseInt(beginS, 10, 64)
	durationS := c.Query("duration")
	duration, _ := strconv.ParseInt(durationS, 10, 64)
	endS := c.Query("end")
	end, _ := strconv.ParseInt(endS, 10, 64)
	name := c.Query("name")
	difficultyS := c.Query("difficulty")
	difficulty, _ := strconv.ParseInt(difficultyS, 10, 8)
	return models.DoneImplement(operator, begin, duration, end, name, byte(difficulty))
}

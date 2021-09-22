package controller

import (
	"../service"
	"github.com/gin-gonic/gin"
	"github.com/hlccd/util/response"
)

func FindHabit(c *gin.Context) {
	list := service.FindHabit(c)
	response.Ok(c, list)
}
func FindUserTask(c *gin.Context) {
	list := service.FindUserTask(c)
	response.Ok(c, list)
}
func FindGroupTask(c *gin.Context) {
	list := service.FindGroupTask(c)
	response.Ok(c, list)
}
func FindImplement(c *gin.Context) {
	list := service.FindImplement(c)
	response.Ok(c, list)
}
func IncreaseHabit(c *gin.Context) {
	b, info := service.IncreaseHabit(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func DoneHabit(c *gin.Context) {
	b, info := service.DoneHabit(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func IncreaseUserTask(c *gin.Context) {
	b, info := service.IncreaseUserTask(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func DoneUserTask(c *gin.Context) {
	b, info := service.DoneUserTask(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}

func IncreaseGroupTask(c *gin.Context) {
	b, info := service.IncreaseGroupTask(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func DoneGroupTask(c *gin.Context) {
	b, info := service.DoneGroupTask(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func DoneImplement(c *gin.Context) {
	b, info := service.DoneImplement(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
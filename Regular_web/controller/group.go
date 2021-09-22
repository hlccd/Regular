package controller

import (
	"../service"
	"github.com/gin-gonic/gin"
	"github.com/hlccd/util/response"
)

func MyGroupList(c *gin.Context) {
	list := service.MyGroupList(c)
	response.Ok(c, list)
}
func FindMember(c *gin.Context) {
	list := service.FindMember(c)
	response.Ok(c, list)
}
func FindGroups(c *gin.Context) {
	list := service.FindGroups(c)
	response.Ok(c, list)
}
func Establish(c *gin.Context) {
	gid, info := service.Establish(c)
	if gid > 0 {
		response.Ok(c, gid)
	} else {
		response.Error(c, info, nil)
	}
}
func ChangeGroupName(c *gin.Context) {
	name, info := service.ChangeGroupName(c)
	if name!="" {
		response.Ok(c, name)
	} else {
		response.Error(c, info, nil)
	}
}
func Join(c *gin.Context) {
	b, info := service.Join(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}

func Kick(c *gin.Context) {
	b, info := service.Kick(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func ChangeMaster(c *gin.Context) {
	b, info := service.ChangeMaster(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func SetupAdministrator(c *gin.Context) {
	b, info := service.SetupAdministrator(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func CancelAdministrator(c *gin.Context) {
	b, info := service.CancelAdministrator(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
func CancelGroup(c *gin.Context) {
	b, info := service.CancelGroup(c)
	if b {
		response.Ok(c, 1)
	} else {
		response.Error(c, info, nil)
	}
}
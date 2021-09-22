package service

import (
	"../dao"
	"../models"
	"fmt"
	"github.com/gin-gonic/gin"
	"strconv"
)

func MyGroupList(c *gin.Context) (list []dao.GroupName) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return dao.MyGroupList(operator)
}
func FindMember(c *gin.Context) (list []int64) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	return models.FindMember(gid)
}
func FindGroups(c *gin.Context) (list []dao.GroupName) {
	return dao.FindGroups()
}

func Establish(c *gin.Context) (gid int64, info string) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	name := c.Query("name")
	b, gid, info := models.Establish(operator, name)
	if !b {
		return 0, info
	}
	return gid, info
}
func ChangeGroupName(c *gin.Context) (name string, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	name = c.Query("name")
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	if models.GetPower(gid, operator) > 0 {
		b, info := models.ChangeGroupName(gid, name)
		fmt.Println(gid, name)
		if !b {
			return "", info
		}
		return name, info
	}
	return "", "unconditional"
}
func Join(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return models.Join(operator, gid)
}
func Kick(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	uidS := c.Query("uid")
	uid, _ := strconv.ParseInt(uidS, 10, 64)
	if models.GetPower(gid, operator) > models.GetPower(gid, uid) {
		return models.Kick(uid, gid)
	}
	return false, "unconditional"
}
func ChangeMaster(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	uidS := c.Query("uid")
	uid, _ := strconv.ParseInt(uidS, 10, 64)
	if models.GetPower(gid, operator) == 2 {
		return models.ChangeMaster(gid, uid)
	}
	return false, "unconditional"
}
func SetupAdministrator(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	uidS := c.Query("uid")
	uid, _ := strconv.ParseInt(uidS, 10, 64)
	if models.GetPower(gid, operator) == 2 {
		if models.GetPower(gid, uid) == 0 {
			return models.SetupAdministrator(gid, uid)
		}
		return false, "the user is not common member"
	}
	return false, "unconditional"
}
func CancelAdministrator(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	uidS := c.Query("uid")
	uid, _ := strconv.ParseInt(uidS, 10, 64)
	if models.GetPower(gid, operator) == 2 {
		if models.GetPower(gid, uid) == 1 {
			return models.CancelAdministrator(gid, uid)
		}
		return false, "the user is not administrator"
	}
	return false, "unconditional"
}

func CancelGroup(c *gin.Context) (b bool, info string) {
	gidS := c.Query("gid")
	gid, _ := strconv.ParseInt(gidS, 10, 64)
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	if models.GetPower(gid, operator) == 2 {
		return models.CancelGroup(gid)
	}
	return false, "unconditional"
}

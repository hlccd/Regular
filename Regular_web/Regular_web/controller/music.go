package controller

import (
	"../service"
	"github.com/gin-gonic/gin"
	"github.com/hlccd/util/response"
)

func Md5Check(c *gin.Context) {
	sign := service.Md5Check(c)
	if sign==1 {
		response.Ok(c, nil)
	} else if sign==0 {
		response.Redirect(c, nil)
	}else {
		response.Error(c, "nil", nil)
	}
}
func MusicList(c *gin.Context) {
	list := service.MusicList(c)
	if list != nil {
		response.Ok(c, list)
	} else {
		response.Error(c, "nil", nil)
	}
}
func UploadMusic(c *gin.Context) {
	ok := service.UploadMusic(c)
	if ok {
		response.Ok(c, nil)
	} else {
		response.Error(c, "nil", nil)
	}
}
func ListenMusic(c *gin.Context) {
	file := service.ListenMusic(c)
	c.Writer.WriteString(string(file))
}
func HotMusic(c *gin.Context) {
	list := service.HotMusic(c)
	if list != nil {
		response.Ok(c, list)
	} else {
		response.Error(c, "nil", nil)
	}
}

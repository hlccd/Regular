package service

import (
	"../dao"
	"crypto/md5"
	"fmt"
	"github.com/gin-gonic/gin"
	"io"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"regexp"
	"strconv"
	"strings"
)

const path = "./file/"

func getFileMD5(file *multipart.FileHeader) (string, error) {
	f, _ := file.Open()
	md5Handle := md5.New()          //创建 md5 句柄
	_, err := io.Copy(md5Handle, f) //将文件内容拷贝到 md5 句柄中
	if nil != err {
		fmt.Println(err)
		return "", err
	}
	md := md5Handle.Sum(nil)        //计算 MD5 值，返回 []byte
	md5str := fmt.Sprintf("%x", md) //将 []byte 转为 string
	return md5str, nil
}
func Md5Check(c *gin.Context) (sign int) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	nameS := c.Query("name")
	singerS := c.Query("singer")
	duration, _ := strconv.Atoi(c.Query("duration"))
	md5 := c.Query("md5")
	return dao.Md5Check(operator, nameS,singerS, md5,duration)
}
func MusicList(c *gin.Context) (list []dao.Music) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	return dao.MusicList(operator)
}
func UploadMusic(c *gin.Context) (b bool) {
	operatorS, _ := c.Get("operator")
	operator, _ := strconv.ParseInt(fmt.Sprintf("%v", operatorS), 10, 64)
	nameS := c.Query("name")
	singerS := c.Query("singer")
	duration, _ := strconv.Atoi(c.Query("duration"))
	music, err := c.FormFile("music")
	md5, _ := getFileMD5(music)
	c.SaveUploadedFile(music, path+md5)
	if err == nil {
		return dao.UploadMusic(operator, nameS,singerS, md5,duration)
	}
	return false
}
func ListenMusic(c *gin.Context) (file []byte) {
	md5 := c.Query("md5")
	return dao.ListenMusic(md5, path)
}
func HotMusic(c *gin.Context) (list []dao.Song) {
	list=make([]dao.Song,0,0)
	id:=c.Query("id")
	client := &http.Client{}
	url := "http://music.163.com/discover/toplist?id="+id
	reqest, err := http.NewRequest("GET", url, nil)
	reqest.Header.Add("user-agent", "Chrome/10.0")
	if err != nil {
		return nil
	}
	res, _ := client.Do(reqest)
	defer res.Body.Close()
	body, _ := ioutil.ReadAll(res.Body)
	mes := string(body)
	compile := regexp.MustCompile(`<a href="/song\?id=\d*">.*</a>`)
	if compile == nil {
		return nil
	}
	result := compile.FindAllStringSubmatch(mes, 100)
	arr := strings.Split(result[0][0],"</li><li>")
	for i := range arr {
		s:=arr[i]
		s=s[:len(s)-4]
		song := strings.Split(s,">")
		list=append(list,dao.Song{song[1],(song[0])[18:len(song[0])-1]})
	}
	return list
}
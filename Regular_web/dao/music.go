package dao

import (
	"io/ioutil"
	"log"
)

type Music struct {
	Name     string `json:"name"`
	Singer   string `json:"singer"`
	Duration int    `json:"duration"`
	Md5      string `json:"md5"`
}
type Song struct {
	Name   string `json:"name"`
	ID     string `json:"id"`
}
func CreateMusicTab() {
	createMd5Tab()
	createMusicTab()
}
func createMd5Tab() {
	_, err := DB.Exec("create table `md5`(`md5` varchar(50) not null unique);")
	if err != nil {
		log.Println("create md5 tab fail:", err)
	}
}
func createMusicTab() {
	_, err := DB.Exec("create table `music`" +
		"(" +
		"`u_id`     bigint       not null references `user` (`u_id`)," +
		"`name`     varchar(100) not null default ''," +
		"`singer`   varchar(100) not null default ''," +
		"`duration` int          not null default 0," +
		"`md5`      varchar(50)  not null references `md5` (`md5`)," +
		"primary key (`u_id`,`md5`)" +
		");")
	if err != nil {
		log.Println("create music tab fail:", err)
	}
}
func Md5Check(operator int64, name, singer, md5 string, duration int) (sign int) {
	row, err := DB.Query("select count(*) from `md5` where `md5`=?", md5)
	defer row.Close()
	if err != nil {
		log.Println("get md5 fail:", err)
		return -1
	}
	num:=0
	if row.Next() {
		err := row.Scan(&num)
		if err != nil {
			return -1
		}
	}
	if num==0 {
		return 0
	}
	row1, err := DB.Query("select count(*) from `music` where `u_id`=? and `md5`=?",operator, md5)
	defer row1.Close()
	if err != nil {
		log.Println("get md5 fail:", err)
		return -1
	}
	if row1.Next() {
		err := row1.Scan(&num)
		if err != nil {
			return -1
		}
	}
	if num==1 {
		return 1
	}
	_, err = DB.Exec("insert into `music`(`u_id`,`name`,`singer`,`duration`,`md5`) values(?,?,?,?,?)", operator, name, singer, duration, md5)
	if err != nil {
		log.Println("insert music fail:", err)
		return -1
	}
	return 1
}
func MusicList(operator int64) (list []Music) {
	rows, err := DB.Query("select `name`,`singer`,`duration`,`md5` from `music` where `u_id`=?",operator)
	if err != nil {
		log.Println("get music list fail:", err)
		rows.Close()
		return nil
	}
	list = make([]Music, 0, 0)
	var m Music
	//查找成功则将所有群组ID加入list并返回
	for rows.Next() {
		err := rows.Scan(&m.Name, &m.Singer,&m.Duration,&m.Md5)
		if err != nil {
			rows.Close()
			return nil
		}
		list = append(list, m)
	}
	rows.Close()
	return list
}
func UploadMusic(operator int64, name, singer, md5 string, duration int) (b bool) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("get music affair begin fail:", err)
		conn.Rollback()
		return false
	}
	_, err = conn.Exec("insert into `music`(`u_id`,`name`,`singer`,`duration`,`md5`) values(?,?,?,?,?)", operator, name, singer, duration, md5)
	if err != nil {
		log.Println("insert music fail:", err)
		conn.Rollback()
		return false
	}
	_, err = conn.Exec("insert into `md5`(`md5`) values(?)", md5)
	if err != nil {
		log.Println("insert md5 fail:", err)
		conn.Rollback()
		return false
	}
	conn.Commit()
	return true
}
func ListenMusic(md5 string, path string) (file []byte) {
	file, _ = ioutil.ReadFile(path + md5)
	return file
}

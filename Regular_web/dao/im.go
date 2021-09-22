package dao

import (
	"log"
)

type Message struct {
	Mes       string `json:"mes"`
	Id        int64  `json:"id"`
	Receiver  int64  `json:"receiver"`
	Type      int8   `json:"type"`
	Group     int64  `json:"group"`
	TimeStamp int64  `json:"timestamp"`
}

func CreateMessageTab() {
	createMessageTab()
	createLoggingTab()
}
func createMessageTab() {
	_, err := DB.Exec("create table `message`" +
		"(" +
		"`message`   varchar(10000) not null ," +
		"`id`        bigint not null references `user` (u_id)," +
		"`receiver`  bigint not null references `user` (u_id)," +
		"`group`     bigint not null references `group` (g_id)," +
		"`type`      int," +
		"`timestamp` bigint" +
		")")
	if err != nil {
		log.Println("create message tab fail:", err)
	}
}
func createLoggingTab() {
	_, err := DB.Exec("create table `logging`" +
		"(" +
		"`message`   varchar(10000) not null ," +
		"`id`        bigint not null references `user` (u_id)," +
		"`receiver`  bigint not null references `user` (u_id)," +
		"`group`     bigint not null references `group` (g_id)," +
		"`type`      int," +
		"`timestamp` bigint" +
		")")
	if err != nil {
		log.Println("create logging tab fail:", err)
	}
}

func SaveLogging(mes Message) (b bool) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("save message logging affair begin fail:", err)
		conn.Rollback()
		return false
	}
	_, err = conn.Exec("insert into "+
		"`logging`(`message`, `id`, `receiver`, `group`, `type`, `timestamp`)"+
		"values(?, ?, ?, ?, ?, ?)",
		mes.Mes, mes.Id, mes.Receiver, mes.Group, mes.Type, mes.TimeStamp)
	if err != nil {
		log.Println("save message logging fail:", err)
		conn.Rollback()
		return false
	}
	//提交事务
	conn.Commit()
	return true
}
func ReleaseLogging(receiver int64) (mess []Message) {
	mess = make([]Message, 0, 0)
	conn, err := DB.Begin()
	if err != nil {
		log.Println("get message logging affair begin fail:", err)
		conn.Rollback()
		return nil
	}
	//查找该账号密码对应的用户名
	rows, err := conn.Query("select "+
		"`message`,`id`,`receiver`,`group`,`type`,`timestamp`"+
		" from `logging` where `receiver`=?",
		receiver)
	if err != nil {
		log.Println("get message logging fail:", err)
		rows.Close()
		conn.Rollback()
		return nil
	}
	//查找成功则将用户名读取到name中并返回同时提交事务
	for rows.Next() {
		var mes Message
		err := rows.Scan(&mes.Mes,
			&mes.Id,
			&mes.Receiver,
			&mes.Group,
			&mes.Type,
			&mes.TimeStamp)
		if err != nil {
			rows.Close()
			conn.Rollback()
			return nil
		}
		mess=append(mess,mes)
	}
	rows.Close()
	_, err = conn.Exec("delete from `logging` where `receiver`=?",
		receiver)
	if err != nil {
		log.Println("delete message logging fail:", err)
		conn.Rollback()
		return nil
	}
	conn.Commit()
	return mess
}

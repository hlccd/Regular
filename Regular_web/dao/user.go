package dao

/**
user数据处理
功能包括：
创建用户数据表

用户登录、注册、注销
用户改名、改密码、改手机号、改电子邮箱
用户新增积极习惯时长、新增消极习惯时长、新增计划完成记录
@author hlccd 2021.6.17
@version 1.0
*/

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"log"
	"time"
)

/**
User
用户信息结构体
结构体中属性包括：
用户ID、用户名、手机号、邮箱、权限、注册时间
积极习惯完成总时长、消极习惯完成总时长、计划执行总次数、计划执行总时间、藏书数量
*/

type User struct {
	U_id         int64  `json:"u_id"`
	Name         string `json:"name"`
	Phone        int64  `json:"phone"`
	Email        int64  `json:"email"`
	Jurisdiction byte   `json:"jurisdiction"`
	Registration int64  `json:"registration"`
	Positive     int64  `json:"positive"`
	Negative     int64  `json:"negative"`
	Number       int64  `json:"number"`
	Duration     int64  `json:"duration"`
	Books        int64  `json:"books"`
}
type IdName struct {
	Id   int64  `json:"id"`
	Name string `json:"name"`
}

func CreateUserTab() {
	createUserTab()
	createFriendTab()
}

/**
CreateUserTab
创建用户数据表
数据表中属性包括：
用户ID、用户名、密码、手机号、邮箱、权限、注册时间
积极习惯完成总时长、消极习惯完成总时长、计划执行总次数、计划执行总时间、藏书数量
*/

func createUserTab() {
	_, err := DB.Exec("create table `user`" +
		"(" +
		"`u_id`         bigint auto_increment," +
		"`name`         varchar(50) not null    default ''," +
		"`password`     varchar(50) not null    default ''," +
		"`phone`        bigint      null unique default null," +
		"`email`        varchar(50) null unique default null," +
		"`jurisdiction` tinyint     not null    default 1," +
		"`registration` bigint      not null," +
		"`positive`     bigint      not null    default 0," +
		"`negative`     bigint      not null    default 0," +
		"`number`       bigint      not null    default 0," +
		"`duration`     bigint      not null    default 0," +
		"`books`        bigint      not null    default 0," +
		"primary key (`u_id`)" +
		") auto_increment = 99999;")
	if err != nil {
		log.Println("create user tab fail:", err)
	}
}
func createFriendTab() {
	_, err := DB.Exec("create table `friend`" +
		"(" +
		"`friendA`  bigint	not null references `user` (u_id)," +
		"`friendB`  bigint	not null references `user` (u_id)," +
		"`type`     tinyint not null," +
		"primary key (`friendA`,`friendB`)" +
		");")
	if err != nil {
		log.Println("create friend tab fail:", err)
	}
}

/**
Login
用户登录
将用户ID和用户密码传入，若登录成功返回用户名
@param	uid			int64	用户id
@param	password	string	用户密码
@return b			bool 	登录是否成功，成功返回true，失败返回false
@return	name		string	用户名，失败返回""
@return	info		string	执行信息
*/

func Login(uid int64, password string) (b bool, name string, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user login affair begin fail:", err)
		conn.Rollback()
		return false, "", "user login affair begin fail"
	}
	//查找该账号密码对应的用户名
	rows, err := conn.Query("select `name` from `user` "+
		"where `u_id`=? and `password`=?",
		uid, password)
	if err != nil {
		log.Println("user login fail:", err)
		rows.Close()
		conn.Rollback()
		return false, "", "user login fail"
	}
	//查找成功则将用户名读取到name中并返回同时提交事务
	if rows.Next() {
		rows.Scan(&name)
		rows.Close()
		conn.Commit()
		return true, name, "user login success"
	}
	rows.Close()
	conn.Rollback()
	return false, "", "user login fail,The user was not found"
}

/**
Register
用户注册
将用户密码和用户名传入，注册成功返回true，否则返回false
@param	password	string	用户密码
@param	name   		string	用户名
@return b			bool 	登录成功返回true，失败返回false
@return	uid 		int64	用户ID，失败返回0
@return	info		string	执行信息
*/

func Register(password, name string) (b bool, uid int64, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user register affair begin fail:", err)
		conn.Rollback()
		return false, 0, "user register affair begin fail"
	}
	//插入用户数据
	row, err := conn.Exec("insert into "+
		"`user`(`u_id`, `name`, `password`, `phone`, `email`, `jurisdiction`, `registration`, `positive`, `negative`, `number`, `duration`, `books`)"+
		"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
		0, name, password, nil, nil, 1, time.Now().Unix(), 0, 0, 0, 0, 0)
	if err != nil {
		log.Println("user register fail:", err)
		conn.Rollback()
		return false, 0, "user register fail"
	}
	//获取该用户ID
	uid, err = row.LastInsertId()
	if err != nil {
		log.Println("user register get id fail:", err)
		conn.Rollback()
		return false, 0, "user register get id fail"
	}
	//提交事务
	conn.Commit()
	return true, uid, "user register success"
}

/**
GainUserMessage
查询用户信息
将用户ID传入，若返回用户各项信息
@param	uid			int64	用户id
@return b			bool 	登录是否成功，成功返回true，失败返回false
@return	user		User	用户各项信息
@return	info		string	执行信息
*/

func GainUserMessage(uid int64) (b bool, user User, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("get user message affair begin fail:", err)
		conn.Rollback()
		return false, user, "get user message affair begin fail"
	}
	//查找该账号密码对应的用户名
	rows, err := conn.Query("select "+
		"`u_id`,`name`,`phone`,`email`,`jurisdiction`,`registration`,`positive`,`negative`,`number`,`duration`,`books`"+
		" from `user` where `u_id`=?",
		uid)
	if err != nil {
		log.Println("get user message fail:", err)
		rows.Close()
		conn.Rollback()
		return false, user, "get user message fail"
	}
	//查找成功则将用户名读取到name中并返回同时提交事务
	if rows.Next() {
		rows.Scan(&user.U_id,
			&user.Name,
			&user.Phone,
			&user.Email,
			&user.Jurisdiction,
			&user.Registration,
			&user.Positive,
			&user.Negative,
			&user.Number,
			&user.Duration,
			&user.Books)
		rows.Close()
		conn.Commit()
		return true, user, "get user message success"
	}
	rows.Close()
	conn.Commit()
	return false, user, "get user message fail,The user was not found"
}

func SearchUsers(search string) (b bool, users []IdName, info string) {
	//开启事务
	conn, err := DB.Begin()
	var m map[int64]bool
	m = make(map[int64]bool)
	users = make([]IdName, 0, 0)
	if err != nil {
		log.Println("get users  affair begin fail:", err)
		conn.Rollback()
		return false, nil, "get users affair begin fail"
	}
	rows, err := conn.Query("select " +
		"`u_id`,`name`" +
		" from `user` where `u_id` like '" + search + "%'")
	if err != nil {
		log.Println("get users fail:", err)
		rows.Close()
		conn.Rollback()
		return false, nil, "get users fail"
	}
	//查找成功则将用户名读取到name中并返回同时提交事务
	for rows.Next() {
		var user IdName
		err := rows.Scan(&user.Id, &user.Name)
		if err != nil {
			rows.Close()
			conn.Rollback()
			return false, nil, "get users fail"
		}
		users = append(users, user)
		m[user.Id] = true
	}
	rows.Close()

	rows, err = conn.Query("select " +
		"`u_id`,`name`" +
		" from `user` where `name` like '%" + search + "%'")
	if err != nil {
		log.Println("get users fail:", err)
		rows.Close()
		conn.Rollback()
		return false, nil, "get users fail"
	}
	//查找成功则将用户名读取到name中并返回同时提交事务
	for rows.Next() {
		var user IdName
		err := rows.Scan(&user.Id, &user.Name)
		if err != nil {
			rows.Close()
			conn.Rollback()
			return false, nil, "get users fail"
		}
		_, ok := m[user.Id]
		if !ok {
			users = append(users, user)
			m[user.Id] = true
		}
	}
	rows.Close()
	conn.Commit()
	return true, users, "get users success"
}

// MakeFriend 预设为好友
func MakeFriend(friendA, friendB int64) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("make friend affair begin fail:", err)
		conn.Rollback()
		return false, "make friend affair begin fail"
	}
	//插入用户数据
	_, err = conn.Exec("insert into "+
		"`friend`(`friendA`, `friendB`, `type`)"+
		"values(?, ?, ?)",
		friendA, friendB, -1)
	if err != nil {
		log.Println("make friend fail:", err)
		conn.Rollback()
		return false, "make friend fail"
	}
	//提交事务
	conn.Commit()
	return true, "make friend success"
}

// ChangeFriend 修改好友状态
func ChangeFriend(friendA, friendB int64, t int8) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("change friend affair begin fail:", err)
		conn.Rollback()
		return false, "change friend affair begin fail"
	}
	//修改用户名
	_, err = conn.Exec("update `friend` set `type` =? where (`friendA`=? and `friendB`=?) or (`friendA`=? and `friendB`=?);",
		t, friendA, friendB, friendB, friendA)
	if err != nil {
		log.Println("change friend fail:", err)
		conn.Rollback()
		return false, "change friend fail"
	}
	//提交事务
	conn.Commit()
	return true, "change friend success"
}

// DeleteFriend 删除好友
func DeleteFriend(friendA, friendB int64) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("make friend affair begin fail:", err)
		conn.Rollback()
		return false, "delete friend affair begin fail"
	}

	_, err = conn.Exec("delete from `friend` where (`friendA`=? and `friendB`=?) or (`friendA`=? and `friendB`=?);",
		friendA, friendB, friendB, friendA)
	if err != nil {
		log.Println("delete friend fail:", err)
		conn.Rollback()
		return false, "delete friend fail"
	}
	//提交事务
	conn.Commit()
	return true, "delete friend success"
}

func FriendList(uid int64) (list []IdName) {
	list = make([]IdName, 0, 0)
	conn, err := DB.Begin()
	if err != nil {
		log.Println("get friend list affair fail:", err)
		conn.Rollback()
		return nil
	}
	rows, err := conn.Query("select `u_id`,`name` from `user` " +
		"where `u_id`<>? and (" +
		"(`u_id` in (select `friendA` from `friend` where `type`>=0 and `friendB`=?))" +
		" or " +
		"(`u_id` in (select `friendB` from `friend` where `type`>=0 and `friendA`=?))" +
		")",
		uid,uid,uid)
	if err != nil {
		log.Println("get friend list fail:", err)
		rows.Close()
		conn.Rollback()
		return nil
	}
	for rows.Next() {
		var in IdName
		err := rows.Scan(&in.Id,&in.Name)
		if err != nil {
			rows.Close()
			conn.Rollback()
			return nil
		}
		list = append(list, in)
	}
	rows.Close()
	conn.Commit()
	return list
}

/**
ChangeUserName
修改用户名
将用户ID和新用户名传入，修改成功返回true，否则返回false
@param	uid			int64	用户ID
@param	name   		string	新用户名
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeUserName(uid int64, name string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change name affair begin fail:", err)
		conn.Rollback()
		return false, "user change name affair begin"
	}
	//修改用户名
	_, err = conn.Exec("update `user` set `name` =? where `u_id`=?;",
		name, uid)
	if err != nil {
		log.Println("user change name fail:", err)
		conn.Rollback()
		return false, "user change name fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change name success"
}

/**
ChangePassword
修改用户密码
将用户ID和新用户密码传入，修改成功返回true，否则返回false
@param	uid			int64	用户ID
@param	password   	string	新用户密码
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangePassword(uid int64, password string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change password affair begin fail:", err)
		conn.Rollback()
		return false, "user change password affair begin"
	}
	//修改用户密码
	_, err = conn.Exec("update `user` set `password` =? where `u_id`=?",
		password, uid)
	if err != nil {
		log.Println("user change password affair fail:", err)
		conn.Rollback()
		return false, "user change password affair fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change password affair success"
}

/**
ChangePhone
修改用户手机号
将用户ID和新用户手机号传入，修改成功返回true，否则返回false
@param	uid			int64	用户ID
@param	phone   	int64	用户手机号
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangePhone(uid, phone int64) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change phone affair begin fail:", err)
		conn.Rollback()
		return false, "user change phone affair begin fail"
	}
	//修改手机号
	_, err = conn.Exec("update `user` set `phone` =? where `u_id`=?",
		phone, uid)
	if err != nil {
		log.Println("user change phone fail:", err)
		conn.Rollback()
		return false, "user change phone fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change phone success"
}

/**
ChangeEmail
修改用户电子邮箱
将用户ID和新用户电子邮箱传入，修改成功返回true，否则返回false
@param	uid			int64	用户ID
@param	email   	string	用户电子邮箱
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeEmail(uid int64, email string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change email affair begin fail:", err)
		conn.Rollback()
		return false, "user change email affair begin fail"
	}
	//修改电子邮箱
	_, err = conn.Exec("update `user` set `email` =? where `u_id`=?;",
		email, uid)
	if err != nil {
		log.Println("user change email fail:", err)
		conn.Rollback()
		return false, "user change email fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change email success"
}

/**
addPositive
新增用户的积极习惯时长
将用户ID和新增的积极习惯时长传入，增加成功返回true，否则返回false
@param 	conn		*sql.Tx	事务链接的上下文
@param	uid			int64	用户ID
@param	positive   	int64	新增的积极习惯时长
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/

func addPositive(conn *sql.Tx, uid, positive int64) (b bool, info string) {
	//新增积极习惯时长
	_, err := conn.Exec("update `user` set `positive` =`positive`+? where `u_id`=?;",
		positive, uid)
	if err != nil {
		log.Println("user add positive duration fail:", err)
		conn.Rollback()
		return false, "user add positive duration fail"
	}
	return true, "user add positive duration success"
}

/**
addNegative
新增用户的消极习惯时长
将用户ID和新增的消极习惯时长传入，增加成功返回true，否则返回false
@param 	conn		*sql.Tx	事务链接的上下文
@param	uid			int64	用户ID
@param	negative   	int64	新增的消极习惯时长
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/
func addNegative(conn *sql.Tx, uid, negative int64) (b bool, info string) {
	//新增消极习惯时长
	_, err := conn.Exec("update `user` set `negative` =`negative`+? where `u_id`=?;",
		negative, uid)
	if err != nil {
		log.Println("user add negative duration fail:", err)
		conn.Rollback()
		return false, "user add negative duration fail"
	}
	return true, "user add negative duration success"
}

/**
addDuration
新增用户的计划完成记录
将用户ID和新增的计划完成记录，增加成功返回true，否则返回false
@param 	conn		*sql.Tx	事务链接的上下文
@param	uid			int64	用户ID
@param	duration   	int64	新增的计划完成记录
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/
func addDuration(conn *sql.Tx, uid, duration int64) (b bool, info string) {
	//新增消极习惯时长
	_, err := conn.Exec("update `user` set `duration` =`duration`+? , `number`=`number`+1 where `u_id`=?;",
		duration, uid)
	if err != nil {
		log.Println("user add duration fail:", err)
		conn.Rollback()
		return false, "user add duration fail"
	}
	return true, "user add duration success"
}

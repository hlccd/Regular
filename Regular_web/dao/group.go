package dao

/**
group数据处理
功能包括：
创建群组表和成员表

群组创建，修改群名，获取成员列表，获取群组列表
获取用户权限，修改用户权限
添加成员，踢出成员，更改群主，设置管理员，取消管理员
增加群成员完成群任务记录
注销群组
@author hlccd 2021.6.17
@version 1.0
*/
import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"log"
)

type GroupName struct {
	Group int64  `json:"group"`
	Name  string `json:"name"`
}

/**
CreateGroupTab
创建规划数据汇总表，统一创建group中的两张表
数据表包括：
群组总表和群组成员总表
*/

func CreateGroupTab() {
	createGroupTab()
	createMemberTab()
}

/**
createGroupTab
获取群组列表
数据表中属性包括：
群组ID，群主ID，群组名，任务执行次数，任务执行总时长
*/
func createGroupTab() {
	_, err := DB.Exec("create table `group`" +
		"(" +
		"`g_id`     bigint auto_increment," +
		"`u_id`     bigint       not null references `user` (u_id)," +
		"`name`     varchar(100) not null default ''," +
		"`number`   bigint       not null default 0," +
		"`duration` bigint       not null default 0," +
		"primary key (`g_id`)" +
		") auto_increment = 99999;")
	if err != nil {
		log.Println("create group tab fail:", err)
	}
}

/**
createMemberTab
创建群组成员总表
数据表中属性包括：
群组ID，成员ID，成员权限，该成员在该群组任务中执行总次数，该成员在该群组任务中执行总时长
*/
func createMemberTab() {
	_, err := DB.Exec("create table `member`" +
		"(" +
		"`g_id`     bigint  not null references `group` (g_id)," +
		"`u_id`     bigint  not null references `user` (u_id)," +
		"`power`    tinyint not null default 0," +
		"`number`   bigint  not null default 0," +
		"`duration` bigint  not null default 0," +
		"primary key (`g_id`, `u_id`)" +
		");")
	if err != nil {
		log.Println("create member tab fail:", err)
	}
}

func MyGroupList(uid int64) (list []GroupName) {
	//开启事务
	list = make([]GroupName, 0, 0)
	conn, err := DB.Begin()
	if err != nil {
		log.Println("get my group list affair begin fail:", err)
		conn.Rollback()
		return nil
	}
	rows, err := conn.Query("select `g_id`,`name` from `group` where `g_id` in (select `g_id` from `member` where `u_id`=?)",
		uid)
	if err != nil {
		log.Println("get my group list fail:", err)
		conn.Rollback()
		return nil
	}
	for rows.Next() {
		var gn GroupName
		err := rows.Scan(&gn.Group, &gn.Name)
		if err != nil {
			rows.Close()
			conn.Rollback()
			return nil
		}
		list = append(list, gn)
	}
	//提交事务
	conn.Commit()
	return list
}

/**
Establish
创建群组
将创建者ID和群名传入进行创建，创建成功返回true，失败返回false
@param	uid			int64	创建者ID
@param	name   		string	群名
@return b			bool 	创建群组成功返回true，失败返回false
@return	gid 		int64	群组ID，失败返回0
@return	info		string	执行信息
*/

func Establish(uid int64, name string) (b bool, gid int64, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("group establish affair begin fail:", err)
		conn.Rollback()
		return false, 0, "group establish affair begin fail"
	}
	//插入群组数据
	row, err := conn.Exec("insert into `group`(`g_id`, `u_id`, `name`, `number`, `duration`) values(?, ?, ?, ?, ?)",
		0, uid, name, 0, 0)
	if err != nil {
		log.Println("group establish fail:", err)
		conn.Rollback()
		return false, 0, "group establish fail"
	}
	//获取该群组ID
	gid, err = row.LastInsertId()
	if err != nil {
		log.Println("group establish get id fail:", err)
		conn.Rollback()
		return false, 0, "group establish get id fail"
	}
	//插入群主信息
	_, err = conn.Exec("insert into `member`(`g_id`, `u_id`, `power`, `number`, `duration`) values(?, ?, ?, ?, ?)",
		gid, uid, 2, 0, 0)
	if err != nil {
		log.Println("insert group master fail:", err)
		conn.Rollback()
		return false, 0, "insert group master fail"
	}
	//提交事务
	conn.Commit()
	return true, gid, "group establish success"
}

func GetAdministrator(gid int64) (list []int64){
	list=make([]int64,0,0)
	rows, err := DB.Query("select `u_id` from `member` where `g_id`=? and `power`>0",
		gid)
	if err != nil {
		log.Println("get administrator fail:", err)
		rows.Close()
		return nil
	}
	//查找成功则将权限读取并进行验证
	var uid int64
	for rows.Next() {
		err := rows.Scan(&uid)
		if err != nil {
			rows.Close()
			return nil
		}
		list=append(list,uid)
	}
	rows.Close()
	return list
}

/**
ChangeGroupName
修改群名
将群组ID和新群名传入，修改成功返回true，否则返回false
@param	gid			int64	群组ID
@param	name   		string	新群名
@return b			bool 	改名成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeGroupName(gid int64, name string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("group change name affair begin fail:", err)
		conn.Rollback()
		return false, "group change name affair begin"
	}
	_, err = conn.Exec("update `group` set `name` =? where `g_id`=?;",
		name, gid)
	if err != nil {
		log.Println("user group name fail:", err)
		conn.Rollback()
		return false, "user group name fail"
	}
	//提交事务
	conn.Commit()
	return true, "change group name success"
}

/**
FindMember
获取成员列表
@param	gid			int64	群组ID
@return list		[]int64 成员列表
*/

func FindMember(gid int64) (list []int64) {
	rows, err := DB.Query("select `u_id` from `member` where `g_id`=?",
		gid)
	if err != nil {
		log.Println("get group member fail:", err)
		rows.Close()
		return nil
	}
	list = make([]int64, 0, 0)
	var uid int64
	for rows.Next() {
		rows.Scan(&uid)
		list = append(list, uid)
	}
	return list
}

/**
FindGroup
查找所有群组ID
@return list		[]int64 符合该部分ID的所有群组ID
*/

func FindGroups() (list []GroupName) {
	//查询所有群组
	rows, err := DB.Query("select `g_id`,`name` from `group` where `g_id`>0")
	if err != nil {
		log.Println("get groups list fail:", err)
		rows.Close()
		return nil
	}
	list = make([]GroupName, 0, 0)
	var gn GroupName
	//查找成功则将所有群组ID加入list并返回
	for rows.Next() {
		err := rows.Scan(&gn.Group, &gn.Name)
		if err != nil {
			rows.Close()
			return nil
		}
		list = append(list, gn)
	}
	rows.Close()
	return list
}

/**
GetPower
获取用户在本群的权限，若不在群内或失败则返回-1,否则返回对应权限
@param	gid			int64	群组ID
@param	uid			int64	用户ID
@return power		int 	用户权限
*/

func GetPower(gid, uid int64) (power int) {
	//查询用户在该群中的权限
	row, err := DB.Query("select `power` from `member` where `g_id`=? and `u_id`=?",
		gid, uid)
	if err != nil {
		log.Println("get user power fail:", err)
		row.Close()
		return -1
	}
	//查找成功则将权限读取并进行验证
	if row.Next() {
		row.Scan(&power)
		row.Close()
		return power
	}
	row.Close()
	return -1
}

/**
setPower
修改用户在本群的权限,成功返回true，失败返回false
@param 	conn		*sql.Tx	事务链接的上下文
@param	gid			int64	群组ID
@param	uid			int64	用户ID
@param	power		int		权限
@return b			bool 	成功返回true，失败返回false
*/
func setPower(conn *sql.Tx, gid, uid int64, power int) (b bool) {
	//查询用户在该群中的权限
	_, err := conn.Exec("update `member` set `power` =? where `g_id`=? and `u_id`=?;",
		power, gid, uid)
	if err != nil {
		log.Println("set user power fail:", err)
		conn.Rollback()
		return false
	}
	return true
}

/**
Join
添加成员
将待添加成员ID和群ID传入进行添加，添加成功返回true，失败返回false
@param	uid			int64	成员ID
@param	gid   		int64	群组ID
@return b			bool 	添加成员成功返回true，失败返回false
@return	info		string	执行信息
*/

func Join(uid, gid int64) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("member join affair begin fail:", err)
		conn.Rollback()
		return false, "member join affair begin fail"
	}
	//插入成员信息
	_, err = conn.Exec("insert into `member`(`g_id`, `u_id`, `power`, `number`, `duration`) values(?, ?, ?, ?, ?)",
		gid, uid, 0, 0, 0)
	if err != nil {
		log.Println("insert group member fail:", err)
		conn.Rollback()
		return false, "insert group member fail"
	}
	//提交事务
	conn.Commit()
	return true, "member join success"
}

/**
Kick
踢出成员
将被踢成员ID和群ID传入进行添加，添加成功返回true，失败返回false
@param	uid			int64	成员ID
@param	gid   		int64	群组ID
@return b			bool 	踢出成员成功返回true，失败返回false
@return	info		string	执行信息
*/

func Kick(uid, gid int64) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("member kick affair begin fail:", err)
		conn.Rollback()
		return false, "member kick affair begin fail"
	}
	//进行踢人
	_, err = conn.Exec("delete from `member` where `g_id`=? and `u_id` =? and `power`=0",
		gid, uid)
	if err != nil {
		log.Println("kick group member fail:", err)
		conn.Rollback()
		return false, "kick group member fail"
	}
	//提交事务
	conn.Commit()
	return true, "kick group member success"
}

/**
ChangeMaster
更换群主
@param	gid			int64	群组ID
@param	master		int64	新群主ID
@return b			bool 	修改群主成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeMaster(gid, master int64) (b bool, info string) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("change master affair begin fail:", err)
		conn.Rollback()
		return false, "change master affair begin fail"
	}
	//查询原来的群主
	row, err := conn.Query("select `u_id` from `group` where `g_id`=?",
		gid)
	if err != nil {
		log.Println("get old group master fail:", err)
		row.Close()
		conn.Rollback()
		return false, "get old group master fail"
	}
	var user int64
	if row.Next() {
		row.Scan(&user)
		row.Close()
		//修改群主和新旧群主的权限
		_, err = conn.Exec("update `group` set `u_id` =? where `g_id`=?;",
			master, gid)
		if err != nil {
			log.Println("change master fail:", err)
			conn.Rollback()
			return false, "change master fail"
		}
		if !setPower(conn, gid, master, 2) {
			conn.Rollback()
			return false, "change master fail"
		}
		if !setPower(conn, gid, user, 0) {
			conn.Rollback()
			return false, "change master fail"
		}
		//提交事务
		conn.Commit()
		return true, "change group master success"
	}
	row.Close()
	conn.Rollback()
	return false, "get old group master fail"
}

/**
SetupAdministrator
设置管理员
@param	gid			int64	群组ID
@param	uid			int64	管理员ID
@return b			bool 	设置管理员成功返回true，失败返回false
@return	info		string	执行信息
*/

func SetupAdministrator(gid, uid int64) (b bool, info string) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("setup administrator affair begin fail:", err)
		conn.Rollback()
		return false, "setup administrator affair begin fail"
	}
	if !setPower(conn, gid, uid, 1) {
		conn.Rollback()
		return false, "setup administrator fail"
	}
	conn.Commit()
	return true, "setup administrator success"
}

/**
CancelAdministrator
取消管理员
@param	gid			int64	群组ID
@param	uid			int64	待取消的管理员ID
@return b			bool 	取消管理员成功返回true，失败返回false
@return	info		string	执行信息
*/

func CancelAdministrator(gid, uid int64) (b bool, info string) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("cancel administrator affair begin fail:", err)
		conn.Rollback()
		return false, "cancel administrator affair begin fail"
	}
	if !setPower(conn, gid, uid, 0) {
		conn.Rollback()
		return false, "cancel administrator fail"
	}
	conn.Commit()
	return true, "cancel administrator success"
}

/**
addRecord
增加群成员完成群任务记录
@param 	conn		*sql.Tx	事务链接的上下文
@param	uid			int64	成员ID
@param	gid   		int64	群组ID
@param	duration   	int64	任务完成耗费时长
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func addRecord(conn *sql.Tx, uid, gid, duration int64) (b bool, info string) {
	//在群组中增加记录
	_, err := conn.Exec("update `group` set `number` =`number`+1,`duration`=`duration`+? where `g_id`=?;",
		duration, gid)
	if err != nil {
		log.Println("insert group record fail:", err)
		conn.Rollback()
		return false, "insert group record fail"
	}
	//在对该成员添加记录
	_, err = conn.Exec("update `member` set `number` =`number`+1,`duration`=`duration`+? where `g_id`=? and `u_id`=?;",
		duration, gid, uid)
	if err != nil {
		log.Println("insert member record fail:", err)
		conn.Rollback()
		return false, "insert member record fail"
	}
	return true, "insert group record success"
}

/**
CancelGroup
注销群组
@param	gid			int64	群组ID
@return b			bool 	注销成功返回true，失败返回false
@return	info		string	执行信息
*/

func CancelGroup(gid int64) (b bool, info string) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("cancel group affair begin fail:", err)
		conn.Rollback()
		return false, "cancel group affair begin fail"
	}
	_, err = conn.Exec("delete from `member` where `g_id`=?",
		gid)
	if err != nil {
		log.Println("delete all member fail:", err)
		conn.Rollback()
		return false, "delete all member fail"
	}
	_, err = conn.Exec("delete from `group` where `g_id`=?",
		gid)
	if err != nil {
		log.Println("delete group fail:", err)
		conn.Rollback()
		return false, "delete group fail"
	}
	conn.Commit()
	return true, "cancel group success"
}

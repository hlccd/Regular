package dao

/**
project数据处理
功能包括：
创建规划数据汇总表,其中包括:
规划标签表、习惯表、个人任务表、群组任务表、执行情况记录表

查找个人习惯，查找个人任务，查找群组任务
添加标签，添加执行记录
新增习惯，新增个人任务，新增群组任务
完成习惯，完成个人任务，完成群组任务
修改习惯备注，修改个人任务备注，修改群组任务备注
@author hlccd 2021.6.18
@version 1.0
*/
import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"log"
)

type Habit struct {
	Name       string `json:"name"`
	Label      string `json:"label"`
	Difficulty byte   `json:"difficulty"`
	Positive   int64  `json:"positive"`
	Negative   int64  `json:"negative"`
	Remark     string `json:"remark"`
}
type Task struct {
	Name       string `json:"name"`
	Label      string `json:"label"`
	Difficulty byte   `json:"difficulty"`
	Duration   int64  `json:"duration"`
	Number     int64  `json:"number"`
	Remark     string `json:"remark"`
}
type Implement struct {
	Name       string `json:"name"`
	Difficulty byte   `json:"difficulty"`
	Begin      int64  `json:"begin"`
	Duration   int64  `json:"duration"`
	End        int64  `json:"end"`
}

/**
CreateProjectTab
创建规划数据汇总表，统一创建project中的五张表
数据表包括：
规划标签表、习惯表、个人任务表、群组任务表、执行情况记录表
*/

func CreateProjectTab() {
	createProjectLabelTab()
	createProjectHabitTab()
	createProjectUserTaskTab()
	createProjectGroupTaskTab()
	createProjectImplementTab()
}

/**
createProjectLabelTab
创建规划标签表
数据表中属性包括：
标签名，标签使用次数
*/
func createProjectLabelTab() {
	_, err := DB.Exec("create table `label`" +
		"(" +
		"`l_name`        varchar(20)," +
		"`number`        bigint not null default 0," +
		"primary key (`l_name`)" +
		");")
	if err != nil {
		log.Println("create project label tab fail:", err)
	}
}

/**
createProjectHabitTab
创建规划习惯表
数据表中属性包括：
习惯名，所属用户，标签名，难度，积极时长，消极时长，重置周期，备注
*/
func createProjectHabitTab() {
	_, err := DB.Exec("create table `habit`" +
		"(" +
		"`h_name`      varchar(20)  default ''," +
		"`u_id`        bigint       references `user` (u_id)," +
		"`l_name`      varchar(20)  not null references `label` (l_name)," +
		"`difficulty`  tinyint      not null default 1," +
		"`positive`    bigint       not null default false," +
		"`negative`    bigint       not null default false," +
		"`remark`      varchar(200) not null default ''," +
		"primary key (`h_name`,`u_id`)" +
		")auto_increment = 1;")
	if err != nil {
		log.Println("create project habit tab fail:", err)
	}
}

/**
createProjectTaskTab
创建规划个人任务表
数据表中属性包括：
任务ID，任务名字，用户ID，标签名，难度，完成次数，时长，备注
*/
func createProjectUserTaskTab() {
	_, err := DB.Exec("create table `user_task`" +
		"(" +
		"`u_t_name`   varchar(20)  not null default ''," +
		"`u_id`       bigint       not null unique references `user` (u_id)," +
		"`l_name`     varchar(20)  not null references `label` (l_name)," +
		"`difficulty` tinyint      not null default 1," +
		"`duration`   bigint       not null default 0," +
		"`number`     bigint       not null default 0," +
		"`remark`     varchar(200) not null default ''," +
		"primary key (`u_t_name`,`u_id`)" +
		");")
	if err != nil {
		log.Println("create project user task tab fail:", err)
	}
}

/**
createProjectGroupTaskTab
创建规划群组任务表
数据表中属性包括：
任务ID，任务名字，群组ID，标签名，难度，完成次数，时长，备注
*/
func createProjectGroupTaskTab() {
	_, err := DB.Exec("create table `group_task`" +
		"(" +
		"`g_t_name`   varchar(20)  not null," +
		"`g_id`       bigint       not null unique references `group` (g_id)," +
		"`l_name`     varchar(20)  not null references `label` (l_name)," +
		"`difficulty` tinyint      not null default 1," +
		"`duration`   bigint       not null default 0," +
		"`number`     bigint       not null default 0," +
		"`remark`     varchar(200) not null default ''," +
		"primary key (`g_t_name`,`g_id`)" +
		");")
	if err != nil {
		log.Println("create project group task tab fail:", err)
	}
}

/**
createProjectImplementTab
创建执行情况记录表
数据表中属性包括：
执行记录ID，名字，用户ID，难度，开始时间戳，结束时间戳
*/
func createProjectImplementTab() {
	_, err := DB.Exec("create table `implement`" +
		"(" +
		"`i_id`       bigint auto_increment," +
		"`i_name`     varchar(20) not null default ''," +
		"`u_id`       bigint      not null references `user` (u_id)," +
		"`difficulty` tinyint     not null default 1," +
		"`begin`      bigint      not null default 0," +
		"`duration`   bigint      not null default 0," +
		"`end`        bigint      not null default 0," +
		"primary key (`i_id`)" +
		")auto_increment = 1;")
	if err != nil {
		log.Println("create project implement tab fail:", err)
	}
}

/**
FindHabit
查找个人习惯
输入用户id返回该用户的所有记录的习惯
@param	uid			int64		用户id
@return list		[]Habit		该用户的所有习惯
*/

func FindHabit(uid int64) (list []Habit) {
	//查询所用用户ID中含有该局部ID的习惯ID列表
	rows, err := DB.Query("select `h_name`,`l_name`,`difficulty`,`positive`,`negative`,`remark`"+
		" from `habit` where `u_id`=?", uid)
	if err != nil {
		log.Println("find habit fail:", err)
		rows.Close()
		return nil
	}
	list = make([]Habit, 0, 0)
	var habit Habit
	//查找成功则将所有习惯名添加进去
	for rows.Next() {
		rows.Scan(&habit.Name, &habit.Label, &habit.Difficulty, &habit.Positive, &habit.Negative, &habit.Remark)
		list = append(list, habit)
	}
	rows.Close()
	return list
}

/**
FindUserTask
查找个人任务
输入用户id返回该用户的所有记录的任务
@param	uid			int64		用户部分id
@return list		[]Task		该用户的的所有任务
*/

func FindUserTask(uid int64) (list []Task) {
	//查询所用用户ID中含有该局部ID的用户ID列表
	rows, err := DB.Query("select `u_t_name`,`l_name`,`difficulty`,`duration`,`number`,`remark`"+
		" from `user_task` where `u_id`=?", uid)
	if err != nil {
		log.Println("find user task fail:", err)
		rows.Close()
		return nil
	}
	list = make([]Task, 0, 0)
	var task Task
	//查找成功则将所有个人任务名添加进去
	for rows.Next() {
		rows.Scan(&task.Name, &task.Label, &task.Difficulty, &task.Duration, &task.Number, &task.Remark)
		list = append(list, task)
	}
	rows.Close()
	return list
}

/**
FindGroupTask
查找群组任务
输入群组id返回该群组的所有记录的任务
@param	uid			int64		用户部分id
@return list		[]Task		该用户的的所有任务
*/

func FindGroupTask(gid int64) (list []Task) {
	//查询所用用户ID中含有该局部ID的用户ID列表
	rows, err := DB.Query("select `g_t_name`,`l_name`,`difficulty`,`duration`,`number`,`remark`"+
		" from `group_task` where `g_id`=?", gid)
	if err != nil {
		log.Println("find group task fail:", err)
		rows.Close()
		return nil
	}
	list = make([]Task, 0, 0)
	var task Task
	//查找成功则将所有群组任务名添加进去
	for rows.Next() {
		rows.Scan(&task.Name, &task.Label, &task.Difficulty, &task.Duration, &task.Number, &task.Remark)
		list = append(list, task)
	}
	rows.Close()
	return list
}

/**
FindImplement
查找个人执行情况
输入用户id返回该用户的所有记录的执行情况
@param	uid			int64		用户id
@return list		[]Implement	该用户的所有执行记录
*/

func FindImplement(uid int64) (list []Implement) {
	//查询所用用户ID中含有该局部ID的习惯ID列表
	rows, err := DB.Query("select `i_name`,`difficulty`,`begin`,`duration`,`end`" +
		" from `implement` where `u_id`=?", uid)
	if err != nil {
		log.Println("find implement fail:", err)
		rows.Close()
		return nil
	}
	list = make([]Implement, 0, 0)
	var implement Implement
	//查找成功则将所有习惯名添加进去
	for rows.Next() {
		rows.Scan(&implement.Name, &implement.Difficulty, &implement.Begin, &implement.Duration, &implement.End)
		list = append(list, implement)
	}
	rows.Close()
	return list
}

/**
addLabel
添加标签
若标签不存在则新增标签，若标签已存在则在原有数量上+1
@param 	conn		*sql.Tx	事务链接的上下文
@param	name		string	标签名
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/
func addLabel(conn *sql.Tx, name string) (b bool, info string) {
	//查询该标签是否已经存在
	rows, err := DB.Query("select 1 from `label` where `l_name`=? limit 1;",
		name)
	if err != nil {
		log.Println("find the label fail:", err)
		rows.Close()
		return false, "find the label fail"
	}
	//将查找结果输入num中检测是否存在该标签
	var num int
	if rows.Next() {
		rows.Scan(&num)
	}
	rows.Close()
	if num == 1 {
		//该标签存在于表中
		//将该标签数量+1
		_, err = conn.Exec("update `label` set `number` =`number`+1 where `l_name`=? ;",
			name)
		if err != nil {
			log.Println("increase the number of label fail:", err)
			conn.Rollback()
			return false, "increase the number of label fail"
		}
	} else {
		//该标签不在表中
		//新增该标签记录
		_, err = conn.Exec("insert into `label`(`l_name`, `number`) values(?, ?)",
			name, 1)
		if err != nil {
			log.Println("insert the label fail:", err)
			conn.Rollback()
			return false, "insert the label fail"
		}
	}
	return true, "add the label success"
}

/**
addImplement
添加执行记录,添加成功返回true,失败返回false
@param 	conn		*sql.Tx	事务链接的上下文
@param	uid			int64	用户ID
@param	begin		int64	开始时间戳
@param	end			int64	结束时间戳
@param	name		string	名字
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func addImplement(conn *sql.Tx, uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	//新增执行记录
	_, err := conn.Exec("insert into " +
		"`implement`(`i_id`, `i_name`, `u_id`, `difficulty`, `begin`,`duration`, `end`) " +
		"values(?, ?, ?, ?, ?, ?, ?)",
		0, name, uid, difficulty, begin, duration, end)
	if err != nil {
		log.Println("add implement fail:", err)
		conn.Rollback()
		return false, "add implement fail"
	}
	b, info = addDuration(conn, uid, end-begin)
	if !b {
		log.Println("add duration fail:", err)
		conn.Rollback()
		return false, "add duration fail"
	}
	return true, "add implement success"
}

/**
IncreaseHabit
添加个人习惯
@param	uid			int64	用户ID
@param	name		string	习惯名
@param	label		string	标签名
@param	remark		string	备注
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func IncreaseHabit(uid int64, name, label, remark string, difficulty byte) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("increase habit affair begin fail:", err)
		conn.Rollback()
		return false, "increase habit affair begin fail"
	}
	//将习惯标签添加到标签表中去
	b, info = addLabel(conn, label)
	if !b {
		log.Println("increase habit but add label fail:", err)
		conn.Rollback()
		return false, "increase habit but add label fail"
	}
	//添加习惯
	_, err = conn.Exec("insert into "+
		"`habit`(`h_name`, `u_id`, `l_name`, `difficulty`, `positive`, `negative`, `remark`)"+
		"values(?, ?, ?, ?, ?, ?, ?)",
		name, uid, label, difficulty, 0, 0, remark)
	if err != nil {
		log.Println("insert habit fail:", err)
		conn.Rollback()
		return false, "insert habit fail"
	}
	//提交事务
	conn.Commit()
	return true, "insert habit success"
}

/**
DoneHabit
添加个人习惯完成记录
@param	uid			int64	用户ID
@param	begin		int64	开始时间戳
@param	end			int64	结束时间戳
@param	name		string	名字
@param	difficulty	byte	难度
@param	PN			bool	是否为积极习惯，true为积极习惯，false为消极习惯
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func DoneHabit(uid, begin, duration, end int64, name string, difficulty byte, PN bool) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("done habit affair begin fail:", err)
		conn.Rollback()
		return false, "done habit affair begin fail"
	}
	//获取习惯的标签名
	row, err := conn.Query("select `l_name` from `habit` where `u_id`=? and `h_name`=?;",
		uid, name)
	var label string
	if row.Next() {
		row.Scan(&label)
	}
	row.Close()
	//将习惯标签添加到标签表中去
	b, info = addLabel(conn, label)
	if !b {
		log.Println("done habit but add label fail:", err)
		conn.Rollback()
		return false, "done habit but add label fail"
	}
	//判断积极or消极习惯并进行更新
	if PN {
		_, err = conn.Exec("update `habit` "+
			"set `positive` =`positive`+? "+
			"where `u_id`=? and `h_name`=?;",
			duration, uid, name)
	} else {
		_, err = conn.Exec("update `habit` "+
			"set `negative` =`negative`+? "+
			"where `u_id`=? and `h_name`=?;",
			duration, uid, name)
	}
	if err != nil {
		log.Println("done habit fail:", err)
		conn.Rollback()
		return false, "done habit fail"
	}
	//将该执行情况添加到用户记录中
	if PN {
		b, info = addPositive(conn, uid, duration)
	} else {
		b, info = addNegative(conn, uid, duration)
	}
	if !b {
		log.Println("insert habit record fail:", err)
		conn.Rollback()
		return false, "insert habit record fail"
	}
	//将该执行情况添加到执行记录中
	b, info = addImplement(conn, uid, begin, duration, end, name, difficulty)
	if !b {
		log.Println("insert habit implement fail:", err)
		conn.Rollback()
		return false, "insert habit implement fail"
	}
	//提交事务
	conn.Commit()
	return true, "done habit success"
}

/**
IncreaseUserTask
添加个人任务
@param	uid			int64	用户ID
@param	name		string	任务名
@param	label		string	标签名
@param	remark		string	备注
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func IncreaseUserTask(uid int64, name, label, remark string, difficulty byte) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("increase user task affair begin fail:", err)
		conn.Rollback()
		return false, "increase user task affair begin fail"
	}
	//将习惯标签添加到标签表中去
	b, info = addLabel(conn, label)
	if !b {
		log.Println("increase user task but add label fail:", err)
		conn.Rollback()
		return false, "increase user task but add label fail"
	}
	//添加个人任务
	_, err = conn.Exec("insert into "+
		"`user_task`(`u_t_name`, `u_id`, `l_name`, `difficulty`, `duration`, `number`, `remark`)"+
		"values( ?, ?, ?, ?, ?, ?, ?)",
		name, uid, label, difficulty, 0, 0, remark)
	if err != nil {
		log.Println("increase user task fail:", err)
		conn.Rollback()
		return false, "increase user task fail"
	}
	//提交事务
	conn.Commit()
	return true, "increase user task success"
}

/**
DoneUserTask
添加个人任务完成记录
@param	uid			int64	用户ID
@param	begin		int64	开始时间戳
@param	end			int64	结束时间戳
@param	name		string	名字
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func DoneUserTask(uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("done user task affair begin fail:", err)
		conn.Rollback()
		return false, "done user task affair begin fail"
	}
	//获取个人任务的标签名
	row, err := conn.Query("select `l_name` from `user_task` where `u_id`=? and `u_t_name`=?;",
		uid, name)
	var label string
	if row.Next() {
		row.Scan(&label)
	}
	row.Close()
	//将标签添加到标签表中去
	b, info = addLabel(conn, label)
	if !b {
		log.Println("done user task but add label fail:", err)
		conn.Rollback()
		return false, "done user task but add label fail"
	}
	_, err = conn.Exec("update `user_task` "+
		"set `duration` =`duration`+? ,`number`=`number`+1 "+
		"where `u_id`=? and `u_t_name`=?;",
		duration, uid, name)
	if err != nil {
		log.Println("done user task fail:", err)
		conn.Rollback()
		return false, "done user task fail"
	}
	//将该执行情况添加到执行记录中
	b, info = addImplement(conn, uid, begin, duration, end, name, difficulty)
	if !b {
		log.Println("insert user task implement fail:", err)
		conn.Rollback()
		return false, "insert user task implement fail"
	}
	//提交事务
	conn.Commit()
	return true, "done user task success"
}

/**
IncreaseGroupTask
添加群组任务
@param	gid			int64	群组ID
@param	name		string	任务名
@param	label		string	标签名
@param	remark		string	备注
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func IncreaseGroupTask(gid int64, name, label, remark string, difficulty byte) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("increase group task affair begin fail:", err)
		conn.Rollback()
		return false, "increase group task affair begin fail"
	}
	//将习惯标签添加到标签表中去
	b, info = addLabel(conn, label)
	if !b {
		log.Println("increase group task but add label fail:", err)
		conn.Rollback()
		return false, "increase group task but add label fail"
	}
	//添加群组任务
	_, err = conn.Exec("insert into "+
		"`group_task`(`g_t_name`, `g_id`, `l_name`, `difficulty`, `duration`, `number`, `remark`)"+
		"values( ?, ?, ?, ?, ?, ?, ?)",
		name, gid, label, difficulty, 0, 0, remark)
	if err != nil {
		log.Println("increase group task fail:", err)
		conn.Rollback()
		return false, "increase group task fail"
	}
	//提交事务
	conn.Commit()
	return true, "increase group task success"
}

/**
DoneGroupTask
添加群组任务完成记录
@param	gid			int64	群组ID
@param	uid			int64	用户ID
@param	begin		int64	开始时间戳
@param	end			int64	结束时间戳
@param	name		string	名字
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func DoneGroupTask(gid, uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("done group task affair begin fail:", err)
		conn.Rollback()
		return false, "done group task affair begin fail"
	}
	//获取群组任务的标签名
	row, err := conn.Query("select `l_name` from `group_task` where `g_id`=? and `g_t_name`=?;",
		gid, name)
	var label string
	if row.Next() {
		row.Scan(&label)
	}
	row.Close()
	//将标签添加到标签表中去
	b, info = addLabel(conn, label)
	if !b {
		log.Println("done group task but add label fail:", err)
		conn.Rollback()
		return false, "done group task but add label fail"
	}
	_, err = conn.Exec("update `group_task` "+
		"set `duration` =`duration`+? ,`number`=`number`+1 "+
		"where `g_id`=? and `g_t_name`=?;",
		duration, uid, name)
	if err != nil {
		log.Println("done user task fail:", err)
		conn.Rollback()
		return false, "done user task fail"
	}
	//将该执行情况添加到执行记录中
	b, info = addImplement(conn, uid, begin, duration, end, name, difficulty)
	if !b {
		log.Println("insert user task implement fail:", err)
		conn.Rollback()
		return false, "insert user task implement fail"
	}
	//将记录添加到群组中
	b, info = addRecord(conn, uid, gid, duration)
	if !b {
		log.Println("done user task but add record fail:", err)
		conn.Rollback()
		return false, "done user task but add record fail"
	}
	//提交事务
	conn.Commit()
	return true, "done user task success"
}

/**
DoneImplement
添加执行记录
@param	uid			int64	用户ID
@param	begin		int64	开始时间戳
@param	duration	int64	执行时长
@param	end			int64	结束时间戳
@param	name		string	名字
@param	difficulty	byte	难度
@return b			bool 	添加成功返回true，失败返回false
@return	info		string	执行信息
*/

func DoneImplement(uid, begin, duration, end int64, name string, difficulty byte) (b bool, info string) {
	conn, err := DB.Begin()
	if err != nil {
		log.Println("done implement affair begin fail:", err)
		conn.Rollback()
		return false, "done implement affair begin fail"
	}
	b, info = addImplement(conn, uid, begin, duration, end, name, difficulty)
	if !b {
		conn.Rollback()
		return false, "done implement fail"
	}
	conn.Commit()
	return true, "done implement success"
}

//以下三项暂定,将会修改

/**
ChangeHabitRemark
修改习惯备注
@param	uid			int64	用户ID
@param	name	   	string	习惯名
@param	remark	   	string	新备注
@return b			bool 	修改成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeHabitRemark(uid int64, name, remark string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change habit remark affair begin fail:", err)
		conn.Rollback()
		return false, "user change habit remark affair begin"
	}
	//修改习惯备注
	_, err = conn.Exec("update `habit` set `remark` =? where `u_id`=? and `h_name`=?",
		remark, uid, name)
	if err != nil {
		log.Println("user change habit remark affair fail:", err)
		conn.Rollback()
		return false, "user change habit remark affair fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change habit remark affair success"
}

/**
ChangeUserTaskRemark
修改个人任务备注
@param	uid			int64	用户ID
@param	name	   	string	群组任务名
@param	remark	   	string	新备注
@return b			bool 	修改成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeUserTaskRemark(uid int64, name, remark string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change user task remark affair begin fail:", err)
		conn.Rollback()
		return false, "user change user task remark affair begin"
	}
	//修改个人任务备注
	_, err = conn.Exec("update `user_task` set `remark` =? where `u_id`=? and `u_t_name`=?",
		remark, uid, name)
	if err != nil {
		log.Println("user change user task remark affair fail:", err)
		conn.Rollback()
		return false, "user change user task remark affair fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change user task remark affair success"
}

/**
ChangeGroupTaskRemark
修改群组任务备注
@param	gid			int64	群组ID
@param	name	   	string	群组任务名
@param	remark	   	string	新备注
@return b			bool 	修改成功返回true，失败返回false
@return	info		string	执行信息
*/

func ChangeGroupTaskRemark(gid int64, name, remark string) (b bool, info string) {
	//开启事务
	conn, err := DB.Begin()
	if err != nil {
		log.Println("user change group task remark affair begin fail:", err)
		conn.Rollback()
		return false, "user change group task remark affair begin"
	}
	//修改群组任务备注
	_, err = conn.Exec("update `group_task` set `remark` =? where `g_id`=? and `g_t_name`=?",
		remark, gid, name)
	if err != nil {
		log.Println("user change group task remark affair fail:", err)
		conn.Rollback()
		return false, "user change group task remark affair fail"
	}
	//提交事务
	conn.Commit()
	return true, "user change group task remark affair success"
}

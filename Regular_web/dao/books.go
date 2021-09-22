package dao

import (
	_ "github.com/go-sql-driver/mysql"
	"log"
)

func CreateBooksTab() {
	createBooksTab()
	createCommentTab()
	createApproveTab()
}
func createBooksTab() {
	_, err := DB.Exec("create table `books`" +
		"(" +
		"`isbn`         bigint," +
		"`title`        varchar(100)  not null default ''," +
		"`deputy_title` varchar(100)  null     default ''," +
		"`publisher`    varchar(100)  not null default ''," +
		"`classify`     varchar(10)   not null default ''," +
		"`theme`        varchar(200)  not null default ''," +
		"`abstract`     varchar(2000) not null default ''," +
		"primary key (`isbn`)" +
		")")
	if err != nil {
		log.Printf("create books tab failured:%v", err)
	}
}
func createCommentTab() {
	_, err := DB.Exec("create table `comment`" +
		"(" +
		"`c_id`      bigint auto_increment," +
		"`u_id`      bigint         null references `user` (u_id)," +
		"`isbn`      bigint         not null references `books` (isbn)," +
		"`comment`   varchar(10000) not null        default ''," +
		"`approve`   bigint         not null        default 0," +
		"`timestamp` bigint         not null unique default 0," +
		"primary key (`c_id`)" +
		") auto_increment = 1;")
	if err != nil {
		log.Printf("create comment tab failured:%v", err)
	}
}
func createApproveTab() {
	_, err := DB.Exec("create table `approve`" +
		"(" +
		"`c_id` bigint references `comment` (c_id)," +
		"`u_id` bigint references `user` (u_id)," +
		"primary key (`c_id`, `u_id`)" +
		")")
	if err != nil {
		log.Printf("create approve tab failured:%v", err)
	}
}

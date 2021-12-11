package dao

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"log"
)

var DB *sql.DB

// Init 初始化mysql数据库
func Init() *sql.DB {
	dbName:="regular"
	db, err := sql.Open("mysql","root:2975hLcCd@tcp(localhost:3306)/"+dbName+"?charset=utf8")
	if err!=nil {
		log.Printf("sql connection failured:%v", err)
		return db
	}
	DB=db
	return db
}
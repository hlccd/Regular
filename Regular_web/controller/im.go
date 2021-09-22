package controller

import (
	"../service"
	"fmt"
	"net"
)

func ListenIm(l net.Listener) {
	service.ImInit()
	for {
		conn, id := service.AcceptConn(l)
		if id == 0 {
			fmt.Println("异常访问", conn.RemoteAddr().String())
			fmt.Fprintf(conn, "close")
			conn.Close()
			continue
		}
		fmt.Println("id=", id)
		fmt.Println("address=", conn.RemoteAddr().String())
		service.SendMessageLogging(conn, id)
		go service.Connection(conn, id)
	}
}

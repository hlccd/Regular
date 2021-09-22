package service

import (
	"../dao"
	"bufio"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"time"
)

var im map[int64]net.Conn

func ImInit() {
	im = make(map[int64]net.Conn)
}
func AcceptConn(l net.Listener) (conn net.Conn, id int64) {
	conn, err := l.Accept()
	if err != nil {
		log.Fatal(err)
	}
	data, err := bufio.NewReader(conn).ReadString('\n')
	if err != nil {
		log.Fatal(err)
	}
	var mes dao.Message
	json.Unmarshal([]byte(data), &mes)
	fmt.Println(data)
	return conn, mes.Id
}

func Connection(conn net.Conn, id int64) {
	im[id] = conn
	for {
		buf := make([]byte, 65535)
		n, err := conn.Read(buf)
		if err != nil {
			fmt.Println(id, "已退出链接")
			fmt.Printf("客户端%s退出\n", conn.RemoteAddr().String())
			delete(im, id)
			conn.Close()
			return
		}
		var mes dao.Message
		json.Unmarshal([]byte(string(buf[:n])), &mes)
		mes.TimeStamp = time.Now().UnixNano() / 1e6
		switch mes.Type {
		case 1:
			sendUser(mes)
		case 2:
			sendGroup(mes)
		case 3:
			makeFriend(mes)
		case 4:
			joinGroup(mes)
		case 5:
			consentAdd(mes)
		case 6:
			consentJoin(mes)
		case 7:
			rejectAdd(mes)
		case 8:
			rejectJoin(mes)
		case 9:
		}
	}
}

func SendMessageLogging(conn net.Conn, id int64) {
	mess := dao.ReleaseLogging(id)
	if mess == nil {
		return
	}
	data, _ := json.Marshal(mess)
	fmt.Fprintf(conn, string(data)+"\n")
}
func sendUser(mes dao.Message) {
	conn, ok := im[mes.Receiver]
	if ok {
		//该用户处于连接中,直接发送
		data, _ := json.Marshal(mes)
		fmt.Fprintf(conn, string(data)+"\n")
	} else {
		dao.SaveLogging(mes)
	}
}
func sendGroup(mes dao.Message) {
	list := dao.FindMember(mes.Group)
	if list != nil {
		for i := 0; i < len(list); i++ {
			if list[i] != mes.Id {
				conn, ok := im[list[i]]
				mes.Receiver = list[i]
				if ok {
					//该用户处于连接中,直接发送
					data, _ := json.Marshal(mes)
					fmt.Fprintf(conn, string(data)+"\n")
				} else {
					dao.SaveLogging(mes)
				}
				fmt.Println("id:",list[i])
			}
		}
	}
}
func makeFriend(mes dao.Message) {
	conn, ok := im[mes.Receiver]
	b, _ := dao.MakeFriend(mes.Id, mes.Receiver)
	if b {
		if ok {
			//该用户处于连接中,直接发送
			data, _ := json.Marshal(mes)
			fmt.Fprintf(conn, string(data)+"\n")
		} else {
			dao.SaveLogging(mes)
		}
	} else {
		rejectAdd(mes)
	}
}
func joinGroup(mes dao.Message) {
	administrators := dao.GetAdministrator(mes.Group)
	if administrators != nil {
		for i := 0; i < len(administrators); i++ {
			conn, ok := im[administrators[i]]
			mes.Receiver = administrators[i]
			if ok {
				//该用户处于连接中,直接发送
				data, _ := json.Marshal(mes)
				fmt.Fprintf(conn, string(data)+"\n")
			} else {
				dao.SaveLogging(mes)
			}
		}
	}
}
func consentAdd(mes dao.Message) {
	conn, ok := im[mes.Receiver]
	b, _ := dao.ChangeFriend(mes.Id, mes.Receiver, 0)
	if b {
		if ok {
			//该用户处于连接中,直接发送
			data, _ := json.Marshal(mes)
			fmt.Fprintf(conn, string(data)+"\n")
		} else {
			dao.SaveLogging(mes)
		}
	} else {
		rejectAdd(mes)
	}
}
func consentJoin(mes dao.Message) {
	if dao.GetPower(mes.Group, mes.Id) > 0 {
		dao.Join(mes.Receiver, mes.Group)
		conn, ok := im[mes.Receiver]
		if ok {
			//该用户处于连接中,直接发送
			data, _ := json.Marshal(mes)
			fmt.Fprintf(conn, string(data)+"\n")
		} else {
			dao.SaveLogging(mes)
		}
	}
}
func rejectAdd(mes dao.Message) {
	conn, ok := im[mes.Receiver]
	dao.DeleteFriend(mes.Id, mes.Receiver)
	if ok {
		//该用户处于连接中,直接发送
		data, _ := json.Marshal(mes)
		fmt.Fprintf(conn, string(data)+"\n")
	} else {
		dao.SaveLogging(mes)
	}
}
func rejectJoin(mes dao.Message) {
	conn, ok := im[mes.Receiver]
	if ok {
		//该用户处于连接中,直接发送
		data, _ := json.Marshal(mes)
		fmt.Fprintf(conn, string(data)+"\n")
	} else {
		dao.SaveLogging(mes)
	}
}

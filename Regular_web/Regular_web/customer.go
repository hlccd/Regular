package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"time"
)

type Message struct {
	Mes       string `json:"mes"`
	Id        int64  `json:"id"`
	Receiver  int64  `json:"receiver"`
	Type      int8   `json:"type"`
	Group     int64  `json:"group"`
	TimeStamp int64  `json:"timestamp"`
}

func main() {
	//conn, err := net.Dial("tcp", "47.108.217.244:2997")
	conn, err := net.Dial("tcp", ":2997")
	if err != nil {
		log.Fatal(err)
	}
	defer conn.Close()
	mes := Message{
		Mes:       "",
		Id:        100000,
		Receiver:  0,
		Type:      0,
		Group:     0,
		TimeStamp: time.Now().UnixNano() / 1e6,
	}
	data, _ := json.Marshal(mes)
	fmt.Fprintf(conn, string(data)+"\n")
	go func(conn net.Conn) {
		var s string
		for {
			fmt.Scanf("%s", &s)
			mes := Message{
				Mes:       s,
				Id:        100000,
				Receiver:  0,
				Type:      2,
				Group:     4,
				TimeStamp: time.Now().UnixNano() / 1e6,
			}
			data, _ := json.Marshal(mes)
			fmt.Fprintf(conn, string(data)+"\n")
			fmt.Println(string(data))
		}
	}(conn)
	for {
		data, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			log.Fatal(err)
		}
		fmt.Printf(data)
	}
}

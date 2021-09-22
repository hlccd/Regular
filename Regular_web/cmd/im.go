package cmd

import (
	"../controller"
	"log"
	"net"
)

func Im() {
	l, err := net.Listen("tcp", ":2997")
	if err != nil {
		log.Fatal(err)
	}
	defer l.Close()

	controller.ListenIm(l)
}

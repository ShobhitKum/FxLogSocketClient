package com.in.sm.socket;

public class RunServer {

	public static void main(String[] args) {
		Server server = new Server();
		server.init();
		server.start();
		//server.stop();
	/*	
		MessageServer server = new MessageServer();
		server.init();
		server.start();
		server.stop();*/
	}
	
}

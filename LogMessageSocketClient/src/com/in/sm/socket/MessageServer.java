package com.in.sm.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageServer implements LifeCycle {
	public final static int PORT = 8136;
	ServerSocket server = null;
	MessageGenerator log = null;

	@Override
	public void init() {

		try {
			server = new ServerSocket(PORT);
			log = new MessageGenerator();
			log.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void start() {
		while (true) {

			Socket connection;
			try {
				System.out.println("going for server accept");
				connection = server.accept();
				System.out.println("Accepted");
				ProcessMessageRequest task = new ProcessMessageRequest(connection, log);
				new Thread(task).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		try {
			System.out.println("Closing socket");
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class ProcessMessageRequest implements Runnable {
	private Socket connection;
	private MessageGenerator reader;

	public ProcessMessageRequest(Socket connection, MessageGenerator reader) {
		this.connection = connection;
		this.reader = reader;
	}

	@Override
	public void run() {
		try {
			System.out.println("wrting to port");
			PrintWriter out = new PrintWriter(connection.getOutputStream());

			while (true) {
				 System.out.println("val-"+reader.queue.peek());
				out.write(reader.queue.take());
				out.flush();
			}
			// System.out.println("Done-------------");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

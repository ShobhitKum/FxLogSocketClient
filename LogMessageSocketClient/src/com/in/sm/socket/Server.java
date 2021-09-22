package com.in.sm.socket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

public class Server implements LifeCycle {
	public final static int PORT = 8136;
	ServerSocket server = null;
	LogFileReader log=null;
	//LogFileWriter writer;

	@Override
	public void init() {

		try {
			server = new ServerSocket(PORT);
			log = new LogFileReader();
			//writer = new LogFileWriter();
			//writer.start();
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
				ProcessRequest task = new ProcessRequest(connection, log);
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

class ProcessRequest implements Runnable{
	private Socket connection;
	private LogFileReader reader;
	
	public ProcessRequest(Socket connection, LogFileReader reader) {
		this.connection=connection;
		this.reader=reader;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("wrting to port");
			Writer out = new OutputStreamWriter(connection.getOutputStream());
			
			System.out.println(reader.getQ().size());
			while(true){
				//System.out.println("val-"+i.next());
				out.write(reader.getQ().take());
				out.flush();
			}
			//System.out.println("Done-------------");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

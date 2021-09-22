package com.in.sm.socket;

import java.io.FileWriter;
import java.io.IOException;

public class LogFileWriter implements Runnable {

	public static String fileName = "D:/fix/messageX1.txt";
	MessageGenerator msg = new MessageGenerator();

	public void write() throws IOException {
		FileWriter fileOutput = new FileWriter(fileName);
		msg.start();
		while (true) {
			try {
				//System.out.println("writing");
				fileOutput.write(msg.queue.take());
				fileOutput.write(System.lineSeparator());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fileOutput.close();
			}
			
		}

	}

	public static void main(String[] args) {

		LogFileWriter r = new LogFileWriter();
		r.start();

	}

	void start() {
		new Thread(new LogFileWriter()).start();
	}

	@Override
	public void run() {
		try {
			write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

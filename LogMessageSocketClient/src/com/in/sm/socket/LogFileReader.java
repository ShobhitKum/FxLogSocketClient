package com.in.sm.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogFileReader implements Runnable {

	public static String fileName = "D:/fix/messageX.txt";
	public static LinkedBlockingQueue<Byte> q= new LinkedBlockingQueue<Byte>(100); 

	public void read() throws IOException {

		FileInputStream fileInputStream = new FileInputStream(new File(fileName));
		FileChannel fileChannel = fileInputStream.getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

		//int read = fileChannel.read(byteBuffer);
		
		
		while(fileChannel.read(byteBuffer)>0){
			byteBuffer.flip();
			int limit = byteBuffer.limit();
			while (limit > 0) {
				//System.out.print((char) byteBuffer.get());
				try {
					q.put(byteBuffer.get());
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				limit--;
			}
			//read = fileChannel.read(byteBuffer);
			
			System.out.println("clear and write again...........");
			byteBuffer.clear();
			
		}
		
		System.out.print("done");
	

		fileChannel.close();
	}

	public static void main(String[] args) {
		try {
			LogFileReader r = new LogFileReader();
			r.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void start(){
		new Thread(new LogFileReader()).start();
	}

	public static LinkedBlockingQueue<Byte> getQ() {
		return q;
	}

	public static void setQ(LinkedBlockingQueue<Byte> q) {
		LogFileReader.q = q;
	}

	@Override
	public void run() {
		try {
			read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}

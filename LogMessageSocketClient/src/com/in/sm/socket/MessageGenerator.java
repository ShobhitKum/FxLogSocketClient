package com.in.sm.socket;

import java.util.concurrent.LinkedBlockingQueue;

import com.sm.fix.Message;
import com.sm.order.OrderProcessor;
import com.sm.order.OrderProcessorImpl;

public class MessageGenerator implements Runnable {
	public static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(100);
	private OrderProcessor impl = new OrderProcessorImpl();
	final static String ORDER_ID = "OrderId";
	final static String EXEC_ID = "ExecId";
	final static String EXCHNG = "NSC42";
	final static String SESSION = "TR_42";
	final static String NEW_ORDER = "D";
	final static String ACK = "8";
	final static String CANCEL_REQUEST = "F";
	final static String REPLACE = "G";

	final static String NEW = "0";
	final static String FILL = "2";
	final static String PARTIAL_FILL = "1";
	final static String CANCELLED = "4";
	final static String REPLACED = "5";
	final static String PENDING_CANCEL = "6";
	final static String REJECTED = "8";
	final static String PENDING_REPLACE = "E";

	@Override
	public void run() {
		while (true) {
			try {
				Message msg = impl.newOrder();
				queue.put(msg.buildMessage());
				queue.put(impl.orderAck(msg, NEW).buildMessage());
				queue.put(impl.orderExecuted(msg, PARTIAL_FILL).buildMessage());
				queue.put(impl.orderExecuted(msg, FILL).buildMessage());

				

				Message msg2 = impl.newOrder();
				
				queue.put(msg.buildMessage());
				queue.put(impl.orderAck(msg2, NEW).buildMessage());
				queue.put(impl.orderExecuted(msg2, PARTIAL_FILL).buildMessage());
				queue.put(impl.orderCancel(msg2).buildMessage());
				queue.put(impl.orderAck(msg2, PENDING_CANCEL).buildMessage());
				queue.put(impl.orderExecuted(msg2, CANCELLED).buildMessage());

				
				Message msg3 = impl.newOrder();
				queue.put(msg3.buildMessage());
				queue.put(impl.orderAck(msg3, NEW).buildMessage());
				queue.put(impl.appendOrder(msg3).buildMessage());
				queue.put(impl.orderAck(msg3, PENDING_REPLACE).buildMessage());
				queue.put(impl.appendOrder(msg3).buildMessage());
				queue.put(impl.orderAck(msg3, PENDING_REPLACE).buildMessage());
				queue.put(impl.appendOrder(msg3).buildMessage());
				queue.put(impl.orderAck(msg3, PENDING_REPLACE).buildMessage());
				queue.put(impl.orderCancel(msg3).buildMessage());
				queue.put(impl.orderAck(msg3, PENDING_CANCEL).buildMessage());
				queue.put(impl.orderExecuted(msg3, CANCELLED).buildMessage());
			
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void start() {
		Thread thread = new Thread(new MessageGenerator());
		thread.start();
	}
}

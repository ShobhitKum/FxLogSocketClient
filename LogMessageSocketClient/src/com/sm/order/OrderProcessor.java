package com.sm.order;

import com.sm.fix.Message;

public interface OrderProcessor {

	public Message newOrder();
	public Message orderAck(Message message,String ackType);
	public Message appendOrder(Message message);
	public Message orderCancel(Message message);
	public Message orderExecuted(Message message, String execType);
	
}

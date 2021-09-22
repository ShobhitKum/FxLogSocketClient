package com.sm.order;

import com.sm.fix.Message;
import com.sm.fix.MsgUtils;

public class OrderProcessorImpl implements OrderProcessor {
	static int seqNum =0;
	final static String ORDER_ID = "OrderId";
	final static String EXEC_ID = "ExecId";
	final static String EXCHNG = "NSC42";
	final static String SESSION = "TR_42";
	final static String NEW_ORDER ="D";
	final static String ACK ="8";
	final static String CANCEL_REQUEST ="F";
	final static String REPLACE ="G";
	
	
	final static String NEW = "0";
	final static String FILL = "2";
	final static String PARTIAL_FILL = "1";
	final static String CANCELLED = "4";
	final static String REPLACED = "5";
	final static String PENDING_CANCEL = "6";
	final static String REJECTED = "8";
	final static String PENDING_REPLACE = "E";



	@Override
	public Message newOrder() {
		Message message = getNewMessage();
		populateSenderAndTarget(message,SESSION,EXCHNG);
		if(message.SENDINGTIME%2==0){
			message.SIDE ="2";
			message.SYMBOL="MRF.T";
		}else{
			message.SIDE ="1";
			message.SYMBOL="JQS.T";
		}
		message.MESSAGETYPE="D";
		message.TEXT="NEW ORDER";
		message.ORDERTYPE = "Mkt";
		return message;
	}


	
	public Message getNewMessage(){
		return getMessage(null);
	}
	
	public Message getMessage(Message msg){
		Message message = msg;
		if(MsgUtils.isNull(message)){
			message = new Message();
			message.ORDERID = MsgUtils.getOrderID(ORDER_ID);
			message.SENDINGTIME = MsgUtils.getTime();
			message.ORDERQTY = MsgUtils.getQty();
			message.PRICE = MsgUtils.getPrice();
		}else{
			msg.SENDINGTIME = MsgUtils.getTime();
			message.ORDERID = MsgUtils.getOrderID(ORDER_ID);
		}
		populateSeq(message);
		return message;
	}
	
	public Message populateSenderAndTarget(Message message,String sender,String target){
		message.SENDERCOMPID = sender;
		message.TARGETCOMPID = target;
		return message;
	}

	public void populateSeq(Message message){
		seqNum++;
		message.SEQNUM =seqNum;
	}
	
/*	public static void main(String[] args) {
		OrderProcessorImpl impl = new OrderProcessorImpl();
		Message msg = impl.newOrder();
		System.out.println(msg.buildMessage());
		System.out.println(impl.orderAck(msg, NEW).buildMessage());
		System.out.println(impl.orderExecuted(msg,PARTIAL_FILL).buildMessage());
		System.out.println(impl.orderExecuted(msg,FILL).buildMessage());
		
		
		Message msg2 = impl.newOrder();
		System.out.println(msg2.buildMessage());
		System.out.println(impl.orderAck(msg2, NEW).buildMessage());
		System.out.println(impl.orderExecuted(msg2,PARTIAL_FILL).buildMessage());
		System.out.println(impl.orderCancel(msg2).buildMessage());
		System.out.println(impl.orderAck(msg2,PENDING_CANCEL).buildMessage());
		System.out.println(impl.orderExecuted(msg2,CANCELLED).buildMessage());


		
		Message msg3 = impl.newOrder();
		System.out.println(msg3.buildMessage());
		System.out.println(impl.orderAck(msg3, NEW).buildMessage());
		System.out.println(impl.appendOrder(msg3).buildMessage());
		System.out.println(impl.orderAck(msg3,PENDING_REPLACE).buildMessage());
		System.out.println(impl.appendOrder(msg3).buildMessage());
		System.out.println(impl.orderAck(msg3,PENDING_REPLACE).buildMessage());
		System.out.println(impl.orderCancel(msg3).buildMessage());
		System.out.println(impl.orderAck(msg3,PENDING_CANCEL).buildMessage());
		System.out.println(impl.orderExecuted(msg3,CANCELLED).buildMessage());
	
	}*/



	@Override
	public Message orderAck(Message message, String ackType) {
		populateSeq(message);
		message.EXECID = MsgUtils.getOrderID(EXEC_ID);
		message.EXECTYPE=ackType;
		message.SENDINGTIME = MsgUtils.getTime();
		message.MESSAGETYPE = ACK;
		if(ackType.equals(PENDING_CANCEL)){
			message.TEXT = "ORDER PENDING_CANCEL ACK";
			message.ORDERSTATUS="6";
		}else if(ackType.equals(NEW)){
			message.TEXT = "NEW ORDER ACK";
			message.ORDERSTATUS=NEW;
		}else if(ackType.equals(PENDING_REPLACE)){
			message.TEXT = "ORDER PENDING_REPLACE ACK";
			message.ORDERSTATUS="E";

		}
	
		populateSenderAndTarget(message,EXCHNG,SESSION);
		return message;
	}



	@Override
	public Message appendOrder(Message message) {
		message.ORIGCLID = message.ORDERID;
		message.ORDERID = MsgUtils.getOrderID(ORDER_ID);
		message.TEXT = "REPLACE ORDER";
		message.SENDINGTIME = MsgUtils.getTime();
		message.MESSAGETYPE = REPLACE;
		message.PRICE = MsgUtils.getPrice();
		message.ORDERQTY = MsgUtils.getQty();
		populateSenderAndTarget(message,SESSION,EXCHNG);
		return message;
	}



	@Override
	public Message orderCancel(Message message) {
		message.ORIGCLID = message.ORDERID;
		message.ORDERID = MsgUtils.getOrderID(ORDER_ID);
		message.TEXT = "CANCEL ORDER";
		message.SENDINGTIME = MsgUtils.getTime();
		message.MESSAGETYPE = CANCEL_REQUEST;
		populateSenderAndTarget(message,SESSION,EXCHNG);
		return message;
	}



	@Override
	public Message orderExecuted(Message message, String execType) {
		String text =null;
		if(execType.equals(FILL)){
			execType = FILL;
		}else if(execType.equals(CANCELLED)){
			execType = CANCELLED;
			text = "CANCELLED";
			message.ORDERSTATUS="4";
			message.LEAVESQTY=0;
		}else if(execType.equals(REPLACED)){
			execType = REPLACED;
			text = "REPLACED";
			message.ORDERSTATUS="5";
			message.LEAVESQTY=message.ORDERQTY;
		}
		//populateSenderAndTarget(message);
		message.EXECTYPE =execType;
		message.TEXT = text;
		message.SENDINGTIME = MsgUtils.getTime();
		if(execType.equals(PARTIAL_FILL)){
			message.LASTQTY = message.ORDERQTY/2;
			message.TEXT = "PARTIAL FILL";
			message.ORDERSTATUS="1";
			message.LEAVESQTY = message.LASTQTY;
		}else if(execType.equals(FILL)){
			message.LASTQTY = message.ORDERQTY - message.LASTQTY;
			message.TEXT = "FILLED";
			message.ORDERSTATUS="2";
			message.LEAVESQTY=0;
		}
		populateSeq(message);
		return message;
	}
}

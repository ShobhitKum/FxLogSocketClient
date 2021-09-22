package com.sm.fix;

import java.math.BigDecimal;

public class Message {
	String fixSeperator = "^A ";
	
	public String ORDERID;
	public String MESSAGETYPE;
	public String SYMBOL;
	public String SENDERCOMPID;
	public String TARGETCOMPID;
	public long SENDINGTIME;
	public String CLORDERID;
	public String EXECID;
	public String ORDERSTATUS;
	public String EXECTYPE;
	public String SIDE;
	public long ORDERQTY;
	public String ORDERTYPE;
	public BigDecimal PRICE;
	public long LASTQTY;
	public long LEAVESQTY;
	public String TEXT;
	public long SEQNUM;
	public String ORIGCLID;
	
	
	enum FixMessage {

		ORDERID(37),
		MESSAGETYPE(35),
		SYMBOL(55),
		SENDERCOMPID(49),
		TARGETCOMPID(56),
		SENDINGTIME(52),
		CLORDERID(11),
		EXECID(17),
		ORDERSTATUS(39),
		EXECTYPE(150),
		SIDE(54),
		ORDERQTY(38),
		ORDERTYPE(40),
		PRICE(44),
		LASTQTY(32),
		LEAVESQTY(151),
		TEXT(58),
		ORIGCLID(41),
		SEQNUM(7);
		
		private int filedNumber; 
		
		FixMessage(int fieldNumber){
			this.filedNumber = fieldNumber;
		}

		public static String getField(int num){
			for (FixMessage msg : FixMessage.values()){
				if(msg.filedNumber == num){
					//System.out.println("MsgName->"+msg.name().toString());
					return msg.name().toString();
				}
			}
			return null;
		}
		
		public static Integer getFixNumber(String s){
			FixMessage value = FixMessage.valueOf(s);
			return value.filedNumber;
		}
		
	}
	
	public String buildMessage(){
		String messsage=null;
		StringBuilder builder = new StringBuilder();
		builder.append(getNum("ORDERID")).append("=").append(ORDERID).append(fixSeperator);
		builder.append(getNum("MESSAGETYPE")).append("=").append(MESSAGETYPE).append(fixSeperator);
		builder.append(getNum("SYMBOL")).append("=").append(SYMBOL).append(fixSeperator);
		builder.append(getNum("SENDERCOMPID")).append("=").append(SENDERCOMPID).append(fixSeperator);
		builder.append(getNum("TARGETCOMPID")).append("=").append(TARGETCOMPID).append(fixSeperator);
		builder.append(getNum("SENDINGTIME")).append("=").append(SENDINGTIME).append(fixSeperator);
		builder.append(getNum("CLORDERID")).append("=").append(CLORDERID).append(fixSeperator);
		checkAndAppend(builder,"EXECID",EXECID);
		checkAndAppend(builder,"ORDERSTATUS",ORDERSTATUS);
		checkAndAppend(builder,"LASTQTY",LASTQTY);
		checkAndAppend(builder,"LEAVESQTY",LEAVESQTY);
		checkAndAppend(builder,"TEXT",TEXT);
		checkAndAppend(builder,"EXECTYPE",EXECTYPE);
		checkAndAppend(builder,"SIDE",SIDE);
		checkAndAppend(builder,"ORDERQTY",ORDERQTY);
		checkAndAppend(builder,"ORDERTYPE",ORDERTYPE);
		checkAndAppend(builder,"ORIGCLID",ORIGCLID);
		checkAndAppend(builder,"SEQNUM",SEQNUM);
		builder.append(getNum("PRICE")).append("=").append(PRICE);
		messsage = builder.toString();
		return messsage;
	}
	
	public StringBuilder checkAndAppend(StringBuilder builder,String type,Object value){
		if(value!=null){
			builder.append(getNum(type)).append("=").append(value).append(fixSeperator);
		}
		return builder;
	}
	
	
	private Integer getNum(String tag){
		return FixMessage.getFixNumber(tag);
	}
}

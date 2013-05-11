package org.os.javaee.jms.core.message;

import java.io.Serializable;

/**
 * <p>Title: MessagePropertyImpl.java</p>
 * <p>Description: MessagePropertyImpl represents a Property of the disconnected {@link MessageImpl}</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
public class MessagePropertyImpl implements Serializable{

	private static final long serialVersionUID = 1L;

	public final static int BOOLEAN_PROPERTY 	=	00000000;  	//	0
	public final static int BYTE_PROPERTY 		=	00000001;	//	1
	public final static int DOUBLE_PROPERTY 	=	00000010;	//	8
	public final static int FLOAT_PROPERTY 		=	00000011;	//	9
	public final static int INTEGER_PROPERTY 	=	00000100;	//	64
	public final static int LONG_PROPERTY 		=	00000101;	//	65
	public final static int OBJECT_PROPERTY 	=	00000110;	//	72
	public final static int SHORT_PROPERTY 		=	00000111;	//	73
	public final static int STRING_PROPERTY 	=	00001000;	//	512
	
	private String key;
	private Object value;
	private int type;
		
	public MessagePropertyImpl(String key,Object value ,int type) {
		this.key=key;
		this.value = value;
		this.type =type;
	}
		
	public String getKey() { return key; }
	public void setKey(String key) { this.key = key; }
		
	public int getType() { return type; }
	public void setType(int type) { this.type = type; }
		
	public Object getValue() { return value; }
	public void setValue(Object value) { this.value = value; }
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tProperty Name \t:"+(key));
		sb.append("\n\tProperty Value \t:"+(value));
		sb.append("\n\tProperty Type \t:"+(type));
		return sb.toString();
	}
}

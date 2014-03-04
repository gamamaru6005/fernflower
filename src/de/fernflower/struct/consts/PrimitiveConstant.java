/*
 *    Fernflower - The Analytical Java Decompiler
 *    http://www.reversed-java.com
 *
 *    (C) 2008 - 2010, Stiver
 *
 *    This software is NEITHER public domain NOR free software 
 *    as per GNU License. See license.txt for more details.
 *
 *    This software is distributed WITHOUT ANY WARRANTY; without 
 *    even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 *    A PARTICULAR PURPOSE. 
 */

package de.fernflower.struct.consts;

import java.io.DataOutputStream;
import java.io.IOException;

import de.fernflower.code.CodeConstants;

/*
 *   Integer, Long, Float, Double, String, Class, UTF8
 */

public class PrimitiveConstant extends PooledConstant {

	// *****************************************************************************
	// public fields
	// *****************************************************************************

	public int index;

	public Object value;
	
	public boolean isArray;
	
	// *****************************************************************************
	// constructors
	// *****************************************************************************
	
	public PrimitiveConstant(int type, Object value) {
		this.type = type;
		this.value = value;
		
		initConstant();
	}

	public PrimitiveConstant(int type, int index) {
		this.type = type;
		this.index = index;
	}
	
	// *****************************************************************************
	// public methods
	// *****************************************************************************
	
	public int getInt() {
		return ((Integer)value).intValue(); 
	}
	
	public long getLong() {
		return ((Long)value).longValue(); 
	}

	public float getFloat() {
		return ((Float)value).floatValue(); 
	}
	
	public double getDouble() {
		return ((Double)value).doubleValue(); 
	}
	
	public String getString() {
		return (String)value;
	}
	
	public void resolveConstant(ConstantPool pool) {

		if(type == CONSTANT_Class || type == CONSTANT_String || type == CONSTANT_MethodType) {
			value = pool.getPrimitiveConstant(index).getString(); 
			initConstant();
		}
	}
	
	public void writeToStream(DataOutputStream out) throws IOException {
		
		out.writeByte(type);
		switch(type) {
			case CONSTANT_Integer:
				out.writeInt(getInt());
				break;
			case CONSTANT_Float:
				out.writeFloat(getFloat());
				break;
			case CONSTANT_Long:
				out.writeLong(getLong());
				break;
			case CONSTANT_Double:
				out.writeDouble(getDouble());
				break;
			case CONSTANT_Utf8:
				out.writeUTF(getString());
				break;
			default: // CONSTANT_Class, CONSTANT_String, CONSTANT_MethodType 
				out.writeShort(index);
		}
	}
	
	public boolean equals(Object obj) {
		
		if(obj == null || !(obj instanceof PrimitiveConstant)) {
			return false;
		}
		
		PrimitiveConstant cn = (PrimitiveConstant)obj;
		
		return this.type == cn.type && 
		 		this.isArray == cn.isArray &&
		 		this.value.equals(cn.value); 
		
	}
	
	private void initConstant() {
		if(type == CONSTANT_Class) {
			String className = getString();
			isArray = (className.length() > 0 && className.charAt(0)=='['); // empty string for a class name seems to be possible in some android files 
		}
	}
	
}

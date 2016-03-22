import java.util.TreeMap;

import javax.swing.text.rtf.RTFEditorKit;


public class DataStructures {
	public char register[][];	
	public char carry;
	public char overflow;
	public char zero;
	public char negative;
	
	long programCounter;
	String nextInstruction;
	
	public TreeMap<String,String> memory = new TreeMap<String,String>();
	
	public DataStructures()
	{
		register=new char[32][64];
		
		for(int i=0;i<31;i++)
		{
 			for(int j=0;j<64;j++)
				register[i][j]='X';
		}
		for(int i=0;i<64;i++)
		{
			register[31][i]='0';
		}
		carry='0';
		overflow='0';
		zero='0';
		negative='0';
		nextInstruction="";
		programCounter=0;
	}
	
	public String getRegisterValue(int index)
	{
		if(index<31)
		{
			String regValue="";
			for(int i=0;i<64;i++)
				regValue=regValue+register[index][i];
			return regValue;
		}
		else 
		{
			return null;
		}
	}
	
	public boolean setRegisterValue(int index,char[] value)
	{
		try{
		if(index<31)
		{
			register[index]=value;
			return true;
		}
		else {
			return false;
		}
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	public char getCarry()
	{
		return carry;
	}
	
	public char getOverflow()
	{
		return overflow;	
	}
	
	public char getZero()
	{
		return zero;
	}
	
	public char getNegative()
	{
		return negative;
	}
}

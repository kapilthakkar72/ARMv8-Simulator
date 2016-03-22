import java.io.FileWriter;
import java.io.IOException;


public class InstructionExecution {
	DataStructures d ;
	int instructionNumber=0;
	//char programCounter[] = new char[64];
	char stackPointer[] = new char[64];
	
	public InstructionExecution(DataStructures dataStructure)
	{
		d=dataStructure;
		//programCounter= padLeft(Integer.toBinaryString(d.programCounter), 64, '0').toCharArray();
		stackPointer=d.register[31];
	}
	public void RET(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		int n=Integer.parseInt(rnS,2);
		
		char target[]=new char[64];
		target = copy(d.register[n]);
		
		d.programCounter=Long.parseLong(new String(target),2);
	}
	public void CBZ(String s,String hexInstruction,FileWriter fw,char sf)
	{
		try{
			// fetch rd
			String rt="";
			for(int i=0;i<5;i++)
			{
				rt=rt+s.charAt(27+i);
			}
			
			int intRt=Integer.parseInt(rt,2);
			
			String imm19="";
			for(int i=0;i<19;i++)
			{
				imm19=imm19+s.charAt(8+i);
			}
			Boolean flag=true;
			if(sf=='1')
			{
				for(int i=0;i<64;i++)
				{
					if(d.register[intRt][i]=='1')
					{
						flag=false;
						break;
					}
					
				}
			}
			else {
				for(int i=0;i<32;i++)
				{
					if(d.register[intRt][i+32]=='1')
					{
						flag=false;
						break;
					}
					
				}
			}
			
			if(flag)
			{
				//String strVal=SignExtend(imm19+"00",64);
				String strVal=imm19+"00";
				
				long offset=binaryToLong(strVal);
				
				
				//offSet=offset/4;
				d.programCounter-=8;
				d.programCounter+=(offset/4)*8;
				
			}
			
		}
		catch(Exception ex)
		{
		
		}
	}
	public long binaryToLong(String s)
	{
		long number=0;
		int i=0;
		int length=s.length();
		int bit=0;
		
		if(s.charAt(0)=='0')
		{
			// positive number
			for(i=length-1;i>=0;i--)
			{
				if(s.charAt(i)=='1')
					number+=Math.pow(2,bit);
				bit++;
			}
			return number;
		}
		else
		{
			// negative number
			i=length-1;
			String newS="";
			while(s.charAt(i)!='1')
			{
				newS=s.charAt(i)+newS;
				i--;				
			}
			newS=s.charAt(i)+newS;
			i--;
			while(i>=0)
			{
				if(s.charAt(i)=='1')
				{
					newS="0"+newS;
					i--;
				}
				else
				{
					newS="1"+newS;
					i--;
				}
			}
			for(i=length-1;i>=0;i--)
			{
				if(newS.charAt(i)=='1')
					number+=Math.pow(2,bit);
				bit++;
			}
			return -number;
		}
		
		
	}
	public void CBNZ(String s,String hexInstruction,FileWriter fw,char sf)
	{
		try{
			// fetch rd
			String rt="";
			for(int i=0;i<5;i++)
			{
				rt=rt+s.charAt(27+i);
			}
			
			int intRt=Integer.parseInt(rt,2);
			
			String imm19="";
			for(int i=0;i<19;i++)
			{
				imm19=imm19+s.charAt(8+i);
			}
			Boolean flag=false;
			if(sf=='1')
			{
				for(int i=0;i<64;i++)
				{
					if(d.register[intRt][i]=='1')
					{
						flag=true;
						break;
					}
					
				}
			}
			else {
				for(int i=0;i<32;i++)
				{
					if(d.register[intRt][i+32]=='1')
					{
						flag=true;
						break;
					}
					
				}
			}
			
			if(flag)
			{
				//String strVal=SignExtend(imm19+"00",64);
				String strVal=imm19+"00";
				long offset=binaryToLong(strVal);
				
				//offSet=offset/4;
				d.programCounter-=8;
				d.programCounter+=(offset/4)*8;
				
			}
			
		}
		catch(Exception ex)
		{
		
		}
	}
	public void B(String s,String hexInstruction,FileWriter fw)
	{
		try{
			// fetch rd
			String imm26="";
			for(int i=0;i<26;i++)
			{
				imm26=imm26+s.charAt(6+i);
			}
			
				//String strVal=SignExtend(imm19+"00",64);
				String strVal=imm26+"00";
				long offset=binaryToLong(strVal);
				
				
				//offSet=offset/4;
				d.programCounter-=8;
				d.programCounter+=(offset/4)*8;
						
		}
		catch(Exception ex)
		{
		
		}
	}
	public void BL(String s,String hexInstruction,FileWriter fw)
	{
		try{
			// fetch rd
			String imm26="";
			for(int i=0;i<26;i++)
			{
				imm26=imm26+s.charAt(6+i);
			}
			
				//String strVal=SignExtend(imm19+"00",64);
				String strVal=imm26+"00";
				long offset=binaryToLong(strVal);
				
				fw.append(instructionNumber+"  "+hexInstruction+"   BL  :"+offset+"\r\n");
				instructionNumber++;
				
				//offSet=offset/4;
				//d.programCounter-=8;
				
				String returnAddress= Long.toBinaryString(d.programCounter);
				returnAddress="0"+returnAddress;
				returnAddress= SignExtend(returnAddress, 64);
				for(int i=0;i<64;i++)
					d.register[30][i]=returnAddress.charAt(i);
				d.programCounter-=8;
				d.programCounter+=(offset/4)*8;
						
		}
		catch(Exception ex)
		{
		
		}
	}
	public void BR(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		int rn=Integer.parseInt(rnS,2);
		String address="";
		for(i=0;i<64;i++)
		{
			address+=d.register[rn][i];
		}
		
		
		int addressLong=Integer.parseInt(address,2);
		
		d.programCounter=addressLong*2;
	}
	public void BLR(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		int rn=Integer.parseInt(rnS,2);
		String address="";
		for(i=0;i<64;i++)
		{
			address+=d.register[rn][i];
		}
		
		String returnAddress= Long.toBinaryString(d.programCounter);
		returnAddress="0"+returnAddress;
		returnAddress= SignExtend(returnAddress, 64);
		for(i=0;i<64;i++)
			d.register[30][i]=returnAddress.charAt(i);
		
		int addressLong=Integer.parseInt(address,2);
		
		d.programCounter=addressLong*2;
	}
	private String SignExtend(String x, int N) 
	{
		// TODO Auto-generated method stub
		int M=x.length();
		if(N>M)
		{
			char sign= x.charAt(0);
			for(int i=0;i<N-M;i++)
				x=sign+x;
		}
		return x;
	}
	
	public void addShiftRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		//Adding to File
		fw.append(instructionNumber+"  "+hexInstruction+"     ADD   W"+rd+" ,W"+rn+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rm][i]='0';
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}
		
		// perform addition
		char carry='0';
		for(i=63;i>31;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='0';	}
				else
				{	d.register[rd][i]='1';	carry='0';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='1';	}
				else
				{	d.register[rd][i]='1';	carry='1';	}
		}
		}
		catch(Exception ex)
		{
		
		}
	}
	public void addShiftRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"   ADD  X"+rd+" ,X"+rn+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=1;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][0]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}
		
		// perform addition
		char carry='0';
		for(i=63;i>-1;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='0';	}
				else
				{	d.register[rd][i]='1';	carry='0';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='1';	}
				else
				{	d.register[rd][i]='1';	carry='1';	}
		}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void subShiftRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"   SUB W"+rd+" ,W"+rn+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rm][i]='0';
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}
		// perform subtraction
		char borrow='0';
		for(i=63;i>31;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='0';	}
				else
				{	d.register[rd][i]='0';	borrow='0';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='1';	}
				else
				{	d.register[rd][i]='0';	borrow='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
		}
		}
		catch(Exception ex)
		{
			
		}
		
	}
	public void subShiftRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"   SUB X"+rd+" ,X"+rn+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=1;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][0]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
		// perform subtraction
		char borrow='0';
		for(i=63;i>-1;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='0';	}
				else
				{	d.register[rd][i]='0';	borrow='0';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='1';	}
				else
				{	d.register[rd][i]='0';	borrow='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
		}
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public void movz32bit(String s,String hexInstruction,FileWriter fw)  
	{
		int i,j;
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		String imm16="";
		for(i=0;i<16;i++)
		{
			imm16=imm16+ s.charAt(i+11);
		}
		
		int rd=Integer.parseInt(rdS, 2);
		fw.append(instructionNumber+"  "+hexInstruction+"   MOV W"+rd+", #"+imm16+"\r\n");
		instructionNumber++;
		
		String posS="";
		posS=""+s.charAt(9)+s.charAt(10)+"0000";
		
		int pos= Integer.parseInt(posS, 2);
		
		char result[]=new char[32];
		
		for(i=0;i<32;i++)
			result[i]='0';
		
		int firstBit = 31 - (pos+15);
		int lastBit = 31 - pos;
		
		for(i=firstBit,j=0;i<=lastBit;i++,j++)
			result[i]=imm16.charAt(j);

		for(i=0;i<32;i++)
			d.register[rd][i]='0';
		
		for(i=32,j=0;i<64;i++,j++)
			d.register[rd][i]=result[j];
	
		
		}
		catch(Exception ex)
		{
			System.out.print("Exception in MOv");
		}
	}
	public void movz64bit(String s,String hexInstruction,FileWriter fw)  
	{
		
		try{
			int i,j;
			
			// fetch rd
			String rdS="";
			for(i=0;i<5;i++)
			{
				rdS=rdS+s.charAt(27+i);
			}
			int rd=Integer.parseInt(rdS, 2);
			
			String imm16="";
			for(i=0;i<16;i++)
				imm16=imm16+s.charAt(i+11);
			
			String posS="";
			posS=""+s.charAt(9)+s.charAt(10)+"0000";
			
			int pos= Integer.parseInt(posS, 2);
			
			char result[]=new char[64];
			
			for(i=0;i<64;i++)
				result[i]='0';
			
			int firstBit = 63 - (pos+15);
			int lastBit = 63 - pos;
			
			for(i=firstBit,j=0;i<=lastBit;i++,j++)
				result[i]=imm16.charAt(j);
			
			for(i=0;i<64;i++)
				d.register[rd][i]=result[i];
			
		}
		catch(Exception ex)
		{
			System.out.print("Exception in MOv");
		}
	}
	
	public void addsShiftRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ADDS  W"+rd+" ,W"+rn+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rm][i]='0';
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
		// perform addition
		char carry='0';
		for(i=63;i>31;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='0';	}
				else
				{	d.register[rd][i]='1';	carry='0';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='1';	}
				else
				{	d.register[rd][i]='1';	carry='1';	}
		}
		
		if( d.register[rm][32]==d.register[rn][32])
		{
			if(d.register[rd][32]==d.register[rm][32])
			{
				d.carry=carry;
			}
			else
			{
				d.overflow='1';
			}
		}
		else
		{
			d.carry=carry;
		}
		
		if(d.register[rd][31]=='1')
			d.negative='1';
		else
			d.negative='0';
		
		boolean zero=true;
		for(i=0;i<32;i++)
			if(d.register[rd][32+i]=='1')
			{
				zero=false;
				break;
			}
		if(zero)
			d.zero='1';
		else
			d.zero='0';
		}
		catch(Exception ex)
		{
			
		}
	}
	public void addsShiftRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ADDS  X"+rd+" ,X"+rn+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
		// perform addition
		char carry='0';
		for(i=63;i>-1;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='0';	}
				else
				{	d.register[rd][i]='1';	carry='0';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(carry=='0')
				{	d.register[rd][i]='1';	carry='0';	}
				else
				{	d.register[rd][i]='0';	carry='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(carry=='0')
				{	d.register[rd][i]='0';	carry='1';	}
				else
				{	d.register[rd][i]='1';	carry='1';	}
		}
		
		if( d.register[rm][63]==d.register[rn][63])
		{
			if(d.register[rd][63]==d.register[rm][63])
			{
				d.carry=carry;
			}
			else
			{
				d.overflow='1';
			}
		}
		else
		{
			d.carry=carry;
		}
		
		if(d.register[rd][63]=='1')
			d.negative='1';
		else
			d.negative='0';
		
		boolean zero=true;
		for(i=0;i<64;i++)
			if(d.register[rd][i]=='1')
			{
				zero=false;
				break;
			}
		if(zero)
			d.zero='1';
		else
			d.zero='0';	
		}
		catch(Exception ex)
		{
			
		}
	}
	public void subsShiftRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  SUBS  X"+rd+" ,X"+rn+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rm][i]='0';
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
		// perform subtraction
		char borrow='0';
		for(i=63;i>31;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='0';	}
				else
				{	d.register[rd][i]='0';	borrow='0';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='1';	}
				else
				{	d.register[rd][i]='0';	borrow='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
		}
		
		if( d.register[rm][32]==d.register[rn][32])
		{
			if(d.register[rd][32]==d.register[rm][32])
			{
				d.carry=borrow;
			}
			else
			{
				d.overflow='1';
			}
		}
		else
		{
			d.carry=borrow;
		}
		
		if(d.register[rd][31]=='1')
			d.negative='1';
		else
			d.negative='0';
		
		boolean zero=true;
		for(i=0;i<32;i++)
			if(d.register[rd][32+i]=='1')
			{
				zero=false;
				break;
			}
		if(zero)
			d.zero='1';
		else
			d.zero='0';
		}
		catch(Exception ex)
		{
			
		}
	}
	public void subsShiftRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  SUBS  W"+rd+" ,W"+rn+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
		// perform subtraction
		char borrow='0';
		for(i=63;i>-1;i--)
		{
			if(d.register[rm][i]=='0' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
			
			if(d.register[rm][i]=='0' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='0';	}
				else
				{	d.register[rd][i]='0';	borrow='0';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='0')
				if(borrow=='0')
				{	d.register[rd][i]='1';	borrow='1';	}
				else
				{	d.register[rd][i]='0';	borrow='1';	}
			
			if(d.register[rm][i]=='1' && d.register[rn][i]=='1')
				if(borrow=='0')
				{	d.register[rd][i]='0';	borrow='0';	}
				else
				{	d.register[rd][i]='1';	borrow='1';	}
		}
		
		if( d.register[rm][63]==d.register[rn][63])
		{
			if(d.register[rd][63]==d.register[rm][63])
			{
				d.carry=borrow;
			}
			else
			{
				d.overflow='1';
			}
		}
		else
		{
			d.carry=borrow;
		}
		
		if(d.register[rd][63]=='1')
			d.negative='1';
		else
			d.negative='0';
		
		boolean zero=true;
		for(i=0;i<64;i++)
			if(d.register[rd][i]=='1')
			{
				zero=false;
				break;
			}
		if(zero)
			d.zero='1';
		else
			d.zero='0';
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public void addImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ADDI  W"+rd+" ,W"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
		// perform addition
				char carry='0';
				for(i=63;i>31;i--)
				{
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='0';	}
						else
						{	d.register[rd][i]='1';	carry='0';	}
					
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='1';	}
						else
						{	d.register[rd][i]='1';	carry='1';	}
				}
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void addImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000000000000000"+"00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000000000000000000000000000"+"00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ADDI  X"+rd+" ,X"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
		// perform addition
				char carry='0';
				for(i=63;i>=0;i--)
				{
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='0';	}
						else
						{	d.register[rd][i]='1';	carry='0';	}
					
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='1';	}
						else
						{	d.register[rd][i]='1';	carry='1';	}
				}
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void addsImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ADDSI  W"+rd+" ,W"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
		// perform addition
				char carry='0';
				for(i=63;i>31;i--)
				{
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='0';	}
						else
						{	d.register[rd][i]='1';	carry='0';	}
					
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='1';	}
						else
						{	d.register[rd][i]='1';	carry='1';	}
				}
				
				if( immDataInCharArray[31]==d.register[rn][31])
				{
					if(d.register[rd][31]==d.register[rn][31])
					{
						d.carry=carry;
					}
					else
					{
						d.overflow='1';
					}
				}
				else
				{
					d.carry=carry;
				}
				
				if(d.register[rd][31]=='1')
					d.negative='1';
				else
					d.negative='0';
				
				boolean zero=true;
				for(i=0;i<32;i++)
					if(d.register[rd][32+i]=='1')
					{
						zero=false;
						break;
					}
				if(zero)
					d.zero='1';
				else
					d.zero='0';
				
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void addsImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000000000000000"+"00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000000000000000000000000000"+"00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ADDSI  X"+rd+" ,X"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
		// perform addition
				char carry='0';
				for(i=63;i>=0;i--)
				{
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='0';	}
						else
						{	d.register[rd][i]='1';	carry='0';	}
					
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='0')
						if(carry=='0')
						{	d.register[rd][i]='1';	carry='0';	}
						else
						{	d.register[rd][i]='0';	carry='1';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='1')
						if(carry=='0')
						{	d.register[rd][i]='0';	carry='1';	}
						else
						{	d.register[rd][i]='1';	carry='1';	}
				}
				if( immDataInCharArray[63]==d.register[rn][63])
				{
					if(d.register[rd][63]==d.register[rn][63])
					{
						d.carry=carry;
					}
					else
					{
						d.overflow='1';
					}
				}
				else
				{
					d.carry=carry;
				}
				
				if(d.register[rd][63]=='1')
					d.negative='1';
				else
					d.negative='0';
				
				boolean zero=true;
				for(i=0;i<64;i++)
					if(d.register[rd][i]=='1')
					{
						zero=false;
						break;
					}
				if(zero)
					d.zero='1';
				else
					d.zero='0';	
				
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}

	public void subImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  SUBI  W"+rd+" ,W"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
				// perform subtraction
				char borrow='0';
				for(i=63;i>31;i--)
				{
					
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='0';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
					
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='0';	}
						else
						{	d.register[rd][i]='0';	borrow='0';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='1';	}
						else
						{	d.register[rd][i]='0';	borrow='1';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='0';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
				}
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
		
		}
	}
	public void subImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		for(i=0;i<32;i++)
			newImmediateData="0"+newImmediateData;
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  SUBI  X"+rd+" ,X"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
		// perform subtraction
				char borrow='0';
				for(i=63;i>-1;i--)
				{
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='0';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
					
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='0';	}
						else
						{	d.register[rd][i]='0';	borrow='0';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='1';	}
						else
						{	d.register[rd][i]='0';	borrow='1';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='1';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
				}
				
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void subsImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  SUBSI  W"+rd+" ,W"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
				// perform subtraction
				char borrow='0';
				for(i=63;i>31;i--)
				{
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='0';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
					
					if(immDataInCharArray[i-32]=='0' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='0';	}
						else
						{	d.register[rd][i]='0';	borrow='0';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='1';	}
						else
						{	d.register[rd][i]='0';	borrow='1';	}
					
					if(immDataInCharArray[i-32]=='1' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='1';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
				}
				if( immDataInCharArray[31]==d.register[rn][31])
				{
					if(d.register[rd][31]==d.register[rn][31])
					{
						d.carry=borrow;
					}
					else
					{
						d.overflow='1';
					}
				}
				else
				{
					d.carry=borrow;
				}
				
				if(d.register[rd][31]=='1')
					d.negative='1';
				else
					d.negative='0';
				
				boolean zero=true;
				for(i=0;i<32;i++)
					if(d.register[rd][32+i]=='1')
					{
						zero=false;
						break;
					}
				if(zero)
					d.zero='1';
				else
					d.zero='0';
				
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void subsImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		//fetch imm12		
		String imm12S="";
		for(i=0;i<12;i++)
		{
			imm12S=imm12S+s.charAt(10+i);
		}
		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8)+s.charAt(9);
		
		String newImmediateData="";
		if(shiftS.equals("00"))
		{
			newImmediateData= "00000000000000000000" + imm12S;
		}
		else
			if(shiftS.equals("01"))
			{
				newImmediateData= "00000000"+imm12S +"000000000000";
			}
			else
			{
				System.out.println("Invalid Input");
			}
		
		for(i=0;i<32;i++)
			newImmediateData="0"+newImmediateData;
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  SUBSI  X"+rd+" ,X"+rn+" ,#"+newImmediateData+"\r\n");
		instructionNumber++;
				
		char immDataInCharArray[]=newImmediateData.toCharArray();
		
		// perform subtraction
				char borrow='0';
				for(i=63;i>-1;i--)
				{
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='0';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
					
					if(immDataInCharArray[i]=='0' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='0';	}
						else
						{	d.register[rd][i]='0';	borrow='0';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='0')
						if(borrow=='0')
						{	d.register[rd][i]='1';	borrow='1';	}
						else
						{	d.register[rd][i]='0';	borrow='1';	}
					
					if(immDataInCharArray[i]=='1' && d.register[rn][i]=='1')
						if(borrow=='0')
						{	d.register[rd][i]='0';	borrow='1';	}
						else
						{	d.register[rd][i]='1';	borrow='1';	}
				}
				
				if( immDataInCharArray[63]==d.register[rn][63])
				{
					if(d.register[rd][63]==d.register[rn][63])
					{
						d.carry=borrow;
					}
					else
					{
						d.overflow='1';
					}
				}
				else
				{
					d.carry=borrow;
				}
				
				if(d.register[rd][63]=='1')
					d.negative='1';
				else
					d.negative='0';
				
				boolean zero=true;
				for(i=0;i<64;i++)
					if(d.register[rd][i]=='1')
					{
						zero=false;
						break;
					}
				if(zero)
					d.zero='1';
				else
					d.zero='0';
				
				if(rd==31)
				{
					// get address to which stack pointer is pointing
					
					String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
					sp= padLeft(sp, 16, '0');
					
					// convert value to hex
					String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
					value=padLeft(value, 16, '0');
					
					// insert value to map
					d.memory.put(sp, value);
				}
		}
		catch(Exception ex)
		{
			
		}
	}

	public void andImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetching immediate operand
		
		String immr="";
		
		char N=s.charAt(9);
		
		for(i=0;i<6;i++)
		{
			immr=immr+s.charAt(i+10);
		}
		
		String imms="";
		for(i=0;i<6;i++)
		{
			imms=imms+s.charAt(i+16);
		}
		
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char wmask[]=new char[64];
		char levels[]=new char[6];
		for(i=0;i<6;i++)
			levels[i]='0';
		
		int len=0;
		
		for(i=0;i<6;i++)
		{
			if(imms.charAt(i)=='0')
			{
				len=5-i;
				break;
			}
		}
		
		char m[]=new char[6];
		
		String mString="1";
		
		for(i=0;i<len;i++)
			mString=mString+"0";
		
		if(mString.length()!=6)
		{
			for(i=0;i<6-mString.length();i++)
				mString="0"+mString;
		}
		
		for(i=0;i<len;i++)
			levels[5-i]='1';
		
		String levelsString= new String(levels);
		int levelsInt=Integer.parseInt(levelsString, 2);
		int immrInt = Integer.parseInt(new String(immr), 2);
		int immsInt = Integer.parseInt(new String(imms), 2);
		
		int S= levelsInt & immsInt;
		int R = levelsInt & immrInt;
		
		int diff= S-R;
		
		int esize=Integer.parseInt(mString,2);
		
		String welem="";
		for(i=0;i<S+1;i++)
			welem=welem+"1";
		
		int welemSize=welem.length();
		for(i=0;i<esize-welemSize;i++)
			welem="0"+welem;
		
		// rotate right
		
		char welemChar[] = welem.toCharArray();
		
		if(R>0)
		{
			for(j=0;j<R;j++)
			{
				char temp=welemChar[0];
				for(i=31;i>1;i--)
				{
					welemChar[i-1]=welemChar[i];
				}
				welemChar[31]=temp;
			}
		}
	
		char immediateOperand[]=new char[32];
		immediateOperand=welemChar;
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDI  W"+rd+" ,W"+rn+" ,#"+new String(immediateOperand)+"\r\n");
		instructionNumber++;
				
		
		// Performing AND Operation
		{
			for(i=0;i<32;i++)
			{
				if(immediateOperand[i]==d.register[rn][32+i] && d.register[rn][32+i]=='1')
				{
					d.register[rd][32+i]='1';
				}
				else
				{
					d.register[rd][32+i]='0';
				}
			}
		}
		
		if(rd==31)
		{
			// get address to which stack pointer is pointing
			
			String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
			sp= padLeft(sp, 16, '0');
			
			// convert value to hex
			String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
			value=padLeft(value, 16, '0');
			
			// insert value to map
			d.memory.put(sp, value);
		}
		
		}
		catch(Exception ex)
		{
			
		}
	}
	public void andImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetching immediate operand
		
		String immr="";
		
		char N=s.charAt(9);
		
		for(i=0;i<6;i++)
		{
			immr=immr+s.charAt(i+10);
		}
		
		String imms="";
		for(i=0;i<6;i++)
		{
			imms=imms+s.charAt(i+16);
		}
		
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char wmask[]=new char[64];
		char levels[]=new char[6];
		for(i=0;i<6;i++)
			levels[i]='0';
		
		int len=0;
		
		for(i=0;i<6;i++)
		{
			if(N=='0')
			if(imms.charAt(i)=='0')
			{
				len=5-i;
				break;
			}
		}
		
		char m[]=new char[6];
		
		String mString="1";
		
		for(i=0;i<len;i++)
			mString=mString+"0";
		
		if(mString.length()!=6)
		{
			for(i=0;i<6-mString.length();i++)
				mString="0"+mString;
		}
		
		for(i=0;i<len;i++)
			levels[5-i]='1';
		
		String levelsString= new String(levels);
		int levelsInt=Integer.parseInt(levelsString, 2);
		int immrInt = Integer.parseInt(new String(immr), 2);
		int immsInt = Integer.parseInt(new String(imms), 2);
		
		int S= levelsInt & immsInt;
		int R = levelsInt & immrInt;
		
		int diff= S-R;
		
		int esize=Integer.parseInt(mString,2);
		
		String welem="";
		for(i=0;i<S+1;i++)
			welem=welem+"1";
		
		int welemSize=welem.length();
		for(i=0;i<esize-welemSize;i++)
			welem="0"+welem;
		
		// rotate right
		
		char welemChar[] = welem.toCharArray();
		
		if(R>0)
		{
			for(j=0;j<R;j++)
			{
				char temp=welemChar[0];
				for(i=31;i>1;i--)
				{
					welemChar[i-1]=welemChar[i];
				}
				welemChar[31]=temp;
			}
		}
	
		char immediateOperand[]=new char[32];
		immediateOperand=welemChar;
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDI  W"+rd+" ,W"+rn+" ,#"+new String(immediateOperand)+"\r\n");
		instructionNumber++;
				
		
		// Performing AND Operation
		{
			for(i=0;i<32;i++)
			{
				if(immediateOperand[i]==d.register[rn][32+i] && d.register[rn][32+i]=='1')
				{
					d.register[rd][32+i]='1';
				}
				else
				{
					d.register[rd][32+i]='0';
				}
			}
		}
		
		if(rd==31)
		{
			// get address to which stack pointer is pointing
			
			String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
			sp= padLeft(sp, 16, '0');
			
			// convert value to hex
			String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
			value=padLeft(value, 16, '0');
			
			// insert value to map
			d.memory.put(sp, value);
		}
		
		}
		catch(Exception ex)
		{
			
		}
	}
	public void andShiftedRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDS  W"+rd+" ,W"+rn+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rm][i]='0';
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}	
		// Performing AND Operation
			
				for(i=0;i<32;i++)
				{
					if(d.register[rm][32+i]==d.register[rn][32+i] && d.register[rn][32+i]=='1')
					{
						d.register[rd][32+i]='1';
					}
					else
					{
						d.register[rd][32+i]='0';
					}
				}
		}
		catch(Exception ex)
		{
			
		}
	}
	public void andShiftedRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDS  X"+rd+" ,X"+rn+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=1;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][0]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
	
	
		// Performing AND Operation
			
				for(i=0;i<64;i++)
				{
					if(d.register[rm][i]==d.register[rn][i] && d.register[rn][i]=='1')
					{
						d.register[rd][i]='1';
					}
					else
					{
						d.register[rd][i]='0';
					}
				}
		}
		catch(Exception ex)
		{
			
		}
	}

	public void andsImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetching immediate operand
		
		String immr="";
		
		char N=s.charAt(9);
		
		for(i=0;i<6;i++)
		{
			immr=immr+s.charAt(i+10);
		}
		
		String imms="";
		for(i=0;i<6;i++)
		{
			imms=imms+s.charAt(i+16);
		}
		
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		
		for(i=0;i<32;i++)
		{
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		char wmask[]=new char[64];
		char levels[]=new char[6];
		for(i=0;i<6;i++)
			levels[i]='0';
		
		int len=0;
		
		for(i=0;i<6;i++)
		{
			if(imms.charAt(i)=='0')
			{
				len=5-i;
				break;
			}
		}
		
		char m[]=new char[6];
		
		String mString="1";
		
		for(i=0;i<len;i++)
			mString=mString+"0";
		
		if(mString.length()!=6)
		{
			for(i=0;i<6-mString.length();i++)
				mString="0"+mString;
		}
		
		for(i=0;i<len;i++)
			levels[5-i]='1';
		
		String levelsString= new String(levels);
		int levelsInt=Integer.parseInt(levelsString, 2);
		int immrInt = Integer.parseInt(new String(immr), 2);
		int immsInt = Integer.parseInt(new String(imms), 2);
		
		int S= levelsInt & immsInt;
		int R = levelsInt & immrInt;
		
		int diff= S-R;
		
		int esize=Integer.parseInt(mString,2);
		
		String welem="";
		for(i=0;i<S+1;i++)
			welem=welem+"1";
		
		int welemSize=welem.length();
		for(i=0;i<esize-welemSize;i++)
			welem="0"+welem;
		
		// rotate right
		
		char welemChar[] = welem.toCharArray();
		
		if(R>0)
		{
			for(j=0;j<R;j++)
			{
				char temp=welemChar[0];
				for(i=31;i>1;i--)
				{
					welemChar[i-1]=welemChar[i];
				}
				welemChar[31]=temp;
			}
		}
	
		char immediateOperand[]=new char[32];
		immediateOperand=welemChar;
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDI  W"+rd+" ,W"+rn+" ,#"+new String(immediateOperand)+"\r\n");
		instructionNumber++;
				
		
		// Performing AND Operation
		{
			for(i=0;i<32;i++)
			{
				if(immediateOperand[i]==d.register[rn][32+i] && d.register[rn][32+i]=='1')
				{
					d.register[rd][32+i]='1';
				}
				else
				{
					d.register[rd][32+i]='0';
				}
			}
		}
		
		if(rd==31)
		{
			// get address to which stack pointer is pointing
			
			String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
			sp= padLeft(sp, 16, '0');
			
			// convert value to hex
			String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
			value=padLeft(value, 16, '0');
			
			// insert value to map
			d.memory.put(sp, value);
		}
		}
		catch(Exception ex)
		{
			
		}			
	}
	public void andsImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetching immediate operand
		
		String immediateOperandS="";
		
		immediateOperandS=immediateOperandS +s.charAt(9);
		for(i=0;i<6;i++)
		{
			immediateOperandS=immediateOperandS+s.charAt(i+16);
		}
		for(i=0;i<6;i++)
		{
			immediateOperandS=immediateOperandS+s.charAt(i+10);
		}
		
		immediateOperandS="00000000000000000000000000000000"+"0000000000000000000" + immediateOperandS;
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDSI  X"+rd+" ,X"+rn+" ,#"+immediateOperandS+"\r\n");
		instructionNumber++;
		
		// Convert immediateOperandS to char array
		char immediateOperand[]=immediateOperandS.toCharArray();
		
		// Performing AND Operation
		{
			for(i=0;i<64;i++)
			{
				if(immediateOperand[i]==d.register[rn][i] && d.register[rn][i]=='1')
				{
					d.register[rd][i]='1';
				}
				else
				{
					d.register[rd][i]='0';
				}
			}
		}
		if(d.register[rd][0]=='1')
			d.negative='1';
		else
			d.negative='0';
		
		boolean zero=true;
		for(i=0;i<64;i++)
			if(d.register[rd][i]=='1')
			{
				zero=false;
				break;
			}
		if(zero)
			d.zero='1';
		else
			d.zero='0';
		
		if(rd==31)
		{
			// get address to which stack pointer is pointing
			
			String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
			sp= padLeft(sp, 16, '0');
			
			// convert value to hex
			String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
			value=padLeft(value, 16, '0');
			
			// insert value to map
			d.memory.put(sp, value);
		}
		
		}
		catch(Exception ex)
		{
			
		}
		
	}
	public void andsShiftRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDSS  W"+rd+" ,W"+rn+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rm][i]='0';
			d.register[rn][i]='0';
			d.register[rd][i]='0';
		}
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=33;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][32]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=32;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}	
	
		// Performing AND Operation
			
				for(i=0;i<32;i++)
				{
					if(d.register[rm][32+i]==d.register[rn][32+i] && d.register[rn][32+i]=='1')
					{
						d.register[rd][32+i]='1';
					}
					else
					{
						d.register[rd][i+32]='0';
					}
				}
				if(d.register[rd][0]=='1')
					d.negative='1';
				else
					d.negative='0';
				
				boolean zero=true;
				for(i=0;i<32;i++)
					if(d.register[rd][32+i]=='1')
					{
						zero=false;
						break;
					}
				if(zero)
					d.zero='1';
				else
					d.zero='0';
		}
		catch(Exception ex)
		{
			
		}
	}
	public void andsShiftRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}

		
		//fetch shift
		String shiftS="";
		shiftS=shiftS + s.charAt(8) + s.charAt(9);
		
		//fetch imm6
		String imm6S="";
		for(i=0;i<6;i++)
		{
			imm6S=imm6S+s.charAt(16+i);
		}
		
		
		// Convert everything to integer
		int shift=Integer.parseInt(shiftS,2);	
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int imm6=Integer.parseInt(imm6S,2);
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  ANDSS  X"+rd+" ,X"+rn+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		if(shift==0)
		{
			// LSL - Logical Shift Left			
			for(i=0;i<imm6;i++)
			{
				for(j=1;j<=63;j++)
					d.register[rm][j-1]=d.register[rm][j];
				d.register[rm][63]='0';
			}
		}
		if(shift==1)
		{
			// LSR - Logical Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
				d.register[rm][0]='0';
			}
		}
		if(shift==2)
		{
			// ASR - Arithmetic Shift Right			
			for(i=0;i<imm6;i++)
			{
				for(j=62;j>=0;j--)
					d.register[rm][j+1]=d.register[rm][j];
			}
		}		
	
	
		// Performing AND Operation
			
				for(i=0;i<64;i++)
				{
					if(d.register[rm][i]==d.register[rn][i] && d.register[rn][i]=='1')
					{
						d.register[rd][i]='1';
					}
					else
					{
						d.register[rd][i]='0';
					}
				}
				if(d.register[rd][0]=='1')
					d.negative='1';
				else
					d.negative='0';
				
				boolean zero=true;
				for(i=0;i<64;i++)
					if(d.register[rd][i]=='1')
					{
						zero=false;
						break;
					}
				if(zero)
					d.zero='1';
				else
					d.zero='0';
		}
		catch(Exception ex)
		{
			
		}

	}

	public void movRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
				
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		
		int rd=Integer.parseInt(rdS,2);			// Destination
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  MOV  W"+rd+" ,W"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<32;i++)
		{
			d.register[rd][i]='0';
		}
		
		for(i=0;i<32;i++)
		{
			d.register[rd][i+32]=d.register[rm][i+32];
		}
		}
		catch(Exception ex)
		{
			
		}

		
	}
	public void movRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
				
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		
		int rd=Integer.parseInt(rdS,2);			// Destination
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		fw.append(instructionNumber+"  "+hexInstruction+"  MOV  X"+rd+" ,X"+rm+"\r\n");
		instructionNumber++;
		
		for(i=0;i<64;i++)
		{
			d.register[rd][i]=d.register[rm][i];
		}		
		}
		catch(Exception ex)
		{
			
		}
	}

	public void bCondition(String s,String hexInstruction,FileWriter fw) throws IOException
	{
		String imm19="";
		int i;
		String cond="";
		
		for(i=0;i<19;i++)
		{
			imm19+=s.charAt(8+i);
		}
		imm19=imm19+"00";
		
		cond+= s.charAt(31)+s.charAt(30)+s.charAt(29)+s.charAt(28);
		
		boolean jump=false;
		
		if(cond.equals("0000"))
			if(d.zero=='1')
				jump=true;
		if(cond.equals("0001"))
			if(d.zero=='0')
				jump=true;
		if(cond.equals("0010"))
			if(d.carry=='1')
				jump=true;
		if(cond.equals("0011"))
			if(d.carry=='0')
				jump=true;
		if(cond.equals("0100"))
			if(d.negative=='1')
				jump=true;
		if(cond.equals("0101"))
			if(d.negative=='0')
				jump=true;
		if(cond.equals("0110"))
			if(d.overflow=='1')
				jump=true;
		if(cond.equals("0111"))
			if(d.overflow=='0')
				jump=true;
		if(cond.equals("1000"))
			if(d.carry=='1' && d.zero=='0')
				jump=true;
		if(cond.equals("1001"))
			if(!(d.carry=='1' && d.zero=='0'))
				jump=true;
		if(cond.equals("1010"))
			if(d.negative==d.overflow)
				jump=true;
		if(cond.equals("1011"))
			if(d.negative!=d.overflow)
				jump=true;
		if(cond.equals("1100"))
			if(d.zero=='1' && d.negative==d.overflow)
				jump=true;
		if(cond.equals("1101"))
			if(!(d.zero=='1' && d.negative==d.overflow))
				jump=true;
		if(cond.equals("1110")||cond.equals("1111"))
			jump=true;
			
		fw.append(instructionNumber+"  "+hexInstruction+"  B."+cond+"\n");
		instructionNumber++;
		
		long offset= binaryToLong(imm19);
		
		d.programCounter-=8;
		d.programCounter+=(offset/4)*8;
		
	}

	public void ADR(String s,String hexInstruction,FileWriter fw)
	{
		try{
			// fetch rd
			
			String rds="";
			for(int i=0;i<5;i++)
			{
				rds=rds+s.charAt(27+i);
			}
			int rd=Integer.parseInt(rds,2);
			

			String immhi="";
			for(int i=0;i<19;i++)
			{
				immhi=immhi+s.charAt(8+i);
			}
			
			String immlo=""+s.charAt(1)+s.charAt(2);
			String imm=immhi+immlo;
			
			long offSet=binaryToLong(imm);
			
			long base =d.programCounter+offSet;
			
			String temp= Integer.toBinaryString((int)base);
			
			if(base>=0)
				temp="0"+temp;
			temp=SignExtend(temp, 64);
			
			for(int i=0;i<temp.length();i++)
			{
				d.register[rd][i]=temp.charAt(i);
			}
									
		}
		catch(Exception ex)
		{
		
		}
	}
	public void ADRP(String s,String hexInstruction,FileWriter fw)
	{
		try{
			// fetch rd
			
			String rds="";
			for(int i=0;i<5;i++)
			{
				rds=rds+s.charAt(27+i);
			}
			int rd=Integer.parseInt(rds,2);
			

			String immhi="";
			for(int i=0;i<19;i++)
			{
				immhi=immhi+s.charAt(8+i);
			}
			
			String immlo=""+s.charAt(1)+s.charAt(2);
			String imm=immhi+immlo+"000000000000";
			
			long offSet=binaryToLong(imm);
			
			String strprogramCounter=Long.toBinaryString(d.programCounter);
			
			if(d.programCounter>=0)
				strprogramCounter="0"+strprogramCounter;
			SignExtend(strprogramCounter, 64);
			StringBuilder sb=new StringBuilder(strprogramCounter);
			
			for(int i=0;i<12;i++)
				sb.setCharAt(63-i,'0');
			
			String sbString = new String(sb);
			
			long base =Integer.parseInt(sbString, 2)+offSet;
			
			String temp= Integer.toBinaryString((int)base);
			
			if(base>=0)
				temp="0"+temp;
			temp=SignExtend(temp, 64);
			
			for(int i=0;i<temp.length();i++)
			{
				d.register[rd][i]=temp.charAt(i);
			}
									
		}
		catch(Exception ex)
		{
		
		}
	}

	public void asrRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int rm=Integer.parseInt(rmS,2);
		int rn=Integer.parseInt(rnS,2);
		int rd=Integer.parseInt(rdS,2);
		
		String operand2=new String(d.register[rm]);
		int operand2Int= Integer.parseInt(operand2,2);
		
		operand2Int = operand2Int % 32;
		
		for(i=32;i<64;i++)
			d.register[rd][i]=d.register[rn][i];
		
		// ASR - Arithmetic Shift Right			
		for(i=0;i<operand2Int;i++)
		{
			for(j=62;j>=32;j--)
				d.register[rd][j+1]=d.register[rd][j];
		}
		
	}
		catch(Exception e)
		{
			
		}
	}
	public void asrRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int rm=Integer.parseInt(rmS,2);
		int rn=Integer.parseInt(rnS,2);
		int rd=Integer.parseInt(rdS,2);
		
		String operand2=new String(d.register[rm]);
		int operand2Int= Integer.parseInt(operand2,2);
		
		operand2Int = operand2Int % 64;
		
		for(i=0;i<64;i++)
			d.register[rd][i]=d.register[rn][i];
		
		// ASR - Arithmetic Shift Right			
		for(i=0;i<operand2Int;i++)
		{
			for(j=62;j>=0;j--)
				d.register[rd][j+1]=d.register[rd][j];
		}
		
	}
		catch(Exception e)
		{
			
		}
	}
	
	public void lslRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int rm=Integer.parseInt(rmS,2);
		int rn=Integer.parseInt(rnS,2);
		int rd=Integer.parseInt(rdS,2);
		
		String operand2=new String(d.register[rm]);
		int operand2Int= Integer.parseInt(operand2,2);
		
		operand2Int = operand2Int % 32;
		
		for(i=32;i<64;i++)
			d.register[rd][i]=d.register[rn][i];
		
		// LSL - Logical Shift Left			
		for(i=0;i<operand2Int;i++)
		{
			for(j=33;j<=63;j++)
				d.register[rd][j-1]=d.register[rd][j];
			d.register[rd][63]='0';
		}
		
	}
		catch(Exception e)
		{
			
		}
	}
	public void lslRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int rm=Integer.parseInt(rmS,2);
		int rn=Integer.parseInt(rnS,2);
		int rd=Integer.parseInt(rdS,2);
		
		String operand2=new String(d.register[rm]);
		int operand2Int= Integer.parseInt(operand2,2);
		
		operand2Int = operand2Int % 64;
		
		for(i=0;i<64;i++)
			d.register[rd][i]=d.register[rn][i];
		
		// LSL - Logical Shift Left			
		for(i=0;i<operand2Int;i++)
		{
			for(j=1;j<=63;j++)
				d.register[rd][j]=d.register[rd][j+1];
			d.register[rd][63]='0';
		}
		
	}
		catch(Exception e)
		{
			
		}
	}
	
	public void lsrRegister32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int rm=Integer.parseInt(rmS,2);
		int rn=Integer.parseInt(rnS,2);
		int rd=Integer.parseInt(rdS,2);
		
		String operand2=new String(d.register[rm]);
		int operand2Int= Integer.parseInt(operand2,2);
		
		operand2Int = operand2Int % 32;
		
		for(i=32;i<64;i++)
			d.register[rd][i]=d.register[rn][i];
		
		// LSR - Logical Shift Right			
		for(i=0;i<operand2Int;i++)
		{
			for(j=62;j>=32;j--)
				d.register[rd][j+1]=d.register[rd][j];
			d.register[rd][32]='0';
		}
		
	}
		catch(Exception e)
		{
			
		}
	}
	public void lsrRegister64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		try{
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int rm=Integer.parseInt(rmS,2);
		int rn=Integer.parseInt(rnS,2);
		int rd=Integer.parseInt(rdS,2);
		
		String operand2=new String(d.register[rm]);
		int operand2Int= Integer.parseInt(operand2,2);
		
		operand2Int = operand2Int % 64;
		
		for(i=0;i<64;i++)
			d.register[rd][i]=d.register[rn][i];
		
		// LSR - Logical Shift Right			
		for(i=0;i<operand2Int;i++)
		{
			for(j=62;j>=0;j--)
				d.register[rd][j+1]=d.register[rd][j];
			d.register[rd][0]='0';
		}
		
	}
		catch(Exception e)
		{
			
		}
	}

	public void movInvertedWideImmediate32bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		int rd=Integer.parseInt(rdS, 2);
		
		String imm16="";
		for(i=0;i<16;i++)
			imm16=imm16+s.charAt(i+11);
		
		String posS="";
		posS=""+s.charAt(9)+s.charAt(10)+"0000";
		
		int pos= Integer.parseInt(posS, 2);
		
		char result[]=new char[32];
		
		result= padLeft("", 32,'0').toCharArray();
		
		int firstBit = 31 - (pos+15);
		int lastBit = 31 - pos;
		
		for(i=firstBit,j=0;i<=lastBit;i++,j++)
			result[i]=imm16.charAt(j);
		
		/*for(i=0;i<32;i++)
			if(result[i]=='0')
				result[i]='1';
			else
				result[i]='0';*/
		
		result=NOT(new String(result)).toCharArray();
		
		//for(i=0;i<32;i++)
			//d.register[rd][i]='0';
		
		String result64= padLeft(new String(result), 64,'0');
		d.register[rd]=result64.toCharArray();
		//for(i=32,j=0;i<64;i++,j++)
			//d.register[rd][i]=result64.charAt(i);
		
	}
	public void movInvertedWideImmediate64bit(String s,String hexInstruction,FileWriter fw)
	{
		int i,j;
		
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		int rd=Integer.parseInt(rdS, 2);
		
		String imm16="";
		for(i=0;i<16;i++)
			imm16=imm16+s.charAt(i+11);
		
		String posS="";
		posS=""+s.charAt(9)+s.charAt(10)+"0000";
		
		int pos= Integer.parseInt(posS, 2);
		
		char result[]=new char[64];
		
		for(i=0;i<64;i++)
			result[i]='0';
		
		int firstBit = 63 - (pos+15);
		int lastBit = 63 - pos;
		
		for(i=firstBit,j=0;i<=lastBit;i++,j++)
			result[i]=imm16.charAt(j);
		
		for(i=0;i<64;i++)
			if(result[i]=='0')
				result[i]='1';
			else
				result[i]='1';
		
		for(i=0;i<64;i++)
			d.register[rd][i]=result[i];
		
	}
	
	public void movBitmaskImmediate(String s,String hexInstruction,FileWriter fw, boolean extend, int datasize)
	{
		int i,j;
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
				
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		int rn= Integer.parseInt(rnS,2);
		int rd= Integer.parseInt(rdS,2);
		
		
		
		String immr="";
		
		char N=s.charAt(9);
		
		for(i=0;i<6;i++)
		{
			immr=immr+s.charAt(i+10);
		}
		
		String imms="";
		for(i=0;i<6;i++)
		{
			imms=imms+s.charAt(i+16);
		}
		
		boolean setflags=false;
		String tempresult[]=decodeBitMast(N, imms, immr, true);
		String imm= tempresult[0];
		
		char operand1[];
		char result[]=new char[datasize];
		if(rn!=31)
		{
			operand1= new String(d.register[rn]).substring(32).toCharArray();
		}
		else
		{
			operand1=padLeft("", datasize, '0').toCharArray();
		}
		char operand2[]= imm.toCharArray();
		
		result=OR(new String(operand1), new String(operand2)).toCharArray();
		
		
		if(rd==31 && !setflags)
		{
			// get address to which stack pointer is pointing
			/*
			String sp=Long.toHexString(Long.parseLong(new String(stackPointer), 2));
			sp= padLeft(sp, 16, '0');
			
			// convert value to hex
			String value=Long.toHexString(Long.parseLong(new String(d.register[rd]), 2));
			value=padLeft(value, 16, '0');
			
			// insert value to map
			d.memory.put(sp, value);
			*/
			stackPointer=copy(result);
		}
		else
		{
			d.register[rd]=copy(result);
		}
		
	}
	
	public void UBFM(String s,String hexInstruction,FileWriter fw, boolean extend, int datasize)
	{
		int i,j;
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
				
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		int rn= Integer.parseInt(rnS,2);
		int rd= Integer.parseInt(rdS,2);
		
		String immr="";
		
		char N=s.charAt(9);
		
		for(i=0;i<6;i++)
		{
			immr=immr+s.charAt(i+10);
		}
		
		String imms="";
		for(i=0;i<6;i++)
		{
			imms=imms+s.charAt(i+16);
		}
		
		String wtMask[] = decodeBitMast(N, imms, immr, false);
		String wMask = wtMask[0];
		String tMask= wtMask[1];
		
		String dst = "";
		dst = padLeft(dst, 32, '0');
		String src = "";
		
		if(datasize == 32)
		{
			for(i=32;i<64;i++)
				src=src+d.register[rn][i];
		}
		else
		{
			src = new String(d.register[rn]);
		}
		int R = Integer.parseInt(immr, 2);
		int S = Integer.parseInt(imms, 2);
		
		String bot=OR( AND(dst,NOT(wMask)),AND (ROR(src, R), wMask));
		String top;
		if(extend)
		{
			top = Replicate(""+src.charAt(src.length()-S-1), datasize);
		}
		else
		{
			top = dst;
		}
		String temp;
		if(datasize==32)
		{
			temp= OR(AND(top,NOT(tMask)),AND(bot,tMask));
			temp=padLeft(temp, 64, '0');
			d.register[rd]=temp.toCharArray();
		}
		else
		{
			d.register[rd]= OR(AND(top,NOT(tMask)),AND(bot,tMask)).toCharArray();
		}
	}
	
	public String Replicate(String a, int N)
	{
		int M = a.length();
		String result=a;
		if(N % M == 0)
		{
			for(int i=1;i<N/M;i++)
				result+=a;			
		}
		return result;
	}
	public String AND (String a , String b)
	{
		String c="";
		if(a.length()!=b.length())
		{
			System.out.println("Length is not same during AND");
			return "";
		}
		else
		{
			for(int i=0;i<a.length();i++)
			{
				if(a.charAt(i)=='1' && b.charAt(i)=='1')
					c=c+"1";
				else
					c=c+"0";
			}
			return c;
		}
	}
	public String OR (String a , String b)
		{
			String c="";
			if(a.length()!=b.length())
			{
				System.out.println("Length is not same during OR");
				return "";
			}
			else
			{
				for(int i=0;i<a.length();i++)
				{
					if(a.charAt(i)=='1' || b.charAt(i)=='1')
						c=c+"1";
					else
						c=c+"0";
				}
				return c;
			}
		
	}
	
	public String[] decodeBitMast(char immN,String imms,String immr, boolean immdiate)
	{
		String wtMask[]=new String[2];
		
		int len = HighestSetBit(immN + NOT(new String(imms)));
		
		String levels = padLeft(Ones(len), 6, '0');
		
		int immsInt = Integer.parseInt(imms,2);
		int immrInt =Integer.parseInt(immr, 2);
		int levelsInt=Integer.parseInt(levels, 2);
		
		int a=0;
		
		int S= immsInt & levelsInt;
		int R = immrInt & levelsInt;
		
		int diff=S-R;
		
		String esize=  padRight("1", len+1 , '0');
		int esizeInt = Integer.parseInt(esize, 2);
		
		String diffS = Integer.toBinaryString(diff);
		int difflength=diffS.length();
		
		diffS=diffS.substring(difflength-len);
		int d=Integer.parseInt(diffS, 2);
		
		String welem = padLeft(Ones(S+1),esizeInt, '0');
		String telem = padLeft(Ones(d+1),esizeInt, '0');
		
		welem= Replicate(ROR(welem, R),esizeInt);
		
		wtMask[0] = welem;
		wtMask[1]= telem;		
		return wtMask;
	}
	
	public String ROR (String s,int n)
	{
		// rotate right
		int j,i;
				char welemChar[] = s.toCharArray();
				
				if(n>0)
				{
					if(s.length()==32 || s.length()==64)
					{
						for(j=0;j<n;j++)
						{
							char temp=welemChar[s.length()-1];
				
							for(i=s.length()-2;i>=0;i--)
							{
								welemChar[i+1]=welemChar[i];
							}
							welemChar[0]=temp;
						}
					}
				}
				return new String(welemChar);
	}
	public String padRight(String a, int size, char pc)
	{
		int length=a.length();
		if(length<size)
		{
			for(int i=0;i<size-length;i++)
				a=a+pc;
		}
		return a;
	}
	public String Ones(int len)
	{
		String a="";
		for(int i=0;i<len;i++)
			a=a+"1";
		return a;
	}
	public String padLeft(String a, int size, char pc)
	{
		int length=a.length();
		if(length<size)
		{
			for(int i=0;i<size-length;i++)
				a=pc+a;
		}
		return a;
	}
	public static String padLeftStatic(String a, int size, char pc)
	{
		int length=a.length();
		if(length<size)
		{
			for(int i=0;i<size-length;i++)
				a=pc+a;
		}
		return a;
	}
	public String NOT(String a)
	{
		char result[]=new char[a.length()];
		
		for(int i=0;i<a.length();i++)
			if(a.charAt(i)=='1')
				result[i]='0';
			else
				result[i]='1';
		
		return new String(result);
	}
	public int HighestSetBit(String s)
	{
		int i;
		for(i=0;i<s.length();i++)
			if(s.charAt(i)=='1')				
				return s.length()-i-1;
		return -1;
	}
	
	public void STRImmediate(String s)
	{
		int i,j;
		boolean wback=false, postindex=false;
		int scale = Integer.parseInt(""+s.charAt(0)+s.charAt(1), 2);
		char offset[]=new char[64];
		
		if(s.charAt(6)=='0'&&s.charAt(7)=='0')
		{
			// post-index or pre-index
			if(s.charAt(20)=='0'&&s.charAt(21)=='1')
			{
				// post-index
				wback=true;
				postindex=true;
				
				// fetch imm9
				String imm9="";
				for(i=0;i<9;i++)
					imm9=imm9+s.charAt(i+11);
				
				offset=signExtend(imm9, 64).toCharArray();
				
			}
			if(s.charAt(20)=='1'&&s.charAt(21)=='1')
			{
				// pre-index
				wback=true;
				postindex=false;
				
				// fetch imm9
				String imm9="";
				for(i=0;i<9;i++)
					imm9=imm9+s.charAt(i+11);
				
				offset=signExtend(imm9, 64).toCharArray();
			}
		}
		if(s.charAt(6)=='0'&&s.charAt(7)=='1')
		{
			// Unsigned offset
			wback=false;
			postindex=false;
			
			// fetch imm12
			String imm12="";
			for(i=0;i<12;i++)
				imm12=imm12+s.charAt(i+10);
			
			offset= LSL(padLeft(imm12, 64, '0'),scale).toCharArray();
		}
		//get Rn
		String Rn="";
		for(i=0;i<5;i++)
			Rn=Rn+s.charAt(i+22);
			
		//get Rt
		String Rt="";
		for(i=0;i<5;i++)
			Rt=Rt+s.charAt(i+27);
		
		int n= Integer.parseInt(Rn,2);
		int t= Integer.parseInt(Rt, 2);
		
		String AccType="AccType_NORMAL";
		
		String memop="";
		boolean signed= false;
		int regsize=0;
		
		// fetch opc
		String opc=""+s.charAt(8)+s.charAt(9);
		
		//fetch size
		String size= ""+s.charAt(0)+s.charAt(1);
		
		if(opc.charAt(0)=='0')
		{
			// store or zero extnding load
			if(opc.charAt(1)=='1')
				memop="MemOp_LOAD";
			else
				memop="MemOp_STORE";
			
			if(size.equals("11"))
				regsize=64;
			else
				regsize=32;
			signed=false;
		}
		else
		{
			if(size.equals("11"))
			{
				memop="MemOp_PREFETCH";
				if(opc.charAt(1)=='1')
				{
					// UnallocatedEncoding();
				}
			}
			else
			{
				// sign-extending load
				memop="MemOp_LOAD";
				if(size.equals("10") && opc.charAt(1)=='1')
				{
					// UnallocatedEncoding();
				}
				
				if(opc.charAt(1)=='1')
					regsize=32;
				else
					regsize=64;
				signed=true;
			}
		}
		
		int datasize = Integer.parseInt(padRight("100", 3+scale, '0'), 2);
		
		char address[]=new char[64];
		char data[]=new char[datasize];
		boolean wb_unknown=false;
		boolean rt_unknown=false;
		
		if(n==31)
		{
			if(!memop.equals("MemOp_PREFETCH"))
			{
				// CheckSPAlignment()
				address=copy(stackPointer);
			}
		}
		else
		{
			address=copy(d.register[n]);
		}
		
		int duplicate=0;
		
		if(!postindex)
			address=addAdresses(address, offset);
		
		if(memop.equals("MemOp_STORE"))
		{
			if(rt_unknown)
			{
				// data = unknown
			}
			else
			{
				data=copy(d.register[t]);
			}
			storeDataToMemory(address, datasize/8, AccType, data);
		}
		if(memop.equals("MemOp_LOAD"))
		{
			
			data=getDataFromMemory(address, datasize/8, AccType);
			if(signed)
			{
				d.register[t]=signExtend(new String(data), regsize).toCharArray();
			}
			else
			{
				d.register[t]=padLeft(new String(data), 64,'0').toCharArray();
			}
		}
		if(memop.equals("MemOp_PREFETCH"))
		{
			// nothing is implemented
			// Prefetch(address,t)
		}
		if(wback)
		{
			if(wb_unknown)
			{
				// address = unknown
			}
			else
			{
				if(postindex)
				{
					address=addAdresses(address, offset);
				}
			}
			if(n==31)
				stackPointer=copy(address);
			else
				d.register[n]=copy(address);
		}
	}

	public char[] addAdresses(char[] add1, char[] add2) 
	{
		// perform addition
		char carry='0',result[]=new char[64];
		int i;
		for(i=63;i>=0;i--)
		{
			if(add1[i]=='0' && add2[i]=='0')
				if(carry=='0')
				{
					result[i]='0';
					carry='0';
				}
				else
				{
					result[i]='1';
					carry='0';
				}
			if(add1[i]=='0' && add2[i]=='1')
				if(carry=='0')
				{
					result[i]='1';
					carry='0';
				}
				else
				{
					result[i]='0';
					carry='1';
				}
			if(add1[i]=='1' && add2[i]=='0')
				if(carry=='0')
				{
					result[i]='1';
					carry='0';
				}
				else
				{
					result[i]='0';
					carry='1';
				}
			if(add1[i]=='1' && add2[i]=='1')
				if(carry=='0')
				{
					result[i]='0';
					carry='1';
				}
				else
				{
					result[i]='1';
					carry='1';
				}
		}
		return result;
	}
	public String signExtend(String a,int n)
	{
		int i,length=a.length();
		String result=a;
		char extendBit=a.charAt(0);
		if(length<n)
		{
			for(i=0;i<n-length;i++)
				result=""+extendBit+result;
		}
		return result;
	}
	private char[] Extend(String parameter1, int n, boolean unsigned) 
	{	
		if(unsigned)
		{
			return padLeft(parameter1, n, '0').toCharArray();
		}
		else
		{
			return signExtend(parameter1, n).toCharArray();
		}
	}
	public String LSL(String a,int shift)
	{
		int size=a.length(),i,j;
		char result[]=new char[size];
		
		for(i=0;i<size;i++)
			result[i]='0';
		
		for(i=shift,j=0;i<size;i++,j++)
			result[j]=a.charAt(i);
		return new String(result);
	}
	public char[] ExtendReg (int reg, String extend_type, int shift,int N)
	{
		// pass N as 64 by default
		
		char val[]=new char[64];
		val=copy(d.register[reg]); 
		boolean unsigned=false;
		int len=0;
		
		if(extend_type.equals("ExtendType_SXTB"))
		{	unsigned=false; 	len=8;		}
		if(extend_type.equals("ExtendType_SXTH"))
		{	unsigned=false; 	len=16;		}
		if(extend_type.equals("ExtendType_SXTW"))
		{	unsigned=false; 	len=32;		}
		if(extend_type.equals("ExtendType_SXTX"))
		{	unsigned=false; 	len=64;		}
		
		if(extend_type.equals("ExtendType_UXTB"))
		{	unsigned=true; 	len=8;		}
		if(extend_type.equals("ExtendType_UXTH"))
		{	unsigned=true; 	len=16;		}
		if(extend_type.equals("ExtendType_UXTW"))
		{	unsigned=true; 	len=32;		}
		if(extend_type.equals("ExtendType_UXTX"))
		{	unsigned=true; 	len=64;		}
		
		
		len= Math.min(len, N-shift);
		
		String parameter1="";
		for(int i=0;i<len;i++)
		{
			parameter1=parameter1+ val[val.length-i-1] ;
		}
		
		parameter1=padRight(parameter1, parameter1.length()+shift, '0');
		
		return Extend(parameter1,N,unsigned);
		
	}
	private String DecodeRegExtend(String option) {
		
		if(option.equals("000")) return "ExtendType_UXTB";
		if(option.equals("001")) return "ExtendType_UXTH";
		if(option.equals("010")) return "ExtendType_UXTW";
		if(option.equals("011")) return "ExtendType_UXTX";
		if(option.equals("100")) return "ExtendType_SXTB";
		if(option.equals("101")) return "ExtendType_SXTH";
		if(option.equals("110")) return "ExtendType_SXTW";
		if(option.equals("111")) return "ExtendType_SXTX";
		
		return null;
	}
	public char[] copy(char a[])
	{
		char result[]=new char[a.length];
		for(int i=0;i<a.length;i++)
			result[i]=a[i];
		return result;
	}
	
	public char[] getDataFromMemory(char[] address, int size, String string) 
	{
		if(size==2)
		{
			// convert address to hex string
			String hexAddress=Long.toHexString(Long.parseLong(new String(address),2));						
			hexAddress=padLeft(hexAddress, 16, '0');	
			//return d.memory.get(hexAddress).toCharArray();
			String data=d.memory.get(hexAddress);
			return Integer.toBinaryString(Integer.parseInt(data,16)).toCharArray();
		}
		if(size==4)
		{
			char nextAddress[]=new char[64];
			char fourBytes[]= padLeft("100", 64, '0').toCharArray();
			nextAddress=addAdresses(address, fourBytes);
			
			// convert both of them to hex string
			String hexAddress=Long.toHexString(Long.parseLong(new String(address),2));
			String nextHexAddress=Long.toHexString(Long.parseLong(new String(nextAddress),2));
			
			hexAddress=padLeft(hexAddress, 16, '0');
			nextHexAddress=padLeft(nextHexAddress, 16, '0');
			
			String result=d.memory.get(nextHexAddress) + d.memory.get(hexAddress);
			
			return Integer.toBinaryString(Integer.parseInt(result,16)).toCharArray();
			
		}
		return null;
	}
	
	public void STPRegisterPair(String s)
	{
		int i,j;
		
		 // get opc
		String opc = "" + s.charAt(0) + s.charAt(1);
		char L = s.charAt(9);
		
		// get imm7
		String imm7="";
		for(i=0;i<7;i++)
			imm7=imm7+s.charAt(i+10);
		
		//get Rt2
		String Rt2="";
		for(i=0;i<5;i++)
			Rt2=Rt2+s.charAt(i+17);
		
		//get Rn
		String Rn="";
		for(i=0;i<5;i++)
			Rn=Rn+s.charAt(i+22);
			
		//get Rt
		String Rt="";
		for(i=0;i<5;i++)
			Rt=Rt+s.charAt(i+27);
		
		boolean postindex=false,preindex=false,signedOffset=false;
		boolean wback=false;
		
		if(s.charAt(7)=='0' && s.charAt(8)=='1')
		{	postindex=true;	 	wback=true;	   }
		if(s.charAt(7)=='1' && s.charAt(8)=='1')
		{	preindex=true;	 	wback=true;	   }
		if(s.charAt(7)=='1' && s.charAt(8)=='0')
		{	signedOffset=true;	wback=false;   }
		
		
		int n= Integer.parseInt(Rn,2);
		int t= Integer.parseInt(Rt, 2);
		int t2=Integer.parseInt(Rt2, 2);
		
		String AccType="AccType_NORMAL";
		
		String memop="";
		if(L=='1')
			memop="MemOp_LOAD";
		else
			memop="MemOp_STORE";
		
		boolean signed= opc.charAt(1)!='0' ;
		int scale= 2 + Integer.parseInt(""+opc.charAt(0),2);
		int datasize = Integer.parseInt(padRight("100", 3+scale, '0'), 2);
		
		char offset[]=new char[64];
		offset= LSL(signExtend(imm7, 64),scale).toCharArray();
		
		char address[]=new char[64];
		char data1[]=new char[datasize];
		char data2[]=new char[datasize];
		int dbytes= datasize / 8;
		boolean rt_unknown=false;
		boolean wb_unknown= false;
		
		if(n==31)
			address= copy(stackPointer);
		else
			address=copy(d.register[n]);
		
		if(!postindex)
			address=addAdresses(address, offset);
		if(memop.equals("MemOp_STORE"))
		{
			if(rt_unknown && t==n)
			{  //data1=unknown
			}
			else
			{
				data1=copy(d.register[t]);
			}
			if(rt_unknown && t2==n)
			{
				//data2=unknown
			}
			else
			{
				data2=copy(d.register[t2]);
			}
			
			char dbytesInArray[] = padLeft(Integer.toBinaryString(dbytes), 64, '0').toCharArray();
			
			storeDataToMemory(address,dbytes,AccType,data1);
			storeDataToMemory(addAdresses(address,dbytesInArray),dbytes,AccType,data2);
		}
		if(memop.equals("MemOp_LOAD"))
		{
			char dbytesInArray[] = padLeft(Integer.toBinaryString(dbytes), 64, '0').toCharArray();
			data1=getDataFromMemory(address, dbytes, AccType);
			data1=getDataFromMemory(addAdresses(address,dbytesInArray), dbytes, AccType);
			if(rt_unknown)
			{
				// data1 and data2 unknown
			}
			if(signed)
			{
				d.register[t]=signExtend(new String(data1), 64).toCharArray();
				d.register[t2]=signExtend(new String(data2), 64).toCharArray();				
			}
			else
			{
				d.register[t]=copy(data1);
				d.register[t2]=copy(data2);
			}
		}
		if(wback)
		{
			if(wb_unknown)
			{
				// address = unknown
			}
			else
				if(postindex)
					address=addAdresses(address, offset);
			if(n==31)
				stackPointer=copy(address);
			else
				d.register[n]=copy(address);
		}
	}
	
	public void storeDataToMemory(char[] address, int dbytes, String accType,char[] data) 
	{
		
		if(dbytes==2)
		{
			// convert address to hex string
			String hexAddress=Long.toHexString(Long.parseLong(new String(address),2));
			String hexData= Long.toHexString(Long.parseLong(new String(data).substring(32),2));
			hexData=padLeft(hexData, 8, '0');
			hexAddress=padLeft(hexAddress, 16, '0');
			d.memory.put(hexAddress, hexData);
		}
		if(dbytes==4)
		{
			// break them in two parts
			String dataString=new String(data);
			String LSB = dataString.substring(32);
			String MSB= dataString.substring(0, 32);
			
			String hexLSB=Long.toHexString(Long.parseLong(LSB, 2));
			String hexMSB=Long.toHexString(Long.parseLong(MSB, 2));
			
			hexLSB=padLeft(hexLSB, 8, '0');
			hexMSB=padLeft(hexMSB, 8, '0');
			
			char nextAddress[]=new char[64];
			char fourBytes[]= padLeft("100", 64, '0').toCharArray();
			nextAddress=addAdresses(address, fourBytes);
			
			// convert both of them to hex string
			String hexAddress=Long.toHexString(Long.parseLong(new String(address),2));
			String nextHexAddress=Long.toHexString(Long.parseLong(new String(nextAddress),2));
			
			hexAddress=padLeft(hexAddress, 16, '0');
			nextHexAddress=padLeft(nextHexAddress, 16, '0');
			
			d.memory.put(hexAddress, hexLSB);
			d.memory.put(nextHexAddress, hexMSB);			
		}
		
	}
	
	public void LDRLiteral(String s)
	{
		int i,j;
		
		// fetch rt
		String rtS="";
		for(i=0;i<5;i++)
			rtS=rtS+s.charAt(i+27);
		int rt=Integer.parseInt(rtS, 2);
		
		// fetch imm19
		String imm19="";
		for(i=0;i<19;i++)
			imm19+=s.charAt(i+8);
		
		boolean signed=false;
		int size=0;
		String MemOp="MemOp_Load";
		
		String opc=""+s.charAt(0)+s.charAt(1);
		
		if(opc.equals("00"))
			size=4;
		if(opc.equals("01"))
			size=8;
		if(opc.equals("10"))
		{
			size=4;
			signed=true;
		}
		if(opc.equals("11"))
			MemOp="MemOp_Prefetch";
			
		char offset[]= signExtend(imm19+"00", 64).toCharArray();
		
		// get the address 
		char address[]=new char[64];
		
		String temp = padLeft(Long.toBinaryString(d.programCounter), 64, '0');
		
		address= addAdresses(temp.toCharArray(),offset);
		
		char data[]=new char[size*8];
		
		if(MemOp.equals("MemOp_Load"))
		{
			data= getDataFromMemory(address,size, "AccType_NORMAL");
		
			if(signed)
				d.register[rt] = signExtend(new String (data), 64).toCharArray();
			else
				d.register[rt]=data;
		}
		if(MemOp.equals("MemOp_Prfetch"))
		{
			// not implemented
		}
		
	}
	public void LDRRegister(String s)
	{
		boolean wback=false;
		boolean postindex = false;
		
		int i,j;
		
		// fetch rt
		String rtS="";
		for(i=0;i<5;i++)
			rtS=rtS+s.charAt(i+27);
		int t=Integer.parseInt(rtS, 2);
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		int n=Integer.parseInt(rnS, 2);
				
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		int m=Integer.parseInt(rmS, 2);
		
		String option=""+ s.charAt(16) + s.charAt(17) + s.charAt(18);
		String size=""+ s.charAt(0)+ s.charAt(1);
		String opc="" + s.charAt(8)+s.charAt(9);
		
		char S = s.charAt(19);
		
		String AccType= "AccType_NORMAL";
		
		int scale= Integer.parseInt(size, 2);
		
		String extend_type= DecodeRegExtend(option);
		
		int shift;
		
		if(S=='1')
			shift=scale;
		else
			shift=0;
		
		String memop="";
		boolean signed=false;
		int regsize=0;
		
		if(opc.charAt(0)=='0')
		{
			// store or zero extending load
			if(opc.charAt(1)=='1')
			{
				memop="MemOp_LOAD";
			}
			else
			{
				memop="MemOp_STORE";
			}
			if(size.equals("11"))
			{
				regsize=64;
			}
			else
			{
				regsize=32;
			}
			signed=false;
		}
		else
		{
			if(size.equals("11"))
			{
				memop="MemOp_PREFETCH";
				if(opc.charAt(1)=='1')
				{
					// UnalloactedEncoding()
				}
			}
			else
			{
				//sign extending load
				memop="MemOp_LOAD";
				if(size.equals("10") && opc.charAt(1)=='1')
				{
					// UnalloacatedEncoding()
				}
				if(opc.charAt(1)=='1')
					regsize=32;
				else
					regsize=64;
				signed=true;
			}
		}
			
		int datasize = Integer.parseInt(padRight("100", 3+scale, '0'), 2);
		
		char offset[]=new char[64];
		char address[]=new char[64];
		char data[]=new char[datasize];
		
		boolean wb_unknown=false;
		boolean rt_unknown=false;
		
		if(n==31)
		{
			if(!memop.equals("MemOp_PREFETCH"))
			{
				// CeckSPAlignment()
			}
			address=copy(stackPointer);
		}
		else
		{
			address=copy(d.register[n]);
		}
		if(!postindex)
		{
			address=addAdresses(address, offset);
		}
		if(memop.equals("MemOp_STORE"))
		{
			if(rt_unknown)
			{
				//data=UNKNOWN
			}
			else
			{
				data=copy(d.register[t]);
			}
			storeDataToMemory(address, datasize /8, AccType, data);
		}
		if(memop.equals("MemOp_LOAD"))
		{
			data=getDataFromMemory(address, datasize /8, AccType);
			if(signed)
			{
				d.register[t]=signExtend(new String(data), regsize).toCharArray();
			}
			else
			{
				d.register[t]=padLeft(new String(data), regsize, '0').toCharArray();
			}
		}
		if(memop.equals("MemOp_PREFETCH"))
		{
			// Prefetch()
		}
		
		if(wback)
		{
			if(wb_unknown)
			{
				// address = unknown
			}
			else
				if(postindex)
				{
					address=addAdresses(address, offset);
				}
			if(n==31)
				stackPointer=copy(address);
			else
				d.register[n]=copy(address);
		}
		
	}

	public void addExtendedRegister32bit(String s)
	{
		int i,j;
		
	
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		
		
		// Convert everything to integer
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		boolean sub_op = s.charAt(1)=='1';
		boolean setflags= s.charAt(2)=='1';
		
		String extend_type=DecodeRegExtend(""+s.charAt(16)+s.charAt(17)+s.charAt(18));
		
		String imm3=""+ s.charAt(19)+s.charAt(20)+s.charAt(21);
		int shift=Integer.parseInt(imm3,2);
		
		int datasize =32;
		String spInHex="";
		 char operand1[]=new char[datasize];
		 if(stackPointer[63]!='X')
		 {
		 long sp = Long.parseLong(new String(stackPointer),2);
		 spInHex=Long.toHexString(sp);
		 spInHex=padLeft(spInHex, 16, '0');
		 }
		 // convert hex string to required length
		 
		 if(rn==31)
		 {			 
			 operand1=d.memory.get(spInHex).toCharArray();
		 }
		 else
		 {
			 operand1=copy(d.register[rn]);
		 }
		 char operand2[]=ExtendReg(rm, extend_type,shift,datasize);
		 
		 char nzcv[]=new char[4];
		 
		 char carry_in;
		 if(sub_op)
		 {
			 operand2=NOT(new String(operand2)).toCharArray();
			 carry_in='1';
		 }
		 else
		 {
			 carry_in='0';
		 }
		 if(datasize==32)
			 operand1=new String(operand1).substring(32).toCharArray();
		 
		 String result= AddWithCarry(operand1,operand2, carry_in, setflags);
		 result=padLeft(result, 64, '0');
		 
		 
		 if(rd==31 && !setflags)
		 {
			 d.memory.put(spInHex,result);
		 }
		 else
		 {
			 d.register[rd]=result.toCharArray();
		 }
			 
	}
	public void addExtendedRegister64bit(String s)
	{
		int i,j;
		
	
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		
		
		// Convert everything to integer
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		boolean sub_op = s.charAt(1)=='1';
		boolean setflags= s.charAt(2)=='1';
		
		String extend_type=DecodeRegExtend(""+s.charAt(16)+s.charAt(17)+s.charAt(18));
		
		String imm3=""+ s.charAt(19)+s.charAt(20)+s.charAt(21);
		int shift=Integer.parseInt(imm3,2);
		
		int datasize =64;
		 
		 char operand1[]=new char[datasize];
		 long sp = Long.parseLong(new String(stackPointer),2);
		 String spInHex=Long.toHexString(sp);
		 // convert hex string to required length
		 spInHex=padLeft(spInHex, 16, '0');
		 if(rn==31)
		 {			 
			 operand1=d.memory.get(spInHex).toCharArray();
		 }
		 else
		 {
			 operand1=copy(d.register[rn]);
		 }
		 char operand2[]=ExtendReg(rm, extend_type,shift,datasize);
		 
		 char nzcv[]=new char[4];
		 
		 char carry_in;
		 if(sub_op)
		 {
			 operand2=NOT(new String(operand2)).toCharArray();
			 carry_in='1';
		 }
		 else
		 {
			 carry_in='0';
		 }
		 
		 String result= AddWithCarry(operand1,operand2, carry_in, setflags);
		 result=padLeft(result, 64, '0');
		 
		 
		 if(rd==31 && !setflags)
		 {
			 d.memory.put(spInHex,result);
		 }
		 else
		 {
			 d.register[rd]=result.toCharArray();
		 }
			 
	}


	private String AddWithCarry(char[] operand1, char[] operand2, char carry_in,boolean setflags) 
	{		
		// perform addition
		int i;
		char carry=carry_in;
		char result[]=new char[operand1.length];
		for(i=operand1.length-1;i>=0;i--)
		{
			if(operand1[i]=='0' && operand2[i]=='0')
				if(carry=='0')
				{	result[i]='0';	carry='0';	}
				else
				{	result[i]='1';	carry='0';	}
			
			if(operand1[i]=='0' && operand2[i]=='1')
				if(carry=='0')
				{	result[i]='1';	carry='0';	}
				else
				{	result[i]='0';	carry='1';	}
			
			if(operand1[i]=='1' && operand2[i]=='0')
				if(carry=='0')
				{	result[i]='1';	carry='0';	}
				else
				{	result[i]='0';	carry='1';	}
			
			if(operand1[i]=='1' && operand2[i]=='1')
				if(carry=='0')
				{	result[i]='0';	carry='1';	}
				else
				{	result[i]='1';	carry='1';	}
		}
		if(setflags)
		{
			int length=result.length;
			if( operand2[0]==operand1[0])
			{
				if(result[0]==operand1[0])
				{
					d.carry=carry;
				}
				else
				{
					d.overflow='1';
				}
			}
			else
			{
				d.carry=carry;
			}
			
			if(result[0]=='1')
				d.negative='1';
			else
				d.negative='0';
			
			boolean zero=true;
			for(i=0;i<length-1;i++)
				if(result[i]=='1')
				{
					zero=false;
					break;
				}
			if(zero)
				d.zero='1';
			else
				d.zero='0';	
		}
		return new String(result);
	}

	public void subExtendedRegister32bit(String s)
	{
		int i,j;
		
	
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		
		
		// Convert everything to integer
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		boolean sub_op = s.charAt(1)=='1';
		boolean setflags= s.charAt(2)=='1';
		
		String extend_type=DecodeRegExtend(""+s.charAt(16)+s.charAt(17)+s.charAt(18));
		
		String imm3=""+ s.charAt(19)+s.charAt(20)+s.charAt(21);
		int shift=Integer.parseInt(imm3,2);
		
		int datasize =32;
		String spInHex="";
		 char operand1[]=new char[datasize];
		 if(stackPointer[63]!='X')
		 {
		 long sp = Long.parseLong(new String(stackPointer),2);
		 spInHex=Long.toHexString(sp);
		 spInHex=padLeft(spInHex, 16, '0');
		 }
		 // convert hex string to required length
		 
		 if(rn==31)
		 {			 
			 operand1=d.memory.get(spInHex).toCharArray();
		 }
		 else
		 {
			 operand1=copy(d.register[rn]);
		 }
		 char operand2[]=ExtendReg(rm, extend_type,shift,datasize);
		 
		 char nzcv[]=new char[4];
		 
		 char carry_in;
		 if(sub_op)
		 {
			 operand2=NOT(new String(operand2)).toCharArray();
			 carry_in='1';
		 }
		 else
		 {
			 carry_in='0';
		 }
		 if(datasize==32)
			 operand1=new String(operand1).substring(32).toCharArray();
		 
		 String result= AddWithCarry(operand1,operand2, carry_in, setflags);
		 result=padLeft(result, 64, '0');
		 
		 
		 if(rd==31 && !setflags)
		 {
			 d.memory.put(spInHex,result);
		 }
		 else
		 {
			 d.register[rd]=result.toCharArray();
		 }
			 
	}
	public void subExtendedRegister64bit(String s)
	{
		int i,j;
		
	
		// fetch rd
		String rdS="";
		for(i=0;i<5;i++)
		{
			rdS=rdS+s.charAt(27+i);
		}
		
		// fetch rn
		String rnS="";
		for(i=0;i<5;i++)
		{
			rnS=rnS+s.charAt(22+i);
		}
		
		// fetch rm
		String rmS="";
		for(i=0;i<5;i++)
		{
			rmS=rmS+s.charAt(11+i);
		}
		
		
		// Convert everything to integer
		
		int rn=	Integer.parseInt(rnS,2);		// First Operand
		int rd=Integer.parseInt(rdS,2);			// Destination
		int rm=	Integer.parseInt(rmS,2);		// Second Operand
		
		boolean sub_op = s.charAt(1)=='1';
		boolean setflags= s.charAt(2)=='1';
		
		String extend_type=DecodeRegExtend(""+s.charAt(16)+s.charAt(17)+s.charAt(18));
		
		String imm3=""+ s.charAt(19)+s.charAt(20)+s.charAt(21);
		int shift=Integer.parseInt(imm3,2);
		
		int datasize =64;
		String spInHex="";
		 char operand1[]=new char[datasize];
		 if(stackPointer[63]!='X')
		 {
		 long sp = Long.parseLong(new String(stackPointer),2);
		 spInHex=Long.toHexString(sp);
		 spInHex=padLeft(spInHex, 16, '0');
		 }
		 // convert hex string to required length
		 
		 if(rn==31)
		 {			 
			 operand1=d.memory.get(spInHex).toCharArray();
		 }
		 else
		 {
			 operand1=copy(d.register[rn]);
		 }
		 char operand2[]=ExtendReg(rm, extend_type,shift,datasize);
		 
		 char nzcv[]=new char[4];
		 
		 char carry_in;
		 if(sub_op)
		 {
			 operand2=NOT(new String(operand2)).toCharArray();
			 carry_in='1';
		 }
		 else
		 {
			 carry_in='0';
		 }
		 if(datasize==32)
			 operand1=new String(operand1).substring(32).toCharArray();
		 
		 String result= AddWithCarry(operand1,operand2, carry_in, setflags);
		 result=padLeft(result, 64, '0');
		 
		 
		 if(rd==31 && !setflags)
		 {
			 d.memory.put(spInHex,result);
		 }
		 else
		 {
			 d.register[rd]=result.toCharArray();
		 }
			 
	}

}


import java.awt.DisplayMode;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import nl.lxtreme.binutils.elf.Elf;
import nl.lxtreme.binutils.elf.ElfHeader;
import nl.lxtreme.binutils.elf.Section;


public class ARMv8Simulator {
	
	File AssemblyOutputFile;
	BufferedWriter bw;
	DataStructures dataStructureObj;
	FileWriter fw;
	long textStartAddress;
	java.util.List<Long> listOfBreakPoints;
	Elf elfObj;
	ElfHeader elfHeader;
//	String nextInstruction;
	//int programCounter;
	
	public ARMv8Simulator(File f)
	{
		try{
		//nextInstruction="";
		//programCounter=0;
			elfObj=new Elf(f);		
			
			
			elfHeader=elfObj.getHeader();
		AssemblyOutputFile=new File("AssemblyFile.txt");
		
		fw=new FileWriter(AssemblyOutputFile,true);
		
		bw=new BufferedWriter(new FileWriter(AssemblyOutputFile));
		
		dataStructureObj=new DataStructures();
		}
		catch(Exception ex)
		{
			System.out.println("Error in assembly output file and file writer");
		}
	}
	
	public static void main(String args[])
	{
		ARMv8Simulator armv8SimObj;
		String runState="run";
		String mode="-run";
		try{
			try{
				mode=args[1];
				if(mode.equalsIgnoreCase("-run"))
					runState="run";
				if(!(mode.equalsIgnoreCase("-debug")||mode.equalsIgnoreCase("-run")))
					{mode="-run";
					runState="run";
					}
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println("Mode is wrong");
			}
			mode="-debug";
			File f=new File(args[0]);
			armv8SimObj=new ARMv8Simulator(f);
			
			
			
			
			// Read text section
			Section textSection=armv8SimObj.elfObj.getSectionByName(".text");			
			String textInstructions= armv8SimObj.elfObj.getbinaryStringFromSection(textSection,0);
			
			// read data section
			Section dataSection=armv8SimObj.elfObj.getSectionByName(".data");			
			String dataSectionContents= armv8SimObj.elfObj.getbinaryStringFromSection(dataSection,0);
			
			if(armv8SimObj.elfHeader.isLittleEndian())
			{
				textInstructions= armv8SimObj.ConvertToBigEndian(textInstructions);
				dataSectionContents=armv8SimObj.ConvertToBigEndian(dataSectionContents);
			}
			
			long dataAddress=dataSection.getAddress();
			long textAddress =textSection.getAddress();
			
			// convert them to hex instructions
			
			
			
			Decoder decoderObj=new Decoder(armv8SimObj.dataStructureObj);
		
			
			
			// Put text section content to map
			int i;
			String temp;
			
			for(i=0;i<textInstructions.length();)
			{
				temp=textInstructions.substring(i,i+8);	
				String textHexAddress = Long.toHexString(textAddress);
				textHexAddress= Utils.padLeft(textHexAddress, 16, '0');
				armv8SimObj.dataStructureObj.memory.put(textHexAddress, temp);
				textAddress=textAddress+4;
				i=i+8;				
			}
			
			for(i=0;i<dataSectionContents.length();)
			{
				temp=dataSectionContents.substring(i,i+8);				
				String dataHexAddress = Long.toHexString(dataAddress);
				dataHexAddress= Utils.padLeft(dataHexAddress, 16, '0');
				armv8SimObj.dataStructureObj.memory.put(dataHexAddress, temp);
				dataAddress=dataAddress+4;
				i=i+8;				
			}
			
			armv8SimObj.textStartAddress=textSection.getAddress();
			armv8SimObj.listOfBreakPoints = new ArrayList<>();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			//java.util.List<String> listOfBreakPoints = new ArrayList<>();
			if(mode.equalsIgnoreCase("-debug"))
			{
				System.out.println("1.Enter break points in format break <Hex Address#>");
				System.out.println("2.Delete break points in format del <Address#>");
				 
				Boolean more=true;
				while (more) {
					//String breakPointString= System.console().readLine();
					
					String breakPointString = br.readLine();
					String tempBreak[]= breakPointString.split("\\s+");
					if(tempBreak[0]!=null)
					{
						if(tempBreak[0].equalsIgnoreCase("break"))
						{
							if(tempBreak[1]!=null)
							{
								try{
								long lbreakPoint=(Long.parseLong(armv8SimObj.hexToBinary(tempBreak[1]),2)-armv8SimObj.textStartAddress)/4;
								armv8SimObj.listOfBreakPoints.add(lbreakPoint);
								}
								catch(Exception e)
								{
									System.out.println("Invalid Address");
								}
								
							}
						}
						else if (tempBreak[0].equalsIgnoreCase("del")) {
							try{
								long lbreakPoint=(Long.parseLong(armv8SimObj.hexToBinary(tempBreak[1]),2)-armv8SimObj.textStartAddress)/4;
								armv8SimObj.listOfBreakPoints.remove(lbreakPoint);
								}
								catch(Exception e)
								{
									System.out.println("Invalid Address");
								}
						}
						else if(tempBreak[0].equalsIgnoreCase("run"))
						{
							runState="run";
							more=false;
						}
						else if (tempBreak[0].equalsIgnoreCase("s")) {
							runState="s";
							//if(!listOfBreakPoints.contains(0))
								//listOfBreakPoints.ad(0);
							more=false;
						}
						else if (tempBreak[0].equalsIgnoreCase("c")) {
							runState="c";
							more=false;
						}
							
					}
				}
			}
			
			textAddress =textSection.getAddress();
			long entryPoint=armv8SimObj.elfHeader.getEntryPoint();
			
			
			
			int instructionLength=8;
			long index=(entryPoint-textAddress)*8;
			long lineNumber;
			String binaryNextInstruction;
			
			armv8SimObj.fw.write("HexInstruction                    Assembly Instruction\r\n");
			
			while (index<textInstructions.length()) {
				lineNumber=(int)index/8;
				if(runState.equalsIgnoreCase("run"))
					armv8SimObj.listOfBreakPoints.clear();
				else if (runState.equalsIgnoreCase("s")) 
					runState= armv8SimObj.DisplayRegisters();
				else if(runState.equalsIgnoreCase("c"))
				{
					if(armv8SimObj.listOfBreakPoints.contains(lineNumber))
						runState= armv8SimObj.DisplayRegisters();
				}
				
				armv8SimObj.dataStructureObj.nextInstruction=textInstructions.substring((int)index,(int)index+instructionLength);
				System.out.println(armv8SimObj.dataStructureObj.nextInstruction);
				binaryNextInstruction=hexToBinary(armv8SimObj.dataStructureObj.nextInstruction);
				//Integer offSet=new Integer(1);
				armv8SimObj.fw.append("  "+armv8SimObj.dataStructureObj.nextInstruction+"Index"+index+"\r\n");
				
				armv8SimObj.dataStructureObj.programCounter+=8;
				decoderObj.ParseInstruction(binaryNextInstruction,1,armv8SimObj.dataStructureObj.nextInstruction,armv8SimObj.fw);
				index=armv8SimObj.dataStructureObj.programCounter;//+offSet.intValue()*8;
				
			}
			System.out.println("----------------------------The final state of Registers and Flag-------------------------------------------");
			armv8SimObj.DisplayDataStructureStatus();
			armv8SimObj.fw.close();
			
			/*ProcessBuilder pb = new ProcessBuilder("Notepad.exe", armv8SimObj.AssemblyOutputFile.getPath());
			pb.start();
			
			JTextField breakPoint=new JTextField();
			
			Object[] BreakPoint={
					"Enter Line number for Breakpoint",
					breakPoint
			};*/
			
			//int select=JOptionPane.showConfirmDialog(null, BreakPoint,"Add BreakPoint",JOptionPane.OK_CANCEL_OPTION);
		
			//java.util.List<Integer> listOfBreakPoints = new ArrayList<>();
			/*while (select==JOptionPane.OK_OPTION){
				try{
				int br=Integer.parseInt(breakPoint.getText());
				listOfBreakPoints.add(br);
				}
				catch(Exception ex)
				{
				
				}	
				
				select=JOptionPane.showConfirmDialog(null, BreakPoint,"Add BreakPoint",JOptionPane.OK_CANCEL_OPTION);
			}*/
			
			/*if(listOfBreakPoints.isEmpty())
			{
				listOfBreakPoints.add((textInstructions.length()/8)-1);
			}*/
			
			/*index=0;
			int lineNumber;
			
			armv8SimObj=new ARMv8Simulator();
			decoderObj=new Decoder(armv8SimObj.dataStructureObj);
			while (index<textInstructions.length()) {
				lineNumber=(int)index/8;
				armv8SimObj.dataStructureObj.nextInstruction=textInstructions.substring((int)index,(int)index+instructionLength);
				binaryNextInstruction=hexToBinary(armv8SimObj.dataStructureObj.nextInstruction);
				
				armv8SimObj.dataStructureObj.programCounter+=8;
				decoderObj.ParseInstruction(binaryNextInstruction,1,armv8SimObj.dataStructureObj.nextInstruction,armv8SimObj.fw);
				
				if(listOfBreakPoints.contains(lineNumber))
					armv8SimObj.DisplayRegisters();
				index=armv8SimObj.dataStructureObj.programCounter;
				
			}*/

		}
		catch(Exception ex)
		{
			System.out.println("Error in elf file "+ex);
		}
		
	}
	
	public void DisplayDataStructureStatus() {
		for(int i=0;i<31;i++)
			if(dataStructureObj.getRegisterValue(i).charAt(63)!='X')
				System.out.println("Register Content for register# "+i+"  is  "+dataStructureObj.getRegisterValue(i));
		System.out.println("Flags");
		System.out.println("Carry is  "+dataStructureObj.getCarry());
		System.out.println("Overflow is  "+dataStructureObj.getOverflow());
		System.out.println("Zero is  "+dataStructureObj.getZero());
		System.out.println("Negative is  "+dataStructureObj.getNegative());
	}
	
	public String DisplayRegisters() {
		String dispInput;
		String runState;
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		DisplayDataStructureStatus();
		
		dispInput=br.readLine();
		while (true) {
			String[] dispInputArray=dispInput.split("\\s");
			
			if(dispInputArray[0].equals("print"))
			{
				long dispFormatInt=-1;
				try{
					
					String dispFormat=dispInputArray[1].trim();
					try{
						 dispFormatInt=Long.parseLong(dispFormat);
					}
					catch(Exception e)
					{
						
					}
					if(dispFormat.equalsIgnoreCase("x"))
					{
						int reg=Integer.parseInt(dispInputArray[2].trim());
						if(reg<32 && reg>=0)
						{
							try{
							long regValue=Long.parseLong(dataStructureObj.getRegisterValue(reg),2);
							System.out.println("Value of register#"+reg +" is "+Long.toHexString(regValue));
							}
							catch(Exception e)
							{
								System.out.println("The register contains garbage value#"+dataStructureObj.getRegisterValue(reg));
							}
						}
						else 
							System.out.println("Wrong register value");
					}
					else if (dispFormat.equalsIgnoreCase("d")) {
						int reg=Integer.parseInt(dispInputArray[2].trim());
						if(reg<32 && reg>=0)
						{
							try{
							long regValue=Long.parseLong(dataStructureObj.getRegisterValue(reg),2);
							System.out.println("Value of register#"+reg +" is "+regValue);
							}
							catch(Exception e)
							{
								System.out.println("The register contains garbage value#"+dataStructureObj.getRegisterValue(reg));
							}
						}
						else 
							System.out.println("Wrong register value");
					}
					else if (dispFormatInt>0) {
						long numberOfMemoryAddressToread;
						long numberOfBytesToread;
						String memoryAddress;
						if(dispInputArray[2].equalsIgnoreCase("b"))
						{
							if(dispInputArray[4]!=null)
							{
								numberOfBytesToread=(dispFormatInt*8)/8;
								String hexContent= readMemoryAddresses(numberOfBytesToread, dispInputArray[4].trim());
								if(hexContent.length()>=dispFormatInt*2)
								{
									if(elfHeader.isLittleEndian())
										hexContent=ConvertToBigEndian(hexContent);
								for(int i=0;i<dispFormatInt;i++)
								{
									String valueToPrint=hexContent.substring(i*2,i*2+2);
									if(dispInputArray[3].equalsIgnoreCase("x"))
										System.out.print(valueToPrint+" ");
									else if (dispInputArray[3].equalsIgnoreCase("d")) {
										try{
											long longValueToPrint=Long.parseLong(valueToPrint,16);
											System.out.print(longValueToPrint+" ");
										}
										catch(Exception e)
										{
											System.out.println("Error in parsing value contained at location: "+valueToPrint);
										}
									}
									else {
										System.out.println("Invalid base character : "+dispInputArray[3]);
									}
								}
								}
								else 
									System.out.println("Error in fetch of memory addresses.");
							}
						}
						else if(dispInputArray[2].equalsIgnoreCase("w"))
						{
							if(dispInputArray[4]!=null)
							{
								numberOfBytesToread=(dispFormatInt*32)/8;
								String hexContent= readMemoryAddresses(numberOfBytesToread, dispInputArray[4].trim());
								
								if(hexContent.length()>=dispFormatInt*8)
								{
								for(int i=0;i<dispFormatInt;i++)
								{
									String valueToPrint=hexContent.substring(i*8,i*8+8);
									if(dispInputArray[3].equalsIgnoreCase("x"))
										System.out.print(valueToPrint+" ");
									else if (dispInputArray[3].equalsIgnoreCase("d")) {
										try{
											long longValueToPrint=Long.parseLong(valueToPrint,16);
											System.out.print(longValueToPrint+" ");
										}
										catch(Exception e)
										{
											System.out.println("Error in parsing value contained at location: "+valueToPrint);
										}
									}
									else {
										System.out.println("Invalid base character : "+dispInputArray[3]);
									}
									
								}
								}
								else 
									System.out.println("Error in fetch of memory addresses.");
							}
						}
						else if(dispInputArray[2].equalsIgnoreCase("d"))
						{
							if(dispInputArray[4]!=null)
							{
								numberOfBytesToread=(dispFormatInt*64)/8;
								String hexContent= readMemoryAddresses(numberOfBytesToread, dispInputArray[4].trim());
								
								if(hexContent.length()>=dispFormatInt*16)
								{
								for(int i=0;i<dispFormatInt;i++)
								{
									String valueToPrint=hexContent.substring(i*16,i*16+16);
									if(dispInputArray[3].equalsIgnoreCase("x"))
										System.out.print(valueToPrint+" ");
									else if (dispInputArray[3].equalsIgnoreCase("d")) {
										try{
											long longValueToPrint=Long.parseLong(valueToPrint,16);
											System.out.print(longValueToPrint+" ");
										}
										catch(Exception e)
										{
											System.out.println("Error in parsing value contained at location: "+valueToPrint);
										}
									}
									else {
										System.out.println("Invalid base character : "+dispInputArray[3]);
									}
									
								}
								}
								else 
									System.out.println("Error in fetch of memory addresses.");
							}
						}
					}
					else {
						System.out.println("Display format not supported");
					}
				}
				
				catch(Exception e)
				{
					
				}
			}
			else if(dispInputArray[0].equalsIgnoreCase("break"))
			{
				if(dispInputArray[1]!=null)
				{
					try{
					long lbreakPoint=(Long.parseLong(this.hexToBinary(dispInputArray[1]),2)-this.textStartAddress)/4;
					listOfBreakPoints.add(lbreakPoint);
					}
					catch(Exception e)
					{
						System.out.println("Invalid Address");
					}
					
				}
			}
			else if (dispInputArray[0].equalsIgnoreCase("del")) {
				try{
					long lbreakPoint=(Long.parseLong(this.hexToBinary(dispInputArray[1]),2)-textStartAddress)/4;
					listOfBreakPoints.remove(lbreakPoint);
					}
					catch(Exception e)
					{
						System.out.println("Invalid Address");
					}
			}
			else if(dispInputArray[0].equalsIgnoreCase("run"))
			{
				runState="run";
				break;
			}
			else if (dispInputArray[0].equalsIgnoreCase("s")) {
				runState="s";
				//if(!listOfBreakPoints.contains(0))
					//listOfBreakPoints.ad(0);
				break;
			}
			else if (dispInputArray[0].equalsIgnoreCase("c")) {
				runState="c";
				break;
			}
			dispInput=br.readLine();
		}
		}
		catch(Exception ex)
		{
			runState="c";
		}
		
		 return runState;
		
	}

	private String readMemoryAddresses(long numberOfBytesToread,String startAddress) {
		String memoryAddress=startAddress;
		long numberOfMemoryAddressToread=numberOfBytesToread/4;
		
		if(numberOfBytesToread%4!=0)
			numberOfMemoryAddressToread++;
		String hexString="";
		for(int i=0;i<numberOfMemoryAddressToread;i++)
		{
			memoryAddress= Utils.padLeft(memoryAddress, 16, '0') ;
			hexString= hexString+dataStructureObj.memory.get(memoryAddress);
			long longMemoryAddress=Long.parseLong(memoryAddress,16);
			longMemoryAddress=longMemoryAddress+4;
			memoryAddress=Long.toHexString(longMemoryAddress);
		}
		return hexString;
	}
	
	private  String ConvertToBigEndian(String strInput) {
		// TODO Auto-generated method stub
		int index=0;
		String newString="";
		while(index<strInput.length())
		{
			String sb;
			sb=strInput.substring(index,index+ 8);
			newString=newString+sb.toCharArray()[6];
			newString=newString+sb.toCharArray()[7];
			
			newString=newString+sb.toCharArray()[4];
			newString=newString+sb.toCharArray()[5];
			
			newString=newString+sb.toCharArray()[2];
			newString=newString+sb.toCharArray()[3];
			
			newString=newString+sb.toCharArray()[0];
			newString=newString+sb.toCharArray()[1];
			
			index=index+8;
		}
		return newString;
		}

	private static String hexToBinary(String hexInstruction) {
		String tempStr="";
		for(char c: hexInstruction.toCharArray())
		{
			String s="";
			s=s+c;
			tempStr=tempStr+String.format("%4s",Integer.toBinaryString(Integer.parseInt(s,16))).replace(' ', '0');
			
		}
		
		return tempStr;
	}


}

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class Debugger extends JFrame {

	private static final long serialVersionUID = 1L;
	int instructionCount;
	private JTable table;
	ARMv8Simulator armv8SimObj;
	Decoder decoderObj;
	ListSelectionModel listSelectionModel;
	Object[] columnTitles = { "Instruction#", "Hex Value of Instruction", "Assembly Instruction","" ,""};
	java.util.List<Integer> listOfBreakPoints;
	
	public Debugger()
	{
		instructionCount=0;
		armv8SimObj=new ARMv8Simulator();
		decoderObj=new Decoder(armv8SimObj.dataStructureObj);
		armv8SimObj.dataStructureObj.programCounter=0;
		 listOfBreakPoints = new ArrayList<>();
	}
	
	public void DebuggerWindow(Object[][] rowDataObjects) {
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
	    DefaultTableModel model = new DefaultTableModel(rowDataObjects, columnTitles);
	    
	    table = new JTable(model) {
	    	 public Class getColumnClass(int column) {
	                switch (column) {
	                    case 4:
	                        return Boolean.class;
	                    default:
	                    	 return String.class;
	                }
	            }
        };
        
        int index=0;
        
        JButton button = new JButton("Display Registers at next breakpoint");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	armv8SimObj=new ARMv8Simulator();
        		decoderObj=new Decoder(armv8SimObj.dataStructureObj);
        		armv8SimObj.dataStructureObj.programCounter=0;
        		JFrame tempframe = new JFrame();
        		JTable registerTable;
        		tempframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	String binaryNextInstruction;
            	int i=0;
            	table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            	
                for (; i < model.getRowCount(); ) {
                	System.out.println(i);
                	
                	//setBackground(Color.BLUE);
                	armv8SimObj.dataStructureObj.nextInstruction= model.getValueAt(i, 1).toString();
                	binaryNextInstruction=table.getValueAt(i, 3).toString();
                	armv8SimObj.dataStructureObj.programCounter+=8;
                	//decoderObj.ParseInstruction(binaryNextInstruction,1,armv8SimObj.dataStructureObj.nextInstruction,armv8SimObj.fw);
                    Boolean checked = (Boolean) table.getValueAt(i, 4);
                    
                    //if (listOfBreakPoints.contains(i)) {
                    if (checked) {
                      //registerTable=  armv8SimObj.DisplayRegisters();
                       //System.out.print("check"+checked);
                       /*JOptionPane pane = new JOptionPane( "Check Console Output For register Values at "+i+ " Or Click OK To Proceed To Next BreakPoint.");
                       // Configure via set methods
                       JDialog dialog = pane.createDialog(frame, "Message");
                       // the line below is added to the example from the docs
                       dialog.setModal(false); // this says not to block background components
                       dialog.show();
                       Object selectedValue = pane.getValue();
                       while ((Boolean)selectedValue!=true) {
					
					}*/
                     /* registerTable.setShowGrid(false);
                      registerTable.setShowHorizontalLines(false);
                      registerTable.setRowSelectionAllowed(true);
                      tempframe.add(registerTable);
                      tempframe.pack();*/
              	 
              	  tempframe.setSize(200, 300);
              	tempframe.setVisible(true);
                      
                       JOptionPane.showMessageDialog(frame,
                    		    "Check Console Output For register Values at "+i+ " Or Click OK To Proceed To Next BreakPoint.");
                    }
                   // i=armv8SimObj.dataStructureObj.programCounter/8;
                }
            }
        });
       /* table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method st
				Boolean checked = (Boolean) model.getValueAt(e.getFirstIndex(), 4);
				if(checked)
					if(!listOfBreakPoints.contains(e.getFirstIndex()))
					{
						System.out.println("ADDED BREAKPOINT "+e.getFirstIndex());
						listOfBreakPoints.add(e.getFirstIndex());
					}
				else
				{
					System.out.println("Removed BREAKPOINT "+e.getFirstIndex());
						listOfBreakPoints.remove(e.getFirstIndex());
				}
		}});*/
        table.setShowGrid(false);
	    table.setShowHorizontalLines(false);
	    table.setRowSelectionAllowed(true);
	    frame.add(new JScrollPane(table));
	    frame.add(button,BorderLayout.SOUTH);
	    frame.pack();
	  
	    frame.setSize(1300, 1300);
	    frame.setVisible(true);
	}
	
	
	
	public Object[] DecodebinaryNextInstructionructions(String binaryNextInstruction,String hexbinaryNextInstruction) 
	{
		Object[] rowData=new Object[5];
		try{
			
			
		File AssemblyOutputFile;
		AssemblyOutputFile=new File("AssemblyFile.html");
		FileWriter fw=new FileWriter(AssemblyOutputFile,true);
		if(hexbinaryNextInstruction.charAt(0)=='D' && hexbinaryNextInstruction.charAt(1)=='5' && hexbinaryNextInstruction.charAt(2)=='0' && hexbinaryNextInstruction.charAt(3)=='3' && hexbinaryNextInstruction.charAt(4)=='2' && hexbinaryNextInstruction.charAt(7)=='F' && binaryNextInstruction.charAt(27)=='1')
		{
			// NOP
			rowData[0]=instructionCount;
			rowData[1]=hexbinaryNextInstruction;
			rowData[2]="NOP";
			rowData[3]=binaryNextInstruction;
			rowData[4]=Boolean.FALSE;
			instructionCount++;
		}
		
		
		if(binaryNextInstruction.charAt(4)=='0')
		{
			// Data Processing - Immediate , Branch binaryNextInstructionruction
			if(binaryNextInstruction.charAt(5)=='0')
			{
				// Data Processing - immediate
				if(binaryNextInstruction.charAt(6)=='0')
				{
					// PC-rel addressing ,  Add/sub immediate
					if(binaryNextInstruction.charAt(7)=='0')
					{
						// PC-Re addressing
						if(binaryNextInstruction.charAt(0)=='0')
						{
							// ADR
							//fw.write(cbuf);
							////IEObj.ADR(binaryNextInstruction, hexbinaryNextInstructionruction, fw);
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ADR";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						else
						{
							// ADRP
							//IEObj.ADRP(binaryNextInstruction, hexbinaryNextInstructionruction, fw);
						}
					}
					else		//binaryNextInstruction.charAt(7)=='1'
					{
						// Add/sub - immediate

						
						if(binaryNextInstruction.charAt(1)=='0') // checks for op bit
						{
							// ADD or ADDS
							if(binaryNextInstruction.charAt(2)=='0') // checks for S bit
							{
								//ADD
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32-bit ADD immediate
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ADD 32 Immediate";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
									//IEObj.addImmediate32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
								}
								else
								{
									//64 bit ADD immediate
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ADD 64 Immediate";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;

									//IEObj.addImmediate64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
								}
							}
							else			// S=1
							{
								// ADDS
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32-bit ADDS immediate
									
									//IEObj.addsImmediate32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ADDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									//64 bit ADDS immediate
									
									//IEObj.addsImmediate64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ADDS 64";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
						}
						else		//binaryNextInstruction.charAt(1)=='1'
						{
							// SUB or SUBS
							
							if(binaryNextInstruction.charAt(2)=='0') // checks for S bit
							{
								// SUB
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32-bit SUB immediate
									
									//IEObj.subImmediate32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="SUB 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									//64 bit SUB immediate
									
									//IEObj.subImmediate64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="SUB 64";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							else			// S=1
							{
								// SUBS
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32-bit SUBS immediate
									
									//IEObj.subsImmediate32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="SUBS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									//64 bit SUBS immediate
									
									//IEObj.subsImmediate64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="SUB 64";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
						}
					}
				}
				else		//binaryNextInstruction.charAt(6)=='1'
				{
					// logical immediate,move wide, (bit field ,extract -> not required)

					
					if(binaryNextInstruction.charAt(7)=='0')
					{
						// logical immediate, move wide

						
						if(binaryNextInstruction.charAt(8)=='0')
						{
							//logical immediate
							
							if(binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(2)=='0')
							{
								//AND - immediate
								
								if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(9)=='0')		// opc
								{
									// 32-bit
									
									//IEObj.andImmediate32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="AND IMMEDIATE 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									// 64 - bit
									
									//IEObj.andImmediate64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="AND IMMEDIATE 64";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							if(binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(2)=='1')
							{
								// ANDS - immediate
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									// 32-bit
									
									//IEObj.andsImmediate32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									// 64 - bit
									
									//IEObj.andsImmediate64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
						}
						else		//binaryNextInstruction.charAt(8)=='1'
						{
							//Move wide immediate

							
							if(binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(2)=='0')
							{
								// MOVN immediate
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32 - bit MOVN Immediate
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
									
								}
								else
								{
									// 64- bit MOVN Immediate
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							
							if(binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(2)=='0')
							{
								// MOVZ immediate
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32 - bit MOVZ Immediate
									
									//IEObj.movz32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									// 64- bit MOVZ Immediate
									
									//IEObj.movz64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							
							if(binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(2)=='1')
							{
								// MOVK immediate
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									//32 - bit MOVK Immediate
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								else
								{
									// 64- bit MOVK Immediate
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							
							if(binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(2)=='1')
							{
								// Invalid binaryNextInstructionruction
								rowData[0]=instructionCount;
								rowData[1]=hexbinaryNextInstruction;
								rowData[2]="ANDS 32";
								rowData[3]=binaryNextInstruction;
								rowData[4]=Boolean.FALSE;
							}
						}

					}
					else		//binaryNextInstruction.charAt(7)=='1'
					{
						// Bit field ,Extract 
						if(binaryNextInstruction.charAt(8)=='0')
						{
							// Bit Field
							
							if(binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(2)=='0')
							{
								// SBFM also known as ASR - Arithmetic Shift Right
								// Here only ASR is implemented NOT SBFM
								
								if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(9)=='0')
								{
									// immediate ASR 32 bit
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
									
								}
								if(binaryNextInstruction.charAt(0)=='1' && binaryNextInstruction.charAt(9)=='1')
								{
									// immediate ASR 64 bit
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							if(binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(2)=='0')  // doubt
							{
								// UBFM which is also used for LSL/LSR
								
								if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(9)=='0')
								{
									// immediate LSL 32 bit
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
									
								}
								if(binaryNextInstruction.charAt(0)=='1' && binaryNextInstruction.charAt(9)=='1')
								{
									// immediate LSL 64 bit
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								
							}
						}
						else  // binaryNextInstruction.charAt(8)=='1'
						{
							// Extract
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
					}
				}
			}
			else 		//binaryNextInstruction.charAt(5)=='1'
			{
				// Branch binaryNextInstructionructions
				if(binaryNextInstruction.charAt(1)=='0')
				{
					// Unconditional Branch (Immediate)
					// Compare and Branch (Immediate)
					// Test and Branch (Immediate)
					if(binaryNextInstruction.charAt(2)=='0')
					{
						// Unconditional Branch (Immediate)
						if(binaryNextInstruction.charAt(0)=='0')
						{
							// B Branch
							//IEObj.B(binaryNextInstruction, hexbinaryNextInstructionruction, fw);
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="B";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						else
						{
							// BL Branch
							//IEObj.BL(binaryNextInstruction, hexbinaryNextInstructionruction, fw);
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="BL";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
					}
					else 		// binaryNextInstruction.charAt(2)=='1'
					{
						// Compare and Branch (Immediate)
						// Test and Branch (Immediate)
						if(binaryNextInstruction.charAt(6)=='0')
						{
							// Compare and Branch (Immediate)
							if(binaryNextInstruction.charAt(7)=='0')
							{
								// CBZ
								if(binaryNextInstruction.charAt(0)=='0')
								{
									// 32 bit CBZ
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="CBZ 32 Immediate";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;

									//IEObj.CBZ(binaryNextInstruction, hexbinaryNextInstructionruction, fw, binaryNextInstruction.charAt(0));
								}
								else
								{
									// 64 bit CBZ
									//IEObj.CBZ(binaryNextInstruction, hexbinaryNextInstructionruction, fw, binaryNextInstruction.charAt(0));
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="CBZ 64";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
							else 		//if(binaryNextInstruction.charAt(7)=='1')
							{
								// CBNZ
								if(binaryNextInstruction.charAt(0)=='0')
								{
									// 32 bit CBNZ
									//IEObj.CBNZ(binaryNextInstruction, hexbinaryNextInstructionruction, fw, binaryNextInstruction.charAt(0));
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="CBNZ 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
									
								}
								else
								{
									// 64 bit CBNZ
									//IEObj.CBNZ(binaryNextInstruction, hexbinaryNextInstructionruction, fw, binaryNextInstruction.charAt(0));
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="CBNZ 64";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
							}
						}
						else		//binaryNextInstruction.charAt(6)=='1'
						{
							// Test and Branch (Immediate)
							// Not to be implemented
						}
					}
				}
				else		//binaryNextInstruction.charAt(1)=='1'
				{
					// Conditional Branch (Immediate)
					// Exception generation
					// System
					// Unconditional Branch (register)
					
					if(binaryNextInstruction.charAt(0)=='0')
					{
						// Conditional Branch (Immediate)
						if(binaryNextInstruction.charAt(7)=='0' && binaryNextInstruction.charAt(27)=='0')
						{
							// B.cond
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
					}
					else		//binaryNextInstruction.charAt(0)=='1'
					{
						// Exception generation
						// System
						// Unconditional Branch (register)
						if(binaryNextInstruction.charAt(6)=='0')
						{
							// Exception generation
							// System
							
							// Not to be implemented
						}
						else		//binaryNextInstruction.charAt(6)=='1'
						{
							// Unconditional Branch (register)
							if(binaryNextInstruction.charAt(8)=='0')
							{
								// BR BLR RET
								if(binaryNextInstruction.charAt(9)=='0')
								{
									// BR BLR
									if(binaryNextInstruction.charAt(10)=='0')
									{
										// BR
										//IEObj.BR(binaryNextInstruction, hexbinaryNextInstructionruction, fw);
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									else		// binaryNextInstruction.charAt(10)=='1'
									{
										// BLR
										//IEObj.BLR(binaryNextInstruction, hexbinaryNextInstructionruction, fw);
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
								}
								else		//binaryNextInstruction.charAt(9)=='1'
								{
									// RET
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
									instructionCount++;
								}
								
							}
							else		//binaryNextInstruction.charAt(8)=='1'
							{
								// Not to be implemented
							}
						}
					}
					
					
				}
			}
		}
		else		//binaryNextInstruction.charAt(4)=='1'
		{
			// load and store binaryNextInstructionruction, data processing
			// for load and store bit 6 can be anything so skipping that
			// 4th bit is yet to be processed/checked
			if(binaryNextInstruction.charAt(6)=='0')
			{
				
				// load and store binaryNextInstructionruction
				// Please check 4th bit also, not 4th index
				// Please check 6th bit also, not 6th index
				if(binaryNextInstruction.charAt(2)=='0')
				{
					// Advanced SIMD -> not to be implemented
					// Load/ Store Exclusive -> Not to be implemented
					// Load Register literal
					if(binaryNextInstruction.charAt(3)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(7)=='0')
					{
						// Load Register literal
						if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0')
						{
							// LDR (literal) 32 bit
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='1')
						{
							// LDR (literal,SIMD&FP) 32 bit
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0')
						{
							// LDR (literal) 64 bit
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						if(binaryNextInstruction.charAt(0)=='0' && binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='1')
						{
							// LDR (literal,SIMD&FP) 64 bit
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						if(binaryNextInstruction.charAt(0)=='1' && binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0')
						{
							// LDRSW (literal)
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
						if(binaryNextInstruction.charAt(0)=='1' && binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='1')
						{
							// LDR (literal,SIMD&FP) 128 bit
							rowData[0]=instructionCount;
							rowData[1]=hexbinaryNextInstruction;
							rowData[2]="ANDS 32";
							rowData[3]=binaryNextInstruction;
							rowData[4]=Boolean.FALSE;
							instructionCount++;
						}
					}
				}
				else  		//binaryNextInstruction.charAt(2)=='1'
				{
					//load and store 3 to 12
					if(binaryNextInstruction.charAt(3)=='0')
					{
						// load / store - 3,4,5,6
						if(binaryNextInstruction.charAt(7)=='0')
						{
							// 3,4
							if(binaryNextInstruction.charAt(8)=='0')
							{
								// 3 
								// not to be implemented
							}
							else
							{
								//4
								// Load/store Register pair (post-indexed)
								if(binaryNextInstruction.charAt(0)=='0'&& binaryNextInstruction.charAt(1)=='0')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
								}
								if(binaryNextInstruction.charAt(0)=='0'&& binaryNextInstruction.charAt(1)=='1')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDPSW -> not to be implemented
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 64 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 64 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP 64 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP 64 bit
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 128 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 128 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
								}
							}
						}
						else  		//binaryNextInstruction.charAt(7)=='1'
						{
							// 5,6
							if(binaryNextInstruction.charAt(8)=='0')
							{
								// 5 
								// Load/store Register pair (offset)
								if(binaryNextInstruction.charAt(0)=='0'&& binaryNextInstruction.charAt(1)=='0')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 32 bit 
										rowData[0]=instructionCount;
										rowData[1]=hexbinaryNextInstruction;
										rowData[2]="ANDS 32";
										rowData[3]=binaryNextInstruction;
										rowData[4]=Boolean.FALSE;
										instructionCount++;
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 32 bit 
									}
								}
								if(binaryNextInstruction.charAt(0)=='0'&& binaryNextInstruction.charAt(1)=='1')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDPSW -> not to be implemented
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 64 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 64 bit 
									}
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP 64 bit 
									}
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP 64 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 128 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 128 bit 
									}
								}
							}
							else
							{
								//6
								// Load/store Register pair (pre-indexed)
								if(binaryNextInstruction.charAt(0)=='0'&& binaryNextInstruction.charAt(1)=='0')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP 32 bit 
									}
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP 32 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 32 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 32 bit 
									}
								}
								if(binaryNextInstruction.charAt(0)=='0'&& binaryNextInstruction.charAt(1)=='1')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDPSW -> not to be implemented
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 64 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 64 bit 
									}
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0')
								{
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP 64 bit 
									}
									if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP 64 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='0')
									{
										//STP(SIMD&FP) 128 bit 
									}
									if(binaryNextInstruction.charAt(5)=='1' && binaryNextInstruction.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 128 bit 
									}
								}
							}
						}
					}
					else  		//binaryNextInstruction.charAt(3)=='1'
					{
						// load / store - 7,8,9,10,11,12
						if(binaryNextInstruction.charAt(7)=='0')
						{
							// 7,8,9,10,11
							if(binaryNextInstruction.charAt(10)=='0')
							{
								//7,8,9,10
								if(binaryNextInstruction.charAt(20)=='0')
								{
									//7,8
									if(binaryNextInstruction.charAt(21)=='0')
									{
										//7
										// not to be implemented
									}
									else
									{
										//8
										// load/store register (immediate post-indexed)
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
										{
											// STR (immediate) 32 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
										{
											// LDR (immediate) 32 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
										{
											// STR (immediate) 64 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
										{
											// LDR (immediate) 64 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='1' && binaryNextInstruction.charAt(9)=='0')
										{
											// LDRSW (immediate) post-indexed
										}
									}
								}
								else
								{
									// 9,10
									if(binaryNextInstruction.charAt(21)=='0')
									{
										//9
										// not to be implemented
									}
									else
									{
										//10
										//load/store register (immediate pre-indexed)
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
										{
											// STR (immediate) 32 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
										{
											// LDR (immediate) 32 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
										{
											// STR (immediate) 64 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
										{
											// LDR (immediate) 64 bit
										}
										if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='1' && binaryNextInstruction.charAt(9)=='0')
										{
											// LDRSW (immediate) pre-indexed
										}
										
									}
								}
							}
							else
							{
								//11
								// load/store register (register offset)
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
								{
									// STR (register) 32 bit
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
								{
									// LDR (register) 32 bit
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
								{
									// STR (register) 64 bit
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
								{
									// LDR (register) 64 bit
								}
								if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='1' && binaryNextInstruction.charAt(9)=='0')
								{
									// LDRSW (register) 
								}
							}
						}
						else		//binaryNextInstruction.charAt(7)=='1'
						{
							//12
							// load/store (unsigned immediate)
							if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
							{
								// STR (immediate) 32 bit
							}
							if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
							{
								// LDR (immediate) 32 bit
							}
							if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='0')
							{
								// STR (immediate) 64 bit
							}
							if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='0' && binaryNextInstruction.charAt(9)=='1')
							{
								// LDR (immediate) 64 bit
							}
							if(binaryNextInstruction.charAt(0)=='1'&& binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(8)=='1' && binaryNextInstruction.charAt(9)=='0')
							{
								// LDRSW (immediate) Unsigned offset
							}
						}
					}
				}
				
				
			}
			else				// binaryNextInstruction.charAt(6)=='1'
			{
				//Data processing binaryNextInstructionructions also binaryNextInstruction.charAt(5)=='0'
				
				if(binaryNextInstruction.charAt(5)=='0' && binaryNextInstruction.charAt(6)=='1')
				{
					
					// Data Processing
					if(binaryNextInstruction.charAt(3)=='0')
					{
						// Logical (Shift Register)
						// Add/Subtract (Shift Register)
						// Add/Subtract (Extended Register)
						
						if(binaryNextInstruction.charAt(7)=='0')
						{
							// Logical (Shift Register)
							
							if(binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(2)=='0' &&binaryNextInstruction.charAt(10)=='0')      // 11th bit is N
							{
								// AND (Shift Register)
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									// 32 bit AND (Shift Register)
									
									//IEObj.andShiftedRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
								}
								else
								{
									// 64-bit AND (Shift Register)
									
									//IEObj.andShiftedRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
								}
							}
							if(binaryNextInstruction.charAt(1)=='1' && binaryNextInstruction.charAt(2)=='1' &&binaryNextInstruction.charAt(10)=='0')      // 11th bit is N
							{
								// ANDS (Shift Register)
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									// 32 bit ANDS (Shift Register)
									
									//IEObj.andsShiftRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
								}
								else
								{
									// 64-bit ANDS (Shift Register)
									
									//IEObj.addsShiftRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
									rowData[0]=instructionCount;
									rowData[1]=hexbinaryNextInstruction;
									rowData[2]="ANDS 32";
									rowData[3]=binaryNextInstruction;
									rowData[4]=Boolean.FALSE;
								}
							}
							if(binaryNextInstruction.charAt(1)=='0' && binaryNextInstruction.charAt(2)=='1' && binaryNextInstruction.charAt(10)=='0')      // 11th bit is N
							{
								// ORR (Shift Register) which is used for MOV shift register
								
								if(binaryNextInstruction.charAt(0)=='0')
								{
									// 32 bit MOV (Shift Register)
									
									//IEObj.movRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
								}
								else
								{
									// 64-bit MOV (Shift Register)
									
									//IEObj.movRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
								}
							}
						}
						else		//binaryNextInstruction.charAt(7)=='1'
						{
							// Add/Subtract (Shift Register)
							// Add/Subtract (Extended Register)
							
							// to distinguish between these 2, check bit 11 i.e. index 10
							
							if(binaryNextInstruction.charAt(10)=='0')
							{
								// Add/Subtract (Shift Register)
								if(binaryNextInstruction.charAt(1)=='0')	// op bit
								{
									// ADD (shifted register)
									// ADDS (shifted register)
									
									if(binaryNextInstruction.charAt(2)=='0')  	// S bit
									{
										// ADD (shifted register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit ADD (shifted register)
											
											//IEObj.addShiftRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
											rowData[0]=instructionCount;
											rowData[1]=hexbinaryNextInstruction;
											rowData[2]="ANDS 32";
											rowData[3]=binaryNextInstruction;
											rowData[4]=Boolean.FALSE;
										}
										else
										{
											// 64-bit ADD (shifted register)
											
											//IEObj.addShiftRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
											rowData[0]=instructionCount;
											rowData[1]=hexbinaryNextInstruction;
											rowData[2]="ANDS 32";
											rowData[3]=binaryNextInstruction;
											rowData[4]=Boolean.FALSE;
										}
									}
									else	// binaryNextInstruction.charAt(2)=='1'  ... S bit
									{
										//ADDS (shifted register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit ADDS (shifted register)
											
											//IEObj.addsShiftRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
											rowData[0]=instructionCount;
											rowData[1]=hexbinaryNextInstruction;
											rowData[2]="ANDS 32";
											rowData[3]=binaryNextInstruction;
											rowData[4]=Boolean.FALSE;
										}
										else
										{
											// 64-bit ADDS (shifted register)
											//IEObj.addShiftRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
											rowData[0]=instructionCount;
											rowData[1]=hexbinaryNextInstruction;
											rowData[2]="ANDS 32";
											rowData[3]=binaryNextInstruction;
											rowData[4]=Boolean.FALSE;
										}
									}
									
								}
								else 		//binaryNextInstruction.charAt(1)=='1' ... op bit
								{
									// SUB (shifted register)
									// SUBS (shifted register)
									
									if(binaryNextInstruction.charAt(2)=='0')  	// S bit
									{
										// SUB (shifted register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit SUB (shifted register)
											//IEObj.subShiftRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
										}
										else
										{
											// 64-bit SUB (shifted register)
											
											//IEObj.subShiftRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
										}
									}
									else	// binaryNextInstruction.charAt(2)=='1'  ... S bit
									{
										//SUBS (shifted register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit SUBS (shifted register)
											//IEObj.subsShiftRegister32bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
										}
										else
										{
											// 64-bit SUBS (shifted register)
											//IEObj.subsShiftRegister64bit(binaryNextInstruction,hexbinaryNextInstructionruction,fw);
										}
									}
									
								}
							}
							else		// binaryNextInstruction.charAt(10)=='1'
							{
								// ADD (extended register)
								// ADDS (extended register)
								// SUB (extended register)'
								// SUBS (extended register)
								if(binaryNextInstruction.charAt(1)=='0')	// op bit
								{
									// ADD (extended register)
									// ADDS (extended register)
									
									if(binaryNextInstruction.charAt(2)=='0')  	// S bit
									{
										// ADD (extended register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit ADD (extended register)
											
										}
										else
										{
											// 64-bit ADD (extended register)
											
										}
									}
									else	// binaryNextInstruction.charAt(2)=='1'  ... S bit
									{
										//ADDS (extended register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit ADDS (extended register)
											
										}
										else
										{
											// 64-bit ADDS (extended register)
											
										}
									}
									
								}
								else		// binaryNextInstruction.charAt(1)=='1' ... op bit
								{
									// SUB (extended register)
									// SUBS (extended register)
									
									if(binaryNextInstruction.charAt(2)=='0')  	// S bit
									{
										// SUB (extended register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit SUB (extended register)
											
										}
										else
										{
											// 64-bit SUB (extended register)
											
										}
									}
									else	// binaryNextInstruction.charAt(2)=='1'  ... S bit
									{
										//SUBS (extended register)
										
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32-bit SUBS (extended register)
											
										}
										else
										{
											// 64-bit SUBS (extended register)
											
										}
									}
								}
							}
							
							
						}
						
					}
					else		//binaryNextInstruction.charAt(3)=='1'
					{
						// Add/Subtract with Carry
						// Conditional Compare (Register)
						// Conditional Compare (Immediate)
						// Conditional Select
						// Data-processing (3 source)
						// Data-processing (2 source)
						// Data-processing (1 source)
						
						if(binaryNextInstruction.charAt(7)=='1')
						{
							// Data-processing (3 source)
							//NOT Required
						}
						else		//binaryNextInstruction.charAt(7)=='0'
						{
							// Add/Subtract with Carry
							// Conditional Compare (Register)
							// Conditional Compare (Immediate)
							// Conditional Select
							// Data-processing (2 source)
							// Data-processing (1 source)
							
							if(binaryNextInstruction.charAt(8)=='1' && binaryNextInstruction.charAt(9)=='1')
							{
								// Data-processing (2 source)
								// Data-processing (1 source)
								
								if(binaryNextInstruction.charAt(1)=='0')
								{
									// Data-processing (2 source)
									if(binaryNextInstruction.charAt(2)=='0' && binaryNextInstruction.charAt(16)=='0' && binaryNextInstruction.charAt(17)=='0' && binaryNextInstruction.charAt(18)=='1' && binaryNextInstruction.charAt(19)=='0' && binaryNextInstruction.charAt(20)=='1' && binaryNextInstruction.charAt(21)=='0')
									{
										// ASRV which is used for ASR (Register)
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32 bit ASR (Register)
										}
										else
										{
											// 64 bit ASR (Register)
										}
									}
									if(binaryNextInstruction.charAt(2)=='0' && binaryNextInstruction.charAt(16)=='0' && binaryNextInstruction.charAt(17)=='0' && binaryNextInstruction.charAt(18)=='1' && binaryNextInstruction.charAt(19)=='0' && binaryNextInstruction.charAt(20)=='0' && binaryNextInstruction.charAt(21)=='0')
									{
										// LSLV which is used for LSL (Register)
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32 bit LSL (Register)
										}
										else
										{
											// 64 bit LSL (Register)
										}
									}
									if(binaryNextInstruction.charAt(2)=='0' && binaryNextInstruction.charAt(16)=='0' && binaryNextInstruction.charAt(17)=='0' && binaryNextInstruction.charAt(18)=='1' && binaryNextInstruction.charAt(19)=='0' && binaryNextInstruction.charAt(20)=='0' && binaryNextInstruction.charAt(21)=='1')
									{
										// LSRV which is used for LSR (Register)
										if(binaryNextInstruction.charAt(0)=='0')
										{
											// 32 bit LSR (Register)
										}
										else
										{
											// 64 bit LSR (Register)
										}
									}
								}
								else  		//binaryNextInstruction.charAt(1)=='1'
								{
									// Data-processing (1 source)
								}
								
							}
							else	//binaryNextInstruction.charAt(8)!=1 || binaryNextInstruction.charAt(9)!=1
							{
								// Add/Subtract with Carry
								// Conditional Compare (Register)
								// Conditional Compare (Immediate)
								// Conditional Select
								if(binaryNextInstruction.charAt(8)=='1')
								{
									// Conditional Select
									// NOT Required
								}
								else		//binaryNextInstruction.charAt(8)=='0'
								{
									// Add/Subtract with Carry
									// Conditional Compare (Register)
									// Conditional Compare (Immediate)
									if(binaryNextInstruction.charAt(9)=='0')
									{
										// Add/Subtract with Carry
									}
									else	//binaryNextInstruction.charAt(9)=='1'
									{
										// Conditional Compare (Register)
										// Conditional Compare (Immediate)
										
										// NOT REQUIRED
									}
								}

							}
							
						}
						
					}
				}
				else	//binaryNextInstruction.charAt(5)!=0 || binaryNextInstruction.charAt(6)!=1
				{
					// not required
				}
			}
		}
		}
		catch(Exception e)
		{
		
		}
 return rowData;
	}

}

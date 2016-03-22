import java.io.FileWriter;
import java.io.IOException;
import java.sql.Ref;


public class Decoder {
	InstructionExecution IEObj;
	
	public Decoder(DataStructures dataStructureObj)
	{
		IEObj=new InstructionExecution(dataStructureObj);
	}
	public void ParseInstruction(String inst,int mode,String hexInstruction,FileWriter fw) throws IOException
	{
		//offSet=1;
		//String inst= "01010010100000000000000101000001";
		
		// For NOP Instruction
		String hexInst= hexInstruction; //Integer.toHexString(Integer.parseInt(inst, 2));
		if(hexInst.charAt(0)=='D' && hexInst.charAt(1)=='5' && hexInst.charAt(2)=='0' && hexInst.charAt(3)=='3' && hexInst.charAt(4)=='2' && hexInst.charAt(7)=='F' && inst.charAt(27)=='1')
		{
			// NOP
		}
		
		
		if(inst.charAt(4)=='0')
		{
			// Data Processing - Immediate , Branch Instruction
			if(inst.charAt(5)=='0')
			{
				// Data Processing - immediate
				if(inst.charAt(6)=='0')
				{
					// PC-rel addressing ,  Add/sub immediate
					if(inst.charAt(7)=='0')
					{
						// PC-Re addressing
						if(inst.charAt(0)=='0')
						{
							// ADR
							IEObj.ADR(inst, hexInstruction, fw);
						}
						else
						{
							// ADRP
							IEObj.ADRP(inst, hexInstruction, fw);
						}
					}
					else		//inst.charAt(7)=='1'
					{
						// Add/sub - immediate

						
						if(inst.charAt(1)=='0') // checks for op bit
						{
							// ADD or ADDS
							if(inst.charAt(2)=='0') // checks for S bit
							{
								//ADD
								if(inst.charAt(0)=='0')
								{
									//32-bit ADD immediate
									
									IEObj.addImmediate32bit(inst,hexInstruction,fw);
								}
								else
								{
									//64 bit ADD immediate
									
									IEObj.addImmediate64bit(inst,hexInstruction,fw);
								}
							}
							else			// S=1
							{
								// ADDS
								if(inst.charAt(0)=='0')
								{
									//32-bit ADDS immediate
									
									IEObj.addsImmediate32bit(inst,hexInstruction,fw);
								}
								else
								{
									//64 bit ADDS immediate
									
									IEObj.addsImmediate64bit(inst,hexInstruction,fw);
								}
							}
						}
						else		//inst.charAt(1)=='1'
						{
							// SUB or SUBS
							
							if(inst.charAt(2)=='0') // checks for S bit
							{
								// SUB
								
								if(inst.charAt(0)=='0')
								{
									//32-bit SUB immediate
									
									IEObj.subImmediate32bit(inst,hexInstruction,fw);
								}
								else
								{
									//64 bit SUB immediate
									
									IEObj.subImmediate64bit(inst,hexInstruction,fw);
								}
							}
							else			// S=1
							{
								// SUBS
								
								if(inst.charAt(0)=='0')
								{
									//32-bit SUBS immediate
									
									IEObj.subsImmediate32bit(inst,hexInstruction,fw);
								}
								else
								{
									//64 bit SUBS immediate
									
									IEObj.subsImmediate64bit(inst,hexInstruction,fw);
								}
							}
						}
					}
				}
				else		//inst.charAt(6)=='1'
				{
					// logical immediate,move wide, (bit field ,extract -> not required)

					
					if(inst.charAt(7)=='0')
					{
						// logical immediate, move wide

						
						if(inst.charAt(8)=='0')
						{
							//logical immediate
							
							if(inst.charAt(1)=='0' && inst.charAt(2)=='0')
							{
								//AND - immediate
								
								if(inst.charAt(0)=='0' && inst.charAt(9)=='0')		// opc
								{
									// 32-bit
									
									IEObj.andImmediate32bit(inst,hexInstruction,fw);
								}
								else
								{
									// 64 - bit
									
									IEObj.andImmediate64bit(inst,hexInstruction,fw);
								}
							}
							if(inst.charAt(1)=='1' && inst.charAt(2)=='1')
							{
								// ANDS - immediate
								
								if(inst.charAt(0)=='0')
								{
									// 32-bit
									
									IEObj.andsImmediate32bit(inst,hexInstruction,fw);
								}
								else
								{
									// 64 - bit
									
									IEObj.andsImmediate64bit(inst,hexInstruction,fw);
								}
							}
							if(inst.charAt(1)=='0' && inst.charAt(2)=='1')
							{
								// ORR Immediate
								int datasize=0;
								if(inst.charAt(0)=='0')
									datasize=32;
								else
									datasize=64;
								
								IEObj.movBitmaskImmediate(inst, hexInstruction, fw, false,datasize);
								
							}
						}
						else		//inst.charAt(8)=='1'
						{
							//Move wide immediate

							
							if(inst.charAt(1)=='0' && inst.charAt(2)=='0')
							{
								// MOVN immediate
								if(inst.charAt(0)=='0')
								{
									//32 - bit MOVN Immediate
									IEObj.movInvertedWideImmediate32bit(inst, hexInstruction, fw);
								}
								else
								{
									// 64- bit MOVN Immediate
									IEObj.movInvertedWideImmediate64bit(inst, hexInstruction, fw);
								}
							}
							
							if(inst.charAt(1)=='1' && inst.charAt(2)=='0')
							{
								// MOVZ immediate
								
								if(inst.charAt(0)=='0')
								{
									//32 - bit MOVZ Immediate
									
									IEObj.movz32bit(inst,hexInstruction,fw);
									
								}
								else
								{
									// 64- bit MOVZ Immediate
									
									IEObj.movz64bit(inst,hexInstruction,fw);
								}
							}
							
							if(inst.charAt(1)=='1' && inst.charAt(2)=='1')
							{
								// MOVK immediate
								
								if(inst.charAt(0)=='0')
								{
									//32 - bit MOVK Immediate
									// not to be implemented
								}
								else
								{
									// 64- bit MOVK Immediate
									// not to be implemented
								}
							}
							
							if(inst.charAt(1)=='0' && inst.charAt(2)=='1')
							{
								// Invalid Instruction
							}
						}

					}
					else		//inst.charAt(7)=='1'
					{
						// Bit field ,Extract 
						if(inst.charAt(8)=='0')
						{
							// Bit Field
							
							if(inst.charAt(1)=='0' && inst.charAt(2)=='0')
							{
								// SBFM also known as ASR - Arithmetic Shift Right
								// Here only ASR is implemented NOT SBFM
								
								if(inst.charAt(0)=='0' && inst.charAt(9)=='0')
								{
									// immediate ASR 32 bit
									IEObj.UBFM(inst, hexInstruction, fw, true, 32);
									
								}
								if(inst.charAt(0)=='1' && inst.charAt(9)=='1')
								{
									// immediate ASR 64 bit
									IEObj.UBFM(inst, hexInstruction, fw, true, 64);
								}
							}
							if(inst.charAt(1)=='1' && inst.charAt(2)=='0')  // doubt
							{
								// UBFM which is also used for LSL/LSR
								
								if(inst.charAt(0)=='0' && inst.charAt(9)=='0')
								{
									// immediate LSL 32 bit
									IEObj.UBFM(inst, hexInstruction, fw, false, 32);
									
								}
								if(inst.charAt(0)=='1' && inst.charAt(9)=='1')
								{
									// immediate LSL 64 bit
									IEObj.UBFM(inst, hexInstruction, fw, false, 64);
								}
								
							}
						}
						else  // inst.charAt(8)=='1'
						{
							// Extract
						}
					}
				}
			}
			else 		//inst.charAt(5)=='1'
			{
				// Branch Instructions
				if(inst.charAt(1)=='0')
				{
					// Unconditional Branch (Immediate)
					// Compare and Branch (Immediate)
					// Test and Branch (Immediate)
					if(inst.charAt(2)=='0')
					{
						// Unconditional Branch (Immediate)
						if(inst.charAt(0)=='0')
						{
							// B Branch
							IEObj.B(inst, hexInstruction, fw);
						}
						else
						{
							// BL Branch
							IEObj.BL(inst, hexInstruction, fw);
						}
					}
					else 		// inst.charAt(2)=='1'
					{
						// Compare and Branch (Immediate)
						// Test and Branch (Immediate)
						if(inst.charAt(6)=='0')
						{
							// Compare and Branch (Immediate)
							if(inst.charAt(7)=='0')
							{
								// CBZ
								if(inst.charAt(0)=='0')
								{
									// 32 bit CBZ
									IEObj.CBZ(inst, hexInstruction, fw, inst.charAt(0));
								}
								else
								{
									// 64 bit CBZ
									IEObj.CBZ(inst, hexInstruction, fw, inst.charAt(0));
								}
							}
							else 		//if(inst.charAt(7)=='1')
							{
								// CBNZ
								if(inst.charAt(0)=='0')
								{
									// 32 bit CBNZ
									IEObj.CBNZ(inst, hexInstruction, fw, inst.charAt(0));
								}
								else
								{
									// 64 bit CBNZ
									IEObj.CBNZ(inst, hexInstruction, fw, inst.charAt(0));
								}
							}
						}
						else		//inst.charAt(6)=='1'
						{
							// Test and Branch (Immediate)
							// Not to be implemented
						}
					}
				}
				else		//inst.charAt(1)=='1'
				{
					// Conditional Branch (Immediate)
					// Exception generation
					// System
					// Unconditional Branch (register)
					
					if(inst.charAt(0)=='0')
					{
						// Conditional Branch (Immediate)
						if(inst.charAt(7)=='0' && inst.charAt(27)=='0')
						{
							// B.cond
							IEObj.bCondition(inst, hexInstruction, fw);
						}
					}
					else		//inst.charAt(0)=='1'
					{
						// Exception generation
						// System
						// Unconditional Branch (register)
						if(inst.charAt(6)=='0')
						{
							// Exception generation
							// System
							
							// Not to be implemented
						}
						else		//inst.charAt(6)=='1'
						{
							// Unconditional Branch (register)
							if(inst.charAt(8)=='0')
							{
								// BR BLR RET
								if(inst.charAt(9)=='0')
								{
									// BR BLR
									if(inst.charAt(10)=='0')
									{
										// BR
										IEObj.BR(inst, hexInstruction, fw);
									}
									else		// inst.charAt(10)=='1'
									{
										// BLR
										IEObj.BLR(inst, hexInstruction, fw);
									}
								}
								else		//inst.charAt(9)=='1'
								{
									// RET
									IEObj.RET(inst, hexInstruction, fw);
								}
								
							}
							else		//inst.charAt(8)=='1'
							{
								// Not to be implemented
							}
						}
					}
					
					
				}
			}
		}
		else		//inst.charAt(4)=='1'
		{
			// load and store instruction, data processing
			// for load and store bit 6 can be anything so skipping that
			// 4th bit is yet to be processed/checked
			if(inst.charAt(6)=='0')
			{
				
				// load and store instruction
				// Please check 4th bit also, not 4th index
				// Please check 6th bit also, not 6th index
				if(inst.charAt(2)=='0')
				{
					// Advanced SIMD -> not to be implemented
					// Load/ Store Exclusive -> Not to be implemented
					// Load Register literal
					if(inst.charAt(3)=='0' && inst.charAt(5)=='0' && inst.charAt(7)=='0')
					{
						// Load Register literal
						if(inst.charAt(0)=='0' && inst.charAt(1)=='0' && inst.charAt(5)=='0')
						{
							// LDR (literal) 32 bit
						}
						if(inst.charAt(0)=='0' && inst.charAt(1)=='0' && inst.charAt(5)=='1')
						{
							// LDR (literal,SIMD&FP) 32 bit
						}
						if(inst.charAt(0)=='0' && inst.charAt(1)=='1' && inst.charAt(5)=='0')
						{
							// LDR (literal) 64 bit
						}
						if(inst.charAt(0)=='0' && inst.charAt(1)=='1' && inst.charAt(5)=='1')
						{
							// LDR (literal,SIMD&FP) 64 bit
						}
						if(inst.charAt(0)=='1' && inst.charAt(1)=='0' && inst.charAt(5)=='0')
						{
							// LDRSW (literal)
						}
						if(inst.charAt(0)=='1' && inst.charAt(1)=='0' && inst.charAt(5)=='1')
						{
							// LDR (literal,SIMD&FP) 128 bit
						}
						IEObj.LDRLiteral(inst);
					}
				}
				else  		//inst.charAt(2)=='1'
				{
					//load and store 3 to 12
					if(inst.charAt(3)=='0')
					{
						// load / store - 3,4,5,6
						if(inst.charAt(7)=='0')
						{
							// 3,4
							if(inst.charAt(8)=='0')
							{
								// 3 
								// not to be implemented
							}
							else
							{
								//4
								// Load/store Register pair (post-indexed)
								if(inst.charAt(0)=='0'&& inst.charAt(1)=='0')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='0')
									{
										//STP 32 bit 
									}
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDP 32 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 32 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 32 bit 
									}
								}
								if(inst.charAt(0)=='0'&& inst.charAt(1)=='1')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDPSW -> not to be implemented
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 64 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 64 bit 
									}
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='0')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='0')
									{
										//STP 64 bit 
									}
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDP 64 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 128 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 128 bit 
									}
								}

								IEObj.STPRegisterPair(inst);
							}
						}
						else  		//inst.charAt(7)=='1'
						{
							// 5,6
							if(inst.charAt(8)=='0')
							{
								// 5 
								// Load/store Register pair (offset)
								IEObj.STPRegisterPair(inst);
								if(inst.charAt(0)=='0'&& inst.charAt(1)=='0')
								{
									
									if(inst.charAt(5)=='0' && inst.charAt(9)=='0')
									{
										//STP 32 bit 
									}
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDP 32 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 32 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 32 bit 
									}
								}
								if(inst.charAt(0)=='0'&& inst.charAt(1)=='1')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDPSW -> not to be implemented
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 64 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 64 bit 
									}
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='0')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='0')
									{
										//STP 64 bit 
									}
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDP 64 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 128 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 128 bit 
									}
								}
							}
							else
							{
								//6
								// Load/store Register pair (pre-indexed)
								IEObj.STPRegisterPair(inst);
								if(inst.charAt(0)=='0'&& inst.charAt(1)=='0')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='0')
									{
										//STP 32 bit 
									}
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDP 32 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 32 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 32 bit 
									}
								}
								if(inst.charAt(0)=='0'&& inst.charAt(1)=='1')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDPSW -> not to be implemented
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 64 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 64 bit 
									}
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='0')
								{
									if(inst.charAt(5)=='0' && inst.charAt(9)=='0')
									{
										//STP 64 bit 
									}
									if(inst.charAt(5)=='0' && inst.charAt(9)=='1')
									{
										//LDP 64 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='0')
									{
										//STP(SIMD&FP) 128 bit 
									}
									if(inst.charAt(5)=='1' && inst.charAt(9)=='1')
									{
										//LDP(SIMD&FP) 128 bit 
									}
								}
							}
						}
					}
					else  		//inst.charAt(3)=='1'
					{
						// load / store - 7,8,9,10,11,12
						if(inst.charAt(7)=='0')
						{
							// 7,8,9,10,11
							if(inst.charAt(10)=='0')
							{
								//7,8,9,10
								if(inst.charAt(20)=='0')
								{
									//7,8
									if(inst.charAt(21)=='0')
									{
										//7
										// not to be implemented
									}
									else
									{
										//8
										// load/store register (immediate post-indexed)
										IEObj.STRImmediate(inst);
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
										{
											// STR (immediate) 32 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
										{
											// LDR (immediate) 32 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
										{
											// STR (immediate) 64 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
										{
											// LDR (immediate) 64 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='1' && inst.charAt(9)=='0')
										{
											// LDRSW (immediate) post-indexed
										}
									}
								}
								else
								{
									// 9,10
									if(inst.charAt(21)=='0')
									{
										//9
										// not to be implemented
									}
									else
									{
										//10
										//load/store register (immediate pre-indexed)
										IEObj.STRImmediate(inst);
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
										{
											// STR (immediate) 32 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
										{
											// LDR (immediate) 32 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
										{
											// STR (immediate) 64 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
										{
											// LDR (immediate) 64 bit
										}
										if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='1' && inst.charAt(9)=='0')
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
								IEObj.STRImmediate(inst);
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
								{
									// STR (register) 32 bit
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
								{
									// LDR (register) 32 bit
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
								{
									// STR (register) 64 bit
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
								{
									// LDR (register) 64 bit
								}
								if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='1' && inst.charAt(9)=='0')
								{
									// LDRSW (register) 
								}
							}
						}
						else		//inst.charAt(7)=='1'
						{
							//12
							// load/store (unsigned immediate)
							IEObj.STRImmediate(inst);
							if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
							{
								// STR (immediate) 32 bit
							}
							if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
							{
								// LDR (immediate) 32 bit
							}
							if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='0')
							{
								// STR (immediate) 64 bit
							}
							if(inst.charAt(0)=='1'&& inst.charAt(1)=='1' && inst.charAt(5)=='0' && inst.charAt(8)=='0' && inst.charAt(9)=='1')
							{
								// LDR (immediate) 64 bit
							}
							if(inst.charAt(0)=='1'&& inst.charAt(1)=='0' && inst.charAt(5)=='0' && inst.charAt(8)=='1' && inst.charAt(9)=='0')
							{
								// LDRSW (immediate) Unsigned offset
							}
						}
					}
				}
				
				
			}
			else				// inst.charAt(6)=='1'
			{
				//Data processing instructions also inst.charAt(5)=='0'
				
				if(inst.charAt(5)=='0' && inst.charAt(6)=='1')
				{
					
					// Data Processing
					if(inst.charAt(3)=='0')
					{
						// Logical (Shift Register)
						// Add/Subtract (Shift Register)
						// Add/Subtract (Extended Register)
						
						if(inst.charAt(7)=='0')
						{
							// Logical (Shift Register)
							
							if(inst.charAt(1)=='0' && inst.charAt(2)=='0' &&inst.charAt(10)=='0')      // 11th bit is N
							{
								// AND (Shift Register)
								
								if(inst.charAt(0)=='0')
								{
									// 32 bit AND (Shift Register)
									
									IEObj.andShiftedRegister32bit(inst,hexInstruction,fw);
								}
								else
								{
									// 64-bit AND (Shift Register)
									
									IEObj.andShiftedRegister64bit(inst,hexInstruction,fw);
								}
							}
							if(inst.charAt(1)=='1' && inst.charAt(2)=='1' &&inst.charAt(10)=='0')      // 11th bit is N
							{
								// ANDS (Shift Register)
								
								if(inst.charAt(0)=='0')
								{
									// 32 bit ANDS (Shift Register)
									
									IEObj.andsShiftRegister32bit(inst,hexInstruction,fw);
								}
								else
								{
									// 64-bit ANDS (Shift Register)
									
									IEObj.addsShiftRegister64bit(inst,hexInstruction,fw);
								}
							}
							if(inst.charAt(1)=='0' && inst.charAt(2)=='1' && inst.charAt(10)=='0')      // 11th bit is N
							{
								// ORR (Shift Register) which is used for MOV shift register
								
								if(inst.charAt(0)=='0')
								{
									// 32 bit MOV (Shift Register)
									
									IEObj.movRegister32bit(inst,hexInstruction,fw);
								}
								else
								{
									// 64-bit MOV (Shift Register)
									
									IEObj.movRegister64bit(inst,hexInstruction,fw);
								}
							}
						}
						else		//inst.charAt(7)=='1'
						{
							// Add/Subtract (Shift Register)
							// Add/Subtract (Extended Register)
							
							// to distinguish between these 2, check bit 11 i.e. index 10
							
							if(inst.charAt(10)=='0')
							{
								// Add/Subtract (Shift Register)
								if(inst.charAt(1)=='0')	// op bit
								{
									// ADD (shifted register)
									// ADDS (shifted register)
									
									if(inst.charAt(2)=='0')  	// S bit
									{
										// ADD (shifted register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit ADD (shifted register)
											
											IEObj.addShiftRegister32bit(inst,hexInstruction,fw);
										}
										else
										{
											// 64-bit ADD (shifted register)
											
											IEObj.addShiftRegister64bit(inst,hexInstruction,fw);
										}
									}
									else	// inst.charAt(2)=='1'  ... S bit
									{
										//ADDS (shifted register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit ADDS (shifted register)
											
											IEObj.addsShiftRegister32bit(inst,hexInstruction,fw);
										}
										else
										{
											// 64-bit ADDS (shifted register)
											IEObj.addShiftRegister64bit(inst,hexInstruction,fw);
										}
									}
									
								}
								else 		//inst.charAt(1)=='1' ... op bit
								{
									// SUB (shifted register)
									// SUBS (shifted register)
									
									if(inst.charAt(2)=='0')  	// S bit
									{
										// SUB (shifted register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit SUB (shifted register)
											IEObj.subShiftRegister32bit(inst,hexInstruction,fw);
										}
										else
										{
											// 64-bit SUB (shifted register)
											
											IEObj.subShiftRegister64bit(inst,hexInstruction,fw);
										}
									}
									else	// inst.charAt(2)=='1'  ... S bit
									{
										//SUBS (shifted register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit SUBS (shifted register)
											IEObj.subsShiftRegister32bit(inst,hexInstruction,fw);
										}
										else
										{
											// 64-bit SUBS (shifted register)
											IEObj.subsShiftRegister64bit(inst,hexInstruction,fw);
										}
									}
									
								}
							}
							else		// inst.charAt(10)=='1'
							{
								// ADD (extended register)
								// ADDS (extended register)
								// SUB (extended register)'
								// SUBS (extended register)
								if(inst.charAt(1)=='0')	// op bit
								{
									// ADD (extended register)
									// ADDS (extended register)
									
									if(inst.charAt(2)=='0')  	// S bit
									{
										// ADD (extended register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit ADD (extended register)
											IEObj.addExtendedRegister32bit(inst);
										}
										else
										{
											// 64-bit ADD (extended register)
											IEObj.addExtendedRegister64bit(inst);
										}
									}
									else	// inst.charAt(2)=='1'  ... S bit
									{
										//ADDS (extended register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit ADDS (extended register)
											IEObj.addExtendedRegister32bit(inst);
										}
										else
										{
											// 64-bit ADDS (extended register)
											IEObj.addExtendedRegister64bit(inst);
										}
									}
									
								}
								else		// inst.charAt(1)=='1' ... op bit
								{
									// SUB (extended register)
									// SUBS (extended register)
									
									if(inst.charAt(2)=='0')  	// S bit
									{
										// SUB (extended register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit SUB (extended register)
											IEObj.subExtendedRegister32bit(inst);
										}
										else
										{
											// 64-bit SUB (extended register)
											IEObj.subExtendedRegister64bit(inst);
										}
									}
									else	// inst.charAt(2)=='1'  ... S bit
									{
										//SUBS (extended register)
										
										if(inst.charAt(0)=='0')
										{
											// 32-bit SUBS (extended register)
											IEObj.subExtendedRegister32bit(inst);
										}
										else
										{
											// 64-bit SUBS (extended register)
											IEObj.subExtendedRegister64bit(inst);
										}
									}
								}
							}
							
							
						}
						
					}
					else		//inst.charAt(3)=='1'
					{
						// Add/Subtract with Carry
						// Conditional Compare (Register)
						// Conditional Compare (Immediate)
						// Conditional Select
						// Data-processing (3 source)
						// Data-processing (2 source)
						// Data-processing (1 source)
						
						if(inst.charAt(7)=='1')
						{
							// Data-processing (3 source)
							//NOT Required
						}
						else		//inst.charAt(7)=='0'
						{
							// Add/Subtract with Carry
							// Conditional Compare (Register)
							// Conditional Compare (Immediate)
							// Conditional Select
							// Data-processing (2 source)
							// Data-processing (1 source)
							
							if(inst.charAt(8)=='1' && inst.charAt(9)=='1')
							{
								// Data-processing (2 source)
								// Data-processing (1 source)
								
								if(inst.charAt(1)=='0')
								{
									// Data-processing (2 source)
									if(inst.charAt(2)=='0' && inst.charAt(16)=='0' && inst.charAt(17)=='0' && inst.charAt(18)=='1' && inst.charAt(19)=='0' && inst.charAt(20)=='1' && inst.charAt(21)=='0')
									{
										// ASRV which is used for ASR (Register)
										if(inst.charAt(0)=='0')
										{
											// 32 bit ASR (Register)
											IEObj.asrRegister32bit(inst, hexInstruction, fw);
										}
										else
										{
											// 64 bit ASR (Register)
											IEObj.asrRegister64bit(inst, hexInstruction, fw);
										}
									}
									if(inst.charAt(2)=='0' && inst.charAt(16)=='0' && inst.charAt(17)=='0' && inst.charAt(18)=='1' && inst.charAt(19)=='0' && inst.charAt(20)=='0' && inst.charAt(21)=='0')
									{
										// LSLV which is used for LSL (Register)
										if(inst.charAt(0)=='0')
										{
											// 32 bit LSL (Register)
											IEObj.lslRegister32bit(inst, hexInstruction, fw);
										}
										else
										{
											// 64 bit LSL (Register)
											IEObj.lslRegister64bit(inst, hexInstruction, fw);
										}
									}
									if(inst.charAt(2)=='0' && inst.charAt(16)=='0' && inst.charAt(17)=='0' && inst.charAt(18)=='1' && inst.charAt(19)=='0' && inst.charAt(20)=='0' && inst.charAt(21)=='1')
									{
										// LSRV which is used for LSR (Register)
										if(inst.charAt(0)=='0')
										{
											// 32 bit LSR (Register)
											IEObj.lsrRegister32bit(inst, hexInstruction, fw);
										}
										else
										{
											// 64 bit LSR (Register)
											IEObj.lsrRegister64bit(inst, hexInstruction, fw);
										}
									}
								}
								else  		//inst.charAt(1)=='1'
								{
									// Data-processing (1 source)
								}
								
							}
							else	//inst.charAt(8)!=1 || inst.charAt(9)!=1
							{
								// Add/Subtract with Carry
								// Conditional Compare (Register)
								// Conditional Compare (Immediate)
								// Conditional Select
								if(inst.charAt(8)=='1')
								{
									// Conditional Select
									// NOT Required
								}
								else		//inst.charAt(8)=='0'
								{
									// Add/Subtract with Carry
									// Conditional Compare (Register)
									// Conditional Compare (Immediate)
									if(inst.charAt(9)=='0')
									{
										// Add/Subtract with Carry
									}
									else	//inst.charAt(9)=='1'
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
				else	//inst.charAt(5)!=0 || inst.charAt(6)!=1
				{
					// not required
				}
			}
		}
		

	}

}

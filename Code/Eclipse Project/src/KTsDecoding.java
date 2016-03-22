
public class KTsDecoding {
	public static void mainst(String args[])
	{
		String inst= "00001011000000100000000000100011";
		
			// We will start with 5th bit		
		
			if(inst.charAt(4)==0)
			{
				// Data Processing - Immediate , Branch Instruction
				if(inst.charAt(5)==0)
				{
					// Data Processing - immediate
					if(inst.charAt(6)==0)
					{
						// PC-rel addressing ,  Add/sub immediate
						if(inst.charAt(7)==0)
						{
							// PC-Re addressing
							if(inst.charAt(0)==0)
							{
								// ADR
								System.out.println("ADR");
							}
							else
							{
								// ADRP
								System.out.println("ADRP");
							}
							
							
						}
						else		//inst.charAt(7)==1
						{
							// Add/sub - immediate
							if(inst.charAt(1)==0) // checks for op bit
							{
								// ADD or ADDS
								
								if(inst.charAt(2)==0) // checks for S bit
								{
									//ADD
									if(inst.charAt(0)==0)
									{
										//32-bit ADD immediate
										System.out.println("32-bit ADD immediate");
									}
									else
									{
										//64 bit ADD immediate
										System.out.println("64 bit ADD immediate");
									}
								}
								else			// S=1
								{
									// ADDS
									if(inst.charAt(0)==0)
									{
										//32-bit ADDS immediate
										System.out.println("32-bit ADDS immediate");
									}
									else
									{
										//64 bit ADDS immediate
										System.out.println("64 bit ADDS immediate");
									}
								}
							}
							else		// op=1
							{
								// SUB or SUBS
								
								if(inst.charAt(2)==0) // checks for S bit
								{
									// SUB
									
									if(inst.charAt(0)==0)
									{
										//32-bit SUB immediate
									}
									else
									{
										//64 bit SUB immediate
									}
								}
								else			// S=1
								{
									// SUBS
									
									if(inst.charAt(0)==0)
									{
										//32-bit SUBS immediate
									}
									else
									{
										//64 bit SUBS immediate
									}
								}
							}
						}
						
					}
					else		// inst.charAt(6)==1
					{
						// logical immediate,move wide, (bit field ,extract -> not required)
						if(inst.charAt(7)==0)
						{
							// logical immediate, move wide
							
							if(inst.charAt(8)==0)
							{
								//logical immediate
								
								if(inst.charAt(1)==0 && inst.charAt(2)==0)
								{
									//AND - immediate
									
									if(inst.charAt(0)==0 && inst.charAt(9)==0)		// opc
									{
										// 32-bit
									}
									else
									{
										// 64 - bit
									}
								}
								else 		// opc is other than 00
								{
									// Not reqd
								}
								
							}
							else		//inst.charAt(8)==1
							{
								//Move wide immediate
								
								if(inst.charAt(1)==0 && inst.charAt(2)==0)
								{
									// MOVN immediate
									if(inst.charAt(0)==0)
									{
										//32 - bit MOVN Immediate
										System.out.println("MOV - MOVN 32 bit");
									}
									else
									{
										// 64- bit MOVN Immediate
										System.out.println("MOV - MOVN 64 bit");
									}
								}
								
								if(inst.charAt(1)==1 && inst.charAt(2)==0)
								{
									// MOVZ immediate
									
									if(inst.charAt(0)==0)
									{
										//32 - bit MOVZ Immediate
									}
									else
									{
										// 64- bit MOVZ Immediate
									}
								}
								
								if(inst.charAt(1)==1 && inst.charAt(2)==1)
								{
									// MOVK immediate
									
									if(inst.charAt(0)==0)
									{
										//32 - bit MOVK Immediate
									}
									else
									{
										// 64- bit MOVK Immediate
									}
								}
								
								if(inst.charAt(1)==0 && inst.charAt(2)==1)
								{
									// Invalid Instruction
								}
							}
						}
						else		// inst.charAt(7)==1
						{
							// Bit field ,Extract -> not reqd
						}
					}
					
				}
				else		//inst.charAt(5)==1
				{
					// Branch Instructions
				}
				
			}
			else			//bit 5 = 1 (inst.charAt(4)==1)
			{
				//load and store instruction, data processing
				// for load and store bit 6 can be anything so skipping that
				// 4th bit is yet to be processed/checked
				if(inst.charAt(6)==0)
				{
					// load and store instruction
					// Please check 4th bit also, not 4th index
					// Please check 6th bit also, not 6th index
				}
				else				// inst.charAt(6)==1
				{
					//Data processing instructions also inst.charAt(5)==0
					
					if(inst.charAt(3)==0 && inst.charAt(5)==0)
					{
						// Logical (Shift Register)
						// Add/Subtract (Shift Register)
						// Add/Subtract (Extended Register)
						
						if(inst.charAt(7)==0)
						{
							// Logical (Shift Register)
							
							if(inst.charAt(1)==0 && inst.charAt(2)==0 &&inst.charAt(10)==0)      // 11th bit is N
							{
								// AND (Shift Register)
								
								if(inst.charAt(0)==0)
								{
									// 32 bit AND (Shift Register)
								}
								else
								{
									// 64-bit AND (Shift Register)
								}
							}
						}
						else		//inst.charAt(7)==1
						{
							// Add/Subtract (Shift Register)
							// Add/Subtract (Extended Register)
							
							// to distinguish between these 2, check bit 11 i.e. index 10
							
							if(inst.charAt(10)==0)
							{
								// Add/Subtract (Shift Register)
								if(inst.charAt(1)==0)	// op bit
								{
									// ADD (shifted register)
									// ADDS (shifted register)
									
									if(inst.charAt(2)==0)  	// S bit
									{
										// ADD (shifted register)
										
										if(inst.charAt(0)==0)
										{
											// 32-bit ADD (shifted register)
											System.out.println("32-bit ADD (shifted register)");
										}
										else
										{
											// 64-bit ADD (shifted register)
										}
									}
									else	// inst.charAt(2)==1  ... S bit
									{
										//ADDS (shifted register)
										
										if(inst.charAt(0)==0)
										{
											// 32-bit ADDS (shifted register)
										}
										else
										{
											// 64-bit ADDS (shifted register)
										}
									}
									
								}
								else 		//inst.charAt(1)==1 ... op bit
								{
									// SUB (shifted register)
									// SUBS (shifted register)
									
									if(inst.charAt(2)==0)  	// S bit
									{
										// SUB (shifted register)
										
										if(inst.charAt(0)==0)
										{
											// 32-bit SUB (shifted register)
										}
										else
										{
											// 64-bit SUB (shifted register)
										}
									}
									else	// inst.charAt(2)==1  ... S bit
									{
										//SUBS (shifted register)
										
										if(inst.charAt(0)==0)
										{
											// 32-bit SUBS (shifted register)
										}
										else
										{
											// 64-bit SUBS (shifted register)
										}
									}
									
								}
							}
							else    // inst.charAt(10)==1
							{
								// Add/Subtract (Extended Register)
								if(inst.charAt(1)==0)		// bit op
								{
									// ADD (Extended Register)
									// ADDS (Extended Register)
									if(inst.charAt(2)==0)	// bit S
									{
										// ADD (Extended Register)
										if(inst.charAt(0)==0)
										{
											// 32-bit ADD (Extended Register)
										}
										else
										{
											// 64-bit ADD (Extended Register)
										}
									}
									else
									{
										// ADDS (Extended Register)
										if(inst.charAt(0)==0)
										{
											// 32-bit ADDS (Extended Register)
										}
										else
										{
											// 64-bit ADDS (Extended Register)
										}
									}
									
								}
								else		//inst.charAt(1)==1   bit op
								{
									// SUB (Extended Register)
									// SUBS (Extended Register
									if(inst.charAt(2)==0)	// bit S
									{
										// SUB (Extended Register)
										if(inst.charAt(0)==0)
										{
											// 32-bit SUB (Extended Register)
										}
										else
										{
											// 64-bit SUB (Extended Register)
										}
									}
									else
									{
										// SUBS (Extended Register)
										if(inst.charAt(0)==0)
										{
											// 32-bit SUBS (Extended Register)
										}
										else
										{
											// 64-bit SUBS (Extended Register)
										}
									}
								
								}
		
							}
						}
						
					}
					else		// inst.charAt(3)==1
					{
						// Add/Subtract with Carry
						// Conditional Compare (Register)
						// Conditional Compare (Immediate)
						// Conditional Select
						// Data-processing (3 source)
						// Data-processing (2 source)
						// Data-processing (1 source)
						
						if(inst.charAt(7)==0)
						{
							if(inst.charAt(8)==0)
							{
								// Add/Subtract with Carry
								// Conditional Compare (Register)
								// Conditional Compare (Immediate)
								
								if(inst.charAt(9)==0)
								{
									// Add/ Subtract with carry
									// NOT reqd
								}
								else		//inst.charAt(9)==1
								{
									// Conditional Compare (Register)
									// Conditional Compare (Immediate)
									
									// NOT Required
								}
							}
							else
							{
								// Not reqd
							}
						}
						else		//inst.charAt(7)==1
						{
							// not reqd
						}
					}
					
				}
				
			}
		}
		
	
}

package cop5556sp17;

import java.util.ArrayList;
import java.util.Collections;

public class Scanner {
	/**
	 * Kind enum
	 */
	
	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}
	public static enum State{
		START,IN_DIGIT,IN_IDENT,AFTER_EQ,AFTER_DIV;
	}
/**
 * Thrown by Scanner when an illegal character is encountered
 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}
	
	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
	public IllegalNumberException(String message){
		super(message);
		}
	}
	

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;
		
		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}
		


	public class Token {
		public final Kind kind;
		public final int pos;  //position in input array
		public final int length;
		@Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }

		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }

		 

		  private Scanner getOuterType() {
		   return Scanner.this;
		  }

		//returns the text of this Token
		public String getText() {
			//TODO IMPLEMENT THIS
			return chars.substring(pos, pos+length);
//			return null;
		}
		
		
		//returns a LinePos object representing the line and column of this Token
		//Edit New Line
		LinePos getLinePos(){
			//TODO IMPLEMENT THIS
			int id1 = Collections.binarySearch(linepos, pos);
			if(id1 < 0){
				id1=-1*(id1+1)-1;
			}
			return new LinePos(id1, pos-linepos.get(id1));
//			return null;
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
			
		}

		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException{
			//TODO IMPLEMENT THIS
			return Integer.parseInt(chars.substring(pos, pos+length));
//			return 0;
		}
		public boolean isKind(Kind k){
			boolean l;
			l = kind.equals(k);
			return l;
		}
		
	}
//Edit New Line
	ArrayList<Integer> linepos;



	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
		//Edit new Line
		linepos = new ArrayList<Integer>();
		linepos.add(0);

	}


	
	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
//		int pos = 0; 
		//TODO IMPLEMENT THIS!!!!
		int pos = 0;
	    int length = chars.length();
	    State state = State.START;
	    int startPos = 0;
	    int ch;
	    while (pos < length) {
//	    	System.out.println(pos);
	        ch = pos < length ? chars.charAt(pos) : -1;
	        switch (state) {
	            case START: {
	            	 pos = skipWhiteSpace(pos);
	            	 if(pos>=length)
	            		 break;
	            	    ch = pos < length ? chars.charAt(pos) : -1;
	            	    startPos = pos;
	            	    //Edit new Line
	            	    //linepos.add(0);

	            	    switch (ch) {
//	            	        case -1: 	{
//	            	        				tokens.add(new Token(Kind.EOF, pos, 0)); 
//	            	        				pos++;
//	            	        			}  break;
	            	        case '&': 	{
											tokens.add(new Token(Kind.AND, startPos, 1));
											pos++;
										} break;
	            	        case '\n': 	{
	    									pos++;
	    									linepos.add(pos);
	    								} break;
	            	        case '+': 	{
	            	        				tokens.add(new Token(Kind.PLUS, startPos, 1));
	            	        				pos++;
	            	        			} break;
	            	        case '*': 	{
	            	        				tokens.add(new Token(Kind.TIMES, startPos, 1));
	            	        				pos++;
	            	        			} break;
	            	        case '=': 	{
	            	        				state = State.AFTER_EQ;
	            	        				pos++;
	            	        			}break;
	            	        case '/': 	{
	            	        				if(pos<length-1 && chars.charAt(pos+1)=='*')
	            	        				{
	            	        					state = State.AFTER_DIV;
	            	        				}
	            	        				else
	            	        				{
	            	        					tokens.add(new Token(Kind.DIV, startPos, 1));
		            	        				pos++;
	            	        				}
	            	        					
	            	        			} break;
	            	        case '%': 	{
	            	        				tokens.add(new Token(Kind.MOD, startPos, 1));
	            	        				pos++;
	            	        			} break;
	            	        case '(': 	{
	            	        				tokens.add(new Token(Kind.LPAREN, startPos, 1));
	            	        				pos++;
	            	        			} break;
	            	        case ')': 	{
	            	        				tokens.add(new Token(Kind.RPAREN, startPos, 1));
	            	        				pos++;
	            	        			} break; 
	            	        case ';': 	{
    	        							tokens.add(new Token(Kind.SEMI, startPos, 1));
    	        							pos++;
    	        						} break; 
	            	        case ',': 	{
    										tokens.add(new Token(Kind.COMMA, startPos, 1));
    										pos++;
    									} break; 
	            	        case '{': 	{
											tokens.add(new Token(Kind.LBRACE, startPos, 1));
											pos++;
										} break;
	            	        case '}': 	{
											tokens.add(new Token(Kind.RBRACE, startPos, 1));
											pos++;
										} break;
	            	        case '0': 	{
	            	        				tokens.add(new Token(Kind.INT_LIT,startPos, 1));
	            	        				pos++;
	            	        			}break;
	            	        case '-':	{
	            	        				
//	            	        				char ch1 = chars.charAt(pos+1);
	            	        				
	            	        				if(pos < length-1 && chars.charAt(pos+1)=='>' )
	            	        				{
	            	        					tokens.add(new Token(Kind.ARROW,startPos, 2));
	            	        					pos=pos+2;
	            	        					state = State.START;
	            	        				}
	            	        				else
	            	        				{
	            	        					tokens.add(new Token(Kind.MINUS,startPos, 1));
	            	        					pos++;
	            	        				}
	            	        					
	            	        			}break;
	            	        case '!':	{
    	        							if(pos < length-1 && chars.charAt(pos+1)=='=' )
    	        							{
    	        								tokens.add(new Token(Kind.NOTEQUAL,startPos, 2));
    	        								pos=pos+2;
    	        								state = State.START;
    	        							}
    	        							else
    	        							{
    	        								tokens.add(new Token(Kind.NOT,startPos, 1));
    	        								pos++;
    	        							}
    	        								
    	        						}break;
	            	        case '|':	{
    										if(pos < length-2 && chars.charAt(pos+1)=='-' && chars.charAt(pos+2)=='>')
    										{
    											tokens.add(new Token(Kind.BARARROW,startPos, 3));
    											pos=pos+3;
    											state = State.START;
    										}
    										else
    										{
    											tokens.add(new Token(Kind.OR,startPos, 1));
    											pos++;
    										}
    											
    									}break;
	            	        case '<':	{
    										if(pos < length-1 && chars.charAt(pos+1)=='-')
    										{
    											tokens.add(new Token(Kind.ASSIGN,startPos, 2));
    											pos+=2;
    											state = State.START;
    										}
    										else if(pos < length-1 && chars.charAt(pos+1)=='=')
    										{
    											tokens.add(new Token(Kind.LE,startPos, 2));
    											pos+=2;
    											state = State.START;
    										}
    										else
    										{
    											tokens.add(new Token(Kind.LT,startPos, 1));
    											pos++;
    										}
    											
    								
    									}break;
	            	        case '>':	{
    										if(pos < length-1 && chars.charAt(pos+1)=='=')
    										{
    											tokens.add(new Token(Kind.GE,startPos, 2));
    											pos+=2;
    											state = State.START;
    										}
    										else
    										{
    											tokens.add(new Token(Kind.GT,startPos, 1));
    											pos++;
    										}
    											
    								
    									}break;
    						
    						
	            	        default: {
	            	            		if (Character.isDigit(ch)) {state = State.IN_DIGIT;pos++;} 
	            	            		else if (Character.isJavaIdentifierStart(ch))
	            	            		{
	            	            			state = State.IN_IDENT;pos++;
	            	            		} 
	            	            		else {throw new IllegalCharException(
	            	                        "illegal char " +ch+" at pos "+pos);
	            	            	}
	            	          }
	            	    } 
	            }  break;
	            case IN_DIGIT: {
	            	if(Character.isDigit(ch))
	            	{
	            		pos++;
	            	}
	            	else
	            	{
	            		try
	            		{
	            			Integer.parseInt(chars.substring(startPos, pos));
	            		}
	            		catch(Exception NumberFormatException)
	            		{
	            			throw new IllegalNumberException("Number overflow/ out of Range"+chars.substring(startPos, pos));
	            		}
	            		tokens.add(new Token(Kind.INT_LIT,startPos, pos-startPos));
	            		state = State.START;
	            	}
	            }  
	            break;
	            case IN_IDENT: {
	            	 if (Character.isJavaIdentifierPart(ch)) {
	                     pos++;
	               } else {
	            	   			int flag=0;
	            	   			for(Kind k:Kind.values())
	            	   			{
//	            	   				System.out.println(k.getText())
	            	   				if(k.getText().equals(chars.substring(startPos,pos)))
	            	   				{
	            	   					tokens.add(new Token(k, startPos, pos-startPos));
	            	   					flag=1;
		            	   				state  = State.START;

	            	   				}
	            	   					
	            	   			}
	            	   			if(flag==0)
	            	   			{
	            	   				tokens.add(new Token(Kind.IDENT, startPos, pos - startPos));
	            	   			}
	            	   			state = State.START;
	               }
	            }  break;
	            case AFTER_EQ: {
	            	ch = chars.charAt(pos);
	            	if(ch=='=')
	            	{
	            		tokens.add(new Token(Kind.EQUAL,startPos, 2));
	            		state=State.START;
	            		pos++;
	            	}
	            	else
	            		throw new IllegalCharException("Illegal Character found at "+pos+"Character "+ch);
	            		
	            }  break;
	            case AFTER_DIV:{
	            	if(pos<length-1 &&((chars.charAt(pos)=='*' && chars.charAt(pos+1)=='/')))
	            	{
	            		state = State.START;
	            		pos=pos+2;
	            	}
	            	else if (ch == '\n'){
	            		pos++;
						linepos.add(pos);
					}
	            	else {
	            		pos++;
	            	}
	            }break;
	            default:  assert false;
	        }// switch(state)
	    } // while
	    switch(state)
	    {
	    	case START:
	    		break;
	    	case IN_DIGIT:	try
    						{
    							Integer.parseInt(chars.substring(startPos, pos));
    						}
    						catch(Exception NumberFormatException)
    						{
    							throw new IllegalNumberException("Number overflow/ out of Range"+chars.substring(startPos, pos));
    						}
	    					tokens.add(new Token(Kind.INT_LIT,startPos, pos-startPos));
	    					break;
	    	case IN_IDENT:	int flag=0;
	    					//System.out.println(chars.substring(startPos,pos));
   							for(Kind k:Kind.values())
   							{
//   							System.out.println(k.getText())
   								if(k.getText().equals(chars.substring(startPos,pos)))
   								{
   									tokens.add(new Token(k, startPos, pos-startPos));
   									flag=1;
   									state  = State.START;

   								}
   					
   							}
   							if(flag==0)
   							{
   								tokens.add(new Token(Kind.IDENT, startPos, pos - startPos));
   							}
   							state = State.START;
   							break;
	    	case AFTER_EQ: 	throw new IllegalCharException("Illegal Character found at "+pos);
//	    					break;
	    }
		tokens.add(new Token(Kind.EOF,pos,0));
		return this;  
	}



	private int skipWhiteSpace(int pos) {
		// TODO Auto-generated method stub
		char ch1;
		if(pos < chars.length())
		{
			ch1  = chars.charAt(pos);
			
			while(pos < chars.length() && chars.charAt(pos)!='\n' && Character.isWhitespace(ch1))
			{
				pos++;
				if(pos < chars.length())
					ch1 = chars.charAt(pos);
			}
		}
		return pos;
	}



	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}
	
	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);		
	}

	

	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		//TODO IMPLEMENT THIS
		return t.getLinePos();
	}


}

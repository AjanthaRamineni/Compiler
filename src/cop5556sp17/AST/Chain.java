package cop5556sp17.AST;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.Type.TypeName;


public abstract class Chain extends Statement {
	private TypeName types;
	public Chain(Token firstToken) {
		super(firstToken);
		types=null;
	}
	public TypeName get_type()
	{
		return types;
	}
	public void set_type(TypeName t)
	{
		types = t;
	}
	
}

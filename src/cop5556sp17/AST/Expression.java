package cop5556sp17.AST;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.Type.TypeName;

public abstract class Expression extends ASTNode {
	
	private Dec decs;
	private TypeName typename;
	protected Expression(Token firstToken) {
		super(firstToken);
	}

	@Override
	abstract public Object visit(ASTVisitor v, Object arg) throws Exception;
	public TypeName get_type()
	{
		return typename;
	}
	public void set_dec(Dec d)
	{
		decs = d;
	}
	public void set_type(TypeName types)
	{
		typename = types;
	}
	public Dec get_dec()
	{
		return decs;
	}
	
}

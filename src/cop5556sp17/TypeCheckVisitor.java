package cop5556sp17;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;
import cop5556sp17.AST.Type.*;

import java.util.ArrayList;
import java.util.List;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.LinePos;
import cop5556sp17.Scanner.Token;
import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.Scanner.Kind.ARROW;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_XLOC;
import static cop5556sp17.Scanner.Kind.KW_YLOC;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;
import static cop5556sp17.Scanner.Kind.*;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String message) {
			super(message);
		}
	}

	SymbolTable symtab = new SymbolTable();

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Chain chain = binaryChain.getE0();
		chain.visit(this, arg);		
		Token token_first = binaryChain.getFirstToken();
		ChainElem chain_elem = binaryChain.getE1();
		chain_elem.visit(this, arg);
		Token token = binaryChain.getArrow();
		if(token.isKind(ARROW))
		{
			if (chain.get_type().equals(URL) && chain_elem.get_type().equals(IMAGE)){
				binaryChain.set_type(IMAGE);
			}
			else if (chain.get_type().equals(FILE) && chain_elem.get_type().equals(IMAGE)){
				binaryChain.set_type(IMAGE);
			}
			
			else if(chain.get_type().equals(FRAME) &&  (chain_elem.getFirstToken().isKind(KW_XLOC)|| chain_elem.getFirstToken().isKind(KW_YLOC)) )
			{
				binaryChain.set_type(INTEGER);
			}
//					if(chain_elem.getFirstToken().isKind(KW_SHOW)||chain_elem.getFirstToken().isKind(KW_HIDE)|| chain_elem.getFirstToken().isKind(KW_MOVE)|| chain_elem.getFirstToken().isKind(KW_XLOC)|| chain_elem.getFirstToken().isKind(KW_YLOC))			
			else if(chain.get_type().equals(FRAME) &&   (chain_elem.getFirstToken().isKind(KW_SHOW)|| chain_elem.getFirstToken().isKind(KW_HIDE)|| chain_elem.getFirstToken().isKind(KW_MOVE)))
			{
//					if(chain_elem.getFirstToken().isKind(KW_SHOW)||chain_elem.getFirstToken().isKind(KW_HIDE)|| chain_elem.getFirstToken().isKind(KW_MOVE)|| chain_elem.getFirstToken().isKind(KW_XLOC)|| chain_elem.getFirstToken().isKind(KW_YLOC))
						binaryChain.set_type(FRAME);
			}
			else if(chain.get_type().equals(IMAGE)  && (chain_elem.getFirstToken().isKind(OP_WIDTH)|| chain_elem.getFirstToken().isKind(OP_HEIGHT)) )
			{
//					if(chain_elem.getFirstToken().isKind(OP_WIDTH)||chain_elem.getFirstToken().isKind(OP_HEIGHT)|| chain_elem.getFirstToken().isKind(KW_SCALE))
						binaryChain.set_type(INTEGER);
			}
			else if(chain.get_type().equals(IMAGE)  && chain_elem.get_type().equals(FRAME))
			{
				binaryChain.set_type(FRAME);
			}
			else if(chain.get_type().equals(IMAGE)  && chain_elem.get_type().equals(FILE)){
					binaryChain.set_type(NONE);
			}
			else if(chain.get_type().equals(IMAGE)  && chain_elem.getFirstToken().isKind(OP_GRAY)|| chain_elem.getFirstToken().isKind(OP_BLUR)|| chain_elem.getFirstToken().isKind(OP_CONVOLVE) )
			{
//					if(chain_elem.getFirstToken().isKind(OP_GRAY)||chain_elem.getFirstToken().isKind(OP_BLUR)|| chain_elem.getFirstToken().isKind(OP_CONVOLVE))
						binaryChain.set_type(IMAGE);
			}
			else if(chain.get_type().equals(IMAGE)  && chain_elem.getFirstToken().isKind(KW_SCALE))
			{
//					if(chain_elem.getFirstToken().isKind(OP_WIDTH)||chain_elem.getFirstToken().isKind(OP_HEIGHT)|| chain_elem.getFirstToken().isKind(KW_SCALE))
						binaryChain.set_type(IMAGE);
			}
			else if(chain.get_type().equals(IMAGE)  && chain_elem.getFirstToken().isKind(IDENT)){
					binaryChain.set_type(IMAGE);
			}
			else
				throw new TypeCheckException("Error!");
		}
		else if(token.isKind(BARARROW))
		{
			if (chain_elem.get_type().equals(TypeName.IMAGE) &&
				(chain_elem.getFirstToken().isKind(OP_GRAY)||chain_elem.getFirstToken().isKind(OP_BLUR)||chain_elem.getFirstToken().isKind( OP_CONVOLVE)))
			{
				binaryChain.set_type(IMAGE);
			}
			else
			{
				throw new TypeCheckException("Error");
			}
		}
		else
		{
			throw new TypeCheckException("Error");
		}

		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expr2 = binaryExpression.getE1();
		Expression expr1 = binaryExpression.getE0();
		expr1.visit(this, arg);
		expr2.visit(this, arg);
		Token op = binaryExpression.getOp();
		if(op.kind.equals(PLUS)||op.kind.equals(MINUS))
		{
			if (expr2.get_type().equals(INTEGER) && expr1.get_type().equals(INTEGER))
				binaryExpression.set_type(INTEGER);
			else if (expr2.get_type().equals(IMAGE) && expr1.get_type().equals(IMAGE))
				binaryExpression.set_type(IMAGE);
			else 
				throw new TypeCheckException("Error");
		}
		else if(op.kind.equals(TIMES))
		{
			if (expr2.get_type().equals(IMAGE) && expr1.get_type().equals(INTEGER))
				binaryExpression.set_type(IMAGE);
			else if (expr2.get_type().equals(INTEGER) && expr1.get_type().equals(INTEGER) )
				binaryExpression.set_type(INTEGER);
			else if (expr2.get_type().equals(INTEGER) && expr1.get_type().equals(IMAGE))
				binaryExpression.set_type(IMAGE);
			else
				throw new TypeCheckException("Error");
		}
		else if(op.kind.equals(DIV))
		{
			if (expr2.get_type().equals(INTEGER) && expr1.get_type().equals(INTEGER))
				binaryExpression.set_type(INTEGER);
			else
				throw new TypeCheckException("Error");
		}
		else if(op.kind.equals(LT)||op.kind.equals(GT)||op.kind.equals(LE)||op.kind.equals(GE))
		{
			if (expr2.get_type().equals(BOOLEAN) && expr1.get_type().equals(BOOLEAN))
				binaryExpression.set_type(BOOLEAN);
			else if (expr2.get_type().equals(INTEGER) && expr1.get_type().equals(INTEGER))
				binaryExpression.set_type(BOOLEAN);
			else
				throw new TypeCheckException("Error");
		}
		else if(op.kind.equals(EQUAL)||op.kind.equals(NOTEQUAL))
		{
			if (expr2.get_type().equals(expr1.get_type()))
				binaryExpression.set_type(BOOLEAN);
			else
				throw new TypeCheckException("Error");
		}
		else 
			throw new TypeCheckException("Error");
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		ArrayList<Dec> decs_stack = block.getDecs();
		ArrayList<Statement> stat_stack = block.getStatements();
		int i,j;
		for(j=0,i=0; i<decs_stack.size() && j<stat_stack.size();)
		{
			if(decs_stack.get(i).firstToken.pos > stat_stack.get(j).firstToken.pos)
			{	
				stat_stack.get(j).visit(this, arg);
				j++;
			}
			else
			{
				decs_stack.get(i).visit(this, arg);
				i++;
			}
		}
		for(int i1=i;i1<decs_stack.size();i1++)
			decs_stack.get(i1).visit(this, arg);
		for(int j1=j;j1<stat_stack.size();j1++)
			stat_stack.get(j1).visit(this, arg);
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		booleanLitExpression.set_type(BOOLEAN);
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if (filterOpChain.getArg().getExprList().size() != 0)
		{
			throw new TypeCheckException("Error");
		}
		filterOpChain.set_type(IMAGE);
		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Tuple tuple = frameOpChain.getArg();
		if (frameOpChain.firstToken.isKind(KW_SHOW)||frameOpChain.firstToken.isKind(KW_HIDE)) 
		{
			if (tuple.getExprList().size() != 0){
				throw new TypeCheckException("Error");
			}
			frameOpChain.set_type(NONE);
		}
		else if (frameOpChain.firstToken.isKind(KW_XLOC)||frameOpChain.firstToken.isKind(KW_YLOC))
		{
			if (tuple.getExprList().size() != 0){
				throw new TypeCheckException("Error");
			}
			frameOpChain.set_type(TypeName.INTEGER);   
		}	
		else if(frameOpChain.firstToken.isKind(KW_MOVE))
		{
			if (tuple.getExprList().size() != 2){
				throw new TypeCheckException("Error");
			}
			frameOpChain.set_type(NONE);
				
		}
		else
			throw new TypeCheckException("Error");

		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = symtab.lookup(identChain.getFirstToken().getText());
		if (dec == null)
			throw new TypeCheckException("Error");
		identChain.set_type(dec.getType());
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = symtab.lookup(identExpression.getFirstToken().getText());
		if (dec == null){
			throw new TypeCheckException("Error");
		}
		identExpression.set_type( dec.getType() );
		identExpression.set_dec( dec);
		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expr = ifStatement.getE();
		expr.visit(this, arg);
		if (!(expr.get_type().equals( TypeName.BOOLEAN )))
		{
			throw new TypeCheckException("Error");
		}
		Block block = ifStatement.getB();
		block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		intLitExpression.set_type(INTEGER);
		return null;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expr = sleepStatement.getE();
		expr.visit(this, arg);
		if (!( expr.get_type().equals( TypeName.INTEGER) ) ){
			throw new TypeCheckException("Error");
		}
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expr = whileStatement.getE();
		expr.visit(this, arg);
		if (! (expr.get_type().equals( TypeName.BOOLEAN ) ) ){
			throw new TypeCheckException("Error");
		}
		Block block = whileStatement.getB();
		block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.insert(declaration.getIdent().getText(), declaration);
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<ParamDec> arg_params = program.getParams();
		for (int i=0; i<arg_params.size(); i++){
			arg_params.get(i).visit(this, arg);
		}
		Block block = program.getB();
		block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		IdentLValue ident = assignStatement.getVar();
		ident.visit(this, arg);
		Expression expr = assignStatement.getE();
		expr.visit(this, arg);
		System.out.println("Expr Type..."+expr.get_type()+"ID Type"+ident.get_type());
		if ( !(expr.get_type().equals( ident.get_type())) ){
			throw new TypeCheckException("Error");
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec dec = symtab.lookup(identX.getText());
		if (dec == null)
			throw new TypeCheckException("Error");
		else {
			identX.set_dec(dec);
		}
		return null;
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if(symtab.insert(paramDec.getIdent().getText(), paramDec))	
			return null;
		else
			throw new TypeCheckException("Error"); 
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		// TODO Auto-generated method stub
		constantExpression.set_type(INTEGER);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		 
		Tuple tuple = imageOpChain.getArg();
//		imageOpChain.set_type(Type.getTypeName(imageOpChain.firstToken));
		if (imageOpChain.firstToken.isKind(OP_WIDTH)||imageOpChain.firstToken.isKind( OP_HEIGHT))
		{
			if (tuple.getExprList().size() != 0){
				throw new TypeCheckException("Error");
			}
			imageOpChain.set_type(TypeName.INTEGER);
		}
		else if (imageOpChain.firstToken.isKind(KW_SCALE)){
			if (tuple.getExprList().size() != 1){
				throw new TypeCheckException("Error");
			}
			imageOpChain.set_type(TypeName.IMAGE);
		}
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		// TODO Auto-generated method stub
		List<Expression> expr = tuple.getExprList();
		Expression e1;
		for (int i=0;i<expr.size();i++){
			expr.get(i).visit(this, arg);
			if(!expr.get(i).get_type().equals(TypeName.INTEGER))
				throw new TypeCheckException("Error");
		}
		return null;
	}
	

}

package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.util.ArrayList;
import java.util.List;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.*;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}
	
	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
	@SuppressWarnings("serial")	
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}

	Scanner scanner;
	Token t;
	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * @return 
	 * 
	 * @throws SyntaxException
	 */
	ASTNode parse() throws SyntaxException {
		ASTNode rt = program();
		matchEOF();
		return rt;
	}

	Expression expression() throws SyntaxException {
		//TODO
		Token first = t;
		Expression expr1,expr2;
		expr1 = term();
		while (t.isKind(LE) || t.isKind(LT) || t.isKind(GE) ||  t.isKind(GT) || t.isKind(EQUAL) || t.isKind(NOTEQUAL)) 
		{
			Kind kind = t.kind;
			Token op=t;
			switch (kind)
			{
				case LE:
				case LT:
				case GE:
				case GT:
				case EQUAL:
				case NOTEQUAL:
					consume();
					break;
			}
			expr2 = term();
			expr1 = new BinaryExpression(first,expr1,op,expr2);
		}
		return expr1;
//		throw new UnimplementedFeatureException();
	}
	AssignmentStatement assign() throws SyntaxException
	{
		Token first = t;
		match(IDENT);
		match(ASSIGN);
		Expression expr = expression();
		return new AssignmentStatement(first,new IdentLValue(first),expr);
	}
	Expression term() throws SyntaxException {
		//TODO
		Token first = t;
		Expression expr1,expr2;
		expr1 = elem();
		while (t.isKind(PLUS) || t.isKind(MINUS) || t.isKind(OR)) 
		{
			Token op = t;
			Kind kind = t.kind;
			switch (kind)
			{
				case PLUS:
				case MINUS:
				case OR:
					consume();
					break;
			}
			expr2 = elem();
			expr1 = new BinaryExpression(first,expr1,op,expr2);
		}
		return expr1;
//		throw new UnimplementedFeatureException();
	}

	Expression elem() throws SyntaxException {
		//TODO
		Token first = t;
		Expression expr1,expr2;
		expr1 = factor();
		while (t.isKind(TIMES) || t.isKind(DIV) || t.isKind(AND) ||  t.isKind(MOD)) 
		{
			Kind kind = t.kind;
			Token op = t;
			switch (kind)
			{
				case TIMES:
				case DIV:
				case AND:
				case MOD:
					consume();
					break;
			}
			expr2 = factor();
			expr1 = new BinaryExpression(first,expr1,op,expr2);
		}
//		throw new UnimplementedFeatureException();
		return expr1;
	}

	Expression factor() throws SyntaxException {
		Token first = t;
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			consume();
			return new IdentExpression(first);
		}
//			break;
		case INT_LIT: {
			consume();
			return new IntLitExpression(first);
		}
//			break;
		case KW_TRUE:
		case KW_FALSE: {
			consume();
			return new BooleanLitExpression(first);
		}
//			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			consume();
			return new ConstantExpression(first);
		}
//			break;
		case LPAREN: {
			consume();
			Expression expr;
			expr = expression();
			match(RPAREN);
			return expr;
		}
//			break;
		default:
			//you will want to provide a more useful error message
			throw new SyntaxException("illegal factor");
		}
	}

	Block block() throws SyntaxException {
		//TODO
		Token first = t;
		ArrayList<Dec> DecList = new ArrayList<>();
		ArrayList<Statement> StatementList = new ArrayList<>();
		match(LBRACE);
//		if(t.isKind(k))
		while(!(t.isKind(RBRACE)))
		{
			if(t.isKind(KW_INTEGER) || t.isKind(KW_BOOLEAN) || t.isKind(KW_IMAGE) || t.isKind(KW_FRAME))
			{
				DecList.add(dec());
			}
			else
			{
				StatementList.add(statement());
			}
		}
		match(RBRACE);
		return new Block(first,DecList,StatementList);
//		throw new UnimplementedFeatureException();
	}

	Program program() throws SyntaxException {
		//TODO
		ArrayList<ParamDec> paramDecList = new ArrayList<>();
		Block block;
		Token first = t;
		
		match(IDENT);
		if(t.isKind(LBRACE))
		{
			block = block();
		}
		else
		{
			paramDecList.add(paramDec());
			while(t.isKind(COMMA))
			{
				
				consume();
				paramDecList.add(paramDec());
			}
			block = block();
		}
		return new Program(first, paramDecList,block);
//		throw new UnimplementedFeatureException();
	}

	ParamDec paramDec() throws SyntaxException {
		
		//TODO
		Token first = t;
//		if(t.isKind(KW_URL) || t.isKind(KW_FILE) || t.isKind(KW_INTEGER) || t.isKind(KW_BOOLEAN))
//		{
//			consume();
//		}
		match(KW_URL, KW_FILE, KW_INTEGER, KW_BOOLEAN);
		Token ident= t;
		match(IDENT);
		return new ParamDec(first,ident);
//		throw new UnimplementedFeatureException();
	}

	Dec dec() throws SyntaxException {
		//TODO
		Token first = t;
//		if(t.isKind(KW_INTEGER) || t.isKind(KW_BOOLEAN) || t.isKind(KW_IMAGE) || t.isKind(KW_FRAME))
//		{
//			consume();
//		}
		match(KW_INTEGER, KW_BOOLEAN, KW_IMAGE, KW_FRAME);
		Token ident = t;
		match(IDENT);
		return new Dec(first,ident);
//		throw new UnimplementedFeatureException();
	}

	Statement statement() throws SyntaxException {
		//TODO
		Token first = t;
		Statement stats;
		Kind kind = t.kind;
		switch (kind) {
			case OP_SLEEP: consume();
//							expression();
							stats = new SleepStatement(first, expression());
							match(SEMI);
							break;
			case KW_WHILE:  stats = whileStatement();
							break;
			case KW_IF:		stats = ifStatement();
							break;
			case IDENT:		if (scanner.peek().isKind(ASSIGN))
							{
								stats = assign();
							}
							else
							{
								stats = chain();
							}
							match(SEMI);
							break;
			default:
					if (t.isKind(OP_BLUR) || t.isKind(OP_GRAY) || t.isKind(OP_CONVOLVE)|| t.isKind(KW_SHOW)|| t.isKind(KW_HIDE)|| t.isKind(KW_MOVE)|| t.isKind(KW_XLOC)||t.isKind( KW_YLOC)|| t.isKind(OP_WIDTH)|| t.isKind(OP_HEIGHT)||t.isKind( KW_SCALE))
					{
						stats = chain();
						match(SEMI);
					}
					else{
						throw new SyntaxException("illegal statement"+t.kind);
					}
		}
//		throw new UnimplementedFeatureException();
		return stats;
	}

	Chain chain() throws SyntaxException {
		//TODO
		Token first = t;
		Chain chain1;
		chain1 = chainElem();
		Token op = t;
		arrowOp();
		ChainElem chain2 = chainElem();
		chain1 = new BinaryChain(first,chain1,op,chain2);
		while(t.isKind(ARROW) || t.isKind(BARARROW))
		{
			op = t;
//			consume();
			match(ARROW, BARARROW);
			chain2 = chainElem();
			chain1 = new BinaryChain(first,chain1,op,chain2);
		}
//		throw new UnimplementedFeatureException();
		return chain1;
	}
	void arrowOp() throws SyntaxException {
		if(t.isKind(ARROW) || t.isKind(BARARROW) )
		{
//			System.out.println("Checking");
			consume();
		}
		else
			throw new SyntaxException("Not an Arrow");
	}
	ChainElem chainElem() throws SyntaxException {
		//TODO
		Token first = t;
		if(t.isKind(IDENT))
		{
			consume();
			return new IdentChain(first);
		}
		else if(t.isKind(OP_BLUR) || t.isKind(OP_GRAY) || t.isKind(OP_CONVOLVE))
		{
			consume();
//			arg();
			return new FilterOpChain(first, arg());
		}
		else if(t.isKind(KW_SHOW) || t.isKind(KW_HIDE) || t.isKind(KW_MOVE) || t.isKind(KW_XLOC) || t.isKind(KW_YLOC))
		{
			consume();
//			arg();
			return new FrameOpChain(first, arg());
		}
		else if(t.isKind(OP_WIDTH) || t.isKind(OP_HEIGHT) || t.isKind(KW_SCALE))
		{
			consume();
//			arg();
			return new ImageOpChain(first, arg());
		}
		else
			throw new SyntaxException("	Missed Brace");
//		throw new UnimplementedFeatureException();
	}
	WhileStatement whileStatement() throws SyntaxException {
		Token first = t;
		Block block;
		Expression expr;
		match(KW_WHILE);
		match(LPAREN);
		expr = expression();
		match(RPAREN);
		block = block();
		return new WhileStatement(first,expr,block);
	}
	IfStatement ifStatement() throws SyntaxException {
		Token first = t;
		Expression expr;
		Block block;
		match(KW_IF);
		match(LPAREN);
		expr = expression();
		match(RPAREN);
		block = block();
		return new IfStatement(first,expr,block);
	}

	Tuple arg() throws SyntaxException {
		//TODO
		List<Expression> args  = new ArrayList<Expression>();
		Token first = t;
		if(t.isKind(LPAREN))
		{
			consume();
			args.add(expression());
			while(t.isKind(COMMA))
			{
				consume();
				args.add(expression());
			}
			match(RPAREN);
		}
//		else
//			first = null;
		return new Tuple(first,args);
//		throw new UnimplementedFeatureException();
	}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.isKind(EOF)) {
			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.isKind(kind)) {
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		for(Kind k:kinds)
		{
			if(t.isKind(k))
				return consume();
		}
		throw new SyntaxException("Compiler found"+t.kind+"Expected"+kinds);
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}

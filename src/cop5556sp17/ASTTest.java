package cop5556sp17;

import static cop5556sp17.Scanner.Kind.PLUS;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.Kind;
import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.*;

public class ASTTest {

	static final boolean doPrint = true;
	static void show(Object s){
		if(doPrint){System.out.println(s);}
	}
	

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}



	@Test
	public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}
	@Test
	public void testParamDecs() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "file abc values";
		Scanner s = new Scanner(input).scan();
		Parser p = new Parser(s);
		ASTNode asts = p.paramDec();
		assertEquals(ParamDec.class, asts.getClass());
		assertEquals(asts instanceof ParamDec, true);
		ParamDec pd = (ParamDec) asts;
		assertEquals(pd.firstToken.kind, Kind.KW_FILE);
		assertEquals(pd.getIdent().kind, Kind.IDENT);
		assertEquals(pd.getIdent().kind, Kind.IDENT);
	}
	
	@Test
	public void testargument1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String i = "((azx*99), 98)";
		Parser p = new Parser(new Scanner(i).scan());
		ASTNode asts = p.arg();
		assertEquals(asts.getClass(), Tuple.class);
		Tuple t = (Tuple) asts;
		assertEquals(t.getExprList().size(), 2);
		assertEquals(t.getFirstToken().kind, Kind.LPAREN);
		assertEquals(t.getExprList().get(0).getClass(), BinaryExpression.class);
		assertEquals(t.getExprList().get(1).getClass(), IntLitExpression.class);
		BinaryExpression be = (BinaryExpression) t.getExprList().get(0);
		assertEquals(be.firstToken.kind, Kind.IDENT);
		assertEquals(be.getFirstToken().equals(t.getFirstToken()), false);
//		assertEquals(be.getFirstToken().hashCode()-t.getFirstToken().hashCode(), 1);
	}
	
	@Test
	public void testifStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String i = "if (99*98==999!=false) {}";
		Parser p = new Parser(new Scanner(i).scan());
		ASTNode ast = p.statement();
		assertEquals(IfStatement.class, ast.getClass());
		IfStatement is = (IfStatement) ast;
		assertEquals(is.firstToken.kind, Kind.KW_IF);
		assertEquals(is.getE().getClass(), BinaryExpression.class);
		
		BinaryExpression sb = (BinaryExpression) is.getE();
		assertEquals(sb.firstToken.kind, Kind.INT_LIT);
		assertEquals(sb.getOp().kind, Kind.NOTEQUAL);
		assertEquals(sb.getE1().getClass(), BooleanLitExpression.class);
		assertEquals(sb.getE0().getClass(), BinaryExpression.class);
		
		BinaryExpression be = (BinaryExpression) sb.getE0();
		assertEquals(be.getFirstToken(), sb.getFirstToken());
		assertEquals(be.getOp().kind, Kind.EQUAL);
		assertEquals(be.getE0().getClass(), BinaryExpression.class);

		BinaryExpression be1 = (BinaryExpression) be.getE0();
		assertEquals(be1.getFirstToken(), sb.getFirstToken());
		assertEquals(be1.getOp().kind, Kind.TIMES);
		
		Block b = is.getB();
		assertEquals(b.getFirstToken().kind, Kind.LBRACE);
		assertEquals(b.getDecs().size(), 0);
		assertEquals(b.getStatements().size(), 0);
	}
	
	@Test
	public void testempty() throws IllegalCharException, IllegalNumberException, SyntaxException {
	String input = "";
	Parser parser = new Parser(new Scanner(input).scan());
	ASTNode asts = parser.arg();
	Tuple t = (Tuple) asts;
	assertEquals(asts.getFirstToken().kind,Kind.EOF);
	assertEquals(t.getExprList().size(), 0);
	}

}

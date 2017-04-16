package cop5556sp17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.Kind;


public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		System.out.println("\n\n\n");
		parser.factor();
	}

	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//System.out.println(scanner);
		Parser parser = new Parser(scanner);
		System.out.println("\n\n\n");
		parser.arg();
	}

	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		/*
		for(int i=0;i<scanner.tokens.size();i++){
			System.out.println(scanner.tokens.get(i).getText());
			System.out.println(scanner.tokens.get(i).kind);
			System.out.println(scanner.tokens.get(i).getLinePos().toString());
		}
		*/
		Parser parser = new Parser(scanner);
		System.out.println("\n\n\n");
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}


	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		System.out.println("\n\n\n");
		parser.parse();
	}
	@Test
	public void testarg() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "123(";
		Parser parser = new Parser(new Scanner(input).scan());
		System.out.println("\n\n\n");
		parser.expression();
		System.out.println(parser.t.getText());
		System.out.println(parser.t.kind);
		System.out.println("\n\n\n");
	}
	
	@Test
	public void testemptyarg() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "()";
		Parser parser = new Parser(new Scanner(input).scan());
		System.out.println("\n\n\n");
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
		System.out.println(parser.t.getText());
		System.out.println(parser.t.kind);
		System.out.println("\n\n\n");
	}
	
	@Test
	public void testExp() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  false*_0/0+false/false<false*_0/0+false/false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.expression();
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(1099%1088+1078*19!=1099%1088+1078*19)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}
	
	@Test
	public void testChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "scale (false*_0/0+false/false<false*_0/0+false/false)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.chainElem();
	}
	@Test
	public void testChainElem1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "$$";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.chainElem();
	}
	
	@Test
	public void testChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "|->";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.chainElem();
		}
	@Test
	public void testWhileStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(false*_0/0+false/false<false*_0/0+false/false){integer $$}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.whileStatement();
        }
	@Test
	public void testifStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(false*_0/0+false/false<false*_0/0+false/false){integer $$}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.ifStatement();
               
        }

	@Test
	public void testArg2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (false*_0/0+false/false<false*_0/0+false/false) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.arg();
	}
	
	@Test
	public void testArg4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "hkbkhkh";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
			parser.arg();
		
	} 

	@Test
	public void testArgerror1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	
	@Test
	public void testParamDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "file $$";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
	}
	
	@Test
	public void testblock() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{integer $$}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.block();
	}	
	
	@Test
	public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog1 integer a, boolean b, url i{}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	
	@Test
	public void testProgramErr() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {a,b}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
		
	}

	@Test
	public void testProgramErr1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {a|->b}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	
	@Test
	public void testProgramErr2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 a|->b}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	
	@Test
	public void testProgramNoErr2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {a|->b;}";
		Parser parser = new Parser(new Scanner(input).scan());
		//thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	
	@Test
	public void testExpression0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "a+b;";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.expression();
		
		input = "a+b-3*4+z/2%r;";
		parser = new Parser(new Scanner(input).scan());
		parser.expression();
	}
	
	@Test
	public void testExpressionErr() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog {while(a,b){}}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testExpressionErr1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog {if(a+b-3,4+z/2%r){}}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	@Test
	public void testExpressionErr2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog {blur -> a; if(alb-3*4++z/2%r){} }";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testAssign() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog { c <- b+c; }";
		Parser parser = new Parser(new Scanner(input).scan());
		//thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testExpr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sampleTest { integer i j<- 10;}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	
//	Test for element
	@Test
	public void testElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc * 6 + scrrenheight / (a)";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.elem();
	}
	
//	Test for term
	@Test
	public void testTerm() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "xyz % screenwidth (a*b)+ true / 6 false - st";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.term();
	}
	
//	Test for chainElement
	@Test
	public void testChainElem10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "atoz92";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
	}
	
	
//	Test for chain
	@Test
	public void testChain1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "height (9) |-> convolve (2,4) -> hide (6,(7)) scale";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
	}
	
	
//	Test dec
	@Test
	public void testDec1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "boolean ab";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.dec();
	}
	
	
//	Test for paramDec
	@Test
	public void testParamDec100() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "url true1";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
	}
	
	
//	Test statement
	@Test
	public void testStmt() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sleep screenwidth * true + (10) != false;";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.statement();
	}
	
//	Test block
	@Test
	public void testBlock() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{integer a10 while ((10)){boolean Boolean}}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.block();
	}
	
//	Test program
	@Test
	public void testProgram() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "ab { integer a10 sleep 10;}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.program();
	}
	

	
	@Test
	public void testProgram10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "x | mos";
		Parser parser = new Parser(new Scanner(input).scan());
		//thrown.expect(Exception.class);
		parser.elem();
	}
//	Test assign
	@Test
	public void testFactor10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(x==2 && y==2)";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.factor();
	}
	
	@Test
	public void paramDecs() throws IllegalCharException, IllegalNumberException, SyntaxException{
	String input = "a url b,integer k,boolean q {integer a boolean b image k sleep a*b+k;}";
	Parser parser = new Parser(new Scanner(input).scan());
	//thrown.expect(Parser.SyntaxException.class);
	parser.parse();
	}
	
	@Test
    public void testProgramNegative1() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testProgramNegative3() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai integer bandi integer{}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testParamDec10() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai url manoj {}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai file manoj {}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai integer manoj {}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai boolean manoj {}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai womt_work manoj {}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testParamDecNegative() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai url {}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testBlock10() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai url manoj {sleep plp ; integer sai boolean bandi sleep plp ; image saibandi frame sai}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai url manoj {sleep plp ;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai url manoj {integer sai}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai url manoj {integer sai";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }


    @Test
    public void testDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {integer sai}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai file manoj {boolean sai image sai frame sai}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai womt_work manoj {boolean sai image sai frame sai hello sai}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testStatement() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {sleep plp ;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while (sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {if (sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {sai -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {sai <- manoj;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {sai -> sai; sai <- manoj; if (sai) {} while (sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testAssign10() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {sai <- manoj;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testChain10() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {sai -> sai -> sai -> sai;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testWhile() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {while(sai) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testIf() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {if(sai) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testArrowOp() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {sai -> sai |-> bandi;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testChainElement() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {gray (sai) -> sai;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {move (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {width (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {sai -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testFilterOp() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {gray (sai) -> sai;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {blur (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {convolve (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {invalid (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testFrameOp() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {show (sai) -> sai;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {hide (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {move (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {xloc (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {yloc (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {yzloc (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testImageOp() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {width (sai) -> sai;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {height (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {scale (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {inv (sai) -> sai;}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        thrown.expect(SyntaxException.class);
        parser.parse();
    }

    @Test
    public void testArg10() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {width -> sai;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {while(sai < manoj) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai < manoj < bandi) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }


    @Test
    public void testTerm10() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {while(sai + manoj) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai + manoj + bandi) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai + manoj + bandi < saibandi > plp) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testElem10() throws SyntaxException, IllegalCharException, IllegalNumberException {
        String input = "sai {while(sai * manoj) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai * manoj / hello + bandi - sai > plp < uf) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }


    @Test
    public void testFactor() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {while(sai * sai) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai * manoj * 5 * true * false * screenwidth * screenheight * (sai)) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testRelOp() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {while(sai < sai) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai < sai <= sai > sai >= sai == sai != sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }


    @Test
    public void testWeakOp() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {while(sai + sai) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai - sai + sai | sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }

    @Test
    public void testStrongOp() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "sai {while(sai * sai) {}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        parser.parse();

        input = "sai {while(sai * sai / sai & sai % sai) {}}";
        scanner = new Scanner(input);
        scanner.scan();
        parser = new Parser(scanner);
        parser.parse();
    }

    @Test
	public void testExpression1000() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a > b <= c";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void testFactor1000() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}

	@Test
	public void testparamDec() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "integer abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.paramDec();
	}

	@Test
	public void testDec1000() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "image abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.dec();
	}

	@Test
	public void testArg1000() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a<-b;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.statement();
	}

	@Test
	public void testArgerror1000() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void testProgram1000() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "abc file b, url b, file c { boolean c1 show (3,5) -> moved |-> c2;}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	@Test
	public void test1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a { frame ab boolean c \n if (c) \n { scale (3,5) -> ab ; \n integer v sleep ( q ** 8); } }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc |-> hide (3,5) -> abcd -> sridhar;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc |-> hide (3,5) -> abcd ->;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test4() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="{ boolean sridhar sleep redd ; frame srii }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test5() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="{ identifier boolean madd }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test6() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="identifier boolean madd { boolean sridhar sleep redd ; frame srii }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test7() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="blur test";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test8() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="hide -> scale -> abd |-> show ->";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test9() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc boolean aaa, integer bbb { ccc <- true; }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}
	@Test
	public void test10() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc {testt <- ( ( (true < false < false == abc != xyz)) ); }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}
	@Test
	public void test11() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}
	@Test
	public void test12() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc {image abc}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}
	@Test
	public void test13() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc {image abc sleep xyz;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}
	@Test
	public void test14() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc {image abc sleep xyz; while(2+3) {}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}
	@Test
	public void test15() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc { boolean abc1 move (2) -> gray |-> 2; }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test16() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="abc { boolean abc1 move (2) -> gray |-> xy; }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test17() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a->b";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.chain();
	}
	@Test
	public void test18() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a->b |-> c";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.chain();
	}
	@Test
	public void test19() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	@Test
	public void test20() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="(a, true, (2))";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	@Test
	public void test21() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a <-";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.assign();
	}
	@Test
	public void test22() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a <- a";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.assign();
	}
	@Test
	public void test23() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="screenheight";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.elem();
	}
	@Test
	public void test24() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="screenheight false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	@Test
	public void test25() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="true & 2 % (a1) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.elem();
	}
	@Test
	public void test26() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="sleep 2; ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}
	@Test
	public void test27() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="if(2) {} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}
	@Test
	public void test28() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="scale (2)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.chainElem();
	}
	@Test
	public void test29() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a1";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.chainElem();
	}
	@Test
	public void test30() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input="a file b, url b, file c { b <- c; }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
//		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
    
	public void testFactor011() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}
	
	@Test
	public void testFactor111() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc,";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//thrown.expect(Parser.SyntaxException.class);
		parser.factor();
	}
	
	@Test
	public void testelem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc*bc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.elem();
	}
	
	@Test
	public void testelem1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.elem();
	}
	
	@Test
	public void testelem2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc**";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.elem();
	}

	@Test
	public void testArg11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        parser.arg();
	}

	@Test
	public void testArgerror11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	
	@Test
	public void testArg1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.arg();
	}
	
	@Test
	public void testArgerror111() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = " (3,3 ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	
	@Test
	public void testStatement11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc->bc;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}
	@Test
	public void testStatement1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc<-3;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}
	@Test
	public void testStatement2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc<-3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}
	@Test
	public void testProgram011() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	@Test
	public void testFactor11() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(32*abc/true+32*abc/true<=32*abc/true+32*abc/true)";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.factor();
	}
	@Test
	public void testFactorError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(32*abc/true+32*abc/true<=32*abc/true+32*abc/true";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.factor();
	}
	@Test
	public void testArg211() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.arg();
	}
	@Test
	public void testArg3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)";
		Parser parser = new Parser(new Scanner(input).scan());
		//thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	@Test
	public void testArg411() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "((32*abc/true+32*abc/true<=32*abc/true+32*abc/true))";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.arg();
	}
	@Test
	public void testArgError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true,)";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}
	@Test
	public void testChainElem11() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
	}
	@Test
	public void testifError1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "if";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.ifStatement();
	}
	@Test
	public void testifError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "if(abs)";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.ifStatement();
	}
	@Test
	public void testchain() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)->show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
	}
	@Test
	public void testchainError() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)->";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.chain();
	}
	@Test
	public void testAssign11() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "abc<-(32*abc/true+32*abc/true<=32*abc/true+32*abc/true)";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.assign();
	}
	@Test
	public void testStatement3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)->show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true);";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.statement();
	}
	@Test
	public void testblock11() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "{integer abc show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)->show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true); boolean b boolean abc show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true)->show(32*abc/true+32*abc/true<=32*abc/true+32*abc/true,32*abc/true+32*abc/true<=32*abc/true+32*abc/true);}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.block();
	}
	
	/**
	 * testing factors
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testFactor2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "test123 123 true false screenwidth screenheight(1)";
		Parser parser = new Parser(new Scanner(input).scan());
//		printTokens(parser.scanner);
		parser.factor();
		parser.factor();
		parser.factor();
		parser.factor();
		parser.factor();
		parser.factor();
		parser.factor();
		thrown.expect(SyntaxException.class);
		parser.factor();
		
	}
	
	/**
	 * test element
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testElem2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "12 * 12 / 9089 % bj & true screenwidth * screenheight";
		Parser parser = new Parser(new Scanner(input).scan());
//		printTokens(parser.scanner);
		parser.elem();
		assertEquals(parser.t.kind, Kind.KW_SCREENWIDTH);
		parser.elem();
		
	}
	
	/**
	 * test term
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testTerm2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "12 + 323 - 323 | (12 * 12 / 9089 % bj & true) screenwidth * screenheight";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.term();
		assertEquals(parser.t.kind, Kind.KW_SCREENWIDTH);
		parser.term();
	}
	
	/**
	 * test expression
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testExpression2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "iijid > ewe < sdjh | eref * sdsd +ssdsd -sdsds > sds / "
				+ "wefjwb & false | true + sdsdd > sdksjdks<sdjskd + sdsfd-"
				+ "(12 + 323 - 323 | (12 * 12 / 9089 % bj & true)) screenwidth * screenheight";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.expression();
		assertEquals(parser.t.kind, Kind.KW_SCREENWIDTH);
		parser.expression();
	}
	
	/**
	 * testing arg
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testArg12() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(iijid > ewe < sdjh | eref * sdsd +ssdsd -sdsds > sds / "
				+ "wefjwb & false | true + sdsdd > sdksjdks<sdjskd + sdsfd-"
				+ "(x12 + 323 - 323 | (12 * 12 / 9089 % bj & true)), "
				+ "screenwidth * screenheight, inp1 < undfd > kjndf + 3 * 0 - hjj%ojio/3334)";
		Parser parser = new Parser(new Scanner(input).scan());
//		printTokens(parser.scanner);
		parser.arg();
		assertEquals(parser.t.kind, Kind.EOF);
	}
	
	/**
	 * 
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testChainElements() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String arg = "(iijid > ewe < sdjh | eref * sdsd +ssdsd -sdsds > sds / "
				+ "wefjwb & false | true + sdsdd > sdksjdks<sdjskd + sdsfd-"
				+ "(x12 + 323 - 323 | (12 * 12 / 9089 % bj & true)), "
				+ "screenwidth * screenheight, inp1 < undfd > kjndf + 3 * 0 - hjj%ojio/3334)";
		String input = "asads" + arg;
		Parser parser = new Parser(new Scanner(input).scan());
//		printTokens(parser.scanner);
		parser.chainElem();
		
		input = "blur" + arg;
		parser = new Parser(new Scanner(input).scan());
//		printTokens(parser.scanner);
		parser.chainElem();
		
		input = "gray" + arg;
		parser = new Parser(new Scanner(input).scan());
//		System.out.println();
//		printTokens(parser.scanner);
		parser.chainElem();
		
		input = "show" + arg;
		parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
		
		input = "hide" + arg;
		parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
		
		input = "xloc" + arg;
		parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
		
		input = "width" + arg;
		parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
		
		input = "height" + arg;
		parser = new Parser(new Scanner(input).scan());
		parser.chainElem();

		input = 12 + arg;
		parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.chainElem();
		
	}
	
	
	
	/**
	 * test assign
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testAssign2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "i <- 32 * 232 + 323 % hj & kajka | 34";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.assign();
		assertEquals(parser.t.kind, Kind.EOF);
	}
	
	/**
	 * test chain
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testChain2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "nkj -> njnj -> blur -> scale(1) -> width (67) -> height (27) -> show -> move(1) ";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
		assertEquals(parser.t.kind, Kind.EOF);
	}
	
	/**
	 * test dec
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testDec2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "image iubijn";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.dec();
		
		input = "frame iubijn";
		parser = new Parser(new Scanner(input).scan());
		parser.dec();
		
		input = "integer iubijn";
		parser = new Parser(new Scanner(input).scan());
		parser.dec();
		
		input = "boolean iubijn";
		parser = new Parser(new Scanner(input).scan());
		parser.dec();
		
		input = "url iubijn";
		parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.dec();
		
	}
	
	/**
	 * test paramDec
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testParamDec2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "integer iubijn";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
		
		input = "boolean iubijn";
		parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
		
		input = "url iubijn";
		parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
		
		input = "file iubijn";
		parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
		
		input = "image iubijn";
		parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.paramDec();
	}
	
	/**
	 * test stmt
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testStmt2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		
		String in = "sleep 1 * 34 * 45 / 324 ;";
		Parser p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);
		
		in = "i <- 10*12/23;";
		p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);
		
		in = "blur(12) -> sds -> blur;";
		p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);
		
		in = "bulb -> bulbasaur;";
		p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);
		
		in = "blub <- bulbasaur;";
		p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);

		in = "bulb -> bulbasaur;";
		p = new Parser(new Scanner(in).scan());
		p.chain();
		assertEquals(p.t.kind, Kind.SEMI);
		
		in = "blub <- bulbasaur;";
		p = new Parser(new Scanner(in).scan());
		p.assign();
		assertEquals(p.t.kind, Kind.SEMI);
		
		in = "if (12*1212/12&jsds*njs) {image ident1 i <- 100; j -> i12;"
				+ "while (12 & 2323) {integer i convolve -> blur(90) -> height(20); pikachu <- 10;}"
				+ " gray(12) -> blur -> xloc;}";
		p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);
		
		in = "while(sel_pokemon * 10) "
				+ "{ bulbasaur <- 10; squirtle <- 10; integer i boolean b "
				+ "if (a < b) {swap -> aParam -> bParam; ass <- 10;}"
				+ "charmander -> scale(10) -> charmiliion -> charizard -> mega_charizard; "
				+ "sleep 10;}";
		p = new Parser(new Scanner(in).scan());
		p.statement();
		assertEquals(p.t.kind, Kind.EOF);
		
	}
	
	/**
	 * test block
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testBlock2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		
		String in = "{}";
		Parser p = new Parser(new Scanner(in).scan());
		p.block();
		
		in = "{<}";
		p = new Parser(new Scanner(in).scan());
		thrown.expect(SyntaxException.class);
		p.block();
		
		in = "{			image a "
				+ "		boolean b "
				+ "		integer i "
				+ "		while(i < n) {"
				+ "			j <- 1; "
				+ "			j -> k -> l -> scale(10, 100);"
				+ "			if (j < m) {"
				+ "				print -> all -> elements;"
				+ "			}"
				+ "		}"
				+ "	}";
		p = new Parser(new Scanner(in).scan());
		p.block();
		
		
		
		
	}
	
	/**
	 * test program
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		
		String in = "solve"
				+ "		integer a,"
				+ "		boolean b,"
				+ "		url u,"
				+ "		file f,"
				+ "		integer a1,"
				+ "		boolean b1"
				+ "		{"
				+ "			integer i"
				+ "			boolean j"
				+ "			while (i == 0) {"
				+ "				i <- 1;"
				+ "				j -> blur(10,10) -> scale(1,10) -> xloc(10,0) -> yloc(0,10);"
				+ "				show(0,0) -> width(10) -> height(10);"
				+ "				if (screenwidth < 20) { newWidth <- 20;}"
				+ "				if (screenheight < 20) { newHheight <- 20;}"
				+ "			}"
				+ "		}";
		
		Parser p = new Parser(new Scanner(in).scan());
		p.program();
	}
	
	/**
	 * testing corner cases in args
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testargs1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String i = "ident2 ";
		Parser p = new Parser(new Scanner(i).scan());
		p.arg();
		assertEquals(Kind.IDENT, p.t.kind);
		
		i = "(if1";
		p = new Parser(new Scanner(i).scan());
		thrown.expect(SyntaxException.class);
		p.parse();
		
	}
	

	/**
	 * testing corner cases in args
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testargs2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String i = "()";
		Parser p = new Parser(new Scanner(i).scan());
		thrown.expect(SyntaxException.class);
		p.arg();
		
	}
	
	/**
	 * testing corner cases in args
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testargs3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String i = "(12)";
		Parser p = new Parser(new Scanner(i).scan());
		p.arg();
		assertEquals(Kind.EOF, p.t.kind);
	}
	
	/**
	 * testing corner cases in args
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 * @throws SyntaxException
	 */
	@Test
	public void testargs4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String i = "(divya == sad + ajantha == cryingBatch * manisha == kid * (sai -> laja*ty))";
		Parser p = new Parser(new Scanner(i).scan());
		thrown.expect(SyntaxException.class);
		p.arg();
		assertEquals(Kind.EOF, p.t.kind);
	}
	
	
	@Test
	public void testAbhinav() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "sta integer a, url b, file f {while(true){x<-x*y*x*20;\nwhile(false){y<-x*y\n*xbc; sleep a > 10 + b * ((10*k)*(ac*false)*screenheight);}}integer x boolean b x -> gray |-> move (a * b, a); }";
		Parser parser = new Parser(new Scanner(input).scan());
		//thrown.expect(SyntaxException.class);
		parser.program();
	}
}
package cop5556sp17;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;
import cop5556sp17.AST.Type.*;
import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.ASTVisitor;
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
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.AST.Type.TypeName.FRAME;
import static cop5556sp17.AST.Type.TypeName.IMAGE;
import static cop5556sp17.AST.Type.TypeName.URL;
import static cop5556sp17.Scanner.Kind.*;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	int slot_stack=1;

	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.getName();
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object",
				new String[] { "java/lang/Runnable" });
		cw.visitSource(sourceFileName, null);

		// generate constructor code
		// get a MethodVisitor
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([Ljava/lang/String;)V", null,
				null);
		mv.visitCode();
		// Create label at start of code
		Label constructorStart = new Label();
		mv.visitLabel(constructorStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering <init>");
		// generate code to call superclass constructor
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		// visit parameter decs to add each as field to the class
		// pass in mv so decs can add their initialization code to the
		// constructor.
		ArrayList<ParamDec> params = program.getParams();
		int i=0;
		for (ParamDec dec : params)
		{
			dec.slot_number = i++;
			cw.visitField(0, dec.getIdent().getText(), dec.getType().getJVMTypeDesc(), null, null);
			dec.visit(this, mv);
		}
		mv.visitInsn(RETURN);
		// create label at end of code
		Label constructorEnd = new Label();
		mv.visitLabel(constructorEnd);
		// finish up by visiting local vars of constructor
		// the fourth and fifth arguments are the region of code where the local
		// variable is defined as represented by the labels we inserted.
		mv.visitLocalVariable("this", classDesc, null, constructorStart, constructorEnd, 0);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, constructorStart, constructorEnd, 1);
		// indicates the max stack size for the method.
		// because we used the COMPUTE_FRAMES parameter in the classwriter
		// constructor, asm
		// will do this for us. The parameters to visitMaxs don't matter, but
		// the method must
		// be called.
		mv.visitMaxs(1, 1);
		// finish up code generation for this method.
		mv.visitEnd();
		// end of constructor

		// create main method which does the following
		// 1. instantiate an instance of the class being generated, passing the
		// String[] with command line arguments
		// 2. invoke the run method.
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null,
				null);
		mv.visitCode();
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering main");
		mv.visitTypeInsn(NEW, className);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "([Ljava/lang/String;)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "run", "()V", false);
		mv.visitInsn(RETURN);
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
		mv.visitLocalVariable("instance", classDesc, null, mainStart, mainEnd, 1);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// create run method
		mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		mv.visitCode();
		Label startRun = new Label();
		mv.visitLabel(startRun);
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering run");
		program.getB().visit(this, null);
		mv.visitInsn(RETURN);
		Label endRun = new Label();
		mv.visitLabel(endRun);
		mv.visitLocalVariable("this", classDesc, null, startRun, endRun, 0);
//TODO  visit the local variables
		mv.visitMaxs(1, 1);
		mv.visitEnd(); // end of run method


		cw.visitEnd();//end of class

		//generate classfile and return it
		return cw.toByteArray();
	}



	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		assignStatement.getE().visit(this, arg);
		CodeGenUtils.genPrint(DEVEL, mv, "\nassignment: " + assignStatement.var.getText() + "=");
		CodeGenUtils.genPrintTOS(GRADE, mv, assignStatement.getE().get_type());
		assignStatement.getVar().visit(this, arg);
		return null;
	}

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		binaryChain.getE0().visit(this, 0);
		if (binaryChain.getArrow().isKind(Kind.BARARROW))
		{
			mv.visitInsn(DUP);
		}
		if (!(binaryChain.getArrow().isKind(Kind.BARARROW)))
		{
			if (binaryChain.getE0().get_type() == FILE)
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromFile",
						PLPRuntimeImageIO.readFromFileDesc, false);
			}
			else if (binaryChain.getE0().get_type() == URL)
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromURL",
						PLPRuntimeImageIO.readFromURLSig, false);

			}
		}

		if (!(binaryChain.getArrow().isKind(Kind.BARARROW)))
		{
			binaryChain.getE1().visit(this, 1);
		}
		else
		{
			binaryChain.getE1().visit(this, 3);
		}

		if (binaryChain.getE1() instanceof IdentChain)
		{
			IdentChain identChain = (IdentChain) binaryChain.getE1();
			if ((identChain.declare instanceof ParamDec))
			{
				if (identChain.declare.getType() == TypeName.INTEGER)
				{
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETFIELD, className, identChain.declare.getIdent().getText(),
							identChain.declare.getType().getJVMTypeDesc());
				}

			}
			else
			{
				if (identChain.declare.getType() == TypeName.INTEGER)
				{
					mv.visitVarInsn(ILOAD, identChain.declare.slot_number);
				}
				else
				{
					mv.visitVarInsn(ALOAD, identChain.declare.slot_number);
				}

			}
		}

		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
      //TODO  Implement this
		TypeName type0,type1;
		Token op;
		type0 = binaryExpression.getE0().get_type();
		type1 = binaryExpression.getE1().get_type();
		op = binaryExpression.getOp();
		//Visiting E0 and E1
		if(op.kind.equals(PLUS))
		{
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if(type0==TypeName.INTEGER &&  type1==TypeName.INTEGER)
			{
				mv.visitInsn(IADD);
			}
			else
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "add", PLPRuntimeImageOps.addSig, false);
			}
		}
		else if(op.kind.equals(MINUS))
		{
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if(type0==TypeName.INTEGER &&  type1==TypeName.INTEGER)
			{
				mv.visitInsn(ISUB);
			}
			else
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "sub", PLPRuntimeImageOps.subSig, false);
			}
		}
		else if(op.kind==(TIMES))
		{
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if ((type0 == TypeName.INTEGER) && (type1 == TypeName.INTEGER))
			{
				mv.visitInsn(IMUL);
			}
			else if ((type0 == TypeName.INTEGER) && (type1 == TypeName.IMAGE))
			{
				mv.visitInsn(SWAP);
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mul", PLPRuntimeImageOps.mulSig, false);
			}
			else
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mul", PLPRuntimeImageOps.mulSig, false);
			}
		}
		else if(op.kind==(DIV))
		{
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if ((type0 == TypeName.INTEGER) && (type1 == TypeName.INTEGER))
			{
				mv.visitInsn(IDIV);
			}
			else
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "div", PLPRuntimeImageOps.divSig, false);
			}
		}
		else if(op.kind==(MOD))
		{
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if ((type0 == TypeName.INTEGER) && (type1 == TypeName.INTEGER))
			{
				mv.visitInsn(IREM);

			}
			else
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mod", PLPRuntimeImageOps.modSig, false);
			}
		}
		switch (op.kind) {
		case OR:
			binaryExpression.getE0().visit(this, arg);
			Label label1_or = new Label();
			mv.visitJumpInsn(IFNE, label1_or);
			binaryExpression.getE1().visit(this, arg);
			mv.visitJumpInsn(IFNE, label1_or);
			mv.visitInsn(ICONST_0);
			Label label2_or = new Label();
			mv.visitJumpInsn(GOTO, label2_or);
			mv.visitLabel(label1_or);
			mv.visitInsn(ICONST_1);
			mv.visitLabel(label2_or);
			break;

		case AND:
			binaryExpression.getE0().visit(this, arg);
			Label label1_and = new Label();
			mv.visitJumpInsn(IFEQ, label1_and);
			binaryExpression.getE1().visit(this, arg);
			mv.visitJumpInsn(IFEQ, label1_and);
			mv.visitInsn(ICONST_1);
			Label label2_and = new Label();
			mv.visitJumpInsn(GOTO, label2_and);
			mv.visitLabel(label1_and);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(label2_and);
			break;

		case LT:
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			Label label1_lt = new Label();
			mv.visitJumpInsn(IF_ICMPGE, label1_lt);
			mv.visitInsn(ICONST_1);
			Label label2_lt = new Label();
			mv.visitJumpInsn(GOTO, label2_lt);
			mv.visitLabel(label1_lt);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(label2_lt);
			break;

		case LE:
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			Label label1_le = new Label();
			mv.visitJumpInsn(IF_ICMPGT, label1_le);
			mv.visitInsn(ICONST_1);
			Label label2_le = new Label();
			mv.visitJumpInsn(GOTO, label2_le);
			mv.visitLabel(label1_le);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(label2_le);

			break;

		case GT:
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			Label label1_gt = new Label();
			mv.visitJumpInsn(IF_ICMPLE, label1_gt);
			mv.visitInsn(ICONST_1);
			Label label2_gt = new Label();
			mv.visitJumpInsn(GOTO, label2_gt);
			mv.visitLabel(label1_gt);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(label2_gt);
			break;

		case GE:
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			Label label1_ge = new Label();
			mv.visitJumpInsn(IF_ICMPLT, label1_ge);
			mv.visitInsn(ICONST_1);
			Label label2_ge = new Label();
			mv.visitJumpInsn(GOTO, label2_ge);
			mv.visitLabel(label1_ge);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(label2_ge);
			break;

		case EQUAL:
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if (type0 == TypeName.INTEGER || type0 == BOOLEAN)
			{
				Label label1_eq = new Label();
				mv.visitJumpInsn(IF_ICMPNE, label1_eq);
				mv.visitInsn(ICONST_1);
				Label label2_eq = new Label();
				mv.visitJumpInsn(GOTO, label2_eq);
				mv.visitLabel(label1_eq);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(label2_eq);
			}
			else
			{
				Label label1_eq = new Label();
				mv.visitJumpInsn(IF_ACMPNE, label1_eq);
				mv.visitInsn(ICONST_1);
				Label label2_eq = new Label();
				mv.visitJumpInsn(GOTO, label2_eq);
				mv.visitLabel(label1_eq);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(label2_eq);
			}

			break;

		case NOTEQUAL:
			binaryExpression.getE0().visit(this, arg);
			binaryExpression.getE1().visit(this, arg);
			if (type0 == TypeName.INTEGER || type0 == BOOLEAN)
			{
				Label label1_neq = new Label();
				mv.visitJumpInsn(IF_ICMPEQ, label1_neq);
				mv.visitInsn(ICONST_1);
				Label label2_neq = new Label();
				mv.visitJumpInsn(GOTO, label2_neq);
				mv.visitLabel(label1_neq);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(label2_neq);
			}
			else
			{
				Label label1_neq = new Label();
				mv.visitJumpInsn(IF_ACMPEQ, label1_neq);
				mv.visitInsn(ICONST_1);
				Label label2_neq = new Label();
				mv.visitJumpInsn(GOTO, label2_neq);
				mv.visitLabel(label1_neq);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(label2_neq);
			}
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		//TODO  Implement this
//		int slot_number=(int)arg;
		Label block_start = new Label();
		mv.visitLineNumber(block.getFirstToken().getLinePos().line, block_start);
		mv.visitLabel(block_start);
		ArrayList<Dec> decs_stack = block.getDecs();
		ArrayList<Statement> stat_stack = block.getStatements();
		int i,j;
		Label start_label = new Label();
		mv.visitLabel(start_label);
		for(j=0,i=0; i<decs_stack.size() && j<stat_stack.size();)
		{
			if(decs_stack.get(i).firstToken.pos > stat_stack.get(j).firstToken.pos)
			{
				stat_stack.get(j).visit(this, mv);

				if (stat_stack.get(j) instanceof BinaryChain)
				{
					mv.visitInsn(POP);
				}
				j++;
			}
			else
			{
				decs_stack.get(i).visit(this, mv);
				i++;
			}
		}
		for(int i1=i;i1<decs_stack.size();i1++)
		{
			decs_stack.get(i1).visit(this, mv);
		}
		for(int j1=j;j1<stat_stack.size();j1++)
		{
			stat_stack.get(j1).visit(this, mv);
			if (stat_stack.get(j) instanceof BinaryChain)
			{
				mv.visitInsn(POP);
			}
		}
		Label end_label = new Label();
		mv.visitLineNumber(0, end_label);
		mv.visitLabel(end_label);
		//Need to check
		List<Dec> list = block.getDecs();
		for (Dec dec : list)
		{
			mv.visitLocalVariable(dec.getIdent().getText(), dec.getType().getJVMTypeDesc(), null, block_start,
			end_label, dec.slot_number);
		}
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		//TODO Implement this
		if(booleanLitExpression.getValue()==true)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);
		return null;
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		ConstantExpression cons_exp = constantExpression;
		Token cons_first_token  = cons_exp.getFirstToken();
		if (cons_first_token.isKind(Kind.KW_SCREENHEIGHT))
		{
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenHeight",
					PLPRuntimeFrame.getScreenHeightSig, false);
		}
		else if (cons_first_token.isKind(Kind.KW_SCREENWIDTH))
		{
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenWidth",
					PLPRuntimeFrame.getScreenWidthSig, false);
		}
		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		//TODO Implement this
		slot_stack++;
		declaration.slot_number = slot_stack;
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		switch (filterOpChain.getFirstToken().kind)
		{
			case OP_BLUR:
				mv.visitInsn(ACONST_NULL);
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "blurOp", PLPRuntimeFilterOps.opSig, false);
			break;

			case OP_GRAY:
			if ((int) arg != 3)
			{
				mv.visitInsn(ACONST_NULL);
			}
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "grayOp", PLPRuntimeFilterOps.opSig, false);
			break;

		case OP_CONVOLVE:
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "convolveOp", PLPRuntimeFilterOps.opSig,false);
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		frameOpChain.getArg().visit(this, arg);
		switch (frameOpChain.getFirstToken().kind) {
		case KW_SHOW:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "showImage", PLPRuntimeFrame.showImageDesc,
					false);
			break;

		case KW_HIDE:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "hideImage", PLPRuntimeFrame.hideImageDesc,
					false);
			break;

		case KW_MOVE:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "moveFrame", PLPRuntimeFrame.moveFrameDesc,
					false);
			break;

		case KW_XLOC:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "getXVal", PLPRuntimeFrame.getXValDesc,
					false);

			break;

		case KW_YLOC:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "getYVal", PLPRuntimeFrame.getYValDesc,
					false);
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		Boolean side = (int) arg == 1;
		if (side)
		{
			if (identChain.declare instanceof ParamDec)
			{
				switch (identChain.declare.getType())
				{
				case INTEGER:
					mv.visitVarInsn(ALOAD, 0);
					mv.visitInsn(SWAP);
					mv.visitFieldInsn(PUTFIELD, className, identChain.declare.getIdent().getText(),
							identChain.declare.getType().getJVMTypeDesc());
					identChain.declare.is_Initialized = true;

					break;

				case FILE:
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETFIELD, className, identChain.declare.getIdent().getText(),
							identChain.declare.getType().getJVMTypeDesc());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write",
							PLPRuntimeImageIO.writeImageDesc, false);
					identChain.declare.is_Initialized = true;
										break;

				default:
					break;
				}
			}
			else
			{
				switch (identChain.declare.getType())
				{
					case INTEGER:
						mv.visitVarInsn(ISTORE, identChain.declare.slot_number);
						identChain.declare.is_Initialized = true;
						break;

					case IMAGE:
						mv.visitVarInsn(ASTORE, identChain.declare.slot_number);
						identChain.declare.is_Initialized = true;
						break;

					case FILE:
						mv.visitVarInsn(ALOAD, identChain.declare.slot_number);
						mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write",
							PLPRuntimeImageIO.writeImageDesc, false);
						identChain.declare.is_Initialized = true;
						break;

					case FRAME:
						if (identChain.declare.is_Initialized)
						{
							mv.visitVarInsn(ALOAD, identChain.declare.slot_number);
							mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "createOrSetFrame",
								PLPRuntimeFrame.createOrSetFrameSig, false);
							mv.visitVarInsn(ASTORE, identChain.declare.slot_number);
						}
						else
						{
							mv.visitInsn(ACONST_NULL);
							mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "createOrSetFrame",
								PLPRuntimeFrame.createOrSetFrameSig, false);
							mv.visitVarInsn(ASTORE, identChain.declare.slot_number);
							identChain.declare.is_Initialized = true;
						}
						break;

					default:
						break;
				}
			}
		}
		else {
			if (identChain.declare instanceof ParamDec) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, className, identChain.declare.getIdent().getText(),
						identChain.declare.getType().getJVMTypeDesc());

			}
			else
			{
				if (identChain.declare.getType() == FRAME)
				{
					if (identChain.declare.is_Initialized)
					{
						mv.visitVarInsn(ALOAD, identChain.declare.slot_number);
					}
					else
					{
						mv.visitInsn(ACONST_NULL);
					}

				}
				else
				{
					mv.visitVarInsn(ALOAD, identChain.declare.slot_number);
				}

			}

		}
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		//TODO Implement this
		if(identExpression.get_dec() instanceof ParamDec)
		{
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(PUTFIELD, className, identExpression.getFirstToken().getText(), identExpression.get_type().getJVMTypeDesc());
		}

		else
		{
			if (identExpression.get_type() == TypeName.BOOLEAN || identExpression.get_type() == TypeName.INTEGER)
			{
				mv.visitVarInsn(ILOAD, identExpression.get_dec().slot_number);
			}
			else
			{
				mv.visitVarInsn(ALOAD, identExpression.get_dec().slot_number);
			}
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		//TODO Implement this
		if(identX.get_dec() instanceof ParamDec)
		{
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(SWAP);
			mv.visitFieldInsn(GETFIELD, className, identX.getFirstToken().getText(), identX.get_type().getJVMTypeDesc());
		}
		else
		{
			if (identX.get_dec().getType() == TypeName.INTEGER || identX.get_dec().getType() == TypeName.BOOLEAN)
			{
				mv.visitVarInsn(ISTORE, identX.get_dec().slot_number);
				identX.get_dec().is_Initialized = true;
			}
			else if (identX.get_dec().getType() == IMAGE)
			{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "copyImage", PLPRuntimeImageOps.copyImageSig, false);
				mv.visitVarInsn(ASTORE, identX.get_dec().slot_number);
				identX.get_dec().is_Initialized = true;
			}
			else
			{
				mv.visitVarInsn(ASTORE, identX.get_dec().slot_number);
				identX.get_dec().is_Initialized = true;
			}
		}
//			mv.visitVarInsn(ILOAD,1);
		return null;

	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		//TODO Implement this
		ifStatement.getE().visit(this, arg);
		Label if_true = new Label();
		mv.visitJumpInsn(IFEQ, if_true);
		ifStatement.getB().visit(this, arg);
		mv.visitLabel(if_true);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		assert false : "not yet implemented";
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		//TODO Implement this
		mv.visitLdcInsn(intLitExpression.value);
		return null;
	}


	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		//TODO Implement this
		//For assignment 5, only needs to handle integers and booleans
		MethodVisitor mv = (MethodVisitor) arg;
		TypeName type_name = paramDec.getType();
		switch (type_name) {
		case BOOLEAN:
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(paramDec.slot_number);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "Z");
			break;

		case INTEGER:
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(paramDec.slot_number);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "I");
			break;

		case FILE:
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/io/File");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(paramDec.slot_number);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "Ljava/io/File;");
			break;

		case URL:

			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(paramDec.slot_number);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "getURL", PLPRuntimeImageIO.getURLSig, false);
			mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), "Ljava/net/URL;");

			break;

		default:
			break;
		}
		return null;

	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		sleepStatement.getE().visit(this, arg);
		mv.visitInsn(I2L);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V", false);
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		List<Expression> exp_list = tuple.getExprList();
		for (Expression exp : exp_list)
		{
			exp.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		//TODO Implement this
		Label check_guard_while = new Label();
		mv.visitJumpInsn(GOTO, check_guard_while);
		Label check_body_while = new Label();
		mv.visitLabel(check_body_while);
		whileStatement.getB().visit(this, arg);
		mv.visitLabel(check_guard_while);
		whileStatement.getE().visit(this, arg);
		mv.visitJumpInsn(IFNE, check_body_while);
		return null;
	}

}

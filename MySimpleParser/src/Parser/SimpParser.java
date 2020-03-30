package Parser;

import AST.ASTNode;
import AST.ASTNodeType;
import AST.SimpleASTNode;
import MyLexer.MySimpleLexer;
import Token.*;

/**
 * 语法解析器
 * 能够解析简单的表达式，声明变量和初始化语句、赋值语句
 * programm -> intDeclare | expressionStatement | assignmentStatement
 * intDeclare -> 'int' Id ( = additive) ';'
 * expressionStatement -> addtive ';'
 * addtive -> multiplicative ( (+ | -) multiplicative)*
 * multiplicative -> primary ( (* | /) primary)*
 * primary -> IntLiteral | Id | (additive)
 */
public class SimpParser {
    /**
     * 解析脚本
     * @param
     * @return
     * @throws Exception
     */
    public static void main(String[] args) {

        SimpParser parser = new SimpParser();
        String script = null;
        ASTNode tree = null;

        try {
            script = "int age = 45+2; age= 20; age+10*2;";
            System.out.println("解析："+script);
            tree = parser.parse(script);
            parser.dumpAST(tree, "");
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        //测试异常语法
        try {
            script = "2+3+;";
            System.out.println("解析："+script);
            tree = parser.parse(script);
            parser.dumpAST(tree, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //测试异常语法
        try {
            script = "2+3*;";
            System.out.println("解析："+script);
            tree = parser.parse(script);
            parser.dumpAST(tree, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public ASTNode parse(String script) throws Exception{
        //创建一个词法分析器
        MySimpleLexer lexer = new MySimpleLexer();
        //建立Token流
        SimpleTokenReader tokens = lexer.tokenize(script);
        SimpleASTNode rootNode = prog(tokens);
        return rootNode;
    }

    /**
     * AST的根节点，解析的入口
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode prog(SimpleTokenReader tokens) throws Exception{
        //建立程序入口节点
        SimpleASTNode node = new SimpleASTNode(ASTNodeType.Programm, "PWC");

        //programm -> intDeclare | expressionStatement | assignmentStatement
        while (tokens.peek() != null){
            //首先匹配是否是是整型变量声明语句
            SimpleASTNode child = inDeclare(tokens);

            if(child == null){
                //如果不是整型变量声明，判断是否是表达式
                child = expressionStatement(tokens);
            }

            if(child == null){
                //如果不是声明也不是表达式，则判断是否是赋值语句
                child = assignmentStatement(tokens);
            }

            if(child != null){
                //只要有一个识别出
                node.addChild(child);
            }else {
                throw new Exception("未知语句！");
            }
        }
        return node;
    }

    /**
     * 表达式语句，即表达式后面跟分号
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode expressionStatement(SimpleTokenReader tokens) throws Exception{
        //获得当前流的位置，便于回溯
        int pos = tokens.getPosition();
        //向下匹配加法
        SimpleASTNode node = additive(tokens);
        //匹配分号
        if(node != null){
            Token token = tokens.peek();
            if(token != null && token.get_TokenType() == TokenType.SemiColon){
                tokens.read();
            }else{
                node = null;
                tokens.setPosition(pos);        //回溯
            }
        }
        //这里不用报错，因为返回null，回到前面prog函数报错。
        return node;
    }

    /**
     * 赋值语句，如 age = 10 * 2;
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode assignmentStatement(SimpleTokenReader tokens) throws Exception{
        SimpleASTNode node = null;
        Token token = tokens.peek(); //预读，看看是不是标识符
        if(token != null && token.get_TokenType() == TokenType.Identifier){
            token = tokens.read();  //读取消耗
            node = new SimpleASTNode(ASTNodeType.AssignmentStmt, token.get_TokenText());
            token = tokens.peek();  //预读，看看是不是等号
            if(token != null && token.get_TokenType() == TokenType.Assignment){
                tokens.read();  //读出，消耗
                SimpleASTNode child = additive(tokens);     //加法表达式匹配
                if(child == null){
                    //出错，等号右边没有正确的表达式
                    throw new Exception("【非法赋值语句】需要一个表达式！");
                }
                else{
                    node.addChild(child);
                    token = tokens.peek();  //读取分号
                    if(token != null && token.get_TokenType() == TokenType.SemiColon){
                        tokens.read();
                    }else {
                        throw new Exception("【非法语句】需要分号！");
                    }
                }
            }
            else{
                tokens.unread();     //回溯一步即可
                node = null;
            }
        }
        return node;
    }

    /**
     * 整型变量声明
     * 声明的时候，可以带等号 + 表达式，
     * 也可以不带等号和表达式
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode inDeclare(SimpleTokenReader tokens) throws Exception{
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if(token != null && token.get_TokenType() == TokenType.INT) {
            token = tokens.read();
            if (tokens.peek().get_TokenType() == TokenType.Identifier) {
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.IntDeclaration, token.get_TokenText());
                token = tokens.peek();
                if (token != null && token.get_TokenType() == TokenType.Assignment) {
                    tokens.read();
                    SimpleASTNode child = additive(tokens);
                    if (child == null) {
                        throw new Exception("【变量声明错误】等号右边需要表达式！");
                    } else {
                        node.addChild(child);
                    }
                }
            } else {
                throw new Exception("【变量声明错误】需要变量名");
            }
            if(node != null){
                token = tokens.peek();
                if(token != null && token.get_TokenType() == TokenType.SemiColon){
                    tokens.read();
                }else{
                    throw new Exception("【变量声明错误】需要分号！");
                }
            }
        }
        return node;
    }

    /**
     * 加法表达式
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode additive(SimpleTokenReader tokens) throws Exception{
        SimpleASTNode child_1 = multiplicative(tokens);
        SimpleASTNode node = child_1;
        if(child_1 != null){
            while(true){
                Token token = tokens.peek();
                if(token != null && (token.get_TokenType() == TokenType.PLUS || token.get_TokenType() == TokenType.MINUS)){
                    token = tokens.read();
                    SimpleASTNode child_2 = multiplicative(tokens);
                    if(child_2 != null){
                        node = new SimpleASTNode(ASTNodeType.Additive, token.get_TokenText());
                        node.addChild(child_1);
                        node.addChild(child_2);
                        child_1 = node;
                    }else {
                        throw new Exception("【加法表达式】需要加号右边部分！");
                    }
                }else{
                    break;
                }
            }
        }
        return node;
    }

    /**
     * 乘法表达式
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode multiplicative(SimpleTokenReader tokens) throws Exception{
        SimpleASTNode child_1 = primary(tokens);
        SimpleASTNode node = child_1;

        while(true){
            Token token = tokens.peek();
            if (token != null && (token.get_TokenType() == TokenType.STAR || token.get_TokenType() == TokenType.SLASH)) {
                tokens.read();
                SimpleASTNode child_2 = primary(tokens);
                if(child_2 != null){
                    node = new SimpleASTNode(ASTNodeType.Multiplicative, token.get_TokenText());
                    node.addChild(child_1);
                    node.addChild(child_2);
                    child_1 = node;
                }
                else{
                    throw new Exception("【乘法表达式】需要乘号右边部分！");
                }
            }else{
                break;
            }
        }
        return node;
    }

    /**
     * 基础表达式
     * 包括标识符、整型变量和括号
     * @param tokens
     * @return
     * @throws Exception
     */
    private SimpleASTNode primary(SimpleTokenReader tokens) throws Exception{
        SimpleASTNode node = null;
        Token token = tokens.peek();
        if(token != null){
            if(token.get_TokenType() == TokenType.IntLiteral){
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.IntLiteral, token.get_TokenText());
            }
            else if(token.get_TokenType() == TokenType.Identifier){
                token = tokens.read();
                node = new SimpleASTNode(ASTNodeType.Identifier, token.get_TokenText());
            }
            else if(token.get_TokenType() == TokenType.LeftParen){
                tokens.read();
                node = additive(tokens);
                if(node != null){
                    token = tokens.peek();
                    if(token != null && token.get_TokenType() == TokenType.RightParen){
                        tokens.read();
                    }else{
                        throw new Exception("【基础表达式】需要右括号！");
                    }
                }else{
                    throw new Exception("【基础表达式】括号中需要表达式！");
                }
            }
        }
        return node;
    }



    /**
     * 打印输出AST的树状结构
     * @param node
     * @param indent 缩进字符，由tab组成，每一级多一个tab
     */
    public void dumpAST(ASTNode node, String indent) {
        System.out.println(indent + node.getType() + " " + node.getText());
        for (ASTNode child : node.getChildren()) {
            dumpAST(child, indent + "\t");
        }
    }
}

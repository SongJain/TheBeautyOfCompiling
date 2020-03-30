package Calculator;

import AST.ASTNode;
import AST.ASTNodeType;
import AST.SimpleASTNode;
import MyLexer.MySimpleLexer;
import Token.SimpleTokenReader;
import Token.Token;
import Token.TokenReader;
import Token.TokenType;

public class SimpleCalculator implements MyCalculator {

    /**
     * 执行脚本
     * @param script
     */
    @Override
    public void evaluate(String script) throws Exception {
        //解析脚本，并生成AST
        ASTNode tree = parse(script);
        //打印AST
        dumpAST(tree,"");
        //计算AST
        evaluate(tree,"");
    }

    /**
     * 解析脚本
     * @param code
     * @return
     */
    @Override
    public ASTNode parse(String code) throws Exception {
        //建立词法分析器
        MySimpleLexer lexer = new MySimpleLexer();
        //得到Token流
        TokenReader tokens = lexer.tokenize(code);
        //得到根节点
        ASTNode rootNode = prog(tokens);
        return rootNode;
    }

    /**
     * 对某个AST节点求值，并打印过程，通过迭代求值
     * @param node
     * @param indent
     * @return
     */
    @Override
    public int evaluate(ASTNode node, String indent) {
        int result = 0;
        System.out.println(indent + "Calculating: " + node.getType());
        switch (node.getType()){
            case Programm:
                //对每个子节点递归求值
                for(ASTNode child : node.getChildren()){
                    result = evaluate(child, indent + "\t");
                }
                break;
            case Additive:
                ASTNode child_1 = node.getChildren().get(0);
                int value_1 = evaluate(child_1, indent + "\t");
                ASTNode child_2 = node.getChildren().get(1);
                int value_2 = evaluate(child_2, indent + "\t");
                if(node.getText().equals("+")){
                    //如果该节点是加法
                    result = value_1 + value_2;
                }else if(node.getText().equals("-")){
                    result = value_1 - value_2;
                }
                break;
            case Multiplicative:
                child_1 = node.getChildren().get(0);
                value_1 = evaluate(child_1, indent + "\t");
                child_2 = node.getChildren().get(1);
                value_2 = evaluate(child_2, indent + "\t");
                if(node.getText().equals("*")){
                    //如果该节点是加法
                    result = value_1 * value_2;
                }else if(node.getText().equals("/")){
                    result = value_1 / value_2;
                }
                break;
            case IntLiteral:
                //整形字面量，直接返回
                result = Integer.valueOf(node.getText()).intValue();
                break;
            default:
        }
        System.out.println(indent + "Result: " + result);
        return result;
    }

    /**
     * 变量声明语句
     * @param tokens
     * @return
     */
    @Override
    public SimpleASTNode intDeclare(SimpleTokenReader tokens) throws Exception {
        SimpleASTNode node = null;
        Token token = tokens.peek();    //预读
        if(token != null && token.get_TokenType() == TokenType.INT){
            //若是INT则继续读
            token = tokens.read();
            if (tokens.peek().get_TokenType() == TokenType.Identifier) { //匹配标识符
                tokens.read();
                //创建当前节点，并把变量名记到AST节点的文本值中
                node = new SimpleASTNode(ASTNodeType.IntDeclaration, token.get_TokenText());
                token = tokens.peek();
                if(token != null && token.get_TokenType() == TokenType.Assignment){
                    //消耗掉等号
                    tokens.read();
                    //匹配一个表达式
                    SimpleASTNode child = additive(tokens);
                    if (child == null) {
                        throw new Exception("【声明错误】：需要表达式");
                    }
                    else{
                        node.addChild(child);
                    }
                }
            }
        } else {
            throw new Exception("【声明错误】：需要变量名称");
        }

        if(node != null){

            token = tokens.peek();
            if (token != null && token.get_TokenType() == TokenType.SemiColon) {
                tokens.read();
            } else {
                throw new Exception("【声明错误】：需要分号");
            }
        }
        return node;
    }

    /**
     * 语法解析：根节点
     * @param tokens
     * @return 返回根节点 root
     */
    private SimpleASTNode prog(TokenReader tokens) throws Exception {
        //创建root节点，标记程序的入口，记作“计算器”
        SimpleASTNode node = new SimpleASTNode(ASTNodeType.Programm, "Calculator");

        //进入迭代，返回子节点
        SimpleASTNode child = additive(tokens);
        if(child != null){
            //若子节点非空，则加入node子节点
            node.addChild(child);
        }
        return node;
    }

    /**
     * 语法解析：加减法表达式
     * @param tokens
     * @return
     */
    private SimpleASTNode additive(TokenReader tokens) throws Exception {
        //先进行乘法解析，乘法优先级高
        SimpleASTNode child_1 = multiplicative(tokens);
        SimpleASTNode node = child_1;

        //预读
        Token token = tokens.peek();
        if(token != null && child_1 != null){
            if(token.get_TokenType() == TokenType.PLUS || token.get_TokenType() == TokenType.MINUS){
                token = tokens.read();
                SimpleASTNode child_2 = additive(tokens);
                if(child_2 != null){
                    //构建子树
                    node = new SimpleASTNode(ASTNodeType.Additive, token.get_TokenText());
                    node.addChild(child_1);
                    node.addChild(child_2);
                }else {
                    throw new Exception("【加法错误】：需要右边部分");
                }
            }
        }
        return node;
    }

    /**
     * 语法解析：乘除法表达式
     * @return
     */
    private SimpleASTNode multiplicative(TokenReader tokens) {
        //先进行基础表达式解析
        SimpleASTNode child_1 = primary(tokens);
        SimpleASTNode node = child_1;
        //预读
        Token token = tokens.peek();
        if(child_1 != null && token != null){
            if(token.get_TokenType() == TokenType.STAR || token.get_TokenType() == TokenType.SLASH){
                //如果获取的是乘法或者除法
                //丢弃
                token = tokens.read();
                //再次向下匹配 基础表达式
                SimpleASTNode child_2 = primary(tokens);
                if(child_2 != null){
                    //说明乘除后面有基础表达式
                    //建立新的父节点，构造一棵乘除法子树
                    node = new SimpleASTNode(ASTNodeType.Multiplicative, token.get_TokenText());
                    node.addChild(child_1);
                    node.addChild(child_2);
                }
            }
        }
        return node;
    }

    /**
     * 语法解析：基础表达式 包括：整形字面量、变量
     * @param tokens
     * @return
     */
    private SimpleASTNode primary(TokenReader tokens) {

        SimpleASTNode node = null;
        //预读一位
        Token token = tokens.peek();
        if(token != null){
            if(token.get_TokenType() == TokenType.IntLiteral){      //如果是整形字面量
                //丢弃已读
                token = tokens.read();
                //创建node节点，添加类型和值
                node = new SimpleASTNode(ASTNodeType.IntLiteral, token.get_TokenText());
            }
            else if(token.get_TokenType() == TokenType.Identifier){      //如果是变量字面量
                //丢弃已读
                token = tokens.read();
                //创建node节点，添加类型和值
                node = new SimpleASTNode(ASTNodeType.Identifier, token.get_TokenText());
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

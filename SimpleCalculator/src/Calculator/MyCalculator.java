package Calculator;

import AST.*;
import MyLexer.SimpleLexer;
import Token.*;

import java.io.IOException;

public interface MyCalculator {
    /**
     * 执行脚本，打印输出AST和求值过程
     * @param script
     */
    public void evaluate(String script) throws Exception;

    /**
     * 解析脚本，并返回根节点
     * @return
     */
    public ASTNode parse(String code) throws Exception;

    /**
     * 对某个节点求值，并打印求值过程
     * @param node
     * @param indent
     * @return
     */
    public int evaluate(ASTNode node, String indent);

    /**
     * 变量声明语句
     * @param tokens
     * @return
     */
    SimpleASTNode intDeclare(SimpleTokenReader tokens) throws Exception;

}

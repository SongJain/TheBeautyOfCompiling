import AST.SimpleASTNode;
import Calculator.SimpleCalculator;
import MyLexer.MySimpleLexer;
import MyLexer.SimpleLexer;
import Token.SimpleTokenReader;
import Token.TokenReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();

        //测试声明变量
        String str1 = "int age = b + 5;";
        System.out.println("解析变量声明语句: " + str1);
        MySimpleLexer mySimpleLexer = new MySimpleLexer();
        SimpleTokenReader tokens = mySimpleLexer.tokenize(str1);
        try {
            SimpleASTNode node = calculator.intDeclare(tokens);
            calculator.dumpAST(node,"");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        //测试表达式
        String script = "2+3*5";
        System.out.println("\n计算: " + script + "，看上去一切正常。");
        calculator.evaluate(script);


        script = "2+3+4";
        System.out.println("\n计算: " + script + "，结合性出现错误。");
        calculator.evaluate(script);
    }
}

<<<<<<< HEAD
package SimpleCalculator;
import ASTInterface.ASTNode;
import SimpleLexer.SimpleLexer;
import TokenInterface.*;

public class Main {

    public static void main(String arg[]){
        SimpleCalculator calculator = new SimpleCalculator();

        //测试变量声明语句的解析
        String script = "int a = b+3;";
        System.out.println("解析变量声明语句: " + script);
        SimpleLexer lexer = new SimpleLexer();
        //解析字符串，形成一个TokenReader
        TokenReader tokens = lexer.tokenize(script);
        try {
            ASTNode node = calculator.intDeclare(tokens);
            calculator.dumpAST(node,"");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        //测试表达式
        script = "2+3*5";
        System.out.println("\n计算: " + script + "，看上去一切正常。");
        calculator.evaluate(script);

        //测试语法错误
        script = "2+";
        System.out.println("\n: " + script + "，应该有语法错误。");
        calculator.evaluate(script);

    }
}
=======
package SimpleCalculator;
import ASTInterface.ASTNode;
import SimpleLexer.SimpleLexer;
import TokenInterface.*;

public class Main {

    public static void main(String arg[]){
        SimpleCalculator calculator = new SimpleCalculator();

        //测试变量声明语句的解析
        String script = "int a = b+3;";
        System.out.println("解析变量声明语句: " + script);
        SimpleLexer lexer = new SimpleLexer();
        //解析字符串，形成一个TokenReader
        TokenReader tokens = lexer.tokenize(script);
        try {
            ASTNode node = calculator.intDeclare(tokens);
            calculator.dumpAST(node,"");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        //测试表达式
        script = "2+3*5";
        System.out.println("\n计算: " + script + "，看上去一切正常。");
        calculator.evaluate(script);

        //测试语法错误
        script = "2+";
        System.out.println("\n: " + script + "，应该有语法错误。");
        calculator.evaluate(script);

    }
}
>>>>>>> be99597c1973833c16ad9abe7c13f1e62143c4b6

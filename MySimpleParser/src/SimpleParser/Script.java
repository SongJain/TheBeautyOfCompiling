package SimpleParser;

import AST.*;
import Parser.SimpParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.HashMap;

/**
 * 脚本解释器
 * 运行脚本：
 * 在命令行下，键入：java SimpleScript
 * 则进入一个REPL界面。你可以依次敲入命令。比如：
 * > 2+3;
 * > int age = 10;
 * > int b;
 * > b = 10*2;
 * > age = age + b;
 * > exit();  //退出REPL界面。
 *
 * 还可以使用一个参数 -v，让每次执行脚本的时候，都输出AST和整个计算过程。
 */
public class Script {

    //HashMap用于存储变量值
    private HashMap<String, Integer> variables = new HashMap<String, Integer>();
    //verbose用于判断是否打印AST树
    private static boolean verbose = false;

    /**
     * 实现简单的REPL
     * @param args
     */
    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("-v")){
            //加了 -v 参数，需要打印AST
            verbose = true;
            System.out.println("verbose mode");
        }
        System.out.println("见见的脚本编译器！");

        //语法解析器
        SimpParser parser = new SimpParser();
        //脚本解析器
        Script script = new Script();
        //输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String scriptText = "";
        System.out.print("\n>");    //提示符

        while (true) {
            try {
                String line = reader.readLine().trim();
                if (line.equals("exit();")) {
                    System.out.println("再见！");
                    break;
                }
                scriptText += line + "\n";
                if (line.endsWith(";")) {
                    ASTNode tree = parser.parse(scriptText);
                    if (verbose) {
                        parser.dumpAST(tree, "");
                    }

                    script.evaluate(tree, "");

                    System.out.print("\n>");   //提示符

                    scriptText = "";
                }

            } catch (Exception e) {
                // e.printStackTrace();

                System.out.println(e.getLocalizedMessage());
                System.out.print("\n>");   //提示符
                scriptText = "";
            }
        }
    }


    /**
     * 遍历AST，计算结果
     * @param node
     * @param indent
     * @return
     */
    private Integer evaluate(ASTNode node, String indent) throws Exception {

        Integer result = null;
        if (verbose) {
            System.out.println(indent + "Calculating: " + node.getType());
        }

        //根据当前节点的类型去选择处理方法
        switch (node.getType()) {
            case Programm:
                for (ASTNode child : node.getChildren()) {
                    result = evaluate(child, indent);
                }
                break;
            case Additive:
                ASTNode child1 = node.getChildren().get(0);
                Integer value1 = evaluate(child1, indent + "\t");
                ASTNode child2 = node.getChildren().get(1);
                Integer value2 = evaluate(child2, indent + "\t");
                if (node.getText().equals("+")) {
                    result = value1 + value2;
                } else {
                    result = value1 - value2;
                }
                break;
            case Multiplicative:
                child1 = node.getChildren().get(0);
                value1 = evaluate(child1, indent + "\t");
                child2 = node.getChildren().get(1);
                value2 = evaluate(child2, indent + "\t");
                if (node.getText().equals("*")) {
                    result = value1 * value2;
                } else {
                    result = value1 / value2;
                }
                break;
            case IntLiteral:
                result = Integer.valueOf(node.getText()).intValue();
                break;
            case Identifier:
                String varName = node.getText();
                if (variables.containsKey(varName)) {
                    Integer value = variables.get(varName);
                    if (value != null) {
                        result = value.intValue();
                    } else {
                        throw new Exception("【变量： " + varName + "】未初始化！");
                    }
                }
                else{
                    throw new Exception("【未知变量】： " + varName);
                }
                break;
            case AssignmentStmt:
                varName = node.getText();
                if (!variables.containsKey(varName)){
                    throw new Exception("【未知变量】： " + varName);
                }   //接着执行下面的代码
            case IntDeclaration:
                varName = node.getText();
                Integer varValue = null;
                if (node.getChildren().size() > 0) {
                    ASTNode child = node.getChildren().get(0);
                    result = evaluate(child, indent + "\t");
                    varValue = Integer.valueOf(result);
                }
                variables.put(varName, varValue);
                break;

            default:
        }
        if(verbose){
            System.out.println(indent + "Result：" + result);
        }else if(indent.equals("")){
            //顶层语句
            if(node.getType() == ASTNodeType.IntDeclaration || node.getType() == ASTNodeType.AssignmentStmt){
                System.out.println(node.getText() + "： " + result);
            }else if (node.getType() != ASTNodeType.Programm){
                System.out.println(result);
            }
        }
        return result;
    }
}

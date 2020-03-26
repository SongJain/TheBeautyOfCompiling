import MyLexer.MySimpleLexer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String str1 = "int age = 45";
        MySimpleLexer simpleLexer = new MySimpleLexer();
        System.out.println("输入："+ str1);
        simpleLexer.parse(str1);
        simpleLexer.Print();

        String str2 = "age >= 45";
        System.out.println("\n输入："+ str2);
        simpleLexer.parse(str2);
        simpleLexer.Print();
    }
}

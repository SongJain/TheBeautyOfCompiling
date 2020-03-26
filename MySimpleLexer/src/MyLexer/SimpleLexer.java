<<<<<<< HEAD
package MyLexer;

import Token.*;

import java.io.IOException;

public interface SimpleLexer {

    /**
     * SimpleToken是Token的实体类
     */
     final class SimpleToken implements Token {
        //Type
        TokenType type;
        //Text
        String  text;


        @Override
        public TokenType get_TokenType(){
            return this.type;
        }

        @Override
        public String get_TokenText(){
            return this.text;
        }
    }

    /**
     * 有限自动机的状态
     */
     enum DfaState{
        Initial,        //初始状态
        Id,             //读入字符
        GE,             //大于等于号状态
        GT,             //大于号状态
        IntLiteral      //数字字面量状态
    }


    //解析语句的函数
    void parse(String s) throws IOException;

    //状态初始化
    DfaState InitToken(char ch);

    //判断是否是字母
    boolean isAlpha(int ch);

    //判断是否是数字
    boolean isDigit(int ch);

    //判断是否是空白字符
    boolean isBlank(int ch);

    //打印结果
    void Print();
}
=======
package MyLexer;

import Token.*;

import java.io.IOException;

public interface SimpleLexer {

    /**
     * SimpleToken是Token的实体类
     */
     final class SimpleToken implements Token {
        //Type
        TokenType type;
        //Text
        String  text;


        @Override
        public TokenType get_TokenType(){
            return this.type;
        }

        @Override
        public String get_TokenText(){
            return this.text;
        }
    }

    /**
     * 有限自动机的状态
     */
     enum DfaState{
        Initial,        //初始状态
        Id,             //读入字符
        GE,             //大于等于号状态
        GT,             //大于号状态
        IntLiteral      //数字字面量状态
    }


    //解析语句的函数
    void parse(String s) throws IOException;

    //状态初始化
    DfaState InitToken(char ch);

    //判断是否是字母
    boolean isAlpha(int ch);

    //判断是否是数字
    boolean isDigit(int ch);

    //判断是否是空白字符
    boolean isBlank(int ch);

    //打印结果
    void Print();
}
>>>>>>> be99597c1973833c16ad9abe7c13f1e62143c4b6

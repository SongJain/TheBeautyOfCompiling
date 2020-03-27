package MyLexer;

import Token.*;


import java.io.IOException;
import java.util.List;

public interface SimpleLexer {

    /**
     * 有限自动机的状态
     */
     enum DfaState{
        Initial,        //初始状态
        Id,             //读入字符
        GE,             //大于等于号状态
        GT,             //大于号状态
        IntLiteral,     //数字字面量状态
        PLUS,           //+
        MINUS,          //-
        STAR,           //*
        SLASH,          ///
        SemiColon,      //;
        Id_int1,        //int
        Id_int2,
        Id_int3,
        Assignment
    }


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

    public SimpleTokenReader tokenize(String code) throws IOException;
}

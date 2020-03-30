package Token;

/**
 * Token的类型
 */
public enum TokenType {

    /**
     * 关键字
     */
    INT,
    If,
    Else,
    /**
     * 操作符号
     */
    STAR,           //乘法
    PLUS,           //加法
    SLASH,          //除法
    MINUS,          //减法

    /**
     * 比较符号
     */
    GE,     // >=
    GT,     // >
    EQ,     // ==
    LE,     // <=
    LT,     // <
    LeftParen, // (
    RightParen,// )

    StringLiteral,   //字符串字面量
    Identifier,     //标识符
    IntLiteral,     //整形字面量
    Assignment,     //等号
    SemiColon       //分号
}

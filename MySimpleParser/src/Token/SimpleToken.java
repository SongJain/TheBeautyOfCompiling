package Token;

/**
 * SimpleToken是Token的实体类
 */
public final class SimpleToken implements Token {
    //Type
    public TokenType type;
    //Text
    public String  text;


    @Override
    public TokenType get_TokenType(){
        return this.type;
    }

    @Override
    public String get_TokenText(){
        return this.text;
    }
}
package Token;

import java.util.List;

public class SimpleTokenReader implements TokenReader{
    //封装
    List<Token> tokens = null;
    int pos = 0;

    //构造函数
    public SimpleTokenReader(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public Token read() {
        if(pos < tokens.size()){
            return tokens.get(pos++);
        }
        return null;
    }

    @Override
    public Token peek() {
        if(pos < tokens.size()){
            return tokens.get(pos);
        }
        return null;
    }

    @Override
    public void unread() {
        if (pos > 0) {
            pos--;
        }
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Override
    public void setPosition(int position) {
        if (position >= 0 && position < tokens.size()){
            pos = position;
        }
    }
}

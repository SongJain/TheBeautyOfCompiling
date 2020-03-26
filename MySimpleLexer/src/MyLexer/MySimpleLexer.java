package MyLexer;
import Token.*;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MySimpleLexer implements SimpleLexer{
    //临时保存Token的文本
    StringBuffer temp_tokenText = null;
    //保存读取的Token
    private List<Token> tokens = null;
    //记录正在解析的Token
    private SimpleToken temp_token = null;

    /**
     * 解析字符串形成Token
     */
    @Override
    public void parse(String s) throws IOException {

        //方便遍历字符
        CharArrayReader reader = new CharArrayReader(s.toCharArray());

        //初始化
        tokens = new ArrayList<Token>();
        temp_token = new SimpleToken();
        temp_tokenText = new StringBuffer();
        DfaState state = DfaState.Initial;

        //遍历变量
        int ich = 0;
        char ch = 0;

        //遍历字符串处理
        while((ich = reader.read()) != -1){
            ch = (char) ich;        //获取每个字符
            switch (state){         //对上一个状态进行讨论
                case Initial:       //如果上一个是初始化状态，则继续对ch初始化
                    state = InitToken(ch);
                    break;
                case Id:            //如果上一个是Id状态，则需要判断ch是否能维持Id状态
                    if(isAlpha(ch) || isDigit(ch)){
                        temp_tokenText.append(ch);
                    }else{
                        state = InitToken(ch);
                    }
                    break;
                case IntLiteral:
                    if(isDigit(ch)){
                        temp_tokenText.append(ch);
                    }else{
                        state = InitToken(ch);
                    }
                    break;
                case GT:
                    if(ch == '='){
                        state = DfaState.GE;
                        temp_token.type = TokenType.GE;
                        temp_tokenText.append(ch);
                    }else{
                        state = InitToken(ch);
                    }
                    break;
                case GE:
                    state = InitToken(ch);
                    break;
                default:
            }
        }
        //最后一个token也保存好
        if(temp_tokenText.length() > 0){
            InitToken(ch);
        }
    }

    /**
     * 初始化有限自动机状态
     * 每当记录完一个Token，都需要初始化状态
     * @param ch 当前读到的字符
     * @return newState 返回读到第一个字符后所确定的状态机的状态
     */
    @Override
    public DfaState InitToken(char ch) {
        if(temp_tokenText.length() > 0){
            //如果有值，需要创建新的Token并且保存到List中
            temp_token.text = temp_tokenText.toString();
            tokens.add(temp_token);

            //重新初始化temp_Token
            temp_token = new SimpleToken();
            //重新初始化temp_TokenText
            temp_tokenText = new StringBuffer();
        }
        //自动机状态初始化
        DfaState newState = DfaState.Initial;

        if(isAlpha(ch)){        //若是字母，则状态为 Id，并且记录ch与type
            newState = DfaState.Id;
            temp_token.type = TokenType.Identifier;
            temp_tokenText.append(ch);
        }
        else if(isDigit(ch)){
            newState = DfaState.IntLiteral;
            temp_token.type = TokenType.IntLiteral;
            temp_tokenText.append(ch);
        }
        else if(ch == '>'){
            newState = DfaState.GT;
            temp_token.type = TokenType.GT;
            temp_tokenText.append(ch);
        }
        return newState;
    }

    /**
     * 判断是否是字母
     * @param ch
     * @return
     */
    @Override
    public boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    /**
     * 判断是否是数字
     * @param ch
     * @return
     */
    @Override
    public boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * 判断是否是空白字符
     * @param ch
     * @return
     */
    @Override
    public boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }

    /**
     * 输出分词结果
     */
    @Override
    public void Print() {
        System.out.println("text\ttype");
        Token token = null;
        for(int i = 0; i < tokens.size(); i++)
            System.out.println( tokens.get(i).get_TokenText() + "\t\t" +  tokens.get(i).get_TokenType());
        }
}

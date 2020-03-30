package MyLexer;

import Token.SimpleToken;
import Token.SimpleTokenReader;
import Token.Token;
import Token.TokenType;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MySimpleLexer implements SimpleLexer {

    public static void main(String[] args) throws IOException {
        MySimpleLexer lexer = new MySimpleLexer();
        String script = "int age = 45;";
        System.out.println("parse :" + script);
        SimpleTokenReader tokenReader = lexer.tokenize(script);
        dump(tokenReader);
    }

    //临时保存Token的文本
    StringBuffer temp_tokenText = null;
    //保存读取的Token
    private List<Token> tokens = null;
    //记录正在解析的Token
    private SimpleToken temp_token = null;


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
            if (ch == 'i') {
                newState = DfaState.Id_int1;
            } else {
                newState = DfaState.Id; //进入Id状态
            }
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
        else if (ch == '+') {
            newState = DfaState.PLUS;
            temp_token.type = TokenType.PLUS;
            temp_tokenText.append(ch);
        }
        else if (ch == '-') {
            newState = DfaState.MINUS;
            temp_token.type = TokenType.MINUS;
            temp_tokenText.append(ch);
        }
        else if (ch == '*') {
            newState = DfaState.STAR;
            temp_token.type = TokenType.STAR;
            temp_tokenText.append(ch);
        }
        else if (ch == '/') {
            newState = DfaState.SLASH;
            temp_token.type = TokenType.SLASH;
            temp_tokenText.append(ch);
        }
        else if (ch == ';') {
            newState = DfaState.SemiColon;
            temp_token.type = TokenType.SemiColon;
            temp_tokenText.append(ch);
        }
        else if (ch == '(') {
            newState = DfaState.LeftParen;
            temp_token.type = TokenType.LeftParen;
            temp_tokenText.append(ch);
        } else if (ch == ')') {
            newState = DfaState.RightParen;
            temp_token.type = TokenType.RightParen;
            temp_tokenText.append(ch);
        }
        else if (ch == '=') {
            newState = DfaState.Assignment;
            temp_token.type = TokenType.Assignment;
            temp_tokenText.append(ch);
        }
        else {
            newState = DfaState.Initial; // skip all unknown patterns
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

    /**
     *
     * @param tokenReader
     */
    public static void dump(SimpleTokenReader tokenReader){
        System.out.println("text\ttype");
        Token token = null;
        while ((token= tokenReader.read())!=null){
            System.out.println(token.get_TokenText()+"\t\t"+token.get_TokenType());
        }
    }
    /**
     * 把输入的字符串Token化
     * @param code
     * @return
     */
    @Override
    public SimpleTokenReader tokenize(String code) throws IOException {
        //建立ArrayList
        tokens = new ArrayList<Token>();
        //字符串拆成字符
        CharArrayReader reader = new CharArrayReader(code.toCharArray());
        //初始化
        temp_tokenText = new StringBuffer();
        temp_token = new SimpleToken();
        int ich = 0;
        char ch = 0;
        DfaState state = DfaState.Initial;
        try {
            while ((ich = reader.read()) != -1){    //没有读到结尾
                ch = (char) ich;
                switch (state){     //由上一次状态确定转移方向
                    case GE:
                    case Assignment:
                    case PLUS:
                    case MINUS:
                    case SLASH:
                    case STAR:
                    case SemiColon:
                    case LeftParen:
                    case RightParen:
                    case Initial:
                        state = InitToken(ch);
                        break;
                    case Id:
                        if(isAlpha(ch) || isDigit(ch)){
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
                    case IntLiteral:
                        if (isDigit(ch)) {
                            temp_tokenText.append(ch);       //继续保持在数字字面量状态
                        } else {
                            state = InitToken(ch);      //退出当前状态，并保存Token
                        }
                        break;
                    case Id_int1:
                        if (ch == 'n') {
                            state = DfaState.Id_int2;
                            temp_tokenText.append(ch);
                        }
                        else if (isDigit(ch) || isAlpha(ch)){
                            state = DfaState.Id;    //切换回Id状态
                            temp_tokenText.append(ch);
                        }
                        else {
                            state = InitToken(ch);
                        }
                        break;
                    case Id_int2:
                        if (ch == 't') {
                            state = DfaState.Id_int3;
                            temp_tokenText.append(ch);
                        }
                        else if (isDigit(ch) || isAlpha(ch)){
                            state = DfaState.Id;    //切换回id状态
                            temp_tokenText.append(ch);
                        }
                        else {
                            state = InitToken(ch);
                        }
                        break;
                    case Id_int3:
                        if (isBlank(ch)) {
                            temp_token.type = TokenType.INT;
                            state = InitToken(ch);
                        }
                        else{
                            state = DfaState.Id;    //切换回Id状态
                            temp_tokenText.append(ch);
                        }
                        break;
                }
            }
            // 把最后一个token送进去
            if (temp_tokenText.length() > 0) {
                InitToken(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SimpleTokenReader(tokens);
    }
}

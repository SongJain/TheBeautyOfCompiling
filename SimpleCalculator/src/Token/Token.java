package Token;

/**
 * Token接口，包含两个方法
 */
public interface Token {
    /**
     * 获取Token
     * @return
     */
    public TokenType get_TokenType();

    /**
     * 获取Token内容
     * @return
     */
    public String get_TokenText();
}

package TokenInterface;

/**
 * Interface.Token 接口，可用于对 Interface.Token 的拓展
 */
public interface Token {
    /**
     * 获取 Interface.Token 的类型
     * @return
     */
    public TokenType getType();

    /**
     * 获取 Interface.Token 的文本内容
     * @return
     */
    public String getText();
}

package Token;

/**
 *  TokenReader是Token读取流，封装了对TokenList操作的方法
 */
public interface TokenReader {

    /**
     * 读取Token，并且丢弃
     * @return
     */
    public Token read();

    /**
     * 预读操作，读取，但不丢弃
     * @return
     */
    public Token peek();

    /**
     * 后退一步，恢复原来的Token
     */
    public void unread();

    /**
     * 获取流当前访问的位置
     * @return
     */
    public int getPosition();

    /**
     * 设置访问位置
     * @param position
     */
    public void setPosition(int position);
}

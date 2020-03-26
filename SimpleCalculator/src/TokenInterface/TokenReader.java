package TokenInterface;

/**
 * TokenReader，Token流，用于实现对 Token 的操作。
 */
public interface TokenReader {
    /**
     * 从Token流中读取Token，并从流中取出
     * @return
     */
    public Token read();
    /**
     * 返回Token流中下一个Token，但不从流中取出。 如果流已经为空，返回null;
     */
    public Token peek();

    /**
     * Token流回退一步。恢复原来的Token。
     */
    public void unread();

    /**
     * 获取Token流当前的读取位置。
     * @return
     */
    public int getPosition();

    /**
     * 设置Token流当前的读取位置
     * @param position
     */
    public void setPosition(int position);
}

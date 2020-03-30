package AST;

import java.util.List;

/**
 * AST的树节点
 */
public interface ASTNode {
    /**
     * 获取父节点
     * @return
     */
    public ASTNode getParent();

    /**
     * 获取子节点
     * @return
     */
    public List<ASTNode> getChildren();

    /**
     * 获取节点类型
     * @return
     */
    public ASTNodeType getType();

    /**
     * 获取节点值
     * @return
     */
    public String getText();
}

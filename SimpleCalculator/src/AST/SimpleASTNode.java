package AST;

import Token.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 简单的AST节点实现类
 */
public class SimpleASTNode implements ASTNode{
    //父节点
    SimpleASTNode parent = null;
    //子节点
    List<ASTNode> children = new ArrayList<>();
    List<ASTNode> readonlyChildren = Collections.unmodifiableList(children);
    //类型
    ASTNodeType nodeType = null;
    String text = null;

    //构造方法
    public SimpleASTNode(ASTNodeType nodeType, String text) {
        this.nodeType = nodeType;
        this.text = text;
    }

    @Override
    public ASTNode getParent() {
        return parent;
    }

    @Override
    public List<ASTNode> getChildren() {
        return readonlyChildren;
    }

    @Override
    public ASTNodeType getType() {
        return nodeType;
    }

    @Override
    public String getText() {
        return text;
    }

    /**
     * 添加子节点
     * @param child
     */
    public void addChild(SimpleASTNode child) {
        children.add(child);
        child.parent = this;
    }
}

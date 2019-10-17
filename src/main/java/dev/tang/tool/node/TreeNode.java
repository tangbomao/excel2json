package dev.tang.tool.node;

public class TreeNode {

    private String label;
    private String id;

    public TreeNode(String label, String id) {
        super();
        this.label = label;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package dev.tang.tool.node;

import dev.tang.tool.export.AbstractExport;
import dev.tang.tool.util.PathUtils;

import java.util.List;
import java.util.Map;

/**
 * Action script 3 Model export
 *
 * @author TangYing
 */
public class TreeNodeExport extends AbstractExport {

    private String fileName;
    private Map<Integer, List<TreeNode>> nodes;
    private Map<Integer, String> categoryMap;

    public TreeNodeExport(String fileName, Map<Integer, List<TreeNode>> nodes, Map<Integer, String> categoryMap) {
        super(fileName, ".xml");

        this.fileName = fileName;
        this.nodes = nodes;
        this.categoryMap = categoryMap;
    }

    @Override
    public void export() {
        String exportUrl = PathUtils.getXmlPath() + fileName + suffix;

        // Generate
        StringBuffer sb = new StringBuffer();
        sb.append("<nodes>\n");
        // Properties
        for (Integer key : nodes.keySet()) {
            List<TreeNode> list = nodes.get(key);

            sb.append("\t<node label='" + categoryMap.get(key) + "'>\n");
            for (TreeNode node : list) {
                sb.append("\t\t<node id='" + node.getId() + "' label='" + node.getLabel() + "'/>\n");
            }
            sb.append("\t</node>\n");
        }
        // Close 
        sb.append("</nodes>\n");

        // Write
        writeJsonFile(exportUrl, sb);
    }
}

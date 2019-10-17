package dev.tang.tool.export;

import dev.tang.tool.util.CharacterUtils;
import dev.tang.tool.util.PathUtils;

import java.util.Map;

/**
 * Action script 3 Model export
 *
 * @author TangYing
 */
public class AsExport extends AbstractExport {

    private boolean clientHidden;
    private Map<String, String> typeMap;
    private Map<String, String> commentMap;

    public AsExport(String fileName, Map<String, String> typeMap, Map<String, String> commentMap, boolean clientHidden) {
        super(fileName, "as");

        this.fileName = fileName;
        this.typeMap = typeMap;
        this.commentMap = commentMap;
        this.clientHidden = clientHidden;
    }

    @Override
    public void export() {
        // File Name
        String className = CharacterUtils.convert(fileName);

        String exportUrl = PathUtils.getAsPath() + className + suffix;

        // Generate
        StringBuffer sb = new StringBuffer();
        sb.append("package cfg {\n\n");
        sb.append("public class " + className + " {\n\n");
        // Properties
        for (String key : typeMap.keySet()) {
            String typeCellContent = typeMap.get(key).toLowerCase();

            // 检查是否要隐藏部分数据
            boolean typeHidden = typeCellContent.contains("hidden");
            if (clientHidden && typeHidden) {

            } else {
                String comment = commentMap.containsKey(key) ? commentMap.get(key).replace("\n", "\t") : "";
                sb.append("\tpublic var " + key + ":" + getAsType(typeMap.get(key)) + ";\t// " + comment + "\n");
            }
        }
        // Constructor
        sb.append("\n\tpublic function " + className + "() {\n\n");
        sb.append("\t}\n");
        // Close class
        sb.append("}\n");
        // Close package
        sb.append("}\n");

        // Write
        writeJsonFile(exportUrl, sb);
    }

    /**
     * Get action script data type
     */
    private String getAsType(String type) {
        type = type.toLowerCase();
        if (type.equals("int")) {
            return "int";
        } else if (type.equals("double")) {
            return "Number";
        } else if (type.equals("boolean")) {
            return "Boolean";
        } else if (type.equals("string")) {
            return "String";
        } else if (type.equals("int[]")) {
            return "Array";
        } else if (type.equals("double[]")) {
            return "Array";
        } else if (type.equals("boolean[]")) {
            return "Array";
        } else if (type.equals("string[]")) {
            return "Array";
        } else {
            return "undefined";
        }
    }
}

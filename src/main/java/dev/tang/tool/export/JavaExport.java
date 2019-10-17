package dev.tang.tool.export;

import dev.tang.tool.util.CharacterUtils;
import dev.tang.tool.util.PathUtils;

import java.util.Map;

/**
 * Action script 3 Model export
 *
 * @author TangYing
 */
public class JavaExport extends AbstractExport {

    private Map<String, String> typeMap;
    private Map<String, String> commentMap;

    public JavaExport(String fileName, Map<String, String> typeMap, Map<String, String> commentMap) {
        super(fileName, "java");

        this.fileName = fileName;
        this.typeMap = typeMap;
        this.commentMap = commentMap;
    }

    @Override
    public void export() {
        // File Name
        String className = CharacterUtils.convert(fileName);

        String exportUrl = PathUtils.getJavaPath() + className + suffix;

        // Generate
        StringBuffer sb = new StringBuffer();
        sb.append("package cn.oookay.server.cache.impl.model\n\n");
        sb.append("public class " + className + " { \n\n");

        for (String key : typeMap.keySet()) {
            String comment = commentMap.containsKey(key) ? commentMap.get(key).replace("\n", "\t") : "";
            sb.append("\tprivate " + getJavaType(typeMap.get(key)) + " " + key + ";\t// " + comment + "\n");
        }

        sb.append("}\n");

        // Write
        writeJsonFile(exportUrl, sb);
    }

    /**
     * Get java data type
     */
    private String getJavaType(String type) {
        type = type.toLowerCase();
        if (type.equals("int")) {
            return "Integer";
        } else if (type.equals("double")) {
            return "Double";
        } else if (type.equals("boolean")) {
            return "Boolean";
        } else if (type.equals("string")) {
            return "String";
        } else if (type.equals("int[]")) {
            return "Integer[]";
        } else if (type.equals("double[]")) {
            return "Double[]";
        } else if (type.equals("boolean[]")) {
            return "Boolean[]";
        } else if (type.equals("string[]")) {
            return "String[]";
        } else {
            return "undefined";
        }
    }
}

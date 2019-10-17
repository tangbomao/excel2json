package dev.tang.tool.export;

import dev.tang.tool.util.PathUtils;

import java.util.List;
import java.util.Map;

/**
 * GSON数据格式文本导出
 *
 * @author TangYing
 */
public class LangExport extends AbstractExport {

    private List<Map<String, Object>> datas;

    public LangExport(String fileName, List<Map<String, Object>> datas) {
        super(fileName, "txt");

        this.fileName = fileName;
        this.datas = datas;
    }

    @Override
    public void export() {
        String exportUrl = PathUtils.getLangPath() + fileName + suffix;

        // Generate
        StringBuffer sb = new StringBuffer();

        for (Map<String, Object> map : datas) {
            String msg = map.get("msg").toString();
            String value = map.get("value").toString();

            if (msg.startsWith("#")) {
                sb.append(msg);
            } else if (!"".equals(msg)) {
                sb.append(msg).append("=").append(value);
            }
            sb.append("\r\n");
        }

        // Write
        writeJsonFile(exportUrl, sb);
    }
}

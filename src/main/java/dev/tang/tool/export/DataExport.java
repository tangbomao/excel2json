package dev.tang.tool.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GSON数据格式文本导出
 */
public class DataExport extends AbstractExport {

    private String outputDir;
    private List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

    public DataExport(String fileName, List<Map<String, Object>> list, String outputDir) {
        super(fileName, "json");

        this.fileName = fileName;
        this.outputDir = outputDir;

        for (Map<String, Object> map : list) {
            if (!map.isEmpty()) {
                this.datas.add(map);
            }
        }
    }

    @Override
    public void export() {
        if (datas.isEmpty()) {
            return;
        }

        String exportUrl = outputDir + fileName + suffix;
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        // Generate
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("\r\n");
        for (int i = 0; i < datas.size(); i++) {
            String tmp = gson.toJson(datas.get(i));
            String s2 = StringEscapeUtils.unescapeJava(tmp);
            sb.append(s2);
            if (i < datas.size() - 1) {
                sb.append(",");
            }
            sb.append("\r\n");
        }
        sb.append("]");

        // Write
        writeJsonFile(exportUrl, sb);
    }
}

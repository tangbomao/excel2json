package dev.tang.tool;

import dev.tang.tool.export.LangExport;
import dev.tang.tool.logger.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.*;

/**
 * 语言文本处理
 * <p>
 * 处理规则如下:
 * 第一列为基础名称，第二列开始为每个语言的语言文本
 * </p>
 *
 * @author TangYing
 */
public class XlsLangReader {

    private static final Logger LOG = new Logger(XlsLangReader.class);

    private String filePath;

    /**
     * 语言文本数据
     * <p>
     * 格式如下
     * 语言文本名称->数据
     */
    private Map<String, List<Map<String, Object>>> datas = new HashMap<String, List<Map<String, Object>>>();

    /**
     * Constructor
     *
     * @param filePath
     */
    public XlsLangReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 导出文件
     */
    public void read() {
        String fileName = filePath.substring(0, filePath.lastIndexOf("."));
        File file = new File(fileName + ".xls");

        Workbook wwb;
        try {
            // 获取内容
            wwb = Workbook.getWorkbook(file);

            // 获取分页第一页
            Sheet sheet = wwb.getSheet(0);

            // 获取第一行，第一行为名称行
            Cell[] firstRows = sheet.getRow(0);

            // 获取总行数
            int totalRowNum = sheet.getRows();
            int totalColumnNum = firstRows.length;

            // 从第二行开始循环加载数据
            for (int i = 1; i < totalRowNum; i++) {
                // 获取当前行所有数据
                Cell[] cells = sheet.getRow(i);
                if (cells == null || cells.length == 0) {
                    continue;
                }

                // 每一行第一列为多语言文本替换名称
                String nameKey = cells[0].getContents();

                // 从第二列开始加载数据
                for (int j = 1; j < totalColumnNum; j++) {
                    // 所属语言文本
                    String lang = firstRows[j].getContents();
                    if ("".equals(lang)) {
                        continue;
                    }

                    // 初始化多语言数据池
                    if (!datas.containsKey(lang)) {
                        datas.put(lang, new LinkedList<Map<String, Object>>());
                    }

                    // 多语言数据列表
                    List<Map<String, Object>> langList = datas.get(lang);

                    // 获取当前单元格文本
                    String value = cells.length <= j ? "" : cells[j] == null ? "" : cells[j].getContents();

                    // 临时数据存储器
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    map.put("msg", nameKey);
                    map.put("value", value);

                    // 放入数据列表
                    langList.add(map);
                }
            }

            // 批量导出数据
            for (Map.Entry<String, List<Map<String, Object>>> entry : datas.entrySet()) {
                String name = entry.getKey();
                List<Map<String, Object>> data = entry.getValue();

                LangExport langExport = new LangExport(name, data);
                langExport.export();
            }

            wwb.close();
        } catch (Exception e) {
            LOG.error("{0}, {1}", fileName, e.toString());
            e.printStackTrace();
        }
    }
}

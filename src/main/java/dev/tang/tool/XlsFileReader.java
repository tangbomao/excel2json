package dev.tang.tool;

import dev.tang.tool.export.AsExport;
import dev.tang.tool.export.DataExport;
import dev.tang.tool.logger.Logger;
import jxl.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Excel file reader
 *
 * @author TangYing
 */
public class XlsFileReader {

    private static final Logger LOG = new Logger(XlsFileReader.class);

    private String filePath;

    /**
     * 输出文件夹名称
     */
    private String outputDir;

    /**
     * 支持客户端隐藏数据
     */
    private boolean clientHidden;

    /**
     * 数据类型
     */
    private Map<String, String> typeMap = new LinkedHashMap<String, String>();

    /**
     * 注释
     */
    private Map<String, String> commentMap = new LinkedHashMap<String, String>();

    /**
     * 数据
     */
    private List<Map<String, Object>> datas = new LinkedList<Map<String, Object>>();

    /**
     * Constructor
     *
     * @param filePath
     */
    public XlsFileReader(String filePath, String outputDir, boolean clientHidden) {
        this.filePath = filePath;
        this.outputDir = outputDir;
        this.clientHidden = clientHidden;
    }

    /**
     * 导出文件
     */
    public void read() {
        int errorRow = 0;
        int errorCell = 0;

        String fileName = filePath.substring(0, filePath.lastIndexOf("."));
        File file = new File(fileName + ".xls");

        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding("ISO-8859-1");

        Workbook wwb;
        try {
            // 获取内容
            wwb = Workbook.getWorkbook(file, workbookSettings);

            // 获取分页第一页
            Sheet sheet = wwb.getSheet(0);

            // ===========================
            // Start to generate
            // ===========================

            // Get first row cells
            Cell[] firstRows = sheet.getRow(0);

            // Get all comments
            errorRow = 0;
            for (int j = 0; j < firstRows.length; j++) {
                errorCell = j;
                Cell cell = firstRows[j];
                if (cell != null) {
                    String key = firstRows[j].getContents();
                    String comment = "";
                    commentMap.put(key, comment);
                }
            }

            // Get second row cells
            Cell[] secondRows = sheet.getRow(1);

            errorRow = 1;
            for (int j = 0; j < secondRows.length; j++) {
                errorCell = j;
                Cell cell = secondRows[j];
                if (cell != null) {
                    String key = firstRows[j].getContents();
                    String type = cell.getContents();
                    typeMap.put(key, type);
                }
            }

            // Get all data
            int totalRowNum = sheet.getRows();
            // For each cell, except first and second row
            for (int i = 2; i < totalRowNum; i++) {
                // Row map data
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                // Get row
                Cell[] rowCells = sheet.getRow(i);

                for (int j = 0; j < firstRows.length; j++) {
                    errorRow = i;
                    errorCell = j;

                    String key = firstRows[j].getContents();
                    Cell cell = rowCells.length > j ? rowCells[j] : null;

                    // Default object value
                    Object obj = "";
                    // Check cell null
                    if (cell != null && !cell.getContents().equals("")) {
                        obj = handleStringCell(key, cell);
                    } else {
                        obj = handleBlankCell(key, cell);
                    }

                    if (obj != null) {
                        // value map
                        map.put(key, obj);
                    }
                }

                // Add data map
                datas.add(map);
            }

            // JSON
            DataExport dataExport = new DataExport(fileName, datas, outputDir);
            dataExport.export();

            wwb.close();
        } catch (IOException ioe) {
            LOG.error("{0}, 第{1}行,  第{2}列, IOException. {3}", fileName, errorRow + 1, errorCell + 1, ioe.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("{0}, 第{1}行,  第{2}列, Exception. {3}", fileName, errorRow + 1, errorCell + 1, e.toString());
        }
    }

    public void exportAs() {
        String fileName = filePath.substring(0, filePath.lastIndexOf("."));
        AsExport asExport = new AsExport(fileName, typeMap, commentMap, clientHidden);
        asExport.export();
    }

    /**
     * Handle blank cell, default value
     *
     * @param cell
     * @return
     */
    private Object handleBlankCell(String key, Cell cell) {
        Object obj = "";
        String typeCellContent = typeMap.get(key).toLowerCase();

        // 检查是否要隐藏部分数据
        boolean typeHidden = typeCellContent.contains("hidden");
        if (clientHidden && typeHidden) {
            return null;
        }

        String type = typeCellContent.split("\\|")[0];
        if (type.equals("int[]")) {
            obj = new int[]{};
        }
        // Double array
        else if (type.equals("double[]")) {
            obj = new double[]{};
        }

        // String array
        else if (type.equals("string[]")) {
            obj = new String[]{};
        }

        // Integer
        else if (type.equals("int")) {
            obj = 0;
        }

        // double
        else if (type.equals("double")) {
            obj = 0.0;
        }

        // boolean
        else if (type.equals("boolean")) {
            obj = false;
        }

        // empty
        else if (type.equals("empty")) {
            obj = null;
        }
        return obj;
    }

    /**
     * Handle string cell
     *
     * @param cell
     * @return
     */
    private Object handleStringCell(String key, Cell cell) {
        Object obj = null;
        String typeCellContent = typeMap.get(key).toLowerCase();

        // 检查是否要隐藏部分数据
        boolean typeHidden = typeCellContent.contains("hidden");
        if (clientHidden && typeHidden) {
            return null;
        }

        // 获取type
        String type = typeCellContent.split("\\|")[0];

        // Get the cell value
        String str = cell.getContents();

        // Boolean
        if (type.equals("boolean")) {
            obj = Boolean.valueOf(Boolean.parseBoolean(str.toLowerCase()));
        }

        // Int
        else if (type.equals("int")) {
            obj = Integer.valueOf(Integer.parseInt(str.toLowerCase()));
        }

        // Double
        else if (type.equals("double")) {
            NumberCell numberCell = (NumberCell) cell;
            obj = numberCell.getValue();
        }

        // Integer array
        else if (type.equals("int[]")) {
            String[] array = str.split("\\|");
            if (array.length != 0) {
                Integer[] numArray = new Integer[array.length];
                for (int k = 0; k < array.length; k++) {
                    numArray[k] = Integer.parseInt(array[k]);
                }
                obj = numArray;
            }
        }

        // Double array
        else if (type.equals("double[]")) {
            String[] array = str.split("\\|");
            if (array.length != 0) {
                BigDecimal[] numArray = new BigDecimal[array.length];
                for (int k = 0; k < array.length; k++) {
                    double d = Double.parseDouble(array[k]);
                    numArray[k] = BigDecimal.valueOf(d);
                }
                obj = numArray;
            }
        }

        // Boolean array
        else if (type.equals("boolean[]")) {
            String[] array = str.split("\\|");
            if (array.length != 0) {
                Boolean[] booleanArray = new Boolean[array.length];
                for (int k = 0; k < array.length; k++) {
                    booleanArray[k] = Boolean.parseBoolean(array[k]);
                }
                obj = booleanArray;

            }
        }

        // String array
        else if (type.equals("string[]")) {
            String[] array = str.split("\\|");
            if (array.length != 0) {
                String[] strArray = new String[array.length];
                for (int k = 0; k < array.length; k++) {
                    strArray[k] = array[k];
                }
                obj = strArray;
            }
        }

        // empty
        else if (type.equals("empty")) {
            obj = null;
        }

        // Other else
        else {
            obj = str;
        }

        return obj;
    }
}

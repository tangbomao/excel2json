package dev.tang.tool.export;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 内容导出
 */
public abstract class AbstractExport {

    /**
     * 导出的文件名称
     */
    protected String fileName;

    /**
     * 导出的格式后缀
     */
    protected String suffix;

    /**
     * 构造函数
     *
     * @param suffix
     */
    public AbstractExport(String fileName, String suffix) {
        this.fileName = fileName;
        this.suffix = "." + suffix;
    }

    /**
     * 导出函数
     */
    abstract public void export();

    /**
     * 写入数据到目标路径
     *
     * @param exportUrl
     * @param sb
     */
    protected void writeJsonFile(String exportUrl, StringBuffer sb) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportUrl), "UTF-8"));
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

package dev.tang.tool;

import dev.tang.tool.util.FileUtils;
import dev.tang.tool.util.PathUtils;

import java.io.File;
import java.util.List;

/**
 * 主入口
 *
 * @author TangYing
 */
public class Main {

    public static void main(String[] args) {
        // 删除错误日志
        FileUtils.deleteFile(PathUtils.getLoggerPath());

        exportClientData();
        exportServerData();
    }

    /**
     * 导出客户端数据文本
     */
    private static void exportClientData() {
        // 清理数据
        FileUtils.mkdir(PathUtils.getClientDataPath());
        FileUtils.deleteAllFile(PathUtils.getClientDataPath());

        // 清理数据
        FileUtils.mkdir(PathUtils.getAsPath());
        FileUtils.deleteAllFile(PathUtils.getAsPath());

        // Get all excel file paths
        List<String> fileList = PathUtils.listFile(PathUtils.XLS_SUFFIX);

        for (String filePath : fileList) {
            XlsFileReader xlsFileReader = new XlsFileReader(filePath, PathUtils.getClientDataPath(), true);
            xlsFileReader.read();
            xlsFileReader.exportAs();
        }

        // 待生成的zip名称和后缀
        String zipName = "data_for_client";
        String suffix = "zip";
        // 待生成的zip保存路径
        String zipFilePath = PathUtils.getRootPath();
        // 删除老的文件
        FileUtils.deleteFile(zipFilePath + File.separator + zipName + "." + suffix);
        // ZIP压缩
        FileUtils.fileToZip(PathUtils.getClientDataPath(), zipFilePath, zipName, suffix);
    }

    /**
     * 导出服务器数据文本
     */
    private static void exportServerData() {
        FileUtils.mkdir(PathUtils.getServerDataPath());
        FileUtils.deleteAllFile(PathUtils.getServerDataPath());

        // Get all excel file paths
        List<String> fileList = PathUtils.listFile(PathUtils.XLS_SUFFIX);

        for (String filePath : fileList) {
            XlsFileReader xlsFileReader = new XlsFileReader(filePath, PathUtils.getServerDataPath(), false);
            xlsFileReader.read();
        }

        // 待生成的zip名称和后缀
        String zipName = "data_for_server";
        String suffix = "zip";
        // 待生成的zip保存路径
        String zipFilePath = PathUtils.getRootPath();
        // 删除老的文件
        FileUtils.deleteFile(zipFilePath + File.separator + zipName + "." + suffix);
        // ZIP压缩
        FileUtils.fileToZip(PathUtils.getServerDataPath(), zipFilePath, zipName, suffix);
    }

    /**
     * 导出语言文本
     */
    private static void exportLang() {
        FileUtils.mkdir(PathUtils.getLangPath());

        // Get all excel file paths
        List<String> fileList = PathUtils.listFile(PathUtils.XLS_SUFFIX);

        for (String filePath : fileList) {
            XlsLangReader xlsLangReader = new XlsLangReader(filePath);
            xlsLangReader.read();
        }
    }
}

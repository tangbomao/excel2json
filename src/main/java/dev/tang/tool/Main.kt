package dev.tang.tool

import dev.tang.tool.util.FileUtils
import dev.tang.tool.util.PathUtils

import java.io.File

/**
 * 主入口
 *
 * @author TangYing
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // 删除错误日志
        FileUtils.deleteFile(PathUtils.loggerPath)

        // 导出客户端数据
        exportClientData()

        // 导出服务端数据
        exportServerData()
    }

    /**
     * 导出客户端数据文本
     */
    private fun exportClientData(zipName: String = "data_for_client") {
        // 清理数据
        FileUtils.mkdir(PathUtils.clientDataPath)
        FileUtils.deleteAllFile(PathUtils.clientDataPath)

        // Get all excel file paths
        val fileList = PathUtils.listFile(PathUtils.xlsSuffix)

        for (filePath in fileList) {
            val xlsFileReader = XlsFileReader(filePath, PathUtils.clientDataPath, true)
            xlsFileReader.read()
        }

        // 待生成的zip名称和后缀
        val suffix = "zip"
        // 待生成的zip保存路径
        val zipFilePath = PathUtils.rootPath
        // 删除老的文件
        FileUtils.deleteFile(zipFilePath + File.separator + zipName + "." + suffix)
        // ZIP压缩
        FileUtils.fileToZip(PathUtils.clientDataPath, zipFilePath, zipName, suffix)
    }

    /**
     * 导出服务器数据文本
     */
    private fun exportServerData(zipName: String = "data_for_server") {
        FileUtils.mkdir(PathUtils.serverDataPath)
        FileUtils.deleteAllFile(PathUtils.serverDataPath)

        // Get all excel file paths
        val fileList = PathUtils.listFile(PathUtils.xlsSuffix)

        for (filePath in fileList) {
            val xlsFileReader = XlsFileReader(filePath, PathUtils.serverDataPath, false)
            xlsFileReader.read()
        }

        // 待生成的zip名称和后缀
        val suffix = "zip"
        // 待生成的zip保存路径
        val zipFilePath = PathUtils.rootPath
        // 删除老的文件
        FileUtils.deleteFile(zipFilePath + File.separator + zipName + "." + suffix)
        // ZIP压缩
        FileUtils.fileToZip(PathUtils.serverDataPath, zipFilePath, zipName, suffix)
    }
}

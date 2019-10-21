package dev.tang.tool.export

import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

/**
 * 内容导出
 */
abstract class AbstractExport
/**
 * 构造函数
 *
 * @param suffix
 */
(
        /**
         * 导出的文件名称
         */
        protected var fileName: String, suffix: String) {

    /**
     * 导出的格式后缀
     */
    protected var suffix: String

    init {
        this.suffix = ".$suffix"
    }

    /**
     * 导出函数
     */
    abstract fun export()

    /**
     * 写入数据到目标路径
     *
     * @param exportUrl
     * @param sb
     */
    protected fun writeJsonFile(exportUrl: String, sb: StringBuffer) {
        try {
            val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(exportUrl), "UTF-8"))
            bw.write(sb.toString())
            bw.flush()
            bw.close()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }

    }
}

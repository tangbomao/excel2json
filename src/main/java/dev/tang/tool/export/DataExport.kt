package dev.tang.tool.export

import com.google.gson.GsonBuilder
import org.apache.commons.lang.StringEscapeUtils
import java.util.*

/**
 * GSON数据格式文本导出
 */
class DataExport(fileName: String, list: List<Map<String, Any>>, private val outputDir: String) : AbstractExport(fileName, "json") {
    private val datas = ArrayList<Map<String, Any>>()

    init {

        this.fileName = fileName

        for (map in list) {
            if (!map.isEmpty()) {
                this.datas.add(map)
            }
        }
    }

    override fun export() {
        if (datas.isEmpty()) {
            return
        }

        val exportUrl = outputDir + fileName + suffix
        val gson = GsonBuilder().disableHtmlEscaping().create()

        // Generate
        val sb = StringBuffer()
        sb.append("[")
        sb.append("\r\n")
        for (i in datas.indices) {
            val tmp = gson.toJson(datas[i])
            val s2 = StringEscapeUtils.unescapeJava(tmp)
            sb.append(s2)
            if (i < datas.size - 1) {
                sb.append(",")
            }
            sb.append("\r\n")
        }
        sb.append("]")

        // Write
        writeJsonFile(exportUrl, sb)
    }
}

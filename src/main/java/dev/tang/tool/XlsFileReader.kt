package dev.tang.tool

import dev.tang.tool.export.DataExport
import dev.tang.tool.logger.Logger
import jxl.Cell
import jxl.Workbook
import jxl.WorkbookSettings
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Excel文件处理器
 *
 * @param outputDir 输出文件夹名称
 * @param clientHidden 支持客户端隐藏数据，默认false
 *
 * @author TangYing
 */
class XlsFileReader(private val filePath: String, private val outputDir: String, private val clientHidden: Boolean = false) {

    companion object {
        private val LOG = Logger(XlsFileReader::class.java)
    }

    /**
     * 数据类型
     */
    private val typeMap = LinkedHashMap<String, String>()

    /**
     * 注释
     */
    private val commentMap = LinkedHashMap<String, String>()

    /**
     * 数据
     */
    private val datas = LinkedList<Map<String, Any>>()

    /**
     * 导出文件
     */
    fun read() {
        var errorRow = 0
        var errorCell = 0

        val fileName = filePath.substring(0, filePath.lastIndexOf("."))
        val file = File("$fileName.xls")

        val workbookSettings = WorkbookSettings()
        workbookSettings.encoding = "ISO-8859-1"

        val wwb: Workbook
        try {
            // 获取内容
            wwb = Workbook.getWorkbook(file, workbookSettings)

            // 获取分页第一页
            val sheet = wwb.getSheet(0)

            // ===========================
            // Start to generate
            // ===========================

            // Get first row cells
            val firstRows = sheet.getRow(0)

            // Get all comments
            for (j in firstRows.indices) {
                errorCell = j
                val cell = firstRows[j]
                if (cell != null) {
                    val key = firstRows[j].contents
                    val comment = ""
                    commentMap[key] = comment
                }
            }

            // Get second row cells
            val secondRows = sheet.getRow(1)

            errorRow = 1
            for (j in secondRows.indices) {
                errorCell = j
                val cell = secondRows[j]
                if (cell != null) {
                    val key = firstRows[j].contents
                    val type = cell.contents
                    typeMap[key] = type
                }
            }

            // Get all data
            val totalRowNum = sheet.rows
            // For each cell, except first and second row
            for (i in 2 until totalRowNum) {
                // Row map data
                val map = LinkedHashMap<String, Any>()
                // Get row
                val rowCells = sheet.getRow(i)

                for (j in firstRows.indices) {
                    errorRow = i
                    errorCell = j

                    val key = firstRows[j].contents
                    val cell = if (rowCells.size > j) rowCells[j] else null

                    // Default object value
                    var obj: Any? = ""
                    // Check cell null
                    if (cell != null && cell.contents != "") {
                        obj = handleStringCell(key, cell)
                    } else {
                        obj = handleBlankCell(key)
                    }

                    if (obj != null) {
                        // value map
                        map[key] = obj
                    }
                }

                // Add data map
                datas.add(map)
            }

            // JSON
            val dataExport = DataExport(fileName, datas, outputDir)
            dataExport.export()

            wwb.close()
        } catch (ioe: IOException) {
            LOG.error("{0}, 第{1}行,  第{2}列, IOException. {3}", fileName, errorRow + 1, errorCell + 1, ioe.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            LOG.error("{0}, 第{1}行,  第{2}列, Exception. {3}", fileName, errorRow + 1, errorCell + 1, e.toString())
        }

    }

    /**
     * 空数据处理，填补有效默认数据
     *
     * @return
     */
    private fun handleBlankCell(key: String): Any? {
        val typeCellContent = typeMap[key]?.toLowerCase() ?: ""

        // 隐藏数据
        val typeHidden = typeCellContent.contains("hidden")
        if (clientHidden && typeHidden) {
            return null
        }

        // 获取数据类型
        val type = typeCellContent.split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
        return when (type) {
            "int[]" -> intArrayOf()
            "double[]" -> doubleArrayOf()
            "string[]" -> arrayOf<String>()
            "int" -> 0
            "double" -> 0.0
            "boolean" -> false
            "empty" -> null
            else -> null
        }
    }

    /**
     * 正常数据数据，根据数据类型转化数据
     *
     * @param cell
     * @return
     */
    private fun handleStringCell(key: String, cell: Cell): Any? {
        val typeCellContent = typeMap[key]?.toLowerCase() ?: ""

        // 隐藏部分数据
        val typeHidden = typeCellContent.contains("hidden")
        if (clientHidden && typeHidden) {
            return null
        }

        // 获取原始数据
        val str = cell.contents

        // 获取数据类型
        val type = typeCellContent.split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
        return when (type) {
            "int[]" -> str.split("\\|").map { it.toInt() }
            "double[]" -> str.split("\\|").map { it.toDouble() }
            "string[]" -> str.split("\\|")
            "int" -> str.toInt()
            "double" -> str.toDouble()
            "boolean" -> str.toBoolean()
            "empty" -> null
            else -> null
        }
    }
}

package dev.tang.tool.logger

import dev.tang.tool.util.FileUtils
import dev.tang.tool.util.PathUtils
import dev.tang.tool.util.TimeUtils

/**
 * 日志显示器
 */
class Logger(private val clazz: Class<*>) {

    fun warn(content: String) {
        var content = content
        val loggerPath = PathUtils.loggerPath
        content = "[Warn] " + TimeUtils.currentTime + " [" + clazz.name + "] " + content
        FileUtils.writeFile(loggerPath, content, true)
    }

    fun warn(content: String, vararg args: Any) {
        var content = content
        for (i in args.indices) {
            content = content.replace("{$i}", args[i].toString())
        }

        val loggerPath = PathUtils.loggerPath
        content = "[Warn] " + TimeUtils.currentTime + " [" + clazz.name + "] " + content
        FileUtils.writeFile(loggerPath, content, true)
    }

    fun error(content: String, vararg args: Any) {
        var content = content
        for (i in args.indices) {
            content = content.replace("{$i}", args[i].toString())
        }

        val loggerPath = PathUtils.loggerPath
        content = "[Error] " + TimeUtils.currentTime + " [" + clazz.name + "] " + content
        FileUtils.writeFile(loggerPath, content, true)
    }
}

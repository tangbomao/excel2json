package dev.tang.tool.logger;

import dev.tang.tool.util.FileUtils;
import dev.tang.tool.util.PathUtils;
import dev.tang.tool.util.TimeUtils;

/**
 * 日志显示器
 */
public class Logger {

    private Class<?> clazz;

    public Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void warn(String content) {
        String loggerPath = PathUtils.getLoggerPath();
        content = "[Warn] " + TimeUtils.getCurrentTime() + " [" + clazz.getName() + "] " + content;
        FileUtils.writeFile(loggerPath, content, true);
    }

    public void warn(String content, Object... args) {
        for (int i = 0; i < args.length; i++) {
            content = content.replace("{" + i + "}", String.valueOf(args[i]));
        }

        String loggerPath = PathUtils.getLoggerPath();
        content = "[Warn] " + TimeUtils.getCurrentTime() + " [" + clazz.getName() + "] " + content;
        FileUtils.writeFile(loggerPath, content, true);
    }

    public void error(String content, Object... args) {
        for (int i = 0; i < args.length; i++) {
            content = content.replace("{" + i + "}", String.valueOf(args[i]));
        }

        String loggerPath = PathUtils.getLoggerPath();
        content = "[Error] " + TimeUtils.getCurrentTime() + " [" + clazz.getName() + "] " + content;
        FileUtils.writeFile(loggerPath, content, true);
    }
}

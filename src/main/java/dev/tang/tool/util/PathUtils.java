package dev.tang.tool.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathUtils {

    private static String rootPath;

    private static final String JAVA_PATH = File.separator + "java" + File.separator;
    private static final String AS_PATH = File.separator + "as" + File.separator;
    private static final String SERVER_DATA_PATH = File.separator + "data_for_server" + File.separator;
    private static final String CLIENT_DATA_PATH = File.separator + "data_for_client" + File.separator;
    private static final String LANG_PATH = File.separator + "locale" + File.separator;

    static {
        rootPath = new File("").getAbsolutePath();
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String getJavaPath() {
        return getRootPath() + JAVA_PATH;
    }

    public static String getAsPath() {
        return getRootPath() + AS_PATH;
    }

    public static String getServerDataPath() {
        return getRootPath() + SERVER_DATA_PATH;
    }

    public static String getClientDataPath() {
        return getRootPath() + CLIENT_DATA_PATH;
    }

    public static String getLangPath() {
        return getRootPath() + LANG_PATH;
    }

    public static String getXmlPath() {
        return getRootPath() + File.separator;
    }

    public static String getLoggerPath() {
        return getRootPath() + File.separator + "error";
    }

    // Excel suffix
    public static final String XLS_SUFFIX = "xls";

    /**
     * List file with suffix in root path
     *
     * @param suffix
     * @return
     */
    public static List<String> listFile(String suffix) {
        List<String> list = new ArrayList<String>();

        File file = new File(rootPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                String filePath = files[i].getName();
                if (suffix != null) {
                    int begIndex = filePath.lastIndexOf(".");
                    String tempSuffix = "";
                    if (begIndex != -1) {
                        tempSuffix = filePath.substring(begIndex + 1, filePath.length());
                    }
                    if (tempSuffix.equals(suffix)) {
                        list.add(filePath);
                    }
                } else {
                    list.add(filePath);
                }
            }
        }
        return list;
    }
}

package dev.tang.tool.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具
 */
public class FileUtils {

    /**
     * Make file directory
     *
     * @param dir
     * @return
     */
    public static String mkdir(String dir) {
        boolean created = false;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            if (dirFile.mkdirs()) {
                created = true;
            } else {

            }
        } else {
            created = true;
        }

        if (created) {
            return dir;
        } else {
            return "";
        }
    }

    /**
     * Write file
     *
     * @param filePath
     * @param content
     * @param append
     */
    public static void writeFile(String filePath, String content, boolean append) {
        try {
            StringBuffer sb = new StringBuffer();
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, append));

            sb.append(content);
            sb.append("\r\n");

            bw.write(sb.toString());
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Write bytes
     *
     * @param filePath
     */
    public static void writeFile(String filePath, byte[] bytes) {
        try {
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException ioe) {
        }
    }

    /**
     * Delete all files
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        boolean success = true;

        File file = new File(path);
        if (!file.exists()) {
            success = false;
        }
        if (file.isDirectory()) {
            success = false;
        }
        success &= file.delete();
        return success;
    }

    /**
     * Delete all files
     *
     * @param path
     * @return
     */
    public static boolean deleteAllFile(String path) {
        boolean success = true;

        File file = new File(path);
        if (!file.exists()) {
            success = false;
        }
        if (!file.isDirectory()) {
            success = false;
        }

        if (success) {
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    success &= temp.delete();
                }
            }
        }
        return success;
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @param suffix         :自定义压缩文件后缀名
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName, String suffix) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
            sourceFile.mkdir(); // 新建目录
        }

        try {
            File zipFile = new File(zipFilePath + "/" + fileName + "." + suffix);
            if (zipFile.exists()) {
                System.out.println(zipFilePath + "目录下存在名字为:" + fileName + "." + suffix + "打包文件.");
            } else {
                File[] sourceFiles = sourceFile.listFiles();
                if (null == sourceFiles || sourceFiles.length < 1) {
                    System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                } else {
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    byte[] bufs = new byte[1024 * 10];
                    for (int i = 0; i < sourceFiles.length; i++) {
                        //创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(sourceFiles[i]);
                        bis = new BufferedInputStream(fis, 1024 * 10);
                        int read = 0;
                        while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                    }
                    flag = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            //关闭流
            try {
                if (null != bis) {
                    bis.close();
                }

                if (null != zos) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return flag;
    }

}

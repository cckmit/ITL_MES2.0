package com.itl.iap.attachment.provider.utils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * rar文件工具类
 *
 * @author 谭强
 * @date 2020-08-21
 * @since jdk1.8
 */
public class RarUtils {

    public static void unRarFile(File srcRar, String dstDirectoryPath) throws IOException, RarException {

        Archive archive = new Archive(srcRar);
        FileHeader fh = archive.nextFileHeader();
        while (fh != null) {
            String path = fh.getFileNameString().replaceAll("\\\\", "/");
            File dirFile = new File(dstDirectoryPath + File.separator
                    + path);
            // 文件夹
            if (fh.isDirectory()) {
                dirFile.mkdirs();
            } else { // 文件
                try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                    if (!dirFile.exists()) {
                        // 相对路径可能多级，可能需要创建父目录.
                        if (!dirFile.getParentFile().exists()) {
                            dirFile.getParentFile().mkdirs();
                        }
                        dirFile.createNewFile();
                    }
                    FileOutputStream os = new FileOutputStream(dirFile);
                    archive.extractFile(fh, os);
                    os.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            fh = archive.nextFileHeader();
        }
        archive.close();

    }
}

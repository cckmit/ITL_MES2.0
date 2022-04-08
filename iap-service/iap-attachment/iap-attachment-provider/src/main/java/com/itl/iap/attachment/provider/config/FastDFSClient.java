package com.itl.iap.attachment.provider.config;

import com.itl.iap.attachment.provider.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * fastDFS工具类
 *
 * @author 谭强
 * @date 2020/8/24
 * @since jdk1.8
 */
@Component
@Slf4j
public class FastDFSClient {
    private static final String FILE_GROUP = "group1";
    private static final String IMAGES_GROUP = "group1";


    @Autowired
    private FastConnectionPool connectionPool;

    /**
     * 上传文件
     *
     * @param buff 文件内容
     * @return String
     */
    public String uploadFile(byte[] buff) {
        return uploadFile(buff, null, null, FILE_GROUP);
    }

    /**
     * 上传文件
     *
     * @param buff 文件内容
     * @param fileName 文件名称
     * @return String
     */
    public String uploadImages(byte[] buff, String fileName) {
        return uploadFile(buff, fileName, null, IMAGES_GROUP);
    }

    /**
     * 按照分组上传文件
     *
     * @param buff 文件内容
     * @param fileName 文件名称
     * @param metaList
     * @param groupName 分组名称
     * @return String
     */
    private String uploadFile(byte[] buff, String fileName, Map<String, String> metaList, String groupName) {
        try {
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name, value);
                }
            }
            /** 获取可用的tracker,并创建存储server */
            StorageClient1 storageClient = connectionPool.checkout();

            String path = null;
            if (!StringUtils.isEmpty(fileName)) {
                path = storageClient.upload_file1(groupName, buff,
                        FileUtils.getFileType(fileName), nameValuePairs);
            } else {
                path = storageClient.upload_file1(groupName, buff,
                        fileName, nameValuePairs);
            }
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(storageClient);
            return path;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件组名
     *
     * @param fileId 文件id
     * @return Map<String, String>
     */
    public Map<String, String> getFileMetadata(String fileId) {
        try {
            /** 获取可用的tracker,并创建存储server */
            StorageClient1 storageClient = connectionPool.checkout();
            NameValuePair[] metaList = storageClient.get_metadata1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(storageClient);
            if (metaList != null) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(), metaItem.getValue());
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     * @return int
     */
    public int deleteFile(String fileId) {
        try {
            /** 获取可用的tracker,并创建存储server */
            StorageClient1 storageClient = connectionPool.checkout();

            int i = storageClient.delete_file1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(storageClient);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 下周文件
     *
     * @param fileId 文件id
     * @return byte[]
     */
    public byte[] downloadFile(String fileId) {
        try {
            /** 获取可用的tracker,并创建存储server */
            StorageClient1 storageClient = connectionPool.checkout();

            byte[] content = storageClient.download_file1(fileId);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(storageClient);

            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileInfo getFileInfo(String fileId, String remoteFileName) {
        try {
            /** 获取可用的tracker,并创建存储server */
            StorageClient1 storageClient = connectionPool.checkout();
            FileInfo fileInfo = storageClient.get_file_info(fileId, remoteFileName);
            /** 上传完毕及时释放连接 */
            connectionPool.checkin(storageClient);
            return fileInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }
}

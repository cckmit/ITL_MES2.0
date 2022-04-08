package com.itl.iap.common.base.model;

import lombok.Data;

/**
 * FastDFS文件类
 *
 * @author lx
 * @date 2020-07-10 17:15
 * @since jdk1.8
 */
@Data
public class FastDFSFile {

    private String name;
    private byte[] content;
    private String ext;
    private String md5;
    private String author;

    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }
}

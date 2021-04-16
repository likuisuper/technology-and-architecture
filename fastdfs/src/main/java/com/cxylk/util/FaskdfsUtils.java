package com.cxylk.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.csource.fastdfs.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Classname FaskdfsUtils
 * @Description Faskdfs通用方法
 * @Author likui
 * @Date 2021/4/16 11:25
 **/
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "fdfs")
public class FaskdfsUtils {
    private Map<String, String> properties;

    private String nginx;

    private static StorageClient1 client = null;

    private static String url = null;

    @PostConstruct
    public void init() throws Exception {
        Properties properties = new Properties();
        properties.putAll(getProperties());
        ClientGlobal.initByProperties(properties);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();

        if (trackerServer == null) {
            throw new RuntimeException("获取Tracker server 发生异常");
        }

        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        client = new StorageClient1(trackerServer, storageServer);
        url = getNginx();
    }

    /**
     * 上传方法
     *
     * @param path 路径
     * @return 上传成功返回id，失败返回null
     */
    public static String upload(String path) throws IOException, MyException {
        File file = new File(path);
        //指定额外的存储信息
        NameValuePair[] nameValuePairs = new NameValuePair[1];
        nameValuePairs[0] = new NameValuePair("filename", file.getName());
        String[] strArray = file.getName().split("\\.");
        String extName = strArray.length > 1 ? strArray[strArray.length - 1] : "";
        //上传文件
        if (client == null) {
            throw new RuntimeException("fastdfs 客户端未初始化");
        }
        return client.upload_file1(file.getAbsolutePath(), extName, nameValuePairs);
    }

    /**
     * 上传方法
     *
     * @param content InputStream，文件名称
     * @return 上传成功返回id，失败返回null
     */
    public static String upload(InputStream content, String fileName) throws IOException, MyException {
        try {
            //指定额外的存储信息
            NameValuePair[] nameValuePairs = new NameValuePair[1];
            nameValuePairs[0] = new NameValuePair("filename", fileName);
            String[] strArray = fileName.split("\\.");
            String extName = strArray.length > 1 ? strArray[strArray.length - 1] : "";
            //上传文件
            if (client == null) {
                throw new RuntimeException("fastdfs 客户端未初始化");
            }
            byte[] bs = IOUtils.toByteArray(content);
            return client.upload_appender_file1(bs, extName, nameValuePairs);
        } finally {
            content.close();
        }
    }

    /**
     * 上传ZIP
     *
     * @param content InputStream
     * @return
     */
    public static List<String> uploadZip(InputStream content) throws IOException, MyException {
        List<String> list = new ArrayList<>();
        ZipInputStream zipInputStream = new ZipInputStream(content, Charset.forName("GBK"));
        try {
            ZipEntry ze;
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String name = ze.getName();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IOUtils.copy(zipInputStream, out);
                    InputStream in = new ByteArrayInputStream(out.toByteArray());
                    list.add(upload(in, name));
                }
            }
        } finally {
            zipInputStream.close();
        }
        return list;
    }

    public static int delete(String path) throws IOException, MyException {
        if (client == null) {
            throw new RuntimeException("fastdfs 客户端未初始化");
        }
        return client.delete_file1(path);
    }

    /**
     * inputtream 转byte数组
     *
     * @param in
     * @return byte[]
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }

    /**
     * 下载
     *
     * @param path fastdfs上传后返回的字符串
     */
    public static synchronized byte[] download(String path) throws IOException, MyException {
        //获取上传时候,写入的文件信息
        if (client == null) {
            throw new RuntimeException("fastdfs 客户端未初始化");
        }

        //下载,返回文件字节数组
        return client.download_file1(path);
    }

    /**
     * 获取带有token的访问地址
     *
     * @param path 示例：group1/M00/00/00/L2ZUml6QisqAUJE3AIOPO1HT6Bo274.mp4
     */
    public static String getTokenUrl(String path) {
        path = path.replace("group1/", "");
        //时间戳 单位为秒
        int ts = (int) (System.currentTimeMillis() / 1000);
        String token;
        try {
            String secretKey = ClientGlobal.getG_secret_key();
            token = ProtoCommon.getToken(path, ts, secretKey);
        } catch (Exception e) {
            log.error("FastDFS获取token异常", e);
            throw new RuntimeException("FastDFS获取token异常");
        }

        return url + path + "?token=" + token + "&ts=" + ts;
    }
}

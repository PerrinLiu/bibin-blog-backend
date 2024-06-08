package com.llpy.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 */
@Component
public class AliOSSUtils {

    private final AliOSSProperties aliOSSProperties;

    public AliOSSUtils(AliOSSProperties aliOSSProperties) {
        this.aliOSSProperties = aliOSSProperties;
    }

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();
        String directory = aliOSSProperties.getDirectory();
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        //生成一个uuid拼接上文件的后缀作为文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
        //文件名称
        fileName = getFileName(fileName, directory);
        //Oss实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //上传文件到 OSS
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }

    //上传字符串
    public String uploadString(String s) throws IOException {
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();
        String directory = aliOSSProperties.getDirectory();

        //文件组成
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        //文件名称
        fileName = getFileName(fileName, directory);

        //Oss实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //上传到 OSS
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(s.getBytes()));
        System.out.println(putObjectResult);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }

    /**
     * 删除文件
     *
     * @param url 文件链接
     */
    public void delete(String url) {
        String endpoint = aliOSSProperties.getEndpoint();
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String bucketName = aliOSSProperties.getBucketName();
        String directory = aliOSSProperties.getDirectory();
        // 提取文件名
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        //拿到完整文件名
        fileName = getFileName(fileName, directory);
        //Oss实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //删除文件
        ossClient.deleteObject(bucketName, fileName);
        // 关闭ossClient
        ossClient.shutdown();
    }

    /**
     * 获取文件名称
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    private String getFileName(String fileName, String directory) {
        //获取当月时间
        LocalDateTime nowDateTime = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        String date = nowDateTime.format(fmt);
        //指定文件夹然后划分月份
        fileName = directory + "/" + date + "/" + fileName;
        return fileName;
    }


}

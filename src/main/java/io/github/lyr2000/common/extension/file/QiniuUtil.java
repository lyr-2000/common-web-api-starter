package io.github.lyr2000.common.extension.file;

import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.github.lyr2000.common.dto.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author lyr
 * @create 2021/2/11 19:42
 */
@Slf4j
@AllArgsConstructor
public class QiniuUtil {


    // @Resource
    final QiniuProperties qiniuProperties;

    // private QiniuUtils qiniuUtils = new QiniuUtils();
    // public QiniuUtils getInstance() {
    //     return qiniuUtils;
    // }


    /**
     * 上传文件，并且返回 url参数
     * @param file
     * @return
     *
     */
    public String uploadFile(MultipartFile file) {
        return uploadFile(file,
                qiniuProperties.getRegion(),
                qiniuProperties.getAccessKey(),
                qiniuProperties.getSecureKey(),
                qiniuProperties.getBucketName(),
                qiniuProperties.getUrlDomain());
    }





    /**
     * 七牛云上传根据
     * @param file 问卷路径
     * @param accessKey  ak
     * @param secretKey  sk
     * @param bucketName 空间
     * @param urlDomain  域名前缀
     * @return 结果
     */
    public static String uploadFile(MultipartFile file,Region region, String accessKey, String secretKey, String bucketName, String urlDomain) {
        // Configuration cfg = new Configuration(Region.region2());
        Configuration cfg = new Configuration(region);
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//         String accessKey = "QRSquLXA 6Npu02_Oe";
//         String secretKey = "RAV8Z ZwV7RvQtOJeG1hM0tlnp5";
//         String bucket = "ywf";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = IdUtil.fastSimpleUUID();

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);
        String result=null;
        try {
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            // System.out.println(putRet.key);
            // System.out.println(putRet.hash);
            // log.info(putRet.key);
            // result = putRet.hash;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            try {
                log.error(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
                log.error(String.valueOf(ex2));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        //图片路径： 域名+'/'+ 具体的hash值
        return urlDomain+'/'+key;

    }
}

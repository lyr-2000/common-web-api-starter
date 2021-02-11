package io.github.lyr2000.common.extension.file;

import com.qiniu.storage.Region;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lyr
 * @create 2021/2/11 19:41
 */
@Configuration
@Data
@Accessors(chain = true)
public class QiniuProperties {
    /**
     * 公钥
     */
    private String accessKey;
    /**
     * 密钥
     */
    private String secureKey;
    /**
     * 桶名字
     */
    private String bucketName;
    /**
     * 域名
     */
    private String urlDomain;
    /**
     * 地区
     */
    private Region region;
}

package io.github.lyr2000.common.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author lyr
 * @create 2021/2/11 18:06
 */
public class PatternUtil {
    /**
     * 匹配 {} 的字符串
     */
    private static final Pattern P = Pattern.compile("(?<=\\{)[^}]*(?=})");

    /**
     * 将 字符串中 的 {} 替换成对应数据
     * @param template
     * @param data
     * @return
     */
    public static  String parseNullAsNull(String template, Map<String,Object> data) {
        Matcher matcher = P.matcher(template);
        while (matcher.find()) {
            String group = matcher.group();
            Object res = data.get(group);
            if (res==null) {
                res = "null";
            }
            template = template.replace("{"+group+"}",res.toString());
        }
        return template;
    }
    public static String parseNullAsBlank(String template,Map<String, Object>data) {
        Matcher matcher = P.matcher(template);
        while (matcher.find()) {
            String group = matcher.group();
            Object res = data.get(group);
            if (res==null) {
                res = "";
            }
            template = template.replace("{"+group+"}",res.toString());
        }
        return template;
    }
}

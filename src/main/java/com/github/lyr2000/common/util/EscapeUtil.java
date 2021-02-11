package com.github.lyr2000.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.HtmlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 对 HTML 标签进行过滤
 * 防止 script标签 脚本等
 * @Author lyr
 * @create 2021/2/10 23:55
 */
@Slf4j
public class EscapeUtil {
    public static void escapeHtml(Object object) {
        if (object==null) return;
        Class clazz = object.getClass();
        try {
            for (Field f: clazz.getDeclaredFields()) {
                int modType = f.getModifiers();
                if (Modifier.isFinal(modType) || Modifier.isStatic(modType)) {
                    //final 或者 static 直接跳过
                    continue;
                }
                if (String.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    String value = (String) f.get(object);
                    if (value!=null) {
                        f.set(object, HtmlUtils.htmlEscape(value));
                    }
                }
            }
        }catch (Exception ex) {
            log.error("escape_error{}",ex.getMessage());
        }
    }
}

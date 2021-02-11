package io.github.lyr2000.common.shiro.util;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;

/**
 * @Author lyr
 * @create 2021/2/9 21:48
 */
@Slf4j
public class ShiroWebUtil {
    enum RenderType {
        /**
         * JSON类型
         */
        JSON,
        /**
         * 文本类型
         */
        TEXT,
        FILE
    }

    /**
     * 渲染JSON
     *
     * @param resp
     * @param jsonObject
     */
    public static void renderJson(HttpServletResponse resp, Object jsonObject) {

        setResponseType(RenderType.JSON, resp);
        try {
            PrintWriter writer = resp.getWriter();

            writer.println(JSONObject.toJSONString(jsonObject));
        } catch (IOException e) {
            log.warn("出现写入异常@webUtil");
        }
    }

    /**
     * 渲染文本
     *
     * @param response
     * @param text
     */
    public static void renderText(HttpServletResponse response, String text) {
        setResponseType(RenderType.TEXT, response);
        try {
            PrintWriter p = response.getWriter();
            p.println(text);
        } catch (IOException e) {
            log.warn("出现写入异常@webUtil");
        }
    }

    public static void render(HttpServletResponse resp, String outValue, RenderType renderType) {
        setResponseType(renderType, resp);
        try {
            PrintWriter p = resp.getWriter();
            p.println(outValue);
        } catch (IOException e) {
            log.warn("出现写入异常@webUtil");
        }
    }

    private static void setResponseType(RenderType type, HttpServletResponse response) {
        switch (type) {
            case JSON: {
                response.setContentType("application/json; charset=UTF-8");
                break;
            }
            case TEXT: {
                response.setContentType("text/html; charset=UTF-8");
                break;
            }
            default: {

            }
        }
        response.setStatus(200);
    }


    /**
     * 获取 JSON
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getJson(HttpServletRequest request, Class<T> clazz) {


        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            // if(responseStrBuilder.length()<=0) {
            //     return null;
            // }
            return JSON.parseObject(responseStrBuilder.toString(), clazz);
        } catch (Exception e) {
            log.error("解析json 失败");
        }

        return null;
    }

    /**
     * 读取字符串
     * @param request
     * @return
     */
    public static String readStr(HttpServletRequest request) {
        try{
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            return responseStrBuilder.toString();
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取字符串
     * @param reader
     * @return
     */
    public static String toStr(BufferedReader reader) {
        String inputStr;
        StringBuilder responseStrBuilder = new StringBuilder();
        try{
            while ((inputStr = reader.readLine()) != null) {
                responseStrBuilder.append(inputStr).append("\r\n");
            }
        }catch (Exception e) {
            log.error("error on io");
        }
        return responseStrBuilder.toString();
    }

    public static JSONObject getJson(HttpServletRequest req) {
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            return JSONObject.parseObject(responseStrBuilder.toString());
        } catch (Exception e) {
            log.error("解析json 失败");
        }
        return null;

    }


    /**
     * 获取表单数据
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getForm(HttpServletRequest request, Class<T> clazz)   {
        try{
            // if (AopPropertyUtils.isJavaApi(clazz)) {
            //     return null;
            // }
            JSONObject map = new JSONObject();
            for (Field field : clazz.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                    continue;
                }
                String key = field.getName();
                map.put(key,request.getParameter(key));
            }

            return map.toJavaObject(clazz);
        }catch (Exception ex) {
            log.error("解析 formData 失败 {}",ex.getMessage());
        }
        return null;
    }

    public static void writeFile(InputStream fromStream, HttpServletResponse response, String filename)  {
        response.setContentType("application/binary;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try (

                BufferedOutputStream to = new BufferedOutputStream(response.getOutputStream());

                BufferedInputStream  from    = new BufferedInputStream(fromStream);
        ){

            // Files.copy(from,to);
            IoUtil.copy(from,to);
            // IoUtil.copyByNIO(from,to,IoUtil.DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}

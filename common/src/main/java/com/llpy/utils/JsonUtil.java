package com.llpy.utils;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper mObjectMapper = new ObjectMapper();
    private static final ObjectMapper mObjectMapperLowerCaseWithUnderScores = new ObjectMapper();

    //在这里进行配置全局
    static {
        //有时JSON字符串中含有我们并不需要的字段，那么当对应的实体类中不含有该字段时，会抛出一个异常，告诉你有些字段（java 原始类型）没有在实体类中找到
        //设置为false即不抛出异常，并设置默认值 int->0 double->0.0
        mObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mObjectMapperLowerCaseWithUnderScores.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //序列化时添加下划线
        mObjectMapperLowerCaseWithUnderScores.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
    }

    public static String beanToJson(Object object) {
        if (object != null) {
            try {
                return mObjectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                System.out.println("bean to json exception");
            }
        }
        return "";
    }

    public static <T> T jsonToBean(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json) && classType != null) {
            //                return mObjectMapper.readValue(json, classType);
            JSONObject jsonObject = JSONObject.parseObject(json);
            return (T) JSONObject.toJavaObject(jsonObject, classType);

        }
        return null;
    }

    public static <T> List<T> jsonToList(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json) && classType != null) {
            try {
                return mObjectMapper.readValue(json, mObjectMapper.getTypeFactory().constructCollectionType(List.class, classType));
            } catch (IOException e) {
                System.out.println("json to list exception");
            }
        }
        return null;
    }

    public static <k, v> Map<k, v> jsonToMap(String json, Class<k> kType, Class<v> vType) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return mObjectMapper.readValue(json, mObjectMapper.getTypeFactory().constructMapType(Map.class, kType, vType));
            } catch (IOException e) {
                System.out.println("json to Map exception");
            }
        }
        return null;
    }

    public static String beanToJsonByLowerCase(Object object) {
        if (object != null) {
            try {
                return mObjectMapperLowerCaseWithUnderScores.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                System.out.println("bean to json with lowerCase exception");
            }
        }
        return "";
    }

    public static <T> T jsonToBeanByLowerCase(String json, Class<T> classType) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return mObjectMapperLowerCaseWithUnderScores.readValue(json, classType);
            } catch (IOException e) {
                System.out.println("json to Bean with lowerCase exception");
            }
        }
        return null;
    }

}
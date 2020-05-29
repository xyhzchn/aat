package com.mob.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TransUtils {

    public static String json2StrForGet(JSONObject json){
        String str = "";
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        if(json != null){

            Set<String> set = json.keySet();
            Iterator it = set.iterator();
            while (it.hasNext()){
                String key = it.next().toString();
                String value = json.get(key).toString();
                if(null != value && value != ""){
                    Object real_value = NumberUtils.isNumber(value) == true?Integer.parseInt(value):value;
                    sb.append(key+"="+real_value+"&");
                }
            }
        }
        return sb.toString();
    }


    public static Map<String,Object> json2map(JSONObject json){
        Map<String,Object> map = new HashMap<String, Object>();
        if(null != json){
            map = json.getInnerMap();
        }
        return map;
    }

    public static Object getValue(String obj){
        if(null != obj){
            if(NumberUtils.isCreatable(obj)){
                return Integer.parseInt(obj);
            }

            if(isJson(obj)){
                return JSONObject.parseObject(obj);
            }
        }
        return obj;
    }

    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

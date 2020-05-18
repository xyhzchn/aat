package com.mob.utils;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

public class Assertion {

    public static boolean flag = true;

    public static List<Error> errors = new ArrayList<Error>();

    public static void verifyEquals(Object actual, Object expected){
        try{

            if(isJSONObject(actual.toString()) && isJSONObject(expected.toString())){
                Assert.assertSame(actual,expected);
            }else if(isJSONArray(actual.toString()) && isJSONObject(expected.toString())){
                Assert.assertSame(actual,expected);
            }else {
                Assert.assertEquals(actual, expected);
            }

        }catch(Error e){
            errors.add(e);
            flag = false;
        }
    }

    public static void verifyEquals(Object actual, Object expected, String message){
        String actualStr = actual.toString();
        String expectedStr = expected.toString();
        try{
            if(isJSONObject(actualStr) && isJSONObject(expectedStr)){
                JSONObject actualJson = JSONObject.parseObject(expectedStr);
                JSONObject expectedJson = JSONObject.parseObject(expectedStr);
                Assert.assertEquals(actualJson,expectedJson,message);
            }else if(isJSONArray(actual.toString()) && isJSONObject(expected.toString())){
                JSONArray actualJson = JSONArray.parseArray(expectedStr);
                JSONArray expectedJson = JSONArray.parseArray(expectedStr);
                Assert.assertEquals(actualJson,expectedJson,message);
            }else {
                Assert.assertEquals(actual, expected,message);
            }
        }catch(Error e){
            errors.add(e);
            flag = false;
        }
    }

    public static boolean isJSONObject(String str){
        if(StringUtils.isBlank(str))
            return false;
        try {
            JSONObject json = JSONObject.parseObject(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isJSONArray(String str){
        if(StringUtils.isBlank(str))
            return false;
        try {
            JSONArray json = JSONArray.parseArray(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
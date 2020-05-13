package com.mob.utils;

import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredUtils {

    public static  Response get(){

        Response response = given().when().get("http://www.baidu.com");
        System.out.println(response.getStatusCode());

        return response;
    }

    public static Response post(String url, String body,Map headerMap){

        if(null == headerMap){
            Map<String,Object> header_default = new HashMap<String, Object>();
            header_default.put("Content-Type","application/json");
            header_default.put("Connection","keep-alive");
            header_default.put("Accept-Encoding","gzip, deflate, br");
            header_default.put("Accept","*/*");
            header_default.put("User-Agent","gzip, deflate, br");
            headerMap = header_default;
        }

        Response response = given().headers(headerMap).body(body).when().post(url);

        return response;
    }

    public static void main(String args[]){
        get();
    }
}

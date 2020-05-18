package com.mob.utils;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssuredUtils {

    public static  Response get(String url,Map headerMap){
        if(null == headerMap){
            Map<String,Object> header_default = new HashMap<String, Object>();
            header_default.put("Content-Type","application/json");
            header_default.put("Connection","keep-alive");
            header_default.put("Accept-Encoding","gzip, deflate, br");
            header_default.put("Accept","*/*");
            header_default.put("User-Agent","gzip, deflate, br");
            headerMap = header_default;
        }

        Response response = given().headers(headerMap).when().get(url);

        return response;
    }

    public static Response post(String url, String body,Map headerMap){

        if(null == headerMap){
            Map<String,Object> header_default = new HashMap<String, Object>();
            header_default.put("Content-Type","application/json");
            header_default.put("Connection","keep-alive");
            header_default.put("Accept-Encoding","gzip, deflate, br");
            header_default.put("Accept","*/*");
            header_default.put("User-Agent","PostmanRuntime/7.24.1");
            headerMap = header_default;
        }

        Response response = given().log().all().headers(headerMap).body(body).when().post(url);

        return response;
    }



}

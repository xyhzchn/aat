package com.mob.utils;

import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

/**
 * @author zhangsht
 * @version 1.0
 * @date 2019/11/28 14:50
 */
public class AssertUtils {

    /**
     * 判断返回值中，某个字段等于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     */
    public static void equal(Response response , String path, Object expect){
        equal(response,path,expect, Parser.JSON);
    }

    /**
     * 判断返回值中，某个字段等于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     * @param parser 响应结果格式转换成自己想要的格式
     */
    public static void equal(Response response, String path, Object expect, Parser parser){
        response.then().statusCode(200).using().defaultParser(parser).body(path,equalTo(expect));
    }

    /**
     * 判断返回值中，某个字段不等于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     */
    public static void notEqual(Response response , String path, Object expect){
        notEqual(response,path,expect, Parser.JSON);
    }


    /**
     * 判断返回值中，某个字段不等于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     * @param parser 响应结果格式转换成自己想要的格式
     */
    public static void notEqual(Response response , String path, Object expect, Parser parser){
        response.then().statusCode(200).using().defaultParser(parser).body(path,not(equalTo(expect)));
    }

    /**
     * 判断返回值中，某个字段大于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     */
    public static void greater(Response response , String path, int expect){
        greater(response,path,expect, Parser.JSON);
    }

    /**
     * 判断返回值中，某个字段大于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     * @param parser 响应结果格式转换成自己想要的格式
     */
    public static void greater(Response response , String path, int expect, Parser parser){
        response.then().statusCode(200).using().defaultParser(parser).body(path, greaterThan(expect));
    }

    /**
     * 判断返回值中，某个字段小于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     */
    public static void less(Response response , String path, int expect){
        less(response,path,expect, Parser.JSON);
    }

    /**
     * 判断返回值中，某个字段小于于自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     * @param parser 响应结果格式转换成自己想要的格式
     */
    public static void less(Response response , String path, int expect, Parser parser){
        response.then().statusCode(200).using().defaultParser(parser).body(path, lessThan(expect));
    }

    /**
     * 判断返回值中，某个字段是自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     */
    public static void isExpert(Response response , String path, int expect){
        isExpert(response,path,expect, Parser.JSON);
    }

    /**
     * 判断返回值中，某个字段是自己期望的值，
     * @param response  接口的响应值
     * @param path json的路径
     * @param expect 期望值
     * @param parser 响应结果格式转换成自己想要的格式
     */
    public static void isExpert(Response response , String path, int expect, Parser parser){
        response.then().statusCode(200).using().defaultParser(parser).body(path, is(expect));
    }
}

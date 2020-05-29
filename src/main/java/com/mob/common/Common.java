package com.mob.common;

public interface Common {
    public final String ENV_ADDR = "http://api.os.test.mob.com";
    public final String CONF5_ADDR = "http://10.18.98.212:8081";
    public final String PUSH_TEST_ADDR = "http://10.18.97.47:8080";
    public final String CREATE_PUSH_TEST_ADDR = "http://10.18.97.47:8081";
    public final String POST = "POST";
    public final String GET = "GET";

    //非定时任务等待时间
    public static long normalTime = 10 * 1000;
}

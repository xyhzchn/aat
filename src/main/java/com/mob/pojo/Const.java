package com.mob.pojo;

public class Const {
    public static final byte HEAD_PACKAGE = 0x01;

    public static final int PACKAGE_MAX = 1024 * 1024 * 32;

    public static final int REQ_LEN = 4 * 1024;

    public static final int TYPE_RESPONSE = 1000;

    public static final int TYPE_MIN = 1000;
    public static final int TYPE_MAX = 9999;
    public static final int ERROR_MAX = 20000;

    // 建了连接请求
    public static final int TYPE_REGISTERED = 1001;
    // 心跳
    public static final int TYPE_TICK = 1002;

    public static final int TYPE_PUSH_START = 9000;
    // 基本信息推送请求，由服务端向客户端推送消息
    public static final int TYPE_PUSH = 9001;
    // 命令请求，服务端要求客户端建立新连接
    public static final int TYPE_REDIRECT = 9002;
    public static final int TYPE_PING = 9003;
    // 命令请求，api本地mapper变化，通知Admin
    public static final int TYPE_MAPPER_REMOVE = 9501;
    // 命令请求，设备上线
    public static final int TYPE_ONLINE_MONITOR = 9502;
    // 命令请求，设备下线
    public static final int TYPE_OFFLINE_MONITOR = 9502;

    // 通用错误
    public static final int ERROR = 10000;
    // 解密失败
    public static final int ERROR_DECRY = 10001;
    // 未知的请求类型
    public static final int ERROR_TYPE = 10002;

    // size(int)
    public static final int TYPE_LEN = 4;


    // 客户端注册tcp成功
    public static final int TYPE_REGISTERED_OK = 1;
    // 服务端要求客户端重定向tcp连接
    public static final int TYPE_CONNECT_REDIRECT = 2;
}


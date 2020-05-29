package com.mob.utils;

import android.os.Handler;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lamfire.code.AES;
import com.lamfire.code.Base64;
import com.mob.pojo.ByteMsg;
import com.mob.pojo.Const;
import com.mob.web.security.common.SafeExecutor;
import org.apache.commons.lang3.StringUtils;
import org.testng.util.Strings;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpUtils {

    private String ip;
    private int port;
    private Integer plat;
    private Socket socket;
    private String token;
    private int timeout = 10 * 1000;
    private final static int CONNECT = 2;
    private final static int RECONNECT = 3;
    String rid;
    private InputStream in;
    private OutputStream out ;
    private Object object;
    private Map<String,Object> message;

    public void startConnect(String ip, int port, String rid, Integer plat , Object object) throws  JSONException{
        this.ip=ip;
        this.port=port;
        this.rid = rid;
        this.plat = plat;
        this.object = object;
        close();
        connectTCP();
    }



    public void startConnect(String ip, int port, String rid, Integer plat) throws JSONException {
        this.ip=ip;
        this.port=port;
        this.rid = rid;
        this.plat = plat;
        close();
        connectTCP();
    }

    public void connectTCP() {
        try {
            System.out.println("pushService tcp Init connect to {" + ip + "}:{" + port + "}");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            this.socket = socket;
            out = socket.getOutputStream();
            in = socket.getInputStream();
            System.out.println("pushService tcp connect successful.");
            sendLogin(plat);
            receive();
        } catch (IOException e) {
            System.out.println("pushService Init connect failed, error:" + e);
            reConnect();
        }
    }

    public void sendLogin(Integer plat)  {
        try {
            System.out.println("pushService login tcp by rid:" + rid );
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appkey","moba6b6c6d6");
            jsonObject.put("plat", plat);
            jsonObject.put("rid", rid);
            jsonObject.put("sdkVersion","20304");
            String content = jsonObject.toString();
            int contentLength = content.getBytes("utf-8").length;
            ByteMsg byteMsg = new ByteMsg();
            byteMsg.setHead(Const.HEAD_PACKAGE);
            byteMsg.setLength(contentLength);
            byteMsg.setType(Const.TYPE_REGISTERED);
            byteMsg.setContent(content.getBytes());

            sendMsg(socket, byteMsg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendMsg(Socket socket,ByteMsg byteMsg) throws IOException {

        byte[] bytes = new byte[8];
        out.write(byteMsg.getHead());
        NumberUtil.toBytes(byteMsg.getLength(),bytes);
        out.write(bytes, 0, 4);
        NumberUtil.toBytes(byteMsg.getType(), bytes);
        out.write(bytes, 0, 4);
        out.write(byteMsg.getContent());
        out.flush();
    }


    private void receive() {
        try {

            while (socket.isConnected() && !socket.isClosed()) {
                //接收socket数据
                // find msg had
                int len;
                for (len = in.read(); len != Const.HEAD_PACKAGE;) {
                    if (len == -1) {
                        throw new Exception("read failed, try reconnect");
                    }
                }
                // read len
                len = readInt(in);
                if (len < 0 || len > Const.PACKAGE_MAX) {
                    continue;
                }
                // read type
                int type = readInt(in);
                if (type < Const.TYPE_MIN || type > Const.ERROR_MAX) {
                    continue;
                }

                // read content
                byte[] content;
                if (len > 0) {
                    content = new byte[len];
                    read(in, content);
                } else {
                    content = new byte[0];
                }

                ByteMsg res = new ByteMsg();
                res.setLength(len);
                res.setType(type);
                res.setContent(content);

                System.out.println("pushService client receive tcp server send msg type:" + type);

                if (type == Const.TYPE_RESPONSE || type == Const.TYPE_PUSH) {
                    String result = "";

                    byte[] deRes = null;
                    if (type == Const.TYPE_PUSH) {
                        decrypt(rid, res);
                        result = new String(res.getContent(), 0, res.getLength());

                    } else {
                        result = new String(content, "utf-8");
                        if (Strings.isNullOrEmpty(result)) {
                            continue;
                        }
                    }

                    Map<String, Object> map = TransUtils.json2map(JSONObject.parseObject(result));

                    HashMap<String, Object> data = (HashMap<String, Object>) TransUtils.json2map(JSONObject.parseObject(map.get("data").toString()));

                    if (type == Const.TYPE_RESPONSE) {
                        int reType = (int) data.get("type");
                        if (reType == Const.TYPE_CONNECT_REDIRECT) {
                            String domain = (String) data.get("domain");
                            ip = domain.substring(0, domain.indexOf(":"));
                            port = Integer.valueOf(domain.substring(domain.indexOf(":") + 1));
                            redirect();
                            return;
                        } else if (reType == Const.TYPE_REGISTERED_OK) {
                            token = String.valueOf(data.get("token"));
                        }
                    } else if (type == Const.TYPE_PUSH) {
                        System.out.println("tcp>>>>>>>>>>>>>com.mob.data:"+data);
                        TcpUtils utils = new TcpUtils();
                        utils.setMessage(data);
                        System.out.println("走到通知啦");
                        String c = new com.alibaba.fastjson.JSONObject(data).getJSONObject("message").getString("c");
                        synchronized (c.intern()){
                            System.out.println(c);
                            c.intern().notifyAll();
                        }
                    }
                } else if (type == Const.TYPE_TICK) {
                    int result = 0;
                    if (len > 0) {
                        result = content[0];
                    }
                    System.out.println("pushService tcp tick type:" + result);
                    if (result == 1) {
                        //心跳成功（继续轮询）
                    } else if (result == 2) {
                        //心跳失败（重建连接）
                        reConnect();
                        return;
                    }
                } else if (type == Const.TYPE_PING) {

                    sendPing();
                }
            }
        } catch (Throwable t) {
            System.out.println("pushService tcp receiver failed, error:" + t);
            reConnect();
        }
    }


    private void readHead(InputStream in) throws IOException {
        while (in.read() != Const.HEAD_PACKAGE) ;
    }

    private int readInt(InputStream in) throws IOException {
        int len = 0x0;
        for (int i = 0; i < 4; i++) {
            len = (len << 8) + (in.read() & 0xFF);
        }
        return len;
    }

    private void read(InputStream in, byte[] buf) throws IOException {
        read(in, buf, 0, buf.length);
    }

    private void read(InputStream in, byte[] buf, int start, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            buf[start + i] = (byte) in.read();
        }
    }

    public void decrypt(String rid, ByteMsg byteMsg) {
        SafeExecutor.exec(() -> {
            String base64 = new String(byteMsg.getContent(), 0, byteMsg.getLength(), "UTF-8");
            byte[] encode = Base64.decode(base64);
            String key = String.format("%16s", rid).replaceAll(" ", "0");
            AES aes = new AES(key.substring(0, 16).getBytes());
            byte[] content = aes.decode(encode);
            byteMsg.setContent(content);
        });
    }

    private void redirect() {
        close();
        connectTCP();
    }

    private void close() {
        try {
            if (in != null) {
                in.close();
                in = null;
            }

            if (out != null) {
                out.close();
                out = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Throwable t) {
            System.out.println("pushService close failed, error:" + t);
        }
    }

    private void reConnect() {
        close();
    }

    private void sendPing() {
        if (socket != null && !socket.isClosed()) {
            try {
                if (StringUtils.isEmpty(token)) {
                    //如果token为空，则直接返回
                    System.out.println("pushService sendPing token is null");
                    return;
                }
                System.out.println("pushService tcp ping by token:" + token);
                ByteMsg byteMsg = new ByteMsg();
                byteMsg.setHead(Const.HEAD_PACKAGE);
                byteMsg.setType(Const.TYPE_TICK);
                byteMsg.setLength(token.getBytes().length);
                byteMsg.setContent(token.getBytes());
                sendMsg(socket, byteMsg);
            } catch (Throwable e) {
                System.out.println("pushService tcp ping failed:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }

    public static void syncInternWait(String object, Long time){
        synchronized (object.intern()) {
            try { object.intern().wait(time); } catch (InterruptedException e) { }
        }
    }
}

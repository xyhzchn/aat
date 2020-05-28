package com.mob.testcase.push;

import com.alibaba.fastjson.JSONObject;
import com.mob.common.Common;
import com.mob.pojo.Api;
import com.mob.pojo.TcpPojo;
import com.mob.utils.*;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PushCase {

    public List<Api> apiList;

    public Response dependResponse;
    public Response response;
    public TcpPojo tcp;
    public Api dependApi;
    TcpUtils sendTcp = new TcpUtils();


    @Parameters({ "sheet_push" })
    @BeforeTest
    public void init(String sheet_push){
        apiList = new ExcelUtils().getExcelContent(sheet_push);
        if(apiList != null){
            Iterator<Api> it = apiList.iterator();
            while (it.hasNext()){
                Api api =  it.next();
                if(api.isRun() == false){
                    it.remove();
                }
            }
        }
    }

    @BeforeClass
    public void pre() throws Exception {

        if(apiList != null){
            String dependUrl = apiList.get(0).getDependUrl();
            String[] dependUrlArr = dependUrl.split(",");
            String sheet = dependUrlArr[0];
            int caseId = Integer.parseInt(dependUrlArr[1]);

            if(null != dependUrl && !(dependUrl.equals(""))){
                List<Api> dependList = new ExcelUtils().getExcelContent(sheet);
                if(dependList != null){
                    for(Api api:dependList){
                        if(api.getCaseID() == caseId){
                            dependApi = api;
                            break;
                        }
                    }
                }
            }
        }


        Response res1 = null;
        String reqPath = Common.PUSH_TEST_ADDR+dependApi.getUrl();
        String method = dependApi.getMethod();
        if(method.toUpperCase().equals(Common.POST)){
            String reqBody = EncryptUtils.generalEncode(dependApi.getReqBody().toJSONString(),"push.properties");
            res1 =  RestAssuredUtils.post(reqPath,reqBody,null);
        }else if(method.toUpperCase().equals(Common.GET)){
            TransUtils.json2StrForGet(dependApi.getReqBody());
        }


        int statusCode = res1.getStatusCode();
        if(statusCode == 200){
            String str = res1.getBody().asString();
            String result = EncryptUtils.generalDecode(str);
            dependResponse = new ResponseBuilder().clone(res1).setBody(result).setHeader("Content-Type", "application/json").build();
        }
        Response content = dependResponse.then().statusCode(200).extract().response();
        String ipUrl = content.path("res.domainList[0]");

        tcp = new TcpPojo();
        tcp.setHost(ipUrl.split(":")[0]);
        tcp.setPort(Integer.parseInt(ipUrl.split(":")[1]));
        tcp.setRid(content.path("res.registrationId").toString());
        tcp.setPlat(1);

        new Thread(() -> {
            sendTcp.startConnect(tcp.getHost(),tcp.getPort(),tcp.getRid(), 1);
        }).start();
    }

    @Test
    public void test() throws Exception {

        if(apiList != null){
            for(Api api:apiList){
                String reqPath = Common.PUSH_TEST_ADDR+api.getUrl();
                String method = api.getMethod();
                if(method.toUpperCase().equals(Common.POST)){
                    String reqBody = EncryptUtils.generalEncode(api.getReqBody().toJSONString(),"push.properties");
                    response =  RestAssuredUtils.post(reqPath,reqBody,null);
                }else if(method.toUpperCase().equals(Common.GET)){
                    TransUtils.json2StrForGet(api.getReqBody());
                }
                int statusCode = response.getStatusCode();
                if(statusCode == 200 || statusCode == 500){
                    String str = response.getBody().asString();
                    String result = "";
                    if(statusCode == 200){
                        result = EncryptUtils.generalDecode(str);
                    }else {
                        result = str;
                    }
                    JSONObject res = (JSONObject) JSONObject.parse(result);
                    Set<String> resKey = res.keySet();
                    Iterator<String> it = resKey.iterator();
                    while (it.hasNext()){
                        String key = it.next();
                        if(api.getResBody_Exp().containsKey(key)){
                            Assertion.verifyEquals(api.getResBody_Exp().get(key),res.get(key),
                                    "[测试接口]："+api.getUrl()
                                            +"; [用例ID]："+api.getCaseID()
                                            +"; [验证字段]: "+"["+key+"];");
                            Reporter.log("[测试接口]："+api.getUrl()
                                    +"; [用例ID]："+api.getCaseID()
                                    +"; [预期结果]："+api.getResBody_Exp().get(key)
                                    +"; [实际结果]："+res.get(key));
                        }
                    }
                }
            }
        }
    }



    @AfterClass
    public void pass(){
        System.out.println("生成测试报告等");
    }

    @AfterTest
    public void distory(){
        System.out.println("其他处理");
    }


    public void androidContentTcp(){

    }

    public void iosContentTcp(){

    }
}

package com.mob.testcase.push;

import com.alibaba.fastjson.JSONObject;
import com.mob.common.Common;
import com.mob.utils.Assertion;
import com.mob.utils.RestAssuredUtils;
import com.mob.utils.TransUtils;
import io.restassured.response.Response;
import org.testng.Reporter;
import org.testng.annotations.*;
import com.mob.pojo.Api;
import com.mob.utils.ExcelUtils;

import java.util.*;

@Listeners({com.mob.utils.AssertionListener.class})
public class LoginCase {

    public List<Api> apiList;

    public Response dependResponse;
    public Response response;

    @Parameters({ "sheet_login" })
    @BeforeTest
    public void init(String sheet_login){
        apiList = new ExcelUtils().getExcelContent(sheet_login);
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
    public void pre(){
        System.out.println("获取依赖接口并将返回值写入全局变量");
    }

    @Test
    public void test() {

        if(apiList != null){
            for(Api api:apiList){
                String reqPath = Common.ENV_ADDR+api.getUrl();
                String method = api.getMethod();
                if(method.toUpperCase().equals(Common.POST)){
                    response =  RestAssuredUtils.post(reqPath,api.getReqBody().toJSONString(),null);
                }else if(method.toUpperCase().equals(Common.GET)){
                    TransUtils.json2StrForGet(api.getReqBody());
                }
                int statusCode = response.getStatusCode();
                if(statusCode == 200){
                    String str = response.getBody().asString();
                    JSONObject res = (JSONObject) JSONObject.parse(str);
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
}

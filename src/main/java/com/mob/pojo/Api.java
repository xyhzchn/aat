package com.mob.pojo;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.Map;

public class Api {
    private String url;
    private String method;
    private String[] argName;
    private String dependUrl;
    private int dependID;
    private String[] dependName;
    private int caseID;
    private String caseName;
    private boolean isRun;
    private Map reqHeader;
    private JSONObject reqBody;
    private JSONObject resBody_Exp;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getArgName() {
        return argName;
    }

    public void setArgName(String[] argName) {
        this.argName = argName;
    }

    public String getDependUrl() {
        return dependUrl;
    }

    public void setDependUrl(String dependUrl) {
        this.dependUrl = dependUrl;
    }

    public String[] getDependName() {
        return dependName;
    }

    public void setDependName(String[] dependName) {
        this.dependName = dependName;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public JSONObject getReqBody() {
        return reqBody;
    }

    public void setReqBody(JSONObject reqBody) {
        this.reqBody = reqBody;
    }

    public JSONObject getResBody_Exp() {
        return resBody_Exp;
    }

    public void setResBody_Exp(JSONObject resBody_Exp) {
        this.resBody_Exp = resBody_Exp;
    }

    public int getDependID() {
        return dependID;
    }

    public void setDependID(int dependID) {
        this.dependID = dependID;
    }

    public Map getReqHeader() {
        return reqHeader;
    }

    public void setReqHeader(Map reqHeader) {
        this.reqHeader = reqHeader;
    }

    @Override
    public String toString() {
        return "Api{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", argName=" + Arrays.toString(argName) +
                ", dependUrl='" + dependUrl + '\'' +
                ", dependID=" + dependID +
                ", dependName=" + Arrays.toString(dependName) +
                ", caseID=" + caseID +
                ", caseName='" + caseName + '\'' +
                ", isRun=" + isRun +
                ", reqHeader=" + reqHeader +
                ", reqBody=" + reqBody +
                ", resBody_Exp=" + resBody_Exp +
                '}';
    }
}

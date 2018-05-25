package com.ffan.qa.biz.services;

import com.ffan.qa.common.Logger;
import com.ffan.qa.network.APIRequest;
import com.ffan.qa.network.APIResponse;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public abstract class ServiceBase implements IService {
    protected  String urlBaseKey;
    protected String urlVirtualPathKey;
    private Map<String, Object> urlData;
    protected String url;
    protected String requestBody;
    protected String respBody;
    protected Map<String, String> headers;

    public ServiceBase(Map<String, Object> urlData, String baseKey, String virtualKey) {
        this.urlData = urlData;
        this.urlBaseKey = baseKey;
        this.urlVirtualPathKey = virtualKey;
        headers = new HashMap<>();
        generateUrl();
    }

    protected APIResponse httpGet() {
        Logger.log("请求地址：" + url);
        APIResponse resp = APIRequest
                .GET(url)
                .headers(headers)
                .invoke();
        this.respBody = resp.getBody();
        return resp;
    }

    protected APIResponse httpFormPost() {
        Logger.log("请求地址：" + url);
        Logger.log("请求数据：" + requestBody);
        APIResponse resp = APIRequest
                .POST(url)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .headers(headers)
                .body(requestBody)
                .invoke();
        respBody = resp.getBody();
        return resp;
    }

    protected APIResponse httpFormPut() {
        Logger.log("请求地址：" + url);
        Logger.log("请求数据：" + requestBody);
        APIResponse resp = APIRequest
                .PUT(url)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .headers(headers)
                .body(requestBody)
                .invoke();
        respBody = resp.getBody();
        return resp;
    }

    protected APIResponse httpJsonPost() {
        Logger.log("请求地址：" + url);
        Logger.log("请求数据：" + requestBody);
        APIResponse resp = APIRequest
                .POST(url)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .body(requestBody)
                .invoke();
        respBody = resp.getBody();
        return resp;
    }

    protected APIResponse httpJsonPut() {
        Logger.log("请求地址：" + url);
        Logger.log("请求数据：" + requestBody);
        APIResponse resp = APIRequest
                .PUT(url)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .headers(headers)
                .body(requestBody)
                .invoke();
        respBody = resp.getBody();
        return resp;
    }

    public ServiceBase formatUrl(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url = url.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return this;
    }

    public ServiceBase buildUrl(Map<String, String> params) {
        url += "?";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url = url + entry.getKey() + "=" + entry.getValue() + "&";
        }
        url = url.substring(0, url.length() - 1);
        return this;
    }

    private void generateUrl() {
        url = urlData.get(urlBaseKey).toString() + urlData.get(urlVirtualPathKey);
    }

    public ServiceBase setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ServiceBase setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public ServiceBase buildBody(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append((isFirst ? "" : "&") + entry.getKey() + "=" + entry.getValue());
            isFirst = false;
        }
        requestBody = sb.toString();
        return this;
    }
}

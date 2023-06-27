/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import java.awt.Cursor;
import java.io.File;
import java.io.InputStream;
import javax.swing.JComponent;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 *
 * @author Administrator
 */
public class RestAPI {

    public static final String AUTHORIZATION_KEY = "authorization";
    private JwtUtil jwtUtil;
    private JComponent component;

    public RestAPI() {
        this.jwtUtil = new JwtUtil();
    }

    public RestAPI(JComponent component) {
        this();
        this.component = component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }

    public Response uploadFile(String url, String key, File file, String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new NullPointerException("filePath must not be null or empty!");
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(MultipartEntityBuilder.create()
                .addBinaryBody(key, file, ContentType.DEFAULT_BINARY, filePath)
                .build());
        return execute(httpPost);
    }

    public Response uploadFile(String url, String key, InputStream inputStream, String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new NullPointerException("filePath must not be null or empty!");
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(MultipartEntityBuilder.create()
                .addBinaryBody(key, inputStream, ContentType.DEFAULT_BINARY, filePath)
                .build());
        return execute(httpPost);
    }

    public Response uploadFile(String url, File file, String filePath) {
        return uploadFile(url, "file", file, filePath);
    }

    public Response sendPost(String url, QueryParam param, JsonBodyAPI body) {
        return sendPost(createUrl(param, url), body);
    }

    public Response sendPost(String url, JsonBodyAPI body) {
        return sendPost(url, body.toJSONString());
    }

    public Response sendPost(String url, String body) {
        HttpPost request = new HttpPost(url);
        return requestHaveStringBody(request, body);
    }

    public Response sendPut(String url, QueryParam param, JsonBodyAPI body) {
        return sendPut(createUrl(param, url), body);
    }

    public Response sendPut(String url, JsonBodyAPI body) {
        if(body == null){
            return sendPut(url, "");
        }
        return sendPut(url, body.toJSONString());
    }

    public Response sendPut(String url, String body) {
        HttpPut request = new HttpPut(url);
        return requestHaveStringBody(request, body);
    }

    public Response sendGet(String url) {
        HttpGet request = new HttpGet(url);
        return execute(request);
    }

    public Response sendGet(String url, QueryParam param) {
        return sendGet(createUrl(param, url));
    }

    public Response sendDelete(String url) {
        HttpDelete request = new HttpDelete(url);
        return execute(request);
    }

    public Response sendDelete(String url, QueryParam param) {
        return sendDelete(createUrl(param, url));
    }

    public Response requestHaveEntity(HttpEntityEnclosingRequestBase request, HttpEntity entity) {
        request.setEntity(entity);
        return execute(request);
    }

    public Response requestHaveStringBody(HttpEntityEnclosingRequestBase request, String body) {
        try {
            return requestHaveEntity(request, new StringEntity(body));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    public void setJwtToken(String stringJWT) {
        this.jwtUtil.setJWT(stringJWT);
    }

    private synchronized Response execute(HttpUriRequest request) {
        if (component != null) {
            this.component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        }
        setDefaultHeader(request);
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();  CloseableHttpResponse response = httpClient.execute(request)) {
            String body = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            return new Response(statusCode, body);
        } catch (Exception e) {
            return new Response(-1, JsonBodyAPI.builder()
                    .put(Response.RESULT, false)
                    .put(Response.MESSAGE, e.getLocalizedMessage()).toJSONString());
        } finally {
            if (component != null) {
                this.component.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private String createUrl(QueryParam param, String url) {
        if (param != null) {
            url = String.format("%s%s", url, param);
        }
        return url;
    }

    private void setDefaultHeader(HttpUriRequest Request) {
        Request.addHeader("Content-Type", "application/json;charset=UTF-8");
        Request.addHeader("Accept", "*/*");
        Request.addHeader(AUTHORIZATION_KEY, jwtUtil.getJWT());
    }

}

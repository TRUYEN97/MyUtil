/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import java.awt.Cursor;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

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

    public static void main(String[] args) {
        FileInfo fileInfo = new FileInfo(FileInfo.type.FILE);
        fileInfo.setFile(new File("C:\\Users\\Administrator\\Desktop\\ambitconfig.txt"));
        fileInfo.setName("init/pom.xml");
        RestAPI aPI = new RestAPI();
        aPI.setJwtToken("Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJBZG1pbmlzdGF0b3IiLCJpYXQiOjE2ODgwODk1OTgsImV4cCI6MTY4ODA5MzE5OH0.6iCB4VGY5C_qJcakitHcXZfLpnc9S2NFF4e9Z29IpjI");
        Response response = aPI.uploadFile("http://localhost:8081/api/v1/file",
                JsonBodyAPI.builder().put("description", "df").toString(),
                fileInfo);
        System.out.println(response.getMessage());
    }

    public Response uploadFile(String url, String data, FileInfo... FileInfos) {
        if (data == null || data.isBlank()) {
            throw new NullPointerException("filePath must not be null or empty!");
        }
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        for (FileInfo file : FileInfos) {
            switch (file.getType()) {
                case FILE -> {
                    String name = file.getName();
                    FileBody body;
                    if (name == null) {
                        body = new FileBody((File) file.getFile(), ContentType.DEFAULT_BINARY);
                    } else {
                        body = new FileBody((File) file.getFile(), ContentType.DEFAULT_BINARY, file.getName());
                    }
                    entityBuilder.addPart("file", body);
                }
                case BYTE ->
                    entityBuilder.addBinaryBody("file", (byte[]) file.getFile(),
                            ContentType.DEFAULT_BINARY, file.getName());
                case INPUT_STREAM ->
                    entityBuilder.addBinaryBody("file", (InputStream) file.getFile(),
                            ContentType.DEFAULT_BINARY, file.getName());
                default ->
                    throw new AssertionError("invalid file type");
            }
        }
        StringBody entity = new StringBody(data, ContentType.TEXT_PLAIN);
        entityBuilder.addPart("entity", entity);
        return executeHaveMultiBody(httpPost, entityBuilder);
    }

    public Response sendPost(String url, QueryParam param, JsonBodyAPI body) {
        return sendPost(createUrl(param, url), body);
    }

    public Response sendPost(String url, JsonBodyAPI body) {
        return sendPost(url, body.toJSONString());
    }

    public Response sendPost(String url, String body) {
        HttpPost request = new HttpPost(url);
        return executeHaveStringBody(request, body);
    }

    public Response sendPut(String url, QueryParam param, JsonBodyAPI body) {
        return sendPut(createUrl(param, url), body);
    }

    public Response sendPut(String url, JsonBodyAPI body) {
        if (body == null) {
            return sendPut(url, "");
        }
        return sendPut(url, body.toJSONString());
    }

    public Response sendPut(String url, String body) {
        HttpPut request = new HttpPut(url);
        return executeHaveStringBody(request, body);
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

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    public void setJwtToken(String stringJWT) {
        this.jwtUtil.setJWT(stringJWT);
    }

    private Response executeHaveMultiBody(HttpEntityEnclosingRequestBase request, MultipartEntityBuilder entityBuilder) {
        request.setEntity(entityBuilder.build());
        request.addHeader("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>");
        return execute(request);
    }

    private Response executeHaveStringBody(HttpEntityEnclosingRequestBase request, String entity) {
        try {
            request.setEntity(new StringEntity(entity));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        request.setHeader("Content-Type", "application/json;charset=UTF-8");
        return execute(request);
    }

    private synchronized Response execute(HttpUriRequest request) {
        if (component != null) {
            this.component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        }
        try ( CloseableHttpClient httpClient = HttpClients.createDefault()) {
            request.addHeader("Accept", "*/*");
            request.addHeader(AUTHORIZATION_KEY, jwtUtil.getJWT());
            try ( CloseableHttpResponse response = httpClient.execute(request)) {
                String body = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();
                return new Response(statusCode, body);
            }
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

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONObject;
import com.tec02.gui.frameGui.Component.MyChooser;
import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.swing.JComponent;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

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

    public Response uploadFile(String url, JsonBodyAPI bodyAPI, FileInfo... FileInfos) {
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        for (FileInfo file : FileInfos) {
            switch (file.getType()) {
                case FILE -> {
                    String name = file.getName();
                    FileBody body;
                    if (name == null) {
                        body = new FileBody((File) file.getFile(), ContentType.MULTIPART_FORM_DATA);
                    } else {
                        body = new FileBody((File) file.getFile(), ContentType.MULTIPART_FORM_DATA, name);
                    }
                    entityBuilder.addPart("file", body);
                }
                case BYTE ->
                    entityBuilder.addBinaryBody("file", (byte[]) file.getFile(),
                            ContentType.MULTIPART_FORM_DATA, file.getName());
                case INPUT_STREAM ->
                    entityBuilder.addBinaryBody("file", (InputStream) file.getFile(),
                            ContentType.MULTIPART_FORM_DATA, file.getName());
                default ->
                    throw new AssertionError("invalid file type");
            }
        }
        if (bodyAPI != null) {
            for (String key : bodyAPI.getKeySet()) {
                Object value = bodyAPI.getString(key);
                if (value == null) {
                    continue;
                }
                entityBuilder.addPart(key, new StringBody(
                        String.valueOf(value), ContentType.MULTIPART_FORM_DATA));
            }
        }
        httpPost.setEntity(entityBuilder.build());
        return execute(httpPost);
    }

    public Response sendPost(String url, RequestParam param, JsonBodyAPI body) {
        return sendPost(createUrl(param, url), body);
    }

    public Response sendPost(String url, JsonBodyAPI body) {
        return sendPost(url, body.toJSONString());
    }

    public Response sendPost(String url, String body) {
        HttpPost request = new HttpPost(url);
        return executeHaveStringBody(request, body);
    }

    public Response sendPut(String url, RequestParam param, JsonBodyAPI body) {
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

    public Response sendGet(String url, RequestParam param) {
        return sendGet(createUrl(param, url));
    }

    public Response sendDelete(String url) {
        HttpDelete request = new HttpDelete(url);
        return execute(request);
    }

    public Response sendDelete(String url, RequestParam param) {
        return sendDelete(createUrl(param, url));
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    public void setJwtToken(String stringJWT) {
        this.jwtUtil.setJWT(stringJWT);
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

    public Response downloadFile(String apiUrl, RequestParam param, CreatePath createPath) {
        return RestAPI.this.downloadFile(createUrl(param, apiUrl), createPath);
    }

    public synchronized Response downloadFile(String apiUrl, CreatePath createPath) {
        try {
            if (component != null) {
                this.component.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            }
            HttpGet httpGet = new HttpGet(apiUrl);
            try ( CloseableHttpClient httpClient = HttpClients.createDefault()) {
                httpGet.addHeader("Accept", "*/*");
                httpGet.addHeader(AUTHORIZATION_KEY, jwtUtil.getJWT());
                try ( CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    Header header = response.getFirstHeader("Content-Disposition");
                    JSONObject attachment = new JSONObject();
                    if (header != null) {
                        for (HeaderElement element : header.getElements()) {
                            String name = element.getName();
                            if (name != null && name.equalsIgnoreCase("attachment")) {
                                for (NameValuePair parameter : element.getParameters()) {
                                    attachment.put(parameter.getName(), parameter.getValue());
                                }
                            }
                        }
                    }
                    File file = createPath.createFile(attachment);
                    if (file != null) {
                        file.getParentFile().mkdirs();
                        try ( InputStream inputStream = response.getEntity().getContent();
                                FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                        }
                        return new Response(response.getStatusLine().getStatusCode(),
                                JsonBodyAPI.builder()
                                        .put(Response.RESULT, true)
                                        .put(Response.MESSAGE, "")
                                        .put(Response.DATA, attachment.toJSONString())
                                        .toString());
                    }
                    return new Response(200, JsonBodyAPI.builder()
                            .put(Response.RESULT, false)
                            .put(Response.MESSAGE, "Cancel").toJSONString());
                }
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

//    public static void main(String[] args) throws IOException {
//        RestAPI aPI = new RestAPI();
//        //aPI.setJwtToken("Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJBZG1pbmlzdGF0b3IiLCJpYXQiOjE2ODg5NzA2ODIsImV4cCI6MTY4ODk3NDI4Mn0.VTwkhS54YqfsQGJr9qIrrgvFPXAxgx3x_v3F-8hP2mI");
//        Response response = aPI.downloadFileFromAPI("http://localhost:8081/api/v1/file/download?id=4&version=1.0.0", "test/test.txt");
//        System.out.println(response.getCode());
//        System.out.println((String) (response.getData()));
//        System.out.println(response.getMessage());
//    }
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

    private String createUrl(RequestParam param, String url) {
        if (param != null) {
            url = String.format("%s%s", url, param);
        }
        return url;
    }

    public interface CreatePath {

        File createFile(JSONObject jsono);

        default File autoCreateFileByAttachment(JSONObject attachment) {
            String name = attachment.getString("name");
            String dir = attachment.getString("path");
            return new File(dir, name);
        }
    }

}

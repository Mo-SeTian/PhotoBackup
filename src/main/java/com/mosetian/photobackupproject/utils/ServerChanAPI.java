package com.mosetian.photobackupproject.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ServerChanAPI {

    // 使用Server酱的API发送消息（POST请求）
    public static void sendMessage(String title, String content) throws Exception {
        // 获取名为"SERVER_API"的环境变量值
        String ServerApi = System.getenv("SERVER_API");
        String apiUrl = "https://sctapi.ftqq.com/" + ServerApi + ".send";

        // 对标题和内容进行URL编码
        String params = "title=" + URLEncoder.encode(title, StandardCharsets.UTF_8) +
                "&desp=" + URLEncoder.encode(content, StandardCharsets.UTF_8);

        // 创建URL对象
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置请求的内容类型
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 设置POST请求的内容长度
        connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));
        // 设置允许向服务器输出
        connection.setDoOutput(true);

        // 发送POST数据
        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
            writer.write(params);
            writer.flush();
        }

        // 检查响应码
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                System.out.println(response); // 打印响应内容
            }
        } else {
            System.out.println("POST请求失败，响应码：" + responseCode);
        }
    }
}
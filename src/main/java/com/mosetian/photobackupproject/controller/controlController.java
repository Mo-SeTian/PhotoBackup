package com.mosetian.photobackupproject.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.DayOfWeek;

@RestController
@RequestMapping("/info")
public class controlController {

    @GetMapping("/week")
    public String getWeek() {
        LocalDate date = LocalDate.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        // 中文星期几
        String[] weekdays = {"", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        return weekdays[dayOfWeek.getValue()];
    }

    @GetMapping("/weather")
    public String getWeather() {
        String apiKey = "429805d0b207b01a91c5de8cda28f947";
        String apiUrl = "http://apis.juhe.cn/simpleWeather/query?key=" + apiKey + "&city=南京";
        try {
            // 创建HttpClient对象
            HttpClient client = HttpClient.newHttpClient();
            // 创建HttpRequest对象
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200){
                // 输出结果
                return response.body();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

package com.example.webhook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebhookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            System.out.println("App Started...");


            RestTemplate restTemplate = new RestTemplate();

            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";


            Map<String, String> request = new HashMap<>();
            request.put("name", "Babulal Hawaldar");
            request.put("regNo", "ADT23SOCB0297");
            request.put("email", "sahil3000t@gmail.com");


            ResponseEntity<Map>  response = restTemplate.postForEntity(url, request, Map.class);


              Map body =   response.getBody();

             String webhook = (String) body.get("webhook");
            String token = (String) body.get("accessToken");

            System.out.println("Webhook URL: " + webhook);
            System.out.println("Access Token: " + token);


            String finalQuery = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) != 1 ORDER BY p.AMOUNT DESC LIMIT 1;";


             HttpHeaders headers = new HttpHeaders();
             headers.set("Authorization", token);
            headers.setContentType(MediaType.APPLICATION_JSON);


            Map<String, String> request2 = new HashMap<>();
            request2.put("finalQuery", finalQuery);


            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request2, headers);


            ResponseEntity<String> result = restTemplate.postForEntity(webhook, entity, String.class);

            System.out.println("Response : " + result.getBody());
            System.out.println("Submitted Successfully  ");
        };
    }
}
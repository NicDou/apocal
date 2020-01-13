package com.jd.apocal.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.jd.apocal.model", "com.jd.common.bean"})
@EnableTransactionManagement
@EnableDiscoveryClient
public class ApocalModelApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApocalModelApplication.class, args);
  }

}

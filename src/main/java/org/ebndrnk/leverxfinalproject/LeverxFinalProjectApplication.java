package org.ebndrnk.leverxfinalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@EnableFeignClients
public class LeverxFinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeverxFinalProjectApplication.class, args);
    }
}

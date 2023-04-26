package io.github.mengmengwang.chatgpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ChatgptWebApiApplication
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
@Slf4j
@SpringBootApplication
public class ChatgptWebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatgptWebApiApplication.class, args);
        log.info("启动完成...");
    }

}

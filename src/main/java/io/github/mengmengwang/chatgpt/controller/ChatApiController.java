package io.github.mengmengwang.chatgpt.controller;

import io.github.mengmengwang.chatgpt.beans.ChatApiResponse;
import io.github.mengmengwang.chatgpt.beans.ChatProcessRequest;
import io.github.mengmengwang.chatgpt.service.ChatHttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 聊天接口
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
@Controller
@Slf4j
public class ChatApiController {

    private final ChatHttpService httpService;

    public ChatApiController(ChatHttpService httpService) {
        this.httpService = httpService;
    }


    /**
     * session
     * <p>
     * {"status":"Success","message":"","data":{"auth":true,"model":"ChatGPTAPI"}}
     *
     * @return 响应
     */
    @CrossOrigin
    @PostMapping(path = "/api/session", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ChatApiResponse session() {
        ChatApiResponse res = new ChatApiResponse();
        HashMap<String, Object> map = new HashMap<>();
        // TODO
        map.put("auth", false);
        map.put("model", "ChatGPTAPI");
        res.setData(map);
        return res;
    }

    /**
     * verify
     * <p>
     * {"token":"abc"}
     *
     * @return 响应
     */
    @CrossOrigin
    @PostMapping(path = "/api/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ChatApiResponse verify() {
        // TODO
        return new ChatApiResponse();
    }

    /**
     * 配置
     * <p>
     * {"message":null,"data":{"apiModel":"ChatGPTAPI","reverseProxy":"-","timeoutMs":100000,"socksProxy":"-","httpsProxy":"-","balance":"-"},"status":"Success"}
     *
     * @return 响应
     */
    @CrossOrigin
    @PostMapping(path = "/api/config", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ChatApiResponse config() {
        ChatApiResponse res = new ChatApiResponse();
        HashMap<String, Object> map = new HashMap<>();
        // TODO
        map.put("auth", true);
        map.put("apiModel", "ChatGPTAPI");
        map.put("reverseProxy", "-");
        map.put("timeoutMs", 100000);
        map.put("socksProxy", "-");
        map.put("httpsProxy", "-");
        map.put("balance", "-");
        res.setData(map);
        return res;
    }

    /**
     * 聊天
     * <p>
     * {"prompt":"今天天气不错 说个天气的笑话","options":{"parentMessageId":"chatcmpl-798nYP5dijOMGJWYbL3sxqLSLTr2H"},"systemMessage":"You are ChatGPT, a large language model trained by OpenAI. Follow the user's instructions carefully. Respond using markdown."}
     * <p>
     * {"role":"assistant","id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","parentMessageId":"89f870ae-75fb-4294-8542-80b75a5f4a4e","text":"","detail":{"id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","object":"chat.completion.chunk","created":1682413186,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"role":"assistant"},"index":0,"finish_reason":null}]}}
     * {"role":"assistant","id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","parentMessageId":"89f870ae-75fb-4294-8542-80b75a5f4a4e","text":"好","delta":"好","detail":{"id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","object":"chat.completion.chunk","created":1682413186,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":"好"},"index":0,"finish_reason":null}]}}
     * {"role":"assistant","id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","parentMessageId":"89f870ae-75fb-4294-8542-80b75a5f4a4e","text":"好的","delta":"的","detail":{"id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","object":"chat.completion.chunk","created":1682413186,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":"的"},"index":0,"finish_reason":null}]}}
     * {"role":"assistant","id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","parentMessageId":"89f870ae-75fb-4294-8542-80b75a5f4a4e","text":"好的，","delta":"，","detail":{"id":"chatcmpl-798oUBlpoSmGhrwlLvw2iqWLpXExH","object":"chat.completion.chunk","created":1682413186,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":"，"},"index":0,"finish_reason":null}]}}
     */
    @CrossOrigin
    @PostMapping(path = "/api/chat-process", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void chatProcess(@RequestBody ChatProcessRequest req, HttpServletResponse response) {
        log.info("/chat-process - {}", req);
        httpService.chatCompletions(req, response);
    }

}

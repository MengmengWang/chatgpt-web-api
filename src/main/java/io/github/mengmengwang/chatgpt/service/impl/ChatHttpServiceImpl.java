package io.github.mengmengwang.chatgpt.service.impl;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import io.github.mengmengwang.chatgpt.beans.ChatProcessRequest;
import io.github.mengmengwang.chatgpt.listener.ChatHttpEventSourceListener;
import io.github.mengmengwang.chatgpt.service.ChatHttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatHttpServiceImpl
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
@Service
@Slf4j
public class ChatHttpServiceImpl implements ChatHttpService {

    private final OpenAiStreamClient client;

    public ChatHttpServiceImpl(OpenAiStreamClient client) {
        this.client = client;
    }

    @Override
    public void chatCompletions(ChatProcessRequest req, HttpServletResponse response) {
        List<Message> messages = new ArrayList<>();
        Message currentMessage = Message.builder().content(req.getPrompt()).role(Message.Role.USER).build();
        messages.add(currentMessage);
        ChatHttpEventSourceListener eventSourceListener = new ChatHttpEventSourceListener(response);
        ChatCompletion completion = ChatCompletion
                .builder()
                .messages(messages)
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .build();
        client.streamChatCompletion(completion, eventSourceListener);
        try {
            eventSourceListener.getCountDownLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

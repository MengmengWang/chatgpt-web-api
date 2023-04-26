package io.github.mengmengwang.chatgpt.service;

import io.github.mengmengwang.chatgpt.beans.ChatProcessRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * ChatHttpService
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
public interface ChatHttpService {

    /**
     * 获取gpt响应
     *
     * @param req      请求参数
     * @param response 响应对象
     */
    void chatCompletions(ChatProcessRequest req, HttpServletResponse response);
}

package io.github.mengmengwang.chatgpt.beans;

import lombok.Data;

/**
 * 响应格式封装
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
@Data
public class ChatApiResponse {

    /**
     * Success,Fail
     */
    private String status = "Success";

    /**
     * message
     */
    private String message = "";

    /**
     * 响应内容
     */
    private Object data;
}

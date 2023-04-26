package io.github.mengmengwang.chatgpt.beans;

import lombok.Data;

/**
 * ChatProcessRequest
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
@Data
public class ChatProcessRequest {

    /**
     * 用户输入内容
     */
    private String prompt;

}

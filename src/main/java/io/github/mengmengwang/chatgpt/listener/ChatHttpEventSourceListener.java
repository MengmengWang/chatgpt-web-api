package io.github.mengmengwang.chatgpt.listener;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.mengmengwang.chatgpt.constant.Constant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 接口请求监听处理
 *
 * @author wangmengmeng
 * @date 2023/4/26
 */
@Slf4j
public class ChatHttpEventSourceListener extends EventSourceListener {

    private long tokens;
    private final CountDownLatch countDownLatch;
    private final HttpServletResponse response;
    private final StringBuffer context = new StringBuffer();

    public ChatHttpEventSourceListener(HttpServletResponse response) {
        this.response = response;
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
        log.info("ChatHttp api响应数据 {}", data);
        tokens += 1;
        if (data.equals(Constant.DONE_TAG)) {
            log.info("ChatHttp api响应结束");
            return;
        }
        //转为JSON
        JSONObject dataObj = JSONUtil.parseObj(data);
        //获取响应内容
        JSONArray choices = dataObj.getJSONArray("choices");
        for (int i = 0; i < choices.size(); i++) {
            JSONObject delta = choices.getJSONObject(i).getJSONObject("delta");
            if (delta != null && delta.containsKey("content")) {
                context.append(delta.getStr("content"));
            }
        }
        //转换格式
        JSONObject obj = JSONUtil.createObj();
        obj.set("role", "assistant");
        obj.set("id", dataObj.getStr("id"));
        obj.set("text", context.toString());
        obj.set("detail", dataObj);
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            // 触发 onDownloadProgress 回调函数
            response.flushBuffer();
        } catch (IOException e) {
            log.error("ChatHttp 写入异常:", e);
        }
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.info("ChatHttp token消耗 {}", tokens - 2);
        log.info("ChatHttp 关闭连接...");
        try {
            response.getOutputStream().close();
        } catch (IOException e) {
            log.error("ChatHttp 流关闭异常:", e);
        } finally {
            countDownLatch.countDown();
        }
    }

    @SneakyThrows
    @Override
    public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {

        writeFailMsg();

        if (Objects.isNull(response)) {
            log.error("ChatHttp 连接异常:", t);
            eventSource.cancel();
            return;
        }

        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("ChatHttp sse异常:{} 异常:{}", body.string(), t);
        } else {
            log.error("ChatHttp sse连接异常:{} 异常:{}", response, t);
        }
        eventSource.cancel();
    }

    private void writeFailMsg() {
        try {
            //转换格式
            JSONObject obj = JSONUtil.createObj();
            obj.set("role", "assistant");
            obj.set("text", Constant.FAIL_MSG);
            OutputStream outputStream = this.response.getOutputStream();
            outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));
            this.response.flushBuffer();
            this.response.getOutputStream().close();
        } catch (IOException e) {
            log.error("ChatHttp 流关闭异常:", e);
        } finally {
            countDownLatch.countDown();
        }
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

}

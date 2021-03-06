package com.sunlights.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sunlights.common.MsgCode;
import com.sunlights.common.Severity;
import com.sunlights.common.vo.Message;
import com.sunlights.common.vo.MessageHeaderVo;
import com.sunlights.common.vo.MessageVo;
import play.Logger;
import play.libs.Json;

import java.util.List;

/**
 * Created by yuan on 9/22/14.
 */
public class MessageUtil {

    private String messageHeader;
    private MessageVo mesageVo;

    private MessageUtil() {
        super();
    }

    private static ThreadLocal<MessageUtil> instance = new ThreadLocal<MessageUtil>() {
        protected MessageUtil initialValue() {
            return (new MessageUtil());
        }
    };

    public static MessageUtil getInstance() {
        return instance.get();
    }

    public void setMessage(Message message) {
        mesageVo = new MessageVo(message);
    }

    public void setMessage(Message message, Object value) {
        setMessage(message);
        mesageVo.setValue(value);
    }

    public String setMessageHeader(List<MessageHeaderVo> value) {
        Message message = mesageVo == null ? new Message(Severity.ERROR, MsgCode.OPERATE_FAILURE) : mesageVo.getMessage();
        ObjectNode json = Json.newObject();
        json.put("message", message.getSeverity());
        json.put("headerValue", Json.toJson(value));
        messageHeader = json.toString();
        Logger.info(json.toString());
        return messageHeader;
    }

    public String getMessageHeader(){
        return messageHeader;
    }

    public JsonNode toJson() {
        return Json.toJson(mesageVo);
    }

    public JsonNode msgToJson(Message message, Object value) {
        setMessage(message, value);
        return toJson();
    }

    public JsonNode msgToJson(Message message) {
        setMessage(message);
        return toJson();
    }

}

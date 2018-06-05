package edu.njpi.rj1621.action.form;

/**
 * @author Fleming
 * 消息封装的数据传输对象
 */
public class MessageDto {

    private String messageType;
    private String data;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

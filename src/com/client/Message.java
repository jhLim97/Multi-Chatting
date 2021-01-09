package com.client;

public class Message {

    private String id;
    private String passwd;
    private String msg;
    private String type; // 메세지 유형(로그인, 로그아웃, 메세지 전달, 시크릿 모드 등)
    private String target;

    public Message() {
        id = "";
        passwd = "";
        msg = "";
        type = "";
        target = "";
    }

    public Message(String id, String passwd, String msg, String type) {
        this.id = id;
        this.passwd = passwd;
        this.msg = msg;
        this.type = type;
    }

    public Message(String id, String passwd, String msg, String type, String target) {
        this.id = id;
        this.passwd = passwd;
        this.msg = msg;
        this.type = type;
        this.target = target;
    }

    public String getType() { return type; }
    public String getId() { return id; }
    public String getMsg() { return msg; }
    public String getTarget() { return target; }

}

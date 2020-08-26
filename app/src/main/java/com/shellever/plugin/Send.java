package com.shellever.plugin;

import android.content.*;

class Send {
    private Context context;
    private Intent intent;
    private Long groupID;
    private Long qq;
    private String nick;
    private Long slqq;

    Send(Context context, Intent i, Long groupID, String nick, long qq) {
        this.context = context;
        this.intent = i;
        this.groupID = groupID;
        this.nick = nick;
        this.qq = qq;

    }

    void s(String message) {
        intent.putExtra("groupid", groupID);// 发送群号参数
        intent.putExtra("message", message);// 发送msg消息参数
        intent.putExtra("img", "");
        intent.putExtra("type", 13L);// type类型
        context.sendBroadcast(intent);
    }
    //私聊
    void sl(String message,Long slqq) {
        intent.putExtra("qq", qq);// 发送群号参数
        intent.putExtra("message", message);// 发送msg消息参数
        intent.putExtra("img", "");
        intent.putExtra("type", 13L);// type类型
        context.sendBroadcast(intent);
    }
}

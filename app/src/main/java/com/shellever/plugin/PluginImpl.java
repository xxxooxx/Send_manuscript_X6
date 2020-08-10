package com.shellever.plugin;

import android.content.*;

public class PluginImpl implements PluginIf {
    @Override
    public String Message(final Context context, final Intent it,
                          final Long groupid, final Long sendid,
                          final String nick, final Long ATQ, final String msg, final String KEY) {
        final Send s = new Send(context, it, groupid, nick, sendid);
        new Thread(new Runnable() {// 发送端口
            @Override
            public void run() {
                new Main(s, groupid, msg, nick, sendid, ATQ,KEY).processMsg();
            }
        }).start();
        return "";// 返回端口
    }
}

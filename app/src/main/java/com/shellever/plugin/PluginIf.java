package com.shellever.plugin;

import android.content.Context;
import android.content.Intent;

public interface PluginIf {
    String Message(final Context context, final Intent it,
                   final Long groupid, final Long sendid,
                   final String nick, final Long ATQ, final String msg, String KEY);
}

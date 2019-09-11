package com.nova.umeng_plugin;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class UmengPlugin implements MethodCallHandler {

    private Activity activity;

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "umeng_plugin");
        channel.setMethodCallHandler(new UmengPlugin(registrar.activity()));
    }


    public UmengPlugin(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        if (call.method.equals("init")) {
            init(call, result);
        } else if (call.method.equals("event")) {
            event(call, result);
        } else if (call.method.equals("eventMap")) {
            eventMap(call, result);
        } else if (call.method.equals("beginLogPageView")) {
            beginLogPageView(call, result);
        } else if (call.method.equals("endLogPageView")) {
            endLogPageView(call, result);
        } else if (call.method.equals("onResume")) {
            onResumeActivity(call, result);
        } else if (call.method.equals("onPause")) {
            onPauseActivity(call, result);
        } else {
            result.notImplemented();
        }
    }


    public void init(MethodCall call, Result result) {
        boolean logEnable = false;
        if (call.hasArgument("logEnable")) {
            logEnable = (boolean) call.argument("logEnable");
        }
        UMConfigure.setLogEnabled(logEnable);
        String channel = "flutter";
        if (call.hasArgument("channel")) {
            channel = (String) call.argument("channel");
        }
        UMConfigure.init(activity, (String) call.argument("appKey"), channel, UMConfigure.DEVICE_TYPE_PHONE, null);
        boolean encrypt = false;
        if (call.hasArgument("encrypt")) {
            encrypt = (boolean) call.argument("encrypt");
        }
        UMConfigure.setEncryptEnabled(encrypt);
        MobclickAgent.openActivityDurationTrack(false);
        result.success(true);

    }

    public void event(MethodCall call, Result result) {
        if (call.hasArgument("label")) {
            String label = (String) call.argument("label");
            MobclickAgent.onEvent(activity, (String) call.argument("eventId"), label);
        } else {
            MobclickAgent.onEvent(activity, (String) call.argument("eventId"));
        }
        result.success(true);
    }

    private void eventMap(MethodCall call, Result result) {
        if (call.hasArgument("map")) {
            Map<String, String> map = call.argument("map");
            MobclickAgent.onEvent(activity, (String) call.argument("eventId"), map);
        }
        result.success(true);

    }

    public void beginLogPageView(MethodCall call, Result result) {
        MobclickAgent.onPageStart((String) call.argument("pageName"));
        result.success(true);
    }

    public void endLogPageView(MethodCall call, Result result) {
        MobclickAgent.onPageEnd((String) call.argument("pageName"));
        result.success(true);
    }

    public void onResumeActivity(MethodCall call, Result result) {
        MobclickAgent.onPause(activity);
        result.success(true);
    }

    public void onPauseActivity(MethodCall call, Result result) {
        MobclickAgent.onPause(activity);
        result.success(true);
    }

}

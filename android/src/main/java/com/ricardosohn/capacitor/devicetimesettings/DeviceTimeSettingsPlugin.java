package com.ricardosohn.capacitor.devicetimesettings;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "DeviceTimeSettings")
public class DeviceTimeSettingsPlugin extends Plugin {

    private BroadcastReceiver timeChangeReceiver;
    private ContentObserver settingsObserver;
    private boolean observing;

    @PluginMethod
    public void getAutomaticTimeStatus(PluginCall call) {
        call.resolve(buildAutomaticTimeStatus());
    }

    @PluginMethod
    public void startObserving(PluginCall call) {
        if (observing) {
            call.resolve();
            return;
        }

        observing = true;

        Handler mainHandler = new Handler(Looper.getMainLooper());

        timeChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notifyListeners("timeSettingsChanged", buildAutomaticTimeStatus());
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        getContext().registerReceiver(timeChangeReceiver, filter);

        settingsObserver = new ContentObserver(mainHandler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                notifyListeners("timeSettingsChanged", buildAutomaticTimeStatus());
            }
        };

        getContext().getContentResolver().registerContentObserver(
                Settings.Global.getUriFor(Settings.Global.AUTO_TIME),
                false,
                settingsObserver
        );

        getContext().getContentResolver().registerContentObserver(
                Settings.Global.getUriFor(Settings.Global.AUTO_TIME_ZONE),
                false,
                settingsObserver
        );

        notifyListeners("timeSettingsChanged", buildAutomaticTimeStatus());
        call.resolve();
    }

    @PluginMethod
    public void stopObserving(PluginCall call) {
        stopObserversInternal();
        call.resolve();
    }

    @PluginMethod
    public void openDateSettings(PluginCall call) {
        try {
            Intent dateSettingsIntent = new Intent(Settings.ACTION_DATE_SETTINGS);
            dateSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(dateSettingsIntent);
            call.resolve();
        } catch (ActivityNotFoundException dateSettingsError) {
            try {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(settingsIntent);
                call.resolve();
            } catch (Exception settingsError) {
                call.reject("Unable to open Date & Time settings", settingsError);
            }
        } catch (Exception error) {
            call.reject("Failed to open settings", error);
        }
    }

    @Override
    protected void handleOnDestroy() {
        stopObserversInternal();
        super.handleOnDestroy();
    }

    private void stopObserversInternal() {
        if (timeChangeReceiver != null) {
            try {
                getContext().unregisterReceiver(timeChangeReceiver);
            } catch (IllegalArgumentException ignored) {
            }
            timeChangeReceiver = null;
        }

        if (settingsObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(settingsObserver);
            settingsObserver = null;
        }

        observing = false;
    }

    private JSObject buildAutomaticTimeStatus() {
        JSObject result = new JSObject();

        int autoTime = Settings.Global.getInt(
                getContext().getContentResolver(),
                Settings.Global.AUTO_TIME,
                0
        );
        int autoTimeZone = Settings.Global.getInt(
                getContext().getContentResolver(),
                Settings.Global.AUTO_TIME_ZONE,
                0
        );

        result.put("autoTime", autoTime == 1);
        result.put("autoTimeZone", autoTimeZone == 1);
        return result;
    }
}

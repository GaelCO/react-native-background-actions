package com.asterinet.react.bgactions;

import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;

public final class BackgroundTaskOptions {
    private final Bundle extras;

    public BackgroundTaskOptions(@NonNull final Bundle extras) {
        this.extras = extras;
    }

    public BackgroundTaskOptions(@NonNull final ReactContext reactContext, @NonNull final ReadableMap options) {
        // Create extras
        extras = Arguments.toBundle(options);
        if (extras == null)
            throw new IllegalArgumentException("Could not convert arguments to bundle");
        // Get taskTitle
        try {
            if (options.getString("taskTitle") == null)
                throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Task title cannot be null");
        }
        // Get taskDesc
        try {
            if (options.getString("taskDesc") == null)
                throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Task description cannot be null");
        }
        // Get iconInt
        try {
            final ReadableMap iconMap = options.getMap("taskIcon");
            if (iconMap == null)
                throw new IllegalArgumentException();
            final String iconName = iconMap.getString("name");
            final String iconType = iconMap.getString("type");
            String iconPackage;
            try {
                iconPackage = iconMap.getString("package");
                if (iconPackage == null)
                    throw new IllegalArgumentException();
            } catch (Exception e) {
                // Get the current package as default
                iconPackage = reactContext.getPackageName();
            }
            final int iconInt = reactContext.getResources().getIdentifier(iconName, iconType, iconPackage);
            extras.putInt("iconInt", iconInt);
            if (iconInt == 0)
                throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Task icon not found");
        }
        // Get color
        try {
            final String color = options.getString("color");
            extras.putInt("color", Color.parseColor(color));
        } catch (Exception e) {
            extras.putInt("color", Color.parseColor("#ffffff"));
        }
    }

    public Bundle getExtras() {
        return extras;
    }

    public String getTaskTitle() {
        return extras.getString("taskTitle", "");
    }

    public String getTaskDesc() {
        return extras.getString("taskDesc", "");
    }

    @IdRes
    public int getIconInt() {
        return extras.getInt("iconInt");
    }

    @ColorInt
    public int getColor() {
        return extras.getInt("color");
    }

    @Nullable
    public String getLinkingURI() {
        return extras.getString("linkingURI");
    }

    @Nullable
    public Bundle getProgressBar() {
        return extras.getBundle("progressBar");
    }

    public int getForegroundServiceType() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return 0;
        }
        final String type = extras.getString("foregroundServiceType", "dataSync");
        switch (type) {
            case "mediaPlayback":
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK;
            case "phoneCall":
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL;
            case "location":
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;
            case "connectedDevice":
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE;
            case "mediaProjection":
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION;
            case "camera":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            case "microphone":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            case "health":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            case "remoteMessaging":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            case "systemExempted":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            case "shortService":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            case "specialUse":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    return ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE;
                }
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            default: // "dataSync"
                return ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
        }
    }
}

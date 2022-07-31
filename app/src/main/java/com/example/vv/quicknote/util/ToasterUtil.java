package com.example.vv.quicknote.util;

import android.content.Context;
import android.widget.Toast;

import com.example.vv.quicknote.BuildConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vivek Verma
 * @since 28/3/21
 */
public class ToasterUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToasterUtil.class);

    private static void toaster(Context context, String msg, Integer duration) {
        if (BuildConfig.DEBUG) LOGGER.info(msg);
        Toast.makeText(context, msg, duration).show();
    }

    public static void shortToaster(Context context, String msg) {
        toaster(context, msg, Toast.LENGTH_SHORT);
    }

    public static void longToaster(Context context, String msg) {
        toaster(context, msg, Toast.LENGTH_LONG);
    }
}
package com.ethan.common.tools;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.ResultReceiver;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Ui related methods
 */
public class UiTools {
    private static int STATUS_BAR_HEIGHT = -1;

    /**
     * get the height of status bar
     * @param activity
     * @return height of status bar
     */
    public static int getStatusBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && STATUS_BAR_HEIGHT == -1) {
            try {
                final Resources res = activity.getResources();

                // 尝试获取status_bar_height这个属性的Id对应的res int value；
                int resourceId = res
                        .getIdentifier("status_bar_height", "dimen", "android");

                if (resourceId <= 0) {
                    Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                    Object object = clazz.newInstance();
                    resourceId = Integer.parseInt(clazz.getField("status_bar_height")
                            .get(object).toString());
                }

                // 如果拿到了就直接调用，获取
                if (resourceId > 0) {
                    STATUS_BAR_HEIGHT = res.getDimensionPixelSize(resourceId);
                } else {
                    // 通过window拿到
                    Rect rect = new Rect();
                    Window window = activity.getWindow();
                    // 拿到根布局的可见区域
                    window.getDecorView().getWindowVisibleDisplayFrame(rect);
                    STATUS_BAR_HEIGHT = rect.top;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return STATUS_BAR_HEIGHT;
    }
    public static int getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


}

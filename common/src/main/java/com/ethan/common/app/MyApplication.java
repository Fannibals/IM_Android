package com.ethan.common.app;

import android.app.Application;
import android.os.SystemClock;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.StringRes;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 外部获取单例
     *
     * @return Application
     */
    public static MyApplication getInstance() {
        return instance;
    }



    /**
     * 获取缓存文件夹地址
     * @return 当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile(){
        return instance.getCacheDir();
    }

    public static File getPortraitTmpFile(){
        // 得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(),"portrait");

        // 创建所有的对应的文件夹
        dir.mkdirs();

        // 删除旧的一些缓存行为文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for(File file:files) {
                file.delete();
            }
        }

        File path = new File(dir, SystemClock.uptimeMillis()+ ".jpg");
        return path.getAbsoluteFile();
    }

    /**
     * 获取声音文件的本地地址
     *
     * @param isTmp 是否是缓存文件， True，每次返回的文件地址是一样的
     * @return 录音文件的地址
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                boolean isTempFile = file.getName().lastIndexOf("tmp.mp3") != -1;
                if (isTmp != isTempFile) {
                    continue;
                }
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // aar
        // 避免用户重复录音&取消而产生一大推垃圾文件
        File path = new File(dir, isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }


    /**
     * 显示一个Toast
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        // Toast 只能在主线程中显示，所有需要进行线程转换，
        // 保证一定是在主线程进行的show操作
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示一个Toast
     *
     * @param msgId 传递的是字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }
}

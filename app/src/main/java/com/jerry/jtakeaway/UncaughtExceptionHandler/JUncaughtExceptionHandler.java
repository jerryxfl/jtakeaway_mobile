package com.jerry.jtakeaway.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class JUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private static JUncaughtExceptionHandler myCrashHandler;

    private JUncaughtExceptionHandler(){}

    public static synchronized JUncaughtExceptionHandler getInstance() {
        if(myCrashHandler == null)
            myCrashHandler = new JUncaughtExceptionHandler();
        return myCrashHandler;
    }

    /**
     * 初始化
     */
    public void init(Context context){
        mContext = context;
        //系统默认处理类
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该类为系统默认处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }



    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(!handleExample(e) && mDefaultHandler != null) { //判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 提示用户出现异常
     * 将异常信息保存
     */
    private boolean handleExample(Throwable ex) {
        if(ex == null)
            return false;

        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();

        //手机设备参数信息
        collectDeviceInfo(mContext);
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * 设备信息
     */
    private void collectDeviceInfo(Context mcontext) {


    }


    /**
     * 保存错误信息到文件中
     */
    private void saveCrashInfoToFile(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable exCause = ex.getCause();
        while (exCause != null) {
            exCause.printStackTrace(printWriter);
            exCause = exCause.getCause();
        }
        printWriter.close();

        long timeMillis = System.currentTimeMillis();
        //错误日志文件名称
        String fileName = "crash-" + timeMillis + ".log";
        //判断sd卡可正常使用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件存储位置
            String path = Environment.getExternalStorageDirectory().getPath() + "/crash_logInfo/";
            File fl = new File(path);
            //创建文件夹
            if(!fl.exists()) {
                fl.mkdirs();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                fileOutputStream.write(writer.toString().getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


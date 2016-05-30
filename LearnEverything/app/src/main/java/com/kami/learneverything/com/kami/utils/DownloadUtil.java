package com.kami.learneverything.com.kami.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kami on 16/5/10.
 */

public class DownloadUtil {

    private static final int START = 1; // 开始下载
    private static final int PUBLISH = 2; // 更新进度
    private static final int ERROR = 3; // 下载错误
    private static final int SUCCESS = 4; // 下载成功

    private OnDownloadListener mListener; // 监听器

    private int count = 0;
    private String filename;

    public static String path = "http://www.gduf.edu.cn/downloadFile.jsp?link=mail/annex/2016/05/09/TXCB2016050916164115&fileName=关于在全省大学生中开展“党中央治国理政新理念新思想新战略知识竞赛”活动的通知.docx";

    //创建线程池
    private Executor threadPool = Executors.newFixedThreadPool(3);

    //负责下载的线程
    static class DownloadRunnable implements Runnable{

        private String url;
        private String fileName;

        private long startIndex;
        private long endIndex;

        private Handler handler;

        public DownloadRunnable(String url, String fileName, long startIndex, long endIndex,Handler handler) {
            this.url = url;
            this.fileName = fileName;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.handler = handler;
        }

        @Override
        public void run() {

            try {
                URL httpUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
                connection.setReadTimeout(20000);
                //设置该线程负责下载的区间
                connection.setRequestProperty("Range","bytes="+startIndex+"-"+endIndex);
                connection.setRequestMethod("GET");

                //访问本地文件
                RandomAccessFile accessFile = new RandomAccessFile(new File(fileName),"rwd");
                accessFile.seek(startIndex);
                InputStream in = connection.getInputStream();
                byte[] b = new byte[1024*4];
                int len = 0;
                while ((len=in.read(b))!=-1){
                    accessFile.write(b,0,len);
                }
                if (accessFile!=null){
                    accessFile.close();
                }
                if (in!=null){
                    in.close();
                }

                //下载完成后发送消息
                Message msg = handler.obtainMessage();
                msg.what = PUBLISH;
                handler.sendMessage(msg);

            } catch (MalformedURLException e) {
               error(handler,fileName);
                e.printStackTrace();
            } catch (IOException e) {
                error(handler,fileName);
                e.printStackTrace();
            }

        }
    }

    public void start(final String url){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case START:
                        mListener.onStart();
                        break;
                    case PUBLISH:
                        mListener.onPublish();
                        count++;
                        break;
                    case ERROR:
                        mListener.onError();
                        break;
                }
                if (count == 3){
                    mListener.onSuccess();
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFile(url,handler);
            }
        }).start();

    }

    private void downloadFile(String url,Handler handler){

        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setReadTimeout(20000);
            connection.setRequestMethod("GET");

            filename = getFilename(url);
            File path = Environment.getExternalStorageDirectory();
            File fileDownload = new File(path+"/myDownload",filename);

            //获取文件长度
            int count = connection.getContentLength();

            if (count>=0){
                Message msg = handler.obtainMessage();
                msg.what = START;
                handler.sendMessage(msg);
            }

            int block = count/3;
            for (int i = 0;i<3;i++){
                long start = i*block;
                long end = (i+1)*block-1;
                if (i == 2){
                    end = count;
                }
                DownloadRunnable runnable = new DownloadRunnable(url,fileDownload.getAbsolutePath(),start,end,handler);
                threadPool.execute(runnable);
            }

        } catch (MalformedURLException e) {
           error(handler,filename);
            e.printStackTrace();
        } catch (IOException e) {
            error(handler,filename);
            e.printStackTrace();
        }

    }

    //监听者接口
    public interface OnDownloadListener {

        public void onStart(); // 回调开始下载

        public void onPublish(); // 回调更新进度

        public void onSuccess(); // 回调下载成功

        public void onError(); // 回调下载出错

    }

    //设置监听器
    public DownloadUtil setOnDownloadListener(OnDownloadListener listener) {
        mListener = listener;
        return this;
    }


    //提取文件名
    public String getFilename(String url) {
        String name = "";
        Log.i("filename str = ", url);
        name = url.substring(url.indexOf("Name=") + 5, url.length());
        return name;
    }


    public static void error(Handler handler,String filename){
        Log.i("onError ", filename+"已删除");
        new File(filename).delete();
        Message msg = handler.obtainMessage();
        msg.what = ERROR;
        handler.sendMessage(msg);
    }



}

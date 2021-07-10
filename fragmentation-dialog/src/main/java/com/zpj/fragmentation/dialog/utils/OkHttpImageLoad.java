package com.zpj.fragmentation.dialog.utils;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.zpj.utils.ContextUtils;
import com.zpj.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by liuting on 16/8/26.
 */
public class OkHttpImageLoad {

    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26;

    private static String IMAGE_CACHE_PATH;

    private volatile static OkHttpImageLoad mInstance;
    private final ConcurrentHashMap<String, Builder> map = new ConcurrentHashMap<>();
    private final Handler handler;

    private OkHttpImageLoad() {
        IMAGE_CACHE_PATH = ContextUtils.getApplicationContext().getExternalCacheDir().getAbsolutePath() + "/thumbnail_cache";
        File file = new File(IMAGE_CACHE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpImageLoad getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpImageLoad.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpImageLoad();
                }
            }
        }
        return mInstance;
    }

    /**
     * 加载图片
     *
     * @param url
     * @param listener
     */
    public void load(String url, ImageDownLoadListener listener) {
        if (TextUtils.isEmpty(url)) {
            listener.onError(new Exception("The link is null"));
            return;
        }
        Builder builder = null;
        if (map.containsKey(url)) {
            builder = map.get(url);
        } else if (checkImageExists(url)) {
            //没有发现正在下载，检验是否已经下载过了
            listener.onSuccess();
            return;
        }
        if (builder == null) {
            builder = new Builder(url, handler);
            map.put(url, builder);
        }
        builder.listener(listener);
        builder.start();
    }

    public static String getImageCachePath() {
        getInstance();
        return IMAGE_CACHE_PATH;
    }

    public static String getCachedPath(String url) {
        String key = generate(url);
        return getImageCachePath() + "/" + key;
    }

    /**
     * 判断图片是否已经存在
     *
     * @param url
     * @return
     */
    public boolean checkImageExists(String url) {
        String key = generate(url);
        String destUrl = IMAGE_CACHE_PATH + "/" + key;
        File file = new File(destUrl);
        if (file.exists()) {
            int size = getMaxSizeOfBitMap(destUrl);
            if (size > 0) {
                return true;
            } else {
                file.delete();
                return false;
            }
        }
        return false;
    }

    public static String generate(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        BigInteger bi = new BigInteger(md5).abs();
        if (imageUri.toLowerCase().endsWith(".gif")) {
            return bi.toString(RADIX) + ".itgif";
        }
        return bi.toString(RADIX) + ".it";
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return hash;
    }

    public static int getMaxSizeOfBitMap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        return Math.max(op.outWidth, op.outHeight);
    }

    /**
     * 解绑监听器,实际下载还在后台进行
     *
     * @param url
     * @param listener
     */
    public void cancel(String url, ImageDownLoadListener listener) {
        if (map.containsKey(url)) {
            Builder builder = map.get(url);
            if (builder != null) {
                builder.removeListener(listener);
            }
        }
    }

    /**
     * 取消下载图片
     *
     * @param url
     * @param listener
     */
    public void destroy(String url, ImageDownLoadListener listener) {
        if (map.containsKey(url)) {
            Builder builder = map.get(url);
            if (builder != null) {
                map.remove(url);
                builder.cancel();
                builder.removeListener(listener);
            }
        }
    }

    public static class Builder {
        private final Handler handler;
        protected String url;
        private final List<ImageDownLoadListener> imageDownLoadListener = new ArrayList<>();
        private boolean isSuccess = false;
        private boolean isStarted = false;
        private float currentProgress = 0f;
        private long total = 0L;
        private State currentState = State.DOWNLOADING;
        private Thread thread;

        private enum State {
            DOWNLOADING, DOWNLOADERROR, DOWNLOADFINISH
        }

        public Builder(String url, Handler handler) {
            this.url = url;
            this.handler = handler;
        }

        public Builder listener(ImageDownLoadListener listener) {
            if (!imageDownLoadListener.contains(listener))
                imageDownLoadListener.add(listener);
            return this;
        }

        public void cancel() {
            if (!isSuccess) {
                //切换到非UI线程，进行网络的取消工作
                if (thread != null) {
                    thread.interrupt();
                }
                downloadCancel();
            }
        }

        private void execute() {
            isStarted = true;
            currentState = State.DOWNLOADING;

            thread = new Thread(() -> {

                try {
                    URL link = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) link.openConnection();
                    int count = 0;
                    while (connection.getResponseCode() / 100 == 3) {
                        if (count > 20) {
                            downloadFail(new ConnectException("too many redirect connection!"));
                            return;
                        }
                        connection = (HttpURLConnection) new URL(connection.getHeaderField("location")).openConnection();
                        count++;
                    }
                    if (connection.getResponseCode() / 100 != 2) {
                        downloadFail(new ConnectException("fail to open connection!"));
                        return;
                    }
                    long totalSize = Long.parseLong(connection.getHeaderField("content-length"));
                    InputStream is = connection.getInputStream();
                    String key = generate(url);
                    String destUrl = getImageCachePath() + "/" + key;
                    OutputStream os = new FileOutputStream(destUrl);

                    byte[] bytes = new byte[1024];
                    int len = 0;
                    long progress = 0;
                    while ((len = is.read(bytes)) != -1) {
                        os.write(bytes, 0, len);
                        progress += len;
                        refreshProgress((float) progress / totalSize, totalSize);
                    }
                    downloadSuccess();
                } catch (IOException e) {
                    e.printStackTrace();
                    downloadFail(e);
                }

//                try {
//                    File file = Glide.with(ContextUtils.getApplicationContext())
//                            .asFile()
////                                .downloadOnly()
//                            .load(url)
//                            .submit()
//                            .get();
//                    String key = generate(url);
//                    String destUrl = getImageCachePath() + "/" + key;
//                    File newFile = new File(destUrl);
//                    FileUtils.copyFileFast(file, newFile);
//                    downloadSuccess();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    downloadFail(e);
//                }
            });
            thread.start();
        }

        /**
         * 如果已经开启就不再执行网络加载操作
         */
        public void start() {
            checkState();
            if (!isStarted) {
                execute();
            }
        }

        private void checkState() {
            switch (currentState) {
                case DOWNLOADING:
                    refreshProgress(currentProgress, total);
                    break;
                case DOWNLOADFINISH:
                    downloadSuccess();
            }
        }

        private void downloadCancel() {
            handler.post(() -> {
                for (ImageDownLoadListener listener : imageDownLoadListener)
                    listener.onCancel();
            });
        }

        private void refreshProgress(final float progress, final long total) {
            this.currentProgress = progress;
            this.total = total;
            handler.post(() -> {
                for (ImageDownLoadListener listener : imageDownLoadListener)
                    listener.inProgress(progress, total);
            });
        }

        private void downloadFail(final Throwable e) {
            e.printStackTrace();
            currentState = State.DOWNLOADERROR;
            String key = generate(url);
            String destUrl = getImageCachePath() + "/" + key;
            File file = new File(destUrl);
            if (file.exists()) file.delete();
            if (imageDownLoadListener.size() == 0) {
                //发现没有绑定任何监听，自动移除当前build
                mInstance.map.remove(url);
                return;
            }
            handler.post(() -> {
                for (ImageDownLoadListener listener : imageDownLoadListener)
                    listener.onError(e);
            });
        }

        private void downloadSuccess() {
            isSuccess = true;
            currentState = State.DOWNLOADFINISH;
            if (imageDownLoadListener.size() == 0) {
                //发现没有绑定任何监听，自动移除当前build
                mInstance.map.remove(url);
                return;
            }
            handler.post(() -> {
                for (ImageDownLoadListener listener : imageDownLoadListener)
                    listener.onSuccess();
            });
        }

        public void removeListener(ImageDownLoadListener listener) {
            imageDownLoadListener.remove(listener);
            if (imageDownLoadListener.size() == 0 && currentState == State.DOWNLOADFINISH) {
                mInstance.map.remove(url);
            }
        }
    }


    public interface ImageDownLoadListener {
        void inProgress(float progress, long total);

        void onError(Throwable e);

        void onSuccess();

        void onCancel();
    }

}
package com.redread.utils;

import android.support.annotation.NonNull;

import com.redread.MyApplication;
import com.redread.model.entity.DownLoad;
import com.redread.model.gen.DownLoadDao;
import com.redread.net.Api;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangshexin on 2018/11/7.
 */

public class DownLoadThread {

    private static DownLoadThread instance;

    public static synchronized DownLoadThread getInstanc() {
        if (instance == null) {
            instance = new DownLoadThread();
        }
        return instance;
    }

    /**
     * 是否有任务在进行，如果有则不用管，没有需要启动
     */
    private boolean ing = false;
    private DownLoadDao dao;

    public DownLoadThread() {
        dao = MyApplication.getInstances().getDaoSession().getDownLoadDao();
    }

    /**
     * 执行下载
     */
    public void downLoad(@NonNull DownLoad task) {
        insertOneTask(task);
        if (ing)
            return;
        ing = true;
        new Thread() {
            @Override
            public void run() {
                DownLoad task = getLastedTask();
                do {

                    try {
                        //1下载图片
                        String coverUrlStr = Api.downUrl + task.getCoverUrl();
                        URL coverUrl = new URL(coverUrlStr);
                        URLConnection connection = coverUrl.openConnection();
                        InputStream coverIns = connection.getInputStream();
                        long coverLongth = coverIns.available();
                        if (coverLongth > 0) {
                            //下图片
                            File picDir = new File(Constant.picture);
                            if (!picDir.isDirectory()) {
                                picDir.mkdirs();
                            }
                            File coverFile = new File(picDir, task.getCoverUrl());
                            //如果存在判断一下大小是不是一样，一样就不下了
                            if (coverFile.exists() && coverFile.length() != coverLongth) {
                                coverFile.delete();
                            }
                            if (!coverFile.exists()) {
                                FileOutputStream coverFos = new FileOutputStream(coverFile);
                                byte buf[] = new byte[1024];
                                int downLoadFileSize = 0;
                                do {
                                    //循环读取
                                    int numread = coverIns.read(buf);
                                    if (numread == -1) {
                                        break;
                                    }
                                    coverFos.write(buf, 0, numread);
                                    downLoadFileSize += numread;
                                    //更新进度条
                                } while (true);
                                coverFos.flush();
                                coverFos.close();
                            }
                            coverIns.close();
                        }
                        //2、下载图书
                        String bookUrlStr = Api.downUrl + task.getUrl();
                        File bookDir = new File(Constant.bookPath);
                        if (!bookDir.isDirectory()) {
                            bookDir.mkdirs();
                        }
                        File bookFile = new File(bookDir, task.getUrl());
                        RandomAccessFile bookRWDFile = new RandomAccessFile(bookFile, "rwd");
                        long totalLength = task.getDataLongth();//文件长度
                        long currentLength = 0;//已下载的长度
                        if (bookFile.exists()) {
                            currentLength = bookFile.length();
                        }
                        URL bookUrl = new URL(bookUrlStr);
                        HttpURLConnection bookUrlConnection = (HttpURLConnection) bookUrl.openConnection();
                        bookUrlConnection.setRequestProperty("Range", "bytes=" + currentLength + "-" + totalLength);//设置下载范围
                        bookRWDFile.seek(currentLength);//指向上次的位置
                        InputStream bookIns = bookUrlConnection.getInputStream();
                        if (bookIns.available() > 0) {
                            byte[] buffer = new byte[1024 * 4];
                            int len;
                            boolean isInterrupt=false;//是否打断了当前的循环,对在下载过程中用户手动操作处理
                            while ((len = bookIns.read(buffer)) != -1&&!isInterrupt) {
                                //写入文件
                                bookRWDFile.write(buffer, 0, len);
                                currentLength += len;
                                task.setDownProgress(currentLength);
                                //查一下状态是不是用户给暂停了或着删除了，进行相应的处理

                                DownLoad resTask = dao.load(task.getId());//查找到表中的记录
                                resTask.setDownProgress(currentLength);
                                resTask.setBookDir(Constant.bookPath+resTask.getUrl());//写入封面的本地路径
                                resTask.setCoverDir(Constant.picture+resTask.getCoverUrl());//写入文件的本地路径

                                switch (resTask.getStatus()) {
                                    case Constant.DOWN_STATUS_WAIT://等待下载
                                    case Constant.DOWN_STATUS_ING://下载中
                                        task.setStatus(Constant.DOWN_STATUS_ING);
                                        //状态正常需更新进度
                                        if (totalLength == currentLength) {
                                            //下完了，更新状态
                                            task.setStatus(Constant.DOWN_STATUS_SUCCESS);
                                        }
                                        //更新
                                        dao.update(resTask);
                                        break;
                                    case Constant.DOWN_STATUS_PAUS://暂停了
                                        isInterrupt=true;
                                        //更新
                                        dao.update(resTask);
                                        break;
                                    case Constant.DOWN_STATUS_FAILE://失败了
                                        //理论上这种状态不用处理，需要用户手动处理
                                        break;
                                    case Constant.DOWN_STATUS_CLEAR://删除了
                                        //用户在书架用了删除，当时可能正在下载中所以在这里要作删除
                                        isInterrupt=true;
                                        dao.deleteByKey(task.getId());
                                        bookRWDFile.close();
                                        bookIns.close();
                                        bookFile.delete();
                                        break;
                                }

                            }
                            //下载完成
                            bookRWDFile.close();
                            bookIns.close();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        //更新为下载失败，并更新状态
                        DownLoad resTask = dao.load(task.getId());
                        resTask.setStatus(Constant.DOWN_STATUS_FAILE);

                    }

                } while ((task = getLastedTask()) != null);
            }
        }.start();
    }

    /**
     * 如果有这本书的任务就不管了，没有就插入
     *
     * @param task
     * @return
     */
    private void insertOneTask(DownLoad task) {
        List<DownLoad> searchResult = dao.queryBuilder().where(new WhereCondition.StringCondition("url ='" + task.getUrl() + "'")).list();
        if (searchResult.size() == 0)
            dao.insert(task);
    }

    /**
     * 取最早的一条登待下载任务
     *
     * @return
     */
    private DownLoad getLastedTask() {
        List<DownLoad> temp = dao.queryBuilder().where(new WhereCondition.StringCondition("status = " + Constant.DOWN_STATUS_WAIT)).orderAsc(DownLoadDao.Properties.UpDate).limit(1).list();
        if (temp.size() != 0)
            return temp.get(0);
        return null;
    }
}

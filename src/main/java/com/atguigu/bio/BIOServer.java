package com.atguigu.bio;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jay on 21/5/2 - 7:26
 */
@Slf4j
@Logger
public class BIOServer {
    public static void main(String[] args) throws Exception {
        /**
         * 思路：
         *  1. 创建一个线程池
         *  2. 如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
         */

        // log4j:WARN No appenders could be found for logger (dao.hsqlmanager).
        // log4j:WARN Please initialize the log4j system properly.
        // log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info
        // 下面第一行代码是解决 上面出现的三条报错日志信息  关于 log4j
        BasicConfigurator.configure();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        log.info("Server is Running");
        while (true) {
            // 监听，等待客户端连接
            final Socket socket = serverSocket.accept();
            log.info("connect a client");
            // create a thread ,make connected
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {

                    handler(socket);
                    // 可以和客户端通讯
                }
            });
        }
    }

    // 编写一个handler方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            // 接收数据
            log.info("thread info id:{},threadName:{}",Thread.currentThread().getId(),Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            while (true){
                log.info("thread info id:{},threadName:{}",Thread.currentThread().getId(),Thread.currentThread().getName());
                int readLen = inputStream.read(bytes);
                /**
                 *条件:判断数据读取完毕
                 */
                 if(readLen != -1) {
                    //log.info("输出客户端发送的数据：{}",new String(bytes,0,readLen)); // 输出客户端发送的数据
                     System.out.println(new String(bytes,0,readLen));
                     System.out.println("输出客户端发送的数据");
                    }else{
                     break;
                 }

            }
        }catch (Exception e){
            e.printStackTrace();;
        }finally {
            log.info("break connect with client");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




}

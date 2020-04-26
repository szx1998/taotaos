package com.taotao.test;


import com.taotao.utils.FtpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyTest {

    @Test
    public void show() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("192.168.47.128");
        ftpClient.login("ftpuser","ftpuser");
        FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\舒志新\\Pictures\\Warframe\\猪大帅.jpg"));
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.storeFile("/home/ftpuser/www/images/123.jpg",inputStream);
        inputStream.close();
        ftpClient.logout();
    }

    @Test
    public void show2() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\舒志新\\Pictures\\Warframe\\猪大帅.jpg"));
        boolean b = FtpUtil.uploadFile("192.168.47.128",21,"ftpuser","ftpuser","/home/ftpuser/www/images","2020/04/25","demo.jpg",inputStream);
        System.out.println(b);
    }
}

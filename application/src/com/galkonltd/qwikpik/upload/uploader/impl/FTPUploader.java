package com.galkonltd.qwikpik.upload.uploader.impl;

import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.upload.UploadResult;
import com.galkonltd.qwikpik.upload.impl.ImageUploadResult;
import com.galkonltd.qwikpik.upload.uploader.Uploader;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/20/2015
 */
public class FTPUploader<T extends BufferedImage> extends Uploader<T> {

    private final FTPClient ftpClient;
    private final String host, user, pass, imageDirectory, webPath;
    private final int port;

    public FTPUploader(String name, String description, String host, String user, String pass, String imageDirectory, String webPath, int port) {
        super(name, description);
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.port = port;
        this.imageDirectory = imageDirectory;
        this.webPath = webPath;
        this.ftpClient = new FTPClient();
    }

    @Override
    public UploadResult upload(T t) {
        try {
            this.ftpClient.connect(this.host, this.port);
            this.ftpClient.login(this.user, this.pass);
            this.ftpClient.enterLocalPassiveMode();

            this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String timestampName = Utils.formatTimestamp(System.currentTimeMillis());
            String randomName = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 8);
            String fileName = URLEncoder.encode(randomName + ".png", "UTF-8");
            String remoteFile = this.imageDirectory + (this.imageDirectory.endsWith(File.separator) || this.imageDirectory.endsWith("/") ? "" : File.separator) + fileName;

            InputStream inputStream = new ByteArrayInputStream(Utils.imageToByteArray(t));
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
                return ImageUploadResult.createNew(t, this.webPath + fileName);
            } else {
                System.err.println("Never completed: " + remoteFile);
                return null;
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (this.ftpClient.isConnected()) {
                    this.ftpClient.logout();
                    this.ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
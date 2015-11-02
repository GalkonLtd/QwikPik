package com.galkonltd.qwikpik.upload.impl;

import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.upload.UploadHistory;
import com.galkonltd.qwikpik.upload.UploadResult;

import java.awt.image.BufferedImage;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/19/2015
 */
public class ImageUploadResult extends UploadResult {

    public static ImageUploadResult createNew(BufferedImage image, String url) {
        ImageUploadResult upload = new ImageUploadResult(url, System.currentTimeMillis());
        Utils.saveImage(upload.getFileName(), image);
        UploadHistory.add(upload);
        return upload;
    }

    public static ImageUploadResult create(String url, long timestamp, String filePath) {
        return new ImageUploadResult(url, timestamp, filePath);
    }

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    private ImageUploadResult(String url, long timestamp) {
        this(url, timestamp, Utils.formatTimestamp(timestamp) + ".png");
    }

    private ImageUploadResult(String url, long timestamp, String fileName) {
        super(url, timestamp);
        this.fileName = fileName;
    }

    public BufferedImage getImage() {
        return Utils.loadImage(this.fileName);
    }

}

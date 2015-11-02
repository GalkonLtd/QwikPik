package com.galkonltd.qwikpik.upload.uploader;

import com.galkonltd.qwikpik.upload.UploadResult;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/15/2015
 */

public abstract class Uploader<T> {

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    private final String name;
    private final String description;

    public Uploader(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Upload the BufferedImage and return the image url as a string.
     * @param t
     * @return
     */
    public abstract UploadResult upload(T t);

}

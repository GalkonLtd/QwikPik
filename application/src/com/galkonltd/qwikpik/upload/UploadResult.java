package com.galkonltd.qwikpik.upload;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/18/2015
 */
public class UploadResult {

    public static UploadResult createAndArchive(String url) {
        UploadResult upload = new UploadResult(url, System.currentTimeMillis());
        UploadHistory.add(upload);
        return upload;
    }

    public static UploadResult create(String url, long timestamp) {
        return new UploadResult(url, timestamp);
    }

    private final String url;
    private final long timestamp;

    protected UploadResult(String url, long timestamp) {
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
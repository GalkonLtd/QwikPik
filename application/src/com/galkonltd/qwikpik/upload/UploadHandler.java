package com.galkonltd.qwikpik.upload;

import com.galkonltd.qwikpik.upload.uploader.Uploader;
import com.galkonltd.qwikpik.upload.uploader.impl.FTPUploader;
import com.galkonltd.qwikpik.upload.uploader.impl.ImgurUploader;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/15/2015
 */
public final class UploadHandler {

    /**
     * A map of {@link Uploader}s.
     */
    private static final HashMap<String, Uploader> uploaders = new HashMap<>();

    /**
     * Initializes the default {@link Uploader}s.
     */
    public static void initialize() {
        Uploader uploader = new ImgurUploader("Imgur", "http://www.imgur.com/");
        uploaders.put(uploader.getName(), uploader);
    }

    /**
     * Attempts to upload a {@link BufferedImage} to the default {@link Uploader}.
     * @param image
     * @return If successful, the image url. If not, null.
     */
    public static UploadResult uploadImage(BufferedImage image) {
        return uploadImage(image, "Imgur");
    }

    /**
     * Attempts to upload the {@link BufferedImage} to the {@link Uploader} for the specified key. If successful, it returns the image url.
     * @param image
     * @param uploaderKey
     * @return The url of the image if successful. If unsuccessful, returns null.
     */
    public static UploadResult uploadImage(BufferedImage image, String uploaderKey) {
        if (!uploaders.containsKey(uploaderKey)) {
            System.err.print("Uploader not found for key: " + uploaderKey);
            return null;
        }
        return uploaders.get(uploaderKey).upload(image);
    }

    public static HashMap<String, Uploader> getUploaders() {
        return uploaders;
    }
}

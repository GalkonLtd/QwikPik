/**
 * Sleeksnap, the open source cross-platform capture uploader
 * Copyright (C) 2012 Nikki <nikki@nikkii.us>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.galkonltd.qwikpik.upload.uploader.impl;

import com.galkonltd.qwikpik.upload.UploadResult;
import com.galkonltd.qwikpik.upload.impl.ImageUploadResult;
import com.galkonltd.qwikpik.upload.uploader.Uploader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public final class ImgurUploader<T extends BufferedImage> extends Uploader<T> {

    private static final String CLIENT_ID = "c93105edd987722";

    public ImgurUploader(String name, String description) {
        super(name, description);
    }

    @Override
    public UploadResult upload(T image) {
        DataOutputStream out = null;
        BufferedReader reader = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();

            URL url = new URL("https://api.imgur.com/3/image.json");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Client-ID " + ImgurUploader.CLIENT_ID);
            connection.setDoOutput(true);

            String encodedString = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(DatatypeConverter.printBase64Binary(baos.toByteArray()), "UTF-8");

            out = new DataOutputStream(connection.getOutputStream());
            byte[] data = encodedString.getBytes();
            out.write(data);
            out.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            String imageUrl = "";
            List<String> lines = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                lines.add(line);
                String[] args = line.split(",");
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("link")) {
                        String sub = args[i].substring(8);
                        sub = sub.substring(0, sub.length() - 2);
                        sub = sub.replace("\\", "");
                        imageUrl = sub;
                    }
                }
            }

            if (imageUrl == null || imageUrl.isEmpty()) {
                System.err.print("Upload failed! Image url is empty.");
                return null;
            }
            return ImageUploadResult.createNew(image, imageUrl);
        } catch (IOException e) {
            if (e.getMessage().contains("1203")) {
                JOptionPane.showMessageDialog(null, "Imgur is currently over capacity. Please try again later, or use a different uploader.", "Error 1203", JOptionPane.ERROR_MESSAGE);
            } else if (e.getMessage().contains("Server returned HTTP response code:")) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.flush();
            image = null;
        }
        return null;
    }

}

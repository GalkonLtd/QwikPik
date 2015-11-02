package com.galkonltd.qwikpik.upload;

import com.galkonltd.qwikpik.Utils;
import com.galkonltd.qwikpik.ui.ApplicationWindow;
import com.galkonltd.qwikpik.ui.impl.SideBar;
import com.galkonltd.qwikpik.upload.impl.ImageUploadResult;
import com.nova.buffer.DataBuffer;
import com.nova.buffer.WriteBuffer;
import com.nova.file.ReadWriteFile;
import com.nova.task.Task;
import com.nova.task.TaskManager;

import java.nio.BufferUnderflowException;
import java.util.LinkedList;
import java.util.List;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/18/2015
 */
public final class UploadHistory {

    private static final int SAVE_VERSION = 1;
    private static final List<UploadResult> uploads = new LinkedList<>();

    public static List<UploadResult> getUploads() {
        return uploads;
    }

    public static void add(UploadResult upload) {
        uploads.add(upload);
        saveHistory();
        if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {
            ApplicationWindow.getInstance().repaint();
        }
    }

    public static void remove(UploadResult upload) {
        uploads.remove(upload);
        saveHistory();
        if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {
            ApplicationWindow.getInstance().repaint();
        }
    }

    public static void remove(int index) {
        uploads.remove(index);
        saveHistory();
        if (SideBar.currentSelectionMatches(SideBar.UPLOAD_HISTORY_LOC[0], SideBar.UPLOAD_HISTORY_LOC[1])) {
            ApplicationWindow.getInstance().repaint();
        }
    }

    public static UploadResult getMostRecent() {
        return uploads.get(uploads.size() - 1);
    }

    public static int size() {
        return uploads.size();
    }

    public static void saveHistory() {
        TaskManager.submitTask(new Task() {
            @Override
            public boolean execute() {
                return SAVE_FILE.write();
            }
        });
    }


    private static final String SAVE_FILE_PATH = Utils.getWorkingDirectory() + "qwikpik.history";

    private static final ReadWriteFile SAVE_FILE = new ReadWriteFile(SAVE_FILE_PATH) {
        @Override
        public void writeToBuffer(WriteBuffer writeBuffer) {
            writeBuffer.putShort(SAVE_VERSION);
            writeBuffer.putInt(size());
            for (int index = 0; index < size(); index++) {
                UploadResult upload = uploads.get(index);
                writeBuffer.putUTF(upload.getUrl());
                writeBuffer.putLong(upload.getTimestamp());
                writeBuffer.putBoolean(upload instanceof ImageUploadResult);
                if (upload instanceof ImageUploadResult) {
                    writeBuffer.putUTF(((ImageUploadResult) upload).getFileName());
                }
            }
        }

        @Override
        public void readFromBuffer(DataBuffer dataBuffer) {
            try {
                int saveVersion = dataBuffer.getShort();
                switch (saveVersion) {
                    case 1: {
                        int historyCount = dataBuffer.getInt();
                        for (int index = 0; index < historyCount; index++) {
                            UploadResult upload;
                            String url = dataBuffer.getUTF();
                            long timestamp = dataBuffer.getLong();
                            boolean isImage = dataBuffer.getBoolean();
                            if (isImage) {
                                String filePath = dataBuffer.getUTF();
                                upload = ImageUploadResult.create(url, timestamp, filePath);
                            } else {
                                upload = UploadResult.create(url, timestamp);
                            }
                            uploads.add(upload);
                        }
                        break;
                    }
                }
            } catch (BufferUnderflowException e) {
                uploads.clear();
                e.printStackTrace();
            }
        }
    };

    public static void load() {
        SAVE_FILE.read();
    }

    public static void close() {
        SAVE_FILE.close();
    }

}

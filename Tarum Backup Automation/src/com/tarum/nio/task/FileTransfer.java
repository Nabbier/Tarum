package com.tarum.nio.task;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileTransfer {

    public long uid;

    private File targetFile;

    private String targetDest;

    private ByteBuffer readBuffer, writeBuffer;

    private FileChannel readChannel, writeChannel;

    public FileTransfer (File targetFile, String targetDest){
        this.targetFile = targetFile;
        this.targetDest = targetDest;
    }

    public void start(){
        int position = readBuffer.position(), count = readBuffer.remaining();
//        try {
//            readChannel = FileChannel.open(targetFile.toPath()).transferFrom(, position, count);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}

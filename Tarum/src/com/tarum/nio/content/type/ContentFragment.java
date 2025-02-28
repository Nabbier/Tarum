package com.tarum.nio.content.type;
import com.tarum.io.content.type.BasicContentContainer;
import com.tarum.util.MathUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class ContentFragment implements Serializable {

    private transient BasicContentContainer parentContainer;
    private long uid;

    private int startPositionIndex, endPositionIndex;

    private int bufferAllocationAmount = DEFAULT_BUFFER_ALLOCATION;

    private transient ByteBuffer contentBuffer;

    public static int DEFAULT_BUFFER_ALLOCATION = 8192;

    public BasicContentContainer getParentContainer() {
        return parentContainer;
    }
    public void setParentContainer(BasicContentContainer parentContainer) {
        this.parentContainer = parentContainer;
    }
    public long getUID() {
        return uid;
    }
    public void setUID(long uid) {
        this.uid = uid;
    }
    public int getStartPositionIndex() {
        return startPositionIndex;
    }
    public void setStartPositionIndex(int startPositionIndex) {
        this.startPositionIndex = startPositionIndex;
    }
    public int getEndPositionIndex() {
        return endPositionIndex;
    }
    public void setEndPositionIndex(int endPositionIndex) {
        this.endPositionIndex = endPositionIndex;
    }
    public int getBufferAllocationAmount() {
        return bufferAllocationAmount;
    }
    public void setBufferAllocationAmount(int bufferAllocationAmount) {
        this.bufferAllocationAmount = bufferAllocationAmount;
    }
    public ByteBuffer getContentBuffer() {
        return contentBuffer;
    }
    public void setContentBuffer(ByteBuffer contentBuffer) {
        this.contentBuffer = contentBuffer;
    }

    public int getDefaultBufferAllocationAmount() {
        return DEFAULT_BUFFER_ALLOCATION;
    }
    public void setDefaultBufferAllocationAmount(int defaultBufferAllocationAmount) {
        this.DEFAULT_BUFFER_ALLOCATION = defaultBufferAllocationAmount;
    }

    public ContentFragment (){
        this(null);
    }
    public ContentFragment (BasicContentContainer parentContainer){
        this.parentContainer = parentContainer;
        this.uid = MathUtils.GenerateUID();

        init();
    }

    private void init(){
        contentBuffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_ALLOCATION);
    }

    /**
     * TODO:
     */
    public ByteBuffer wrapContent (byte[] data){
        return ByteBuffer.wrap(data);
    }

}

package org.qingy.irpc.framefork.core.common;

import java.io.Serializable;
import java.util.Arrays;

import static org.qingy.irpc.framefork.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Author: QingY
 * @Date: Created in 21:33 2024-04-24
 * @Description:
 */
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = -7203772284443033559L;
    private short magicNumber = MAGIC_NUMBER;
    private int contentLength;
    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "magicNumber=" + magicNumber +
                ", contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}

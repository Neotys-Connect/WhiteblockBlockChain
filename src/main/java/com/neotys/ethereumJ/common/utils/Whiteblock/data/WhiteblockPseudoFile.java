package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class WhiteblockPseudoFile implements ContentBody{
    private final String name;
    private final String data;
    private StringBody body;

    public WhiteblockPseudoFile(String name, String data) throws UnsupportedEncodingException {
        this.name = name;
        this.data = data;
        this.body = new StringBody(data);
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getFilename() {
        return name;
    }

    public void writeTo(OutputStream out) throws IOException {
        this.body.writeTo(out);
    }
    public String getCharset() {
        return this.body.getCharset();
    }
    public String getMimeType() {
        return this.body.getMimeType();
    }
    public long getContentLength() {
        return this.body.getContentLength();
    }
    public String getMediaType() {
        return this.body.getMediaType();
    }
    public String getSubType() {
        return this.body.getSubType();
    }
    public String getTransferEncoding() {
        return this.body.getTransferEncoding();
    }
}

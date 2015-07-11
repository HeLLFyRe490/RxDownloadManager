package com.norddev.downloadmanager.downloader;

import com.squareup.okhttp.Response;

import java.io.IOException;

public class HTTPResponseException extends IOException {
    public HTTPResponseException(Response response) {
        super(response.code()+" "+response.message());
    }
}

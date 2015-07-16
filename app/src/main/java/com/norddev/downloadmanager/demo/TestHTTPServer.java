package com.norddev.downloadmanager.demo;

import android.util.Log;

import com.norddev.downloadmanager.common.C;
import com.norddev.downloadmanager.common.Util;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestHTTPServer {

    private static final String TAG = "TestHTTPServer";
    private final MockWebServer mServer;

    private static final long FILE_SIZE = 1024 * 1024;
    private static final long THROTTLE_RATE = 100 * 1024;

    public TestHTTPServer() {
        mServer = new MockWebServer();
        mServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                Log.d(TAG, request.toString());
                MockResponse response = new MockResponse();
                long bytesToServe;
                long[] range = Util.parseRange(request.getHeader("Range"));
                if(range != null) {
                    if(range[0] >= FILE_SIZE){
                        return response.setResponseCode(416).setStatus("Requested Range Not Satisfiable");
                    }
                    if(range[1] == C.UNKNOWN) {
                        bytesToServe = FILE_SIZE - range[0];
                    } else {
                        bytesToServe = range[1] - range[0] + 1;
                        if(bytesToServe > FILE_SIZE){
                            return response.setResponseCode(416).setStatus("Requested Range Not Satisfiable");
                        }
                    }
                    String contentRange = Util.buildContentRange(range[0], bytesToServe, FILE_SIZE);
                    response.addHeader("Content-Range", contentRange).setResponseCode(201);
                } else {
                    bytesToServe = FILE_SIZE;
                    response.setResponseCode(200);
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream((int)bytesToServe);
                for(int i = 0; i < bytesToServe; i++){
                    out.write(i % 16);
                }
                return response.setBody(new String(out.toByteArray())).throttleBody(THROTTLE_RATE, 1, TimeUnit.SECONDS);
            }
        });
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mServer.start(8080);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

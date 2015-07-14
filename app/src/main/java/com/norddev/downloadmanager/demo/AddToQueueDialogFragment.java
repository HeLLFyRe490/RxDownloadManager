package com.norddev.downloadmanager.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.queue.DownloadRequest;
import com.norddev.downloadmanager.queue.api.DownloadRequestFactory;

/**
 *
 */
public class AddToQueueDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final DownloadManager downloadManager = DemoApplication.get().getDownloadManager();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add To Queue");
        builder.setCancelable(true);
        builder.setView(new EditText(getActivity()));
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadSpec spec = new DownloadSpec("key", "http://localhost:8080");
                DownloadRequestFactory factory = downloadManager.getDownloadRequestFactory();
                DownloadRequest request = factory.createRequest(spec);
                downloadManager.getRequestQueue().enqueue(request);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

}

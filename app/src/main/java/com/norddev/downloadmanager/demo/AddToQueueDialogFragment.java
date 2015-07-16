package com.norddev.downloadmanager.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.norddev.downloadmanager.DownloadManager;
import com.norddev.downloadmanager.downloader.DownloadRequest;
import com.norddev.downloadmanager.downloader.DownloadSpec;
import com.norddev.downloadmanager.downloader.api.DownloadRequestFactory;

/**
 *
 */
public class AddToQueueDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_to_queue, null, false);
        final EditText keyEdit = (EditText) v.findViewById(R.id.dialog_add_to_queue_request_key_edit);
        final EditText urlEdit = (EditText) v.findViewById(R.id.dialog_add_to_queue_request_url_edit);
        urlEdit.setText("http://localhost:8080");

        final DownloadManager downloadManager = DemoApplication.get().getDownloadManager();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add To Queue");
        builder.setCancelable(true);
        builder.setView(v);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String keyValue = keyEdit.getText().toString();
                String urlValue = urlEdit.getText().toString();
                DownloadSpec spec = new DownloadSpec(keyValue, urlValue);
                DownloadRequestFactory factory = downloadManager.getDownloadRequestFactory();
                DownloadRequest request = factory.createRequest(spec);
                if (!downloadManager.getRequestQueue().add(request)) {
                    Toast.makeText(getActivity(), "Request with key already exists", Toast.LENGTH_SHORT).show();
                }
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

package com.orientalmusic.music.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.dialog.DownloadDialog;
import com.orientalmusic.music.entity.Book;
import com.orientalmusic.music.task.DownloadBookTask;

import java.io.File;


public class BookDetailsFragment extends Fragment {

    private MainActivity activity;
    private Book book;

    private TextView txtTitle;
    private TextView txtDetails;
    private Button btnView;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(MainActivity activity, Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        fragment.activity = activity;
        fragment.book = book;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        attachEvents();
        viewData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void attachViews() {
        txtTitle = getView().findViewById(R.id.txtTitle);
        txtDetails = getView().findViewById(R.id.txtDetails);
        txtTitle.setTypeface(activity.getTypeface());
        btnView = getView().findViewById(R.id.btnView);
    }

    private void attachEvents() {
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewPdf())
                    downloadBook("downloading books please wait");
            }
        });
    }

    private void viewData() {
        if (book != null) {
            txtTitle.setText(book.getTitle());
            txtDetails.setText(book.getDescription());
        }
    }

    private boolean viewPdf() {
        boolean exist = false;
        File file = new File(getContext().getFilesDir(), book.getUrl());
        if (file.exists()) {
            activity.showPdf(file);
            exist = true;
        }

        return exist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    private void downloadBook(String message) {
        final DownloadDialog progressDialog = new DownloadDialog(getContext());
        progressDialog.setTitle(getString(R.string.downloading));
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);

        final DownloadBookTask downloadBookTask = new DownloadBookTask(getContext(), new DownloadBookTask.Listener() {
            @Override
            public void onPreDownload() {
                progressDialog.show();
            }

            @Override
            public void onProgress(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onCompleted(boolean success) {
                progressDialog.dismiss();
                if (getContext() == null) {
                    return;
                }
                if (success) {
                    viewPdf();
                } else {

                }
            }

            @Override
            public void onCancelled() {
                progressDialog.dismiss();
            }
        });

        progressDialog.setTask(downloadBookTask);
        downloadBookTask.execute(book);
    }

}

/*
 * DownloadDocumentTask.java
 *   PSPDFKit
 *
 *   Copyright (c) 2014-2016 PSPDFKit GmbH. All rights reserved.
 *
 *   THIS SOURCE CODE AND ANY ACCOMPANYING DOCUMENTATION ARE PROTECTED BY INTERNATIONAL COPYRIGHT LAW
 *   AND MAY NOT BE RESOLD OR REDISTRIBUTED. USAGE IS BOUND TO THE PSPDFKIT LICENSE AGREEMENT.
 *   UNAUTHORIZED REPRODUCTION OR DISTRIBUTION IS SUBJECT TO CIVIL AND CRIMINAL PENALTIES.
 *   This notice may not be removed from this file.
 */

package com.orientalmusic.music.task;

import android.content.Context;
import android.os.AsyncTask;

import com.orientalmusic.music.C;
import com.orientalmusic.music.entity.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This downloads a document from an input stream
 */
public class DownloadBookTask extends AsyncTask<Book, Integer, Boolean> {
    private final Context context;
    private final Listener listener;
    public DownloadBookTask(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onPreDownload();
    }

    protected Boolean doInBackground(Book... books) {
        boolean result;
        Book book = books[0];
        String bookUrl = C.BOOKS_URL + book.getUrl();
        File outputFile = new File(context.getFilesDir(), book.getUrl());
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);

            int read;
            byte[] buffer = new byte[8192];

            URL url = new URL(bookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20000);
            InputStream connectionInputStream = conn.getInputStream();
            long len = conn.getContentLength();
            long totalRead = 0;

            while ((read = connectionInputStream.read(buffer)) > 0 && !isCancelled()) {
                fos.write(buffer, 0, read);
                totalRead += read;
                publishProgress((int) (((double) totalRead / (double) len) * 100.0));
            }
            connectionInputStream.close();
            fos.close();
            //   DataStore.saveDownloadedBookVersion(context, book.getId(), book.getVersion());
            result = !isCancelled();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        listener.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        listener.onCompleted(result);
    }

    @Override
    protected void onCancelled() {
        listener.onCancelled();
    }

    public interface Listener {
        void onPreDownload();

        void onProgress(int progress);

        void onCompleted(boolean success);

        void onCancelled();
    }
}

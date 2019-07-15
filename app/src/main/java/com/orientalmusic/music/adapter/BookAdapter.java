package com.orientalmusic.music.adapter;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.entity.Book;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by kameloov on 7/23/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.VideoHolder> {
    private static String url = "http://myoriental-music.com/service/icons/";
    MainActivity contentActivity;
    List<Book> bookList;
    List<Book> temp;
    private boolean loading = false;

    public BookAdapter(MainActivity activity, List<Book> bookList) {
        this.bookList = bookList;
        this.contentActivity = activity;
        if (bookList.size() > 0) {
            loadMore(bookList.get(0).getCategory());
        }
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contentActivity).inflate(R.layout.list_item_book, parent, false);
        return new VideoHolder(v, contentActivity);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Book book = bookList.get(position);
        if (book != null)
            holder.bindCategory(book);
    }

    public void loadMore(int catId) {
        if (loading)
            return;
        LOadBooks lOadBooks = new LOadBooks(catId, findMinId());
        lOadBooks.execute();
    }

    private int findMinId() {
        int min = 0;
        if (bookList != null && bookList.size() > 0) {
            min = bookList.get(0).getId();
            for (Book book : bookList) {
                if (book.getId() < min)
                    min = book.getId();
            }
        }
        return min;
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgVIdeo;
        private TextView txtTitle;
        private MainActivity contentActivity;
        private Book book;

        public VideoHolder(View itemView, MainActivity contentActivity) {
            super(itemView);
            this.contentActivity = contentActivity;
            imgVIdeo = (ImageView) itemView.findViewById(R.id.imgVideo);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtTitle.setTypeface(contentActivity.getTypeface());
            itemView.setOnClickListener(this);
        }

        public void bindCategory(Book book) {
            this.book = book;
            txtTitle.setText(book.getTitle());
            //    Glide.with(contentActivity).load(url +book.getIcon()).into(imgVIdeo);
        }

        @Override
        public void onClick(View view) {
            contentActivity.showbook(book);
        }
    }


    class LOadBooks extends AsyncTask<Void, Void, Service.Response> {

        private int catId;
        private int minId;

        public LOadBooks(int catId, int minId) {

            this.minId = minId;
            this.catId = catId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (temp != null) {
                bookList.addAll(temp);
                notifyDataSetChanged();
            }
            loading = true;
            temp = null;
        }

        @Override
        protected void onPostExecute(Service.Response result) {
            super.onPostExecute(result);
            loading = false;
            if (result != null) {
                if (result.isSuccess()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Book>>() {
                    }.getType();
                    Log.e("getBooks", result.getResponseObject().toString());
                    List<Book> bookList = gson.fromJson(result.getResponseObject(), type);
                    if (bookList != null) {
                        temp = bookList;
                    }
                }

            }
        }

        @Override
        protected Service.Response doInBackground(Void... voids) {
            Service service = new Service();
            Service.Response object = null;
            try {
                object = service.getBooks(minId, catId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
    }
}

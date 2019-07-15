package com.orientalmusic.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.adapter.BookAdapter;
import com.orientalmusic.music.entity.Book;
import com.orientalmusic.music.entity.SubCategory;
import com.orientalmusic.music.service.Service;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BooksFragment extends Fragment {
    private MainActivity activity;
    private SubCategory subCategory;
    private RecyclerView lstBooks;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private TextView txtEmpty;

    public BooksFragment() {
        // Required empty public constructor
    }


    public static BooksFragment newInstance(MainActivity activity, SubCategory subCategory) {
        BooksFragment fragment = new BooksFragment();
        fragment.activity = activity;
        fragment.subCategory = subCategory;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attachViews();
        layoutManager = new LinearLayoutManager(getActivity());
        lstBooks.setLayoutManager(layoutManager);
        LOadBooks task = new LOadBooks(subCategory.getId(), 0, progressBar, txtEmpty);
        task.execute();
    }

    private void attachViews() {
        lstBooks = (RecyclerView) getView().findViewById(R.id.lstVideos);
        txtEmpty = (TextView) getView().findViewById(R.id.txtEmpty);
        txtEmpty.setTypeface(activity.getTypeface());
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        lstBooks.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (layoutManager != null && lstBooks.getAdapter() != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (layoutManager.findLastVisibleItemPosition() > lstBooks.getAdapter().getItemCount() - 4) {
                            BookAdapter adapter = ((BookAdapter) lstBooks.getAdapter());
                            if (adapter != null)
                                adapter.loadMore(subCategory.getId());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }


    class LOadBooks extends AsyncTask<Void, Void, Service.Response> {
        private ProgressBar progressBar;
        private View txtEmpty;
        private int catId;
        private int minId;

        public LOadBooks(int catId, int minId, ProgressBar progressBar, View txtEmpty) {
            this.txtEmpty = txtEmpty;
            this.progressBar = progressBar;
            this.minId = minId;
            this.catId = catId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtEmpty.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Service.Response result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                if (result.isSuccess()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Book>>() {
                    }.getType();
                    Log.e("getBooks", result.getResponseObject().toString());
                    List<Book> bookList = gson.fromJson(result.getResponseObject(), type);
                    if (bookList != null) {
                        BookAdapter adapter = new BookAdapter(activity, bookList);
                        lstBooks.setAdapter(adapter);
                    } else
                        txtEmpty.setVisibility(View.VISIBLE);
                } else
                    txtEmpty.setVisibility(View.VISIBLE);

            } else
                txtEmpty.setVisibility(View.VISIBLE);
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

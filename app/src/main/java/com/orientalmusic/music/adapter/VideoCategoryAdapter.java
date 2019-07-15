package com.orientalmusic.music.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orientalmusic.music.R;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.entity.Category;

import java.util.List;

/**
 * Created by kameloov on 7/23/2017.
 */

public class VideoCategoryAdapter extends RecyclerView.Adapter<VideoCategoryAdapter.CategoryHolder> {
    private static String url = "http://myoriental-music.com/service/icons/";
    MainActivity contentActivity;
    List<Category> categoryList;

    public VideoCategoryAdapter(MainActivity activity, List<Category> categoryList) {
        this.categoryList = categoryList;
        this.contentActivity = activity;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contentActivity).inflate(R.layout.list_item_category, parent, false);
        return new CategoryHolder(v, contentActivity);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category != null)
            holder.bindCategory(category);
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgCategory;
        private TextView txtCategory;
        private MainActivity contentActivity;
        private Category category;

        public CategoryHolder(View itemView, MainActivity contentActivity) {
            super(itemView);
            this.contentActivity = contentActivity;
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            itemView.setOnClickListener(this);
        }

        public void bindCategory(Category category) {
            this.category = category;
            txtCategory.setText(category.getName());
            txtCategory.setTypeface(contentActivity.getTypeface());
            Glide.with(contentActivity).load(url + category.getImage()).into(imgCategory);
        }

        @Override
        public void onClick(View view) {
            contentActivity.showSubCategories(category,true);
        }
    }
}

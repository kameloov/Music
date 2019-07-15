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
import com.orientalmusic.music.entity.SubCategory;

import java.util.List;

/**
 * Created by kameloov on 7/23/2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CategoryHolder> {
    private static String url = "http://myoriental-music.com/service/icons/";
    MainActivity contentActivity;
    List<SubCategory> subCategoryList;
    private boolean isVideo;

    public SubCategoryAdapter(MainActivity activity, List<SubCategory> subCategoryList,boolean isVideo) {
        this.subCategoryList = subCategoryList;
        this.contentActivity = activity;
        this.isVideo = isVideo;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contentActivity).inflate(R.layout.list_item_category, parent, false);
        return new CategoryHolder(v, contentActivity);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        SubCategory subCategory = subCategoryList.get(position);
        if (subCategory != null)
            holder.bindCategory(subCategory);
    }


    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgCategory;
        private TextView txtCategory;
        private MainActivity contentActivity;
        private SubCategory subCategory;

        public CategoryHolder(View itemView, MainActivity contentActivity) {
            super(itemView);
            this.contentActivity = contentActivity;
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            itemView.setOnClickListener(this);
        }

        public void bindCategory(SubCategory subCategory) {
            this.subCategory = subCategory;
            txtCategory.setText(subCategory.getName());
            txtCategory.setTypeface(contentActivity.getTypeface());
            Glide.with(contentActivity).load(url + subCategory.getImage()).into(imgCategory);
        }

        @Override
        public void onClick(View view) {
            if (SubCategoryAdapter.this.isVideo){
                contentActivity.showVideos(subCategory);
            }else
            contentActivity.showBooks(subCategory);
        }
    }
}

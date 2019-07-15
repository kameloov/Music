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
import com.orientalmusic.music.entity.More;

import java.util.List;

/**
 * Created by kameloov on 7/23/2017.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreHolder> {
    private static String url = "http://myoriental-music.com/service/icons/";
    MainActivity contentActivity;
    List<More> moreList;

    public MoreAdapter(MainActivity activity, List<More> moreList) {
        this.moreList = moreList;
        this.contentActivity = activity;
    }

    @Override
    public MoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contentActivity).inflate(R.layout.list_item_more, parent, false);
        return new MoreHolder(v, contentActivity);
    }

    @Override
    public void onBindViewHolder(MoreHolder holder, int position) {
        More more = moreList.get(position);
        if (more != null)
            holder.bindMore(more);
    }


    @Override
    public int getItemCount() {
        return moreList.size();
    }

    public static class MoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgCategory;
        private TextView txtCategory;
        private MainActivity contentActivity;
        private More more;

        public MoreHolder(View itemView, MainActivity contentActivity) {
            super(itemView);
            this.contentActivity = contentActivity;
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            txtCategory.setTypeface(contentActivity.getTypeface());
            itemView.setOnClickListener(this);
        }

        public void bindMore(More more) {
            this.more = more;
            txtCategory.setText(more.getTitle());
            txtCategory.setTypeface(contentActivity.getTypeface());
            Glide.with(contentActivity).load(url + more.getImage()).into(imgCategory);
        }

        @Override
        public void onClick(View view) {
            contentActivity.showMoreDetails(more);
        }
    }
}

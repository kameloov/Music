package com.orientalmusic.music.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.orientalmusic.music.R;
import com.orientalmusic.music.entity.More;
import java.util.HashMap;
import java.util.List;

public class ImagesAdapter extends PagerAdapter {
    private List<More> moreList;
    private Context context;
    private static String url = "http://myoriental-music.com/service/icons/";

    public ImagesAdapter( Context context,List<More> moreList) {
        this.context = context;
        this.moreList =  moreList;
        Log.e("moreList","the count is "+moreList.size());
    }

    @Override
    public int getCount() {
        return moreList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.list_item_more_pager, container,false);
        ImageView img = (ImageView) v.findViewById(R.id.imgIcon);
        TextView txtTitle = v.findViewById(R.id.txtTitle);
        More more = moreList.get(position);
        Glide.with(context).load(url + more.getImage()).into(img);
        txtTitle.setText(more.getTitle());
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }

}

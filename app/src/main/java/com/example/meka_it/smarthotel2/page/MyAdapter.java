package com.example.meka_it.smarthotel2.page;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meka_it.smarthotel.R;

public class MyAdapter extends ArrayAdapter<String> {

    int[] pic,pic2,pic3;
    String[] names,names2,names3;
    String[] title,title2,title3;
    Context mContext,mContext2,mContext3;

    public MyAdapter(Context context, String[] foodname, String[] foodtitle, int[] foodpic) {
        super(context, R.layout.simple_list_item_1);
        this.names = foodname;
        this.title = foodtitle;
        this.pic = foodpic;
        this.mContext = context;

        this.names2 = foodname;
        this.title2 = foodtitle;
        this.pic2 = foodpic;
        this.mContext2 = context;

        this.names3 = foodname;
        this.title3 = foodtitle;
        this.pic3 = foodpic;
        this.mContext3 = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.simple_list_item_1, parent, false);
            mViewHolder.mPic = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textView);
            mViewHolder.mTitle = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mPic.setImageResource(pic[position]);
        mViewHolder.mName.setText(names[position]);
        mViewHolder.mTitle.setText(title[position]);

        mViewHolder.mPic.setImageResource(pic2[position]);
        mViewHolder.mName.setText(names2[position]);
        mViewHolder.mTitle.setText(title2[position]);

        mViewHolder.mPic.setImageResource(pic3[position]);
        mViewHolder.mName.setText(names3[position]);
        mViewHolder.mTitle.setText(title3[position]);
        return convertView;
    }

    static class ViewHolder {
        ImageView mPic;
        TextView mName;
        TextView mTitle;
    }
}

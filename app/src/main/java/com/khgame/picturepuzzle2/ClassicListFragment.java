package com.khgame.picturepuzzle2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.khgame.picturepuzzle2.model.ClassicPicture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkang on 2017/1/7.
 */

public class ClassicListFragment extends AbstractListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GridView gridView = (GridView) inflater.inflate(R.layout.three_col_listview, null);
        gridView.setAdapter(listAdapter);
        return  gridView;
    }

    /**
     * classic list view adapter
     */
    private ListAdapter listAdapter = new BaseAdapter() {
        List<ClassicPicture> pictures = new ArrayList<>();
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int i) {
            return pictures.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(ClassicListFragment.this.getContext());
            imageView.setImageResource(R.mipmap.ic_launcher);
            return imageView;
        }
    };
}

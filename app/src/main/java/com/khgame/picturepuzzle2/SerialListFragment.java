package com.khgame.picturepuzzle2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle.service.model.Serial;
import com.khgame.picturepuzzle.service.GetAllSerialsOperation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zkang on 2017/1/7.
 */

public class SerialListFragment extends AbstractListFragment {
    private static String TAG = "SerialListFragment";

    @BindView(R.id.gridview)
    GridView gridView;

    SerialListAdapter listAdapter = new SerialListAdapter();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.three_col_listview, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        gridView.setAdapter(listAdapter);
        listAdapter.loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllSerialsOperation().callback(new Operation.Callback<List<Serial>, Void>(){
            @Override
            public void onSuccess(List<Serial> serials) {
                Log.d("kzz", "List size:" + serials.size());
            }
        }).enqueue();
    }

    class SerialListAdapter extends BaseAdapter {

        List<Serial> serials = new ArrayList<>();
        public void loadData(){
            new GetAllSerialsOperation().callback(new Operation.Callback<List<Serial>, Void>(){
                @Override
                public void onSuccessMainThread(List<Serial> serials) {
                    SerialListAdapter.this.serials = serials;
                    SerialListAdapter.this.notifyDataSetChanged();
                }
            }).enqueue();
        }
        @Override
        public int getCount() {
            return serials.size();
        }

        @Override
        public Object getItem(int i) {
            return serials.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Serial serial = serials.get(i);

            Context context = SerialListFragment.this.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.serial_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textView = (TextView) view.findViewById(R.id.title);
            imageView.setLayoutParams(getLayoutParams());
            ImageLoader.getInstance().displayImage(serial.coverUrl, new ImageViewAware(imageView));
            textView.setText(serial.name);
            return view;
        }

        private LinearLayout.LayoutParams getLayoutParams() {
            android.graphics.Point point = new android.graphics.Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(point);
            int displayWidth = point.x;
            int imageW = displayWidth / 3;
            int imageH = imageW * 4 / 3;
            return new LinearLayout.LayoutParams(imageW, imageH);
        }
    }
}

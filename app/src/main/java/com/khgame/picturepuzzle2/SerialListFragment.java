package com.khgame.picturepuzzle2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khgame.picturepuzzle.common.Operation;
import com.khgame.picturepuzzle.service.Serial;
import com.khgame.picturepuzzle.service.GetAllSerialsOperation;

import java.util.List;

/**
 * Created by zkang on 2017/1/7.
 */

public class SerialListFragment extends AbstractListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("serial list");
        return textView;
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
}

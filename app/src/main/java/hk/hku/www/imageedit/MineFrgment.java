package hk.hku.www.imageedit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MineFrgment extends TabFragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        view=inflater.inflate(R.layout.activity_imageuplading,container,false);
        return view;
    }
}

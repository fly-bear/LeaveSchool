package com.leaveschool.query;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leaveschool.R;
import com.leaveschool.main.DropEditText;

/**
 * Created by Administrator on 2018/3/17.
 */

public class DetailItem extends LinearLayout{

    private TextView mKeyView;
    private TextView mValueView;

    public DetailItem(Context context) {
        super(context);
        init();
    }

    public DetailItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DetailItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //
        LayoutInflater.from(getContext()).inflate(R.layout.detail_item,this, true);

        mKeyView = (TextView) findViewById(R.id.profile_key);
        mValueView = (TextView) findViewById(R.id.profile_value);
    }

    public void set(int visibility, int iconResId, String key, String value){
        setVisibility(visibility);
        mKeyView.setText(key);
        mValueView.setText(value);
    }
    public void updateValue(String value){
        mValueView.setText(value);
    }
}

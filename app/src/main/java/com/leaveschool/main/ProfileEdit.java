package com.leaveschool.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leaveschool.R;

/**
 * Created by Administrator on 2018/3/12.
 */

public class ProfileEdit extends LinearLayout {
    private ImageView mIconView;
    private TextView mKeyView;
    private DropEditText mValueView;
    private ImageView mRightArrowView;

    public ProfileEdit(Context context) {
        super(context);
        init();
    }

    public ProfileEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileEdit(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        // zz ????
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_edit,this, true);
        findAllViews();
    }
    private void findAllViews(){
//        mIconView = (ImageView) findViewById(R.id.profile_icon);
        mKeyView = (TextView) findViewById(R.id.profile_key);
        mValueView = (DropEditText) findViewById(R.id.profile_value);
//        mRightArrowView = (ImageView) findViewById(R.id.right_arrow);
    }
    public void set(int visibility, int iconResId, String key, String value){
        mValueView.getmDropImage().setVisibility(visibility);
//        mIconView.setImageResource(iconResId);
        mKeyView.setText(key);
        mValueView.setText(value);
    }
    public void updateValue(String value){
        mValueView.setText(value);
    }
    public String getValue(){
        return mValueView.getText().toString();
    }

//    protected void disableEdit(){
//        mRightArrowView.setVisibility(GONE);
//    }

    public TextView getmKeyView() {
        return mKeyView;
    }
    public DropEditText getmValueView() {
        return mValueView;
    }
}













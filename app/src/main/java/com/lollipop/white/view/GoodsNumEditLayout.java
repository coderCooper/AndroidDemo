package com.lollipop.white.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.lollipop.demo.R;
import com.lollipop.white.listener.IntResultListener;
import com.lollipop.white.util.ToastUtil;

public class GoodsNumEditLayout extends RelativeLayout {
    private ImageButton leftBtn;
    private EditText numTv;
    private ImageButton rightBtn;
    private int currentNum;
    private int maxNum;
    private IntResultListener listener;

    public GoodsNumEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.goods_num_input, this, true);
        leftBtn = (ImageButton) findViewById(R.id.btn_left);
        numTv = (EditText) findViewById(R.id.text);
        numTv.addTextChangedListener(textWatcher);
        rightBtn = (ImageButton) findViewById(R.id.btn_right);
        leftBtn.setOnClickListener(clickListener);
        rightBtn.setOnClickListener(clickListener);
        numTv.setSelection(numTv.getText().length());
        currentNum = 1;
    }

    public void setDisable() {
        leftBtn.setEnabled(false);
        rightBtn.setEnabled(false);
        numTv.setEnabled(false);
    }

    public void addListener(IntResultListener listener){
        this.listener = listener;
    }

    public void setCurNum(int num){
        currentNum = num;
        numTv.setText(String.valueOf(currentNum));
        numTv.setSelection(numTv.getText().length());
        if (currentNum <= 1){
            leftBtn.setEnabled(false);
        } else {
            leftBtn.setEnabled(true);
        }
        if (currentNum < maxNum){
            rightBtn.setEnabled(true);
        } else {
            rightBtn.setEnabled(false);
        }
    }

    public void setMaxNum(int num){
        if (num == 0) {
            num = 1;
        }
        maxNum = num;
        if (maxNum == 1){
            leftBtn.setEnabled(false);
            rightBtn.setEnabled(false);
            numTv.setEnabled(false);
        } else {
            leftBtn.setEnabled(true);
            rightBtn.setEnabled(true);
            numTv.setEnabled(true);
        }
        if (currentNum > num){
            currentNum = num;
            numTv.setText(String.valueOf(currentNum));
            numTv.setSelection(numTv.getText().length());
        }
        if (currentNum == 1){
            leftBtn.setEnabled(false);
        }
    }

    public int getCurrentNum() {
        String data = numTv.getText().toString().trim();
        if (TextUtils.isEmpty(data)){
            numTv.setText("1");
            numTv.setSelection(1);
            return 1;
        }
        int currentNum = Integer.valueOf(data);
        if (currentNum <= 0){
            numTv.setText("1");
            numTv.setSelection(1);
            return 1;
        }
        return currentNum;
    }

    private OnClickListener clickListener = new OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_left:
                    currentNum--;
                    if (currentNum <= 1){
                        leftBtn.setEnabled(false);
                    }
                    if (currentNum < maxNum){
                        rightBtn.setEnabled(true);
                    }
                    numTv.setText(String.valueOf(currentNum));
                    numTv.setSelection(numTv.getText().length());
                    if (listener != null){
                        listener.result(currentNum);
                    }
                    break;
                case R.id.btn_right:
                    currentNum++;
                    if (currentNum > 1){
                        leftBtn.setEnabled(true);
                    }
                    if (currentNum == maxNum){
                        rightBtn.setEnabled(false);
                    }
                    numTv.setText(String.valueOf(currentNum));
                    numTv.setSelection(numTv.getText().length());
                    if (listener != null){
                        listener.result(currentNum);
                    }
                    break;
            }
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(numTv.getWindowToken(), 0);
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String numTV = numTv.getText().toString();
            if (TextUtils.isEmpty(numTV)){
                leftBtn.setEnabled(false);
                rightBtn.setEnabled(true);
                return;
            }
            int num = 0;
            try {
                num = Integer.valueOf(numTV.trim());
            } catch (Exception e){
                ToastUtil.showShortText("请输入数字");
            }
            currentNum = num;
            if (currentNum == 1){
                leftBtn.setEnabled(false);
            } else {
                leftBtn.setEnabled(true);
            }
            if (currentNum < maxNum){
                rightBtn.setEnabled(true);
            } else {
                rightBtn.setEnabled(false);
            }
        }
    };
}
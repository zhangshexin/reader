package com.redread;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.redread.databinding.LayoutTestBinding;

/**
 * Created by zhangshexin on 2018/8/30.
 */

public class Activity_test extends Activity {
    private LayoutTestBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.layout_test);
       binding.buttonPdf.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intentPdf=new Intent(Activity_test.this,Activity_pdf.class);
               startActivity(intentPdf);
           }
       });
       binding.buttonTxt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intentTxt=new Intent(Activity_test.this,Activity_txt.class);
               startActivity(intentTxt);
           }
       });

    }
}

package com.zfg.largeimagedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_large;
    private Button btn_compress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_large = findViewById(R.id.btn_large);
        btn_compress = findViewById(R.id.btn_compress);

        btn_large.setOnClickListener(this);
        btn_compress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_large:
                startActivity(new Intent(MainActivity.this, LargeActivity.class));
                break;
            case R.id.btn_compress:
                startActivity(new Intent(MainActivity.this, CompressActivity.class));
                break;
            default:
                break;
        }
    }
}

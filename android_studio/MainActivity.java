package com.example.mikazuki;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private EditText op1,op2;
    private Button button_add,button_sub,button_mul,button_div;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        button_add.setOnClickListener(this);
        button_sub.setOnClickListener(this);
        button_mul.setOnClickListener(this);
        button_div.setOnClickListener(this);

    }
    private void initView(){
        op1 = findViewById(R.id.editText);
        op2 = findViewById(R.id.editText2);
        button_add = findViewById(R.id.button1); //找到组件
        button_sub = findViewById(R.id.button2);
        button_mul = findViewById(R.id.button3);
        button_div = findViewById(R.id.button4);

        result =findViewById(R.id.textView);

    }
    public void onClick(View v) {
        int a=Integer.parseInt(op1.getText().toString());
        int b=Integer.parseInt(op2.getText().toString());
        switch (v.getId()) {
            //Toast.makeText(this, String.valueOf(a), Toast.LENGTH_LONG).show();
            case R.id.button1:
                a=a+b;
                break;
            case R.id.button2:
                a=a-b;
                break;
            case R.id.button3:
                a=a*b;
                break;
            case R.id.button4:
                a=a/b;
                break;
        }
        result.setText(String.valueOf(a));
    }
}

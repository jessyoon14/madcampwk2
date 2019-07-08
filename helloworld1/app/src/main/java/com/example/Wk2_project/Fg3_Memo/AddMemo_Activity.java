package com.example.Wk2_project.Fg3_Memo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Wk2_project.R;

import java.util.ArrayList;

public class AddMemo_Activity extends AppCompatActivity {
    //ArrayList<MemoInfo> dInfoArrayList = new ArrayList<>();
    String mdate = null;
    ArrayList<String> mitem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        final EditText etDate = (EditText) findViewById(R.id.pdate);
        final EditText etItem = (EditText) findViewById(R.id.item);

        Intent i = getIntent();
        final String cur_date = i.getExtras().getString("date");
        etDate.setText(cur_date);
        // DB에 데이터 추가
        Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = etDate.getText().toString();
                String item = etItem.getText().toString();
                //int price = Integer.parseInt(etPrice.getText().toString());
                //mdate.add(date);
                mitem.add(item);
                Intent data = new Intent();
                data.putExtra("date", cur_date);
                data.putExtra("item", mitem);
                setResult(0,data);
                finish();
            }
        });

        Button exit = (Button) findViewById(R.id.onClick_exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "나가기", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("date", cur_date);
                data.putExtra("item", mitem);
                setResult(0,data);
                finish();
            }

        });


    }

}
package com.example.Wk2_project.Fg3_Memo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Wk2_project.DBHelper;
import com.example.Wk2_project.PreActivity;
import com.example.Wk2_project.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ModifyMemo_Activity extends AppCompatActivity {
    String email = PreActivity.user_email;
    String memo_id;
    String cur_date;
    String content;
    String item;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmemo);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "MemoBook30.db", null, 1);
       // final TextView result = (TextView) findViewById(R.id.result);

        final EditText etDate = (EditText) findViewById(R.id.pdate);
        final EditText etItem = (EditText) findViewById(R.id.item);
        //final EditText etPrice = (EditText) findViewById(R.id.price);

        Intent i = getIntent();
        memo_id = i.getExtras().getString("date").split("&&")[0];
        cur_date = i.getExtras().getString("date").split("&&")[1];
        content = i.getExtras().getString("date").split("&&")[2];
        etDate.setText(cur_date);
        etItem.setText(content, TextView.BufferType.EDITABLE);


        // DB에 있는 데이터 수정
        Button update = (Button) findViewById(R.id.edit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item = etItem.getText().toString(); //일정내용
                date = etDate.getText().toString();
                new JSONTask3().execute("http://143.248.38.245:7080/api/books/email/"+email+"/schedule");
                //int price = Integer.parseInt(etPrice.getText().toString());
                Toast.makeText(v.getContext(), "일정이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                //dbHelper.update(item, memo_id);
//                setResult(RESULT_OK);
                Intent resultIntent = new Intent();
                //resultIntent.putExtra("date2", date);
                setResult(1,resultIntent);
                finish();
                //result.setText(dbHelper.getResultof(cur_date));
            }
        });
//

        Button exit = (Button) findViewById(R.id.onClick_exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "나가기", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("date2", cur_date);

                setResult(1,data);
                finish();
            }

        });


    }
    public class JSONTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String urls[]) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", memo_id);
                jsonObject.accumulate("date", date);
                jsonObject.accumulate("schedule", item);
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("PUT");//PUT방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());


                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            finish();
        }
    }

}

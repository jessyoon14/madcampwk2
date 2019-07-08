package com.example.Wk2_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Wk2_project.Fg1_Contact.ContactInfo;
import com.example.Wk2_project.Fg3_Memo.Adapter_memo;
import com.example.Wk2_project.Fg3_Memo.AddMemo_Activity;
import com.example.Wk2_project.Fg3_Memo.DeleteMemo_Activity;
import com.example.Wk2_project.Fg3_Memo.MemoInfo;
import com.example.Wk2_project.Fg3_Memo.ModifyMemo_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    Adapter_memo myAdapter;
    ArrayList<MemoInfo> dInfoArrayList = new ArrayList<>();
    ArrayList<MemoInfo> MemoInfoArrayList = new ArrayList<>();
    String email = PreActivity.user_email;
    JSONArray jarray;
    JSONObject parray;
    String mdate;
    ArrayList<String> mitem;
    String dating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment3, container, false);
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = view.findViewById(R.id.recycler_view_memo);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext(), "MemoBook30.db", null, 1);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");


        final String[] cur_date = {simpleDateFormat.format(date)};
        dating = cur_date[0];

       // final TextView text_content = view.findViewById(R.id.text_content);
        final TextView date_text = view.findViewById(R.id.date_text);

//        text_content.setText(dbHelper.getResultof(cur_date[0]));
        String date_title = cur_date[0].split("-")[1]+" 월 "+cur_date[0].split("-")[2]+" 일" + "  일정";
        date_text.setText(date_title);
       // CalendarView calendar = (CalendarView)view.findViewById(R.id.calendar);
        myAdapter = new Adapter_memo(this.getContext(), MemoInfoArrayList);
        mRecyclerView.setAdapter(myAdapter);

        new JSONTask().execute("http://143.248.38.245:7080/api/books/date/" + cur_date[0] + "/email/" + email);

        myAdapter.setOnItemClickListener(new Adapter_memo.OnItemClickListener() {
        @Override
        public void onHandleSelection(final int position, int request_code, final ArrayList<MemoInfo> MemoInfoArrayList){
            switch (request_code) {
                case 1: {
                    Intent secondActivity = new Intent(getActivity(), ModifyMemo_Activity.class);
                    secondActivity.putExtra("date", MemoInfoArrayList.get(position).id + "&&" + MemoInfoArrayList.get(position).date + "&&" + MemoInfoArrayList.get(position).content);
                    startActivityForResult(secondActivity, request_code);
                    break;
                }
                case 2: {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("삭제");
                    builder.setMessage("해당 일정을 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getActivity(), DeleteMemo_Activity.class);
                                    i.putExtra("date", MemoInfoArrayList.get(position).id + "&&" + MemoInfoArrayList.get(position).date + "&&" + MemoInfoArrayList.get(position).content);

                                    startActivityForResult(i, 2);
                                    Toast.makeText(getActivity(), "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                }

                            });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        }
            // ... Start a new Activity here and pass the values
        });


        CalendarView calendar = (CalendarView)view.findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                // TODO Auto-generated method stub
                cur_date[0] = ""+year+"-"+(month+1)+"-" +dayOfMonth;

                Log.i("선택",cur_date[0]);

                String date_title = (month+1)+ " 월 " + dayOfMonth + " 일 일정";
                date_text.setText(date_title);
                dating = cur_date[0];
                Log.i("dating", dating);
                new JSONTask().execute("http://143.248.38.245:7080/api/books/date/" + dating + "/email/" + email);

                //dInfoArrayList = dbHelper.getResultof(cur_date[0]);
                //MemoInfoArrayList.clear();
                //for (int i=0; i<dInfoArrayList.size(); i++){
                //    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
                //}
                //myAdapter.notifyDataSetChanged();


            }



            });

//        Button modify = (Button) view.findViewById(R.id.bt_edit);
//        modify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddMemo_Activity.class);
                i.putExtra("date", cur_date[0]);
                startActivityForResult(i,0);
                //v.getContext().startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext(), "MemoBook30.db", null, 1);
        switch (requestCode){
            case 0:
                mdate = data.getStringExtra("date");
                mitem = data.getStringArrayListExtra("item");
                new JSONTask1().execute("http://143.248.38.245:7080/api/books/schedule/"+email);
                dating = mdate;
                //go to JSONTask1 postexcute
                //dInfoArrayList = dbHelper.getResultof(mdate);
                //MemoInfoArrayList.clear();
                //for (int i=0; i<dInfoArrayList.size(); i++){
                //    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
                //}
                //myAdapter.notifyDataSetChanged();
                break;
            case 1:
                String mdate2 = data.getStringExtra("date2");


                dInfoArrayList = dbHelper.getResultof(mdate2);
                MemoInfoArrayList.clear();
                for (int i=0; i<dInfoArrayList.size(); i++){
                    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
                }
                myAdapter.notifyDataSetChanged();

                break;

            case 2:
                String mdate3 = data.getStringExtra("date3");
                new JSONTask().execute("http://143.248.38.245:7080/api/books/date/" + dating + "/email/" + email);
                //dInfoArrayList = dbHelper.getResultof(mdate3);
                MemoInfoArrayList.clear();
                //int i;
                //for (i=0; i<dInfoArrayList.size(); i++){
                //    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
        //}
                //Log.i("dd",Integer.toString(i));
                myAdapter.notifyDataSetChanged();

        }


    }
//DB에서 schedule불러오기
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                jarray = new JSONArray(result);
                   // JSONArray 생성
                MemoInfoArrayList.clear();
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    String id = jObject.getString("_id");
                    String date = jObject.getString("date");
                    String content= jObject.getString("schedule");

                    MemoInfoArrayList.add(new MemoInfo(id, date, content));
                    dInfoArrayList.add(new MemoInfo(id, date, content));

                }
                myAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    //DB에 일정 추가(mdate, item)
    public class JSONTask1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String urls[]) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("date", mdate);
                jsonObject.accumulate("schedule", mitem);
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
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
            //dInfoArrayList = dbHelper.getResultof(mdate);
            new JSONTask().execute("http://143.248.38.245:7080/api/books/date/" + dating + "/email/" + email);
        }
    }


}

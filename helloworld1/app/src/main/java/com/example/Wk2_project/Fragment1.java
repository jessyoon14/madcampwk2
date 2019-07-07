package com.example.Wk2_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Wk2_project.Fg1_Contact.ContactInfo;
import com.example.Wk2_project.Fg1_Contact.ContactaddActivity;
import com.example.Wk2_project.Fg1_Contact.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    RecyclerView mRecyclerView;
    EditText editSearch;
    Button addbutton;
    ArrayList<ContactInfo> dInfoArrayList = new ArrayList<>();
    ArrayList<ContactInfo> contactInfoArrayList = new ArrayList<>();
    public MyAdapter myAdapter;
    String address;
    String mname;
    String mphnumber;
    String mdate;
    JSONArray jarray;
    JSONObject parray;
    String email = PreActivity.user_email;



    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public Fragment1(){

    }
    public void getContactList() {
        Log.i("email", email);
        new JSONTask().execute("http://143.248.36.28:7080/api/books/" + email);
        dInfoArrayList.clear();
        contactInfoArrayList.clear();
/*
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, projection, null,
                selectionArgs, sortOrder);
        address = "[";
        if (cursor.moveToFirst()) {
            do {
                long photo_id = cursor.getLong(2);
                long person_id = cursor.getLong(3);
                String name = cursor.getString(1);
                String phone = cursor.getString(0);
                String x = "{" + "'name':" + "'" + name + "'" + ","+"'phone':" + "'" + phone + "'," +"'image_id':" +"'" + String.valueOf(photo_id) +"',"+ "'id':"+"'" + String.valueOf(person_id) +"'}, ";
                address += x;
            } while (cursor.moveToNext());
        }
        address = address.substring(0,address.length()-2);
        address += "]";
        */
        //return address;
    }
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
                parray = new JSONObject(result);
                jarray = parray.getJSONArray("contact_list");   // JSONArray 생성
                contactInfoArrayList.clear();
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    String id = jObject.getString("_id");
                    String name = jObject.getString("name");
                    String phnumber = jObject.getString("phnumber");
                    String date = jObject.getString("date");

                    contactInfoArrayList.add(new ContactInfo(id, name, phnumber, date));
                    dInfoArrayList.add(new ContactInfo(id, name, phnumber, date));
                    myAdapter.notifyDataSetChanged();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView.LayoutManager mLayoutManager;

        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        editSearch = view.findViewById(R.id.editSearch);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        addbutton = view.findViewById(R.id.bt_add);



        //String address = getContactList();

        /*
        String address =
                "[{'name':'김현석', 'phone':'010-0000-0000'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'이현석', 'phone':'010-1111-1111'},"+
                        "{'name':'양현석', 'phone':'010-2222-2222'}]";
*/
        /*
        try {
            JSONArray jarray = new JSONArray(address);   // JSONArray 생성
            contactInfoArrayList.clear();
            dInfoArrayList.clear();
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String phone = jObject.getString("phone");
                String name = jObject.getString("name");
                Long id = jObject.getLong("id");
                Long image_id = jObject.getLong("image_id");

                contactInfoArrayList.add(new ContactInfo(id, image_id, name, phone));
                dInfoArrayList.add(new ContactInfo(id, image_id, name, phone));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

*/
        myAdapter = new MyAdapter(contactInfoArrayList);
        mRecyclerView.setAdapter(myAdapter);
        getContactList();
        addbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(view.getContext(), ContactaddActivity.class);
                startActivityForResult(i,0);
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.fab3);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().recreate();
            }
        });

        return view;
    }
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        contactInfoArrayList.clear();
        charText = charText.toLowerCase();
        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            contactInfoArrayList.addAll(dInfoArrayList);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < dInfoArrayList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (dInfoArrayList.get(i).name.toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    contactInfoArrayList.add(dInfoArrayList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        myAdapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity().recreate();
    }
}
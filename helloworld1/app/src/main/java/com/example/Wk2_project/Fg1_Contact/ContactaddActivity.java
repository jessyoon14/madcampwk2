package com.example.Wk2_project.Fg1_Contact;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Wk2_project.PreActivity;
import com.example.Wk2_project.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ContactaddActivity extends AppCompatActivity {
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    ArrayList<ContactInfo> contactInfoArrayList = new ArrayList<>();
    EditText name;
    EditText phnumber;
    EditText date;
    String mname;
    String mphnumber;
    String mdate;
    String email = PreActivity.user_email;
    ImageView cimage;
    Intent intent;
    String ImageDecode;
    String tempimage;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactadd);

        Button btn_add = findViewById(R.id.bt_add);
        Button btn_get = findViewById(R.id.bt_get);
        name = findViewById(R.id.name);
        phnumber = findViewById(R.id.phnumber);
        date = findViewById(R.id.pdate);
        cimage = findViewById(R.id.imaging);
/*
        cimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, 10);
            }
        });
*/
        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mname = name.getText().toString();
                mphnumber = phnumber.getText().toString();
                mdate = date.getText().toString();
                try {
                    new JSONTask1().execute("http://143.248.38.245:7080/api/books/"+email).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        btn_get.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

                startActivityForResult(intent, 5);


            }
        });

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 10 && resultCode == RESULT_OK
                    && null != data) {
                Uri myuri = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(myuri,
                        FILE, null, null, null);

                cursor.moveToFirst();
                Log.i("sugi", "entered choose image");
                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                if (ImageDecode == null) Log.i("imageDecode", "ImageDecode is null error");

                Log.i("imageDecode", "ImageDecode: " + ImageDecode);
                ByteArrayOutputStream baos = new  ByteArrayOutputStream();
                BitmapFactory.decodeFile(ImageDecode).compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                tempimage = Base64.encodeToString(b, Base64.DEFAULT);

                //cimage.setImageURI(myuri);
                Log.i("imageasdf", tempimage);
                cursor.close();
                baos.close();
                cimage.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }


        if(resultCode == RESULT_OK && requestCode == 5)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

            cursor.moveToFirst();
            mname = cursor.getString(0);        //0은 이름을 얻어옵니다.
            mphnumber = cursor.getString(1);   //1은 번호를 받아옵니다.
            mdate = "date";
            cursor.close();
            new JSONTask1().execute("http://143.248.38.245:7080/api/books/"+email);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class JSONTask1 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String urls[]) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("image", tempimage);
                jsonObject.accumulate("name", mname);
                jsonObject.accumulate("phnumber", mphnumber);
                jsonObject.accumulate("date", mdate);
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
            Intent data = new Intent();
            setResult(0,data);
            finish();
        }
    }
}

package com.example.Wk2_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment2 extends Fragment {
    private ArrayList<String> images;
    private ArrayList<String> files = new ArrayList<>();
    Button button1;
    GridView gallery;
    public static TextView instaname;
    public static CircularImageView profilepic;
    JSONArray jarray = new JSONArray();
    private static final int GALLERY_REQUEST_CODE = 10;
    private View view;
    Uri uri;


    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttach(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_fragment2, container, false);
        // Inflate the layout for this fragment
        button1 = view.findViewById(R.id.button);
        profilepic = view.findViewById(R.id.profilepic);
        if (PreActivity.profilep != null) profilepic.setImageBitmap(PreActivity.profilep);
        instaname = view.findViewById(R.id.instaname);
        if (PreActivity.user_name != null) instaname.setText(PreActivity.user_name);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
        //get json-list of all images in server
        new JSONTask().execute("http://143.248.36.28:5000/files");
        gallery = (GridView) view.findViewById(R.id.galleryGridView);

        return view;
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    uri = selectedImage;
                    new UploadFile().execute(selectedImage);
//                    try {
//                        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), selectedImage);
//                        new UploadFile();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    break;
            }
    }

    public void showImages(){
        gallery.setAdapter(new ImageAdapter((Activity) requireActivity()));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getContext(),
                            "position " + position + " " + images.get(position),
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            Activity mActivity =(Activity) context;
        }
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
            Log.d("My tag", result);
            try {
                jarray = new JSONArray(result);   // JSONArray 생성
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    String name = jObject.getString("filename");
                    files.add(name);
                    //later add something more
                }
                Collections.reverse(files);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("tag", files.toString());

            showImages();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Bitmap, Bitmap> {
        public DownloadImageTask(ImageView bmImage1) {
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
//            gallery.setAdapter(new ImageAdapter((Activity) requireActivity()));
//            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1,
//                                        int position, long arg3) {
//                    if (null != images && !images.isEmpty())
//                        Toast.makeText(
//                                getContext(),
//                                "position " + position + " " + images.get(position),
//                                Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//            if (result!= null)
//                return result;
        }
    }

    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = files;
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(530, 530));

            } else {
                picturesView = (ImageView) convertView;
            }


            //String filename = images.get(0);
            //images.remove(0);
            Glide.with(context).load("http://143.248.36.28:5000/files/" + images.get(position))//.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
                    .into(picturesView);
//
//            if (images.size() >0 ) {
//                String filename = images.get(0);
//                images.remove(0);
//                Glide.with(context).load("http://143.248.36.28:5000/files/" + filename)
//                        .placeholder(R.drawable.ic_launcher_background).centerCrop()
//                        .into(picturesView);
//            }
//            Glide.with(context).load("http://143.248.36.28:5000/files/nandroid.jpg.jpg")//load(images.get(position))
//                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
//                    .into(picturesView);

            return picturesView;
        }
    }

    public class UploadFile extends AsyncTask<Uri, String, String> {
        String file_name = "file";
        Uri uri;
        @Override
        protected String doInBackground(Uri[] params) {
            try {
                uri = params[0];
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024 * 1024;
                //todo change URL as per client ( MOST IMPORTANT )
                URL url = new URL("http://143.248.36.28:5000/upload");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs &amp; Outputs.
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                // Set HTTP method to POST.
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                FileInputStream fileInputStream;
                DataOutputStream outputStream;
                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);

//                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\""+ lineEnd);//("Content-Disposition: form-data; name=\"reference\""+ lineEnd);
//                outputStream.writeBytes(lineEnd);
//                outputStream.writeBytes("my_refrence_text");
//                outputStream.writeBytes(lineEnd);
//                outputStream.writeBytes(twoHyphens + boundary + lineEnd);

                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + uri.getLastPathSegment() +"\"" + lineEnd);//used to be name=\"uploadFile\"

                outputStream.writeBytes(lineEnd);

                String[] proj = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
                cursor.moveToNext();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

                fileInputStream = new FileInputStream(path);//(uri.getPath());
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = connection.getResponseCode();
                String result = null;
                if (serverResponseCode == 200) {
                    StringBuilder s_buffer = new StringBuilder();
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        s_buffer.append(inputLine);
                    }
                    result = s_buffer.toString();
                }
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
                if (result != null) {
                    Log.d("result_for upload", result);
                    //file_name = getDataFromInputStream(result, "file_name");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file_name;
        }
        protected void onPostExecute(String result) {
            getActivity().recreate();
            //new JSONTask().execute("http://143.248.36.28:5000/files");

        }
    }


    public class JSONTaskUpdate extends AsyncTask<String, String, String> {

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
            Log.d("My tag", result);
            try {
                jarray = new JSONArray(result);   // JSONArray 생성
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    String name = jObject.getString("filename");
                    files.add(name);
                    //later add something more
                }
                Collections.reverse(files);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("tag", files.toString());

//            ImageView picturesView;
//            if (convertView == null) {
//                picturesView = new ImageView(context);
//                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                picturesView
//                        .setLayoutParams(new GridView.LayoutParams(270, 270));
//
//            } else {
//                picturesView = (ImageView) convertView;
//            }

//            Glide.with(getContext()).load("http://143.248.36.28:5000/files/" + images.get(files.get(0)))
//                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
//                    .into(picturesView);
        }

    }
}
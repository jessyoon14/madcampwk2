package com.example.Wk2_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.Wk2_project.Retrofit.IMyService;
import com.example.Wk2_project.Retrofit.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

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
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

//import static com.example.Wk2_project.Fragment2.profilepic;

///https://www.youtube.com/watch?time_continue=114&v=4Xq2FUJvE-c
public class PreActivity extends AppCompatActivity {
    public static String user_email;
    public static String user_name;
    TextView txt_create_account, displayName, emailID;
    ImageView displayImage;
    public static Bitmap profilep;
    MaterialEditText edt_login_email,edt_login_password;
    Button btn_login;
    private String userEmail;
    LoginButton loginButton;
    String register_email;
    public AccessToken accessToken = null;
    boolean isLoggedIn;
    
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    private static final String EMAIL = "email";

    CallbackManager callbackManager;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        accessToken = AccessToken.getCurrentAccessToken();
        setContentView(R.layout.activity_pre);
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //Init view
        edt_login_email = (MaterialEditText) findViewById(R.id.edt_email);
        edt_login_password = (MaterialEditText) findViewById(R.id.edt_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loginUser(edt_login_email.getText().toString(),
                        edt_login_password.getText().toString());
            }
        });

        txt_create_account = (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                final View register_layout = LayoutInflater.from(PreActivity.this)
                        .inflate(R.layout.register_layout, null);

                new MaterialStyledDialog.Builder(PreActivity.this)
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edt_register_email = (MaterialEditText) register_layout.findViewById(R.id.edt_email);
                                MaterialEditText edt_register_name = (MaterialEditText) register_layout.findViewById(R.id.edt_name);
                                MaterialEditText edt_register_password = (MaterialEditText) register_layout.findViewById(R.id.edt_password);
                                if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                                    Toast.makeText(PreActivity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
                                    Toast.makeText(PreActivity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                                    Toast.makeText(PreActivity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(edt_register_email.getText().toString(),
                                        edt_register_name.getText().toString(),
                                        edt_register_password.getText().toString());
                            }
                        }).show();
            }
        });

        //FACEBOOK LOGIN
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL, "public_profile"));
        //displayName = findViewById(R.id.displayName);
        //emailID = findViewById(R.id.emailID);
        //displayImage=findViewById(R.id.displayImage);
        LoginManager.getInstance().logOut();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(PreActivity.this, "Facebook Login successful", Toast.LENGTH_SHORT).show();
                        //Intent i = new Intent(PreActivity.this, MainActivity.class);
                        //user_email = emailID.getText().toString();

                        //startActivity(i);
                        //finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(PreActivity.this, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(PreActivity.this, "Facebook Login Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //App code
                Toast.makeText(PreActivity.this, "Facebook Login successful", Toast.LENGTH_SHORT).show();
                // Retrieving access token using the LoginResult
                AccessToken accessToken = loginResult.getAccessToken();
                useLoginInformation(accessToken);

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(PreActivity.this, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(PreActivity.this, "Facebook Login Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void registerUser(final String email, String name, String password ){
        compositeDisposable.add(iMyService.registerUser(email, name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(PreActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        Log.i("before", response);
                        if (response.equals("\"Registration success\"")){
                            Log.i("after", response);
                            //make a new DB for this user!!!!!
                            register_email = email;
                            new JSONTask6().execute("http://143.248.38.245:7080/api/books");

                        }
                    }

                }));
    }
    private void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(email,password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String response) throws Exception {
                Toast.makeText(PreActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                Log.i("bcd", response);
                if (response.equals("\"Login success\"")){
                    Log.i("asd", response);
                    Intent i = new Intent(PreActivity.this, MainActivity.class);
                    user_email = edt_login_email.getText().toString();
                    startActivity(i);
                    finish();
                }
            }

        }));
    }

    private void useLoginInformation(AccessToken accessToken) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    //displayName.setText(name);
                    //emailID.setText(email);
                    //userEmail = email;
                    new DownloadImageTask(displayImage).execute(image);
                    Intent i = new Intent(PreActivity.this, MainActivity.class);
                    user_email = email;//emailID.getText().toString();
                    user_name = name;
                    //Fragment2.instaname.setText(name);
                    register_email = user_email;
                    new JSONTask6().execute("http://143.248.38.245:7080/api/books");
                    startActivity(i);
                    finish();
                    //displayImage.setImage;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        protected void onPreExecute() {
            //displayImage.setImageResource(R.drawable.ic_launcher_background);
        }
        public DownloadImageTask(ImageView bmImage1) {
            this.bmImage = bmImage1;
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
            profilep = result;
            Fragment2.profilepic.setImageBitmap(result);
            //if (result!= null)
                //bmImage.setImageBitmap(result);
        }
    }
    public class JSONTask6 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String urls[]) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("email", register_email);

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
        }
    }

}

package com.example.dbservertest.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    public static Retrofit getInstance() {
        if (instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/") //in emulator, localhost will change to 10.0.2.2 //("http://143.248.36.28:3000")//
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
//
//    public static Retrofit getUserDB(String email) {
//        if (instance == null)
//            instance = new Retrofit.Builder()
//                    .baseUrl("http://10.0.2.2:3000/" + email) //in emulator, localhost will change to 10.0.2.2 //("http://143.248.36.28:3000")//
//                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
//        return instance;
//    }
}

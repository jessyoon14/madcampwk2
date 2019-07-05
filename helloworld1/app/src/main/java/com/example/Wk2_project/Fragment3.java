package com.example.Wk2_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.Wk2_project.Fg3_Memo.Adapter_memo;
import com.example.Wk2_project.Fg3_Memo.AddMemo_Activity;
import com.example.Wk2_project.Fg3_Memo.DeleteMemo_Activity;
import com.example.Wk2_project.Fg3_Memo.MemoInfo;
import com.example.Wk2_project.Fg3_Memo.ModifyMemo_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d");


        final String[] cur_date = {simpleDateFormat.format(date)};

       // final TextView text_content = view.findViewById(R.id.text_content);
        final TextView date_text = view.findViewById(R.id.date_text);

//        text_content.setText(dbHelper.getResultof(cur_date[0]));
        String date_title = cur_date[0].split("/")[1]+" 월 "+cur_date[0].split("/")[2]+" 일" + "  일정";
        date_text.setText(date_title);
       // CalendarView calendar = (CalendarView)view.findViewById(R.id.calendar);

        dInfoArrayList = dbHelper.getResultof(cur_date[0]);
        for (int i=0; i<dInfoArrayList.size(); i++){
            MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
        }

        myAdapter = new Adapter_memo(this.getContext(), MemoInfoArrayList);
        mRecyclerView.setAdapter(myAdapter);
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
                cur_date[0] = ""+year+"/"+(month+1)+"/" +dayOfMonth;

                Log.i("선택",cur_date[0]);

                String date_title = (month+1)+ " 월 " + dayOfMonth + " 일 일정";
                date_text.setText(date_title);

                dInfoArrayList = dbHelper.getResultof(cur_date[0]);
                MemoInfoArrayList.clear();
                for (int i=0; i<dInfoArrayList.size(); i++){
                    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
                }
                myAdapter.notifyDataSetChanged();


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
                String mdate = data.getStringExtra("date");
                ArrayList<String> mitem = data.getStringArrayListExtra("item");
                for(int e=0; e<mitem.size(); e++){
                    Log.i("asd",mdate);
                    Log.i("asd",mitem.get(e));
                    dbHelper.insert(mdate, mitem.get(e));
                }

                dInfoArrayList = dbHelper.getResultof(mdate);
                MemoInfoArrayList.clear();
                for (int i=0; i<dInfoArrayList.size(); i++){
                    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
                }
                myAdapter.notifyDataSetChanged();
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
                dInfoArrayList = dbHelper.getResultof(mdate3);
                MemoInfoArrayList.clear();
                int i;
                for (i=0; i<dInfoArrayList.size(); i++){
                    MemoInfoArrayList.add(new MemoInfo(dInfoArrayList.get(i).id, dInfoArrayList.get(i).date,dInfoArrayList.get(i).content));
        }
                Log.i("dd",Integer.toString(i));
                myAdapter.notifyDataSetChanged();

        }


    }


}

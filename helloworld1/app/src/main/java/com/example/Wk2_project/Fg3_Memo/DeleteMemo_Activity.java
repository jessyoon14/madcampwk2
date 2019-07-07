package com.example.Wk2_project.Fg3_Memo;


        import android.content.Intent;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;

        import com.example.Wk2_project.DBHelper;

public class DeleteMemo_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "MemoBook30.db", null, 1);
        // final TextView result = (TextView) findViewById(R.id.result);

        Intent i = getIntent();
        final String memo_id = i.getExtras().getString("date").split("&&")[0];
        final String date = i.getExtras().getString("date").split("&&")[1];

        dbHelper.delete(memo_id);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("date3", date);
        setResult(2,resultIntent);
        //result.setText(dbHelper.getResultof(cur_date));
        finish();

    }

}

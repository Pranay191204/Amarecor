package com.amarecor.pranay.amarecor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amarecor.pranay.amarecor.R;

public class Report extends AppCompatActivity {
    TextView t1;
    Button b1,b2;
   // ImageButton btnAdd,btnAdd1;
    SharedPreferences sp;
    SharedPreferences.Editor et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        t1=(TextView)findViewById(R.id.textView2);
        b1=(Button)findViewById(R.id.button4);
        b2=(Button)findViewById(R.id.button5);
        // btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        // btnAdd1 = (ImageButton) findViewById(R.id.btnAdd1);

        SharedPreferences sp=getSharedPreferences("Session",MODE_PRIVATE);
        final String Ename = sp.getString("EmpName", "");
        final String Erole = sp.getString("EmpRole", "");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Report.this,AddReports.class);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Erole.equals("1"))
                {
                    Intent i = new Intent(Report.this,ReportView.class);
                    startActivity(i);
                }
                else if (Erole.equals("2"))
                {
                    Intent i = new Intent(Report.this,ReportView.class);
                    i.putExtra("name",Ename);
                    startActivity(i);
                }
                else if (Erole .equals("3"))
                {
                    Intent i = new Intent(Report.this,ReportView.class);
                    i.putExtra("name",Ename);
                    startActivity(i);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            et.clear();
            et.commit();
            startActivity(new Intent(Report.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(Report.this,MainActivity.class));
            finish();
        }
        return true;

    }
}
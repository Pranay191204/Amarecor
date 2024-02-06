package com.amarecor.pranay.amarecor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.amarecor.pranay.amarecor.R;

public class ViewingExp extends AppCompatActivity {
    Button b7,b9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_exp);
        b7=(Button)findViewById(R.id.button7);
        b9=(Button)findViewById(R.id.button9);
       // SharedPreferences sp;
      //  SharedPreferences.Editor et;

        SharedPreferences sp=getSharedPreferences("Session",MODE_PRIVATE);
        final String Ename = sp.getString("EmpName", "");
        final String Erole = sp.getString("EmpRole", "");

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Erole.equals("1"))
                {
                    Intent i = new Intent(ViewingExp.this,SeeExpenseActivity.class);
                    startActivity(i);
                }
                else if (Erole.equals("2"))
                {
                    Intent i = new Intent(ViewingExp.this,SeeExpenseActivity.class);
                    i.putExtra("name",Ename);
                    startActivity(i);
                }
                else if (Erole .equals("3"))
                {
                    Intent i = new Intent(ViewingExp.this,SeeExpenseActivity.class);
                    i.putExtra("name",Ename);
                    startActivity(i);
                }


            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Erole.equals("1"))
                {
                    Intent i1=new Intent(ViewingExp.this,SeeExpenseOutActivity.class);
                    startActivity(i1);
                }
                else if (Erole.equals("2"))
                {
                    Intent i1=new Intent(ViewingExp.this,SeeExpenseOutActivity.class);
                    i1.putExtra("name",Ename);
                    startActivity(i1);
                }
                else if (Erole .equals("3"))
                {
                    Intent i1=new Intent(ViewingExp.this,SeeExpenseOutActivity.class);
                    i1.putExtra("name",Ename);
                    startActivity(i1);
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
         //   et.clear();
          //  et.commit();
            startActivity(new Intent(ViewingExp.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(ViewingExp.this,MainActivity.class));
            finish();
        }
        return true;
    }
}

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

public class Expense extends AppCompatActivity {
    TextView t1;
    Button b1,b2,b3;
    SharedPreferences sp;
    SharedPreferences.Editor et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        t1=(TextView)findViewById(R.id.textView2);
        b1=(Button)findViewById(R.id.button4);
        b2=(Button)findViewById(R.id.button5);
        b3=(Button)findViewById(R.id.button6);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Expense.this,AddExpense.class);
                startActivity(i);
                finish();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Expense.this,AddExpenseHq.class);
                startActivity(i);
                finish();
            }
        });

        sp=getSharedPreferences("Session",MODE_PRIVATE);
        et=sp.edit();
        final String Ename = sp.getString("EmpName", "");
        final String Erole = sp.getString("EmpRole", "");

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Erole.equals("1"))
                {
                    Intent i = new Intent(Expense.this,ViewingExp.class);
                    startActivity(i);
                    finish();
                }
                else if (Erole.equals("2"))
                {
                    Intent i = new Intent(Expense.this,ViewingExp.class);
                    i.putExtra("name",Ename);
                    startActivity(i);finish();
                }
                else if (Erole .equals("3"))
                {
                    Intent i = new Intent(Expense.this,ViewingExp.class);
                    i.putExtra("name",Ename);
                    startActivity(i); finish();
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
            startActivity(new Intent(Expense.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(Expense.this,MainActivity.class));
            finish();
        }
        return true;

    }
}



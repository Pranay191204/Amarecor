package com.amarecor.pranay.amarecor;

import android.Manifest;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.TextView;

        import com.amarecor.pranay.amarecor.R;

public class HomePage extends AppCompatActivity {
    private CardView reportcv,expensecv;
    Toolbar toolbar;

    SharedPreferences sp;
    SharedPreferences.Editor et;
TextView tv,tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    sp = getSharedPreferences("Session", MODE_PRIVATE);
                    et = sp.edit();
                    String Ename = sp.getString("EmpName", "");
                    String Erole = sp.getString("EmpRole", "");
                    String role="";
                    if(Erole.equals("1"))
                    {
                        role="Admin";
                    } else if (Erole.equals("2")) {
                        role="Manager";
                    } else if (Erole .equals("3")) {
                        role="Medical Representative";
                    }
                    tv=(TextView)findViewById(R.id.textView4);
                    tv1=(TextView)findViewById(R.id.textView);
                    tv2=(TextView)findViewById(R.id.textView5);
                    tv.setText("Welcome,");
                    tv1.setText(Ename);
                    tv2.setText(role);
                    reportcv = (CardView)findViewById(R.id.repid);

                    expensecv = (CardView)findViewById(R.id.expid);



                    //add onclick listner
                    reportcv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent  i = new Intent(HomePage.this,Report.class);
                            startActivity(i);
                        }
                    });

                    expensecv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent  i = new Intent(HomePage.this,Expense.class);
                            startActivity(i);
                        }
                    });

                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId()==R.id.logout)
       {
           et.clear();
           et.commit();
           startActivity(new Intent(HomePage.this,MainActivity.class));
           finish();
       }
        return true;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(this,HomePage.class));
            finish();
         }
         else
        {
            startActivity(new Intent(this,HomePage.class));
            finish();
        }
    }
}

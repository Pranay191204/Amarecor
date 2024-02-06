package com.amarecor.pranay.amarecor;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amarecor.pranay.amarecor.R;

public class OutsideExpenseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_expense_view);
        TabLayout tbl=(TabLayout) findViewById(R.id.tablay);
        tbl.addTab(tbl.newTab().setText("Expense Details"));
        tbl.addTab(tbl.newTab().setText("Location Details"));
        tbl.addTab(tbl.newTab().setText("Other Details"));
        tbl.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager vp=(ViewPager)findViewById(R.id.viewpage);

        PageAdapterExp pageAdapterexp=new PageAdapterExp(getSupportFragmentManager(),tbl.getTabCount());

        vp.setAdapter(pageAdapterexp);

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tbl));

        tbl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
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
          //  et.clear();
           // et.commit();
            startActivity(new Intent(OutsideExpenseView.this,MainActivity.class));
            finish();
        }
        else{
            if(item.getItemId()==R.id.home)
                startActivity(new Intent(OutsideExpenseView.this,MainActivity.class));
            finish();
        }
        return true;
    }
}

package com.amarecor.pranay.amarecor;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amarecor.pranay.amarecor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseDetails extends Fragment {
    String id;
    JSONObject json1=null;
    ProgressDialog pd;
    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,e11;
    String expid;
    String Url1="http://amarecor.com/AndroidFiles/viewfullexp.php";


    public AddExpenseDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V=inflater.inflate(R.layout.fragment_add_expense_details, container, false);

        e1=(EditText)V.findViewById(R.id.Expid);
        e2=(EditText)V.findViewById(R.id.empid);
        e3=(EditText)V.findViewById(R.id.editText42);
        e4=(EditText)V.findViewById(R.id.hq);
       // e5=(EditText)V.findViewById(R.id.role);
        e5=(EditText)V.findViewById(R.id.expdate);
        e6=(EditText)V.findViewById(R.id.sr1);
        e7=(EditText)V.findViewById(R.id.expdetail);
        e8=(EditText)V.findViewById(R.id.exprs);
        e9=(EditText)V.findViewById(R.id.dailyallow);
        e10=(EditText)V.findViewById(R.id.totalexp);
        e11=(EditText)V.findViewById(R.id.remark);

        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("expid");
      //  new Basic.getRepData().execute(id);
        new AddExpenseDetails.getAllData().execute(id);
        // Inflate the layout for this fragment
        return V;
    }

    private class getAllData extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(getActivity());
            pd.setTitle("Loading");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String rep_id = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("expid", rep_id));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url1);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("Emp data11", str);
                JSONArray jsonArray1 = new JSONArray(str);
                json1 =jsonArray1.getJSONObject(0);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            try {
                //repid=json1.getString("rep_srno");
//
//                Log.e("repid",expid);
                String Exp_No = json1.getString("Exp_No");
                String emp_id = json1.getString("emp_id");
                String Emp_name = json1.getString("Emp_name");
                String hq = json1.getString("hq");
              //  String emp_role = json1.getString("emp_role");
                String Date = json1.getString("Date");
                String exp_place = json1.getString("exp_place");
                String other_exp = json1.getString("other_exp");
                String other_exp_rs = json1.getString("other_exp_rs");
                String exp_da = json1.getString("exp_da");
                String exp_finaltotal = json1.getString("exp_total");
                String remark = json1.getString("remark");

                e1.setText(Exp_No);
                e2.setText(emp_id);
                e3.setText(Emp_name);
                e4.setText(hq);
              //  e5.setText(emp_role);
                e5.setText(Date);
                e6.setText(exp_place);
                e7.setText(other_exp);
                e8.setText(other_exp_rs);
                e9.setText(exp_da);
                e10.setText(exp_finaltotal);
                e11.setText(remark);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}

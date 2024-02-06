package com.amarecor.pranay.amarecor;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.amarecor.pranay.amarecor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class Other extends Fragment {
    ListView listView3;
    String id;
    ArrayList<HashMap<String,String>> al=new ArrayList<>();
    String Url1="http://amarecor.com/AndroidFiles/getexpoutsidefull.php";
    JSONObject json;

    public Other() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_other, container, false);
        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("expid");

        listView3 = (ListView) v.findViewById(R.id.listview3);
        //listView1.setAdapter(null);
       // Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        new getSqlDetails3().execute(id);

        return v;

    }
    private class getSqlDetails3 extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            String tempname = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("exp_id", tempname));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url1);
                hp.setEntity(new UrlEncodedFormEntity(li));
                HttpResponse hr = hc.execute(hp);
                String str = EntityUtils.toString(hr.getEntity(), "UTF-8");
                Log.e("Emp data", str);
                JSONArray jsonarray = new JSONArray(str);
                al.clear();
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject json = jsonarray.getJSONObject(i);
                    HashMap<String,String> hm=new HashMap<>();
                    hm.put("place",json.getString("place"));
                    hm.put("otherexpname",json.getString("otherexpname"));
                    hm.put("otherexprs",json.getString("otherexprs"));
                    hm.put("dailyallowance",json.getString("dailyallowance"));
                    hm.put("finalTotal",json.getString("finalTotal"));
                    hm.put("remark",json.getString("remark"));
                    al.add(hm);
                }

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
            //d pd.dismiss();
            String from[]={"place","otherexpname","otherexprs","dailyallowance","finalTotal","remark"};
            int to[]={R.id.place,R.id.otherexpdetail,R.id.otherexprs,R.id.editText69,R.id.finaltotal,R.id.remark};
            SimpleAdapter sa1=new SimpleAdapter(getActivity(),al,R.layout.otheritem,from,to);
            listView3.setAdapter(sa1);

        }
    }
}

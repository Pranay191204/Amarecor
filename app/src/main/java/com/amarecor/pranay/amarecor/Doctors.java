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

import static android.media.CamcorderProfile.get;


/**
 * A simple {@link Fragment} subclass.
 */
public class Doctors extends Fragment {
    ListView listView;
    String id;
    ArrayList<HashMap<String,String>> al=new ArrayList<>();
   String Url="http://amarecor.com/AndroidFiles/getDocreport.php";
   JSONObject json;
//ProgressDialog pd;
    public Doctors() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_doctors, container, false);
        Bundle bundle =getActivity().getIntent().getExtras();
        id = bundle.getString("repid");

        listView = (ListView) v.findViewById(R.id.listview);
        //listView.setAdapter(null);
      //  Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();

        new getSqlDetails().execute(id);
        // Inflate the layout for this fragment
        return v;

    }

    private class getSqlDetails extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
           /* pd=new ProgressDialog(getActivity());
            pd.setTitle("Loading");
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();*/

        }

        @Override
        protected String doInBackground(String... strings) {
            String tempname = strings[0];
            List<NameValuePair> li = new ArrayList<NameValuePair>();
            li.add(new BasicNameValuePair("rep_id", tempname));

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(Url);
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
                    hm.put("doctor_name",json.getString("doctor_name"));
                    hm.put("doctar_type",json.getString("doctar_type"));
                    hm.put("product_promated",json.getString("product_promated"));
                    hm.put("visit_date",json.getString("visit_date"));
                    hm.put("sample",json.getString("sample"));
                    hm.put("sample_name",json.getString("sample_name"));
                    hm.put("gift_given",json.getString("gift_given"));
                    hm.put("gift_given_text",json.getString("gift_given_text"));

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
         //   pd.dismiss();
            String from[]={"place","doctor_name","doctar_type","product_promated","visit_date","sample","sample_name","gift_given","gift_given_text"};
            int to[]={R.id.sr1,R.id.sr2,R.id.sr3,R.id.product,R.id.visittime,R.id.editText3,R.id.sampname,R.id.editText4,R.id.giftnam};
            SimpleAdapter sa=new SimpleAdapter(getActivity(),al,R.layout.docitem,from,to);
            listView.setAdapter(sa);

        }
    }
}

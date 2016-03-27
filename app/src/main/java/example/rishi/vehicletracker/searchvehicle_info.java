package example.rishi.vehicletracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by rishi on 21/4/15.
 */

public class searchvehicle_info extends ActionBarActivity {
    protected static String url1="http://192.168.43.166:8888/vehicle/vehicle_fetch.php";
    String resp,info,s,found_loc;
    TextView found=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            info= extras.getString("getno");
        }
        found=(TextView)findViewById(R.id.found);
        new GetDetails().execute();

    }
    private class GetDetails extends AsyncTask<Void,Void,Integer>
    {
        HttpClient httpclient;
        HttpGet get;
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Starting",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                httpclient = new DefaultHttpClient();
                get = new HttpGet(url1);
                HttpResponse response = httpclient.execute(get);
                System.out.println(response.toString());
                Log.d("===============",response.toString());
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity);
                System.out.println(resp);
                Log.d("===============",resp);
            }
            catch (Exception e)
            {
                Log.e("===============","error in php"+e.toString());
            }
            try
            {
                s="";
                JSONObject json=new JSONObject();
                JSONArray jarray=new JSONArray(resp);
                for(int i=0;i<jarray.length();i++)
                {
                    //System.out.println("i am here 1"+email.getText().toString()+" "+password.getText().toString());

                    json=jarray.getJSONObject(i);
                    if(info.equals(json.getString("number").toString()))
                    {
                        found_loc=json.getString("rto").toString();
                        return 1;

                    }
                    else
                    {
                        continue;
                    }

                }

            }
            catch(Exception e)
            {
                Log.e("log_tag","error parsing data"+e.toString());
            }
            return 0;
        }
        protected void onPostExecute(Integer flag) {

            if(flag==1)
            {
                found.setText("your vehicle found at "+found_loc+" RTO office");
            }
            else if(flag==0)
            {
                found.setText("your vehicle is not found at any of the RTO offices");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

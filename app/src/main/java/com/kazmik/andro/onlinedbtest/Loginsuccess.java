package com.kazmik.andro.onlinedbtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.kazmik.andro.onlinedbtest.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Loginsuccess extends Activity {

    private String jsonResult;
    JSONArray jsonMainNode;
    JSONObject jsonResponse;
    JSONObject jsonChildNode = null,node = null;

    String url="http://kazmikkhan.comli.com/phpfetchdetails.php";
    private ListView listView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);
        final Bundle b = new Bundle();
        listView = (ListView) findViewById(R.id.listView1);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching Data");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Populating List");
        dialog.show();
        accessWebService();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final View selectedView = view ;
                //String name = listView.getItemAtPosition(position).toString();
                TextView txt  = (TextView) view.findViewById(R.id.tvlistviewname);
                final String name = txt.getText().toString();

                SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd");
                final Dialog diag = new Dialog(Loginsuccess.this);
                diag.setCanceledOnTouchOutside(false);
                diag.setCancelable(false);
                diag.setContentView(R.layout.dbdialog);
                diag.setTitle("Detail of Student");
                TextView namea= (TextView)diag.findViewById(R.id.tvdiagname);
                TextView classa= (TextView)diag.findViewById(R.id.tvdiagclass);
                TextView batch = (TextView)diag.findViewById(R.id.tvdiagbatch);
                TextView bg= (TextView)diag.findViewById(R.id.tvdiagbg);
                TextView mob= (TextView)diag.findViewById(R.id.tvdiagmob);
                TextView hos= (TextView)diag.findViewById(R.id.tvdiaghostel);
                TextView lastdon =(TextView)diag.findViewById(R.id.tvdiaglastdon);
                try {

                for (int i = 0; i < jsonMainNode.length(); i++) {

                    jsonChildNode = jsonMainNode.getJSONObject(i);


                    if(name.equals(jsonChildNode.optString("name")))
                    {
                        node = jsonChildNode;
                        namea.setText(name);
                        classa.setText(jsonChildNode.optString("class"));
                        bg.setText(jsonChildNode.optString("bg"));
                        batch.setText(jsonChildNode.optString("batchfrom")+"-"+jsonChildNode.optString("batchto"));
                        mob.setText(jsonChildNode.optString("mob"));
                        hos.setText(jsonChildNode.optString("address"));
                        lastdon.setText(jsonChildNode.optString("lastdon"));
                        b.putString("name",name);
                        b.putString("class",jsonChildNode.optString("class"));
                        b.putString("batch",jsonChildNode.optString("batchfrom"));
                        b.putString("mob",jsonChildNode.optString("mob"));
                        b.putString("address",jsonChildNode.optString("address"));
                        b.putString("lastdon",jsonChildNode.optString("lastdon"));
                        b.putString("bg",jsonChildNode.optString("bg"));
                    }

                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Button bok = (Button)diag.findViewById(R.id.bdiagdok);
                bok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diag.dismiss();
                    }
                });
                Button del = (Button) diag.findViewById(R.id.bdiagddel);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(Loginsuccess.this);
                        alert.setTitle("Confirm Delete");
                        alert
                                .setMessage("Do you want to DELETE these entry?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        diag.dismiss();
                                        String cls = node.optString("class");
                                        String bch = node.optString("bacthfrom");
                                        deleterecord(name, cls, bch);
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alert.create();

                        // show it
                        alertDialog.show();
                    }
                });
                Button bupdate = (Button)diag.findViewById(R.id.bdiagdupdate);
                bupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent k = new Intent(Loginsuccess.this,UpdateEntry.class);
                        k.putExtras(b);
                        startActivity(k);
                    }
                });
                diag.show();

            }
        });
    }

    private void deleterecord(String name, String cls, String bch) {
        deleterecordphp del = new deleterecordphp(Loginsuccess.this,name,cls,bch);
        del.execute();
    }
    private class deleterecordphp extends AsyncTask<String, Void, String> {

        ProgressDialog progress;
           Context c;
        String name,clasd,batchd;
        public deleterecordphp(Loginsuccess loginsuccess, String name, String cls, String bch) {
            this.c = loginsuccess;
            progress= new ProgressDialog(this.c);
            this.name = name;
            clasd = cls;
            batchd = bch;

        }
        protected void onPreExecute(){

            progress.setTitle("Deleting Record");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.setMessage("Updating Database At Server");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String link = "http://kazmikkhan.comli.com/phpupdatedetails.php";
                String data  = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("clas", "UTF-8")
                        + "=" + URLEncoder.encode(clasd, "UTF-8");
                data += "&" + URLEncoder.encode("batch", "UTF-8")
                        + "=" + URLEncoder.encode(batchd, "UTF-8");
                Toast.makeText(Loginsuccess.this,name+"\n"+clasd+"\n"+batchd,Toast.LENGTH_SHORT).show();
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter
                        (conn.getOutputStream());
                wr.write( data );
                wr.flush();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }

                return sb.toString();

            }
            catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("success"))
            {
                Intent k = new Intent(Loginsuccess.this,Loginsuccess.class);
                startActivity(k);
                Loginsuccess.this.finish();
                Toast.makeText(Loginsuccess.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
            else
            {
                Intent k = new Intent(Loginsuccess.this,Loginsuccess.class);
                startActivity(k);
                Toast.makeText(Loginsuccess.this,"Record Not Deleted",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }
    }

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }

    }// end async task

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            jsonResponse = new JSONObject(jsonResult);
             jsonMainNode = jsonResponse.optJSONArray("user_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String outPut;

                 outPut=name;
                employeeList.add(createEmployee("usernames", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.listviewsamp,
                new String[] { "usernames" }, new int[] { R.id.tvlistviewname });
        listView.setAdapter(simpleAdapter);

        dialog.dismiss();
    }

    private HashMap<String, String> createEmployee(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loginsuccess, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.action_newentry){
            Intent k = new Intent(Loginsuccess.this,AddNewEntry.class);
            startActivity(k);
        }
        return super.onOptionsItemSelected(item);
    }



}

package com.kazmik.andro.onlinedbtest;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class AddNewEntry extends ActionBarActivity {

    EditText name,batch,addr,mob,lastdon;
    Button submit;
    String clas,bloodg,namea,mobile,address,last;
    int batcha;
    Spinner classs,bg;
    String[] classes,groups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);
        name = (EditText)findViewById(R.id.etanename);
        submit = (Button)findViewById(R.id.banesubmit);
        classs = (Spinner)findViewById(R.id.saneclass);
        batch = (EditText)findViewById(R.id.etanebatch);
        addr = (EditText)findViewById(R.id.etaneaddr);
        lastdon = (EditText)findViewById(R.id.etanelastdon);
        mob = (EditText)findViewById(R.id.etanemob);
        bg = (Spinner)findViewById(R.id.sanebg);
        classes = new String[]{"CSE", "CE", "EEE", "ECE", "ME", "ICE"};
        groups = new String[]  {"A+" , "A-" ,"B+","B-","O+" , "O-","AB+" , "AB-" };
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, classes);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter_state_bg = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groups);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classs.setAdapter(adapter_state);
        bg.setAdapter(adapter_state_bg);
        bg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodg = bg.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        classs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classs.setSelection(position);
                clas = classs.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namea = name.getText().toString();
                mobile = mob.getText().toString();
                batcha = Integer.parseInt(batch.getText().toString());
                address = addr.getText().toString();
                last = lastdon.getText().toString();
                Toast.makeText(AddNewEntry.this,namea+"\n"+clas+"\n"+batcha+"-"+(batcha+4)+"\n"+bloodg+"\n"+mobile+"\n"+address+"\n"+last,Toast.LENGTH_SHORT).show();
                submitdata();



            }
        });
    }

    public void submitdata(){
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_entry, menu);
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
        return super.onOptionsItemSelected(item);
    }
}

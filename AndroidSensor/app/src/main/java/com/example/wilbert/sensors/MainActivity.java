package com.example.wilbert.sensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager SensorHandler;
    List<Sensor> Sensors;
    List<String> NameSensors;
    List<Integer> TypeSensors;
    ListView LOL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LOL = (ListView) findViewById(R.id.SensorList);
        SensorHandler = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensors = SensorHandler.getSensorList(Sensor.TYPE_ALL);
        NameSensors = new ArrayList<String>();
        TypeSensors = new ArrayList<Integer>();
        for (Sensor sensor : Sensors) {
            NameSensors.add(sensor.getName());
            TypeSensors.add(sensor.getType());
        }
        LOL.setAdapter(new ArrayAdapter<String>(this,R.layout.list_sensor_view,R.id.listText ,NameSensors));
        LOL.setOnItemClickListener(new ListClickHandler());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            // TODO Auto-generated method stub
            TextView listText = (TextView) view.findViewById(R.id.listText);

            //Log.i("HelloListView", "Encontre"+ listText.getText().toString());
            Integer text = TypeSensors.get(position);
            // create intent to start another activity
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            // add the selected text item to our intent.
            intent.putExtra("selected-item", text);
            startActivity(intent);
        }
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

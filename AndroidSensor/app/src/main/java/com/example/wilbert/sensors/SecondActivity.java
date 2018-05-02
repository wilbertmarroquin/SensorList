package com.example.wilbert.sensors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends Activity implements SensorEventListener {
    TextView textView;
    private SensorManager mSensorManager;
    private Integer TypeSensor;
    private float[] gravity = new float[3];
    private ImageView image;
    private float currentDegree = 0f;
    private float currentDegreeY = 0f;
    private float currentDegreeX = 0f;
    private float X = 0f;
    private float Y = 0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = (TextView) findViewById(R.id.textView);
        // get the intent from which this activity is called.
        Intent intent = getIntent();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // fetch value from key-value pair and make it visible on TextView.
        TypeSensor = intent.getIntExtra("selected-item", 1);
        image = (ImageView) findViewById(R.id.image);
        if(TypeSensor == Sensor.TYPE_GYROSCOPE){
            image.setImageResource(R.drawable.carta);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (TypeSensor == Sensor.TYPE_LINEAR_ACCELERATION
                || TypeSensor == Sensor.TYPE_ROTATION_VECTOR || TypeSensor == Sensor.TYPE_GRAVITY || TypeSensor == Sensor.TYPE_GYROSCOPE_UNCALIBRATED
                || TypeSensor == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            getGravity(event);
        }
        else{
            if (TypeSensor == Sensor.TYPE_LIGHT || TypeSensor == Sensor.TYPE_PROXIMITY || TypeSensor == Sensor.TYPE_STEP_DETECTOR)
            {
                if(TypeSensor == Sensor.TYPE_PROXIMITY)
                {
                    getProximity(event);
                }
                if (TypeSensor == Sensor.TYPE_LIGHT) {
                    getAmbient(event);
                }
            }
            else{
                if (TypeSensor == Sensor.TYPE_ACCELEROMETER)
                {
                    image.setImageResource(R.drawable.carta);
                    getAccelerometer(event);
                }
                else
                {
                    if(TypeSensor == Sensor.TYPE_GYROSCOPE)
                    {
                       getGyroscope(event);
                    }
                    else
                    {
                        textView.setText("\n \n"+ mSensorManager.getDefaultSensor(TypeSensor));
                    }
                }
            }
        }
    }
    private  void getGravity(SensorEvent event){
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        textView.setText("X: " + x +"\n" + "Y: " + y +"\n" + "Z: " + z +"\n");
        if(TypeSensor == Sensor.TYPE_GRAVITY)
        {
            System.arraycopy(event.values, 0, gravity,
                    0, gravity.length);
        }
    }
    private  void getAmbient(SensorEvent event){
        float[] values = event.values;
        float x = values[0];
        image.setImageResource(R.drawable.gran_escudo_gardna);
        image.setAlpha(x/100);
    }

    private void getAccelerometer(SensorEvent event) {
        float[] linear_acceleration = new float[3];
        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        X -= linear_acceleration[0]*10;
        Y += linear_acceleration[1]*10;
        image.setX(X);
        image.setY(Y);
        //textView.setText("X: " + linear_acceleration[0] +"\n" + "Y: " + linear_acceleration[1] +"\n" + "Z: " + linear_acceleration[2] +"\n");

    }
    private void getProximity(SensorEvent event){
        float[] values = event.values;
        float x = values[0];
        //textView.setText(mSensorManager.getDefaultSensor(TypeSensor).getName() + x +"\n" );
        if(x == 1.0f)
        {
            image.setImageResource(R.drawable.gran_escudo_gardna);
        }
        else {
            image.setImageResource(R.drawable.carta);
        }
    }
    private void getGyroscope(SensorEvent event) {
        float degree = -Math.round(event.values[2]*10);
        float degreeY = -Math.round(event.values[1]*10);
        float degreeX = -Math.round(event.values[0]*10);
        image.setRotation(currentDegree);
        image.setRotationY(degreeY);
        image.setRotationX(degreeX);
        currentDegree -= degree;
        currentDegreeY -= degreeY;
        currentDegreeX -= degreeX;
        /*if(event.values[2] > 0.5f) { // anticlockwise
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        } else if(event.values[2] < -0.5f) { // clockwise
            getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
        }*/
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(TypeSensor),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}

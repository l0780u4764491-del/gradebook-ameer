package com.ameer.gradebook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.studentsButton).setOnClickListener(v ->
            Toast.makeText(this, "إدارة الطلاب - سيتم تطويرها", Toast.LENGTH_SHORT).show());

        findViewById(R.id.classesButton).setOnClickListener(v ->
            Toast.makeText(this, "الشعب الدراسية - سيتم تطويرها", Toast.LENGTH_SHORT).show());

        findViewById(R.id.gradesButton).setOnClickListener(v ->
            Toast.makeText(this, "إدخال الدرجات - سيتم تطويره", Toast.LENGTH_SHORT).show());

        findViewById(R.id.printButton).setOnClickListener(v ->
            Toast.makeText(this, "الطباعة - سيتم تطويرها", Toast.LENGTH_SHORT).show());
    }
}

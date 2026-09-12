package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    private final ArrayList<String> classes = new ArrayList<>();

    private static final String PREFS = "gradebook_data";
    private static final String KEY_CLASSES = "classes";
    private static final String KEY_STUDENTS = "students";

    private int dark = Color.rgb(25, 35, 50);
    private int gray = Color.rgb(100, 110, 120);
    private int light = Color.rgb(245, 247, 250);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        loadClasses();

        showHome();
    }

    // ============================================================
    // الصفحة الرئيسية
    // ============================================================

    private void showHome() {

        LinearLayout root = createRoot();

        TextView title = text(
                "سجل درجات الاجتماعيات",
                30,
                dark,
                true
        );

        TextView teacher = text(
                "الأستاذ أمير محمد",
                23,
                Color.DKGRAY,
                false
        );

        TextView subtitle = text(
                "نظام إدارة درجات الطلاب",
                18,
                gray,
                false
        );

        root.addView(title);
        root.addView(teacher);
        root.addView(subtitle);

        addSpace(root, 55);

        Button students = button("إدارة الطلاب");
        Button classesButton = button("الشعب الدراسية");
        Button grades = button("إدخال الدرجات");
        Button records = button("طباعة السجلات");

        root.addView(students);
        addSpace(root, 18);

        root.addView(classesButton);
        addSpace(root, 18);

        root.addView(grades);
        addSpace(root, 18);

        root.addView(records);

        students.setOnClickListener(v -> showStudents());
        classesButton.setOnClickListener(v -> showClasses());
        grades.setOnClickListener(v -> showGrades());
        records.setOnClickListener(v -> showRecords());

        setContentView(root);
    }

    // ============================================================
    // إدارة الطلاب
    // ============================================================

    private void showStudents() {

        LinearLayout root = createRoot();

        addBackButton(root);

        TextView title = text(
                "إدارة الطلاب",
                28,
                dark,
                true
        );

        root.addView(title);
        addSpace(root, 15);

        Button add = button("＋ إضافة طالب");
        root.addView(add);

        addSpace(root, 15);

        ListView list = new ListView(this);
        root.addView(
                list,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        refreshStudentsList(list);

        add.setOnClickListener(v -> showAddStudentDialog(list));

        setContentView(root);
    }

    private void refreshStudentsList(ListView list) {

        ArrayList<String> names = new ArrayList<>();

        JSONArray students = getStudents();

        for (int i = 0; i < students.length(); i++) {

            try {

                JSONObject student = students.getJSONObject(i);

                String name = student.optString("name", "");
                String className = student.optString("class", "");

                names.add(
                        name + "\nالشعبة: " +
                                (className.isEmpty() ? "غير محددة" : className)
                );

            } catch (JSONException ignored) {
            }
        }

        if (names.isEmpty()) {
            names.add("لا يوجد طلاب حتى الآن");
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        names
                );

        list.setAdapter(adapter);

        list.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    if (getStudents().length() == 0) {
                        return true;
                    }

                    confirmDeleteStudent(position, list);

                    return true;
                }
        );
    }

    private void showAddStudentDialog(ListView list) {

        LinearLayout box = dialogLayout();

        EditText name = editText("اسم الطالب");

        Spinner spinner = new Spinner(this);

        ArrayList<String> classOptions = new ArrayList<>();

        classOptions.add("بدون شعبة");

        classOptions.addAll(classes);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        classOptions
                );

        spinner.setAdapter(adapter);

        box.addView(name);
        addSpace(box, 12);
        box.addView(spinner);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("إضافة طالب")
                        .setView(box)
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .setNegativeButton("إلغاء", null)
                        .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String studentName =
                        name.getText().toString().trim();

                if (studentName.isEmpty()) {

                    name.setError("أدخل اسم الطالب");
                    return;
                }

                String className =
                        spinner.getSelectedItem().toString();

                if (className.equals("بدون شعبة")) {
                    className = "";
                }

                addStudent(studentName, className);

                Toast.makeText(
                        this,
                        "تمت إضافة الطالب بنجاح",
                        Toast.LENGTH_SHORT
                ).show();

                dialog.dismiss();

                refreshStudentsList(list);
            });
        });

        dialog.show();
    }

    private void confirmDeleteStudent(
            int position,
            ListView list
    ) {

        JSONArray students = getStudents();

        if (position >= students.length()) {
            return;
        }

        try {

            String name =
                    students.getJSONObject(position)
                            .optString("name");

            new AlertDialog.Builder(this)
                    .setTitle("حذف الطالب")
                    .setMessage(
                            "هل تريد حذف الطالب:\n" +
                                    name + " ؟"
                    )
                    .setPositiveButton(
                            "حذف",
                            (dialog, which) -> {

                                deleteStudent(position);

                                Toast.makeText(
                                        this,
                                        "تم حذف الطالب",
                                        Toast.LENGTH_SHORT
                                ).show();

                                refreshStudentsList(list);
                            }
                    )
                    .setNegativeButton(
                            "إلغاء",
                            null
                    )
                    .show();

        } catch (JSONException ignored) {
        }
    }

    // ============================================================
    // الشعب الدراسية
    // ============================================================

    private void showClasses() {

        LinearLayout root = createRoot();

        addBackButton(root);

        TextView title = text(
                "الشعب الدراسية",
                28,
                dark,
                true
        );

        root.addView(title);
        addSpace(root, 15);

        Button add = button("＋ إضافة شعبة");

        root.addView(add);
        addSpace(root, 15);

        ListView list = new ListView(this);

        root.addView(
                list,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        refreshClassesList(list);

        add.setOnClickListener(
                v -> showAddClassDialog(list)
        );

        setContentView(root);
    }

    private void refreshClassesList(ListView list) {

        ArrayList<String> data =
                new ArrayList<>(classes);

        if (data.isEmpty()) {
            data.add("لا توجد شعب حتى الآن");
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        data
                );

        list.setAdapter(adapter);

        list.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    if (classes.isEmpty()) {
                        return true;
                    }

                    String className =
                            classes.get(position);

                    new AlertDialog.Builder(this)
                            .setTitle("حذف الشعبة")
                            .setMessage(
                                    "هل تريد حذف شعبة:\n" +
                                            className + " ؟"
                            )
                            .setPositiveButton(
                                    "حذف",
                                    (d, w) -> {

                                        classes.remove(position);

                                        saveClasses();

                                        refreshClassesList(list);

                                        Toast.makeText(
                                                this,
                                                "تم حذف الشعبة",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                            )
                            .setNegativeButton(
                                    "إلغاء",
                                    null
                            )
                            .show();

                    return true;
                }
        );
    }

    private void showAddClassDialog(ListView list) {

        EditText input =
                editText("اسم الشعبة");

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("إضافة شعبة")
                        .setView(input)
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String value =
                        input.getText()
                                .toString()
                                .trim();

                if (value.isEmpty()) {

                    input.setError(
                            "أدخل اسم الشعبة"
                    );

                    return;
                }

                if (classes.contains(value)) {

                    input.setError(
                            "هذه الشعبة موجودة مسبقًا"
                    );

                    return;
                }

                classes.add(value);

                saveClasses();

                Toast.makeText(
                        this,
                        "تمت إضافة الشعبة",
                        Toast.LENGTH_SHORT
                ).show();

                dialog.dismiss();

                refreshClassesList(list);
            });
        });

        dialog.show();
    }

    // ============================================================
    // إدخال الدرجات
    // ============================================================

    private void showGrades() {

        LinearLayout root = createRoot();

        addBackButton(root);

        TextView title = text(
                "إدخال الدرجات",
                28,
                dark,
                true
        );

        root.addView(title);
        addSpace(root, 20);

        JSONArray students = getStudents();

        if (students.length() == 0) {

            TextView empty = text(
                    "لا يوجد طلاب.\nأضف الطلاب أولاً من إدارة الطلاب.",
                    20,
                    gray,
                    false
            );

            root.addView(empty);

            setContentView(root);
            return;
        }

        ArrayList<String> names =
                new ArrayList<>();

        for (int i = 0; i < students.length(); i++) {

            try {
                names.add(
                        students.getJSONObject(i)
                                .optString("name")
                );
            } catch (JSONException ignored) {
            }
        }

        Spinner spinner = new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        names
                );

        spinner.setAdapter(adapter);

        root.addView(spinner);

        addSpace(root, 20);

        EditText monthly =
                gradeEditText(
                        "درجة الشهر الأول"
                );

        EditText mid =
                gradeEditText(
                        "درجة نصف السنة"
                );

        EditText second =
                gradeEditText(
                        "درجة الشهر الثاني"
                );

        EditText finalExam =
                gradeEditText(
                        "درجة الامتحان النهائي"
                );

        root.addView(monthly);
        addSpace(root, 10);

        root.addView(mid);
        addSpace(root, 10);

        root.addView(second);
        addSpace(root, 10);

        root.addView(finalExam);

        addSpace(root, 20);

        Button save =
                button("💾 حفظ الدرجات");

        root.addView(save);

        Button load =
                button("استرجاع درجات الطالب");

        root.addView(load);

        spinner.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        loadGrades(
                                position,
                                monthly,
                                mid,
                                second,
                                finalExam
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                    }
                }
        );

        save.setOnClickListener(v -> {

            int position =
                    spinner.getSelectedItemPosition();

            saveGrades(
                    position,
                    monthly,
                    mid,
                    second,
                    finalExam
            );
        });

        load.setOnClickListener(v -> {

            int position =
                    spinner.getSelectedItemPosition();

            loadGrades(
                    position,
                    monthly,
                    mid,
                    second,
                    finalExam
            );

            Toast.makeText(
                    this,
                    "تم استرجاع الدرجات",
                    Toast.LENGTH_SHORT
            ).show();
        });

        setContentView(root);
    }

    private EditText gradeEditText(String hint) {

        EditText e = editText(hint);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        return e;
    }

    private void saveGrades(
            int position,
            EditText monthly,
            EditText mid,
            EditText second,
            EditText finalExam
    ) {

        JSONArray students = getStudents();

        if (position < 0 ||
                position >= students.length()) {
            return;
        }

        try {

            JSONObject student =
                    students.getJSONObject(position);

            student.put(
                    "monthly",
                    parseGrade(monthly)
            );

            student.put(
                    "mid",
                    parseGrade(mid)
            );

            student.put(
                    "second",
                    parseGrade(second)
            );

            student.put(
                    "final",
                    parseGrade(finalExam)
            );

            saveStudents(students);

            Toast.makeText(
                    this,
                    "تم حفظ الدرجات بنجاح",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (JSONException e) {

            Toast.makeText(
                    this,
                    "حدث خطأ أثناء الحفظ",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private double parseGrade(EditText edit) {

        String value =
                edit.getText()
                        .toString()
                        .trim();

        if (value.isEmpty()) {
            return 0;
        }

        try {

            double number =
                    Double.parseDouble(value);

            if (number < 0) {
                return 0;
            }

            if (number > 100) {
                return 100;
            }

            return number;

        } catch (Exception e) {
            return 0;
        }
    }

    private void loadGrades(
            int position,
            EditText monthly,
            EditText mid,
            EditText second,
            EditText finalExam
    ) {

        JSONArray students

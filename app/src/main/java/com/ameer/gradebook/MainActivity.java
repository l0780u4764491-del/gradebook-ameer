package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> classes = new ArrayList<>();

    private JSONArray studentData = new JSONArray();
    private JSONArray classData = new JSONArray();

    private LinearLayout mainLayout;

    private final int BLUE = Color.rgb(31, 95, 150);
    private final int WHITE = Color.WHITE;
    private final int DARK = Color.rgb(35, 35, 35);
    private final int LIGHT = Color.rgb(248, 249, 250);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("gradebook_data", MODE_PRIVATE);

        loadData();

        showHome();
    }

    private void loadData() {
        try {
            String studentsJson = prefs.getString("students", "[]");
            String classesJson = prefs.getString("classes", "[]");

            studentData = new JSONArray(studentsJson);
            classData = new JSONArray(classesJson);

            students.clear();
            classes.clear();

            for (int i = 0; i < studentData.length(); i++) {
                JSONObject obj = studentData.getJSONObject(i);
                students.add(obj.optString("name", ""));
            }

            for (int i = 0; i < classData.length(); i++) {
                JSONObject obj = classData.getJSONObject(i);
                classes.add(obj.optString("name", ""));
            }

        } catch (Exception e) {
            studentData = new JSONArray();
            classData = new JSONArray();
        }
    }

    private void saveData() {
        prefs.edit()
                .putString("students", studentData.toString())
                .putString("classes", classData.toString())
                .apply();
    }

    private void setupLayout(String title) {

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(LIGHT);
        mainLayout.setPadding(18, 18, 18, 18);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        scroll.addView(mainLayout);

        setContentView(scroll);

        TextView header = new TextView(this);
        header.setText(title);
        header.setTextColor(WHITE);
        header.setTextSize(27);
        header.setGravity(Gravity.CENTER);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setBackgroundColor(BLUE);
        header.setPadding(10, 22, 10, 22);

        mainLayout.addView(
                header,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
    }

    private Button makeButton(String text) {

        Button button = new Button(this);

        button.setText(text);
        button.setTextSize(20);
        button.setTextColor(WHITE);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
        button.setBackgroundColor(BLUE);
        button.setPadding(10, 18, 10, 18);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 6, 0, 6);

        button.setLayoutParams(params);

        return button;
    }

    private TextView makeText(String text, float size) {

        TextView tv = new TextView(this);

        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(DARK);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 12, 10, 12);

        return tv;
    }

    private EditText makeEditText(String hint) {

        EditText edit = new EditText(this);

        edit.setHint(hint);
        edit.setTextSize(18);
        edit.setTextColor(DARK);
        edit.setHintTextColor(Color.GRAY);
        edit.setPadding(20, 12, 20, 12);

        return edit;
    }

    private void addSpace(int height) {

        View space = new View(this);

        mainLayout.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        height
                )
        );
    }

    private void showHome() {

        setupLayout("سجل درجات الاجتماعيات");

        TextView teacher = makeText(
                "الأستاذ أمير محمد\nنظام إدارة درجات الطلاب",
                21
        );

        teacher.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        mainLayout.addView(teacher);

        addSpace(8);

        Button studentsButton = makeButton("👨‍🎓 إدارة الطلاب");

        studentsButton.setOnClickListener(v -> showStudents());

        mainLayout.addView(studentsButton);

        Button classesButton = makeButton("🏫 الشعب الدراسية");

        classesButton.setOnClickListener(v -> showClasses());

        mainLayout.addView(classesButton);

        Button gradesButton = makeButton("📝 إدخال الدرجات");

        gradesButton.setOnClickListener(v -> showGrades());

        mainLayout.addView(gradesButton);

        Button recordsButton = makeButton("📋 السجلات والتقارير");

        recordsButton.setOnClickListener(v -> showRecords());

        mainLayout.addView(recordsButton);

        Button printButton = makeButton("📤 مشاركة السجلات");

        printButton.setOnClickListener(v -> shareRecords());

        mainLayout.addView(printButton);

        addSpace(15);

        TextView info = makeText(
                "عدد الطلاب: " + students.size() +
                "\nعدد الشعب: " + classes.size(),
                18
        );

        mainLayout.addView(info);
    }

    private void showStudents() {

        setupLayout("إدارة الطلاب");

        Button add = makeButton("➕ إضافة طالب");

        add.setOnClickListener(v -> addStudentDialog());

        mainLayout.addView(add);

        EditText search = makeEditText("🔎 البحث عن طالب");

        mainLayout.addView(search);

        LinearLayout list = new LinearLayout(this);

        list.setOrientation(LinearLayout.VERTICAL);

        mainLayout.addView(list);

        renderStudents(list, "");

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                renderStudents(list, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addBackButton();
    }

    private void renderStudents(
            LinearLayout list,
            String query) {

        list.removeAllViews();

        String q = query.trim().toLowerCase(Locale.getDefault());

        int found = 0;

        for (int i = 0; i < studentData.length(); i++) {

            try {

                JSONObject student =
                        studentData.getJSONObject(i);

                String name =
                        student.optString("name", "");

                String className =
                        student.optString("class", "");

                if (!name.toLowerCase(
                        Locale.getDefault()).contains(q)) {
                    continue;
                }

                found++;

                final int index = i;

                Button item = makeButton(
                        "👤 " + name +
                        "\n🏫 " + className
                );

                item.setTextSize(18);

                item.setOnClickListener(
                        v -> showStudentOptions(index)
                );

                list.addView(item);

            } catch (Exception e) {
                // تجاهل الطالب التالف
            }
        }

        if (found == 0) {

            TextView empty = makeText(
                    "لا يوجد طلاب مطابقون.\nاضغط «إضافة طالب» لإضافة طالب.",
                    20
            );

            list.addView(empty);
        }
    }

    private void addStudentDialog() {

        LinearLayout box = new LinearLayout(this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(30, 10, 30, 5);

        EditText name =
                makeEditText("اسم الطالب");

        EditText className =
                makeEditText("الشعبة / الصف");

        box.addView(name);
        box.addView(className);

        new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(box)
                .setNegativeButton("إلغاء", null)
                .setPositiveButton("حفظ", null)
                .create();

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("إضافة طالب")
                        .setView(box)
                        .setNegativeButton("إلغاء", null)
                        .setPositiveButton("حفظ", null)
                        .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String studentName =
                        name.getText().toString().trim();

                String studentClass =
                        className.getText().toString().trim();

                if (studentName.isEmpty()) {

                    name.setError("اكتب اسم الطالب");

                    return;
                }

                if (studentClass.isEmpty()) {

                    className.setError("اكتب الشعبة");

                    return;
                }

                try {

                    JSONObject obj = new JSONObject();

                    obj.put("name", studentName);
                    obj.put("class", studentClass);
                    obj.put("monthly", 0);
                    obj.put("mid", 0);
                    obj.put("second", 0);
                    obj.put("final", 0);

                    studentData.put(obj);

                    students.add(studentName);

                    saveData();

                    dialog.dismiss();

                    Toast.makeText(
                            this,
                            "تمت إضافة الطالب بنجاح",
                            Toast.LENGTH_SHORT
                    ).show();

                    showStudents();

                } catch (Exception e) {

                    Toast.makeText(
                            this,
                            "حدث خطأ أثناء إضافة الطالب",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });

        dialog.show();
    }

    private void showStudentOptions(int index) {

        String[] options = {
                "✏️ تعديل بيانات الطالب",
                "🗑️ حذف الطالب",
                "📝 إدخال درجات الطالب"
        };

        new AlertDialog.Builder(this)
                .setTitle("خيارات الطالب")
                .setItems(options, (dialog, which) -> {

                    if (which == 0) {
                        editStudentDialog(index);
                    }

                    if (which == 1) {
                        deleteStudent(index);
                    }

                    if (which == 2) {
                        showStudentGrades(index);
                    }
                })
                .show();
    }

    private void editStudentDialog(int index) {

        try {

            JSONObject student =
                    studentData.getJSONObject(index);

            LinearLayout box = new LinearLayout(this);

            box.setOrientation(LinearLayout.VERTICAL);
            box.setPadding(30, 10, 30, 5);

            EditText name =
                    makeEditText("اسم الطالب");

            EditText className =
                    makeEditText("الشعبة");

            name.setText(
                    student.optString("name", "")
            );

            className.setText(
                    student.optString("class", "")
            );

            box.addView(name);
            box.addView(className);

            AlertDialog dialog =
                    new AlertDialog.Builder(this)
                            .setTitle("تعديل الطالب")
                            .setView(box)
                            .setNegativeButton("إلغاء", null)
                            .setPositiveButton("حفظ", null)
                            .create();

            dialog.setOnShowListener(d -> {

                dialog.getButton(
                        AlertDialog.BUTTON_POSITIVE
                ).setOnClickListener(v -> {

                    String newName =
                            name.getText().toString().trim();

                    String newClass =
                            className.getText().toString().trim();

                    if (newName.isEmpty()) {

                        name.setError("اكتب اسم الطالب");

                        return;
                    }

                    if (newClass.isEmpty()) {

                        className.setError("اكتب الشعبة");

                        return;
                    }

                    try {

                        student.put("name", newName);
                        student.put("class", newClass);

                        students.set(index, newName);

                        saveData();

                        dialog.dismiss();

                        showStudents();

                    } catch (Exception e) {

                        Toast.makeText(
                                this,
                                "تعذر تعديل الطالب",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            });

            dialog.show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر فتح بيانات الطالب",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void deleteStudent(int index) {

        try {

            JSONObject student =
                    studentData.getJSONObject(index);

            String name =
                    student.optString("name", "");

            new AlertDialog.Builder(this)
                    .setTitle("حذف الطالب")
                    .setMessage(
                            "هل أنت متأكد من حذف:\n" + name + " ؟"
                    )
                    .setNegativeButton("إلغاء", null)
                    .setPositiveButton(
                            "حذف",
                            (dialog, which) -> {

                                try {

                                    studentData.remove(index);

                                    if (index < students.size()) {
                                        students.remove(index);
                                    }

                                    saveData();

                                    Toast.makeText(
                                            this,
                                            "تم حذف الطالب",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    showStudents();

                                } catch (Exception e) {

                                    Toast.makeText(
                                            this,
                                            "تعذر حذف الطالب",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    )
                    .show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر حذف الطالب",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }    private void showClasses() {

        setupLayout("الشعب الدراسية");

        Button add = makeButton("➕ إضافة شعبة");

        add.setOnClickListener(v -> addClassDialog());

        mainLayout.addView(add);

        if (classData.length() == 0) {

            mainLayout.addView(
                    makeText(
                            "لا توجد شعب حالياً.\nاضغط «إضافة شعبة».",
                            20
                    )
            );
        }

        for (int i = 0; i < classData.length(); i++) {

            try {

                JSONObject obj =
                        classData.getJSONObject(i);

                String name =
                        obj.optString("name", "");

                final int index = i;

                Button item =
                        makeButton("🏫 " + name);

                item.setOnClickListener(
                        v -> showClassOptions(index)
                );

                mainLayout.addView(item);

            } catch (Exception e) {
                // تجاهل
            }
        }

        addBackButton();
    }

    private void addClassDialog() {

        EditText input =
                makeEditText("اسم الشعبة");

        new AlertDialog.Builder(this)
                .setTitle("إضافة شعبة")
                .setView(input)
                .setNegativeButton("إلغاء", null)
                .setPositiveButton(
                        "إضافة",
                        (dialog, which) -> {

                            String name =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (name.isEmpty()) {

                                Toast.makeText(
                                        this,
                                        "اكتب اسم الشعبة",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            try {

                                JSONObject obj =
                                        new JSONObject();

                                obj.put("name", name);

                                classData.put(obj);
                                classes.add(name);

                                saveData();

                                Toast.makeText(
                                        this,
                                        "تمت إضافة الشعبة",
                                        Toast.LENGTH_SHORT
                                ).show();

                                showClasses();

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "تعذر إضافة الشعبة",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }

    private void showClassOptions(int index) {

        String[] options = {
                "🗑️ حذف الشعبة"
        };

        new AlertDialog.Builder(this)
                .setTitle("خيارات الشعبة")
                .setItems(
                        options,
                        (dialog, which) -> {

                            if (which == 0) {
                                deleteClass(index);
                            }
                        }
                )
                .show();
    }

    private void deleteClass(int index) {

        try {

            JSONObject obj =
                    classData.getJSONObject(index);

            String name =
                    obj.optString("name", "");

            new AlertDialog.Builder(this)
                    .setTitle("حذف الشعبة")
                    .setMessage(
                            "حذف الشعبة «" +
                            name +
                            "»؟"
                    )
                    .setNegativeButton(
                            "إلغاء",
                            null
                    )
                    .setPositiveButton(
                            "حذف",
                            (dialog, which) -> {

                                classData.remove(index);

                                if (index < classes.size()) {
                                    classes.remove(index);
                                }

                                saveData();

                                showClasses();
                            }
                    )
                    .show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر حذف الشعبة",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showGrades() {

        setupLayout("إدخال الدرجات");

        if (studentData.length() == 0) {

            mainLayout.addView(
                    makeText(
                            "لا يوجد طلاب.\nأضف الطلاب أولاً من «إدارة الطلاب».",
                            20
                    )
            );

            addBackButton();

            return;
        }

        TextView info =
                makeText(
                        "اختر الطالب لإدخال أو تعديل درجاته",
                        19
                );

        mainLayout.addView(info);

        EditText search =
                makeEditText("🔎 البحث عن طالب");

        mainLayout.addView(search);

        LinearLayout list =
                new LinearLayout(this);

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        mainLayout.addView(list);

        renderGradeStudents(list, "");

        search.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        renderGradeStudents(
                                list,
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                }
        );

        addBackButton();
    }

    private void renderGradeStudents(
            LinearLayout list,
            String query) {

        list.removeAllViews();

        String q =
                query.trim()
                        .toLowerCase(
                                Locale.getDefault()
                        );

        int found = 0;

        for (int i = 0;
             i < studentData.length();
             i++) {

            try {

                JSONObject student =
                        studentData.getJSONObject(i);

                String name =
                        student.optString(
                                "name",
                                ""
                        );

                if (!name.toLowerCase(
                        Locale.getDefault()
                ).contains(q)) {
                    continue;
                }

                final int index = i;

                Button item =
                        makeButton(
                                "📝 " + name
                        );

                item.setOnClickListener(
                        v -> showStudentGrades(index)
                );

                list.addView(item);

                found++;

            } catch (Exception e) {
                // تجاهل
            }
        }

        if (found == 0) {

            list.addView(
                    makeText(
                            "لا يوجد طالب مطابق.",
                            19
                    )
            );
        }
    }

    private void showStudentGrades(int index) {

        try {

            JSONObject student =
                    studentData.getJSONObject(index);

            String name =
                    student.optString(
                            "name",
                            ""
                    );

            String className =
                    student.optString(
                            "class",
                            ""
                    );

            setupLayout("درجات الطالب");

            TextView title =
                    makeText(
                            "👤 " + name +
                            "\n🏫 الشعبة: " +
                            className,
                            21
                    );

            title.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            mainLayout.addView(title);

            EditText monthly =
                    makeEditText(
                            "درجة الشهرية"
                    );

            EditText mid =
                    makeEditText(
                            "درجة نصف السنة"
                    );

            EditText second =
                    makeEditText(
                            "درجة الشهر الثاني"
                    );

            EditText finalGrade =
                    makeEditText(
                            "درجة الامتحان النهائي"
                    );

            monthly.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            mid.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            second.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            finalGrade.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            monthly.setText(
                    String.valueOf(
                            student.optDouble(
                                    "monthly",
                                    0
                            )
                    )
            );

            mid.setText(
                    String.valueOf(
                            student.optDouble(
                                    "mid",
                                    0
                            )
                    )
            );

            second.setText(
                    String.valueOf(
                            student.optDouble(
                                    "second",
                                    0
                            )
                    )
            );

            finalGrade.setText(
                    String.valueOf(
                            student.optDouble(
                                    "final",
                                    0
                            )
                    )
            );

            mainLayout.addView(
                    makeText(
                            "الدرجات من 0 إلى 100",
                            17
                    )
            );

            mainLayout.addView(monthly);
            mainLayout.addView(mid);
            mainLayout.addView(second);
            mainLayout.addView(finalGrade);

            Button save =
                    makeButton(
                            "💾 حفظ الدرجات"
                    );

            save.setOnClickListener(
                    v -> saveStudentGrades(
                            index,
                            monthly,
                            mid,
                            second,
                            finalGrade
                    )
            );

            mainLayout.addView(save);

            Button clear =
                    makeButton(
                            "🧹 تصفير الدرجات"
                    );

            clear.setOnClickListener(
                    v -> {

                        monthly.setText("0");
                        mid.setText("0");
                        second.setText("0");
                        finalGrade.setText("0");
                    }
            );

            mainLayout.addView(clear);

            Button back =
                    makeButton(
                            "← العودة"
                    );

            back.setOnClickListener(
                    v -> showGrades()
            );

            mainLayout.addView(back);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر فتح درجات الطالب",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void saveStudentGrades(
            int index,
            EditText monthly,
            EditText mid,
            EditText second,
            EditText finalGrade) {

        try {

            JSONObject student =
                    studentData.getJSONObject(index);

            double m =
                    getGrade(monthly);

            double mi =
                    getGrade(mid);

            double s =
                    getGrade(second);

            double f =
                    getGrade(finalGrade);

            student.put("monthly", m);
            student.put("mid", mi);
            student.put("second", s);
            student.put("final", f);

            saveData();

            Toast.makeText(
                    this,
                    "تم حفظ الدرجات بنجاح",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر حفظ الدرجات",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private double getGrade(EditText edit) {

        try {

            String text =
                    edit.getText()
                            .toString()
                            .trim();

            if (text.isEmpty()) {
                return 0;
            }

            double value =
                    Double.parseDouble(text);

            if (value < 0) {
                value = 0;
            }

            if (value > 100) {
                value = 100;
            }

            return value;

        } catch (Exception e) {

            return 0;
        }
    }

    private void showRecords() {

        setupLayout("السجلات والتقارير");

        if (studentData.length() == 0) {

            mainLayout.addView(
                    makeText(
                            "لا يوجد طلاب لعرض السجلات.",
                            20
                    )
            );

            addBackButton();

            return;
        }

        mainLayout.addView(
                makeText(
                        "سجل درجات جميع الطلاب",
                        21
                )
        );

        for (int i = 0;
             i < studentData.length();
             i++) {

            try {

                JSONObject student =
                        studentData.getJSONObject(i);

                String name =
                        student.optString(
                                "name",
                                ""
                        );

                String className =
                        student.optString(
                                "class",
                                ""
                        );

                double monthly =
                        student.optDouble(
                                "monthly",
                                0
                        );

                double mid =
                        student.optDouble(
                                "mid",
                                0
                        );

                double second =
                        student.optDouble(
                                "second",
                                0
                        );

                double finalGrade =
                        student.optDouble(
                                "final",
                                0
                        );

                double total =
                        monthly +
                        mid +
                        second +
                        finalGrade;

                double average =
                        total / 4.0;

                String result =
                        average >= 50
                        ? "ناجح"
                        : "راسب";

                String record =
                        "👤 " + name +
                        "\n🏫 الشعبة: " + className +
                        "\nالشهرية: " +
                        formatNumber(monthly) +
                        "\nنصف السنة: " +
                        formatNumber(mid) +
                        "\nالشهر الثاني: " +
                        formatNumber(second) +
                        "\nالنهائي: " +
                        formatNumber(finalGrade) +
                        "\nالمجموع: " +
                        formatNumber(total) +
                        "\nالمعدل: " +
                        formatNumber(average) +
                        "\nالنتيجة: " + result;

                TextView item =
                        makeText(record, 17);

                item.setGravity(
                        Gravity.RIGHT
                );

                item.setPadding(
                        15,
                        18,
                        15,
                        18
                );

                mainLayout.addView(item);

                View line =
                        new View(this);

                line.setBackgroundColor(
                        Color.LTGRAY
                );

                mainLayout.addView(
                        line,
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                2
                        )
                );

            } catch (Exception e) {
                // تجاهل
            }
        }

        Button share =
                makeButton(
                        "📤 مشاركة السجل"
                );

        share.setOnClickListener(
                v -> shareRecords()
        );

        mainLayout.addView(share);

        addBackButton();
    }    private String formatNumber(double number) {

        if (number == Math.floor(number)) {

            return String.valueOf(
                    (int) number
            );
        }

        return String.format(
                Locale.US,
                "%.2f",
                number
        );
    }

    private String buildRecordsText() {

        StringBuilder text =
                new StringBuilder();

        text.append(
                "سجل درجات الاجتماعيات\n"
        );

        text.append(
                "الأستاذ أمير محمد\n"
        );

        text.append(
                "====================\n\n"
        );

        for (int i = 0;
             i < studentData.length();
             i++) {

            try {

                JSONObject student =
                        studentData.getJSONObject(i);

                String name =
                        student.optString(
                                "name",
                                ""
                        );

                String className =
                        student.optString(
                                "class",
                                ""
                        );

                double monthly =
                        student.optDouble(
                                "monthly",
                                0
                        );

                double mid =
                        student.optDouble(
                                "mid",
                                0
                        );

                double second =
                        student.optDouble(
                                "second",
                                0
                        );

                double finalGrade =
                        student.optDouble(
                                "final",
                                0
                        );

                double total =
                        monthly +
                        mid +
                        second +
                        finalGrade;

                double average =
                        total / 4.0;

                String result =
                        average >= 50
                        ? "ناجح"
                        : "راسب";

                text.append(
                        "الطالب: "
                );

                text.append(name);

                text.append("\n");

                text.append(
                        "الشعبة: "
                );

                text.append(className);

                text.append("\n");

                text.append(
                        "الشهرية: "
                );

                text.append(
                        formatNumber(monthly)
                );

                text.append("\n");

                text.append(
                        "نصف السنة: "
                );

                text.append(
                        formatNumber(mid)
                );

                text.append("\n");

                text.append(
                        "الشهر الثاني: "
                );

                text.append(
                        formatNumber(second)
                );

                text.append("\n");

                text.append(
                        "النهائي: "
                );

                text.append(
                        formatNumber(finalGrade)
                );

                text.append("\n");

                text.append(
                        "المجموع: "
                );

                text.append(
                        formatNumber(total)
                );

                text.append("\n");

                text.append(
                        "المعدل: "
                );

                text.append(
                        formatNumber(average)
                );

                text.append("\n");

                text.append(
                        "النتيجة: "
                );

                text.append(result);

                text.append(
                        "\n"
                );

                text.append(
                        "--------------------\n"
                );

            } catch (Exception e) {
                // تجاهل
            }
        }

        return text.toString();
    }

    private void shareRecords() {

        if (studentData.length() == 0) {

            Toast.makeText(
                    this,
                    "لا توجد سجلات لمشاركتها",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String records =
                buildRecordsText();

        Intent intent =
                new Intent(
                        Intent.ACTION_SEND
                );

        intent.setType("text/plain");

        intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "سجل درجات الاجتماعيات"
        );

        intent.putExtra(
                Intent.EXTRA_TEXT,
                records
        );

        startActivity(
                Intent.createChooser(
                        intent,
                        "مشاركة السجل"
                )
        );
    }

    private void addBackButton() {

        addSpace(12);

        Button back =
                makeButton(
                        "← العودة للرئيسية"
                );

        back.setOnClickListener(
                v -> showHome()
        );

        mainLayout.addView(back);
    }

    @Override
    public void onBackPressed() {

        showHome();
    }
}

package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    private final ArrayList<String> students = new ArrayList<>();
    private final ArrayList<String> classes = new ArrayList<>();

    private final ArrayList<StudentGrade> grades = new ArrayList<>();

    private LinearLayout root;

    private int darkText = Color.rgb(35, 35, 35);
    private int primary = Color.rgb(25, 90, 150);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("gradebook_data", MODE_PRIVATE);

        loadData();

        showHome();
    }

    // ============================================================
    // البيانات
    // ============================================================

    private static class StudentGrade {
        String student;
        String className;
        double monthly;
        double mid;
        double second;
        double finalExam;

        StudentGrade(String student, String className,
                     double monthly, double mid,
                     double second, double finalExam) {
            this.student = student;
            this.className = className;
            this.monthly = monthly;
            this.mid = mid;
            this.second = second;
            this.finalExam = finalExam;
        }

        double total() {
            return monthly + mid + second + finalExam;
        }

        double average() {
            return total() / 4.0;
        }
    }

    private void saveData() {
        try {
            JSONArray studentArray = new JSONArray();

            for (String student : students) {
                studentArray.put(student);
            }

            JSONArray classArray = new JSONArray();

            for (String className : classes) {
                classArray.put(className);
            }

            JSONArray gradeArray = new JSONArray();

            for (StudentGrade grade : grades) {
                JSONObject object = new JSONObject();

                object.put("student", grade.student);
                object.put("className", grade.className);
                object.put("monthly", grade.monthly);
                object.put("mid", grade.mid);
                object.put("second", grade.second);
                object.put("finalExam", grade.finalExam);

                gradeArray.put(object);
            }

            prefs.edit()
                    .putString("students", studentArray.toString())
                    .putString("classes", classArray.toString())
                    .putString("grades", gradeArray.toString())
                    .apply();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "حدث خطأ أثناء حفظ البيانات",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadData() {
        students.clear();
        classes.clear();
        grades.clear();

        try {
            String studentsText = prefs.getString("students", "[]");

            JSONArray studentArray = new JSONArray(studentsText);

            for (int i = 0; i < studentArray.length(); i++) {
                students.add(studentArray.getString(i));
            }

            String classesText = prefs.getString("classes", "[]");

            JSONArray classArray = new JSONArray(classesText);

            for (int i = 0; i < classArray.length(); i++) {
                classes.add(classArray.getString(i));
            }

            String gradesText = prefs.getString("grades", "[]");

            JSONArray gradeArray = new JSONArray(gradesText);

            for (int i = 0; i < gradeArray.length(); i++) {

                JSONObject object = gradeArray.getJSONObject(i);

                grades.add(new StudentGrade(
                        object.optString("student"),
                        object.optString("className"),
                        object.optDouble("monthly", 0),
                        object.optDouble("mid", 0),
                        object.optDouble("second", 0),
                        object.optDouble("finalExam", 0)
                ));
            }

        } catch (Exception e) {
            // في حال كانت البيانات غير موجودة أو تالفة
        }
    }

    // ============================================================
    // الواجهة الأساسية
    // ============================================================

    private void prepareScreen(String title) {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(16));
        root.setBackgroundColor(Color.rgb(248, 249, 250));

        TextView titleView = new TextView(this);

        titleView.setText(title);
        titleView.setTextSize(25);
        titleView.setTextColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(
                dp(10),
                dp(18),
                dp(10),
                dp(18)
        );
        titleView.setBackgroundColor(primary);

        root.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        setContentView(scroll(root));
    }

    private ScrollView scroll(View view) {

        ScrollView scrollView = new ScrollView(this);

        scrollView.setFillViewport(true);
        scrollView.addView(view);

        return scrollView;
    }

    private TextView text(String value, float size) {

        TextView view = new TextView(this);

        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(darkText);
        view.setGravity(Gravity.RIGHT);
        view.setPadding(
                dp(8),
                dp(10),
                dp(8),
                dp(10)
        );

        return view;
    }

    private Button button(String title) {

        Button button = new Button(this);

        button.setText(title);
        button.setTextSize(18);
        button.setAllCaps(false);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(primary);
        button.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                dp(7),
                0,
                dp(7)
        );

        button.setLayoutParams(params);

        return button;
    }

    private EditText input(String hint) {

        EditText editText = new EditText(this);

        editText.setHint(hint);
        editText.setTextSize(18);
        editText.setSingleLine(true);
        editText.setGravity(Gravity.RIGHT);

        editText.setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
        );

        return editText;
    }

    private int dp(int value) {
        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    // ============================================================
    // الصفحة الرئيسية
    // ============================================================

    private void showHome() {

        prepareScreen("سجل درجات الاجتماعيات");

        TextView teacher = text(
                "الأستاذ أمير محمد",
                21
        );

        teacher.setGravity(Gravity.CENTER);
        teacher.setTextColor(primary);

        root.addView(teacher);

        TextView subtitle = text(
                "نظام إدارة درجات الطلاب",
                18
        );

        subtitle.setGravity(Gravity.CENTER);

        root.addView(subtitle);

        TextView information = text(
                "عدد الطلاب: " + students.size()
                        + "\nعدد الشعب: " + classes.size(),
                17
        );

        information.setGravity(Gravity.CENTER);

        root.addView(information);

        Button studentsButton =
                button("إدارة الطلاب");

        studentsButton.setOnClickListener(
                v -> showStudents()
        );

        root.addView(studentsButton);

        Button classesButton =
                button("الشعب الدراسية");

        classesButton.setOnClickListener(
                v -> showClasses()
        );

        root.addView(classesButton);

        Button gradesButton =
                button("إدخال الدرجات");

        gradesButton.setOnClickListener(
                v -> showGrades()
        );

        root.addView(gradesButton);

        Button recordsButton =
                button("طباعة السجلات");

        recordsButton.setOnClickListener(
                v -> showRecords()
        );

        root.addView(recordsButton);
    }

    // ============================================================
    // إدارة الطلاب
    // ============================================================

    private void showStudents() {

        prepareScreen("إدارة الطلاب");

        Button add = button("➕ إضافة طالب");

        add.setOnClickListener(
                v -> addStudentDialog()
        );

        root.addView(add);

        if (students.isEmpty()) {

            TextView empty = text(
                    "لا يوجد طلاب حالياً.\nاضغط على «إضافة طالب» لإضافة أول طالب.",
                    18
            );

            empty.setGravity(Gravity.CENTER);

            root.addView(empty);

        } else {

            TextView count = text(
                    "عدد الطلاب: " + students.size(),
                    18
            );

            root.addView(count);

            for (int i = 0; i < students.size(); i++) {

                final int index = i;

                LinearLayout row =
                        new LinearLayout(this);

                row.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                row.setGravity(Gravity.CENTER_VERTICAL);

                TextView name = text(
                        (i + 1) + " - " + students.get(i),
                        18
                );

                name.setGravity(Gravity.RIGHT);

                row.addView(
                        name,
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        )
                );

                Button delete =
                        new Button(this);

                delete.setText("حذف");
                delete.setTextSize(15);
                delete.setAllCaps(false);

                delete.setOnClickListener(
                        v -> deleteStudent(index)
                );

                row.addView(
                        delete,
                        new LinearLayout.LayoutParams(
                                dp(80),
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                root.addView(row);
            }
        }

        Button back = button("⬅ العودة للرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        root.addView(back);
    }

    private void addStudentDialog() {

        final EditText input =
                input("اسم الطالب");

        new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(input)
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
                                        "يرجى إدخال اسم الطالب",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            if (students.contains(name)) {

                                Toast.makeText(
                                        this,
                                        "الطالب موجود مسبقاً",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            students.add(name);

                            saveData();

                            showStudents();

                            Toast.makeText(
                                    this,
                                    "تمت إضافة الطالب",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    private void deleteStudent(int index) {

        if (index < 0 || index >= students.size()) {
            return;
        }

        String name = students.get(index);

        new AlertDialog.Builder(this)
                .setTitle("حذف الطالب")
                .setMessage(
                        "هل تريد حذف الطالب:\n" + name + " ؟"
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            students.remove(index);

                            for (int i = grades.size() - 1;
                                 i >= 0;
                                 i--) {

                                if (grades.get(i).student
                                        .equals(name)) {

                                    grades.remove(i);
                                }
                            }

                            saveData();

                            showStudents();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // ============================================================
    // الشعب الدراسية
    // ============================================================

    private void showClasses() {

        prepareScreen("الشعب الدراسية");

        Button add = button("➕ إضافة شعبة");

        add.setOnClickListener(
                v -> addClassDialog()
        );

        root.addView(add);

        if (classes.isEmpty()) {

            TextView empty = text(
                    "لا توجد شعب دراسية حالياً.",
                    18
            );

            empty.setGravity(Gravity.CENTER);

            root.addView(empty);

        } else {

            TextView count = text(
                    "عدد الشعب: " + classes.size(),
                    18
            );

            root.addView(count);

            for (int i = 0; i < classes.size(); i++) {

                final int index = i;

                LinearLayout row =
                        new LinearLayout(this);

                row.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                row.setGravity(
                        Gravity.CENTER_VERTICAL
                );

                TextView name = text(
                        (i + 1) + " - " + classes.get(i),
                        18
                );

                row.addView(
                        name,
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        )
                );

                Button delete =
                        new Button(this);

                delete.setText("حذف");
                delete.setAllCaps(false);

                delete.setOnClickListener(
                        v -> deleteClass(index)
                );

                row.addView(
                        delete,
                        new LinearLayout.LayoutParams(
                                dp(80),
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );

                root.addView(row);
            }
        }

        Button back = button("⬅ العودة للرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        root.addView(back);
    }

    private void addClassDialog() {

        final EditText input =
                input("اسم الشعبة");

        new AlertDialog.Builder(this)
                .setTitle("إضافة شعبة")
                .setView(input)
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
                                        "يرجى إدخال اسم الشعبة",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            if (classes.contains(name)) {

                                Toast.makeText(
                                        this,
                                        "الشعبة موجودة مسبقاً",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            classes.add(name);

                            saveData();

                            showClasses();

                            Toast.makeText(
                                    this,
                                    "تمت إضافة الشعبة",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    private void deleteClass(int index) {

        if (index < 0 || index >= classes.size()) {
            return;
        }

        String name = classes.get(index);

        new AlertDialog.Builder(this)
                .setTitle("حذف الشعبة")
                .setMessage(
                        "هل تريد حذف الشعبة:\n" + name + " ؟"
                )
                .setPositiveButton(
                        "حذف",
                 (dialog, which) -> {

                    classes.remove(index);

                    saveData();

                    showClasses();
                }
        )
        .setNegativeButton(
                "إلغاء",
                null
        )
        .show();
    }

    // ============================================================
    // إدخال الدرجات
    // ============================================================

    private void showGrades() {

        prepareScreen("إدخال الدرجات");

        if (students.isEmpty()) {

            TextView message = text(
                    "لا يوجد طلاب.\nأضف الطلاب أولاً من إدارة الطلاب.",
                    19
            );

            message.setGravity(Gravity.CENTER);

            root.addView(message);

        } else {

            TextView message = text(
                    "اختر الطالب وأدخل درجاته ثم اضغط حفظ الدرجات.",
                    17
            );

            root.addView(message);

            final Spinner studentSpinner =
                    new Spinner(this);

            ArrayAdapter<String> studentAdapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            students
                    );

            studentAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
            );

            studentSpinner.setAdapter(studentAdapter);

            root.addView(studentSpinner);

            final Spinner classSpinner =
                    new Spinner(this);

            ArrayList<String> classOptions =
                    new ArrayList<>();

            if (classes.isEmpty()) {
                classOptions.add("بدون شعبة");
            } else {
                classOptions.addAll(classes);
            }

            ArrayAdapter<String> classAdapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            classOptions
                    );

            classAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
            );

            classSpinner.setAdapter(classAdapter);

            root.addView(classSpinner);

            EditText monthly =
                    gradeInput("درجة الشهر الأول");

            EditText mid =
                    gradeInput("درجة نصف السنة");

            EditText second =
                    gradeInput("درجة الشهر الثاني");

            EditText finalExam =
                    gradeInput("درجة الامتحان النهائي");

            root.addView(monthly);
            root.addView(mid);
            root.addView(second);
            root.addView(finalExam);

            Button save =
                    button("💾 حفظ الدرجات");

            save.setOnClickListener(v -> {

                String student =
                        studentSpinner.getSelectedItem().toString();

                String className =
                        classSpinner.getSelectedItem().toString();

                double monthlyValue =
                        gradeValue(monthly);

                double midValue =
                        gradeValue(mid);

                double secondValue =
                        gradeValue(second);

                double finalValue =
                        gradeValue(finalExam);

                if (monthlyValue < 0 ||
                        midValue < 0 ||
                        secondValue < 0 ||
                        finalValue < 0) {

                    Toast.makeText(
                            this,
                            "الدرجات يجب أن تكون من 0 إلى 100",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                StudentGrade old =
                        findGrade(student);

                if (old != null) {

                    old.className = className;
                    old.monthly = monthlyValue;
                    old.mid = midValue;
                    old.second = secondValue;
                    old.finalExam = finalValue;

                } else {

                    grades.add(
                            new StudentGrade(
                                    student,
                                    className,
                                    monthlyValue,
                                    midValue,
                                    secondValue,
                                    finalValue
                            )
                    );
                }

                saveData();

                Toast.makeText(
                        this,
                        "تم حفظ درجات الطالب بنجاح",
                        Toast.LENGTH_SHORT
                ).show();
            });

            root.addView(save);

            Button records =
                    button("📋 عرض السجلات");

            records.setOnClickListener(
                    v -> showRecords()
            );

            root.addView(records);
        }

        Button back =
                button("⬅ العودة للرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        root.addView(back);
    }

    private EditText gradeInput(String hint) {

        EditText editText = input(hint);

        editText.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        return editText;
    }

    private double gradeValue(EditText editText) {

        String value =
                editText.getText().toString().trim();

        if (value.isEmpty()) {
            return 0;
        }

        try {

            double number =
                    Double.parseDouble(value);

            if (number < 0 || number > 100) {
                return -1;
            }

            return number;

        } catch (Exception e) {

            return -1;
        }
    }

    private StudentGrade findGrade(String student) {

        for (StudentGrade grade : grades) {

            if (grade.student.equals(student)) {
                return grade;
            }
        }

        return null;
    }

    // ============================================================
    // السجلات
    // ============================================================

    private void showRecords() {

        prepareScreen("سجلات الطلاب");

        if (grades.isEmpty()) {

            TextView empty = text(
                    "لا توجد درجات محفوظة حتى الآن.",
                    19
            );

            empty.setGravity(Gravity.CENTER);

            root.addView(empty);

        } else {

            for (StudentGrade grade : grades) {

                TextView record = text(
                        "👤 الطالب: " + grade.student
                                + "\n🏫 الشعبة: " + grade.className
                                + "\nالشهر الأول: " + grade.monthly
                                + "\nنصف السنة: " + grade.mid
                                + "\nالشهر الثاني: " + grade.second
                                + "\nالنهائي: " + grade.finalExam
                                + "\nالمجموع: " + grade.total()
                                + "\nالمعدل: "
                                + String.format(
                                        Locale.US,
                                        "%.2f",
                                        grade.average()
                                ),
                        17
                );

                root.addView(record);
            }
        }

        Button share =
                button("📤 مشاركة السجلات");

        share.setOnClickListener(
                v -> shareRecords()
        );

        root.addView(share);

        Button back =
                button("⬅ العودة للرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        root.addView(back);
    }

    private void shareRecords() {

        if (grades.isEmpty()) {

            Toast.makeText(
                    this,
                    "لا توجد سجلات لمشاركتها",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringBuilder text =
                new StringBuilder();

        text.append("سجل درجات الاجتماعيات\n");
        text.append("الأستاذ أمير محمد\n");
        text.append("====================\n\n");

        for (StudentGrade grade : grades) {

            text.append("الطالب: ")
                    .append(grade.student)
                    .append("\n");

            text.append("الشعبة: ")
                    .append(grade.className)
                    .append("\n");

            text.append("الشهر الأول: ")
                    .append(grade.monthly)
                    .append("\n");

            text.append("نصف السنة: ")
                    .append(grade.mid)
                    .append("\n");

            text.append("الشهر الثاني: ")
                    .append(grade.second)
                    .append("\n");

            text.append("النهائي: ")
                    .append(grade.finalExam)
                    .append("\n");

            text.append("المجموع: ")
                    .append(grade.total())
                    .append("\n");

            text.append("المعدل: ")
                    .append(
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    grade.average()
                            )
                    )
                    .append("\n");

            text.append("--------------------\n");
        }

        Intent intent =
                new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");

        intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "سجل درجات الاجتماعيات"
        );

        intent.putExtra(
                Intent.EXTRA_TEXT,
                text.toString()
        );

        startActivity(
                Intent.createChooser(
                        intent,
                        "مشاركة السجلات"
                )
        );
    }

    @Override
    public void onBackPressed() {
        showHome();
    }
}          

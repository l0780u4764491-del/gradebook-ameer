package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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

    private final int darkText = Color.rgb(35, 35, 35);
    private final int primary = Color.rgb(25, 90, 150);
    private final int background = Color.rgb(248, 249, 250);

    // ============================================================
    // بداية التطبيق
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(
                "gradebook_data",
                MODE_PRIVATE
        );

        loadData();

        showHome();
    }

    // ============================================================
    // كلاس الدرجات
    // ============================================================

    private static class StudentGrade {

        String student;
        String className;

        double monthly;
        double mid;
        double second;
        double finalExam;

        StudentGrade(
                String student,
                String className,
                double monthly,
                double mid,
                double second,
                double finalExam
        ) {
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

    // ============================================================
    // حفظ البيانات
    // ============================================================

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

                object.put(
                        "student",
                        grade.student
                );

                object.put(
                        "className",
                        grade.className
                );

                object.put(
                        "monthly",
                        grade.monthly
                );

                object.put(
                        "mid",
                        grade.mid
                );

                object.put(
                        "second",
                        grade.second
                );

                object.put(
                        "finalExam",
                        grade.finalExam
                );

                gradeArray.put(object);
            }

            prefs.edit()
                    .putString(
                            "students",
                            studentArray.toString()
                    )
                    .putString(
                            "classes",
                            classArray.toString()
                    )
                    .putString(
                            "grades",
                            gradeArray.toString()
                    )
                    .apply();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "حدث خطأ أثناء حفظ البيانات",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    // ============================================================
    // تحميل البيانات
    // ============================================================

    private void loadData() {

        students.clear();
        classes.clear();
        grades.clear();

        try {

            String studentsText =
                    prefs.getString(
                            "students",
                            "[]"
                    );

            JSONArray studentArray =
                    new JSONArray(studentsText);

            for (int i = 0;
                 i < studentArray.length();
                 i++) {

                students.add(
                        studentArray.getString(i)
                );
            }

            String classesText =
                    prefs.getString(
                            "classes",
                            "[]"
                    );

            JSONArray classArray =
                    new JSONArray(classesText);

            for (int i = 0;
                 i < classArray.length();
                 i++) {

                classes.add(
                        classArray.getString(i)
                );
            }

            String gradesText =
                    prefs.getString(
                            "grades",
                            "[]"
                    );

            JSONArray gradeArray =
                    new JSONArray(gradesText);

            for (int i = 0;
                 i < gradeArray.length();
                 i++) {

                JSONObject object =
                        gradeArray.getJSONObject(i);

                grades.add(
                        new StudentGrade(
                                object.optString(
                                        "student"
                                ),
                                object.optString(
                                        "className"
                                ),
                                object.optDouble(
                                        "monthly",
                                        0
                                ),
                                object.optDouble(
                                        "mid",
                                        0
                                ),
                                object.optDouble(
                                        "second",
                                        0
                                ),
                                object.optDouble(
                                        "finalExam",
                                        0
                                )
                        )
                );
            }

        } catch (Exception e) {

            // في حال عدم وجود بيانات سابقة
        }
    }

    // ============================================================
    // تجهيز الشاشة
    // ============================================================

    private void prepareScreen(String title) {

        root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        root.setBackgroundColor(
                background
        );

        TextView titleView =
                new TextView(this);

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

        titleView.setBackgroundColor(
                primary
        );

        root.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        setContentView(
                scroll(root)
        );
    }

    // ============================================================
    // ScrollView
    // ============================================================

    private ScrollView scroll(View view) {

        ScrollView scrollView =
                new ScrollView(this);

        scrollView.setFillViewport(true);

        scrollView.addView(view);

        return scrollView;
    }

    // ============================================================
    // نص
    // ============================================================

    private TextView text(
            String value,
            float size
    ) {

        TextView view =
                new TextView(this);

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

    // ============================================================
    // زر
    // ============================================================

    private Button button(String title) {

        Button button =
                new Button(this);

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

    // ============================================================
    // حقل الإدخال
    // ============================================================

    private EditText input(String hint) {

        EditText editText =
                new EditText(this);

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

    // ============================================================
    // تحويل dp
    // ============================================================

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    // ============================================================
    // الصفحة الرئيسية
    // ============================================================

    private void showHome() {

        prepareScreen(
                "سجل درجات الاجتماعيات"
        );

        TextView teacher =
                text(
                        "الأستاذ أمير محمد",
                        21
                );

        teacher.setGravity(
                Gravity.CENTER
        );

        teacher.setTextColor(
                primary
        );

        root.addView(teacher);

        TextView subtitle =
                text(
                        "نظام إدارة درجات الطلاب",
                        18
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        root.addView(subtitle);

        TextView information =
                text(
                        "عدد الطلاب: "
                                + students.size()
                                + "\nعدد الشعب: "
                                + classes.size()
                                + "\nعدد السجلات: "
                                + grades.size(),
                        17
                );

        information.setGravity(
                Gravity.CENTER
        );

        root.addView(information);

        Button studentsButton =
                button(
                        "👨‍🎓 إدارة الطلاب"
                );

        studentsButton.setOnClickListener(
                v -> showStudents()
        );

        root.addView(
                studentsButton
        );

        Button classesButton =
                button(
                        "🏫 الشعب الدراسية"
                );

        classesButton.setOnClickListener(
                v -> showClasses()
        );

        root.addView(
                classesButton
        );

        Button gradesButton =
                button(
                        "📝 إدخال الدرجات"
                );

        gradesButton.setOnClickListener(
                v -> showGrades()
        );

        root.addView(
                gradesButton
        );

        Button recordsButton =
                button(
                        "📋 السجلات"
                );

        recordsButton.setOnClickListener(
                v -> showRecords()
        );

        root.addView(
                recordsButton
        );
    }

    // ============================================================
    // إدارة الطلاب
    // ============================================================

    private void showStudents() {

        prepareScreen(
                "إدارة الطلاب"
        );

        TextView count =
                text(
                        "عدد الطلاب: "
                                + students.size(),
                        18
                );

        count.setGravity(
                Gravity.CENTER
        );

        root.addView(count);

        final EditText search =
                input(
                        "🔍 البحث عن طالب"
                );

        root.addView(search);

        Button add =
                button(
                        "➕ إضافة طالب"
                );

        add.setOnClickListener(
                v -> addStudentDialog()
        );

        root.addView(add);

        final LinearLayout list =
                new LinearLayout(this);

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        root.addView(list);

        Runnable refreshList =
                () -> {

                    list.removeAllViews();

                    String query =
                            search.getText()
                                    .toString()
                                    .trim()
                                    .toLowerCase(
                                            Locale.ROOT
                                    );

                    int found = 0;

                    for (int i = 0;
                         i < students.size();
                         i++) {

                        String studentName =
                                students.get(i);

                        if (!query.isEmpty()
                                && !studentName
                                .toLowerCase(
                                        Locale.ROOT
                                )
                                .contains(query)) {

                            continue;
                        }

                        found++;

                        final int index = i;

                        LinearLayout row =
                                new LinearLayout(
                                        this
                                );

                        row.setOrientation(
                                LinearLayout.HORIZONTAL
                        );

                        row.setGravity(
                                Gravity.CENTER_VERTICAL
                        );

                        TextView name =
                                text(
                                        found
                                                + " - "
                                                + studentName,
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

                        Button edit =
                                new Button(this);

                        edit.setText("✏️");
                        edit.setAllCaps(false);
                        edit.setTextSize(16);

                        edit.setOnClickListener(
                                v ->
                                        editStudentDialog(
                                                index
                                        )
                        );

                        row.addView(
                                edit,
                                new LinearLayout.LayoutParams(
                                        dp(60),
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                        );

                        Button delete =
                                new Button(this);

                        delete.setText("🗑️");
                        delete.setAllCaps(false);
                        delete.setTextSize(16);

                        delete.setOnClickListener(
                                v ->
                                        deleteStudent(
                                                index
                                        )
                        );

                        row.addView(
                                delete,
                                new LinearLayout.LayoutParams(
                                        dp(60),
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                        );

                        list.addView(row);
                    }

                    if (found == 0) {

                        TextView empty =
                                text(
                                        students.isEmpty()
                                                ? "لا يوجد طلاب حالياً."
                                                : "لا يوجد طالب بهذا الاسم.",
                                        18
                                );

                        empty.setGravity(
                                Gravity.CENTER
                        );

                        list.addView(empty);
                    }
                };

        search.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextCh
                                public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                String query = s.toString().trim().toLowerCase(Locale.ROOT);

                list.removeAllViews();

                for (int i = 0; i < students.size(); i++) {
                    String student = students.get(i);

                    if (student.toLowerCase(Locale.ROOT).contains(query)) {
                        final int index = i;

                        Button item = new Button(MainActivity.this);
                        item.setText("👤 " + student);
                        item.setTextSize(18);
                        item.setGravity(Gravity.CENTER);
                        item.setAllCaps(false);

                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showStudentGrades(index);
                            }
                        });

                        list.addView(item);
                    }
                }

                if (list.getChildCount() == 0) {
                    TextView empty = new TextView(MainActivity.this);
                    empty.setText("لا يوجد طالب مطابق للبحث");
                    empty.setTextSize(18);
                    empty.setGravity(Gravity.CENTER);
                    empty.setPadding(dp(20), dp(30), dp(20), dp(30));
                    list.addView(empty);
                }
            }

            @Override
            public void afterTextChanged(
                    android.text.Editable s
            ) {
            }
        });

    }

    private void showStudentGrades(int index) {
        if (index < 0 || index >= students.size()) {
            return;
        }

        String name = students.get(index);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("درجات الطالب");

        builder.setMessage(
                "الطالب: " + name +
                "\n\nيمكنك إدخال درجات الطالب من قسم إدخال الدرجات."
        );

        builder.setPositiveButton(
                "حسناً",
                null
        );

        builder.show();
    }

}

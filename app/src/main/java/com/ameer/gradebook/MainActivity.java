package com.ameer.gradebook;

import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends android.app.Activity {

    private LinearLayout mainLayout;
    private LinearLayout studentsLayout;

    private SharedPreferences preferences;
    private ArrayList<Student> students = new ArrayList<>();

    private final int DARK = Color.rgb(35, 39, 47);
    private final int BLUE = Color.rgb(40, 100, 190);
    private final int GREEN = Color.rgb(40, 150, 90);
    private final int RED = Color.rgb(200, 55, 55);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("gradebook_data", MODE_PRIVATE);

        loadStudents();
        createInterface();
    }

    // =========================
    // إنشاء الواجهة
    // =========================

    private void createInterface() {

        ScrollView scrollView = new ScrollView(this);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(25, 30, 25, 40);
        mainLayout.setBackgroundColor(Color.rgb(247, 248, 252));

        scrollView.addView(mainLayout);

        // العنوان
        TextView title = textView(
                "سجل درجات الاجتماعيات",
                28,
                Color.rgb(30, 35, 45),
                true
        );
        title.setGravity(Gravity.CENTER);
        mainLayout.addView(title);

        // اسم المدرس
        TextView teacher = textView(
                "الأستاذ أمير محمد",
                22,
                Color.rgb(70, 75, 85),
                true
        );
        teacher.setGravity(Gravity.CENTER);
        addMargin(teacher, 10, 5, 10, 5);
        mainLayout.addView(teacher);

        TextView subtitle = textView(
                "نظام إدارة درجات الطلاب",
                18,
                Color.rgb(100, 105, 115),
                false
        );
        subtitle.setGravity(Gravity.CENTER);
        mainLayout.addView(subtitle);

        // زر إضافة طالب
        Button addButton = button(
                "＋ إضافة طالب جديد",
                BLUE
        );

        addButton.setOnClickListener(v -> showAddStudentDialog());

        addMargin(addButton, 0, 25, 0, 15);
        mainLayout.addView(addButton);

        // زر مسح جميع البيانات
        Button clearButton = button(
                "مسح جميع البيانات",
                RED
        );

        clearButton.setOnClickListener(v -> confirmDeleteAll());

        addMargin(clearButton, 0, 0, 0, 20);
        mainLayout.addView(clearButton);

        // عنوان الطلاب
        TextView listTitle = textView(
                "قائمة الطلاب",
                22,
                Color.rgb(35, 40, 50),
                true
        );

        listTitle.setGravity(Gravity.RIGHT);
        mainLayout.addView(listTitle);

        studentsLayout = new LinearLayout(this);
        studentsLayout.setOrientation(LinearLayout.VERTICAL);

        addMargin(studentsLayout, 0, 15, 0, 0);
        mainLayout.addView(studentsLayout);

        refreshStudents();

        setContentView(scrollView);
    }

    // =========================
    // إضافة طالب
    // =========================

    private void showAddStudentDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 10, 30, 10);

        EditText nameInput = new EditText(this);
        nameInput.setHint("اسم الطالب");
        nameInput.setTextSize(18);
        nameInput.setGravity(Gravity.RIGHT);
        layout.addView(nameInput);

        EditText gradeInput = new EditText(this);
        gradeInput.setHint("درجة الاجتماعيات");
        gradeInput.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );
        gradeInput.setTextSize(18);
        gradeInput.setGravity(Gravity.RIGHT);

        addMargin(gradeInput, 0, 15, 0, 0);
        layout.addView(gradeInput);

        new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(layout)
                .setPositiveButton("إضافة", null)
                .setNegativeButton("إلغاء", null)
                .create()
                .setOnShowListener(dialog -> {

                    AlertDialog alert =
                            (AlertDialog) dialog;

                    alert.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(v -> {

                        String name =
                                nameInput.getText()
                                        .toString()
                                        .trim();

                        String gradeText =
                                gradeInput.getText()
                                        .toString()
                                        .trim();

                        if (name.isEmpty()) {
                            nameInput.setError("اكتب اسم الطالب");
                            return;
                        }

                        if (gradeText.isEmpty()) {
                            gradeInput.setError("اكتب الدرجة");
                            return;
                        }

                        double grade;

                        try {
                            grade = Double.parseDouble(gradeText);
                        } catch (Exception e) {
                            gradeInput.setError("الدرجة غير صحيحة");
                            return;
                        }

                        if (grade < 0 || grade > 100) {
                            gradeInput.setError(
                                    "الدرجة يجب أن تكون بين 0 و100"
                            );
                            return;
                        }

                        students.add(
                                new Student(name, grade)
                        );

                        saveStudents();
                        refreshStudents();

                        alert.dismiss();
                    });
                }).show();
    }

    // =========================
    // عرض الطلاب
    // =========================

    private void refreshStudents() {

        if (studentsLayout == null)
            return;

        studentsLayout.removeAllViews();

        if (students.isEmpty()) {

            TextView empty = textView(
                    "لا يوجد طلاب حالياً\nاضغط على «إضافة طالب جديد» للبدء",
                    18,
                    Color.GRAY,
                    false
            );

            empty.setGravity(Gravity.CENTER);
            empty.setPadding(20, 40, 20, 40);

            studentsLayout.addView(empty);

            return;
        }

        for (int i = 0; i < students.size(); i++) {

            Student student = students.get(i);

            studentsLayout.addView(
                    createStudentCard(student, i)
            );
        }
    }

    // =========================
    // بطاقة الطالب
    // =========================

    private LinearLayout createStudentCard(
            Student student,
            int position
    ) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(25, 20, 25, 20);

        GradientDrawableHelper.setBackground(
                card,
                Color.WHITE,
                18
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 18);

        card.setLayoutParams(cardParams);

        // رقم الطالب
        TextView number = textView(
                "الطالب رقم " + (position + 1),
                14,
                Color.GRAY,
                false
        );

        number.setGravity(Gravity.RIGHT);
        card.addView(number);

        // اسم الطالب
        TextView name = textView(
                student.name,
                22,
                Color.rgb(30, 35, 45),
                true
        );

        name.setGravity(Gravity.RIGHT);

        addMargin(name, 0, 5, 0, 5);
        card.addView(name);

        // الدرجة
        String result = String.format(
                Locale.US,
                "الدرجة: %.1f / 100",
                student.grade
        );

        TextView grade = textView(
                result,
                18,
                getGradeColor(student.grade),
                true
        );

        grade.setGravity(Gravity.RIGHT);
        card.addView(grade);

        // التقييم
        TextView evaluation = textView(
                getEvaluation(student.grade),
                16,
                getGradeColor(student.grade),
                false
        );

        evaluation.setGravity(Gravity.RIGHT);
        card.addView(evaluation);

        // الأزرار
        LinearLayout buttons = new LinearLayout(this);

        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setGravity(Gravity.CENTER);

        Button edit = button(
                "تعديل",
                BLUE
        );

        Button delete = button(
                "حذف",
                RED
        );

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        buttonParams.setMargins(5, 15, 5, 0);

        buttons.addView(edit, buttonParams);
        buttons.addView(delete, buttonParams);

        card.addView(buttons);

        edit.setOnClickListener(
                v -> showEditStudentDialog(position)
        );

        delete.setOnClickListener(
                v -> confirmDeleteStudent(position)
        );

        return card;
    }

    // =========================
    // تعديل طالب
    // =========================

    private void showEditStudentDialog(int position) {

        Student student = students.get(position);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 10, 30, 10);

        EditText nameInput = new EditText(this);
        nameInput.setText(student.name);
        nameInput.setTextSize(18);
        nameInput.setGravity(Gravity.RIGHT);
        layout.addView(nameInput);

        EditText gradeInput = new EditText(this);

        gradeInput.setText(
                String.format(
                        Locale.US,
                        "%.1f",
                        student.grade
                )
        );

        gradeInput.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        gradeInput.setTextSize(18);
        gradeInput.setGravity(Gravity.RIGHT);

        addMargin(gradeInput, 0, 15, 0, 0);
        layout.addView(gradeInput);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("تعديل بيانات الطالب")
                        .setView(layout)
                        .setPositiveButton("حفظ", null)
                        .setNegativeButton("إلغاء", null)
                        .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String name =
                        nameInput.getText()
                                .toString()
                                .trim();

                String gradeText =
                        gradeInput.getText()
                                .toString()
                                .trim();

                if (name.isEmpty()) {
                    nameInput.setError("اكتب اسم الطالب");
                    return;
                }

                double grade;

                try {
                    grade =
                            Double.parseDouble(gradeText);
                } catch (Exception e) {
                    gradeInput.setError(
                            "الدرجة غير صحيحة"
                    );
                    return;
                }

                if (grade < 0 || grade > 100) {
                    gradeInput.setError(
                            "الدرجة يجب أن تكون بين 0 و100"
                    );
                    return;
                }

                student.name = name;
                student.grade = grade;

                saveStudents();
                refreshStudents();

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    // =========================
    // حذف طالب
    // =========================

    private void confirmDeleteStudent(int position) {

        Student student = students.get(position);

        new AlertDialog.Builder(this)
                .setTitle("حذف الطالب")
                .setMessage(
                        "هل تريد حذف الطالب:\n" +
                        student.name + " ؟"
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            students.remove(position);

                            saveStudents();
                            refreshStudents();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================
    // حذف الجميع
    // =========================

    private void confirmDeleteAll() {

        if (students.isEmpty()) {
            Toast.makeText(
                    this,
                    "لا توجد بيانات للحذف",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("مسح جميع البيانات")
                .setMessage(
                        "سيتم حذف جميع الطلاب والدرجات.\n" +
                        "هل أنت متأكد؟"
                )
                .setPositiveButton(
                        "نعم، احذف",
                        (dialog, which) -> {

                            students.clear();

                            saveStudents();
                            refreshStudents();
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================
    // حفظ البيانات
    // =========================

    private void saveStudents() {

        try {

            JSONArray array = new JSONArray();

            for (Student student : students) {

                JSONObject object =
                        new JSONObject();

                object.put(
                        "name",
                        student.name
                );

                object.put(
                        "grade",
                        student.grade
                );

                array.put(object);
            }

            preferences
                    .edit()
                    .putString(
                            "students",
                            array.toString()
                    )
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // تحميل البيانات
    // =========================

    private void loadStudents() {

        students.clear();

        String data =
                preferences.getString(
                        "students",
                        ""
                );

        if (data.isEmpty())
            return;

        try {

            JSONArray array =
                    new JSONArray(data);

            for (int i = 0;
                 i < array.length();
                 i++) {

                JSONObject object =
                        array.getJSONObject(i);

                String name =
                        object.getString("name");

                double grade =
                        object.getDouble("grade");

                students.add(
                        new Student(name, grade)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // تقييم الدرجة
    // =========================

    private String getEvaluation(double grade) {

        if (grade >= 90)
            return "ممتاز جداً";

        if (grade >= 80)
            return "ممتاز";

        if (grade >= 70)
            return "جيد جداً";

        if (grade >= 60)
            return "جيد";

        if (grade >= 50)
            return "مقبول";

        return "راسب";
    }

    private int getGradeColor(double grade) {

        if (grade >= 50)
            return GREEN;

        return RED;
    }

    // =========================
    // أدوات الواجهة
    // =========================

    private TextView textView(
            String text,
            float size,
            int color,
            boolean bold
    ) {

        TextView view =
                new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold)
            view.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

        view.setPadding(5, 5, 5, 5);

        return view;
    }

    private Button button(
            String text,
            int color
    ) {

        Button button =
                new Button(this);

        button.setText(text);
        button.setTextSize(17);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);

        GradientDrawableHelper.setBackground(
                button,
                color,
                14
        );

        return button;
    }

    private void addMargin(
            View view,
            int left,
            int top,
            int right,
            int bottom
    ) {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                left,
                top,
                right,
                bottom
        );

        view.setLayoutParams(params);
    }

    // =========================
    // كلاس الطالب
    // =========================

    private static class Student {

        String name;
        double grade;

        Student(
                String name,
                double grade
        ) {

            this.name = name;
            this.grade = grade;
        }
    }

    // =========================
    // خلفيات مستديرة
    // =========================

    private static class GradientDrawableHelper {

        static void setBackground(
                View view,
                int color,
                int radius
        ) {

            android.graphics.drawable.GradientDrawable drawable =
                    new android.graphics.drawable.GradientDrawable();

            drawable.setColor(color)

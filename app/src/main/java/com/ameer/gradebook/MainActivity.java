package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * سجل درجات الاجتماعيات
 * الأستاذ أمير محمد
 *
 * تطبيق كامل لإدارة:
 * - الطلاب
 * - الشعب الدراسية
 * - الدرجات
 * - طباعة السجلات
 *
 * لا يحتاج إلى مكتبات خارجية.
 */
public class MainActivity extends Activity {

    // ============================================================
    // المتغيرات الأساسية
    // ============================================================

    private LinearLayout rootLayout;
    private SharedPreferences prefs;

    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<String> classes = new ArrayList<>();

    private static final String PREFS = "gradebook_data";
    private static final String STUDENTS_KEY = "students";
    private static final String CLASSES_KEY = "classes";

    private static final int BG_COLOR = Color.rgb(247, 248, 252);
    private static final int TEXT_COLOR = Color.rgb(35, 40, 50);
    private static final int SUBTEXT_COLOR = Color.rgb(90, 95, 105);
    private static final int BLUE = Color.rgb(40, 100, 190);
    private static final int GREEN = Color.rgb(40, 145, 85);
    private static final int RED = Color.rgb(200, 55, 55);
    private static final int GRAY = Color.rgb(100, 105, 115);
    private static final int CARD_COLOR = Color.WHITE;

    // ============================================================
    // عند تشغيل التطبيق
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        loadData();

        // إضافة شعب افتراضية عند أول تشغيل
        if (classes.isEmpty()) {
            classes.add("الشعبة الأولى");
            classes.add("الشعبة الثانية");
            saveData();
        }

        showHome();
    }

    // ============================================================
    // الشاشة الرئيسية
    // ============================================================

    private void showHome() {

        rootLayout = createRoot();

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(25), dp(20), dp(35));

        // العنوان
        TextView title = text(
                "سجل درجات الاجتماعيات",
                29,
                TEXT_COLOR,
                true
        );
        title.setGravity(Gravity.CENTER);
        content.addView(title);

        // اسم المدرس
        TextView teacher = text(
                "الأستاذ أمير محمد",
                23,
                SUBTEXT_COLOR,
                true
        );
        teacher.setGravity(Gravity.CENTER);
        setMargins(teacher, 0, 8, 0, 5);
        content.addView(teacher);

        // الوصف
        TextView description = text(
                "نظام إدارة درجات الطلاب",
                18,
                SUBTEXT_COLOR,
                false
        );
        description.setGravity(Gravity.CENTER);
        setMargins(description, 0, 0, 0, 25);
        content.addView(description);

        // زر إدارة الطلاب
        Button studentsButton = menuButton(
                "إدارة الطلاب",
                BLUE
        );
        studentsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showStudents();
                    }
                }
        );
        content.addView(studentsButton);

        // زر الشعب
        Button classesButton = menuButton(
                "الشعب الدراسية",
                GREEN
        );
        classesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showClasses();
                    }
                }
        );
        content.addView(classesButton);

        // زر الدرجات
        Button gradesButton = menuButton(
                "إدخال الدرجات",
                Color.rgb(110, 80, 180)
        );
        gradesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGrades();
                    }
                }
        );
        content.addView(gradesButton);

        // زر الطباعة
        Button printButton = menuButton(
                "طباعة السجلات",
                Color.rgb(70, 100, 120)
        );
        printButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        printRecords();
                    }
                }
        );
        content.addView(printButton);

        // إحصائيات
        LinearLayout stats = card();
        stats.setOrientation(LinearLayout.VERTICAL);

        TextView statsTitle = text(
                "ملخص السجل",
                20,
                TEXT_COLOR,
                true
        );
        statsTitle.setGravity(Gravity.RIGHT);
        stats.addView(statsTitle);

        TextView statsText = text(
                "عدد الطلاب: " + students.size()
                        + "\nعدد الشعب: " + classes.size()
                        + "\nالطلاب الناجحون: " + countPassed()
                        + "\nالطلاب الراسبون: " + countFailed(),
                17,
                SUBTEXT_COLOR,
                false
        );
        statsText.setGravity(Gravity.RIGHT);

        setMargins(statsText, 0, 12, 0, 0);
        stats.addView(statsText);

        setMargins(stats, 0, 25, 0, 0);
        content.addView(stats);

        scroll.addView(content);
        rootLayout.addView(scroll);

        setContentView(rootLayout);
    }

    // ============================================================
    // إدارة الطلاب
    // ============================================================

    private void showStudents() {

        rootLayout = createRoot();

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(20), dp(18), dp(30));

        // شريط العنوان
        content.addView(topBar(
                "إدارة الطلاب",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showHome();
                    }
                }
        ));

        Button add = menuButton(
                "＋ إضافة طالب",
                BLUE
        );

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddStudentDialog();
                    }
                }
        );

        content.addView(add);

        if (students.isEmpty()) {

            TextView empty = text(
                    "لا يوجد طلاب حالياً\n\nاضغط «إضافة طالب» لإضافة أول طالب.",
                    19,
                    GRAY,
                    false
            );

            empty.setGravity(Gravity.CENTER);
            setMargins(empty, 0, 45, 0, 0);

            content.addView(empty);

        } else {

            for (int i = 0; i < students.size(); i++) {

                final int index = i;
                Student s = students.get(i);

                LinearLayout studentCard = card();
                studentCard.setOrientation(
                        LinearLayout.VERTICAL
                );

                TextView name = text(
                        s.name,
                        21,
                        TEXT_COLOR,
                        true
                );
                name.setGravity(Gravity.RIGHT);
                studentCard.addView(name);

                TextView classText = text(
                        "الشعبة: " + s.className,
                        16,
                        SUBTEXT_COLOR,
                        false
                );
                classText.setGravity(Gravity.RIGHT);
                studentCard.addView(classText);

                TextView grade = text(
                        "الدرجة: "
                                + formatGrade(s.grade)
                                + " / 100",
                        18,
                        getGradeColor(s.grade),
                        true
                );
                grade.setGravity(Gravity.RIGHT);
                studentCard.addView(grade);

                TextView result = text(
                        getEvaluation(s.grade),
                        16,
                        getGradeColor(s.grade),
                        true
                );
                result.setGravity(Gravity.RIGHT);
                studentCard.addView(result);

                LinearLayout buttons = new LinearLayout(this);
                buttons.setOrientation(
                        LinearLayout.HORIZONTAL
                );

                Button edit = smallButton(
                        "تعديل",
                        BLUE
                );

                Button delete = smallButton(
                        "حذف",
                        RED
                );

                LinearLayout.LayoutParams bp =
                        new LinearLayout.LayoutParams(
                                0,
                                dp(50),
                                1
                        );

                bp.setMargins(
                        dp(4),
                        dp(12),
                        dp(4),
                        0
                );

                buttons.addView(edit, bp);
                buttons.addView(delete, bp);

                studentCard.addView(buttons);

                edit.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showEditStudentDialog(index);
                            }
                        }
                );

                delete.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteStudent(index);
                            }
                        }
                );

                setMargins(
                        studentCard,
                        0,
                        15,
                        0,
                        0
                );

                content.addView(studentCard);
            }
        }

        ScrollView scroll = new ScrollView(this);
        scroll.addView(content);

        rootLayout.addView(scroll);

        setContentView(rootLayout);
    }

    // ============================================================
    // إضافة طالب
    // ============================================================

    private void showAddStudentDialog() {

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        EditText name = input(
                "اسم الطالب"
        );

        form.addView(name);

        Spinner classSpinner = new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        classes
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        classSpinner.setAdapter(adapter);

        setMargins(
                classSpinner,
                0,
                12,
                0,
                0
        );

        form.addView(classSpinner);

        EditText grade = input(
                "الدرجة من 100"
        );

        grade.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        setMargins(
                grade,
                0,
                12,
                0,
                0
        );

        form.addView(grade);

        final AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("إضافة طالب")
                        .setView(form)
                        .setPositiveButton(
                                "إضافة",
                                null
                        )
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                new DialogInterfaceHelper() {
                    @Override
                    public void run() {

                        dialog.getButton(
                                AlertDialog.BUTTON_POSITIVE
                        ).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String studentName =
                                                name.getText()
                                                        .toString()
                                                        .trim();

                                        String gradeText =
                                                grade.getText()
                                                        .toString()
                                                        .trim();

                                        if (studentName.isEmpty()) {
                                            name.setError(
                                                    "اكتب اسم الطالب"
                                            );
                                            return;
                                        }

                                        if (gradeText.isEmpty()) {
                                            grade.setError(
                                                    "اكتب الدرجة"
                                            );
                                            return;
                                        }

                                        double value;

                                        try {
                                            value =
                                                    Double.parseDouble(
                                                            gradeText
                                                    );
                                        } catch (Exception e) {
                                            grade.setError(
                                                    "الدرجة غير صحيحة"
                                            );
                                            return;
                                        }

                                        if (value < 0 ||
                                                value > 100) {

                                            grade.setError(
                                                    "الدرجة يجب أن تكون بين 0 و100"
                                            );
                                            return;
                                        }

                                        String selectedClass =
                                                classSpinner
                                                        .getSelectedItem()
                                                        .toString();

                                        students.add(
                                                new Student(
                                                        studentName,
                                                        selectedClass,
                                                        value
                                                )
                                        );

                                        saveData();

                                        dialog.dismiss();

                                        showStudents();
                                    }
                                }
                        );
                    }
                }
        );

        dialog.show();
    }

    // ============================================================
    // تعديل طالب
    // ============================================================

    private void showEditStudentDialog(
            final int position
    ) {

        final Student student =
                students.get(position);

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        final EditText name =
                input("اسم الطالب");

        name.setText(student.name);

        form.addView(name);

        final Spinner spinner =
                new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        classes
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        int selected = classes.indexOf(
                student.className
        );

        if (selected >= 0)
            spinner.setSelection(selected);

        setMargins(
                spinner,
                0,
                12,
                0,
                0
        );

        form.addView(spinner);

        final EditText grade =
                input("الدرجة من 100");

        grade.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        grade.setText(
                formatGrade(student.grade)
        );

        setMargins(
                grade,
                0,
                12,
                0,
                0
        );

        form.addView(grade);

        final AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("تعديل الطالب")
                        .setView(form)
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .setNegativeButton(
                                "إلغاء

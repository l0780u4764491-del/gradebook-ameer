package com.ameer.gradebook;

import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends android.app.Activity {

    private LinearLayout mainLayout;
    private SharedPreferences prefs;

    private final String PREFS = "gradebook_data";
    private final String STUDENTS = "students";
    private final String BRANCHES = "branches";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        showHome();
    }

    // =========================================================
    // الصفحة الرئيسية
    // =========================================================

    private void showHome() {

        mainLayout = createMainLayout();

        TextView title = text(
                "سجل درجات الاجتماعيات",
                30,
                true
        );

        TextView teacher = text(
                "الأستاذ أمير محمد",
                25,
                true
        );

        TextView subtitle = text(
                "نظام إدارة درجات الطلاب",
                20,
                false
        );

        mainLayout.addView(title);
        mainLayout.addView(teacher);
        mainLayout.addView(subtitle);

        Space space = new Space(this);
        mainLayout.addView(space, new LinearLayout.LayoutParams(
                1, 60
        ));

        Button students = button("إدارة الطلاب");
        Button branches = button("الشعب الدراسية");
        Button grades = button("إدخال الدرجات");
        Button print = button("طباعة السجلات");

        mainLayout.addView(students);
        mainLayout.addView(branches);
        mainLayout.addView(grades);
        mainLayout.addView(print);

        students.setOnClickListener(v -> showStudents());
        branches.setOnClickListener(v -> showBranches());
        grades.setOnClickListener(v -> showGrades());
        print.setOnClickListener(v -> showPrint());

        setContentView(mainLayout);
    }

    // =========================================================
    // إدارة الطلاب
    // =========================================================

    private void showStudents() {

        mainLayout = createMainLayout();

        addBackButton();

        TextView title = text("إدارة الطلاب", 28, true);
        mainLayout.addView(title);

        Button add = button("➕ إضافة طالب");
        mainLayout.addView(add);

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        mainLayout.addView(list);

        loadStudents(list);

        add.setOnClickListener(v -> addStudentDialog(list));

        setContentView(mainLayout);
    }

    private void loadStudents(LinearLayout list) {

        list.removeAllViews();

        Set<String> students =
                prefs.getStringSet(STUDENTS, new HashSet<>());

        ArrayList<String> names = new ArrayList<>(students);
        Collections.sort(names);

        if (names.isEmpty()) {

            TextView empty = text(
                    "لا يوجد طلاب حاليًا",
                    20,
                    false
            );

            list.addView(empty);
            return;
        }

        for (String name : names) {

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(20, 15, 20, 15);

            TextView student = new TextView(this);
            student.setText(name);
            student.setTextSize(20);
            student.setGravity(Gravity.RIGHT);
            student.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            Button delete = new Button(this);
            delete.setText("حذف");

            row.addView(student,
                    new LinearLayout.LayoutParams(
                            0,
                            -2,
                            1
                    ));

            row.addView(delete);

            list.addView(row);

            delete.setOnClickListener(v -> {

                new AlertDialog.Builder(this)
                        .setTitle("حذف الطالب")
                        .setMessage("هل تريد حذف الطالب " + name + "؟")
                        .setPositiveButton("نعم", (d, w) -> {

                            Set<String> current =
                                    new HashSet<>(
                                            prefs.getStringSet(
                                                    STUDENTS,
                                                    new HashSet<>()
                                            )
                                    );

                            current.remove(name);

                            prefs.edit()
                                    .putStringSet(STUDENTS, current)
                                    .apply();

                            loadStudents(list);
                        })
                        .setNegativeButton("إلغاء", null)
                        .show();
            });
        }
    }

    private void addStudentDialog(LinearLayout list) {

        EditText input = new EditText(this);
        input.setHint("اسم الطالب");
        input.setTextSize(20);
        input.setGravity(Gravity.RIGHT);

        new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(input)
                .setPositiveButton("إضافة", (dialog, which) -> {

                    String name = input.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(
                                this,
                                "اكتب اسم الطالب أولاً",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    Set<String> students =
                            new HashSet<>(
                                    prefs.getStringSet(
                                            STUDENTS,
                                            new HashSet<>()
                                    )
                            );

                    students.add(name);

                    prefs.edit()
                            .putStringSet(STUDENTS, students)
                            .apply();

                    loadStudents(list);

                    Toast.makeText(
                            this,
                            "تمت إضافة الطالب",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    // =========================================================
    // الشعب الدراسية
    // =========================================================

    private void showBranches() {

        mainLayout = createMainLayout();

        addBackButton();

        mainLayout.addView(
                text("الشعب الدراسية", 28, true)
        );

        Button add = button("➕ إضافة شعبة");
        mainLayout.addView(add);

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        mainLayout.addView(list);

        loadBranches(list);

        add.setOnClickListener(v -> addBranchDialog(list));

        setContentView(mainLayout);
    }

    private void loadBranches(LinearLayout list) {

        list.removeAllViews();

        Set<String> branches =
                prefs.getStringSet(
                        BRANCHES,
                        new HashSet<>()
                );

        ArrayList<String> data =
                new ArrayList<>(branches);

        Collections.sort(data);

        if (data.isEmpty()) {

            list.addView(
                    text(
                            "لا توجد شعب مضافة",
                            20,
                            false
                    )
            );

            return;
        }

        for (String branch : data) {

            LinearLayout row = new LinearLayout(this);

            row.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            row.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            TextView name = text(
                    branch,
                    20,
                    false
            );

            Button delete = new Button(this);
            delete.setText("حذف");

            row.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            0,
                            -2,
                            1
                    )
            );

            row.addView(delete);

            list.addView(row);

            delete.setOnClickListener(v -> {

                Set<String> current =
                        new HashSet<>(
                                prefs.getStringSet(
                                        BRANCHES,
                                        new HashSet<>()
                                )
                        );

                current.remove(branch);

                prefs.edit()
                        .putStringSet(
                                BRANCHES,
                                current
                        )
                        .apply();

                loadBranches(list);
            });
        }
    }

    private void addBranchDialog(LinearLayout list) {

        EditText input = new EditText(this);

        input.setHint("مثال: الثالث أ");
        input.setTextSize(20);
        input.setGravity(Gravity.RIGHT);

        new AlertDialog.Builder(this)
                .setTitle("إضافة شعبة")
                .setView(input)
                .setPositiveButton(
                        "إضافة",
                        (dialog, which) -> {

                            String branch =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (branch.isEmpty()) {
                                return;
                            }

                            Set<String> branches =
                                    new HashSet<>(
                                            prefs.getStringSet(
                                                    BRANCHES,
                                                    new HashSet<>()
                                            )
                                    );

                            branches.add(branch);

                            prefs.edit()
                                    .putStringSet(
                                            BRANCHES,
                                            branches
                                    )
                                    .apply();

                            loadBranches(list);
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    // =========================================================
    // إدخال الدرجات
    // =========================================================

    private void showGrades() {

        mainLayout = createMainLayout();

        addBackButton();

        mainLayout.addView(
                text(
                        "إدخال الدرجات",
                        28,
                        true
                )
        );

        Set<String> students =
                prefs.getStringSet(
                        STUDENTS,
                        new HashSet<>()
                );

        if (students.isEmpty()) {

            mainLayout.addView(
                    text(
                            "أضف الطلاب أولاً من إدارة الطلاب",
                            20,
                            false
                    )
            );

            setContentView(mainLayout);
            return;
        }

        ArrayList<String> names =
                new ArrayList<>(students);

        Collections.sort(names);

        for (String name : names) {

            LinearLayout row =
                    new LinearLayout(this);

            row.setOrientation(
                    LinearLayout.HORIZONTAL
            );

            row.setGravity(
                    Gravity.CENTER_VERTICAL
            );

            TextView student =
                    text(name, 18, false);

            EditText grade =
                    new EditText(this);

            grade.setHint("الدرجة");
            grade.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER |
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            grade.setTextSize(18);

            String saved =
                    prefs.getString(
                            "grade_" + name,
                            ""
                    );

            grade.setText(saved);

            Button save =
                    new Button(this);

            save.setText("حفظ");

            row.addView(
                    student,
                    new LinearLayout.LayoutParams(
                            0,
                            -2,
                            1
                    )
            );

            row.addView(
                    grade,
                    new LinearLayout.LayoutParams(
                            150,
                            -2
                    )
            );

            row.addView(save);

            mainLayout.addView(row);

            save.setOnClickListener(v -> {

                String value =
                        grade.getText()
                                .toString()
                                .trim();

                if (value.isEmpty()) {

                    Toast.makeText(
                            this,
                            "أدخل الدرجة",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                try {

                    double number =
                            Double.parseDouble(value);

                    if (number < 0 || number > 100) {

                        Toast.makeText(
                                this,
                                "الدرجة يجب أن تكون بين 0 و100",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    prefs.edit()
                            .putString(
                                    "grade_" + name,
                                    value
                            )
                            .apply();

                    Toast.makeText(
                            this,
                            "تم حفظ درجة " + name,
                            Toast.LENGTH_SHORT
                    ).show();

                } catch (Exception e) {

                    Toast.makeText(
                            this,
                            "الدرجة غير صحيحة",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }

        setContentView(mainLayout);
    }

    // =========================================================
    // طباعة السجلات / عرض السجل
    // =========================================================

    private void showPrint() {

        mainLayout = createMainLayout();

        addBackButton();

        mainLayout.addView(
                text(
                        "السجلات",
                        28,
                        true
                )
        );

        Set<String> students =
                prefs.getStringSet(
                        STUDENTS,
                        new HashSet<>()
                );

        if (students.isEmpty()) {

            mainLayout.addView(
                    text(
                            "لا توجد بيانات لعرضها",
                            20,
                            false
                    )
            );

            setContentView(mainLayout);
            return;
        }

        ArrayList<String> names =
                new ArrayList<>(students);

        Collections.sort(names);

        double total = 0;
        int count = 0;

        for (String name : names) {

            String grade =
                    prefs.getString(
                            "grade_" + name,
                            "لم تدخل"
                    );

            TextView record =
                    text(
                            name + "   —   " + grade,
                            19,
                            false
                    );

            record.setPadding(
                    15,
                    15,
                    15,
                    15
            );

            mainLayout.addView(record);

            if (!grade.equals("لم تدخل")) {

                try {
                    total +=
                            Double.parseDouble(grade);
                    count++;
                } catch (Exception ignored) {
                }
            }
        }

        if (count > 0) {

            double average =
                    total / count;

            mainLayout.addView(
                    text(
                            "المعدل: " +
                                    String.format(
                                            "%.2f",
                                            average
                                    ),
                            22,
                            true
                    )
            );
        }

        Button clear =
                button("مسح جميع الدرجات");

        mainLayout.addView(clear);

        clear.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("مسح الدرجات")
                    .setMessage(
                            "هل تريد مسح جميع الدرجات؟"
                    )
                    .setPositiveButton(
                            "نعم",
                            (d, w) -> {

                                for (String name : names) {

                                    prefs.edit()
                                            .remove(
                                                    "grade_" + name
                                            )
                                            .apply();
                                }

                                showPrint();
                            }
                    )
                    .setNegativeButton(
                            "إلغاء",
                            null
                    )
                    .show();
        });

        setContentView(mainLayout);
    }

    // =========================================================
    // أدوات الواجهة
    // =========================================================

    private LinearLayout createMainLayout() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        layout.setPadding(
                30,
                30,
                30,
                30
        );

        layout.setBackgroundColor(
                android.graphics.Color.rgb(
     

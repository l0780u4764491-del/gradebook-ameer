package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends Activity {

    private SharedPreferences prefs;

    private JSONArray students = new JSONArray();
    private JSONArray classes = new JSONArray();

    private LinearLayout root;

    private final int BLUE = Color.rgb(24,72,130);
    private final int DARK = Color.rgb(10,38,72);
    private final int BG = Color.rgb(246,248,251);
    private final int RED = Color.rgb(190,45,45);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(
                "gradebook",
                Context.MODE_PRIVATE
        );

        loadData();
        showHome();
    }

    private void loadData() {

        try {

            students = new JSONArray(
                    prefs.getString(
                            "students",
                            "[]"
                    )
            );

            classes = new JSONArray(
                    prefs.getString(
                            "classes",
                            "[]"
                    )
            );

        } catch (Exception e) {

            students = new JSONArray();
            classes = new JSONArray();
        }
    }

    private void saveData() {

        prefs.edit()
                .putString(
                        "students",
                        students.toString()
                )
                .putString(
                        "classes",
                        classes.toString()
                )
                .apply();
    }

    private void setup() {

        ScrollView scroll =
                new ScrollView(this);

        root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                20,
                20,
                20,
                35
        );

        root.setBackgroundColor(BG);

        scroll.addView(root);

        setContentView(scroll);
    }

    private TextView txt(
            String value,
            int size,
            boolean bold) {

        TextView t =
                new TextView(this);

        t.setText(value);

        t.setTextSize(size);

        t.setTextColor(
                Color.DKGRAY
        );

        t.setPadding(
                8,
                10,
                8,
                10
        );

        if (bold) {

            t.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        return t;
    }

    private void title(String value) {

        TextView t =
                txt(
                        value,
                        22,
                        true
                );

        t.setTextColor(
                Color.WHITE
        );

        t.setGravity(
                Gravity.CENTER
        );

        t.setPadding(
                10,
                28,
                10,
                28
        );

        t.setBackgroundColor(
                DARK
        );

        root.addView(t);
    }

    private Button button(String value) {

        Button b =
                new Button(this);

        b.setText(value);

        b.setTextSize(16);

        b.setAllCaps(false);

        b.setTextColor(
                Color.WHITE
        );

        b.setBackgroundColor(
                BLUE
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        58
                );

        params.setMargins(
                0,
                6,
                0,
                6
        );

        root.addView(
                b,
                params
        );

        return b;
    }

    private EditText input(
            String hint) {

        EditText e =
                new EditText(this);

        e.setHint(hint);

        e.setTextSize(16);

        e.setPadding(
                12,
                5,
                12,
                5
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        60
                );

        params.setMargins(
                0,
                5,
                0,
                5
        );

        e.setLayoutParams(params);

        return e;
    }

    private EditText gradeInput(
            String hint) {

        EditText e =
                input(hint);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        return e;
    }

    private LinearLayout card() {

        LinearLayout c =
                new LinearLayout(this);

        c.setOrientation(
                LinearLayout.VERTICAL
        );

        c.setPadding(
                16,
                12,
                16,
                12
        );

        c.setBackgroundColor(
                Color.WHITE
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        params.setMargins(
                0,
                6,
                0,
                6
        );

        c.setLayoutParams(params);

        return c;
    }

    private void addText(
            LinearLayout card,
            String value,
            int size,
            boolean bold) {

        card.addView(
                txt(
                        value,
                        size,
                        bold
                )
        );
    }

    private void toast(
            String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private ArrayList<String> classList() {

        ArrayList<String> list =
                new ArrayList<>();

        for (int i = 0;
             i < classes.length();
             i++) {

            try {

                list.add(
                        classes.getString(i)
                );

            } catch (Exception e) {
                // تجاهل
            }
        }

        if (list.size() == 0) {

            list.add(
                    "الصف الأول"
            );
        }

        return list;
    }

    private Spinner spinner() {

        Spinner s =
                new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_item,
                        classList()
                );

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        s.setAdapter(adapter);

        return s;
    }

    private void showHome() {

        setup();

        title(
                "سجل درجات الاجتماعيات"
        );

        TextView info =
                txt(
                        "الأستاذ أمير محمد\n" +
                        "نظام إدارة درجات الطلاب",
                        18,
                        true
                );

        info.setGravity(
                Gravity.CENTER
        );

        root.addView(info);

        Button studentsButton =
                button(
                        "👨‍🎓 إدارة الطلاب"
                );

        studentsButton.setOnClickListener(
                v -> showStudents()
        );

        Button classesButton =
                button(
                        "🏫 إدارة الصفوف"
                );

        classesButton.setOnClickListener(
                v -> showClasses()
        );

        Button gradesButton =
                button(
                        "📝 إدخال الدرجات"
                );

        gradesButton.setOnClickListener(
                v -> showGrades()
        );

        Button recordsButton =
                button(
                        "📋 سجل الدرجات"
                );

        recordsButton.setOnClickListener(
                v -> showRecords()
        );

        Button rankingButton =
                button(
                        "🏆 ترتيب الطلاب"
                );

        rankingButton.setOnClickListener(
                v -> showRanking()
        );

        Button reportsButton =
                button(
                        "📊 التقارير"
                );

        reportsButton.setOnClickListener(
                v -> showReports()
        );

        Button backupButton =
                button(
                        "💾 النسخ الاحتياطي"
                );

        backupButton.setOnClickListener(
                v -> showBackup()
        );

        Button settingsButton =
                button(
                        "⚙️ الإعدادات"
                );

        settingsButton.setOnClickListener(
                v -> showSettings()
        );

        root.addView(
                txt(
                        "عدد الطلاب: " +
                        students.length() +
                        "\nعدد الصفوف: " +
                        classes.length(),
                        16,
                        true
                )
        );
    }

    // ===== نهاية القسم 1 =====
    // ===== القسم 2: إدارة الطلاب =====

    private void showStudents() {

        setup();

        title("إدارة الطلاب");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        Button add =
                button("＋ إضافة طالب");

        add.setOnClickListener(
                v -> addStudent()
        );

        if (students.length() == 0) {

            root.addView(
                    txt(
                            "لا يوجد طلاب.",
                            17,
                            false
                    )
            );

            return;
        }

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject student =
                        students.getJSONObject(i);

                final int index = i;

                LinearLayout card =
                        card();

                addText(
                        card,
                        student.optString("name"),
                        19,
                        true
                );

                addText(
                        card,
                        "المدرسة: " +
                        student.optString("school"),
                        15,
                        false
                );

                addText(
                        card,
                        "الصف: " +
                        student.optString("className"),
                        15,
                        false
                );

                Button edit =
                        new Button(this);

                edit.setText("✏️ تعديل");

                edit.setOnClickListener(
                        v -> editStudent(index)
                );

                Button delete =
                        new Button(this);

                delete.setText("🗑️ حذف");

                delete.setTextColor(RED);

                delete.setOnClickListener(
                        v -> deleteStudent(index)
                );

                card.addView(edit);
                card.addView(delete);

                root.addView(card);

            } catch (Exception e) {
                // تجاهل الطالب غير الصالح
            }
        }
    }

    private void addStudent() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                25,
                5,
                25,
                5
        );

        EditText name =
                input("اسم الطالب");

        EditText school =
                input("اسم المدرسة");

        Spinner spinner =
                spinner();

        box.addView(name);
        box.addView(school);
        box.addView(spinner);

        new AlertDialog.Builder(this)

                .setTitle("إضافة طالب")

                .setView(box)

                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            try {

                                String studentName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                if (studentName.isEmpty()) {

                                    toast(
                                            "أدخل اسم الطالب"
                                    );

                                    return;
                                }

                                JSONObject student =
                                        new JSONObject();

                                student.put(
                                        "name",
                                        studentName
                                );

                                student.put(
                                        "school",
                                        school.getText()
                                                .toString()
                                                .trim()
                                );

                                student.put(
                                        "className",
                                        spinner
                                                .getSelectedItem()
                                                .toString()
                                );

                                student.put(
                                        "monthly",
                                        0
                                );

                                student.put(
                                        "mid",
                                        0
                                );

                                student.put(
                                        "second",
                                        0
                                );

                                student.put(
                                        "final",
                                        0
                                );

                                students.put(
                                        student
                                );

                                saveData();

                                showStudents();

                                toast(
                                        "تمت إضافة الطالب"
                                );

                            } catch (Exception e) {

                                toast(
                                        "حدث خطأ أثناء إضافة الطالب"
                                );
                            }
                        }
                )

                .setNegativeButton(
                        "إلغاء",
                        null
                )

                .show();
    }

    private void editStudent(
            final int index) {

        try {

            JSONObject student =
                    students.getJSONObject(index);

            LinearLayout box =
                    new LinearLayout(this);

            box.setOrientation(
                    LinearLayout.VERTICAL
            );

            box.setPadding(
                    25,
                    5,
                    25,
                    5
            );

            EditText name =
                    input("اسم الطالب");

            name.setText(
                    student.optString("name")
            );

            EditText school =
                    input("اسم المدرسة");

            school.setText(
                    student.optString("school")
            );

            Spinner spinner =
                    spinner();

            for (int i = 0;
                 i < spinner.getCount();
                 i++) {

                if (
                        spinner
                                .getItemAtPosition(i)
                                .toString()
                                .equals(
                                        student.optString(
                                                "className"
                                        )
                                )
                ) {

                    spinner.setSelection(i);

                    break;
                }
            }

            box.addView(name);
            box.addView(school);
            box.addView(spinner);

            new AlertDialog.Builder(this)

                    .setTitle("تعديل الطالب")

                    .setView(box)

                    .setPositiveButton(
                            "حفظ",
                            (dialog, which) -> {

                                try {

                                    String newName =
                                            name.getText()
                                                    .toString()
                                                    .trim();

                                    if (
                                            newName.isEmpty()
                                    ) {

                                        toast(
                                                "أدخل اسم الطالب"
                                        );

                                        return;
                                    }

                                    student.put(
                                            "name",
                                            newName
                                    );

                                    student.put(
                                            "school",
                                            school.getText()
                                                    .toString()
                                                    .trim()
                                    );

                                    student.put(
                                            "className",
                                            spinner
                                                    .getSelectedItem()
                                                    .toString()
                                    );

                                    saveData();

                                    showStudents();

                                    toast(
                                            "تم تعديل بيانات الطالب"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء التعديل"
                                    );
                                }
                            }
                    )

                    .setNegativeButton(
                            "إلغاء",
                            null
                    )

                    .show();

        } catch (Exception e) {

            toast(
                    "تعذر فتح بيانات الطالب"
            );
        }
    }

    private void deleteStudent(
            final int index) {

        new AlertDialog.Builder(this)

                .setTitle("حذف الطالب")

                .setMessage(
                        "هل تريد حذف هذا الطالب؟"
                )

                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            try {

                                JSONArray newStudents =
                                        new JSONArray();

                                for (int i = 0;
                                     i < students.length();
                                     i++) {

                                    if (i != index) {

                                        newStudents.put(
                                                students
                                                        .getJSONObject(i)
                                        );
                                    }
                                }

                                students =
                                        newStudents;

                                saveData();

                                showStudents();

                                toast(
                                        "تم حذف الطالب"
                                );

                            } catch (Exception e) {

                                toast(
                                        "حدث خطأ أثناء الحذف"
                                );
                            }
                        }
                )

                .setNegativeButton(
                        "إلغاء",
                        null
                )

                .show();
    }

    // ===== نهاية القسم 2 =====
    // ===== القسم 3: إدارة الصفوف =====

    private void showClasses() {

        setup();

        title("إدارة الصفوف");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        Button add =
                button("＋ إضافة صف");

        add.setOnClickListener(
                v -> addClass()
        );

        if (classes.length() == 0) {

            root.addView(
                    txt(
                            "لا توجد صفوف حالياً.",
                            17,
                            false
                    )
            );

            return;
        }

        for (int i = 0;
             i < classes.length();
             i++) {

            try {

                final int index = i;

                String className =
                        classes.getString(i);

                LinearLayout card =
                        card();

                addText(
                        card,
                        className,
                        19,
                        true
                );

                int count = 0;

                for (int j = 0;
                     j < students.length();
                     j++) {

                    JSONObject student =
                            students.getJSONObject(j);

                    if (
                            className.equals(
                                    student.optString(
                                            "className"
                                    )
                            )
                    ) {

                        count++;
                    }
                }

                addText(
                        card,
                        "عدد الطلاب: " + count,
                        15,
                        false
                );

                Button rename =
                        new Button(this);

                rename.setText("✏️ تعديل اسم الصف");

                rename.setOnClickListener(
                        v -> editClass(index)
                );

                Button delete =
                        new Button(this);

                delete.setText("🗑️ حذف الصف");

                delete.setTextColor(RED);

                delete.setOnClickListener(
                        v -> deleteClass(index)
                );

                card.addView(rename);
                card.addView(delete);

                root.addView(card);

            } catch (Exception e) {
                // تجاهل الصف غير الصالح
            }
        }
    }

    private void addClass() {

        final EditText name =
                input("اسم الصف");

        new AlertDialog.Builder(this)

                .setTitle("إضافة صف")

                .setView(name)

                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            String className =
                                    name.getText()
                                            .toString()
                                            .trim();

                            if (className.isEmpty()) {

                                toast(
                                        "أدخل اسم الصف"
                                );

                                return;
                            }

                            try {

                                for (int i = 0;
                                     i < classes.length();
                                     i++) {

                                    if (
                                            classes
                                                    .getString(i)
                                                    .equals(
                                                            className
                                                    )
                                    ) {

                                        toast(
                                                "هذا الصف موجود مسبقاً"
                                        );

                                        return;
                                    }
                                }

                                classes.put(
                                        className
                                );

                                saveData();

                                showClasses();

                                toast(
                                        "تمت إضافة الصف"
                                );

                            } catch (Exception e) {

                                toast(
                                        "حدث خطأ أثناء إضافة الصف"
                                );
                            }
                        }
                )

                .setNegativeButton(
                        "إلغاء",
                        null
                )

                .show();
    }

    private void editClass(
            final int index) {

        try {

            final String oldName =
                    classes.getString(index);

            final EditText name =
                    input("اسم الصف");

            name.setText(oldName);

            new AlertDialog.Builder(this)

                    .setTitle("تعديل اسم الصف")

                    .setView(name)

                    .setPositiveButton(
                            "حفظ",
                            (dialog, which) -> {

                                String newName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                if (
                                        newName.isEmpty()
                                ) {

                                    toast(
                                            "أدخل اسم الصف"
                                    );

                                    return;
                                }

                                try {

                                    if (
                                            newName.equals(
                                                    oldName
                                            )
                                    ) {

                                        return;
                                    }

                                    for (int i = 0;
                                         i < classes.length();
                                         i++) {

                                        if (
                                                i != index &&
                                                classes
                                                        .getString(i)
                                                        .equals(
                                                                newName
                                                        )
                                        ) {

                                            toast(
                                                    "هذا الصف موجود مسبقاً"
                                            );

                                            return;
                                        }
                                    }

                                    classes.put(
                                            index,
                                            newName
                                    );

                                    for (int i = 0;
                                         i < students.length();
                                         i++) {

                                        JSONObject student =
                                                students
                                                        .getJSONObject(i);

                                        if (
                                                oldName.equals(
                                                        student.optString(
                                                                "className"
                                                        )
                                                )
                                        ) {

                                            student.put(
                                                    "className",
                                                    newName
                                            );
                                        }
                                    }

                                    saveData();

                                    showClasses();

                                    toast(
                                            "تم تعديل اسم الصف"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء تعديل الصف"
                                    );
                                }
                            }
                    )

                    .setNegativeButton(
                            "إلغاء",
                            null
                    )

                    .show();

        } catch (Exception e) {

            toast(
                    "تعذر تعديل الصف"
            );
        }
    }

    private void deleteClass(
            final int index) {

        try {

            final String className =
                    classes.getString(index);

            int count = 0;

            for (int i = 0;
                 i < students.length();
                 i++) {

                JSONObject student =
                        students.getJSONObject(i);

                if (
                        className.equals(
                                student.optString(
                                        "className"
                                )
                        )
                ) {

                    count++;
                }
            }

            String message;

            if (count > 0) {

                message =
                        "هذا الصف يحتوي على " +
                        count +
                        " طالب.\n\n" +
                        "حذف الصف لن يحذف الطلاب، " +
                        "لكن ستحتاج إلى إعادة تعيين صفهم.\n\n" +
                        "هل تريد المتابعة؟";

            } else {

                message =
                        "هل تريد حذف الصف؟";
            }

            new AlertDialog.Builder(this)

                    .setTitle("حذف الصف")

                    .setMessage(message)

                    .setPositiveButton(
                            "حذف",
                            (dialog, which) -> {

                                try {

                                    JSONArray newClasses =
                                            new JSONArray();

                                    for (int i = 0;
                                         i < classes.length();
                                         i++) {

                                        if (i != index) {

                                            newClasses.put(
                                                    classes
                                                            .getString(i)
                                            );
                                        }
                                    }

                                    classes =
                                            newClasses;

                                    saveData();

                                    showClasses();

                                    toast(
                                            "تم حذف الصف"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء حذف الصف"
                                    );
                                }
                            }
                    )

                    .setNegativeButton(
                            "إلغاء",
                            null
                    )

                    .show();

        } catch (Exception e) {

            toast(
                    "تعذر حذف الصف"
            );
        }
    }

    // ===== نهاية القسم 3 =====
    // ===== القسم 4: إدخال الدرجات والسجل =====

    private void showGrades() {

        setup();

        title("إدخال الدرجات");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        if (students.length() == 0) {

            root.addView(
                    txt(
                            "أضف الطلاب أولاً من إدارة الطلاب.",
                            17,
                            false
                    )
            );

            return;
        }

        Spinner classSpinner =
                spinner();

        root.addView(classSpinner);

        Button open =
                button("فتح طلاب الصف");

        open.setOnClickListener(
                v -> showGradeStudents(
                        classSpinner
                                .getSelectedItem()
                                .toString()
                )
        );
    }

    private void showGradeStudents(
            String className) {

        setup();

        title(
                "درجات " + className
        );

        Button back =
                button("← إدخال الدرجات");

        back.setOnClickListener(
                v -> showGrades()
        );

        boolean found = false;

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject student =
                        students.getJSONObject(i);

                if (
                        !className.equals(
                                student.optString(
                                        "className"
                                )
                        )
                ) {
                    continue;
                }

                found = true;

                final int index = i;

                LinearLayout card =
                        card();

                addText(
                        card,
                        student.optString("name"),
                        19,
                        true
                );

                addText(
                        card,
                        "الصف: " +
                        student.optString("className"),
                        14,
                        false
                );

                Button grades =
                        new Button(this);

                grades.setText(
                        "📝 إدخال / تعديل الدرجات"
                );

                grades.setOnClickListener(
                        v -> editGrades(index)
                );

                card.addView(grades);

                root.addView(card);

            } catch (Exception e) {
                // تجاهل
            }
        }

        if (!found) {

            root.addView(
                    txt(
                            "لا يوجد طلاب في هذا الصف.",
                            17,
                            false
                    )
            );
        }
    }

    private void editGrades(
            final int index) {

        try {

            JSONObject student =
                    students.getJSONObject(index);

            LinearLayout box =
                    new LinearLayout(this);

            box.setOrientation(
                    LinearLayout.VERTICAL
            );

            box.setPadding(
                    20,
                    5,
                    20,
                    5
            );

            addText(
                    box,
                    student.optString("name"),
                    19,
                    true
            );

            EditText monthly =
                    gradeInput(
                            "أعمال الشهر"
                    );

            monthly.setText(
                    String.valueOf(
                            student.optDouble(
                                    "monthly",
                                    0
                            )
                    )
            );

            EditText mid =
                    gradeInput(
                            "نصف السنة"
                    );

            mid.setText(
                    String.valueOf(
                            student.optDouble(
                                    "mid",
                                    0
                            )
                    )
            );

            EditText second =
                    gradeInput(
                            "النصف الثاني"
                    );

            second.setText(
                    String.valueOf(
                            student.optDouble(
                                    "second",
                                    0
                            )
                    )
            );

            EditText finalGrade =
                    gradeInput(
                            "الدرجة النهائية"
                    );

            finalGrade.setText(
                    String.valueOf(
                            student.optDouble(
                                    "final",
                                    0
                            )
                    )
            );

            box.addView(monthly);
            box.addView(mid);
            box.addView(second);
            box.addView(finalGrade);

            new AlertDialog.Builder(this)

                    .setTitle("درجات الطالب")

                    .setView(box)

                    .setPositiveButton(
                            "حفظ",
                            (dialog, which) -> {

                                try {

                                    double m =
                                            readGrade(
                                                    monthly
                                            );

                                    double mi =
                                            readGrade(
                                                    mid
                                            );

                                    double s =
                                            readGrade(
                                                    second
                                            );

                                    double f =
                                            readGrade(
                                                    finalGrade
                                            );

                                    if (
                                            m < 0 ||
                                            m > 100 ||
                                            mi < 0 ||
                                            mi > 100 ||
                                            s < 0 ||
                                            s > 100 ||
                                            f < 0 ||
                                            f > 100
                                    ) {

                                        toast(
                                                "الدرجات يجب أن تكون بين 0 و100"
                                        );

                                        return;
                                    }

                                    student.put(
                                            "monthly",
                                            m
                                    );

                                    student.put(
                                            "mid",
                                            mi
                                    );

                                    student.put(
                                            "second",
                                            s
                                    );

                                    student.put(
                                            "final",
                                            f
                                    );

                                    saveData();

                                    toast(
                                            "تم حفظ الدرجات"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء حفظ الدرجات"
                                    );
                                }
                            }
                    )

                    .setNegativeButton(
                            "إلغاء",
                            null
                    )

                    .show();

        } catch (Exception e) {

            toast(
                    "تعذر فتح درجات الطالب"
            );
        }
    }

    private double readGrade(
            EditText field) {

        String value =
                field.getText()
                        .toString()
                        .trim();

        if (value.isEmpty()) {
            return 0;
        }

        return Double.parseDouble(value);
    }

    private void showRecords() {

        setup();

        title("سجل الدرجات");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        if (students.length() == 0) {

            root.addView(
                    txt(
                            "لا يوجد طلاب.",
                            17,
                            false
                    )
            );

            return;
        }

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject student =
                        students.getJSONObject(i);

                LinearLayout card =
                        card();

                String name =
                        student.optString(
                                "name"
                        );

                String className =
                        student.optString(
                                "className"
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

                double average =
                        (
                                monthly +
                                mid +
                                second +
                                finalGrade
                        ) / 4.0;

                addText(
                        card,
                        name,
                        19,
                        true
                );

                addText(
                        card,
                        "الصف: " + className,
                        15,
                        false
                );

                addText(
                        card,
                        "أعمال الشهر: " +
                        formatGrade(monthly),
                        15,
                        false
                );

                addText(
                        card,
                        "نصف السنة: " +
                        formatGrade(mid),
                        15,
                        false
                );

                addText(
                        card,
                        "النصف الثاني: " +
                        formatGrade(second),
                        15,
                        false
                );

                addText(
                        card,
                        "النهائي: " +
                        formatGrade(finalGrade),
                        15,
                        false
                );

                addText(
                        card,
                        "المعدل: " +
                        formatGrade(average),
                        17,
                        true
                );

                root.addView(card);

            } catch (Exception e) {
                // تجاهل
            }
        }
    }

    private String formatGrade(
            double value) {

        if (
                value == Math.floor(value)
        ) {

            return String.valueOf(
                    (int) value
            );
        }

        return String.format(
                Locale.US,
                "%.2f",
                value
        );
    }

    // ===== نهاية القسم 4 =====
    // ===== القسم 5 والأخير: الترتيب والتقارير والنسخ الاحتياطي =====

    private void showRanking() {

        setup();

        title("🏆 ترتيب الطلاب");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        ArrayList<JSONObject> list =
                new ArrayList<>();

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                list.add(
                        students.getJSONObject(i)
                );

            } catch (Exception e) {
                // تجاهل
            }
        }

        Collections.sort(
                list,
                new Comparator<JSONObject>() {

                    @Override
                    public int compare(
                            JSONObject a,
                            JSONObject b) {

                        double av =
                                averageOf(a);

                        double bv =
                                averageOf(b);

                        return Double.compare(
                                bv,
                                av
                        );
                    }
                }
        );

        if (list.size() == 0) {

            root.addView(
                    txt(
                            "لا يوجد طلاب لترتيبهم.",
                            17,
                            false
                    )
            );

            return;
        }

        for (int i = 0;
             i < list.size();
             i++) {

            JSONObject student =
                    list.get(i);

            LinearLayout card =
                    card();

            addText(
                    card,
                    "المركز " + (i + 1),
                    18,
                    true
            );

            addText(
                    card,
                    student.optString("name"),
                    19,
                    true
            );

            addText(
                    card,
                    "الصف: " +
                    student.optString(
                            "className"
                    ),
                    15,
                    false
            );

            addText(
                    card,
                    "المعدل: " +
                    formatGrade(
                            averageOf(student)
                    ),
                    17,
                    true
            );

            root.addView(card);
        }
    }

    private double averageOf(
            JSONObject student) {

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

        return (
                monthly +
                mid +
                second +
                finalGrade
        ) / 4.0;
    }

    private void showReports() {

        setup();

        title("📊 التقارير");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        int totalStudents =
                students.length();

        int totalClasses =
                classes.length();

        double totalAverage = 0;

        int passed = 0;

        int failed = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject student =
                        students.getJSONObject(i);

                double average =
                        averageOf(student);

                totalAverage += average;

                if (average >= 50) {
                    passed++;
                } else {
                    failed++;
                }

            } catch (Exception e) {
                // تجاهل
            }
        }

        double generalAverage = 0;

        if (totalStudents > 0) {

            generalAverage =
                    totalAverage /
                    totalStudents;
        }

        LinearLayout summary =
                card();

        addText(
                summary,
                "ملخص عام",
                20,
                true
        );

        addText(
                summary,
                "عدد الصفوف: " +
                totalClasses,
                16,
                false
        );

        addText(
                summary,
                "عدد الطلاب: " +
                totalStudents,
                16,
                false
        );

        addText(
                summary,
                "عدد الناجحين: " +
                passed,
                16,
                false
        );

        addText(
                summary,
                "عدد الراسبين: " +
                failed,
                16,
                false
        );

        addText(
                summary,
                "المعدل العام: " +
                formatGrade(
                        generalAverage
                ),
                18,
                true
        );

        root.addView(summary);

        for (int i = 0;
             i < classes.length();
             i++) {

            try {

                String className =
                        classes.getString(i);

                int count = 0;

                double total = 0;

                for (int j = 0;
                     j < students.length();
                     j++) {

                    JSONObject student =
                            students.getJSONObject(j);

                    if (
                            className.equals(
                                    student.optString(
                                            "className"
                                    )
                            )
                    ) {

                        count++;

                        total +=
                                averageOf(
                                        student
                                );
                    }
                }

                double avg = 0;

                if (count > 0) {

                    avg =
                            total / count;
                }

                LinearLayout classCard =
                        card();

                addText(
                        classCard,
                        className,
                        19,
                        true
                );

                addText(
                        classCard,
                        "عدد الطلاب: " +
                        count,
                        15,
                        false
                );

                addText(
                        classCard,
                        "معدل الصف: " +
                        formatGrade(avg),
                        17,
                        true
                );

                root.addView(
                        classCard
                );

            } catch (Exception e) {
                // تجاهل
            }
        }
    }

    private void showBackup() {

        setup();

        title("💾 النسخ الاحتياطي");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        addTextToRoot(
                "يمكنك حفظ نسخة احتياطية من بيانات الطلاب والصفوف داخل التطبيق.",
                16,
                false
        );

        Button export =
                button(
                        "📦 إنشاء نسخة احتياطية"
                );

        export.setOnClickListener(
                v -> exportBackup()
        );

        Button clear =
                button(
                        "🗑️ حذف جميع البيانات"
                );

        clear.setTextColor(RED);

        clear.setOnClickListener(
                v -> clearAllData()
        );
    }

    private void addTextToRoot(
            String value,
            int size,
            boolean bold) {

        root.addView(
                txt(
                        value,
                        size,
                        bold
                )
        );
    }

    private void exportBackup() {

        try {

            JSONObject backup =
                    new JSONObject();

            backup.put(
                    "students",
                    students
            );

            backup.put(
                    "classes",
                    classes
            );

            String data =
                    backup.toString(2);

            new AlertDialog.Builder(this)

                    .setTitle(
                            "النسخة الاحتياطية"
                    )

                    .setMessage(data)

                    .setPositiveButton(
                            "حسناً",
                            null
                    )

                    .show();

        } catch (Exception e) {

            toast(
                    "تعذر إنشاء النسخة الاحتياطية"
            );
        }
    }

    private void clearAllData() {

        new AlertDialog.Builder(this)

                .setTitle(
                        "حذف جميع البيانات"
                )

                .setMessage(
                        "تحذير!\n\n" +
                        "سيتم حذف جميع الطلاب " +
                        "والصفوف والدرجات من التطبيق.\n\n" +
                        "هل أنت متأكد؟"
                )

                .setPositiveButton(
                        "حذف الكل",
                        (dialog, which) -> {

                            students =
                                    new JSONArray();

                            classes =
                                    new JSONArray();

                            saveData();

                            showHome();

                            toast(
                                    "تم حذف جميع البيانات"
                            );
                        }
                )

                .setNegativeButton(
                        "إلغاء",
                        null
                )

                .show();
    }

    private void showSettings() {

        setup();

        title("⚙️ الإعدادات");

        Button back =
                button("← الرئيسية");

        back.setOnClickListener(
                v -> showHome()
        );

        LinearLayout card =
                card();

        addText(
                card,
                "سجل درجات الاجتماعيات",
                20,
                true
        );

        addText(
                card,
                "الأستاذ أمير محمد",
                17,
                false
        );

        addText(
                card,
                "الإصدار 1.0",
                15,
                false
        );

        root.addView(card);

        Button reset =
                button(
                        "♻️ إعادة ضبط البيانات"
                );

        reset.setOnClickListener(
                v -> clearAllData()
        );

        Button about =
                button(
                        "ℹ️ حول التطبيق"
                );

        about.setOnClickListener(
                v -> showAbout()
        );
    }

    private void showAbout() {

        new AlertDialog.Builder(this)

                .setTitle(
                        "حول التطبيق"
                )

                .setMessage(
                        "سجل درجات الاجتماعيات\n\n" +
                        "الأستاذ أمير محمد\n\n" +
                        "تطبيق لإدارة الطلاب " +
                        "والصفوف والدرجات والتقارير."
                )

                .setPositiveButton(
                        "حسناً",
                        null
                )

                .show();
    }

    @Override
    public void onBackPressed() {

        showHome();
    }

    // ===== نهاية MainActivity =====
            }

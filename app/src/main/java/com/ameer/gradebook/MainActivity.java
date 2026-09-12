package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String PREFS = "gradebook_data";
    private static final String KEY_STUDENTS = "students";
    private static final String KEY_CLASSES = "classes";

    private static final int BLUE = Color.rgb(25, 118, 210);
    private static final int DARK_BLUE = Color.rgb(13, 71, 161);
    private static final int LIGHT = Color.rgb(245, 247, 250);
    private static final int WHITE = Color.WHITE;
    private static final int DARK = Color.rgb(35, 40, 45);
    private static final int GRAY = Color.rgb(100, 110, 120);
    private static final int GREEN = Color.rgb(46, 125, 50);
    private static final int RED = Color.rgb(198, 40, 40);
    private static final int ORANGE = Color.rgb(239, 108, 0);

    private SharedPreferences prefs;

    private JSONArray students = new JSONArray();
    private JSONArray classes = new JSONArray();

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(DARK_BLUE);
        getWindow().setNavigationBarColor(Color.BLACK);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        loadData();
        showHome();
    }

    private void loadData() {
        try {
            String studentsText =
                    prefs.getString(KEY_STUDENTS, "[]");

            String classesText =
                    prefs.getString(KEY_CLASSES, "[]");

            students = new JSONArray(studentsText);
            classes = new JSONArray(classesText);

        } catch (Exception e) {
            students = new JSONArray();
            classes = new JSONArray();
        }
    }

    private void saveData() {
        prefs.edit()
                .putString(
                        KEY_STUDENTS,
                        students.toString()
                )
                .putString(
                        KEY_CLASSES,
                        classes.toString()
                )
                .apply();
    }

    private int dp(int value) {
        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    private GradientDrawable rounded(
            int color,
            float radius
    ) {
        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);
        drawable.setCornerRadius(dp((int) radius));

        return drawable;
    }

    private GradientDrawable bordered(
            int background,
            int border,
            float radius
    ) {
        GradientDrawable drawable =
                rounded(background, radius);

        drawable.setStroke(
                dp(1),
                border
        );

        return drawable;
    }

    private TextView text(
            String value,
            int size,
            int color
    ) {
        TextView view =
                new TextView(this);

        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);

        view.setGravity(
                Gravity.RIGHT | Gravity.CENTER_VERTICAL
        );

        return view;
    }

    private TextView title(String value) {
        TextView view =
                text(
                        value,
                        23,
                        DARK
                );

        view.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        view.setPadding(
                dp(4),
                dp(8),
                dp(4),
                dp(8)
        );

        return view;
    }

    private Button button(
            String value,
            int color
    ) {
        Button view =
                new Button(this);

        view.setText(value);
        view.setTextSize(15);
        view.setTextColor(WHITE);
        view.setAllCaps(false);
        view.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        view.setGravity(Gravity.CENTER);

        view.setBackground(
                rounded(color, 14)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        params.setMargins(
                0,
                dp(4),
                0,
                dp(4)
        );

        view.setLayoutParams(params);

        return view;
    }

    private Button smallButton(
            String value,
            int color
    ) {
        Button view =
                new Button(this);

        view.setText(value);
        view.setTextSize(13);
        view.setTextColor(WHITE);
        view.setAllCaps(false);

        view.setBackground(
                rounded(color, 12)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        dp(95),
                        dp(45)
                );

        view.setLayoutParams(params);

        return view;
    }

    private EditText input(String hint) {
        EditText view =
                new EditText(this);

        view.setHint(hint);
        view.setTextSize(15);
        view.setTextColor(DARK);
        view.setHintTextColor(GRAY);

        view.setSingleLine(true);

        view.setPadding(
                dp(14),
                dp(4),
                dp(14),
                dp(4)
        );

        view.setBackground(
                bordered(
                        WHITE,
                        Color.LTGRAY,
                        12
                )
        );

        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                )
        );

        return view;
    }

    private LinearLayout content() {
        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(24)
        );

        return layout;
    }

    private LinearLayout horizontal() {
        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.HORIZONTAL
        );

        layout.setGravity(
                Gravity.CENTER_VERTICAL
        );

        return layout;
    }

    private ScrollView scroll(
            View child
    ) {
        ScrollView view =
                new ScrollView(this);

        view.setFillViewport(true);
        view.addView(child);

        return view;
    }

    private void prepareRoot() {
        root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(LIGHT);

        setContentView(
                scroll(root)
        );
    }

    private TextView space(int height) {
        TextView view =
                new TextView(this);

        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );

        return view;
    }

    private View spaceHorizontal(int width) {
        View view = new View(this);

        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        dp(width),
                        1
                )
        );

        return view;
    }

    private TextView section(String value) {
        TextView view =
                text(
                        value,
                        17,
                        DARK_BLUE
                );

        view.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        view.setPadding(
                dp(4),
                dp(10),
                dp(4),
                dp(6)
        );

        return view;
    }

    private LinearLayout card() {
        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                dp(14),
                dp(14),
                dp(14),
                dp(14)
        );

        layout.setBackground(
                bordered(
                        WHITE,
                        Color.rgb(
                                225,
                                228,
                                232
                        ),
                        14
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        params.setMargins(
                0,
                dp(5),
                0,
                dp(5)
        );

        layout.setLayoutParams(params);

        return layout;
    }

    private LinearLayout statCard(
            String label,
            String value,
            int color
    ) {
        LinearLayout layout = card();

        LinearLayout row =
                horizontal();

        LinearLayout info =
                new LinearLayout(this);

        info.setOrientation(
                LinearLayout.VERTICAL
        );

        TextView title =
                text(
                        label,
                        14,
                        GRAY
                );

        TextView number =
                text(
                        value,
                        25,
                        color
                );

        number.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        info.addView(title);
        info.addView(number);

        row.addView(
                info,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        layout.addView(row);

        return layout;
    }

    private int totalStudents() {
        return students.length();
    }

    private int totalClasses() {
        return classes.length();
    }

    private int passedStudents() {
        int count = 0;

        for (int i = 0; i < students.length(); i++) {
            JSONObject student =
                    students.optJSONObject(i);

            if (student != null
                    && average(student) >= 50) {
                count++;
            }
        }

        return count;
    }

    private double generalAverage() {
        if (students.length() == 0) {
            return 0;
        }

        double total = 0;

        for (int i = 0; i < students.length(); i++) {
            JSONObject student =
                    students.optJSONObject(i);

            if (student != null) {
                total += average(student);
            }
        }

        return total / students.length();
    }

    private void showHome() {
        prepareRoot();

        LinearLayout page =
                content();

        TextView appTitle =
                title(
                        "سجل درجات الاجتماعيات"
                );

        appTitle.setTextColor(
                DARK_BLUE
        );

        appTitle.setGravity(
                Gravity.CENTER
        );

        page.addView(appTitle);

        TextView teacher =
                text(
                        "الأستاذ أمير محمد",
                        16,
                        GRAY
                );

        teacher.setGravity(
                Gravity.CENTER
        );

        page.addView(teacher);

        page.addView(space(5));

        TextView subtitle =
                text(
                        "نظام إدارة درجات الطلاب",
                        14,
                        GRAY
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        page.addView(subtitle);

        page.addView(space(15));

        page.addView(
                section("لوحة المعلومات")
        );

        page.addView(
                statCard(
                        "إجمالي الطلاب",
                        String.valueOf(
                                totalStudents()
                        ),
                        BLUE
                )
        );

        page.addView(
                statCard(
                        "إجمالي الصفوف",
                        String.valueOf(
                                totalClasses()
                        ),
                        DARK_BLUE
                )
        );

        page.addView(
                statCard(
                        "الطلاب الناجحون",
                        String.valueOf(
                                passedStudents()
                        ),
                        GREEN
                )
        );

        page.addView(
                statCard(
                        "المعدل العام",
                        format(
                                generalAverage()
                        ),
                        ORANGE
                )
        );

        page.addView(space(12));

        page.addView(
                section("إدارة السجل")
        );

        Button studentsButton =
                button(
                        "👨‍🎓 إدارة الطلاب",
                        BLUE
                );

        Button classesButton =
                button(
                        "🏫 إدارة الصفوف",
                        DARK_BLUE
                );

        Button gradesButton =
                button(
                        "📝 إدخال الدرجات",
                        GREEN
                );

        Button recordsButton =
                button(
                        "📋 سجل الدرجات",
                        ORANGE
                );

        page.addView(studentsButton);
        page.addView(classesButton);
        page.addView(gradesButton);
               Button rankingButton =
                button(
                        "🏆 ترتيب الطلاب",
                        BLUE
                );

        Button reportButton =
                button(
                        "📊 التقرير العام",
                        DARK_BLUE
                );

        Button classReportButton =
                button(
                        "📚 تقارير الصفوف",
                        ORANGE
                );

        page.addView(rankingButton);
        page.addView(reportButton);
        page.addView(classReportButton);

        page.addView(space(8));

        page.addView(
                section("أدوات النظام")
        );

        Button backupButton =
                button(
                        "💾 النسخ الاحتياطي والمشاركة",
                        BLUE
                );

        Button settingsButton =
                button(
                        "⚙️ الإعدادات",
                        GRAY
                );

        page.addView(backupButton);
        page.addView(settingsButton);

        rankingButton.setOnClickListener(
                v -> showRanking()
        );

        reportButton.setOnClickListener(
                v -> showReport()
        );

        classReportButton.setOnClickListener(
                v -> showClassReport()
        );

        backupButton.setOnClickListener(
                v -> showBackup()
        );

        settingsButton.setOnClickListener(
                v -> showSettings()
        );
    }

    private void showStudents() {
        prepareRoot();

        LinearLayout page = content();

        LinearLayout header = horizontal();

        TextView heading =
                title("إدارة الطلاب");

        header.addView(
                heading,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        Button add =
                smallButton(
                        "＋ إضافة طالب",
                        GREEN
                );

        header.addView(add);

        page.addView(header);
        page.addView(space(10));

        EditText search =
                input("بحث عن اسم الطالب...");

        page.addView(search);
        page.addView(space(10));

        LinearLayout list =
                new LinearLayout(this);

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        page.addView(list);

        renderStudents(
                list,
                ""
        );

        search.addTextChangedListener(
                new TextWatcher() {

                    @Override
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
                        renderStudents(
                                list,
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );

        add.setOnClickListener(
                v -> addStudentDialog()
        );
    }

    private void renderStudents(
            LinearLayout list,
            String query
    ) {
        list.removeAllViews();

        ArrayList<JSONObject> found =
                new ArrayList<>();

        String search =
                query == null
                        ? ""
                        : query.trim()
                        .toLowerCase(
                                Locale.ROOT
                        );

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            String name =
                    student.optString(
                            "name",
                            ""
                    );

            if (search.isEmpty()
                    || name.toLowerCase(
                    Locale.ROOT
            ).contains(search)) {

                found.add(student);
            }
        }

        TextView count =
                text(
                        "عدد النتائج: "
                                + found.size(),
                        14,
                        GRAY
                );

        count.setPadding(
                dp(4),
                dp(4),
                dp(4),
                dp(8)
        );

        list.addView(count);

        if (found.isEmpty()) {

            TextView empty =
                    text(
                            "لا توجد نتائج.",
                            16,
                            GRAY
                    );

            empty.setGravity(
                    Gravity.CENTER
            );

            empty.setPadding(
                    0,
                    dp(30),
                    0,
                    dp(30)
            );

            list.addView(empty);
            return;
        }

        for (JSONObject student : found) {
            list.addView(
                    studentCard(student)
            );
        }
    }

    private View studentCard(
            JSONObject student
    ) {
        LinearLayout item = card();

        LinearLayout top =
                horizontal();

        TextView name =
                text(
                        student.optString(
                                "name",
                                "بدون اسم"
                        ),
                        18,
                        DARK
                );

        name.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        top.addView(
                name,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        Button edit =
                smallButton(
                        "تعديل",
                        BLUE
                );

        Button delete =
                smallButton(
                        "حذف",
                        RED
                );

        top.addView(edit);
        top.addView(
                spaceHorizontal(5)
        );
        top.addView(delete);

        item.addView(top);

        String school =
                student.optString(
                        "school",
                        ""
                );

        String className =
                student.optString(
                        "className",
                        ""
                );

        TextView info =
                text(
                        "المدرسة: "
                                + (
                                school.isEmpty()
                                        ? "غير محددة"
                                        : school
                        )
                                + "\nالصف: "
                                + (
                                className.isEmpty()
                                        ? "غير محدد"
                                        : className
                        ),
                        14,
                        GRAY
                );

        info.setPadding(
                0,
                dp(8),
                0,
                0
        );

        item.addView(info);

        edit.setOnClickListener(
                v -> editStudentDialog(student)
        );

        delete.setOnClickListener(
                v -> {
                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "حذف الطالب"
                            )
                            .setMessage(
                                    "هل أنت متأكد من حذف الطالب:\n"
                                            + student.optString(
                                            "name",
                                            ""
                                    )
                                            + " ؟"
                            )
                            .setNegativeButton(
                                    "إلغاء",
                                    null
                            )
                            .setPositiveButton(
                                    "حذف",
                                    (dialog, which) ->
                                            deleteStudent(
                                                    student
                                            )
                            )
                            .show();
                }
        );

        return item;
    }

    private void addStudentDialog() {
        LinearLayout form =
                new LinearLayout(this);

        form.setOrientation(
                LinearLayout.VERTICAL
        );

        form.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        EditText name =
                input("اسم الطالب");

        EditText school =
                input("اسم المدرسة");

        form.addView(name);
        form.addView(space(8));
        form.addView(school);
        form.addView(space(8));

        ArrayList<String> options =
                classOptions();

        Spinner classSpinner =
                spinner(options);

        form.addView(classSpinner);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إضافة طالب جديد"
                        )
                        .setView(form)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "إضافة",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String studentName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                String schoolName =
                                        school.getText()
                                                .toString()
                                                .trim();

                                if (studentName.isEmpty()) {
                                    name.setError(
                                            "أدخل اسم الطالب"
                                    );
                                    return;
                                }

                                try {
                                    JSONObject student =
                                            new JSONObject();

                                    student.put(
                                            "name",
                                            studentName
                                    );

                                    student.put(
                                            "school",
                                            schoolName
                                    );

                                    student.put(
                                            "className",
                                            classSpinner
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

                                    dialog.dismiss();

                                    showStudents();

                                    toast(
                                            "تمت إضافة الطالب بنجاح"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء إضافة الطالب"
                                    );
                                }
                            }
                    );
                }
        );

        dialog.show();
    }

    private void editStudentDialog(
            JSONObject student
    ) {
        LinearLayout form =
                new LinearLayout(this);

        form.setOrientation(
                LinearLayout.VERTICAL
        );

        form.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        EditText name =
                input("اسم الطالب");

        name.setText(
                student.optString(
                        "name",
                        ""
                )
        );

        EditText school =
                input("اسم المدرسة");

        school.setText(
                student.optString(
                        "school",
                        ""
                )
        );

        ArrayList<String> options =
                classOptions();

        Spinner classSpinner =
                spinner(options);

        String currentClass =
                student.optString(
                        "className",
                        ""
                );

        int selected =
                options.indexOf(
                        currentClass
                );

        if (selected >= 0) {
            classSpinner.setSelection(
                    selected
            );
        }

        form.addView(name);
        form.addView(space(8));
        form.addView(school);
        form.addView(space(8));
        form.addView(classSpinner);

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "تعديل بيانات الطالب"
                        )
                        .setView(form)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String newName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                if (newName.isEmpty()) {
                                    name.setError(
                                            "أدخل اسم الطالب"
                                    );
                                    return;
                                }

                                try {

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
                                            classSpinner
                                                    .getSelectedItem()
                                                    .toString()
                                    );

                                    saveData();

                                    dialog.dismiss();

                                    showStudents();

                                    toast(
                                            "تم حفظ التعديلات"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "تعذر حفظ التعديلات"
                                    );
                                }
                            }
                    );
                }
        );

        dialog.show();
    }

    private void deleteStudent(
            JSONObject student
    ) {
        JSONArray updated =
                new JSONArray();

        String targetName =
                student.optString(
                        "name",
                        ""
                );

        String targetSchool =
                student.optString(
                        "school",
                        ""
                );

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject item =
                    students.optJSONObject(i);

            if (item == null) {
                continue;
            }

            boolean sameName =
                    item.optString(
                            "name",
                            ""
                    ).equals(targetName);

            boolean sameSchool =
                    item.optString(
                            "school",
                            ""
                    ).equals(targetSchool);

            if (!sameName || !sameSchool) {
                updated.put(item);
            }
        }

        students = updated;

        saveData();

        showStudents();

        toast(
                "تم حذف الطالب"
        );
    }

    private ArrayList<String> classOptions() {
        ArrayList<String> result =
                new ArrayList<>();

        for (int i = 0;
             i < classes.length();
             i++) {

            String value =
                    classes.optString(
                            i,
                            ""
                    );

            if (!value.isEmpty()) {
                result.add(value);
            }
        }

        if (result.isEmpty()) {
    private void showClasses() {
        prepareRoot();

        LinearLayout page = content();

        LinearLayout header = horizontal();

        TextView heading = title("إدارة الصفوف");

        header.addView(
                heading,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        Button add =
                smallButton(
                        "＋ إضافة صف",
                        GREEN
                );

        header.addView(add);

        page.addView(header);
        page.addView(space(10));

        LinearLayout list =
                new LinearLayout(this);

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        page.addView(list);

        renderClasses(list);

        add.setOnClickListener(
                v -> addClassDialog()
        );
    }

    private void renderClasses(
            LinearLayout list
    ) {
        list.removeAllViews();

        if (classes.length() == 0) {

            list.addView(
                    text(
                            "لا توجد صفوف مضافة حالياً.",
                            16,
                            GRAY
                    )
            );

            return;
        }

        for (int i = 0;
             i < classes.length();
             i++) {

            final int index = i;

            String className =
                    classes.optString(
                            i,
                            ""
                    );

            LinearLayout item =
                    card();

            LinearLayout row =
                    horizontal();

            LinearLayout names =
                    new LinearLayout(this);

            names.setOrientation(
                    LinearLayout.VERTICAL
            );

            TextView name =
                    text(
                            className,
                            18,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            TextView studentsCount =
                    text(
                            "عدد الطلاب: "
                                    + countInClass(
                                    className
                            ),
                            14,
                            GRAY
                    );

            names.addView(name);
            names.addView(studentsCount);

            row.addView(
                    names,
                    new LinearLayout.LayoutParams(
                            0,
                            -2,
                            1
                    )
            );

            Button edit =
                    smallButton(
                            "تعديل",
                            BLUE
                    );

            Button delete =
                    smallButton(
                            "حذف",
                            RED
                    );

            row.addView(edit);
            row.addView(
                    spaceHorizontal(5)
            );
            row.addView(delete);

            item.addView(row);
            list.addView(item);

            edit.setOnClickListener(
                    v -> editClassDialog(index)
            );

            delete.setOnClickListener(
                    v -> {

                        new AlertDialog.Builder(this)
                                .setTitle(
                                        "حذف الصف"
                                )
                                .setMessage(
                                        "هل تريد حذف الصف:\n"
                                                + className
                                                + " ؟"
                                )
                                .setNegativeButton(
                                        "إلغاء",
                                        null
                                )
                                .setPositiveButton(
                                        "حذف",
                                        (dialog, which) ->
                                                deleteClass(
                                                        index
                                                )
                                )
                                .show();
                    }
            );
        }
    }

    private int countInClass(
            String className
    ) {
        int count = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student != null
                    && student.optString(
                    "className",
                    ""
            ).equals(className)) {

                count++;
            }
        }

        return count;
    }

    private void addClassDialog() {
        EditText input =
                input("اسم الصف");

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إضافة صف جديد"
                        )
                        .setView(input)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "إضافة",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String name =
                                        input.getText()
                                                .toString()
                                                .trim();

                                if (name.isEmpty()) {
                                    input.setError(
                                            "أدخل اسم الصف"
                                    );
                                    return;
                                }

                                for (int i = 0;
                                     i < classes.length();
                                     i++) {

                                    if (classes.optString(
                                            i,
                                            ""
                                    ).equals(name)) {

                                        input.setError(
                                                "هذا الصف موجود مسبقاً"
                                        );

                                        return;
                                    }
                                }

                                classes.put(name);

                                saveData();

                                dialog.dismiss();

                                showClasses();

                                toast(
                                        "تمت إضافة الصف"
                                );
                            }
                    );
                }
        );

        dialog.show();
    }

    private void editClassDialog(
            int index
    ) {
        String current =
                classes.optString(
                        index,
                        ""
                );

        EditText input =
                input("اسم الصف");

        input.setText(current);
        input.setSelection(
                input.length()
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "تعديل الصف"
                        )
                        .setView(input)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                String newName =
                                        input.getText()
                                                .toString()
                                                .trim();

                                if (newName.isEmpty()) {
                                    input.setError(
                                            "أدخل اسم الصف"
                                    );
                                    return;
                                }

                                for (int i = 0;
                                     i < classes.length();
                                     i++) {

                                    if (i != index
                                            && classes.optString(
                                            i,
                                            ""
                                    ).equals(newName)) {

                                        input.setError(
                                                "هذا الصف موجود مسبقاً"
                                        );

                                        return;
                                    }
                                }

                                String oldName =
                                        classes.optString(
                                                index,
                                                ""
                                        );

                                try {

                                    classes.put(
                                            index,
                                            newName
                                    );

                                    for (int i = 0;
                                         i < students.length();
                                         i++) {

                                        JSONObject student =
                                                students.optJSONObject(i);

                                        if (student != null
                                                && student.optString(
                                                "className",
                                                ""
                                        ).equals(oldName)) {

                                            student.put(
                                                    "className",
                                                    newName
                                            );
                                        }
                                    }

                                    saveData();

                                    dialog.dismiss();

                                    showClasses();

                                    toast(
                                            "تم تعديل الصف"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء تعديل الصف"
                                    );
                                }
                            }
                    );
                }
        );

        dialog.show();
    }

    private void deleteClass(
            int index
    ) {
        String className =
                classes.optString(
                        index,
                        ""
                );

        if (countInClass(className) > 0) {

            new AlertDialog.Builder(this)
                    .setTitle(
                            "لا يمكن حذف الصف"
                    )
                    .setMessage(
                            "يوجد طلاب مسجلون في هذا الصف.\n"
                                    + "انقل الطلاب إلى صف آخر أولاً."
                    )
                    .setPositiveButton(
                            "حسناً",
                            null
                    )
                    .show();

            return;
        }

        JSONArray updated =
                new JSONArray();

        for (int i = 0;
             i < classes.length();
             i++) {

            if (i != index) {
                updated.put(
                        classes.optString(
                                i,
                                ""
                        )
                );
            }
        }

        classes = updated;

        saveData();

        showClasses();

        toast(
                "تم حذف الصف"
        );
    }

    private void showGrades() {
        prepareRoot();

        LinearLayout page =
                content();

        LinearLayout header =
                horizontal();

        TextView heading =
                title("إدخال الدرجات");

        header.addView(
                heading,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        page.addView(header);
        page.addView(space(10));

        TextView info =
                text(
                        "اختر الطالب ثم أدخل درجاته.\n"
                                + "يمكن تعديل الدرجات في أي وقت.",
                        14,
                        GRAY
                );

        page.addView(info);
        page.addView(space(12));

        LinearLayout list =
                new LinearLayout(this);

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        page.addView(list);

        renderGradeList(
                list,
                ""
        );
    }

    private void renderGradeList(
            LinearLayout list,
            String classFilter
    ) {
        list.removeAllViews();

        if (students.length() == 0) {

            TextView empty =
                    text(
                            "لا يوجد طلاب حالياً.\n"
                                    + "أضف الطلاب أولاً من قسم إدارة الطلاب.",
                            16,
                            GRAY
                    );

            empty.setGravity(
                    Gravity.CENTER
            );

            empty.setPadding(
                    0,
                    dp(40),
                    0,
                    dp(40)
            );

            list.addView(empty);

            return;
        }

        ArrayList<String> filters =
                new ArrayList<>();

        filters.add("جميع الصفوف");

        for (int i = 0;
             i < classes.length();
             i++) {

            String value =
                    classes.optString(
                            i,
                            ""
                    );

            if (!value.isEmpty()) {
                filters.add(value);
            }
        }

        Spinner filter =
                spinner(filters);

        if (classFilter != null
                && !classFilter.isEmpty()) {

            int position =
                    filters.indexOf(
                            classFilter
                    );

            if (position >= 0) {
                filter.setSelection(
                        position
                );
            }
        }

        LinearLayout filterBox =
                card();

        TextView filterTitle =
                text(
                        "تصفية حسب الصف",
                        15,
                        DARK
                );

        filterBox.addView(
                filterTitle
        );

        filterBox.addView(
                space(5)
        );

        filterBox.addView(filter);

        list.addView(filterBox);
        list.addView(space(8));

        LinearLayout studentsList =
                new LinearLayout(this);

        studentsList.setOrientation(
                LinearLayout.VERTICAL
        );

        list.addView(studentsList);

        String selected =
                filter.getSelectedItem() == null
                        ? "جميع الصفوف"
                        : filter.getSelectedItem()
                        .toString();

        renderGradeStudents(
                studentsList,
                selected
        );

        filter.setOnItemSelectedListener(
                new android.widget.AdapterView
                        .OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        renderGradeStudents(
                                studentsList,
                                filters.get(position)
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                    }
                }
        );
    }

    private void renderGradeStudents(
            LinearLayout list,
            String selectedClass
    ) {
        list.removeAllViews();

        int count = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            String studentClass =
                    student.optString(
                            "className",
                            ""
                    );

            if (!selectedClass.equals(
                    "جميع الصفوف"
            )
                    && !studentClass.equals(
                    selectedClass
            )) {

                continue;
            }

            count++;

            LinearLayout item =
                    card();

            LinearLayout row =
                    horizontal();

            LinearLayout details =
                    new LinearLayout(this);

            details.setOrientation(
                    LinearLayout.VERTICAL
            );

            TextView name =
                    text(
                            student.optString(
                                    "name",
                                    "بدون اسم"
                            ),
                            17,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            TextView cls =
                    text(
                            "الصف: "
                                    + (
                                    studentClass.isEmpty()
                                            ? "غير محدد"
                                            : studentClass
                            ),
                            13,
                            GRAY
                    );

            details.addView(name);
            details.addView(cls);

            row.addView(
                    details,
                    new LinearLayout.LayoutParams(
                            0,
                            -2,
                                private EditText gradeInput(
            String hint,
            double value
    ) {
        EditText input =
                input(hint);

        input.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        if (value > 0) {
            input.setText(
                    format(value)
            );

            input.setSelection(
                    input.length()
            );
        }

        return input;
    }

    private double grade(
            EditText input
    ) {
        try {
            String value =
                    input.getText()
                            .toString()
                            .trim();

            if (value.isEmpty()) {
                return 0;
            }

            double result =
                    Double.parseDouble(value);

            if (result < 0) {
                return 0;
            }

            if (result > 100) {
                return 100;
            }

            return result;

        } catch (Exception e) {
            return 0;
        }
    }

    private void showGradeEditor(
            JSONObject student
    ) {
        ScrollView scroll =
                new ScrollView(this);

        LinearLayout form =
                new LinearLayout(this);

        form.setOrientation(
                LinearLayout.VERTICAL
        );

        form.setPadding(
                dp(20),
                dp(5),
                dp(20),
                dp(5)
        );

        scroll.addView(form);

        TextView studentName =
                text(
                        student.optString(
                                "name",
                                "بدون اسم"
                        ),
                        20,
                        DARK
                );

        studentName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        form.addView(studentName);
        form.addView(space(5));

        TextView studentClass =
                text(
                        "الصف: "
                                + student.optString(
                                "className",
                                "غير محدد"
                        ),
                        14,
                        GRAY
                );

        form.addView(studentClass);
        form.addView(space(15));

        TextView note =
                text(
                        "الدرجات من 0 إلى 100",
                        13,
                        GRAY
                );

        form.addView(note);
        form.addView(space(8));

        EditText monthly =
                gradeInput(
                        "درجة الشهرية",
                        student.optDouble(
                                "monthly",
                                0
                        )
                );

        EditText mid =
                gradeInput(
                        "درجة نصف السنة",
                        student.optDouble(
                                "mid",
                                0
                        )
                );

        EditText second =
                gradeInput(
                        "درجة الشهر الثاني",
                        student.optDouble(
                                "second",
                                0
                        )
                );

        EditText finalGrade =
                gradeInput(
                        "الدرجة النهائية",
                        student.optDouble(
                                "final",
                                0
                        )
                );

        form.addView(monthly);
        form.addView(space(8));

        form.addView(mid);
        form.addView(space(8));

        form.addView(second);
        form.addView(space(8));

        form.addView(finalGrade);
        form.addView(space(15));

        TextView preview =
                text(
                        "المعدل: "
                                + format(
                                average(student)
                        ),
                        17,
                        BLUE
                );

        preview.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        form.addView(preview);

        TextWatcher watcher =
                new TextWatcher() {

                    @Override
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

                        double avg =
                                (
                                        grade(monthly)
                                                + grade(mid)
                                                + grade(second)
                                                + grade(finalGrade)
                                ) / 4.0;

                        preview.setText(
                                "المعدل: "
                                        + format(avg)
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                };

        monthly.addTextChangedListener(
                watcher
        );

        mid.addTextChangedListener(
                watcher
        );

        second.addTextChangedListener(
                watcher
        );

        finalGrade.addTextChangedListener(
                watcher
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "درجات الطالب"
                        )
                        .setView(scroll)
                        .setNegativeButton(
                                "إلغاء",
                                null
                        )
                        .setPositiveButton(
                                "حفظ",
                                null
                        )
                        .create();

        dialog.setOnShowListener(
                d -> {

                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    ).setOnClickListener(
                            v -> {

                                try {

                                    student.put(
                                            "monthly",
                                            grade(monthly)
                                    );

                                    student.put(
                                            "mid",
                                            grade(mid)
                                    );

                                    student.put(
                                            "second",
                                            grade(second)
                                    );

                                    student.put(
                                            "final",
                                            grade(finalGrade)
                                    );

                                    saveData();

                                    dialog.dismiss();

                                    showGrades();

                                    toast(
                                            "تم حفظ درجات الطالب"
                                    );

                                } catch (Exception e) {

                                    toast(
                                            "حدث خطأ أثناء حفظ الدرجات"
                                    );
                                }
                            }
                    );
                }
        );

        dialog.show();
    }

    private double average(
            JSONObject student
    ) {
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
                monthly
                        + mid
                        + second
                        + finalGrade
        ) / 4.0;
    }

    private String format(
            double value
    ) {
        if (value == (long) value) {
            return String.valueOf(
                    (long) value
            );
        }

        return String.format(
                Locale.US,
                "%.2f",
                value
        );
    }

    private String result(
            double average
    ) {
        if (average >= 50) {
            return "ناجح";
        }

        return "راسب";
    }

    private String gradeText(
            double average
    ) {
        if (average >= 90) {
            return "ممتاز";
        }

        if (average >= 80) {
            return "جيد جداً";
        }

        if (average >= 70) {
            return "جيد";
        }

        if (average >= 60) {
            return "متوسط";
        }

        if (average >= 50) {
            return "مقبول";
        }

        return "راسب";
    }

    private void showRecords() {
        prepareRoot();

        LinearLayout page =
                content();

        TextView heading =
                title("سجل الدرجات");

        page.addView(heading);
        page.addView(space(8));

        TextView description =
                text(
                        "عرض شامل لدرجات جميع الطلاب ونتائجهم.",
                        14,
                        GRAY
                );

        page.addView(description);
        page.addView(space(12));

        LinearLayout list =
                new LinearLayout(this);

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        page.addView(list);

        renderRecords(list);
    }

    private void renderRecords(
            LinearLayout list
    ) {
        list.removeAllViews();

        if (students.length() == 0) {

            TextView empty =
                    text(
                            "لا يوجد طلاب لعرض السجل.",
                            16,
                            GRAY
                    );

            empty.setGravity(
                    Gravity.CENTER
            );

            empty.setPadding(
                    0,
                    dp(35),
                    0,
                    dp(35)
            );

            list.addView(empty);

            return;
        }

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            double avg =
                    average(student);

            LinearLayout item =
                    card();

            TextView name =
                    text(
                            student.optString(
                                    "name",
                                    "بدون اسم"
                            ),
                            18,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            item.addView(name);

            TextView cls =
                    text(
                            "الصف: "
                                    + student.optString(
                                    "className",
                                    "غير محدد"
                            ),
                            14,
                            GRAY
                    );

            item.addView(cls);
            item.addView(space(8));

            TextView grades =
                    text(
                            "الشهرية: "
                                    + format(
                                    student.optDouble(
                                            "monthly",
                                            0
                                    )
                            )
                                    + "    |    نصف السنة: "
                                    + format(
                                    student.optDouble(
                                            "mid",
                                            0
                                    )
                            )
                                    + "\n"
                                    + "الشهر الثاني: "
                                    + format(
                                    student.optDouble(
                                            "second",
                                            0
                                    )
                            )
                                    + "    |    النهائية: "
                                    + format(
                                    student.optDouble(
                                            "final",
                                            0
                                    )
                            ),
                            14,
                            DARK
                    );

            item.addView(grades);
            item.addView(space(8));

            TextView resultText =
                    text(
                            "المعدل: "
                                    + format(avg)
                                    + "  —  "
                                    + gradeText(avg)
                                    + "  —  "
                                    + result(avg),
                            15,
                            avg >= 50
                                    ? GREEN
                                    : RED
                    );

            resultText.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            item.addView(resultText);

            list.addView(item);
        }
    }

    private void showRanking() {
        prepareRoot();

        LinearLayout page =
                content();

        TextView heading =
                title("ترتيب الطلاب");

        page.addView(heading);
        page.addView(space(8));

        TextView description =
                text(
                        "ترتيب الطلاب من الأعلى معدلًا إلى الأقل.",
                        14,
                        GRAY
                );

        page.addView(description);
        page.addView(space(12));

        ArrayList<JSONObject> ranking =
                new ArrayList<>();

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student != null) {
                ranking.add(student);
            }
        }

        Collections.sort(
                ranking,
                (a, b) -> Double.compare(
                        average(b),
                        average(a)
                )
        );

        if (ranking.isEmpty()) {

            page.addView(
                    text(
                            "لا يوجد طلاب حالياً.",
                            16,
                            GRAY
                    )
            );

            return;
        }

        int position = 1;

        for (JSONObject student : ranking) {

            double avg =
                    average(student);

            LinearLayout item =
                    card();

            LinearLayout row =
                    horizontal();

            TextView number =
                    text(
                            "#" + position,
                            20,
                            BLUE
                    );

            number.setGravity(
                    Gravity.CENTER
            );

            number.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            row.addView(
                    number,
                    new LinearLayout.LayoutParams(
                            dp(55),
                            dp(50)
                    )
            );

            LinearLayout details =
                    new LinearLayout(this);

            details.setOrientation(
                    LinearLayout.VERTICAL
            );

            TextView name =
                    text(
                            student.optString(
                                    "name",
                                    "بدون اسم"
                            ),
                            17,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            TextView cls =
                    text(
                            student.optString(
                                    "className",
                                    "غير محدد"
                            ),
                            13,
                            GRAY
                    );

            details.addView(name);
            details.addView(cls);

            row.addView(
                    details,
                    new LinearLayout.LayoutParams(
                            0,
                            -2,
                            1
                    )
            );

            TextView avgText =
                    text(
                            format(avg),
                            20,
                            avg >= 50
                                    ? GREEN
                                    : RED
                    );

            avgText.setGravity(
                    Gravity.CENTER
            );

            avgText.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            row.addView(
                    avgText,
                    new LinearLayout.LayoutParams(
                            dp(70),
                            dp(50)
                    )
            );

            item.addView(row);
            page.addView(item);

            position++;
        }
    }    private void showClassReport() {
        prepareRoot();

        LinearLayout page = content();

        TextView heading =
                title("تقارير الصفوف");

        page.addView(heading);
        page.addView(space(10));

        if (classes.length() == 0) {
            page.addView(
                    text(
                            "لا توجد صفوف مضافة.",
                            16,
                            GRAY
                    )
            );
            return;
        }

        for (int i = 0;
             i < classes.length();
             i++) {

            String className =
                    classes.optString(i, "");

            LinearLayout item = card();

            TextView name =
                    text(
                            className,
                            18,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            item.addView(name);
            item.addView(space(5));

            int count =
                    countInClass(className);

            double avg =
                    classAverage(className);

            TextView info =
                    text(
                            "عدد الطلاب: "
                                    + count
                                    + "\nمتوسط الصف: "
                                    + format(avg),
                            14,
                            GRAY
                    );

            item.addView(info);
            item.addView(space(8));

            Button report =
                    smallButton(
                            "عرض التقرير",
                            BLUE
                    );

            item.addView(report);

            report.setOnClickListener(
                    v -> showOneClassReport(
                            className
                    )
            );

            page.addView(item);
        }
    }

    private double classAverage(
            String className
    ) {
        double total = 0;
        int count = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            if (student.optString(
                    "className",
                    ""
            ).equals(className)) {

                total += average(student);
                count++;
            }
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }

    private void showOneClassReport(
            String className
    ) {
        prepareRoot();

        LinearLayout page =
                content();

        TextView heading =
                title("تقرير " + className);

        page.addView(heading);
        page.addView(space(8));

        int count =
                countInClass(className);

        double avg =
                classAverage(className);

        LinearLayout summary =
                card();

        TextView summaryText =
                text(
                        "عدد الطلاب: "
                                + count
                                + "\nمتوسط الصف: "
                                + format(avg),
                        17,
                        DARK
                );

        summaryText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        summary.addView(summaryText);

        page.addView(summary);
        page.addView(space(10));

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            if (!student.optString(
                    "className",
                    ""
            ).equals(className)) {
                continue;
            }

            double studentAverage =
                    average(student);

            LinearLayout item =
                    card();

            TextView name =
                    text(
                            student.optString(
                                    "name",
                                    "بدون اسم"
                            ),
                            16,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            item.addView(name);
            item.addView(space(5));

            TextView resultView =
                    text(
                            "المعدل: "
                                    + format(studentAverage)
                                    + "\nالتقدير: "
                                    + gradeText(
                                    studentAverage
                            )
                                    + "\nالنتيجة: "
                                    + result(studentAverage),
                            14,
                            studentAverage >= 50
                                    ? GREEN
                                    : RED
                    );

            item.addView(resultView);

            page.addView(item);
        }
    }

    private void showReport() {
        prepareRoot();

        LinearLayout page =
                content();

        TextView heading =
                title("التقرير العام");

        page.addView(heading);
        page.addView(space(10));

        int totalStudents =
                students.length();

        int passed = 0;
        int failed = 0;
        double totalAverage = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            double avg =
                    average(student);

            totalAverage += avg;

            if (avg >= 50) {
                passed++;
            } else {
                failed++;
            }
        }

        double generalAverage =
                totalStudents == 0
                        ? 0
                        : totalAverage
                        / totalStudents;

        page.addView(
                statCard(
                        "إجمالي الطلاب",
                        String.valueOf(
                                totalStudents
                        ),
                        BLUE
                )
        );

        page.addView(
                statCard(
                        "الناجحون",
                        String.valueOf(passed),
                        GREEN
                )
        );

        page.addView(
                statCard(
                        "الراسبون",
                        String.valueOf(failed),
                        RED
                )
        );

        page.addView(
                statCard(
                        "المعدل العام",
                        format(generalAverage),
                        ORANGE
                )
        );

        page.addView(space(12));

        double successRate =
                totalStudents == 0
                        ? 0
                        : (passed * 100.0)
                        / totalStudents;

        TextView details =
                text(
                        "نسبة النجاح: "
                                + format(successRate)
                                + "%",
                        16,
                        DARK
                );

        details.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        page.addView(
                cardWithView(details)
        );
    }

    private View cardWithView(
            View child
    ) {
        LinearLayout container =
                card();

        container.addView(child);

        return container;
    }

    private void showBackup() {
        prepareRoot();

        LinearLayout page =
                content();

        TextView heading =
                title("النسخ الاحتياطي");

        page.addView(heading);
        page.addView(space(8));

        TextView description =
                text(
                        "يمكنك إنشاء نسخة نصية من بيانات "
                                + "الطلاب والصفوف والدرجات "
                                + "وحفظها أو إرسالها.",
                        15,
                        GRAY
                );

        page.addView(description);
        page.addView(space(15));

        Button export =
                button(
                        "💾 مشاركة نسخة احتياطية",
                        BLUE
                );

        Button summary =
                button(
                        "📋 مشاركة ملخص الدرجات",
                        GREEN
                );

        page.addView(export);
        page.addView(summary);

        export.setOnClickListener(
                v -> shareText(
                        createBackupText()
                )
        );

        summary.setOnClickListener(
                v -> shareText(
                        createSummaryText()
                )
        );
    }

    private String createBackupText() {
        StringBuilder data =
                new StringBuilder();

        data.append(
                "سجل درجات الاجتماعيات\n"
        );

        data.append(
                "الأستاذ أمير محمد\n\n"
        );

        data.append(
                "=== الصفوف ===\n"
        );

        for (int i = 0;
             i < classes.length();
             i++) {

            data.append(
                    i + 1
                            + ". "
                            + classes.optString(
                            i,
                            ""
                    )
                            + "\n"
            );
        }

        data.append(
                "\n=== الطلاب ===\n"
        );

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            data.append(
                    "\nالطالب: "
                            + student.optString(
                            "name",
                            ""
                    )
            );

            data.append(
                    "\nالمدرسة: "
                            + student.optString(
                            "school",
                            ""
                    )
            );

            data.append(
                    "\nالصف: "
                            + student.optString(
                            "className",
                            ""
                    )
            );

            data.append(
                    "\nالشهرية: "
                            + format(
                            student.optDouble(
                                    "monthly",
                                    0
                            )
                    )
            );

            data.append(
                    "\nنصف السنة: "
                            + format(
                            student.optDouble(
                                    "mid",
                                    0
                            )
                    )
            );

            data.append(
                    "\nالشهر الثاني: "
                            + format(
                            student.optDouble(
                                    "second",
                                    0
                            )
                    )
            );

            data.append(
                    "\nالنهائية: "
                            + format(
                            student.optDouble(
                                    "final",
                                    0
                            )
                    )
            );

            data.append(
                    "\nالمعدل: "
                            + format(
                            average(student)
                    )
            );

            data.append(
                    "\n--------------------\n"
            );
        }

        return data.toString();
    }

    private String createSummaryText() {
        StringBuilder data =
                new StringBuilder();

        data.append(
                "سجل درجات الاجتماعيات\n"
        );

        data.append(
                "الأستاذ أمير محمد\n\n"
        );

        for (int i = 0;
             i < students.length();
             i++) {

            JSONObject student =
                    students.optJSONObject(i);

            if (student == null) {
                continue;
            }

            double avg =
                    average(student);

            data.append(
                    student.optString(
                            "name",
                            "بدون اسم"
                    )
            );

            data.append(
                    " | "
                            + student.optString(
                            "className",
                            "غير محدد"
                    )
            );

            data.append(
                    " | المعدل: "
                            + format(avg)
            );

            data.append(
                    " | "
                            + result(avg)
            );

            data.append("\n");
        }

        return data.toString();
    }

    private void shareText(
            String value
    ) {
        try {

            Intent intent =
                    new Intent(
                            Intent.ACTION_SEND
                    );

            intent.setType(
                    "text/plain"
            );

            intent.putExtra(
                    Intent.EXTRA_TEXT,
                    value
            );

            startActivity(
                    Intent.createChooser(
                            intent,
                            "مشاركة البيانات"
                    )
            );

        } catch (Exception e) {

            toast(
                    "تعذر فتح المشاركة"
            );
        }
    }

    private void showSettings() {
        prepareRoot();

        LinearLayout page =
                content();

        TextView heading =
                title("الإعدادات");

        page.addView(heading);
        page.addView(space(10));

        LinearLayout appInfo =
                card();

        TextView info =
                text(
                        "سجل درجات الاجتماعيات\n"
                                + "الأستاذ أمير محمد\n\n"
                                + "نظام إدارة درجات الطلاب",
                        16,
                        DARK
                );

        info.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        appInfo.addView(info);
        page.addView(appInfo);
        page.addView(space(12));

        Button report =
                button(
                        "📊 التقرير العام",
                        BLUE
                );

        Button classReports =
                button(
                        "📚 تقارير الصفوف",
                        DARK_BLUE
                );

        Button clear =
                button(
                        "🗑️ حذف جميع البيانات",
                        RED
                );

        page.addView(report);
        page.addView(classReports);
        page.addView(clear);
        page.addView(space(15));

        TextView version =
                text(
                        "الإصدار 1.0",
                        13,
                        GRAY
                );

        version.setGravity(
                Gravity.CENTER
        );

        page.addView(version);

        report.setOnClickListener(
                v -> showReport()
        );

        classReports.setOnClickListener(
                v -> showClassReport()
        );

        clear.setOnClickListener(
                v -> confirmClearData()
        );
    }

    private void confirmClearData() {
        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف جميع البيانات"
                )
                .setMessage(
                        "تحذير!\n\n"
                                + "سيتم حذف جميع الطلاب "
                                + "والصفوف والدرجات.\n\n"
                                + "لا يمكن التراجع عن هذه العملية."
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حذف نهائياً",
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
                .show();
    }

    private void toast(
            String message
    ) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onBackPressed() {
        showHome();
    }
                    }

package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String PREFS = "gradebook_data";
    private static final String KEY_STUDENTS = "students";
    private static final String KEY_CLASSES = "classes";

    private SharedPreferences prefs;
    private JSONArray students = new JSONArray();
    private JSONArray classes = new JSONArray();

    private LinearLayout root;

    private final int BLUE = Color.rgb(35, 102, 158);
    private final int DARK = Color.rgb(35, 35, 35);
    private final int LIGHT = Color.rgb(248, 249, 250);
    private final int GREEN = Color.rgb(40, 130, 70);
    private final int RED = Color.rgb(190, 55, 55);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        loadData();

        showHome();
    }

    private void loadData() {
        try {
            students = new JSONArray(
                    prefs.getString(KEY_STUDENTS, "[]")
            );

            classes = new JSONArray(
                    prefs.getString(KEY_CLASSES, "[]")
            );

        } catch (Exception e) {
            students = new JSONArray();
            classes = new JSONArray();
        }
    }

    private void saveData() {
        prefs.edit()
                .putString(KEY_STUDENTS, students.toString())
                .putString(KEY_CLASSES, classes.toString())
                .apply();
    }

    private int dp(int value) {
        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
                + 0.5f
        );
    }

    private TextView title(String text) {
        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(27);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(null, 1);

        t.setPadding(
                dp(12),
                dp(14),
                dp(12),
                dp(14)
        );

        t.setBackgroundColor(BLUE);

        return t;
    }

    private TextView info(String text) {
        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(18);
        t.setTextColor(DARK);
        t.setGravity(Gravity.CENTER);

        t.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        return t;
    }

    private Button button(String text) {
        Button b = new Button(this);

        b.setText(text);
        b.setTextSize(18);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setBackgroundColor(BLUE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                );

        p.setMargins(
                dp(8),
                dp(6),
                dp(8),
                dp(6)
        );

        b.setLayoutParams(p);

        return b;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(18);
        e.setGravity(Gravity.RIGHT);
        e.setSingleLine(true);

        e.setPadding(
                dp(12),
                dp(8),
                dp(12),
                dp(8)
        );

        return e;
    }

    private void prepareRoot() {
        root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(LIGHT);

        root.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        setContentView(root);
    }

    private LinearLayout content() {
        LinearLayout c =
                new LinearLayout(this);

        c.setOrientation(
                LinearLayout.VERTICAL
        );

        c.setPadding(
                dp(14),
                dp(12),
                dp(14),
                dp(20)
        );

        c.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        return c;
    }

    private ScrollView scroll(
            LinearLayout content
    ) {
        ScrollView s =
                new ScrollView(this);

        s.setFillViewport(true);
        s.addView(content);

        return s;
    }

    private void addSpace(
            LinearLayout c,
            int height
    ) {
        TextView space =
                new TextView(this);

        space.setHeight(dp(height));

        c.addView(space);
    }

    private void showHome() {
        prepareRoot();

        root.addView(
                title("سجل درجات الاجتماعيات")
        );

        root.addView(
                info("الأستاذ أمير محمد")
        );

        root.addView(
                info("نظام إدارة درجات الطلاب")
        );

        addSpace(root, 8);

        Button students =
                button("👨‍🎓 إدارة الطلاب");

        Button classesButton =
                button("🏫 الشعب الدراسية");

        Button grades =
                button("📊 إدخال الدرجات");

        Button records =
                button("📋 سجل الدرجات والتقارير");

        Button backup =
                button("💾 النسخ الاحتياطي والمشاركة");

        root.addView(students);
        root.addView(classesButton);
        root.addView(grades);
        root.addView(records);
        root.addView(backup);

        students.setOnClickListener(
                v -> showStudents()
        );

        classesButton.setOnClickListener(
                v -> showClasses()
        );

        grades.setOnClickListener(
                v -> showGrades()
        );

        records.setOnClickListener(
                v -> showRecords()
        );

        backup.setOnClickListener(
                v -> shareBackup()
        );

        TextView count =
                info(
                        "عدد الطلاب: "
                                + students.length()
                                + "\nعدد الشعب: "
                                + classes.length()
                );

        count.setTextSize(16);

        root.addView(count);
    }

    private void showStudents() {
        prepareRoot();

        root.addView(
                title("إدارة الطلاب")
        );

        Button add =
                button("➕ إضافة طالب");

        root.addView(add);

        EditText search =
                input("🔎 البحث عن طالب");

        root.addView(search);

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        renderStudents(
                list,
                ""
        );

        search.addTextChangedListener(
                new android.text.TextWatcher() {

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
                                s.toString().trim()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s
                    ) {
                    }
                }
        );

        add.setOnClickListener(
                v -> addStudentDialog()
        );

        Button back =
                button("⬅ العودة للرئيسية");

        root.addView(back);

        back.setOnClickListener(
                v -> showHome()
        );
    }

    private void renderStudents(
            LinearLayout list,
            String query
    ) {
        list.removeAllViews();

        boolean found = false;

        for (int i = 0;
             i < students.length();
             i++) {

            try {
                JSONObject st =
                        students.getJSONObject(i);

                String name =
                        st.optString(
                                "name",
                                ""
                        );

                String lowerName =
                        name.toLowerCase(
                                Locale.ROOT
                        );

                String lowerQuery =
                        query.toLowerCase(
                                Locale.ROOT
                        );

                if (!query.isEmpty()
                        && !lowerName.contains(
                                lowerQuery
                        )) {
                    continue;
                }

                found = true;

                final int index = i;

                String className =
                        st.optString(
                                "className",
                                "بدون شعبة"
                        );

                Button item =
                        button(
                                "👤 " + name
                                        + "\n"
                                        + "الشعبة: "
                                        + className
                        );

                item.setOnClickListener(
                        v -> showStudentOptions(index)
                );

                list.addView(item);

            } catch (Exception ignored) {
            }
        }

        if (!found) {
            TextView empty =
                    info(
                            query.isEmpty()
                                    ? "لا يوجد طلاب حالياً."
                                    : "لا يوجد طالب مطابق للبحث."
                    );

            empty.setTextSize(20);

            list.addView(empty);
        }
    }

    private void addStudentDialog() {
        LinearLayout box =
                content();

        EditText name =
                input("اسم الطالب");

        EditText school =
                input("اسم المدرسة (اختياري)");

        box.addView(name);
        box.addView(school);

        Spinner classSpinner =
                new Spinner(this);

        ArrayList<String> options =
                new ArrayList<>();

        options.add("بدون شعبة");

        for (int i = 0;
             i < classes.length();
             i++) {

            options.add(
                    classes.optString(
                            i,
                            ""
                    )
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        options
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        classSpinner.setAdapter(adapter);

        box.addView(classSpinner);

        new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(box)
                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            String studentName =
                                    name.getText()
                                            .toString()
                                            .trim();

                            if (studentName.isEmpty()) {
                                Toast.makeText(
                                        this,
                                        "اكتب اسم الطالب",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            try {
                                JSONObject st =
                                        new JSONObject();

                                st.put(
                                        "name",
                                        studentName
                                );

                                st.put(
                                        "school",
                                        school.getText()
                                                .toString()
                                                .trim()
                                );

                                st.put(
                                        "className",
                                        classSpinner
                                                .getSelectedItem()
                                                .toString()
                                );

                                st.put("monthly", 0);
                                st.put("mid", 0);
                                st.put("second", 0);
                                st.put("final", 0);

                                students.put(st);

                                saveData();

                                showStudents();

                                Toast.makeText(
                                        this,
                                        "تمت إضافة الطالب بنجاح",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } catch (Exception e) {
                                Toast.makeText(
                                        this,
                                        "حدث خطأ أثناء إضافة الطالب",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .show();
    }

    private void showStudentOptions(
            int index
    ) {
        String[] options = {
                "✏️ تعديل البيانات",
                "🗑️ حذف الطالب",
                "📊 إدخال الدرجات",
                "📋 عرض تقرير الطالب"
        };

        new AlertDialog.Builder(this)
                .setTitle("خيارات الطالب")
                .setItems(
                        options,
                        (dialog, which) -> {

                            if (which == 0) {
                                editStudentDialog(index);

                            } else if (which == 1) {
                                deleteStudent(index);

                            } else if (which == 2) {
                                showGradeEditor(index);

                            } else {
                                showStudentReport(index);
                            }
                        }
                )
                .show();
    }

        private void editStudentDialog(int index) {

        try {

            JSONObject st =
                    students.getJSONObject(index);

            LinearLayout box =
                    content();

            EditText name =
                    input("اسم الطالب");

            EditText school =
                    input("اسم المدرسة");

            name.setText(
                    st.optString(
                            "name",
                            ""
                    )
            );

            school.setText(
                    st.optString(
                            "school",
                            ""
                    )
            );

            box.addView(name);
            box.addView(school);

            Spinner classSpinner =
                    new Spinner(this);

            ArrayList<String> options =
                    new ArrayList<>();

            options.add("بدون شعبة");

            for (int i = 0;
                 i < classes.length();
                 i++) {

                options.add(
                        classes.optString(
                                i,
                                ""
                        )
                );
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            options
                    );

            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
            );

            classSpinner.setAdapter(adapter);

            String oldClass =
                    st.optString(
                            "className",
                            "بدون شعبة"
                    );

            int selected = 0;

            for (int i = 0;
                 i < options.size();
                 i++) {

                if (options.get(i).equals(oldClass)) {
                    selected = i;
                    break;
                }
            }

            classSpinner.setSelection(selected);

            box.addView(classSpinner);

            new AlertDialog.Builder(this)
                    .setTitle("تعديل بيانات الطالب")
                    .setView(box)
                    .setPositiveButton(
                            "حفظ",
                            (dialog, which) -> {

                                String newName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                if (newName.isEmpty()) {

                                    Toast.makeText(
                                            this,
                                            "اسم الطالب مطلوب",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                try {

                                    st.put(
                                            "name",
                                            newName
                                    );

                                    st.put(
                                            "school",
                                            school.getText()
                                                    .toString()
                                                    .trim()
                                    );

                                    st.put(
                                            "className",
                                            classSpinner
                                                    .getSelectedItem()
                                                    .toString()
                                    );

                                    saveData();

                                    showStudents();

                                    Toast.makeText(
                                            this,
                                            "تم تعديل بيانات الطالب",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } catch (Exception e) {

                                    Toast.makeText(
                                            this,
                                            "حدث خطأ أثناء التعديل",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    )
                    .setNegativeButton(
                            "إلغاء",
                            null
                    )
                    .show();

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

            JSONObject st =
                    students.getJSONObject(index);

            String name =
                    st.optString(
                            "name",
                            "الطالب"
                    );

            new AlertDialog.Builder(this)
                    .setTitle("حذف الطالب")
                    .setMessage(
                            "هل أنت متأكد من حذف:\n"
                                    + name
                                    + " ؟\n\n"
                                    + "سيتم حذف بيانات الطالب ودرجاته."
                    )
                    .setPositiveButton(
                            "حذف",
                            (dialog, which) -> {

                                students.remove(index);

                                saveData();

                                showStudents();

                                Toast.makeText(
                                        this,
                                        "تم حذف الطالب",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                    )
                    .setNegativeButton(
                            "إلغاء",
                            null
                    )
                    .show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر حذف الطالب",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showClasses() {

        prepareRoot();

        root.addView(
                title("الشعب الدراسية")
        );

        Button add =
                button("➕ إضافة شعبة");

        root.addView(add);

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        renderClasses(list);

        add.setOnClickListener(
                v -> addClassDialog()
        );

        Button back =
                button("⬅ العودة للرئيسية");

        root.addView(back);

        back.setOnClickListener(
                v -> showHome()
        );
    }

    private void renderClasses(
            LinearLayout list
    ) {

        list.removeAllViews();

        if (classes.length() == 0) {

            list.addView(
                    info(
                            "لا توجد شعب دراسية.\n"
                                    + "اضغط «إضافة شعبة» لإنشاء شعبة جديدة."
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

            int studentCount = 0;

            for (int j = 0;
                 j < students.length();
                 j++) {

                try {

                    JSONObject st =
                            students.getJSONObject(j);

                    if (className.equals(
                            st.optString(
                                    "className",
                                    ""
                            )
                    )) {

                        studentCount++;
                    }

                } catch (Exception ignored) {
                }
            }

            Button item =
                    button(
                            "🏫 "
                                    + className
                                    + "\n"
                                    + "عدد الطلاب: "
                                    + studentCount
                    );

            item.setOnClickListener(
                    v -> classOptions(index)
            );

            list.addView(item);
        }
    }

    private void addClassDialog() {

        LinearLayout box =
                content();

        EditText name =
                input("اسم الشعبة");

        box.addView(name);

        new AlertDialog.Builder(this)
                .setTitle("إضافة شعبة")
                .setView(box)
                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            String className =
                                    name.getText()
                                            .toString()
                                            .trim();

                            if (className.isEmpty()) {

                                Toast.makeText(
                                        this,
                                        "اكتب اسم الشعبة",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            for (int i = 0;
                                 i < classes.length();
                                 i++) {

                                if (className.equals(
                                        classes.optString(
                                                i,
                                                ""
                                        )
                                )) {

                                    Toast.makeText(
                                            this,
                                            "هذه الشعبة موجودة مسبقاً",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }
                            }

                            classes.put(className);

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

    private void classOptions(
            int index
    ) {

        String className =
                classes.optString(
                        index,
                        ""
                );

        String[] options = {
                "✏️ تعديل اسم الشعبة",
                "🗑️ حذف الشعبة",
                "👨‍🎓 عرض طلاب الشعبة"
        };

        new AlertDialog.Builder(this)
                .setTitle(className)
                .setItems(
                        options,
                        (dialog, which) -> {

                            if (which == 0) {

                                editClassDialog(index);

                            } else if (which == 1) {

                                deleteClass(index);

                            } else {

                                showClassStudents(className);
                            }
                        }
                )
                .show();
    }

    private void editClassDialog(
            int index
    ) {

        String oldName =
                classes.optString(
                        index,
                        ""
                );

        EditText input =
                input("اسم الشعبة");

        input.setText(oldName);

        new AlertDialog.Builder(this)
                .setTitle("تعديل الشعبة")
                .setView(input)
                .setPositiveButton(
                        "حفظ",
                        (dialog, which) -> {

                            String newName =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (newName.isEmpty()) {

                                Toast.makeText(
                                        this,
                                        "اكتب اسم الشعبة",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            if (!newName.equals(oldName)) {

                                for (int i = 0;
                                     i < classes.length();
                                     i++) {

                                    if (newName.equals(
                                            classes.optString(
                                                    i,
                                                    ""
                                            )
                                    )) {

                                        Toast.makeText(
                                                this,
                                                "هذه الشعبة موجودة مسبقاً",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;
                                    }
                                }
                            }
try {
    classes.put(index, newName);
} catch (Exception e) {
    Toast.makeText(
            this,
            "حدث خطأ أثناء تعديل الشعبة",
            Toast.LENGTH_SHORT
    ).show();
    return;
}

                            for (int i = 0;
                                 i < students.length();
                                 i++) {

                                try {

                                    JSONObject st =
                                            students.getJSONObject(i);

                                    if (oldName.equals(
                                            st.optString(
                                                    "className",
                                                    ""
                                            )
                                    )) {

                                        st.put(
                                                "className",
                                                newName
                                        );
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            saveData();

                            showClasses();

                            Toast.makeText(
                                    this,
                                    "تم تعديل الشعبة",
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

    private void deleteClass(
            int index
    ) {

        String className =
                classes.optString(
                        index,
                        ""
                );

        new AlertDialog.Builder(this)
                .setTitle("حذف الشعبة")
                .setMessage(
                        "هل تريد حذف شعبة:\n"
                                + className
                                + " ؟\n\n"
                                + "سيتم نقل طلابها إلى «بدون شعبة»."
                )
                .setPositiveButton(
                        "حذف",
                        (dialog, which) -> {

                            classes.remove(index);

                            for (int i = 0;
                                 i < students.length();
                                 i++) {

                                try {

                                    JSONObject st =
                                            students.getJSONObject(i);

                                    if (className.equals(
                                            st.optString(
                                                    "className",
                                                    ""
                                            )
                                    )) {

                                        st.put(
                                                "className",
                                                "بدون شعبة"
                                        );
                                    }

                                } catch (Exception ignored) {
                                }
                            }

                            saveData();

                            showClasses();

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
    }

    private void showClassStudents(
            String className
    ) {

        prepareRoot();

        root.addView(
                title(
                        "طلاب شعبة " + className
                )
        );

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        boolean found = false;

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject st =
                        students.getJSONObject(i);

                if (!className.equals(
                        st.optString(
                                "className",
                                ""
                        )
                )) {
                    continue;
                }

                found = true;

                String name =
                        st.optString(
                                "name",
                                ""
                        );

                Button item =
                        button(
                                "👤 " + name
                        );

                final int studentIndex = i;

                item.setOnClickListener(
                        v -> showStudentOptions(
                                studentIndex
                        )
                );

                list.addView(item);

            } catch (Exception ignored) {
            }
        }

        if (!found) {

            list.addView(
                    info(
                            "لا يوجد طلاب في هذه الشعبة."
                    )
            );
        }

        Button back =
                button("⬅ العودة للشعب");

        root.addView(back);

        back.setOnClickListener(
                v -> showClasses()
        );
    }

        private void showGrades() {

        prepareRoot();

        root.addView(
                title("إدخال الدرجات")
        );

        TextView note =
                info(
                        "اختر الطالب لإدخال أو تعديل درجاته"
                );

        note.setTextSize(17);

        root.addView(note);

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        if (students.length() == 0) {

            list.addView(
                    info(
                            "لا يوجد طلاب.\n"
                                    + "أضف الطلاب أولاً من إدارة الطلاب."
                    )
            );

        } else {

            for (int i = 0;
                 i < students.length();
                 i++) {

                try {

                    JSONObject st =
                            students.getJSONObject(i);

                    final int index = i;

                    String name =
                            st.optString(
                                    "name",
                                    ""
                            );

                    String className =
                            st.optString(
                                    "className",
                                    "بدون شعبة"
                            );

                    double avg =
                            average(st);

                    Button item =
                            button(
                                    "👤 " + name
                                            + "\n"
                                            + "الشعبة: "
                                            + className
                                            + "\n"
                                            + "المعدل: "
                                            + number(avg)
                            );

                    list.addView(item);

                    item.setOnClickListener(
                            v -> showGradeEditor(index)
                    );

                } catch (Exception ignored) {
                }
            }
        }

        Button back =
                button("⬅ العودة للرئيسية");

        root.addView(back);

        back.setOnClickListener(
                v -> showHome()
        );
    }

    private void showGradeEditor(
            int index
    ) {

        try {

            JSONObject st =
                    students.getJSONObject(index);

            String name =
                    st.optString(
                            "name",
                            ""
                    );

            LinearLayout box =
                    content();

            TextView studentTitle =
                    info(
                            "الطالب: " + name
                    );

            studentTitle.setTextSize(21);
            studentTitle.setTypeface(null, 1);

            box.addView(studentTitle);

            EditText monthly =
                    input("درجة الشهر الأول (0 - 100)");

            EditText mid =
                    input("درجة نصف السنة (0 - 100)");

            EditText second =
                    input("درجة الشهر الثاني (0 - 100)");

            EditText finalExam =
                    input("درجة الامتحان النهائي (0 - 100)");

            monthly.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER
                            | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            mid.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER
                            | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            second.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER
                            | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            finalExam.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER
                            | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            monthly.setText(
                    number(
                            st.optDouble(
                                    "monthly",
                                    0
                            )
                    )
            );

            mid.setText(
                    number(
                            st.optDouble(
                                    "mid",
                                    0
                            )
                    )
            );

            second.setText(
                    number(
                            st.optDouble(
                                    "second",
                                    0
                            )
                    )
            );

            finalExam.setText(
                    number(
                            st.optDouble(
                                    "final",
                                    0
                            )
                    )
            );

            box.addView(
                    info("درجة الشهر الأول")
            );

            box.addView(monthly);

            box.addView(
                    info("درجة نصف السنة")
            );

            box.addView(mid);

            box.addView(
                    info("درجة الشهر الثاني")
            );

            box.addView(second);

            box.addView(
                    info("درجة الامتحان النهائي")
            );

            box.addView(finalExam);

            new AlertDialog.Builder(this)
                    .setTitle("درجات الطالب")
                    .setView(box)
                    .setPositiveButton(
                            "حفظ الدرجات",
                            (dialog, which) -> {

                                try {

                                    double m =
                                            readGrade(
                                                    monthly
                                            );

                                    double h =
                                            readGrade(
                                                    mid
                                            );

                                    double s =
                                            readGrade(
                                                    second
                                            );

                                    double f =
                                            readGrade(
                                                    finalExam
                                            );

                                    if (m < 0
                                            || h < 0
                                            || s < 0
                                            || f < 0
                                            || m > 100
                                            || h > 100
                                            || s > 100
                                            || f > 100) {

                                        Toast.makeText(
                                                this,
                                                "يجب أن تكون كل درجة بين 0 و100",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        return;
                                    }

                                    st.put(
                                            "monthly",
                                            m
                                    );

                                    st.put(
                                            "mid",
                                            h
                                    );

                                    st.put(
                                            "second",
                                            s
                                    );

                                    st.put(
                                            "final",
                                            f
                                    );

                                    saveData();

                                    Toast.makeText(
                                            this,
                                            "تم حفظ الدرجات",
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
                    )
                    .setNegativeButton(
                            "إلغاء",
                            null
                    )
                    .show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر فتح درجات الطالب",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private double readGrade(
            EditText field
    ) {

        String value =
                field.getText()
                        .toString()
                        .trim();

        if (value.isEmpty()) {
            return 0;
        }

        try {

            return Double.parseDouble(
                    value
            );

        } catch (Exception e) {

            return -1;
        }
    }

    private double total(
            JSONObject st
    ) {

        return st.optDouble(
                "monthly",
                0
        )
                + st.optDouble(
                        "mid",
                        0
                )
                + st.optDouble(
                        "second",
                        0
                )
                + st.optDouble(
                        "final",
                        0
                );
    }

    private double average(
            JSONObject st
    ) {

        return total(st) / 4.0;
    }

    private String number(
            double value
    ) {

        if (value == Math.floor(value)) {

            return String.format(
                    Locale.ROOT,
                    "%.0f",
                    value
            );
        }

        return String.format(
                Locale.ROOT,
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

        root.addView(
                title("سجل الدرجات والتقارير")
        );

        LinearLayout menu =
                content();

        root.addView(
                scroll(menu),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        Button all =
                button("📋 جميع الطلاب");

        Button successful =
                button("✅ الطلاب الناجحون");

        Button failed =
                button("❌ الطلاب الراسبون");

        Button ranking =
                button("🏆 ترتيب الطلاب حسب المعدل");

        Button classesReport =
                button("🏫 تقرير الشعب");

        menu.addView(all);
        menu.addView(successful);
        menu.addView(failed);
        menu.addView(ranking);
        menu.addView(classesReport);

        all.setOnClickListener(
                v -> showAllStudentsReport()
        );

        successful.setOnClickListener(
                v -> showResultReport(true)
        );

        failed.setOnClickListener(
                v -> showResultReport(false)
        );

        ranking.setOnClickListener(
                v -> showRanking()
        );

        classesReport.setOnClickListener(
                v -> showClassReports()
        );

        Button back =
                button("⬅ العودة للرئيسية");

        root.addView(back);

        back.setOnClickListener(
                v -> showHome()
        );
    }

    private void showAllStudentsReport() {

        prepareRoot();

        root.addView(
                title("تقرير جميع الطلاب")
        );

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        if (students.length() == 0) {

            list.addView(
                    info("لا يوجد طلاب.")
            );

        } else {

            for (int i = 0;
                 i < students.length();
                 i++) {

                try {

                    JSONObject st =
                            students.getJSONObject(i);

                    list.addView(
                            studentReport(st)
                    );

                } catch (Exception ignored) {
                }
            }
        }

        Button back =
                button("⬅ العودة للتقارير");

        root.addView(back);

        back.setOnClickListener(
                v -> showRecords()
        );
    }

    private void showResultReport(
            boolean passed
    ) {

        prepareRoot();

        root.addView(
                title(
                        passed
                                ? "الطلاب الناجحون"
                                : "الطلاب الراسبون"
                )
        );

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        int count = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject st =
                        students.getJSONObject(i);

                double avg =
                        average(st);

                boolean isPassed =
                        avg >= 50;

                if (isPassed != passed) {
                    continue;
                }

                count++;

                list.addView(
                        studentReport(st)
                );

            } catch (Exception ignored) {
            }
        }

        if (count == 0) {

            list.addView(
                    info(
                            passed
                                    ? "لا يوجد طلاب ناجحون."
                                    : "لا يوجد طلاب راسبون."
                    )
            );
        }

        TextView total =
                info(
                        "العدد: " + count
                );

        total.setTextSize(19);

        list.addView(total);

        Button back =
                button("⬅ العودة للتقارير");

        root.addView(back);

        back.setOnClickListener(
                v -> showRecords()
        );
    }

    private TextView studentReport(
            JSONObject st
    ) {

        String name =
                st.optString(
                        "name",
                        ""
                );

        String school =
                st.optString(
                        "school",
                        ""
                );

        String className =
                st.optString(
                        "className",
                        "بدون شعبة"
                );

        double monthly =
                st.optDouble(
                        "monthly",
                        0
                );

        double mid =
                st.optDouble(
                        "mid",
                        0
                );

        double second =
                st.optDouble(
                        "second",
                        0
                );

        double finalExam =
                st.optDouble(
                        "final",
                        0
                );

        double total =
                monthly
                        + mid
                        + second
                        + finalExam;

        double avg =
                total / 4.0;

        TextView report =
                new TextView(this);

        StringBuilder text =
                new StringBuilder();

        text.append(
                "👤 الطالب: "
        );

        text.append(name);

        text.append("\n");

        if (!school.isEmpty()) {

            text.append(
                    "🏫 المدرسة: "
            );

            text.append(school);

            text.append("\n");
        }

        text.append(
                "📚 الشعبة: "
        );

        text.append(className);

        text.append("\n\n");

        text.append(
                "الشهر الأول: "
        );

        text.append(
                number(monthly)
        );

        text.append("\n");

        text.append(
                "نصف السنة: "
        );

        text.append(
                number(mid)
        );

        text.append("\n");

        text.append(
                "الشهر الثاني: "
        );

        text.append(
                number(second)
        );

        text.append("\n");

        text.append(
                "النهائي: "
        );

        text.append(
                number(finalExam)
        );

        text.append("\n\n");

        text.append(
                "المجموع: "
        );

        text.append(
                number(total)
        );

        text.append("\n");

        text.append(
                "المعدل: "
        );

        text.append(
                number(avg)
        );

        text.append("\n");

        text.append(
                "التقدير: "
        );

        text.append(
                gradeText(avg)
        );

        text.append("\n");

        text.append(
                "النتيجة: "
        );

        text.append(
                result(avg)
        );

        report.setText(
                text.toString()
        );

        report.setTextSize(17);

        report.setTextColor(DARK);

        report.setGravity(
                Gravity.RIGHT
        );

        report.setPadding(
                dp(14),
                dp(14),
                dp(14),
                dp(14)
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        p.setMargins(
                dp(4),
                dp(6),
                dp(4),
                dp(6)
        );

        report.setLayoutParams(p);

        report.setBackgroundColor(
                Color.WHITE
        );

        return report;
    }

    private void showStudentReport(
            int index
    ) {

        try {

            JSONObject st =
                    students.getJSONObject(index);

            prepareRoot();

            root.addView(
                    title("تقرير الطالب")
            );

            LinearLayout box =
                    content();

            root.addView(
                    scroll(box),
                    new LinearLayout.LayoutParams(
                            -1,
                            0,
                            1
                    )
            );

            box.addView(
                    studentReport(st)
            );

            Button edit =
                    button("✏️ تعديل الدرجات");

            Button back =
                    button("⬅ العودة");

            box.addView(edit);
            box.addView(back);

            edit.setOnClickListener(
                    v -> showGradeEditor(index)
            );

            back.setOnClickListener(
                    v -> showStudents()
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر عرض التقرير",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showRanking() {

        prepareRoot();

        root.addView(
                title("🏆 ترتيب الطلاب")
        );

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        ArrayList<JSONObject> ranking =
                new ArrayList<>();

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                ranking.add(
                        students.getJSONObject(i)
                );

            } catch (Exception ignored) {
            }
        }

        Collections.sort(
                ranking,
                new Comparator<JSONObject>() {

                    @Override
                    public int compare(
                            JSONObject a,
                            JSONObject b
                    ) {

                        double avgA =
                                average(a);

                        double avgB =
                                average(b);

                        return Double.compare(
                                avgB,
                                avgA
                        );
                    }
                }
        );

        if (ranking.isEmpty()) {

            list.addView(
                    info(
                            "لا يوجد طلاب للترتيب."
                    )
            );

        } else {

            int position = 1;

            for (JSONObject st : ranking) {

                double avg =
                        average(st);

                TextView item =
                        info(
                                position
                                        + ". "
                                        + st.optString(
                                                "name",
                                                ""
                                        )
                                        + "\n"
                                        + "المعدل: "
                                        + number(avg)
                                        + " — "
                                        + gradeText(avg)
                        );

                item.setTextSize(18);

                list.addView(item);

                position++;
            }
        }

        Button back =
                button("⬅ العودة للتقارير");

        root.addView(back);

        back.setOnClickListener(
                v -> showRecords()
        );
    }

    private void showClassReports() {

        prepareRoot();

        root.addView(
                title("تقارير الشعب")
        );

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        if (classes.length() == 0) {

            list.addView(
                    info(
                            "لا توجد شعب دراسية."
                    )
            );

        } else {

            for (int i = 0;
                 i < classes.length();
                 i++) {

                String className =
                        classes.optString(
                                i,
                                ""
                        );

                Button item =
                        button(
                                "🏫 "
                                        + className
                        );

                list.addView(item);

                item.setOnClickListener(
                        v -> showOneClassReport(
                                className
                        )
                );
            }
        }

        Button back =
                button("⬅ العودة للتقارير");

        root.addView(back);

        back.setOnClickListener(
                v -> showRecords()
        );
    }

    private void showOneClassReport(
            String className
    ) {

        prepareRoot();

        root.addView(
                title(
                        "تقرير شعبة "
                                + className
                )
        );

        LinearLayout list =
                content();

        root.addView(
                scroll(list),
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        int count = 0;

        double totalAverage = 0;

        for (int i = 0;
             i < students.length();
             i++) {

            try {

                JSONObject st =
                        students.getJSONObject(i);

                if (!className.equals(
                        st.optString(
                                "className",
                                ""
                        )
                )) {

                    continue;
                }

                count++;

                totalAverage +=
                        average(st);

                list.addView(
                        studentReport(st)
                );

            } catch (Exception ignored) {
            }
        }

        if (count == 0) {

            list.addView(
                    info(
                            "لا يوجد طلاب في هذه الشعبة."
                    )
            );

        } else {

            double classAverage =
                    totalAverage / count;

            list.addView(
                    info(
                            "عدد الطلاب: "
                                    + count
                                    + "\nمتوسط الشعبة: "
                                    + number(
                                            classAverage
                                    )
                    )
            );
        }

        Button back =
                button("⬅ العودة للشعب");

        root.addView(back);

        back.setOnClickListener(
                v -> showClassReports()
        );
    }

        private void shareBackup() {

        try {

            StringBuilder backup =
                    new StringBuilder();

            backup.append(
                    "سجل درجات الاجتماعيات\n"
            );

            backup.append(
                    "الأستاذ أمير محمد\n"
            );

            backup.append(
                    "====================\n\n"
            );

            backup.append(
                    "الشعب الدراسية:\n"
            );

            for (int i = 0;
                 i < classes.length();
                 i++) {

                backup.append(
                        "- "
                );

                backup.append(
                        classes.optString(
                                i,
                                ""
                        )
                );

                backup.append("\n");
            }

            backup.append(
                    "\n====================\n\n"
            );

            backup.append(
                    "الطلاب والدرجات:\n\n"
            );

            for (int i = 0;
                 i < students.length();
                 i++) {

                try {

                    JSONObject st =
                            students.getJSONObject(i);

                    String name =
                            st.optString(
                                    "name",
                                    ""
                            );

                    String school =
                            st.optString(
                                    "school",
                                    ""
                            );

                    String className =
                            st.optString(
                                    "className",
                                    "بدون شعبة"
                            );

                    double monthly =
                            st.optDouble(
                                    "monthly",
                                    0
                            );

                    double mid =
                            st.optDouble(
                                    "mid",
                                    0
                            );

                    double second =
                            st.optDouble(
                                    "second",
                                    0
                            );

                    double finalExam =
                            st.optDouble(
                                    "final",
                                    0
                            );

                    double total =
                            monthly
                                    + mid
                                    + second
                                    + finalExam;

                    double avg =
                            total / 4.0;

                    backup.append(
                            "الطالب: "
                    );

                    backup.append(name);

                    backup.append("\n");

                    if (!school.isEmpty()) {

                        backup.append(
                                "المدرسة: "
                        );

                        backup.append(
                                school
                        );

                        backup.append("\n");
                    }

                    backup.append(
                            "الشعبة: "
                    );

                    backup.append(
                            className
                    );

                    backup.append("\n");

                    backup.append(
                            "الشهر الأول: "
                    );

                    backup.append(
                            number(monthly)
                    );

                    backup.append("\n");

                    backup.append(
                            "نصف السنة: "
                    );

                    backup.append(
                            number(mid)
                    );

                    backup.append("\n");

                    backup.append(
                            "الشهر الثاني: "
                    );

                    backup.append(
                            number(second)
                    );

                    backup.append("\n");

                    backup.append(
                            "النهائي: "
                    );

                    backup.append(
                            number(finalExam)
                    );

                    backup.append("\n");

                    backup.append(
                            "المجموع: "
                    );

                    backup.append(
                            number(total)
                    );

                    backup.append("\n");

                    backup.append(
                            "المعدل: "
                    );

                    backup.append(
                            number(avg)
                    );

                    backup.append("\n");

                    backup.append(
                            "التقدير: "
                    );

                    backup.append(
                            gradeText(avg)
                    );

                    backup.append("\n");

                    backup.append(
                            "النتيجة: "
                    );

                    backup.append(
                            result(avg)
                    );

                    backup.append(
                            "\n--------------------\n"
                    );

                } catch (Exception ignored) {
                }
            }

            Intent share =
                    new Intent(
                            Intent.ACTION_SEND
                    );

            share.setType(
                    "text/plain"
            );

            share.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "سجل درجات الاجتماعيات"
            );

            share.putExtra(
                    Intent.EXTRA_TEXT,
                    backup.toString()
            );

            startActivity(
                    Intent.createChooser(
                            share,
                            "مشاركة سجل الدرجات"
                    )
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "تعذر إنشاء النسخة الاحتياطية",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onBackPressed() {

        showHome();
    }
}

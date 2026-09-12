package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<String> classes = new ArrayList<>();

    private LinearLayout root;
    private int blue = Color.rgb(35, 75, 120);
    private int lightBlue = Color.rgb(235, 242, 250);
    private int dark = Color.rgb(35, 35, 35);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("gradebook_data", Context.MODE_PRIVATE);

        loadData();
        showHome();
    }

    // =========================================================
    // DATA
    // =========================================================

    private static class Student {
        String name;
        String className;
        double monthly;
        double mid;
        double second;
        double finalGrade;

        Student(String name, String className) {
            this.name = name;
            this.className = className;
        }
    }

    private void loadData() {
        students.clear();
        classes.clear();

        try {
            String studentData = prefs.getString("students", "[]");
            JSONArray arr = new JSONArray(studentData);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                Student s = new Student(
                        o.optString("name", ""),
                        o.optString("class", "")
                );

                s.monthly = o.optDouble("monthly", 0);
                s.mid = o.optDouble("mid", 0);
                s.second = o.optDouble("second", 0);
                s.finalGrade = o.optDouble("final", 0);

                students.add(s);
            }

            String classData = prefs.getString("classes", "[]");
            JSONArray classArr = new JSONArray(classData);

            for (int i = 0; i < classArr.length(); i++) {
                classes.add(classArr.getString(i));
            }

        } catch (Exception e) {
            Toast.makeText(this, "تعذر تحميل البيانات", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData() {
        try {
            JSONArray arr = new JSONArray();

            for (Student s : students) {
                JSONObject o = new JSONObject();

                o.put("name", s.name);
                o.put("class", s.className);
                o.put("monthly", s.monthly);
                o.put("mid", s.mid);
                o.put("second", s.second);
                o.put("final", s.finalGrade);

                arr.put(o);
            }

            JSONArray classArr = new JSONArray();

            for (String c : classes) {
                classArr.put(c);
            }

            prefs.edit()
                    .putString("students", arr.toString())
                    .putString("classes", classArr.toString())
                    .apply();

        } catch (Exception e) {
            Toast.makeText(this, "حدث خطأ أثناء حفظ البيانات", Toast.LENGTH_SHORT).show();
        }
    }

    // =========================================================
    // GENERAL UI
    // =========================================================

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, float size) {
        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(dark);
        t.setGravity(Gravity.CENTER);
        t.setPadding(dp(10), dp(8), dp(10), dp(8));

        return t;
    }

    private Button button(String title) {
        Button b = new Button(this);

        b.setText(title);
        b.setTextSize(18);
        b.setTextColor(Color.BLACK);
        b.setGravity(Gravity.CENTER);
        b.setAllCaps(false);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(220, 223, 225));
        bg.setCornerRadius(dp(8));

        b.setBackground(bg);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
                );

        p.setMargins(dp(20), dp(8), dp(20), dp(8));

        b.setLayoutParams(p);

        return b;
    }

    private LinearLayout createRoot() {
        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(dp(10), dp(15), dp(10), dp(15));
        layout.setBackgroundColor(Color.rgb(247, 249, 253));

        return layout;
    }

    private ScrollView scroll(LinearLayout content) {
        ScrollView s = new ScrollView(this);

        s.setFillViewport(true);
        s.addView(content);

        return s;
    }

    private TextView title(String value) {
        TextView t = text(value, 30);

        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setTextColor(Color.rgb(25, 35, 50));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        p.setMargins(0, dp(10), 0, dp(5));

        t.setLayoutParams(p);

        return t;
    }

    private TextView subtitle(String value) {
        TextView t = text(value, 21);

        t.setTextColor(Color.rgb(90, 100, 110));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        p.setMargins(0, 0, 0, dp(15));

        t.setLayoutParams(p);

        return t;
    }

    private TextView header(String value) {
        TextView t = text(value, 23);

        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setTextColor(Color.WHITE);
        t.setBackgroundColor(blue);

        t.setPadding(dp(10), dp(15), dp(10), dp(15));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        p.setMargins(0, 0, 0, dp(15));

        t.setLayoutParams(p);

        return t;
    }

    private Button backButton() {
        Button b = button("← العودة");

        b.setTextSize(17);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });

        return b;
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHome() {
        root = createRoot();

        TextView t = title("سجل درجات الاجتماعيات");
        root.addView(t);

        root.addView(subtitle("الأستاذ أمير محمد"));

        TextView info = text("نظام إدارة درجات الطلاب", 21);
        info.setTextColor(Color.rgb(110, 120, 130));
        root.addView(info);

        addSpace(15);

        Button studentsButton = button("إدارة الطلاب");

        studentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStudents();
            }
        });

        root.addView(studentsButton);

        Button classesButton = button("الشعب الدراسية");

        classesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClasses();
            }
        });

        root.addView(classesButton);

        Button gradesButton = button("إدخال الدرجات");

        gradesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGrades();
            }
        });

        root.addView(gradesButton);

        Button recordsButton = button("طباعة السجلات");

        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecords();
            }
        });

        root.addView(recordsButton);

        addSpace(15);

        TextView count = text(
                "عدد الطلاب: " + students.size()
                        + "\nعدد الشعب: " + classes.size(),
                18
        );

        count.setTextColor(Color.rgb(90, 100, 110));

        root.addView(count);

        setContentView(scroll(root));
    }

    private void addSpace(int height) {
        TextView space = new TextView(this);

        space.setHeight(dp(height));

        root.addView(space);
    }

    // =========================================================
    // STUDENTS
    // =========================================================

    private void showStudents() {
        root = createRoot();

        root.addView(header("إدارة الطلاب"));

        Button back = backButton();
        root.addView(back);

        Button add = button("＋ إضافة طالب");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudentDialog();
            }
        });

        root.addView(add);

        if (students.size() == 0) {
            TextView empty = text(
                    "لا يوجد طلاب حاليًا\nاضغط «إضافة طالب» لإضافة أول طالب",
                    19
            );

            empty.setPadding(dp(10), dp(30), dp(10), dp(30));

            root.addView(empty);

        } else {

            for (int i = 0; i < students.size(); i++) {

                final Student student = students.get(i);

                LinearLayout card = new LinearLayout(this);

                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(dp(15), dp(10), dp(15), dp(10));
                card.setBackgroundColor(Color.WHITE);

                LinearLayout.LayoutParams cp =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                cp.setMargins(dp(10), dp(5), dp(10), dp(5));

                card.setLayoutParams(cp);

                TextView name = text(
                        (i + 1) + ". " + student.name,
                        20
                );

                name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

                card.addView(name);

                TextView cls = text(
                        "الشعبة: " +
                                (student.className.length() == 0
                                        ? "غير محددة"
                                        : student.className),
                        17
                );

                card.addView(cls);

                Button delete = new Button(this);

                delete.setText("حذف الطالب");
                delete.setTextSize(15);
                delete.setAllCaps(false);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDeleteStudent(student);
                    }
                });

                card.addView(delete);

                root.addView(card);
            }
        }

        setContentView(scroll(root));
    }

    private void addStudentDialog() {

        LinearLayout box = new LinearLayout(this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(25), dp(5), dp(25), 0);

        final EditText name = new EditText(this);

        name.setHint("اسم الطالب");
        name.setTextSize(18);
        name.setSingleLine(true);

        box.addView(name);

        final EditText className = new EditText(this);

        className.setHint("الشعبة (مثال: أ)");
        className.setTextSize(18);
        className.setSingleLine(true);

        box.addView(className);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(box)
                .setNegativeButton("إلغاء", null)
                .setPositiveButton("حفظ", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {

                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                String n = name.getText().toString().trim();
                                String c = className.getText().toString().trim();

                                if (n.length() == 0) {
                                    name.setError("اكتب اسم الطالب");
                                    return;
                                }

                                Student s = new Student(n, c);

                                students.add(s);

                                if (c.length() > 0 && !classes.contains(c)) {
                                    classes.add(c);
                                }

                                saveData();

                                dialog.dismiss();

                                Toast.makeText(
                                        MainActivity.this,
                                        "تمت إضافة الطالب بنجاح",
                                        Toast.LENGTH_SHORT
                                ).show();

                                showStudents();
                            }
                        });
            }
        });

        dialog.show();
    }

    private void confirmDeleteStudent(final Student student) {

        new AlertDialog.Builder(this)
                .setTitle("حذف الطالب")
                .setMessage(
                        "هل تريد حذف الطالب:\n\n" +
                                student.name +
                                "\n\nسيتم حذف درجاته أيضًا."
                )
                .setNegativeButton("إلغاء", null)
                .setPositiveButton("حذف", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        students.remove(student);

                        saveData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حذف الطالب",
                                Toast.LENGTH_SHORT
                        ).show();

                        showStudents();
                    }
                })
                .show();
    }

    // =========================================================
    // CLASSES
    // =========================================================

    private void showClasses() {

        root = createRoot();

        root.addView(header("الشعب الدراسية"));

        root.addView(backButton());

        Button add = button("＋ إضافة شعبة");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClassDialog();
            }
        });

        root.addView(add);

        if (classes.size() == 0) {

            root.addView(text(
                    "لا توجد شعب دراسية حاليًا",
                    19
            ));

        } else {

            for (int i = 0; i < classes.size(); i++) {

                final String className = classes.get(i);

                LinearLayout row = new LinearLayout(this);

                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(dp(15), dp(8), dp(15), dp(8));

                LinearLayout.LayoutParams rp =
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                rp.setMargins(dp(10), dp(5), dp(10), dp(5));

                row.setLayoutParams(rp);

                TextView name = text(
                        "الشعبة: " + className,
                        19
                );

                LinearLayout.LayoutParams np =
                        new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1
                        );

                name.setLayoutParams(np);

                row.addView(name);

                Button delete = new Button(this);

                delete.setText("حذف");
                delete.setAllCaps(false);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("حذف الشعبة")
                                .setMessage(
                                        "هل تريد حذف الشعبة: " +
                                                className + " ؟"
                                )
                                .setNegativeButton("إلغاء", null)
                                .setPositiveButton(
                                        "حذف",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                classes.remove(className);

                                                saveData();

                                                showClasses();
                                            }
                                        }
                                )
                                .show();
                    }
                });

                row.addView(delete);

                root.addView(row);
            }
        }

        setContentView(scroll(root));
    }

    private void addClassDialog() {

        final EditText input = new EditText(this);

        input.setHint("اسم الشعبة");
        input.setTextSize(18);
        input.setSingleLine(true);
        input.setPadding(dp(20), dp(10), dp(20), dp(10));

        new AlertDialog.Builder(this)
                .setTitle("إضافة شعبة")
     

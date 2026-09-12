package com.ameer.gradebook;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.*;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import org.json.*;
import java.util.*;

public class MainActivity extends Activity {

    SharedPreferences p;
    JSONArray students = new JSONArray();
    JSONArray classes = new JSONArray();
    LinearLayout root;

    int BLUE = Color.rgb(24,72,130);
    int DARK = Color.rgb(10,38,72);
    int BG = Color.rgb(246,248,251);
    int RED = Color.rgb(190,45,45);

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        p = getSharedPreferences(
                "gradebook",
                MODE_PRIVATE);

        load();
        home();
    }

    void load() {
        try {
            students = new JSONArray(
                    p.getString("students","[]"));

            classes = new JSONArray(
                    p.getString("classes","[]"));

        } catch(Exception e) {

            students = new JSONArray();
            classes = new JSONArray();
        }
    }

    void save() {
        p.edit()
                .putString(
                        "students",
                        students.toString())
                .putString(
                        "classes",
                        classes.toString())
                .apply();
    }

    void base() {
        ScrollView s =
                new ScrollView(this);

        root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL);

        root.setPadding(
                20,20,20,35);

        root.setBackgroundColor(BG);

        s.addView(root);

        setContentView(s);
    }

    TextView text(
            String t,
            int size,
            boolean bold) {

        TextView v =
                new TextView(this);

        v.setText(t);
        v.setTextSize(size);
        v.setTextColor(Color.DKGRAY);

        v.setPadding(
                8,10,8,10);

        if(bold) {
            v.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD);
        }

        return v;
    }

    void title(String t) {

        TextView v =
                text(t,22,true);

        v.setTextColor(Color.WHITE);

        v.setGravity(
                Gravity.CENTER);

        v.setPadding(
                10,28,10,28);

        v.setBackgroundColor(DARK);

        root.addView(
                v,
                new LinearLayout.LayoutParams(
                        -1,
                        -2));
    }

    Button btn(String t) {

        Button b =
                new Button(this);

        b.setText(t);
        b.setTextSize(16);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);
        b.setBackgroundColor(BLUE);

        LinearLayout.LayoutParams x =
                new LinearLayout.LayoutParams(
                        -1,
                        58);

        x.setMargins(
                0,6,0,6);

        root.addView(b,x);

        return b;
    }

    EditText input(String hint) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);

        e.setPadding(
                12,5,12,5);

        LinearLayout.LayoutParams x =
                new LinearLayout.LayoutParams(
                        -1,
                        60);

        x.setMargins(
                0,5,0,5);

        e.setLayoutParams(x);

        return e;
    }

    EditText gradeInput(String hint) {

        EditText e =
                input(hint);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);

        return e;
    }

    LinearLayout card() {

        LinearLayout c =
                new LinearLayout(this);

        c.setOrientation(
                LinearLayout.VERTICAL);

        c.setPadding(
                16,12,16,12);

        c.setBackgroundColor(
                Color.WHITE);

        LinearLayout.LayoutParams x =
                new LinearLayout.LayoutParams(
                        -1,
                        -2);

        x.setMargins(
                0,6,0,6);

        c.setLayoutParams(x);

        return c;
    }

    void addCard(LinearLayout c) {
        root.addView(c);
    }

    void cardText(
            LinearLayout c,
            String t,
            int size,
            boolean bold) {

        c.addView(
                text(
                        t,
                        size,
                        bold));
    }

    void home() {

        base();

        title(
                "سجل درجات الاجتماعيات");

        TextView h =
                text(
                        "الأستاذ أمير محمد\n" +
                        "نظام إدارة درجات الطلاب",
                        18,
                        true);

        h.setGravity(
                Gravity.CENTER);

        root.addView(h);

        Button a =
                btn("👨‍🎓 إدارة الطلاب");

        a.setOnClickListener(
                v -> students());

        Button b =
                btn("🏫 إدارة الصفوف");

        b.setOnClickListener(
                v -> classes());

        Button c =
                btn("📝 إدخال الدرجات");

        c.setOnClickListener(
                v -> grades());

        Button d =
                btn("📋 سجل الدرجات");

        d.setOnClickListener(
                v -> records());

        Button e =
                btn("🏆 ترتيب الطلاب");

        e.setOnClickListener(
                v -> ranking());

        Button f =
                btn("📊 التقارير");

        f.setOnClickListener(
                v -> reports());

        Button g =
                btn("💾 النسخ الاحتياطي");

        g.setOnClickListener(
                v -> backup());

        Button z =
                btn("⚙️ الإعدادات");

        z.setOnClickListener(
                v -> settings());

        root.addView(
                text(
                        "عدد الطلاب: " +
                        students.length() +
                        "\nعدد الصفوف: " +
                        classes.length(),
                        16,
                        true));
    }

    ArrayList<String> classList() {

        ArrayList<String> a =
                new ArrayList<>();

        for(int i=0;
            i<classes.length();
            i++) {

            try {
                a.add(
                        classes.getString(i));

            } catch(Exception e) {}
        }

        if(a.size()==0) {
            a.add("الصف الأول");
        }

        return a;
    }

    Spinner spinner() {

        Spinner s =
                new Spinner(this);

        ArrayAdapter<String> a =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_item,
                        classList());

        a.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        s.setAdapter(a);

        return s;
    }

    void students() {

        base();

        title("إدارة الطلاب");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        Button add =
                btn("＋ إضافة طالب");

        add.setOnClickListener(
                v -> addStudent());

        if(students.length()==0) {

            root.addView(
                    text(
                            "لا يوجد طلاب.",
                            17,
                            false));

            return;
        }

        for(int i=0;
            i<students.length();
            i++) {

            try {

                JSONObject s =
                        students.getJSONObject(i);

                final int n = i;

                LinearLayout c =
                        card();

                cardText(
                        c,
                        s.optString("name"),
                        19,
                        true);

                cardText(
                        c,
                        "المدرسة: " +
                        s.optString("school"),
                        15,
                        false);

                cardText(
                        c,
                        "الصف: " +
                        s.optString("className"),
                        15,
                        false);

                Button edit =
                        new Button(this);

                edit.setText(
                        "✏️ تعديل");

                edit.setOnClickListener(
                        v -> editStudent(n));

                Button del =
                        new Button(this);

                del.setText(
                        "🗑️ حذف");

                del.setTextColor(RED);

                del.setOnClickListener(
                        v -> deleteStudent(n));

                c.addView(edit);
                c.addView(del);

                addCard(c);

            } catch(Exception e) {}
        }
    }

    void addStudent() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL);

        box.setPadding(
                25,5,25,5);

        EditText name =
                input("اسم الطالب");

        EditText school =
                input("اسم المدرسة");

        Spinner sp =
                spinner();

        box.addView(name);
        box.addView(school);
        box.addView(sp);

        new AlertDialog.Builder(this)
                .setTitle("إضافة طالب")
                .setView(box)
                .setPositiveButton(
                        "حفظ",
                        (d,w) -> {

                            String n =
                                    name.getText()
                                            .toString()
                                            .trim();

                            if(n.isEmpty()) {

                                toast(
                                        "أدخل اسم الطالب");

                                return;
                            }

                            try {

                                JSONObject s =
                                        new JSONObject();

                                s.put(
                                        "name",
                                        n);

                                s.put(
                                        "school",
                                        school.getText()
                                                .toString()
                                                .trim());

                                s.put(
                                        "className",
                                        sp.getSelectedItem()
                                                .toString());

                                s.put(
                                        "monthly",
                                        0);

                                s.put(
                                        "mid",
                                        0);

                                s.put(
                                        "second",
                                        0);

                                s.put(
                                        "final",
                                        0);

                                students.put(s);

                                save();

                                students();

                                toast(
                                        "تمت إضافة الطالب");

                            } catch(Exception e) {

                                toast(
                                        "حدث خطأ");
                            }
                        })
                .setNegativeButton(
                        "إلغاء",
                        null)
                .show();
    }

    void editStudent(
            final int i) {

        try {

            JSONObject s =
                    students.getJSONObject(i);

            LinearLayout box =
                    new LinearLayout(this);

            box.setOrientation(
                    LinearLayout.VERTICAL);

            box.setPadding(
                    25,5,25,5);

            EditText name =
                    input("اسم الطالب");

            name.setText(
                    s.optString("name"));

            EditText school =
                    input("اسم المدرسة");

            school.setText(
                    s.optString("school"));

            Spinner sp =
                    spinner();

            for(int x=0;
                x<sp.getCount();
                x++) {

                if(sp.getItemAtPosition(x)
                        .toString()
                        .equals(
                                s.optString(
                                        "className"))) {

                    sp.setSelection(x);

                    break;
                }
            }

            box.addView(name);
            box.addView(school);
            box.addView(sp);

            new AlertDialog.Builder(this)
                    .setTitle(
                            "تعديل الطالب")
                    .setView(box)
                    .setPositiveButton(
                            "حفظ",
                            (d,w) -> {

                                try {

                                    s.put(
                                            "name",
                                            name.getText()
                                                    .toString()
                                                    .trim());

                                    s.put(
                                            "school",
                                            school.getText()
                                                    .toString()
                                                    .trim());

                                    s.put(
                                            "className",
                                            sp.getSelectedItem()
                                                    .toString());

                                    save();

                                    students();

                                } catch(Exception e) {

                                    toast(
                                            "حدث خطأ");
                                }
                            })
                    .setNegativeButton(
                            "إلغاء",
                            null)
                    .show();

        } catch(Exception e) {

            toast(
                    "تعذر فتح الطالب");
        }
    }

    void deleteStudent(
            final int index) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الطالب")
                .setMessage(
                        "هل تريد حذف هذا الطالب؟")
                .setPositiveButton(
                        "حذف",
                        (d,w) -> {

                            try {

                                JSONArray a =
                                        new JSONArray();

                                for(int i=0;
                                    i<students.length();
                                    i++) {

                                    if(i != index) {

                                        a.put(
                                                students
                                                        .getJSONObject(i));
                                    }
                                }

                                students = a;

                                save();

                                students();

                            } catch(Exception e) {

                                toast(
                                        "حدث خطأ");
                            }
                        })
                .setNegativeButton(
                        "إلغاء",
                        null)
                .show();
    }

    /    void classes() {

        base();

        title("إدارة الصفوف");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        Button add =
                btn("＋ إضافة صف");

        add.setOnClickListener(
                v -> addClass());

        if(classes.length()==0) {

            root.addView(
                    text(
                            "لا توجد صفوف مضافة.",
                            17,
                            false));

            return;
        }

        for(int i=0;
            i<classes.length();
            i++) {

            try {

                final int n = i;

                String name =
                        classes.getString(i);

                LinearLayout c =
                        card();

                cardText(
                        c,
                        name,
                        19,
                        true);

                int count = 0;

                for(int j=0;
                    j<students.length();
                    j++) {

                    try {

                        JSONObject s =
                                students.getJSONObject(j);

                        if(name.equals(
                                s.optString(
                                        "className"))) {

                            count++;
                        }

                    } catch(Exception e) {}
                }

                cardText(
                        c,
                        "عدد الطلاب: " +
                        count,
                        15,
                        false);

                Button edit =
                        new Button(this);

                edit.setText(
                        "✏️ تعديل اسم الصف");

                edit.setOnClickListener(
                        v -> editClass(n));

                Button del =
                        new Button(this);

                del.setText(
                        "🗑️ حذف الصف");

                del.setTextColor(RED);

                del.setOnClickListener(
                        v -> deleteClass(n));

                c.addView(edit);
                c.addView(del);

                addCard(c);

            } catch(Exception e) {}
        }
    }

    void addClass() {

        EditText name =
                input("اسم الصف");

        new AlertDialog.Builder(this)
                .setTitle(
                        "إضافة صف")
                .setView(name)
                .setPositiveButton(
                        "حفظ",
                        (d,w) -> {

                            String n =
                                    name.getText()
                                            .toString()
                                            .trim();

                            if(n.isEmpty()) {

                                toast(
                                        "أدخل اسم الصف");

                                return;
                            }

                            try {

                                boolean exists =
                                        false;

                                for(int i=0;
                                    i<classes.length();
                                    i++) {

                                    if(n.equals(
                                            classes.getString(i))) {

                                        exists = true;
                                        break;
                                    }
                                }

                                if(exists) {

                                    toast(
                                            "هذا الصف موجود مسبقاً");

                                    return;
                                }

                                classes.put(n);

                                save();

                                classes();

                                toast(
                                        "تمت إضافة الصف");

                            } catch(Exception e) {

                                toast(
                                        "حدث خطأ");
                            }
                        })
                .setNegativeButton(
                        "إلغاء",
                        null)
                .show();
    }

    void editClass(
            final int index) {

        try {

            String oldName =
                    classes.getString(index);

            EditText name =
                    input("اسم الصف");

            name.setText(oldName);

            new AlertDialog.Builder(this)
                    .setTitle(
                            "تعديل الصف")
                    .setView(name)
                    .setPositiveButton(
                            "حفظ",
                            (d,w) -> {

                                try {

                                    String newName =
                                            name.getText()
                                                    .toString()
                                                    .trim();

                                    if(newName.isEmpty()) {

                                        toast(
                                                "أدخل اسم الصف");

                                        return;
                                    }

                                    if(!oldName.equals(
                                            newName)) {

                                        for(int i=0;
                                            i<classes.length();
                                            i++) {

                                            if(newName.equals(
                                                    classes.getString(i))) {

                                                toast(
                                                        "هذا الصف موجود مسبقاً");

                                                return;
                                            }
                                        }
                                    }

                                    classes.put(
                                            index,
                                            newName);

                                    for(int i=0;
                                        i<students.length();
                                        i++) {

                                        JSONObject s =
                                                students
                                                        .getJSONObject(i);

                                        if(oldName.equals(
                                                s.optString(
                                                        "className"))) {

                                            s.put(
                                                    "className",
                                                    newName);
                                        }
                                    }

                                    save();

                                    classes();

                                } catch(Exception e) {

                                    toast(
                                            "حدث خطأ");
                                }
                            })
                    .setNegativeButton(
                            "إلغاء",
                            null)
                    .show();

        } catch(Exception e) {

            toast(
                    "تعذر تعديل الصف");
        }
    }

    void deleteClass(
            final int index) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الصف")
                .setMessage(
                        "سيتم حذف الصف من القائمة، " +
                        "ولن يتم حذف الطلاب. هل تريد المتابعة؟")
                .setPositiveButton(
                        "حذف",
                        (d,w) -> {

                            try {

                                String name =
                                        classes.getString(index);

                                JSONArray a =
                                        new JSONArray();

                                for(int i=0;
                                    i<classes.length();
                                    i++) {

                                    if(i != index) {

                                        a.put(
                                                classes.getString(i));
                                    }
                                }

                                classes = a;

                                for(int i=0;
                                    i<students.length();
                                    i++) {

                                    JSONObject s =
                                            students
                                                    .getJSONObject(i);

                                    if(name.equals(
                                            s.optString(
                                                    "className"))) {

                                        s.put(
                                                "className",
                                                "");
                                    }
                                }

                                save();

                                classes();

                            } catch(Exception e) {

                                toast(
                                        "حدث خطأ");
                            }
                        })
                .setNegativeButton(
                        "إلغاء",
                        null)
                .show();
    }

    void grades() {

        base();

        title("إدخال الدرجات");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        if(students.length()==0) {

            root.addView(
                    text(
                            "أضف الطلاب أولاً من إدارة الطلاب.",
                            17,
                            true));

            return;
        }

        for(int i=0;
            i<students.length();
            i++) {

            try {

                final int n = i;

                JSONObject s =
                        students.getJSONObject(i);

                LinearLayout c =
                        card();

                cardText(
                        c,
                        s.optString("name"),
                        19,
                        true);

                cardText(
                        c,
                        "الصف: " +
                        s.optString("className"),
                        15,
                        false);

                Button edit =
                        new Button(this);

                edit.setText(
                        "📝 إدخال / تعديل الدرجات");

                edit.setOnClickListener(
                        v -> gradeStudent(n));

                c.addView(edit);

                addCard(c);

            } catch(Exception e) {}
        }
    }

    void gradeStudent(
            final int index) {

        try {

            JSONObject s =
                    students.getJSONObject(index);

            LinearLayout box =
                    new LinearLayout(this);

            box.setOrientation(
                    LinearLayout.VERTICAL);

            box.setPadding(
                    25,5,25,5);

            EditText monthly =
                    gradeInput(
                            "درجة الشهر الأول");

            EditText mid =
                    gradeInput(
                            "درجة نصف السنة");

            EditText second =
                    gradeInput(
                            "درجة الشهر الثاني");

            EditText fin =
                    gradeInput(
                            "الدرجة النهائية");

            monthly.setText(
                    String.valueOf(
                            s.optDouble(
                                    "monthly",
                                    0)));

            mid.setText(
                    String.valueOf(
                            s.optDouble(
                                    "mid",
                                    0)));

            second.setText(
                    String.valueOf(
                            s.optDouble(
                                    "second",
                                    0)));

            fin.setText(
                    String.valueOf(
                            s.optDouble(
                                    "final",
                                    0)));

            box.addView(
                    text(
                            "الطالب: " +
                            s.optString("name"),
                            18,
                            true));

            box.addView(monthly);
            box.addView(mid);
            box.addView(second);
            box.addView(fin);

            new AlertDialog.Builder(this)
                    .setTitle(
                            "درجات الطالب")
                    .setView(box)
                    .setPositiveButton(
                            "حفظ",
                            (d,w) -> {

                                try {

                                    s.put(
                                            "monthly",
                                            number(monthly));

                                    s.put(
                                            "mid",
                                            number(mid));

                                    s.put(
                                            "second",
                                            number(second));

                                    s.put(
                                            "final",
                                            number(fin));

                                    save();

                                    toast(
                                            "تم حفظ الدرجات");

                                } catch(Exception e) {

                                    toast(
                                            "حدث خطأ في الدرجات");
                                }
                            })
                    .setNegativeButton(
                            "إلغاء",
                            null)
                    .show();

        } catch(Exception e) {

            toast(
                    "تعذر فتح درجات الطالب");
        }
    }

    double number(EditText e) {

        try {

            String x =
                    e.getText()
                            .toString()
                            .trim();

            if(x.isEmpty()) {
                return 0;
            }

            double n =
                    Double.parseDouble(x);

            if(n < 0) {
                n = 0;
            }

            if(n > 100) {
                n = 100;
            }

            return n;

        } catch(Exception ex) {

            return 0;
        }
    }

    //     void records() {

        base();

        title("سجل الدرجات");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        if(students.length()==0) {

            root.addView(
                    text(
                            "لا يوجد طلاب.",
                            17,
                            false));

            return;
        }

        for(int i=0;
            i<students.length();
            i++) {

            try {

                JSONObject s =
                        students.getJSONObject(i);

                LinearLayout c =
                        card();

                double m =
                        s.optDouble(
                                "monthly",
                                0);

                double mid =
                        s.optDouble(
                                "mid",
                                0);

                double sec =
                        s.optDouble(
                                "second",
                                0);

                double fin =
                        s.optDouble(
                                "final",
                                0);

                double total =
                        m + mid + sec + fin;

                double avg =
                        total / 4.0;

                cardText(
                        c,
                        s.optString("name"),
                        19,
                        true);

                cardText(
                        c,
                        "الصف: " +
                        s.optString("className"),
                        15,
                        false);

                cardText(
                        c,
                        "الشهر الأول: " +
                        fmt(m),
                        15,
                        false);

                cardText(
                        c,
                        "نصف السنة: " +
                        fmt(mid),
                        15,
                        false);

                cardText(
                        c,
                        "الشهر الثاني: " +
                        fmt(sec),
                        15,
                        false);

                cardText(
                        c,
                        "النهائية: " +
                        fmt(fin),
                        15,
                        false);

                cardText(
                        c,
                        "المجموع: " +
                        fmt(total),
                        16,
                        true);

                cardText(
                        c,
                        "المعدل: " +
                        fmt(avg),
                        17,
                        true);

                addCard(c);

            } catch(Exception e) {}
        }
    }

    String fmt(double n) {

        if(n == (long)n) {

            return String.valueOf(
                    (long)n);
        }

        return String.format(
                Locale.US,
                "%.2f",
                n);
    }

    void ranking() {

        base();

        title("ترتيب الطلاب");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        if(students.length()==0) {

            root.addView(
                    text(
                            "لا يوجد طلاب.",
                            17,
                            false));

            return;
        }

        ArrayList<JSONObject> list =
                new ArrayList<>();

        for(int i=0;
            i<students.length();
            i++) {

            try {

                list.add(
                        students.getJSONObject(i));

            } catch(Exception e) {}
        }

        Collections.sort(
                list,
                new Comparator<JSONObject>() {

                    @Override
                    public int compare(
                            JSONObject a,
                            JSONObject b) {

                        double aa =
                                average(a);

                        double bb =
                                average(b);

                        return Double.compare(
                                bb,
                                aa);
                    }
                });

        for(int i=0;
            i<list.size();
            i++) {

            JSONObject s =
                    list.get(i);

            LinearLayout c =
                    card();

            double avg =
                    average(s);

            cardText(
                    c,
                    "المركز " +
                    (i+1),
                    16,
                    true);

            cardText(
                    c,
                    s.optString("name"),
                    19,
                    true);

            cardText(
                    c,
                    "الصف: " +
                    s.optString("className"),
                    15,
                    false);

            cardText(
                    c,
                    "المعدل: " +
                    fmt(avg),
                    18,
                    true);

            addCard(c);
        }
    }

    double average(
            JSONObject s) {

        double a =
                s.optDouble(
                        "monthly",
                        0);

        double b =
                s.optDouble(
                        "mid",
                        0);

        double c =
                s.optDouble(
                        "second",
                        0);

        double d =
                s.optDouble(
                        "final",
                        0);

        return (a+b+c+d)/4.0;
    }

    void reports() {

        base();

        title("التقارير");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        int totalStudents =
                students.length();

        double sum = 0;

        int success = 0;

        int failed = 0;

        for(int i=0;
            i<students.length();
            i++) {

            try {

                JSONObject s =
                        students.getJSONObject(i);

                double avg =
                        average(s);

                sum += avg;

                if(avg >= 50) {

                    success++;

                } else {

                    failed++;
                }

            } catch(Exception e) {}
        }

        double general = 0;

        if(totalStudents > 0) {

            general =
                    sum / totalStudents;
        }

        LinearLayout c =
                card();

        cardText(
                c,
                "إحصائيات عامة",
                20,
                true);

        cardText(
                c,
                "عدد الطلاب: " +
                totalStudents,
                17,
                false);

        cardText(
                c,
                "عدد الصفوف: " +
                classes.length(),
                17,
                false);

        cardText(
                c,
                "الناجحون: " +
                success,
                17,
                false);

        cardText(
                c,
                "الراسبون: " +
                failed,
                17,
                false);

        cardText(
                c,
                "المعدل العام: " +
                fmt(general),
                18,
                true);

        addCard(c);

        for(int i=0;
            i<classes.length();
            i++) {

            try {

                String className =
                        classes.getString(i);

                int count = 0;

                int pass = 0;

                double sumClass = 0;

                for(int j=0;
                    j<students.length();
                    j++) {

                    JSONObject s =
                            students.getJSONObject(j);

                    if(className.equals(
                            s.optString(
                                    "className"))) {

                        count++;

                        double avg =
                                average(s);

                        sumClass += avg;

                        if(avg >= 50) {

                            pass++;
                        }
                    }
                }

                double classAvg = 0;

                if(count > 0) {

                    classAvg =
                            sumClass / count;
                }

                LinearLayout x =
                        card();

                cardText(
                        x,
                        className,
                        19,
                        true);

                cardText(
                        x,
                        "عدد الطلاب: " +
                        count,
                        15,
                        false);

                cardText(
                        x,
                        "الناجحون: " +
                        pass,
                        15,
                        false);

                cardText(
                        x,
                        "الراسبون: " +
                        (count-pass),
                        15,
                        false);

                cardText(
                        x,
                        "معدل الصف: " +
                        fmt(classAvg),
                        17,
                        true);

                addCard(x);

            } catch(Exception e) {}
        }
    }

    //     void backup() {

        base();

        title("النسخ الاحتياطي");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        LinearLayout c =
                card();

        cardText(
                c,
                "نسخة احتياطية للبيانات",
                20,
                true);

        cardText(
                c,
                "يمكنك حفظ بيانات الطلاب والصفوف " +
                "في ملف نصي ومشاركته.",
                16,
                false);

        Button export =
                btn("📤 تصدير نسخة احتياطية");

        export.setOnClickListener(
                v -> exportBackup());

        Button restore =
                btn("📥 استيراد نسخة احتياطية");

        restore.setOnClickListener(
                v -> toast(
                        "سيتم تفعيل الاستيراد في الإصدار القادم"));

        addCard(c);
    }

    void exportBackup() {

        try {

            JSONObject data =
                    new JSONObject();

            data.put(
                    "students",
                    students);

            data.put(
                    "classes",
                    classes);

            Intent share =
                    new Intent(
                            Intent.ACTION_SEND);

            share.setType(
                    "text/plain");

            share.putExtra(
                    Intent.EXTRA_TEXT,
                    data.toString(2));

            startActivity(
                    Intent.createChooser(
                            share,
                            "حفظ أو مشاركة النسخة الاحتياطية"));

        } catch(Exception e) {

            toast(
                    "تعذر إنشاء النسخة الاحتياطية");
        }
    }

    void settings() {

        base();

        title("الإعدادات");

        Button back =
                btn("← الرئيسية");

        back.setOnClickListener(
                v -> home());

        LinearLayout c =
                card();

        cardText(
                c,
                "إعدادات سجل الدرجات",
                20,
                true);

        cardText(
                c,
                "اسم النظام: سجل درجات الاجتماعيات",
                16,
                false);

        cardText(
                c,
                "الأستاذ: أمير محمد",
                16,
                false);

        cardText(
                c,
                "عدد الطلاب المسجلين: " +
                students.length(),
                16,
                false);

        cardText(
                c,
                "عدد الصفوف: " +
                classes.length(),
                16,
                false);

        addCard(c);

        Button clear =
                btn("⚠️ مسح جميع البيانات");

        clear.setTextColor(
                Color.WHITE);

        clear.setOnClickListener(
                v -> confirmClear());

        root.addView(clear);
    }

    void confirmClear() {

        new AlertDialog.Builder(this)

                .setTitle(
                        "مسح جميع البيانات")

                .setMessage(
                        "تحذير: سيتم حذف جميع الطلاب " +
                        "والصفوف والدرجات من التطبيق. " +
                        "هل أنت متأكد؟")

                .setPositiveButton(
                        "حذف الكل",
                        (d,w) -> {

                            students =
                                    new JSONArray();

                            classes =
                                    new JSONArray();

                            save();

                            home();

                            toast(
                                    "تم مسح جميع البيانات");
                        })

                .setNegativeButton(
                        "إلغاء",
                        null)

                .show();
    }

    void toast(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    //     // ===== القسم 5 والأخير =====

    @Override
    public void onBackPressed() {

        home();
    }

}
// ===== نهاية MainActivity.java =====

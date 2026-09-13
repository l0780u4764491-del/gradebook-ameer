package com.ameer.gradebook;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {

    // =========================================================
    // GradeBook
    // القسم 1
    // =========================================================

    private static final String PREFS =
            "GradeBookData";

    // ألوان التطبيق
    private static final int BACKGROUND =
            Color.rgb(246, 248, 252);

    private static final int CARD =
            Color.WHITE;

    private static final int PRIMARY =
            Color.rgb(36, 92, 172);

    private static final int PRIMARY_DARK =
            Color.rgb(25, 67, 130);

    private static final int SUCCESS =
            Color.rgb(24, 134, 82);

    private static final int WARNING =
            Color.rgb(232, 155, 25);

    private static final int DANGER =
            Color.rgb(210, 60, 72);

    private static final int TEXT =
            Color.rgb(30, 41, 59);

    private static final int MUTED =
            Color.rgb(100, 116, 139);

    // عناصر الواجهة
    private LinearLayout root;
    private LinearLayout content;

    // التخزين
    private SharedPreferences prefs;

    // البيانات
    private final ArrayList<Student> students =
            new ArrayList<>();

    private final ArrayList<Subject> subjects =
            new ArrayList<>();

    private final Map<String, GradeRecord> grades =
            new HashMap<>();

    // الاختيارات الحالية
    private int selectedStudent = -1;
    private int selectedSubject = -1;

    // =========================================================
    // بداية التطبيق
    // =========================================================

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        prefs = getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        );

        loadBasicData();

        showHome();
    }

    // =========================================================
    // تحميل البيانات الأساسية
    // =========================================================

    private void loadBasicData() {

        students.clear();
        subjects.clear();

        int studentCount =
                prefs.getInt(
                        "students_count",
                        0
                );

        for (int i = 0; i < studentCount; i++) {

            String name =
                    prefs.getString(
                            "student_" + i + "_name",
                            ""
                    );

            String number =
                    prefs.getString(
                            "student_" + i + "_number",
                            ""
                    );

            String grade =
                    prefs.getString(
                            "student_" + i + "_grade",
                            ""
                    );

            String section =
                    prefs.getString(
                            "student_" + i + "_section",
                            ""
                    );

            if (!name.trim().isEmpty()) {

                students.add(
                        new Student(
                                name,
                                number,
                                grade,
                                section
                        )
                );
            }
        }

        int subjectCount =
                prefs.getInt(
                        "subjects_count",
                        0
                );

        for (int i = 0; i < subjectCount; i++) {

            String name =
                    prefs.getString(
                            "subject_" + i + "_name",
                            ""
                    );

            if (!name.trim().isEmpty()) {

                subjects.add(
                        new Subject(name)
                );
            }
        }
    }

    // =========================================================
    // حفظ البيانات الأساسية
    // =========================================================

    private void saveBasicData() {

        SharedPreferences.Editor e =
                prefs.edit();

        e.putInt(
                "students_count",
                students.size()
        );

        for (int i = 0; i < students.size(); i++) {

            Student s =
                    students.get(i);

            e.putString(
                    "student_" + i + "_name",
                    s.name
            );

            e.putString(
                    "student_" + i + "_number",
                    s.number
            );

            e.putString(
                    "student_" + i + "_grade",
                    s.grade
            );

            e.putString(
                    "student_" + i + "_section",
                    s.section
            );
        }

        e.putInt(
                "subjects_count",
                subjects.size()
        );

        for (int i = 0; i < subjects.size(); i++) {

            e.putString(
                    "subject_" + i + "_name",
                    subjects.get(i).name
            );
        }

        e.apply();
    }

    // =========================================================
    // إنشاء الواجهة الأساسية
    // =========================================================

    private void createBase() {

        root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                BACKGROUND
        );

        root.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        setContentView(root);

        // شريط العنوان
        LinearLayout toolbar =
                new LinearLayout(this);

        toolbar.setGravity(
                Gravity.CENTER_VERTICAL
        );

        toolbar.setPadding(
                18,
                14,
                18,
                14
        );

        toolbar.setBackgroundColor(
                PRIMARY
        );

        TextView icon =
                text(
                        "📚",
                        25,
                        Color.WHITE
                );

        icon.setGravity(
                Gravity.CENTER
        );

        toolbar.addView(
                icon,
                new LinearLayout.LayoutParams(
                        45,
                        45
                )
        );

        TextView title =
                text(
                        "سجل الدرجات",
                        20,
                        Color.WHITE
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        toolbar.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        root.addView(toolbar);

        // منطقة المحتوى
        ScrollView scroll =
                new ScrollView(this);

        content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                18,
                20,
                18,
                30
        );

        content.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );
    }

    // =========================================================
    // الشاشة الرئيسية
    // =========================================================

    private void showHome() {

        createBase();

        TextView heading =
                text(
                        "سجل الدرجات",
                        28,
                        TEXT
                );

        heading.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(heading);

        TextView sub =
                text(
                        "نظام متكامل لإدارة الطلاب والدرجات",
                        14,
                        MUTED
                );

        content.addView(sub);

        space(14);

        addWelcome();

        space(14);

        addStatistics();

        space(20);

        TextView management =
                text(
                        "الإدارة",
                        20,
                        TEXT
                );

        management.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(management);

        space(8);

        addMenu(
                "👨‍🎓 إدارة الطلاب",
                "إضافة وتعديل وحذف والبحث عن الطلاب",
                PRIMARY,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showStudents();
                    }
                }
        );

        addMenu(
                "📚 إدارة المواد",
                "إضافة المواد الدراسية وتنظيمها",
                SUCCESS,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSubjects();
                    }
                }
        );

        addMenu(
                "📝 إدخال الدرجات",
                "تسجيل الدرجات اليومية والشهرية",
                WARNING,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGradeSelection();
                    }
                }
        );

        addMenu(
                "📊 النتائج والتحليل",
                "المعدلات والترتيب والأوائل",
                PRIMARY_DARK,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showResults();
                    }
                }
        );

        addMenu(
                "📄 التقارير",
                "تقارير الطلاب وكشوف الدرجات",
                DANGER,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showReports();
                    }
                }
        );
    }

    // =========================================================
    // بطاقة الترحيب
    // =========================================================

    private void addWelcome() {

        LinearLayout card =
                makeCard();

        TextView t =
                text(
                        "مرحبًا بك 👋",
                        21,
                        PRIMARY
                );

        t.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(t);

        TextView d =
                text(
                        "تابع طلابك ودرجاتهم ونتائجهم من مكان واحد.",
                        14,
                        MUTED
                );

        card.addView(d);

        content.addView(card);
    }

    // =========================================================
    // نهاية القسم 1
    // ========================================================
        // =========================================================
    // القسم 2
    // الإحصائيات + البطاقات + نماذج البيانات
    // =========================================================

    private void addStatistics() {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        addStatistic(
                row,
                "الطلاب",
                String.valueOf(
                        students.size()
                ),
                PRIMARY
        );

        addStatistic(
                row,
                "المواد",
                String.valueOf(
                        subjects.size()
                ),
                SUCCESS
        );

        content.addView(row);
    }

    // =========================================================
    // بطاقة إحصائية
    // =========================================================

    private void addStatistic(
            LinearLayout parent,
            String label,
            String value,
            int color
    ) {

        LinearLayout card =
                makeCard();

        card.setGravity(
                Gravity.CENTER
        );

        TextView valueView =
                text(
                        value,
                        26,
                        color
                );

        valueView.setGravity(
                Gravity.CENTER
        );

        valueView.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(valueView);

        TextView labelView =
                text(
                        label,
                        13,
                        MUTED
                );

        labelView.setGravity(
                Gravity.CENTER
        );

        card.addView(labelView);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        params.setMargins(
                5,
                0,
                5,
                0
        );

        parent.addView(
                card,
                params
        );
    }

    // =========================================================
    // بطاقة قائمة
    // =========================================================

    private void addMenu(
            String title,
            String description,
            int color,
            View.OnClickListener listener
    ) {

        LinearLayout card =
                makeCard();

        card.setPadding(
                18,
                15,
                18,
                15
        );

        TextView titleView =
                text(
                        title,
                        18,
                        color
                );

        titleView.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(titleView);

        TextView descriptionView =
                text(
                        description,
                        13,
                        MUTED
                );

        descriptionView.setPadding(
                0,
                5,
                0,
                0
        );

        card.addView(
                descriptionView
        );

        card.setOnClickListener(
                listener
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

        content.addView(
                card,
                params
        );
    }

    // =========================================================
    // إنشاء بطاقة عامة
    // =========================================================

    private LinearLayout makeCard() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                CARD
        );

        background.setCornerRadius(
                22
        );

        background.setStroke(
                1,
                Color.rgb(
                        228,
                        232,
                        238
                )
        );

        card.setBackground(
                background
        );

        card.setElevation(3);

        card.setPadding(
                16,
                16,
                16,
                16
        );

        return card;
    }

    // =========================================================
    // إنشاء نص
    // =========================================================

    private TextView text(
            String value,
            float size,
            int color
    ) {

        TextView view =
                new TextView(this);

        view.setText(value);

        view.setTextSize(size);

        view.setTextColor(color);

        view.setGravity(
                Gravity.RIGHT
        );

        view.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        return view;
    }

    // =========================================================
    // إضافة مسافة
    // =========================================================

    private void space(int height) {

        View view =
                new View(this);

        content.addView(
                view,
                new LinearLayout.LayoutParams(
                        1,
                        height
                )
        );
    }

    // =========================================================
    // زر إجراء
    // =========================================================

    private Button actionButton(
            String title,
            int color
    ) {

        Button button =
                new Button(this);

        button.setText(title);

        button.setTextSize(15);

        button.setTextColor(
                Color.WHITE
        );

        button.setAllCaps(false);

        button.setGravity(
                Gravity.CENTER
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(color);

        background.setCornerRadius(
                18
        );

        button.setBackground(
                background
        );

        button.setPadding(
                8,
                10,
                8,
                10
        );

        return button;
    }

    // =========================================================
    // حقل إدخال
    // =========================================================

    private EditText input(
            String hint
    ) {

        EditText field =
                new EditText(this);

        field.setHint(hint);

        field.setTextSize(15);

        field.setTextColor(TEXT);

        field.setHintTextColor(
                MUTED
        );

        field.setGravity(
                Gravity.RIGHT
        );

        field.setSingleLine(true);

        field.setPadding(
                17,
                12,
                17,
                12
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                CARD
        );

        background.setCornerRadius(
                18
        );

        background.setStroke(
                1,
                Color.rgb(
                        220,
                        225,
                        232
                )
        );

        field.setBackground(
                background
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        params.setMargins(
                0,
                5,
                0,
                5
        );

        field.setLayoutParams(params);

        return field;
    }

    // =========================================================
    // نموذج الطالب
    // =========================================================

    private static class Student {

        String name;
        String number;
        String grade;
        String section;

        Student(
                String name,
                String number,
                String grade,
                String section
        ) {

            this.name = name;
            this.number = number;
            this.grade = grade;
            this.section = section;
        }
    }

    // =========================================================
    // نموذج المادة
    // =========================================================

    private static class Subject {

        String name;

        Subject(String name) {
            this.name = name;
        }
    }

    // =========================================================
    // نموذج سجل الدرجات
    // =========================================================

    private static class GradeRecord {

        double daily1;
        double daily2;
        double daily3;

        double activity;
        double behavior;

        double month;

        double monthAverage;

        double firstMonth;
        double secondMonth;

        double firstTerm;
        double secondTerm;

        double midYear;
        double annual;

        GradeRecord() {

            daily1 = 0;
            daily2 = 0;
            daily3 = 0;

            activity = 0;
            behavior = 0;

            month = 0;
            monthAverage = 0;

            firstMonth = 0;
            secondMonth = 0;

            firstTerm = 0;
            secondTerm = 0;

            midYear = 0;
            annual = 0;
        }
    }

    // =========================================================
    // معرف سجل الدرجة
    // =========================================================

    private String gradeKey(
            int studentIndex,
            int subjectIndex
    ) {

        return "grade_"
                + studentIndex
                + "_"
                + subjectIndex;
    }

    // =========================================================
    // الحصول على سجل درجات
    // =========================================================

    private GradeRecord getGradeRecord(
            int studentIndex,
            int subjectIndex
    ) {

        String key =
                gradeKey(
                        studentIndex,
                        subjectIndex
                );

        GradeRecord record =
                grades.get(key);

        if (record == null) {

            record =
                    new GradeRecord();

            grades.put(
                    key,
                    record
            );
        }

        return record;
    }

    // =========================================================
    // حساب مجموع اليومي
    // مطابق لمنطق Excel:
    // SUM(B:F)
    // =========================================================

    private double dailyTotal(
            GradeRecord r
    ) {

        return r.daily1
                + r.daily2
                + r.daily3
                + r.activity
                + r.behavior;
    }

    // =========================================================
    // حساب معدل الشهر
    // مطابق لمنطق Excel:
    // ROUND((G + H) / 2, 0)
    // =========================================================

    private double calculateMonthAverage(
            double dailyTotal,
            double month
    ) {

        return Math.round(
                (dailyTotal + month) / 2.0
        );
    }

    // =========================================================
    // حساب سعي الفصل
    // متوسط الشهر الأول والثاني
    // =========================================================

    private double calculateTerm(
            double firstMonth,
            double secondMonth
    ) {

        return Math.round(
                (firstMonth + secondMonth)
                        / 2.0
        );
    }

    // =========================================================
    // حساب السعي السنوي
    // الفصل الأول + نصف السنة + الفصل الثاني
    // =========================================================

    private double calculateAnnual(
            double firstTerm,
            double midYear,
            double secondTerm
    ) {

        double total =
                firstTerm
                        + midYear
                        + secondTerm;

        return Math.ceil(
                total
        ) / 3.0;
    }

    // =========================================================
    // نهاية القسم 2
    // =========================================================
    // =========================================================
    // القسم 3
    // شاشة إدارة الطلاب
    // إضافة وتعديل وحذف الطلاب
    // =========================================================

    private void showStudents() {

        createBase();

        TextView title =
                text(
                        "إدارة الطلاب",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "إدارة بيانات الطلاب وحفظها بشكل دائم",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(14);

        // -----------------------------------------------------
        // ملخص الطلاب
        // -----------------------------------------------------

        LinearLayout summary =
                makeCard();

        TextView summaryTitle =
                text(
                        "ملخص الطلاب",
                        18,
                        TEXT
                );

        summaryTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        summary.addView(summaryTitle);

        TextView total =
                text(
                        "إجمالي الطلاب: "
                                + students.size(),
                        14,
                        PRIMARY
                );

        summary.addView(total);

        TextView sections =
                text(
                        "عدد الشعب: "
                                + countSections(),
                        14,
                        MUTED
                );

        summary.addView(sections);

        content.addView(summary);

        space(10);

        // -----------------------------------------------------
        // زر إضافة طالب
        // -----------------------------------------------------

        Button add =
                actionButton(
                        "＋ إضافة طالب جديد",
                        PRIMARY
                );

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddStudent();
                    }
                }
        );

        content.addView(add);

        space(12);

        // -----------------------------------------------------
        // البحث
        // -----------------------------------------------------

        final android.widget.EditText search =
                input(
                        "🔍 البحث باسم الطالب أو الرقم"
                );

        content.addView(search);

        space(10);

        // -----------------------------------------------------
        // قائمة الطلاب
        // -----------------------------------------------------

        if (students.isEmpty()) {

            LinearLayout empty =
                    makeCard();

            empty.setGravity(
                    Gravity.CENTER
            );

            TextView emptyTitle =
                    text(
                            "لا توجد طلاب مضافة",
                            18,
                            TEXT
                    );

            emptyTitle.setGravity(
                    Gravity.CENTER
            );

            emptyTitle.setTypeface(
                    null,
                    Typeface.BOLD
            );

            empty.addView(emptyTitle);

            TextView emptyInfo =
                    text(
                            "ابدأ بإضافة أول طالب إلى السجل",
                            14,
                            MUTED
                    );

            emptyInfo.setGravity(
                    Gravity.CENTER
            );

            empty.addView(emptyInfo);

            content.addView(empty);

        } else {

            for (int i = 0;
                 i < students.size();
                 i++) {

                addStudentItem(
                        i,
                        students.get(i)
                );
            }
        }

        // -----------------------------------------------------
        // البحث المباشر
        // -----------------------------------------------------

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

                        filterStudents(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s
                    ) {
                    }
                }
        );
    }

    // =========================================================
    // حساب عدد الشعب
    // =========================================================

    private int countSections() {

        ArrayList<String> list =
                new ArrayList<>();

        for (Student student : students) {

            if (student.section == null) {
                continue;
            }

            String section =
                    student.section.trim();

            if (section.isEmpty()) {
                continue;
            }

            if (!list.contains(section)) {
                list.add(section);
            }
        }

        return list.size();
    }

    // =========================================================
    // عنصر طالب
    // =========================================================

    private void addStudentItem(
            final int index,
            final Student student
    ) {

        LinearLayout card =
                makeCard();

        card.setPadding(
                18,
                16,
                18,
                16
        );

        TextView name =
                text(
                        student.name,
                        18,
                        TEXT
                );

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(name);

        TextView number =
                text(
                        "رقم الطالب: "
                                + student.number,
                        13,
                        MUTED
                );

        card.addView(number);

        TextView classInfo =
                text(
                        "الصف: "
                                + student.grade
                                + "   •   الشعبة: "
                                + student.section,
                        13,
                        MUTED
                );

        card.addView(classInfo);

        LinearLayout buttons =
                new LinearLayout(this);

        buttons.setOrientation(
                LinearLayout.HORIZONTAL
        );

        buttons.setGravity(
                Gravity.CENTER
        );

        Button edit =
                actionButton(
                        "تعديل",
                        PRIMARY
                );

        Button delete =
                actionButton(
                        "حذف",
                        DANGER
                );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        p.setMargins(
                4,
                8,
                4,
                0
        );

        buttons.addView(
                edit,
                p
        );

        buttons.addView(
                delete,
                p
        );

        card.addView(buttons);

        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedStudent =
                                index;

                        showEditStudent(
                                index
                        );
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

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                0,
                5,
                0,
                5
        );

        content.addView(
                card,
                cp
        );
    }

    // =========================================================
    // إضافة طالب
    // =========================================================

    private void showAddStudent() {

        createBase();

        TextView title =
                text(
                        "إضافة طالب جديد",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView info =
                text(
                        "أدخل بيانات الطالب",
                        14,
                        MUTED
                );

        content.addView(info);

        space(14);

        final android.widget.EditText name =
                input(
                        "اسم الطالب الثلاثي"
                );

        final android.widget.EditText number =
                input(
                        "رقم الطالب"
                );

        final android.widget.EditText grade =
                input(
                        "الصف"
                );

        final android.widget.EditText section =
                input(
                        "الشعبة"
                );

        content.addView(name);
        content.addView(number);
        content.addView(grade);
        content.addView(section);

        space(12);

        Button save =
                actionButton(
                        "حفظ الطالب",
                        SUCCESS
                );

        content.addView(save);

        Button cancel =
                actionButton(
                        "رجوع",
                        MUTED
                );

        content.addView(cancel);

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showStudents();
                    }
                }
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String n =
                                name.getText()
                                        .toString()
                                        .trim();

                        if (n.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "يرجى إدخال اسم الطالب",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        Student student =
                                new Student(
                                        n,
                                        number.getText()
                                                .toString()
                                                .trim(),
                                        grade.getText()
                                                .toString()
                                                .trim(),
                                        section.getText()
                                                .toString()
                                                .trim()
                                );

                        students.add(student);

                        saveBasicData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حفظ الطالب",
                                Toast.LENGTH_SHORT
                        ).show();

                        showStudents();
                    }
                }
        );
    }

    // =========================================================
    // نهاية القسم 3
    // =====================================================
        // =========================================================
    // القسم 4
    // تعديل وحذف وبحث الطلاب
    // =========================================================

    private void showEditStudent(final int index) {

        if (index < 0 || index >= students.size()) {
            showStudents();
            return;
        }

        final Student old =
                students.get(index);

        createBase();

        TextView title =
                text(
                        "تعديل بيانات الطالب",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "تحديث بيانات الطالب المحدد",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(16);

        final android.widget.EditText name =
                input(
                        "اسم الطالب الثلاثي"
                );

        name.setText(
                old.name
        );

        final android.widget.EditText number =
                input(
                        "رقم الطالب"
                );

        number.setText(
                old.number
        );

        final android.widget.EditText grade =
                input(
                        "الصف"
                );

        grade.setText(
                old.grade
        );

        final android.widget.EditText section =
                input(
                        "الشعبة"
                );

        section.setText(
                old.section
        );

        content.addView(name);
        content.addView(number);
        content.addView(grade);
        content.addView(section);

        space(14);

        LinearLayout infoCard =
                makeCard();

        TextView infoTitle =
                text(
                        "ملاحظة",
                        16,
                        PRIMARY
                );

        infoTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        infoCard.addView(
                infoTitle
        );

        TextView infoText =
                text(
                        "تعديل بيانات الطالب لا يحذف درجاته المسجلة.",
                        13,
                        MUTED
                );

        infoCard.addView(
                infoText
        );

        content.addView(
                infoCard
        );

        space(12);

        Button save =
                actionButton(
                        "حفظ التعديلات",
                        SUCCESS
                );

        content.addView(
                save
        );

        Button cancel =
                actionButton(
                        "إلغاء والعودة",
                        MUTED
                );

        content.addView(
                cancel
        );

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showStudents();
                    }
                }
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newName =
                                name.getText()
                                        .toString()
                                        .trim();

                        if (newName.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "يرجى إدخال اسم الطالب",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        old.name =
                                newName;

                        old.number =
                                number.getText()
                                        .toString()
                                        .trim();

                        old.grade =
                                grade.getText()
                                        .toString()
                                        .trim();

                        old.section =
                                section.getText()
                                        .toString()
                                        .trim();

                        saveBasicData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم تحديث بيانات الطالب",
                                Toast.LENGTH_SHORT
                        ).show();

                        selectedStudent =
                                -1;

                        showStudents();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // حذف الطالب
    // ---------------------------------------------------------

    private void deleteStudent(
            final int index
    ) {

        if (index < 0 ||
                index >= students.size()) {

            return;
        }

        final Student student =
                students.get(index);

        new android.app.AlertDialog.Builder(
                MainActivity.this
        )
                .setTitle(
                        "حذف الطالب"
                )
                .setMessage(
                        "هل تريد حذف الطالب:\n"
                                + student.name
                                + " ؟"
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حذف",
                        new android.content.DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(
                                    android.content.DialogInterface dialog,
                                    int which
                            ) {

                                students.remove(
                                        index
                                );

                                removeStudentGrades(
                                        index
                                );

                                saveBasicData();

                                Toast.makeText(
                                        MainActivity.this,
                                        "تم حذف الطالب",
                                        Toast.LENGTH_SHORT
                                ).show();

                                selectedStudent =
                                        -1;

                                showStudents();
                            }
                        }
                )
                .show();
    }

    // ---------------------------------------------------------
    // حذف سجلات درجات الطالب
    // ---------------------------------------------------------

    private void removeStudentGrades(
            int studentIndex
    ) {

        ArrayList<String> removeKeys =
                new ArrayList<>();

        String prefix =
                studentIndex + "_";

        for (String key :
                grades.keySet()) {

            if (key.startsWith(prefix)) {

                removeKeys.add(
                        key
                );
            }
        }

        for (String key :
                removeKeys) {

            grades.remove(
                    key
            );
        }
    }

    // ---------------------------------------------------------
    // البحث في الطلاب
    // ---------------------------------------------------------

    private void filterStudents(
            String query
    ) {

        if (content == null) {
            return;
        }

        String q =
                query == null
                        ? ""
                        : query.trim()
                                .toLowerCase(
                                        Locale.getDefault()
                                );

        createBase();

        TextView title =
                text(
                        "إدارة الطلاب",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView subtitle =
                text(
                        "نتائج البحث في سجل الطلاب",
                        14,
                        MUTED
                );

        content.addView(
                subtitle
        );

        space(14);

        final android.widget.EditText search =
                input(
                        "🔍 البحث باسم الطالب أو الرقم"
                );

        search.setText(
                query
        );

        content.addView(
                search
        );

        space(12);

        int found =
                0;

        for (int i = 0;
             i < students.size();
             i++) {

            Student student =
                    students.get(i);

            String name =
                    student.name == null
                            ? ""
                            : student.name
                                    .toLowerCase(
                                            Locale.getDefault()
                                    );

            String number =
                    student.number == null
                            ? ""
                            : student.number
                                    .toLowerCase(
                                            Locale.getDefault()
                                    );

            if (q.isEmpty()
                    || name.contains(q)
                    || number.contains(q)) {

                addStudentItem(
                        i,
                        student
                );

                found++;
            }
        }

        if (found == 0) {

            LinearLayout empty =
                    makeCard();

            empty.setGravity(
                    Gravity.CENTER
            );

            TextView result =
                    text(
                            "لا توجد نتائج",
                            18,
                            TEXT
                    );

            result.setGravity(
                    Gravity.CENTER
            );

            result.setTypeface(
                    null,
                    Typeface.BOLD
            );

            empty.addView(
                    result
            );

            TextView help =
                    text(
                            "جرّب البحث باسم مختلف أو رقم طالب آخر",
                            13,
                            MUTED
                    );

            help.setGravity(
                    Gravity.CENTER
            );

            empty.addView(
                    help
            );

            content.addView(
                    empty
            );
        }

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

                        if (!s.toString()
                                .equals(query)) {

                            filterStudents(
                                    s.toString()
                            );
                        }
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s
                    ) {
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // بطاقة معلومات الطالب
    // ---------------------------------------------------------

    private LinearLayout studentInfoCard(
            Student student
    ) {

        LinearLayout card =
                makeCard();

        TextView name =
                text(
                        student.name,
                        20,
                        TEXT
                );

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                name
        );

        TextView number =
                text(
                        "رقم الطالب: "
                                + student.number,
                        13,
                        MUTED
                );

        card.addView(
                number
        );

        TextView grade =
                text(
                        "الصف: "
                                + student.grade,
                        13,
                        MUTED
                );

        card.addView(
                grade
        );

        TextView section =
                text(
                        "الشعبة: "
                                + student.section,
                        13,
                        MUTED
                );

        card.addView(
                section
        );

        return card;
    }

    // ---------------------------------------------------------
    // البحث عن طالب بالرقم
    // ---------------------------------------------------------

    private int findStudentByNumber(
            String number
    ) {

        if (number == null) {
            return -1;
        }

        String value =
                number.trim();

        if (value.isEmpty()) {
            return -1;
        }

        for (int i = 0;
             i < students.size();
             i++) {

            Student student =
                    students.get(i);

            if (student.number != null
                    && student.number
                            .trim()
                            .equals(value)) {

                return i;
            }
        }

        return -1;
    }

    // =========================================================
    // نهاية القسم 4
    // ======================================================
        // =========================================================
    // القسم 5
    // إدارة المواد
    // إضافة وتعديل وحذف المواد
    // =========================================================

    private void showSubjects() {

        createBase();

        TextView title =
                text(
                        "إدارة المواد",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "إضافة المواد الدراسية وإدارتها",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(14);

        // -----------------------------------------------------
        // ملخص المواد
        // -----------------------------------------------------

        LinearLayout summary =
                makeCard();

        TextView summaryTitle =
                text(
                        "ملخص المواد الدراسية",
                        18,
                        TEXT
                );

        summaryTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        summary.addView(
                summaryTitle
        );

        TextView total =
                text(
                        "إجمالي المواد: "
                                + subjects.size(),
                        14,
                        PRIMARY
                );

        summary.addView(
                total
        );

        content.addView(
                summary
        );

        space(10);

        // -----------------------------------------------------
        // إضافة مادة
        // -----------------------------------------------------

        Button add =
                actionButton(
                        "＋ إضافة مادة جديدة",
                        PRIMARY
                );

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddSubject();
                    }
                }
        );

        content.addView(
                add
        );

        space(12);

        // -----------------------------------------------------
        // قائمة المواد
        // -----------------------------------------------------

        if (subjects.isEmpty()) {

            LinearLayout empty =
                    makeCard();

            empty.setGravity(
                    Gravity.CENTER
            );

            TextView emptyTitle =
                    text(
                            "لا توجد مواد مضافة",
                            18,
                            TEXT
                    );

            emptyTitle.setGravity(
                    Gravity.CENTER
            );

            emptyTitle.setTypeface(
                    null,
                    Typeface.BOLD
            );

            empty.addView(
                    emptyTitle
            );

            TextView emptyInfo =
                    text(
                            "أضف المواد الدراسية للبدء بإدخال الدرجات",
                            14,
                            MUTED
                    );

            emptyInfo.setGravity(
                    Gravity.CENTER
            );

            empty.addView(
                    emptyInfo
            );

            content.addView(
                    empty
            );

        } else {

            for (int i = 0;
                 i < subjects.size();
                 i++) {

                addSubjectItem(
                        i,
                        subjects.get(i)
                );
            }
        }
    }

    // ---------------------------------------------------------
    // بطاقة المادة
    // ---------------------------------------------------------

    private void addSubjectItem(
            final int index,
            final Subject subject
    ) {

        LinearLayout card =
                makeCard();

        card.setPadding(
                18,
                16,
                18,
                16
        );

        TextView title =
                text(
                        subject.name,
                        19,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        TextView number =
                text(
                        "رقم المادة: "
                                + (index + 1),
                        13,
                        MUTED
                );

        card.addView(
                number
        );

        LinearLayout buttons =
                new LinearLayout(this);

        buttons.setOrientation(
                LinearLayout.HORIZONTAL
        );

        buttons.setGravity(
                Gravity.CENTER
        );

        Button edit =
                actionButton(
                        "تعديل",
                        PRIMARY
                );

        Button delete =
                actionButton(
                        "حذف",
                        DANGER
                );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        p.setMargins(
                4,
                8,
                4,
                0
        );

        buttons.addView(
                edit,
                p
        );

        buttons.addView(
                delete,
                p
        );

        card.addView(
                buttons
        );

        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedSubject =
                                index;

                        showEditSubject(
                                index
                        );
                    }
                }
        );

        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteSubject(
                                index
                        );
                    }
                }
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                0,
                5,
                0,
                5
        );

        content.addView(
                card,
                cp
        );
    }

    // ---------------------------------------------------------
    // إضافة مادة
    // ---------------------------------------------------------

    private void showAddSubject() {

        createBase();

        TextView title =
                text(
                        "إضافة مادة جديدة",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView subtitle =
                text(
                        "أدخل اسم المادة الدراسية",
                        14,
                        MUTED
                );

        content.addView(
                subtitle
        );

        space(16);

        final android.widget.EditText name =
                input(
                        "اسم المادة"
                );

        content.addView(
                name
        );

        space(12);

        Button save =
                actionButton(
                        "حفظ المادة",
                        SUCCESS
                );

        content.addView(
                save
        );

        Button cancel =
                actionButton(
                        "رجوع",
                        MUTED
                );

        content.addView(
                cancel
        );

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSubjects();
                    }
                }
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String value =
                                name.getText()
                                        .toString()
                                        .trim();

                        if (value.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "يرجى إدخال اسم المادة",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (findSubject(
                                value
                        ) >= 0) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "هذه المادة موجودة مسبقاً",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        subjects.add(
                                new Subject(
                                        value
                                )
                        );

                        saveBasicData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حفظ المادة",
                                Toast.LENGTH_SHORT
                        ).show();

                        showSubjects();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // تعديل مادة
    // ---------------------------------------------------------

    private void showEditSubject(
            final int index
    ) {

        if (index < 0 ||
                index >= subjects.size()) {

            showSubjects();
            return;
        }

        final Subject subject =
                subjects.get(index);

        createBase();

        TextView title =
                text(
                        "تعديل المادة",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView subtitle =
                text(
                        "تعديل اسم المادة الدراسية",
                        14,
                        MUTED
                );

        content.addView(
                subtitle
        );

        space(16);

        final android.widget.EditText name =
                input(
                        "اسم المادة"
                );

        name.setText(
                subject.name
        );

        content.addView(
                name
        );

        space(12);

        Button save =
                actionButton(
                        "حفظ التعديل",
                        SUCCESS
                );

        content.addView(
                save
        );

        Button cancel =
                actionButton(
                        "رجوع",
                        MUTED
                );

        content.addView(
                cancel
        );

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSubjects();
                    }
                }
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String value =
                                name.getText()
                                        .toString()
                                        .trim();

                        if (value.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "يرجى إدخال اسم المادة",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        int duplicate =
                                findSubject(
                                        value
                                );

                        if (duplicate >= 0
                                && duplicate != index) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "اسم المادة مستخدم مسبقاً",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        subject.name =
                                value;

                        saveBasicData();

                        selectedSubject =
                                -1;

                        Toast.makeText(
                                MainActivity.this,
                                "تم تعديل المادة",
                                Toast.LENGTH_SHORT
                        ).show();

                        showSubjects();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // البحث عن مادة
    // ---------------------------------------------------------

    private int findSubject(
            String name
    ) {

        if (name == null) {
            return -1;
        }

        String value =
                name.trim();

        for (int i = 0;
             i < subjects.size();
             i++) {

            Subject subject =
                    subjects.get(i);

            if (subject.name != null
                    && subject.name
                            .trim()
                            .equalsIgnoreCase(
                                    value
                            )) {

                return i;
            }
        }

        return -1;
    }

    // ---------------------------------------------------------
    // حذف المادة
    // ---------------------------------------------------------

    private void deleteSubject(
            final int index
    ) {

        if (index < 0 ||
                index >= subjects.size()) {

            return;
        }

        final Subject subject =
                subjects.get(index);

        new android.app.AlertDialog.Builder(
                MainActivity.this
        )
                .setTitle(
                        "حذف المادة"
                )
                .setMessage(
                        "هل تريد حذف مادة:\n"
                                + subject.name
                                + " ؟\n\n"
                                + "سيتم حذف ارتباطات درجات هذه المادة."
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حذف",
                        new android.content.DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(
                                    android.content.DialogInterface dialog,
                                    int which
                            ) {

                                removeSubjectGrades(
                                        index
                                );

                                subjects.remove(
                                        index
                                );

                                saveBasicData();

                                selectedSubject =
                                        -1;

                                Toast.makeText(
                                        MainActivity.this,
                                        "تم حذف المادة",
                                        Toast.LENGTH_SHORT
                                ).show();

                                showSubjects();
                            }
                        }
                )
                .show();
    }

    // ---------------------------------------------------------
    // حذف درجات المادة
    // ---------------------------------------------------------

    private void removeSubjectGrades(
            int subjectIndex
    ) {

        ArrayList<String> removeKeys =
                new ArrayList<>();

        String suffix =
                "_" + subjectIndex;

        for (String key :
                grades.keySet()) {

            if (key.endsWith(
                    suffix
            )) {

                removeKeys.add(
                        key
                );
            }
        }

        for (String key :
                removeKeys) {

            grades.remove(
                    key
            );
        }
    }

    // =========================================================
    // نهاية القسم 5
    // =========================================================
    // =========================================================
    // القسم 6
    // شاشة اختيار الطالب والمادة وإدخال الدرجات
    // =========================================================

    private void showGradeSelection() {

        createBase();

        TextView title =
                text(
                        "إدخال الدرجات",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "اختر الطالب والمادة لإدخال درجاته",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(16);

        if (students.isEmpty()) {

            LinearLayout card =
                    makeCard();

            TextView t =
                    text(
                            "لا يوجد طلاب",
                            19,
                            TEXT
                    );

            t.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(t);

            TextView info =
                    text(
                            "أضف الطلاب أولاً من إدارة الطلاب.",
                            14,
                            MUTED
                    );

            card.addView(info);

            content.addView(card);

            return;
        }

        if (subjects.isEmpty()) {

            LinearLayout card =
                    makeCard();

            TextView t =
                    text(
                            "لا توجد مواد",
                            19,
                            TEXT
                    );

            t.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(t);

            TextView info =
                    text(
                            "أضف المواد أولاً من إدارة المواد.",
                            14,
                            MUTED
                    );

            card.addView(info);

            content.addView(card);

            return;
        }

        // -----------------------------------------------------
        // اختيار الطالب
        // -----------------------------------------------------

        TextView studentTitle =
                text(
                        "الطالب",
                        17,
                        TEXT
                );

        studentTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                studentTitle
        );

        final android.widget.Spinner studentSpinner =
                new android.widget.Spinner(this);

        ArrayList<String> studentNames =
                new ArrayList<>();

        for (Student student :
                students) {

            studentNames.add(
                    student.name
            );
        }

        android.widget.ArrayAdapter<String>
                studentAdapter =
                new android.widget.ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        studentNames
                );

        studentAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        studentSpinner.setAdapter(
                studentAdapter
        );

        content.addView(
                studentSpinner
        );

        space(12);

        // -----------------------------------------------------
        // اختيار المادة
        // -----------------------------------------------------

        TextView subjectTitle =
                text(
                        "المادة الدراسية",
                        17,
                        TEXT
                );

        subjectTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                subjectTitle
        );

        final android.widget.Spinner subjectSpinner =
                new android.widget.Spinner(this);

        ArrayList<String> subjectNames =
                new ArrayList<>();

        for (Subject subject :
                subjects) {

            subjectNames.add(
                    subject.name
            );
        }

        android.widget.ArrayAdapter<String>
                subjectAdapter =
                new android.widget.ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        subjectNames
                );

        subjectAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        subjectSpinner.setAdapter(
                subjectAdapter
        );

        content.addView(
                subjectSpinner
        );

        space(18);

        Button open =
                actionButton(
                        "فتح سجل الدرجات",
                        PRIMARY
                );

        content.addView(
                open
        );

        open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int studentIndex =
                                studentSpinner
                                        .getSelectedItemPosition();

                        int subjectIndex =
                                subjectSpinner
                                        .getSelectedItemPosition();

                        if (studentIndex < 0
                                || subjectIndex < 0) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "يرجى اختيار الطالب والمادة",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        selectedStudent =
                                studentIndex;

                        selectedSubject =
                                subjectIndex;

                        showGradeEntry(
                                studentIndex,
                                subjectIndex
                        );
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // شاشة إدخال الدرجات
    // ---------------------------------------------------------

    private void showGradeEntry(
            int studentIndex,
            int subjectIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()
                || subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            showGradeSelection();
            return;
        }

        final Student student =
                students.get(
                        studentIndex
                );

        final Subject subject =
                subjects.get(
                        subjectIndex
                );

        final GradeRecord record =
                getGradeRecord(
                        studentIndex,
                        subjectIndex
                );

        createBase();

        TextView title =
                text(
                        "سجل الدرجات",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView studentInfo =
                text(
                        student.name
                                + "  •  "
                                + subject.name,
                        15,
                        PRIMARY
                );

        studentInfo.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                studentInfo
        );

        space(14);

        // -----------------------------------------------------
        // بطاقة الدرجات اليومية
        // -----------------------------------------------------

        LinearLayout dailyCard =
                makeCard();

        TextView dailyTitle =
                text(
                        "درجات اليومية",
                        19,
                        TEXT
                );

        dailyTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        dailyCard.addView(
                dailyTitle
        );

        space(8);

        final android.widget.EditText daily1 =
                input(
                        "اليومية الأولى"
                );

        final android.widget.EditText daily2 =
                input(
                        "اليومية الثانية"
                );

        final android.widget.EditText daily3 =
                input(
                        "اليومية الثالثة"
                );

        final android.widget.EditText activity =
                input(
                        "النشاط"
                );

        final android.widget.EditText behavior =
                input(
                        "السلوك"
                );

        daily1.setText(
                formatNumber(record.daily1)
        );

        daily2.setText(
                formatNumber(record.daily2)
        );

        daily3.setText(
                formatNumber(record.daily3)
        );

        activity.setText(
                formatNumber(record.activity)
        );

        behavior.setText(
                formatNumber(record.behavior)
        );

        dailyCard.addView(
                daily1
        );

        dailyCard.addView(
                daily2
        );

        dailyCard.addView(
                daily3
        );

        dailyCard.addView(
                activity
        );

        dailyCard.addView(
                behavior
        );

        content.addView(
                dailyCard
        );

        space(10);

        // -----------------------------------------------------
        // درجة الشهر
        // -----------------------------------------------------

        LinearLayout monthCard =
                makeCard();

        TextView monthTitle =
                text(
                        "درجة الشهر",
                        19,
                        TEXT
                );

        monthTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        monthCard.addView(
                monthTitle
        );

        final android.widget.EditText month =
                input(
                        "درجة الشهر"
                );

        month.setText(
                formatNumber(record.month)
        );

        monthCard.addView(
                month
        );

        content.addView(
                monthCard
        );

        space(10);

        // -----------------------------------------------------
        // النتائج المحسوبة
        // -----------------------------------------------------

        final LinearLayout resultCard =
                makeCard();

        TextView resultTitle =
                text(
                        "النتائج المحسوبة",
                        19,
                        TEXT
                );

        resultTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        resultCard.addView(
                resultTitle
        );

        final TextView totalText =
                text(
                        "مجموع اليومية: "
                                + formatNumber(
                                        dailyTotal(record)
                                ),
                        15,
                        PRIMARY
                );

        final TextView averageText =
                text(
                        "معدل الشهر: "
                                + formatNumber(
                                        record.monthAverage
                                ),
                        15,
                        SUCCESS
                );

        resultCard.addView(
                totalText
        );

        resultCard.addView(
                averageText
        );

        content.addView(
                resultCard
        );

        space(12);

        Button calculate =
                actionButton(
                        "حساب الدرجات",
                        PRIMARY
                );

        content.addView(
                calculate
        );

        calculate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        record.daily1 =
                                readNumber(
                                        daily1
                                );

                        record.daily2 =
                                readNumber(
                                        daily2
                                );

                        record.daily3 =
                                readNumber(
                                        daily3
                                );

                        record.activity =
                                readNumber(
                                        activity
                                );

                        record.behavior =
                                readNumber(
                                        behavior
                                );

                        record.month =
                                readNumber(
                                        month
                                );

                        double total =
                                dailyTotal(
                                        record
                                );

                        record.monthAverage =
                                calculateMonthAverage(
                                        total,
                                        record.month
                                );

                        totalText.setText(
                                "مجموع اليومية: "
                                        + formatNumber(
                                                total
                                        )
                        );

                        averageText.setText(
                                "معدل الشهر: "
                                        + formatNumber(
                                                record.monthAverage
                                        )
                        );

                        saveGradeData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حساب وحفظ الدرجات",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        Button save =
                actionButton(
                        "حفظ السجل",
                        SUCCESS
                );

        content.addView(
                save
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        record.daily1 =
                                readNumber(
                                        daily1
                                );

                        record.daily2 =
                                readNumber(
                                        daily2
                                );

                        record.daily3 =
                                readNumber(
                                        daily3
                                );

                        record.activity =
                                readNumber(
                                        activity
                                );

                        record.behavior =
                                readNumber(
                                        behavior
                                );

                        record.month =
                                readNumber(
                                        month
                                );

                        record.monthAverage =
                                calculateMonthAverage(
                                        dailyTotal(record),
                                        record.month
                                );

                        saveGradeData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حفظ سجل الدرجات",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        Button back =
                actionButton(
                        "اختيار طالب ومادة أخرى",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGradeSelection();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // قراءة رقم من حقل الإدخال
    // ---------------------------------------------------------

    private double readNumber(
            android.widget.EditText field
    ) {

        if (field == null) {
            return 0;
        }

        String value =
                field.getText()
                        .toString()
                        .trim();

        if (value.isEmpty()) {
            return 0;
        }

        try {

            value =
                    value.replace(
                            ",",
                            "."
                    );

            return Double.parseDouble(
                    value
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // ---------------------------------------------------------
    // تنسيق الأرقام
    // ---------------------------------------------------------

    private String formatNumber(
            double value
    ) {

        if (Math.abs(
                value -
                Math.round(value)
        ) < 0.0001) {

            return String.valueOf(
                    (long) Math.round(value)
            );
        }

        return String.format(
                Locale.US,
                "%.1f",
                value
        );
    }

    // =========================================================
    // نهاية القسم 6
    // =========================================================
    // =========================================================
    // القسم 7
    // حفظ واسترجاع سجلات الدرجات
    // =========================================================

    private void saveGradeData() {

        if (prefs == null) {
            return;
        }

        SharedPreferences.Editor editor =
                prefs.edit();

        // -----------------------------------------------------
        // حذف النسخة القديمة من سجلات الدرجات
        // -----------------------------------------------------

        int oldCount =
                prefs.getInt(
                        "grades_count",
                        0
                );

        for (int i = 0;
             i < oldCount;
             i++) {

            editor.remove(
                    "grade_key_" + i
            );
        }

        // -----------------------------------------------------
        // حفظ جميع سجلات الدرجات
        // -----------------------------------------------------

        int count =
                grades.size();

        editor.putInt(
                "grades_count",
                count
        );

        int position =
                0;

        for (Map.Entry<String, GradeRecord> entry :
                grades.entrySet()) {

            String key =
                    entry.getKey();

            GradeRecord r =
                    entry.getValue();

            String base =
                    "grade_" + position + "_";

            editor.putString(
                    base + "key",
                    key
            );

            editor.putString(
                    base + "daily1",
                    String.valueOf(
                            r.daily1
                    )
            );

            editor.putString(
                    base + "daily2",
                    String.valueOf(
                            r.daily2
                    )
            );

            editor.putString(
                    base + "daily3",
                    String.valueOf(
                            r.daily3
                    )
            );

            editor.putString(
                    base + "activity",
                    String.valueOf(
                            r.activity
                    )
            );

            editor.putString(
                    base + "behavior",
                    String.valueOf(
                            r.behavior
                    )
            );

            editor.putString(
                    base + "month",
                    String.valueOf(
                            r.month
                    )
            );

            editor.putString(
                    base + "monthAverage",
                    String.valueOf(
                            r.monthAverage
                    )
            );

            editor.putString(
                    base + "firstMonth",
                    String.valueOf(
                            r.firstMonth
                    )
            );

            editor.putString(
                    base + "secondMonth",
                    String.valueOf(
                            r.secondMonth
                    )
            );

            editor.putString(
                    base + "firstTerm",
                    String.valueOf(
                            r.firstTerm
                    )
            );

            editor.putString(
                    base + "secondTerm",
                    String.valueOf(
                            r.secondTerm
                    )
            );

            editor.putString(
                    base + "midYear",
                    String.valueOf(
                            r.midYear
                    )
            );

            editor.putString(
                    base + "annual",
                    String.valueOf(
                            r.annual
                    )
            );

            position++;
        }

        editor.apply();
    }

    // ---------------------------------------------------------
    // استرجاع سجلات الدرجات
    // ---------------------------------------------------------

    private void loadGradeData() {

        if (prefs == null) {
            return;
        }

        grades.clear();

        int count =
                prefs.getInt(
                        "grades_count",
                        0
                );

        for (int i = 0;
             i < count;
             i++) {

            String base =
                    "grade_" + i + "_";

            String key =
                    prefs.getString(
                            base + "key",
                            null
                    );

            if (key == null ||
                    key.trim().isEmpty()) {

                continue;
            }

            GradeRecord r =
                    new GradeRecord();

            r.daily1 =
                    readStoredDouble(
                            base + "daily1"
                    );

            r.daily2 =
                    readStoredDouble(
                            base + "daily2"
                    );

            r.daily3 =
                    readStoredDouble(
                            base + "daily3"
                    );

            r.activity =
                    readStoredDouble(
                            base + "activity"
                    );

            r.behavior =
                    readStoredDouble(
                            base + "behavior"
                    );

            r.month =
                    readStoredDouble(
                            base + "month"
                    );

            r.monthAverage =
                    readStoredDouble(
                            base + "monthAverage"
                    );

            r.firstMonth =
                    readStoredDouble(
                            base + "firstMonth"
                    );

            r.secondMonth =
                    readStoredDouble(
                            base + "secondMonth"
                    );

            r.firstTerm =
                    readStoredDouble(
                            base + "firstTerm"
                    );

            r.secondTerm =
                    readStoredDouble(
                            base + "secondTerm"
                    );

            r.midYear =
                    readStoredDouble(
                            base + "midYear"
                    );

            r.annual =
                    readStoredDouble(
                            base + "annual"
                    );

            grades.put(
                    key,
                    r
            );
        }
    }

    // ---------------------------------------------------------
    // قراءة رقم محفوظ
    // ---------------------------------------------------------

    private double readStoredDouble(
            String key
    ) {

        if (prefs == null) {
            return 0;
        }

        String value =
                prefs.getString(
                        key,
                        "0"
                );

        if (value == null ||
                value.trim().isEmpty()) {

            return 0;
        }

        try {

            return Double.parseDouble(
                    value
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // ---------------------------------------------------------
    // الحصول على سجل الطالب والمادة
    // ---------------------------------------------------------

    private GradeRecord getOrCreateGrade(
            int studentIndex,
            int subjectIndex
    ) {

        String key =
                gradeKey(
                        studentIndex,
                        subjectIndex
                );

        GradeRecord record =
                grades.get(
                        key
                );

        if (record == null) {

            record =
                    new GradeRecord();

            grades.put(
                    key,
                    record
            );
        }

        return record;
    }

    // ---------------------------------------------------------
    // التحقق من وجود سجل
    // ---------------------------------------------------------

    private boolean hasGradeRecord(
            int studentIndex,
            int subjectIndex
    ) {

        String key =
                gradeKey(
                        studentIndex,
                        subjectIndex
                );

        return grades.containsKey(
                key
        );
    }

    // ---------------------------------------------------------
    // حساب جميع القيم لسجل واحد
    // ---------------------------------------------------------

    private void recalculateRecord(
            GradeRecord record
    ) {

        if (record == null) {
            return;
        }

        double total =
                dailyTotal(
                        record
                );

        record.monthAverage =
                calculateMonthAverage(
                        total,
                        record.month
                );

        record.firstTerm =
                calculateTerm(
                        record.firstMonth,
                        record.secondMonth
                );

        record.secondTerm =
                calculateTerm(
                        record.secondMonth,
                        record.monthAverage
                );

        record.annual =
                calculateAnnual(
                        record.firstTerm,
                        record.midYear,
                        record.secondTerm
                );
    }

    // ---------------------------------------------------------
    // إعادة حساب جميع السجلات
    // ---------------------------------------------------------

    private void recalculateAllGrades() {

        for (GradeRecord record :
                grades.values()) {

            recalculateRecord(
                    record
            );
        }

        saveGradeData();
    }

    // ---------------------------------------------------------
    // حفظ البيانات الأساسية والدرجات معًا
    // ---------------------------------------------------------

    private void saveEverything() {

        saveBasicData();

        saveGradeData();
    }

    // ---------------------------------------------------------
    // تحميل جميع بيانات التطبيق
    // ---------------------------------------------------------

    private void loadEverything() {

        loadBasicData();

        loadGradeData();

        recalculateAllGrades();
    }

    // ---------------------------------------------------------
    // مسح جميع الدرجات
    // ---------------------------------------------------------

    private void clearGradeData() {

        grades.clear();

        if (prefs != null) {

            prefs.edit()
                    .remove(
                            "grades_count"
                    )
                    .apply();
        }
    }

    // ---------------------------------------------------------
    // عدد سجلات الدرجات
    // ---------------------------------------------------------

    private int getGradeRecordsCount() {

        return grades.size();
    }

    // =========================================================
    // نهاية القسم 7
    // =========================================================
    // =========================================================
    // القسم 8
    // الفصول الدراسية والحسابات السنوية
    // =========================================================

    private void showTermMenu() {

        createBase();

        TextView title =
                text(
                        "الفصول الدراسية",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "إدارة درجات الأشهر والسعي ونصف السنة",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(16);

        addTermButton(
                "الفصل الأول - الشهر الأول",
                1
        );

        addTermButton(
                "الفصل الأول - الشهر الثاني",
                2
        );

        addTermButton(
                "سعي الفصل الأول",
                3
        );

        addTermButton(
                "نصف السنة",
                4
        );

        addTermButton(
                "الفصل الثاني - الشهر الأول",
                5
        );

        addTermButton(
                "الفصل الثاني - الشهر الثاني",
                6
        );

        addTermButton(
                "سعي الفصل الثاني",
                7
        );

        addTermButton(
                "السعي السنوي",
                8
        );

        addTermButton(
                "السعي السنوي النهائي",
                9
        );
    }

    // ---------------------------------------------------------
    // زر الفصل
    // ---------------------------------------------------------

    private void addTermButton(
            String title,
            final int type
    ) {

        Button button =
                actionButton(
                        title,
                        PRIMARY
                );

        content.addView(
                button
        );

        space(6);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showTermScreen(
                                type
                        );
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // شاشة الفصل
    // ---------------------------------------------------------

    private void showTermScreen(
            final int type
    ) {

        createBase();

        String titleText =
                getTermTitle(
                        type
                );

        TextView title =
                text(
                        titleText,
                        25,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView subtitle =
                text(
                        "اختر الطالب والمادة لإدارة درجات هذه المرحلة",
                        14,
                        MUTED
                );

        content.addView(
                subtitle
        );

        space(16);

        if (students.isEmpty()
                || subjects.isEmpty()) {

            LinearLayout card =
                    makeCard();

            TextView message =
                    text(
                            students.isEmpty()
                                    ? "أضف الطلاب أولاً."
                                    : "أضف المواد أولاً.",
                            17,
                            TEXT
                    );

            message.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(
                    message
            );

            content.addView(
                    card
            );

            return;
        }

        // -----------------------------------------------------
        // الطالب
        // -----------------------------------------------------

        final android.widget.Spinner studentSpinner =
                createStudentSpinner();

        content.addView(
                text(
                        "الطالب",
                        16,
                        TEXT
                )
        );

        content.addView(
                studentSpinner
        );

        space(10);

        // -----------------------------------------------------
        // المادة
        // -----------------------------------------------------

        final android.widget.Spinner subjectSpinner =
                createSubjectSpinner();

        content.addView(
                text(
                        "المادة",
                        16,
                        TEXT
                )
        );

        content.addView(
                subjectSpinner
        );

        space(16);

        Button open =
                actionButton(
                        "فتح الدرجات",
                        PRIMARY
                );

        content.addView(
                open
        );

        open.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int studentIndex =
                                studentSpinner
                                        .getSelectedItemPosition();

                        int subjectIndex =
                                subjectSpinner
                                        .getSelectedItemPosition();

                        showTermEntry(
                                type,
                                studentIndex,
                                subjectIndex
                        );
                    }
                }
        );

        space(8);

        Button back =
                actionButton(
                        "العودة إلى الفصول",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTermMenu();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // قائمة الطلاب
    // ---------------------------------------------------------

    private android.widget.Spinner createStudentSpinner() {

        android.widget.Spinner spinner =
                new android.widget.Spinner(this);

        ArrayList<String> names =
                new ArrayList<>();

        for (Student student :
                students) {

            names.add(
                    student.name
            );
        }

        android.widget.ArrayAdapter<String>
                adapter =
                new android.widget.ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        names
                );

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinner.setAdapter(
                adapter
        );

        return spinner;
    }

    // ---------------------------------------------------------
    // قائمة المواد
    // ---------------------------------------------------------

    private android.widget.Spinner createSubjectSpinner() {

        android.widget.Spinner spinner =
                new android.widget.Spinner(this);

        ArrayList<String> names =
                new ArrayList<>();

        for (Subject subject :
                subjects) {

            names.add(
                    subject.name
            );
        }

        android.widget.ArrayAdapter<String>
                adapter =
                new android.widget.ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        names
                );

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinner.setAdapter(
                adapter
        );

        return spinner;
    }

    // ---------------------------------------------------------
    // اسم المرحلة
    // ---------------------------------------------------------

    private String getTermTitle(
            int type
    ) {

        switch (type) {

            case 1:
                return "الفصل الأول - الشهر الأول";

            case 2:
                return "الفصل الأول - الشهر الثاني";

            case 3:
                return "سعي الفصل الأول";

            case 4:
                return "نصف السنة";

            case 5:
                return "الفصل الثاني - الشهر الأول";

            case 6:
                return "الفصل الثاني - الشهر الثاني";

            case 7:
                return "سعي الفصل الثاني";

            case 8:
                return "السعي السنوي";

            case 9:
                return "السعي السنوي النهائي";

            default:
                return "الدرجات";
        }
    }

    // ---------------------------------------------------------
    // إدخال درجة المرحلة
    // ---------------------------------------------------------

    private void showTermEntry(
            final int type,
            final int studentIndex,
            final int subjectIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()
                || subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            return;
        }

        final GradeRecord record =
                getOrCreateGrade(
                        studentIndex,
                        subjectIndex
                );

        final Student student =
                students.get(
                        studentIndex
                );

        final Subject subject =
                subjects.get(
                        subjectIndex
                );

        createBase();

        TextView title =
                text(
                        getTermTitle(type),
                        25,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView info =
                text(
                        student.name
                                + " • "
                                + subject.name,
                        15,
                        PRIMARY
                );

        info.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                info
        );

        space(16);

        // -----------------------------------------------------
        // المراحل الشهرية
        // -----------------------------------------------------

        if (type == 1
                || type == 2
                || type == 5
                || type == 6) {

            showMonthlyEntry(
                    type,
                    record
            );

            return;
        }

        // -----------------------------------------------------
        // نصف السنة
        // -----------------------------------------------------

        if (type == 4) {

            showMidYearEntry(
                    record
            );

            return;
        }

        // -----------------------------------------------------
        // المراحل المحسوبة
        // -----------------------------------------------------

        showCalculatedTerm(
                type,
                record
        );
    }

    // ---------------------------------------------------------
    // إدخال الشهر
    // ---------------------------------------------------------

    private void showMonthlyEntry(
            final int type,
            final GradeRecord record
    ) {

        final android.widget.EditText value =
                input(
                        "الدرجة"
                );

        double current = 0;

        if (type == 1) {
            current =
                    record.firstMonth;
        } else if (type == 2) {
            current =
                    record.secondMonth;
        } else if (type == 5) {
            current =
                    record.firstMonth;
        } else if (type == 6) {
            current =
                    record.secondMonth;
        }

        value.setText(
                formatNumber(
                        current
                )
        );

        content.addView(
                value
        );

        space(12);

        Button save =
                actionButton(
                        "حفظ الدرجة",
                        SUCCESS
                );

        content.addView(
                save
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        double result =
                                readNumber(
                                        value
                                );

                        if (type == 1
                                || type == 5) {

                            record.firstMonth =
                                    result;

                        } else {

                            record.secondMonth =
                                    result;
                        }

                        record.firstTerm =
                                calculateTerm(
                                        record.firstMonth,
                                        record.secondMonth
                                );

                        saveGradeData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حفظ الدرجة",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // نصف السنة
    // ---------------------------------------------------------

    private void showMidYearEntry(
            final GradeRecord record
    ) {

        final android.widget.EditText value =
                input(
                        "درجة نصف السنة"
                );

        value.setText(
                formatNumber(
                        record.midYear
                )
        );

        content.addView(
                value
        );

        space(12);

        Button save =
                actionButton(
                        "حفظ نصف السنة",
                        SUCCESS
                );

        content.addView(
                save
        );

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        record.midYear =
                                readNumber(
                                        value
                                );

                        saveGradeData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حفظ نصف السنة",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // النتائج المحسوبة
    // ---------------------------------------------------------

    private void showCalculatedTerm(
            int type,
            GradeRecord record
    ) {

        recalculateRecord(
                record
        );

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        getTermTitle(type),
                        18,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        addResultLine(
                card,
                "الشهر الأول",
                record.firstMonth
        );

        addResultLine(
                card,
                "الشهر الثاني",
                record.secondMonth
        );

        addResultLine(
                card,
                "سعي الفصل الأول",
                record.firstTerm
        );

        addResultLine(
                card,
                "نصف السنة",
                record.midYear
        );

        addResultLine(
                card,
                "السعي السنوي",
                record.annual
        );

        content.addView(
                card
        );

        saveGradeData();
    }

    // ---------------------------------------------------------
    // سطر نتيجة
    // ---------------------------------------------------------

    private void addResultLine(
            LinearLayout parent,
            String title,
            double value
    ) {

        TextView line =
                text(
                        title
                                + ": "
                                + formatNumber(value),
                        15,
                        PRIMARY
                );

        parent.addView(
                line
        );
    }

    // =========================================================
    // نهاية القسم 8
    // =========================================================
    // =========================================================
    // القسم 9
    // شاشة النتائج الشاملة
    // =========================================================

    private void showResults() {

        createBase();

        TextView title =
                text(
                        "النتائج",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "عرض النتائج والمعدلات لجميع الطلاب",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(16);

        if (students.isEmpty()) {

            LinearLayout card =
                    makeCard();

            TextView message =
                    text(
                            "لا توجد بيانات للعرض",
                            19,
                            TEXT
                    );

            message.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(
                    message
            );

            content.addView(
                    card
            );

            return;
        }

        // -----------------------------------------------------
        // اختيار الطالب
        // -----------------------------------------------------

        content.addView(
                text(
                        "اختر الطالب",
                        16,
                        TEXT
                )
        );

        final android.widget.Spinner studentSpinner =
                createStudentSpinner();

        content.addView(
                studentSpinner
        );

        space(10);

        // -----------------------------------------------------
        // اختيار المادة
        // -----------------------------------------------------

        content.addView(
                text(
                        "اختر المادة",
                        16,
                        TEXT
                )
        );

        final android.widget.Spinner subjectSpinner =
                createSubjectSpinner();

        content.addView(
                subjectSpinner
        );

        space(14);

        Button view =
                actionButton(
                        "عرض النتيجة",
                        PRIMARY
                );

        content.addView(
                view
        );

        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int studentIndex =
                                studentSpinner
                                        .getSelectedItemPosition();

                        int subjectIndex =
                                subjectSpinner
                                        .getSelectedItemPosition();

                        showStudentResult(
                                studentIndex,
                                subjectIndex
                        );
                    }
                }
        );

        space(16);

        // -----------------------------------------------------
        // ملخص عام للصف
        // -----------------------------------------------------

        addClassSummary();
    }

    // ---------------------------------------------------------
    // نتيجة طالب ومادة
    // ---------------------------------------------------------

    private void showStudentResult(
            int studentIndex,
            int subjectIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()
                || subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            return;
        }

        Student student =
                students.get(
                        studentIndex
                );

        Subject subject =
                subjects.get(
                        subjectIndex
                );

        GradeRecord record =
                getOrCreateGrade(
                        studentIndex,
                        subjectIndex
                );

        recalculateRecord(
                record
        );

        createBase();

        TextView title =
                text(
                        "نتيجة الطالب",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        TextView info =
                text(
                        student.name
                                + "  •  "
                                + subject.name,
                        16,
                        PRIMARY
                );

        info.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                info
        );

        TextView details =
                text(
                        "الصف: "
                                + student.grade
                                + "   •   الشعبة: "
                                + student.section,
                        13,
                        MUTED
                );

        content.addView(
                details
        );

        space(14);

        // -----------------------------------------------------
        // بطاقة الدرجات الشهرية
        // -----------------------------------------------------

        LinearLayout monthly =
                makeCard();

        TextView monthlyTitle =
                text(
                        "الدرجات الشهرية",
                        19,
                        TEXT
                );

        monthlyTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        monthly.addView(
                monthlyTitle
        );

        addResultLine(
                monthly,
                "الشهر الأول",
                record.firstMonth
        );

        addResultLine(
                monthly,
                "الشهر الثاني",
                record.secondMonth
        );

        content.addView(
                monthly
        );

        space(10);

        // -----------------------------------------------------
        // بطاقة السعي
        // -----------------------------------------------------

        LinearLayout terms =
                makeCard();

        TextView termsTitle =
                text(
                        "السعي والفصول",
                        19,
                        TEXT
                );

        termsTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        terms.addView(
                termsTitle
        );

        addResultLine(
                terms,
                "سعي الفصل الأول",
                record.firstTerm
        );

        addResultLine(
                terms,
                "نصف السنة",
                record.midYear
        );

        addResultLine(
                terms,
                "سعي الفصل الثاني",
                record.secondTerm
        );

        content.addView(
                terms
        );

        space(10);

        // -----------------------------------------------------
        // النتيجة السنوية
        // -----------------------------------------------------

        LinearLayout annual =
                makeCard();

        TextView annualTitle =
                text(
                        "النتيجة السنوية",
                        20,
                        SUCCESS
                );

        annualTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        annual.addView(
                annualTitle
        );

        TextView annualValue =
                text(
                        formatNumber(
                                record.annual
                        ),
                        32,
                        SUCCESS
                );

        annualValue.setGravity(
                Gravity.CENTER
        );

        annualValue.setTypeface(
                null,
                Typeface.BOLD
        );

        annual.addView(
                annualValue
        );

        content.addView(
                annual
        );

        space(12);

        // -----------------------------------------------------
        // تعديل الدرجات
        // -----------------------------------------------------

        Button edit =
                actionButton(
                        "تعديل درجات الطالب",
                        PRIMARY
                );

        content.addView(
                edit
        );

        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedStudent =
                                studentIndex;

                        selectedSubject =
                                subjectIndex;

                        showGradeEntry(
                                studentIndex,
                                subjectIndex
                        );
                    }
                }
        );

        Button back =
                actionButton(
                        "العودة إلى النتائج",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showResults();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // ملخص الصف
    // ---------------------------------------------------------

    private void addClassSummary() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "ملخص الصف",
                        19,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        double total =
                0;

        int count =
                0;

        double highest =
                -1;

        double lowest =
                Double.MAX_VALUE;

        String highestName =
                "-";

        String lowestName =
                "-";

        for (int i = 0;
             i < students.size();
             i++) {

            double studentAverage =
                    getStudentAverage(
                            i
                    );

            if (studentAverage <= 0) {
                continue;
            }

            total +=
                    studentAverage;

            count++;

            if (studentAverage >
                    highest) {

                highest =
                        studentAverage;

                highestName =
                        students.get(i)
                                .name;
            }

            if (studentAverage <
                    lowest) {

                lowest =
                        studentAverage;

                lowestName =
                        students.get(i)
                                .name;
            }
        }

        double average =
                count == 0
                        ? 0
                        : total / count;

        addResultLine(
                card,
                "متوسط الصف",
                average
        );

        addResultLine(
                card,
                "أعلى معدل",
                highest < 0
                        ? 0
                        : highest
        );

        addResultLine(
                card,
                "أدنى معدل",
                lowest == Double.MAX_VALUE
                        ? 0
                        : lowest
        );

        TextView top =
                text(
                        "الأعلى: "
                                + highestName,
                        13,
                        SUCCESS
                );

        card.addView(
                top
        );

        TextView bottom =
                text(
                        "الأدنى: "
                                + lowestName,
                        13,
                        DANGER
                );

        card.addView(
                bottom
        );

        content.addView(
                card
        );
    }

    // ---------------------------------------------------------
    // معدل الطالب
    // ---------------------------------------------------------

    private double getStudentAverage(
            int studentIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()) {

            return 0;
        }

        double total =
                0;

        int count =
                0;

        for (int s = 0;
             s < subjects.size();
             s++) {

            String key =
                    gradeKey(
                            studentIndex,
                            s
                    );

            GradeRecord record =
                    grades.get(
                            key
                    );

            if (record == null) {
                continue;
            }

            recalculateRecord(
                    record
            );

            if (record.annual > 0) {

                total +=
                        record.annual;

                count++;
            }
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }

    // ---------------------------------------------------------
    // عدد الطلاب الذين لديهم درجات
    // ---------------------------------------------------------

    private int getStudentsWithGrades() {

        int count =
                0;

        for (int i = 0;
             i < students.size();
             i++) {

            if (getStudentAverage(i) > 0) {
                count++;
            }
        }

        return count;
    }

    // ---------------------------------------------------------
    // هل الطالب لديه درجات؟
    // ---------------------------------------------------------

    private boolean studentHasGrades(
            int studentIndex
    ) {

        for (int s = 0;
             s < subjects.size();
             s++) {

            if (hasGradeRecord(
                    studentIndex,
                    s
            )) {

                return true;
            }
        }

        return false;
    }

    // ---------------------------------------------------------
    // نسبة إكمال الدرجات
    // ---------------------------------------------------------

    private double getCompletionRate(
            int studentIndex
    ) {

        if (subjects.isEmpty()) {
            return 0;
        }

        int completed =
                0;

        for (int s = 0;
             s < subjects.size();
             s++) {

            if (hasGradeRecord(
                    studentIndex,
                    s
            )) {

                completed++;
            }
        }

        return (
                completed * 100.0
        ) / subjects.size();
    }

    // =========================================================
    // نهاية القسم 9
    // =========================================================
    // =========================================================
    // القسم 10
    // التقارير والتحليلات والترتيب
    // =========================================================

    private void showReports() {

        createBase();

        TextView title =
                text(
                        "التقارير والتحليلات",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(title);

        TextView subtitle =
                text(
                        "لوحة تحليلية شاملة لأداء الطلاب",
                        14,
                        MUTED
                );

        content.addView(subtitle);

        space(16);

        // -----------------------------------------------------
        // الإحصائيات العامة
        // -----------------------------------------------------

        addReportStatistics();

        space(12);

        // -----------------------------------------------------
        // ترتيب الطلاب
        // -----------------------------------------------------

        addRankingSection();

        space(12);

        // -----------------------------------------------------
        // الطلاب المتفوقون
        // -----------------------------------------------------

        addTopStudents();

        space(12);

        // -----------------------------------------------------
        // الطلاب الذين يحتاجون متابعة
        // -----------------------------------------------------

        addWeakStudents();

        space(12);

        // -----------------------------------------------------
        // إكمال الدرجات
        // -----------------------------------------------------

        addCompletionSection();
    }

    // ---------------------------------------------------------
    // إحصائيات التقارير
    // ---------------------------------------------------------

    private void addReportStatistics() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "الإحصائيات العامة",
                        19,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        int studentsCount =
                students.size();

        int gradedStudents =
                getStudentsWithGrades();

        double total =
                0;

        int count =
                0;

        double highest =
                0;

        double lowest =
                Double.MAX_VALUE;

        for (int i = 0;
             i < students.size();
             i++) {

            double average =
                    getStudentAverage(i);

            if (average <= 0) {
                continue;
            }

            total +=
                    average;

            count++;

            if (average > highest) {
                highest =
                        average;
            }

            if (average < lowest) {
                lowest =
                        average;
            }
        }

        double classAverage =
                count == 0
                        ? 0
                        : total / count;

        if (lowest == Double.MAX_VALUE) {
            lowest = 0;
        }

        addResultLine(
                card,
                "إجمالي الطلاب",
                studentsCount
        );

        addResultLine(
                card,
                "الطلاب أصحاب الدرجات",
                gradedStudents
        );

        addResultLine(
                card,
                "متوسط الصف",
                classAverage
        );

        addResultLine(
                card,
                "أعلى معدل",
                highest
        );

        addResultLine(
                card,
                "أدنى معدل",
                lowest
        );

        content.addView(
                card
        );
    }

    // ---------------------------------------------------------
    // ترتيب الطلاب
    // ---------------------------------------------------------

    private void addRankingSection() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "ترتيب الطلاب",
                        19,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        ArrayList<Integer> indexes =
                new ArrayList<>();

        for (int i = 0;
             i < students.size();
             i++) {

            if (getStudentAverage(i) > 0) {
                indexes.add(i);
            }
        }

        // -----------------------------------------------------
        // ترتيب تنازلي حسب المعدل
        // -----------------------------------------------------

        for (int i = 0;
             i < indexes.size();
             i++) {

            for (int j = i + 1;
                 j < indexes.size();
                 j++) {

                double a =
                        getStudentAverage(
                                indexes.get(i)
                        );

                double b =
                        getStudentAverage(
                                indexes.get(j)
                        );

                if (b > a) {

                    int temp =
                            indexes.get(i);

                    indexes.set(
                            i,
                            indexes.get(j)
                    );

                    indexes.set(
                            j,
                            temp
                    );
                }
            }
        }

        if (indexes.isEmpty()) {

            TextView empty =
                    text(
                            "لا توجد درجات كافية للترتيب.",
                            14,
                            MUTED
                    );

            card.addView(
                    empty
            );

        } else {

            int limit =
                    Math.min(
                            indexes.size(),
                            20
                    );

            for (int position = 0;
                 position < limit;
                 position++) {

                int index =
                        indexes.get(
                                position
                        );

                Student student =
                        students.get(
                                index
                        );

                double average =
                        getStudentAverage(
                                index
                        );

                TextView row =
                        text(
                                (position + 1)
                                        + "  •  "
                                        + student.name
                                        + "  —  "
                                        + formatNumber(
                                                average
                                        ),
                                15,
                                position < 3
                                        ? SUCCESS
                                        : TEXT
                        );

                if (position < 3) {

                    row.setTypeface(
                            null,
                            Typeface.BOLD
                    );
                }

                card.addView(
                        row
                );
            }
        }

        content.addView(
                card
        );
    }

    // ---------------------------------------------------------
    // أوائل الطلاب
    // ---------------------------------------------------------

    private void addTopStudents() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "أوائل الطلاب",
                        19,
                        SUCCESS
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        ArrayList<Integer> indexes =
                getSortedStudentIndexes();

        int limit =
                Math.min(
                        indexes.size(),
                        5
                );

        if (limit == 0) {

            card.addView(
                    text(
                            "لا توجد بيانات كافية.",
                            14,
                            MUTED
                    )
            );

        } else {

            for (int i = 0;
                 i < limit;
                 i++) {

                int index =
                        indexes.get(i);

                Student student =
                        students.get(index);

                double average =
                        getStudentAverage(
                                index
                        );

                card.addView(
                        text(
                                "🥇 "
                                        + (i + 1)
                                        + " - "
                                        + student.name
                                        + " : "
                                        + formatNumber(
                                                average
                                        ),
                                15,
                                TEXT
                        )
                );
            }
        }

        content.addView(
                card
        );
    }

    // ---------------------------------------------------------
    // الطلاب الذين يحتاجون متابعة
    // ---------------------------------------------------------

    private void addWeakStudents() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "طلاب يحتاجون متابعة",
                        19,
                        DANGER
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        boolean found =
                false;

        for (int i = 0;
             i < students.size();
             i++) {

            double average =
                    getStudentAverage(
                            i
                    );

            if (average > 0
                    && average < 50) {

                found =
                        true;

                Student student =
                        students.get(i);

                card.addView(
                        text(
                                student.name
                                        + " : "
                                        + formatNumber(
                                                average
                                        ),
                                15,
                                DANGER
                        )
                );
            }
        }

        if (!found) {

            card.addView(
                    text(
                            "لا توجد حالات ضمن هذا التصنيف.",
                            14,
                            MUTED
                    )
            );
        }

        content.addView(
                card
        );
    }

    // ---------------------------------------------------------
    // نسبة إكمال الدرجات
    // ---------------------------------------------------------

    private void addCompletionSection() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "اكتمال السجلات",
                        19,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        if (students.isEmpty()
                || subjects.isEmpty()) {

            card.addView(
                    text(
                            "لا توجد بيانات كافية.",
                            14,
                            MUTED
                    )
            );

            content.addView(
                    card
            );

            return;
        }

        double totalRate =
                0;

        int count =
                0;

        for (int i = 0;
             i < students.size();
             i++) {

            totalRate +=
                    getCompletionRate(i);

            count++;
        }

        double rate =
                count == 0
                        ? 0
                        : totalRate / count;

        addResultLine(
                card,
                "نسبة إكمال السجل",
                rate
        );

        TextView description =
                text(
                        "تعكس النسبة مدى إدخال سجلات الدرجات للطلاب والمواد.",
                        13,
                        MUTED
                );

        card.addView(
                description
        );

        content.addView(
                card
        );
    }

    // ---------------------------------------------------------
    // ترتيب مؤشرات الطلاب
    // ---------------------------------------------------------

    private ArrayList<Integer>
            getSortedStudentIndexes() {

        ArrayList<Integer> result =
                new ArrayList<>();

        for (int i = 0;
             i < students.size();
             i++) {

            if (getStudentAverage(i) > 0) {

                result.add(
                        i
                );
            }
        }

        for (int i = 0;
             i < result.size();
             i++) {

            for (int j = i + 1;
                 j < result.size();
                 j++) {

                double first =
                        getStudentAverage(
                                result.get(i)
                        );

                double second =
                        getStudentAverage(
                                result.get(j)
                        );

                if (second > first) {

                    int temp =
                            result.get(i);

                    result.set(
                            i,
                            result.get(j)
                    );

                    result.set(
                            j,
                            temp
                    );
                }
            }
        }

        return result;
    }

    // ---------------------------------------------------------
    // متوسط مادة محددة للصف
    // ---------------------------------------------------------

    private double getSubjectAverage(
            int subjectIndex
    ) {

        if (subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            return 0;
        }

        double total =
                0;

        int count =
                0;

        for (int studentIndex = 0;
             studentIndex < students.size();
             studentIndex++) {

            String key =
                    gradeKey(
                            studentIndex,
                            subjectIndex
                    );

            GradeRecord record =
                    grades.get(
                            key
                    );

            if (record == null) {
                continue;
            }

            recalculateRecord(
                    record
            );

            if (record.annual <= 0) {
                continue;
            }

            total +=
                    record.annual;

            count++;
        }

        return count == 0
                ? 0
                : total / count;
    }

    // ---------------------------------------------------------
    // أعلى طالب
    // ---------------------------------------------------------

    private String getTopStudentName() {

        ArrayList<Integer> indexes =
                getSortedStudentIndexes();

        if (indexes.isEmpty()) {
            return "-";
        }

        return students.get(
                indexes.get(0)
        ).name;
    }

    // ---------------------------------------------------------
    // معدل أعلى طالب
    // ---------------------------------------------------------

    private double getTopStudentAverage() {

        ArrayList<Integer> indexes =
                getSortedStudentIndexes();

        if (indexes.isEmpty()) {
            return 0;
        }

        return getStudentAverage(
                indexes.get(0)
        );
    }

    // =========================================================
    // نهاية القسم 10
    // =========================================================
    // =========================================================
    // القسم 11
    // الحسابات الأكاديمية والنتائج النهائية
    // =========================================================

    private double calculateFirstTerm(
            GradeRecord record
    ) {

        if (record == null) {
            return 0;
        }

        return Math.round(
                (
                        record.firstMonth
                                + record.secondMonth
                ) / 2.0
        );
    }

    // ---------------------------------------------------------
    // حساب سعي الفصل الثاني
    // ---------------------------------------------------------

    private double calculateSecondTermValue(
            GradeRecord record
    ) {

        if (record == null) {
            return 0;
        }

        return Math.round(
                (
                        record.firstMonth
                                + record.secondMonth
                ) / 2.0
        );
    }

    // ---------------------------------------------------------
    // حساب السعي السنوي
    // ---------------------------------------------------------

    private double calculateAnnualValue(
            GradeRecord record
    ) {

        if (record == null) {
            return 0;
        }

        double firstTerm =
                record.firstTerm;

        double midYear =
                record.midYear;

        double secondTerm =
                record.secondTerm;

        double total =
                firstTerm
                        + midYear
                        + secondTerm;

        return Math.ceil(
                total
        ) / 3.0;
    }

    // ---------------------------------------------------------
    // إعادة حساب السجل الأكاديمي
    // ---------------------------------------------------------

    private void updateAcademicRecord(
            GradeRecord record
    ) {

        if (record == null) {
            return;
        }

        // الشهر الأول
        record.firstTerm =
                calculateFirstTerm(
                        record
                );

        // الفصل الثاني
        record.secondTerm =
                calculateSecondTermValue(
                        record
                );

        // السعي السنوي
        record.annual =
                calculateAnnualValue(
                        record
                );
    }

    // ---------------------------------------------------------
    // تحديث جميع السجلات الأكاديمية
    // ---------------------------------------------------------

    private void updateAllAcademicRecords() {

        for (GradeRecord record :
                grades.values()) {

            updateAcademicRecord(
                    record
            );
        }

        saveGradeData();
    }

    // ---------------------------------------------------------
    // الحصول على السجل بعد تحديث الحسابات
    // ---------------------------------------------------------

    private GradeRecord getUpdatedRecord(
            int studentIndex,
            int subjectIndex
    ) {

        GradeRecord record =
                getOrCreateGrade(
                        studentIndex,
                        subjectIndex
                );

        updateAcademicRecord(
                record
        );

        return record;
    }

    // ---------------------------------------------------------
    // حساب معدل جميع مواد الطالب
    // ---------------------------------------------------------

    private double calculateStudentAnnualAverage(
            int studentIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()) {

            return 0;
        }

        double total =
                0;

        int count =
                0;

        for (int subjectIndex = 0;
             subjectIndex < subjects.size();
             subjectIndex++) {

            GradeRecord record =
                    grades.get(
                            gradeKey(
                                    studentIndex,
                                    subjectIndex
                            )
                    );

            if (record == null) {
                continue;
            }

            updateAcademicRecord(
                    record
            );

            if (record.annual <= 0) {
                continue;
            }

            total +=
                    record.annual;

            count++;
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }

    // ---------------------------------------------------------
    // حساب معدل مادة لجميع الطلاب
    // ---------------------------------------------------------

    private double calculateSubjectAnnualAverage(
            int subjectIndex
    ) {

        if (subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            return 0;
        }

        double total =
                0;

        int count =
                0;

        for (int studentIndex = 0;
             studentIndex < students.size();
             studentIndex++) {

            GradeRecord record =
                    grades.get(
                            gradeKey(
                                    studentIndex,
                                    subjectIndex
                            )
                    );

            if (record == null) {
                continue;
            }

            updateAcademicRecord(
                    record
            );

            if (record.annual <= 0) {
                continue;
            }

            total +=
                    record.annual;

            count++;
        }

        if (count == 0) {
            return 0;
        }

        return total / count;
    }

    // ---------------------------------------------------------
    // تحديد حالة الطالب
    // ---------------------------------------------------------

    private String getStudentStatus(
            double average
    ) {

        if (average <= 0) {

            return "لا توجد درجات";
        }

        if (average >= 50) {

            return "ناجح";
        }

        return "يحتاج متابعة";
    }

    // ---------------------------------------------------------
    // لون الحالة
    // ---------------------------------------------------------

    private int getStatusColor(
            double average
    ) {

        if (average <= 0) {
            return MUTED;
        }

        if (average >= 50) {
            return SUCCESS;
        }

        return DANGER;
    }

    // ---------------------------------------------------------
    // بطاقة النتيجة الأكاديمية
    // ---------------------------------------------------------

    private LinearLayout createAcademicCard(
            Student student,
            Subject subject,
            GradeRecord record
    ) {

        LinearLayout card =
                makeCard();

        updateAcademicRecord(
                record
        );

        TextView name =
                text(
                        student.name,
                        19,
                        TEXT
                );

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                name
        );

        TextView subjectText =
                text(
                        "المادة: "
                                + subject.name,
                        14,
                        PRIMARY
                );

        card.addView(
                subjectText
        );

        addResultLine(
                card,
                "الشهر الأول",
                record.firstMonth
        );

        addResultLine(
                card,
                "الشهر الثاني",
                record.secondMonth
        );

        addResultLine(
                card,
                "سعي الفصل الأول",
                record.firstTerm
        );

        addResultLine(
                card,
                "نصف السنة",
                record.midYear
        );

        addResultLine(
                card,
                "سعي الفصل الثاني",
                record.secondTerm
        );

        addResultLine(
                card,
                "السعي السنوي",
                record.annual
        );

        TextView status =
                text(
                        "الحالة: "
                                + getStudentStatus(
                                        record.annual
                                ),
                        15,
                        getStatusColor(
                                record.annual
                        )
                );

        status.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                status
        );

        return card;
    }

    // ---------------------------------------------------------
    // عرض نتائج مادة لجميع الطلاب
    // ---------------------------------------------------------

    private void showSubjectResults(
            int subjectIndex
    ) {

        if (subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            return;
        }

        Subject subject =
                subjects.get(
                        subjectIndex
                );

        createBase();

        TextView title =
                text(
                        "نتائج المادة",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        content.addView(
                text(
                        subject.name,
                        16,
                        PRIMARY
                )
        );

        space(14);

        double average =
                calculateSubjectAnnualAverage(
                        subjectIndex
                );

        LinearLayout summary =
                makeCard();

        TextView summaryTitle =
                text(
                        "ملخص المادة",
                        18,
                        TEXT
                );

        summaryTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        summary.addView(
                summaryTitle
        );

        addResultLine(
                summary,
                "متوسط المادة",
                average
        );

        content.addView(
                summary
        );

        space(10);

        for (int studentIndex = 0;
             studentIndex < students.size();
             studentIndex++) {

            GradeRecord record =
                    grades.get(
                            gradeKey(
                                    studentIndex,
                                    subjectIndex
                            )
                    );

            if (record == null) {
                continue;
            }

            Student student =
                    students.get(
                            studentIndex
                    );

            content.addView(
                    createAcademicCard(
                            student,
                            subject,
                            record
                    )
            );

            space(6);
        }

        Button back =
                actionButton(
                        "العودة",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showResults();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // السعي النهائي للطالب
    // ---------------------------------------------------------

    private double getFinalStudentAverage(
            int studentIndex
    ) {

        double total =
                0;

        int count =
                0;

        for (int subjectIndex = 0;
             subjectIndex < subjects.size();
             subjectIndex++) {

            GradeRecord record =
                    grades.get(
                            gradeKey(
                                    studentIndex,
                                    subjectIndex
                            )
                    );

            if (record == null) {
                continue;
            }

            updateAcademicRecord(
                    record
            );

            if (record.annual <= 0) {
                continue;
            }

            total +=
                    record.annual;

            count++;
        }

        return count == 0
                ? 0
                : total / count;
    }

    // ---------------------------------------------------------
    // عدد المواد المكتملة للطالب
    // ---------------------------------------------------------

    private int getCompletedSubjects(
            int studentIndex
    ) {

        int count =
                0;

        for (int subjectIndex = 0;
             subjectIndex < subjects.size();
             subjectIndex++) {

            GradeRecord record =
                    grades.get(
                            gradeKey(
                                    studentIndex,
                                    subjectIndex
                            )
                    );

            if (record == null) {
                continue;
            }

            updateAcademicRecord(
                    record
            );

            if (record.annual > 0) {
                count++;
            }
        }

        return count;
    }

    // =========================================================
    // نهاية القسم 11
    // =========================================================
    // =========================================================
    // القسم 12
    // لوحة الطالب التفصيلية والتحليل
    // =========================================================

    private void showStudentDetails(
            final int studentIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()) {

            showStudents();
            return;
        }

        Student student =
                students.get(
                        studentIndex
                );

        createBase();

        TextView title =
                text(
                        "ملف الطالب",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        content.addView(
                text(
                        student.name,
                        19,
                        PRIMARY
                )
        );

        space(12);

        // -----------------------------------------------------
        // معلومات الطالب
        // -----------------------------------------------------

        LinearLayout info =
                makeCard();

        TextView infoTitle =
                text(
                        "معلومات الطالب",
                        18,
                        TEXT
                );

        infoTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        info.addView(
                infoTitle
        );

        addInfoLine(
                info,
                "رقم الطالب",
                student.number
        );

        addInfoLine(
                info,
                "الصف",
                student.grade
        );

        addInfoLine(
                info,
                "الشعبة",
                student.section
        );

        content.addView(
                info
        );

        space(10);

        // -----------------------------------------------------
        // الإحصائيات العامة
        // -----------------------------------------------------

        double average =
                getFinalStudentAverage(
                        studentIndex
                );

        int completed =
                getCompletedSubjects(
                        studentIndex
                );

        int totalSubjects =
                subjects.size();

        double completion =
                totalSubjects == 0
                        ? 0
                        : (
                            completed * 100.0
                            / totalSubjects
                        );

        LinearLayout statistics =
                makeCard();

        TextView statisticsTitle =
                text(
                        "ملخص الأداء",
                        18,
                        TEXT
                );

        statisticsTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        statistics.addView(
                statisticsTitle
        );

        addResultLine(
                statistics,
                "المعدل العام",
                average
        );

        addResultLine(
                statistics,
                "المواد المكتملة",
                completed
        );

        addResultLine(
                statistics,
                "إجمالي المواد",
                totalSubjects
        );

        addResultLine(
                statistics,
                "نسبة الإكمال",
                completion
        );

        TextView status =
                text(
                        "الحالة: "
                                + getStudentStatus(
                                        average
                                ),
                        16,
                        getStatusColor(
                                average
                        )
                );

        status.setTypeface(
                null,
                Typeface.BOLD
        );

        statistics.addView(
                status
        );

        content.addView(
                statistics
        );

        space(10);

        // -----------------------------------------------------
        // مؤشر الأداء
        // -----------------------------------------------------

        content.addView(
                createPerformanceCard(
                        average
                )
        );

        space(10);

        // -----------------------------------------------------
        // المواد
        // -----------------------------------------------------

        TextView subjectsTitle =
                text(
                        "تفاصيل المواد",
                        21,
                        TEXT
                );

        subjectsTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                subjectsTitle
        );

        space(6);

        if (subjects.isEmpty()) {

            content.addView(
                    text(
                            "لا توجد مواد مضافة حاليًا.",
                            15,
                            MUTED
                    )
            );

        } else {

            for (int subjectIndex = 0;
                 subjectIndex < subjects.size();
                 subjectIndex++) {

                Subject subject =
                        subjects.get(
                                subjectIndex
                        );

                GradeRecord record =
                        grades.get(
                                gradeKey(
                                        studentIndex,
                                        subjectIndex
                                )
                        );

                if (record == null) {

                    record =
                            new GradeRecord();
                }

                content.addView(
                        createStudentSubjectCard(
                                subject,
                                record
                        )
                );

                space(7);
            }
        }

        // -----------------------------------------------------
        // أفضل وأضعف مادة
        // -----------------------------------------------------

        LinearLayout analysis =
                createStudentSubjectAnalysis(
                        studentIndex
                );

        content.addView(
                analysis
        );

        space(12);

        Button edit =
                actionButton(
                        "تعديل بيانات الطالب",
                        PRIMARY
                );

        content.addView(
                edit
        );

        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showEditStudent(
                                studentIndex
                        );
                    }
                }
        );

        Button back =
                actionButton(
                        "العودة إلى الطلاب",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showStudents();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // سطر معلومات
    // ---------------------------------------------------------

    private void addInfoLine(
            LinearLayout parent,
            String title,
            String value
    ) {

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

        TextView left =
                text(
                        title,
                        14,
                        MUTED
                );

        TextView right =
                text(
                        value == null
                                ? ""
                                : value,
                        15,
                        TEXT
                );

        right.setTypeface(
                null,
                Typeface.BOLD
        );

        row.addView(
                left,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        row.addView(
                right,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        parent.addView(
                row
        );

        spaceInside(
                parent,
                5
        );
    }

    // ---------------------------------------------------------
    // مؤشر الأداء
    // ---------------------------------------------------------

    private LinearLayout createPerformanceCard(
            double average
    ) {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "مؤشر الأداء",
                        18,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        TextView value =
                text(
                        formatNumber(
                                average
                        ),
                        30,
                        getStatusColor(
                                average
                        )
                );

        value.setGravity(
                Gravity.CENTER
        );

        value.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                value
        );

        ProgressBar progress =
                new ProgressBar(
                        this,
                        null,
                        android.R.attr.progressBarStyleHorizontal
                );

        progress.setMax(
                100
        );

        int progressValue =
                (int) Math.max(
                        0,
                        Math.min(
                                100,
                                Math.round(
                                        average
                                )
                        )
                );

        progress.setProgress(
                progressValue
        );

        LinearLayout.LayoutParams pp =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        22
                );

        pp.setMargins(
                0,
                12,
                0,
                6
        );

        card.addView(
                progress,
                pp
        );

        TextView description =
                text(
                        getPerformanceDescription(
                                average
                        ),
                        14,
                        MUTED
                );

        description.setGravity(
                Gravity.CENTER
        );

        card.addView(
                description
        );

        return card;
    }

    // ---------------------------------------------------------
    // وصف مستوى الأداء
    // ---------------------------------------------------------

    private String getPerformanceDescription(
            double average
    ) {

        if (average <= 0) {

            return "لم يتم تسجيل درجات كافية بعد";
        }

        if (average >= 90) {

            return "أداء ممتاز جدًا";
        }

        if (average >= 80) {

            return "أداء ممتاز";
        }

        if (average >= 70) {

            return "أداء جيد جدًا";
        }

        if (average >= 60) {

            return "أداء جيد";
        }

        if (average >= 50) {

            return "أداء مقبول";
        }

        return "يحتاج إلى متابعة وتحسين";
    }

    // ---------------------------------------------------------
    // بطاقة المادة داخل ملف الطالب
    // ---------------------------------------------------------

    private LinearLayout createStudentSubjectCard(
            Subject subject,
            GradeRecord record
    ) {

        updateAcademicRecord(
                record
        );

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        subject.name,
                        18,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        addResultLine(
                card,
                "الشهر الأول",
                record.firstMonth
        );

        addResultLine(
                card,
                "الشهر الثاني",
                record.secondMonth
        );

        addResultLine(
                card,
                "سعي الفصل الأول",
                record.firstTerm
        );

        addResultLine(
                card,
                "نصف السنة",
                record.midYear
        );

        addResultLine(
                card,
                "سعي الفصل الثاني",
                record.secondTerm
        );

        addResultLine(
                card,
                "السعي السنوي",
                record.annual
        );

        TextView state =
                text(
                        getStudentStatus(
                                record.annual
                        ),
                        14,
                        getStatusColor(
                                record.annual
                        )
                );

        state.setTypeface(
                null,
                Typeface.BOLD
        );

        state.setGravity(
                Gravity.CENTER
        );

        card.addView(
                state
        );

        return card;
    }

    // ---------------------------------------------------------
    // تحليل أفضل وأضعف مادة
    // ---------------------------------------------------------

    private LinearLayout createStudentSubjectAnalysis(
            int studentIndex
    ) {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "تحليل المواد",
                        18,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        if (subjects.isEmpty()) {

            card.addView(
                    text(
                            "لا توجد بيانات لتحليلها.",
                            14,
                            MUTED
                    )
            );

            return card;
        }

        int bestIndex =
                -1;

        int weakIndex =
                -1;

        double best =
                -1;

        double weak =
                Double.MAX_VALUE;

        for (int subjectIndex = 0;
             subjectIndex < subjects.size();
             subjectIndex++) {

            GradeRecord record =
                    grades.get(
                            gradeKey(
                                    studentIndex,
                                    subjectIndex
                            )
                    );

            if (record == null) {
                continue;
            }

            updateAcademicRecord(
                    record
            );

            if (record.annual <= 0) {
                continue;
            }

            if (record.annual > best) {

                best =
                        record.annual;

                bestIndex =
                        subjectIndex;
            }

            if (record.annual < weak) {

                weak =
                        record.annual;

                weakIndex =
                        subjectIndex;
            }
        }

        if (bestIndex >= 0) {

            addResultText(
                    card,
                    "أفضل مادة",
                    subjects.get(
                            bestIndex
                    ).name
                            + " — "
                            + formatNumber(
                                    best
                            )
            );
        }

        if (weakIndex >= 0) {

            addResultText(
                    card,
                    "أضعف مادة",
                    subjects.get(
                            weakIndex
                    ).name
                            + " — "
                            + formatNumber(
                                    weak
                            )
            );
        }

        if (bestIndex < 0) {

            card.addView(
                    text(
                            "لا توجد درجات كافية للتحليل.",
                            14,
                            MUTED
                    )
            );
        }

        return card;
    }

    // ---------------------------------------------------------
    // سطر نصي للنتائج
    // ---------------------------------------------------------

    private void addResultText(
            LinearLayout parent,
            String title,
            String value
    ) {

        LinearLayout row =
                new LinearLayout(
                        this
                );

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView a =
                text(
                        title,
                        14,
                        MUTED
                );

        TextView b =
                text(
                        value,
                        15,
                        TEXT
                );

        b.setTypeface(
                null,
                Typeface.BOLD
        );

        row.addView(
                a,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        row.addView(
                b,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        parent.addView(
                row
        );

        spaceInside(
                parent,
                6
        );
    }

    // ---------------------------------------------------------
    // مساحة داخلية
    // ---------------------------------------------------------

    private void spaceInside(
            LinearLayout parent,
            int dp
    ) {

        Space s =
                new Space(
                        this
                );

        parent.addView(
                s,
                new LinearLayout.LayoutParams(
                        1,
                        dp
                )
        );
    }

    // =========================================================
    // نهاية القسم 12
    // =========================================================
    // =========================================================
    // القسم 13
    // شاشة إدخال الدرجات الاحترافية
    // =========================================================

    private void showProfessionalGradeEntry(
            final int studentIndex,
            final int subjectIndex
    ) {

        if (studentIndex < 0
                || studentIndex >= students.size()
                || subjectIndex < 0
                || subjectIndex >= subjects.size()) {

            showGradeSelection();
            return;
        }

        selectedStudent =
                studentIndex;

        selectedSubject =
                subjectIndex;

        final Student student =
                students.get(
                        studentIndex
                );

        final Subject subject =
                subjects.get(
                        subjectIndex
                );

        final GradeRecord record =
                getOrCreateGrade(
                        studentIndex,
                        subjectIndex
                );

        createBase();

        TextView title =
                text(
                        "إدخال الدرجات",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        content.addView(
                text(
                        student.name,
                        19,
                        PRIMARY
                )
        );

        content.addView(
                text(
                        "المادة: "
                                + subject.name,
                        15,
                        MUTED
                )
        );

        space(12);

        // -----------------------------------------------------
        // بطاقة معلومات الطالب
        // -----------------------------------------------------

        LinearLayout studentCard =
                makeCard();

        TextView studentTitle =
                text(
                        "بيانات الطالب",
                        18,
                        TEXT
                );

        studentTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        studentCard.addView(
                studentTitle
        );

        addInfoLine(
                studentCard,
                "الرقم",
                student.number
        );

        addInfoLine(
                studentCard,
                "الصف",
                student.grade
        );

        addInfoLine(
                studentCard,
                "الشعبة",
                student.section
        );

        content.addView(
                studentCard
        );

        space(10);

        // -----------------------------------------------------
        // حقول الدرجات
        // -----------------------------------------------------

        LinearLayout gradeCard =
                makeCard();

        TextView gradeTitle =
                text(
                        "درجات الشهر",
                        19,
                        TEXT
                );

        gradeTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        gradeCard.addView(
                gradeTitle
        );

        final EditText daily1 =
                createGradeField(
                        "اليومية الأولى",
                        record.daily1
                );

        final EditText daily2 =
                createGradeField(
                        "اليومية الثانية",
                        record.daily2
                );

        final EditText daily3 =
                createGradeField(
                        "اليومية الثالثة",
                        record.daily3
                );

        final EditText activity =
                createGradeField(
                        "النشاط",
                        record.activity
                );

        final EditText behavior =
                createGradeField(
                        "السلوك",
                        record.behavior
                );

        final EditText month =
                createGradeField(
                        "درجة الشهر",
                        record.month
                );

        gradeCard.addView(
                daily1
        );

        gradeCard.addView(
                daily2
        );

        gradeCard.addView(
                daily3
        );

        gradeCard.addView(
                activity
        );

        gradeCard.addView(
                behavior
        );

        gradeCard.addView(
                month
        );

        content.addView(
                gradeCard
        );

        space(10);

        // -----------------------------------------------------
        // بطاقة الحساب المباشر
        // -----------------------------------------------------

        final LinearLayout resultCard =
                makeCard();

        TextView resultTitle =
                text(
                        "الحساب المباشر",
                        19,
                        TEXT
                );

        resultTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        resultCard.addView(
                resultTitle
        );

        final TextView totalText =
                text(
                        "مجموع اليوميات: 0",
                        16,
                        PRIMARY
                );

        final TextView averageText =
                text(
                        "معدل الشهر: 0",
                        17,
                        SUCCESS
                );

        totalText.setTypeface(
                null,
                Typeface.BOLD
        );

        averageText.setTypeface(
                null,
                Typeface.BOLD
        );

        resultCard.addView(
                totalText
        );

        resultCard.addView(
                averageText
        );

        content.addView(
                resultCard
        );

        // -----------------------------------------------------
        // دالة تحديث الحساب
        // -----------------------------------------------------

        final Runnable update =
                new Runnable() {

                    @Override
                    public void run() {

                        double d1 =
                                readNumber(
                                        daily1
                                );

                        double d2 =
                                readNumber(
                                        daily2
                                );

                        double d3 =
                                readNumber(
                                        daily3
                                );

                        double act =
                                readNumber(
                                        activity
                                );

                        double beh =
                                readNumber(
                                        behavior
                                );

                        double mon =
                                readNumber(
                                        month
                                );

                        double total =
                                d1
                                        + d2
                                        + d3
                                        + act
                                        + beh;

                        double avg =
                                calculateMonthAverage(
                                        total,
                                        mon
                                );

                        totalText.setText(
                                "مجموع اليوميات: "
                                        + formatNumber(
                                                total
                                        )
                        );

                        averageText.setText(
                                "معدل الشهر: "
                                        + formatNumber(
                                                avg
                                        )
                        );
                    }
                };

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

                        update.run();
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                };

        daily1.addTextChangedListener(
                watcher
        );

        daily2.addTextChangedListener(
                watcher
        );

        daily3.addTextChangedListener(
                watcher
        );

        activity.addTextChangedListener(
                watcher
        );

        behavior.addTextChangedListener(
                watcher
        );

        month.addTextChangedListener(
                watcher
        );

        update.run();

        space(10);

        // -----------------------------------------------------
        // زر الحفظ
        // -----------------------------------------------------

        Button save =
                actionButton(
                        "حفظ الدرجات",
                        PRIMARY
                );

        content.addView(
                save
        );

        save.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        record.daily1 =
                                readNumber(
                                        daily1
                                );

                        record.daily2 =
                                readNumber(
                                        daily2
                                );

                        record.daily3 =
                                readNumber(
                                        daily3
                                );

                        record.activity =
                                readNumber(
                                        activity
                                );

                        record.behavior =
                                readNumber(
                                        behavior
                                );

                        record.month =
                                readNumber(
                                        month
                                );

                        record.monthAverage =
                                calculateMonthAverage(
                                        dailyTotal(
                                                record
                                        ),
                                        record.month
                                );

                        saveGradeData();

                        Toast.makeText(
                                MainActivity.this,
                                "تم حفظ الدرجات بنجاح",
                                Toast.LENGTH_SHORT
                        ).show();

                        showStudentDetails(
                                studentIndex
                        );
                    }
                }
        );

        // -----------------------------------------------------
        // زر مسح الدرجات
        // -----------------------------------------------------

        Button clear =
                actionButton(
                        "مسح درجات الشهر",
                        DANGER
                );

        content.addView(
                clear
        );

        clear.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        daily1.setText("");
                        daily2.setText("");
                        daily3.setText("");
                        activity.setText("");
                        behavior.setText("");
                        month.setText("");

                        record.daily1 = 0;
                        record.daily2 = 0;
                        record.daily3 = 0;
                        record.activity = 0;
                        record.behavior = 0;
                        record.month = 0;
                        record.monthAverage = 0;

                        saveGradeData();

                        update.run();
                    }
                }
        );

        // -----------------------------------------------------
        // الانتقال إلى ملف الطالب
        // -----------------------------------------------------

        Button details =
                actionButton(
                        "عرض ملف الطالب",
                        SUCCESS
                );

        content.addView(
                details
        );

        details.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        showStudentDetails(
                                studentIndex
                        );
                    }
                }
        );

        // -----------------------------------------------------
        // العودة
        // -----------------------------------------------------

        Button back =
                actionButton(
                        "العودة",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        showGradeSelection();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // إنشاء حقل درجة
    // ---------------------------------------------------------

    private EditText createGradeField(
            String hint,
            double value
    ) {

        EditText field =
                new EditText(
                        this
                );

        field.setHint(
                hint
        );

        field.setTextSize(
                15
        );

        field.setTextColor(
                TEXT
        );

        field.setHintTextColor(
                MUTED
        );

        field.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        field.setGravity(
                Gravity.RIGHT
                        | Gravity.CENTER_VERTICAL
        );

        field.setSingleLine(
                true
        );

        if (value > 0) {

            field.setText(
                    formatNumber(
                            value
                    )
            );

            field.setSelection(
                    field.length()
            );
        }

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                5,
                0,
                5
        );

        field.setLayoutParams(
                params
        );

        return field;
    }

    // ---------------------------------------------------------
    // فتح إدخال درجات الطالب من القائمة
    // ---------------------------------------------------------

    private void openStudentGradeEntry(
            int studentIndex,
            int subjectIndex
    ) {

        showProfessionalGradeEntry(
                studentIndex,
                subjectIndex
        );
    }

    // ---------------------------------------------------------
    // شاشة اختيار الطالب والمادة للإدخال
    // ---------------------------------------------------------

    private void showProfessionalGradeSelection() {

        createBase();

        TextView title =
                text(
                        "إدخال درجات جديدة",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        content.addView(
                text(
                        "اختر الطالب والمادة للبدء",
                        15,
                        MUTED
                )
        );

        space(14);

        if (students.isEmpty()) {

            content.addView(
                    text(
                            "أضف طالبًا أولًا.",
                            16,
                            WARNING
                    )
            );

            return;
        }

        if (subjects.isEmpty()) {

            content.addView(
                    text(
                            "أضف مادة أولًا.",
                            16,
                            WARNING
                    )
            );

            return;
        }

        TextView studentLabel =
                text(
                        "الطالب",
                        16,
                        TEXT
                );

        content.addView(
                studentLabel
        );

        final Spinner studentSpinner =
                createStudentSpinner();

        content.addView(
                studentSpinner
        );

        space(10);

        TextView subjectLabel =
                text(
                        "المادة",
                        16,
                        TEXT
                );

        content.addView(
                subjectLabel
        );

        final Spinner subjectSpinner =
                createSubjectSpinner();

        content.addView(
                subjectSpinner
        );

        space(14);

        Button open =
                actionButton(
                        "فتح سجل الدرجات",
                        PRIMARY
                );

        content.addView(
                open
        );

        open.setOnClickListener(
        new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int s =
                        studentSpinner.getSelectedItemPosition();

                int m =
                        subjectSpinner.getSelectedItemPosition();

                if (s >= 0 && m >= 0) {

                    openStudentGradeEntry(
                            s,
                            m
                    );
                }
            }
        }
);
    }
        // =========================================================
    // القسم 14
    // لوحة التحليل المتقدمة وتقارير الصف
    // =========================================================
 private void showAdvancedDashboard() {
        createBase();

        TextView title =
                text(
                        "لوحة التحليل",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        content.addView(
                text(
                        "تحليل شامل لأداء الطلاب والمواد",
                        15,
                        MUTED
                )
        );

        space(14);

        // -----------------------------------------------------
        // حساب الإحصائيات
        // -----------------------------------------------------

        int totalStudents =
                students.size();

        int totalSubjects =
                subjects.size();

        int completedStudents =
                0;

        int successfulStudents =
                0;

        int weakStudents =
                0;

        double totalAverage =
                0;

        double highestAverage =
                -1;

        double lowestAverage =
                Double.MAX_VALUE;

        String highestName =
                "لا يوجد";

        String lowestName =
                "لا يوجد";

        for (int i = 0;
             i < students.size();
             i++) {

            double average =
                    getFinalStudentAverage(
                            i
                    );

            if (average <= 0) {
                continue;
            }

            completedStudents++;

            totalAverage +=
                    average;

            if (average >= 50) {

                successfulStudents++;

            } else {

                weakStudents++;
            }

            if (average > highestAverage) {

                highestAverage =
                        average;

                highestName =
                        students.get(
                                i
                        ).name;
            }

            if (average < lowestAverage) {

                lowestAverage =
                        average;

                lowestName =
                        students.get(
                                i
                        ).name;
            }
        }

        double classAverage =
                completedStudents == 0
                        ? 0
                        : totalAverage
                                / completedStudents;

        double completionRate =
                totalStudents == 0
                        ? 0
                        : completedStudents
                                * 100.0
                                / totalStudents;

        // -----------------------------------------------------
        // بطاقة عدد الطلاب
        // -----------------------------------------------------

        addDashboardMetric(
                "عدد الطلاب",
                String.valueOf(
                        totalStudents
                ),
                PRIMARY
        );

        // -----------------------------------------------------
        // بطاقة عدد المواد
        // -----------------------------------------------------

        addDashboardMetric(
                "عدد المواد",
                String.valueOf(
                        totalSubjects
                ),
                SUCCESS
        );

        // -----------------------------------------------------
        // بطاقة معدل الصف
        // -----------------------------------------------------

        addDashboardMetric(
                "معدل الصف",
                formatNumber(
                        classAverage
                ),
                PRIMARY
        );

        // -----------------------------------------------------
        // بطاقة نسبة الإكمال
        // -----------------------------------------------------

        addDashboardMetric(
                "نسبة إكمال الدرجات",
                formatNumber(
                        completionRate
                ) + "%",
                WARNING
        );

        space(10);

        // -----------------------------------------------------
        // أفضل طالب
        // -----------------------------------------------------

        LinearLayout bestCard =
                makeCard();

        TextView bestTitle =
                text(
                        "🏆 الطالب الأعلى",
                        19,
                        SUCCESS
                );

        bestTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        bestCard.addView(
                bestTitle
        );

        bestCard.addView(
                text(
                        highestName,
                        18,
                        TEXT
                )
        );

        if (highestAverage >= 0) {

            addResultLine(
                    bestCard,
                    "المعدل",
                    highestAverage
            );
        }

        content.addView(
                bestCard
        );

        space(8);

        // -----------------------------------------------------
        // أقل طالب
        // -----------------------------------------------------

        LinearLayout lowestCard =
                makeCard();

        TextView lowestTitle =
                text(
                        "📌 يحتاج إلى متابعة",
                        19,
                        DANGER
                );

        lowestTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        lowestCard.addView(
                lowestTitle
        );

        lowestCard.addView(
                text(
                        lowestName,
                        18,
                        TEXT
                )
        );

        if (lowestAverage
                != Double.MAX_VALUE) {

            addResultLine(
                    lowestCard,
                    "المعدل",
                    lowestAverage
            );
        }

        content.addView(
                lowestCard
        );

        space(10);

        // -----------------------------------------------------
        // حالات الطلاب
        // -----------------------------------------------------

        LinearLayout statusCard =
                makeCard();

        TextView statusTitle =
                text(
                        "حالة الطلاب",
                        19,
                        TEXT
                );

        statusTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        statusCard.addView(
                statusTitle
        );

        addResultLine(
                statusCard,
                "طلاب لديهم نتائج",
                completedStudents
        );

        addResultLine(
                statusCard,
                "ناجحون",
                successfulStudents
        );

        addResultLine(
                statusCard,
                "يحتاجون متابعة",
                weakStudents
        );

        addResultLine(
                statusCard,
                "بدون نتائج",
                totalStudents
                        - completedStudents
        );

        content.addView(
                statusCard
        );

        space(10);

        // -----------------------------------------------------
        // توزيع المستويات
        // -----------------------------------------------------

        content.addView(
                createGradeDistributionCard()
        );

        space(10);

        // -----------------------------------------------------
        // ترتيب الطلاب
        // -----------------------------------------------------

        TextView rankingTitle =
                text(
                        "ترتيب الطلاب",
                        21,
                        TEXT
                );

        rankingTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                rankingTitle
        );

        space(6);

        ArrayList<Integer> ranking =
                getSortedStudentIndexes();

        int rank =
                1;

        for (Integer index :
                ranking) {

            double average =
                    getFinalStudentAverage(
                            index
                    );

            LinearLayout card =
                    makeCard();

            TextView rankText =
                    text(
                            "#" + rank
                                    + "  "
                                    + students.get(
                                            index
                                    ).name,
                            17,
                            TEXT
                    );

            rankText.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(
                    rankText
            );

            addResultLine(
                    card,
                    "المعدل",
                    average
            );

            addResultText(
                    card,
                    "الحالة",
                    getStudentStatus(
                            average
                    )
            );

            final int selected =
                    index;

            Button open =
                    actionButton(
                            "فتح ملف الطالب",
                            PRIMARY
                    );

            card.addView(
                    open
            );

            open.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(
                                View v
                        ) {

                            showStudentDetails(
                                    selected
                            );
                        }
                    }
            );

            content.addView(
                    card
            );

            space(6);

            rank++;

            if (rank > 20) {
                break;
            }
        }

        space(10);

        // -----------------------------------------------------
        // تقارير المواد
        // -----------------------------------------------------

        TextView subjectTitle =
                text(
                        "تحليل المواد",
                        21,
                        TEXT
                );

        subjectTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                subjectTitle
        );

        space(6);

        for (int i = 0;
             i < subjects.size();
             i++) {

            final int subjectIndex =
                    i;

            double average =
                    calculateSubjectAnnualAverage(
                            i
                    );

            LinearLayout card =
                    makeCard();

            TextView name =
                    text(
                            subjects.get(
                                    i
                            ).name,
                            17,
                            TEXT
                    );

            name.setTypeface(
                    null,
                    Typeface.BOLD
            );

            card.addView(
                    name
            );

            addResultLine(
                    card,
                    "متوسط المادة",
                    average
            );

            Button details =
                    actionButton(
                            "عرض نتائج المادة",
                            PRIMARY
                    );

            card.addView(
                    details
            );

            details.setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(
                                View v
                        ) {

                            showSubjectResults(
                                    subjectIndex
                            );
                        }
                    }
            );

            content.addView(
                    card
            );

            space(6);
        }

        Button back =
                actionButton(
                        "العودة",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        showHome();
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // بطاقة إحصائية صغيرة
    // ---------------------------------------------------------

    private void addDashboardMetric(
            String title,
            String value,
            int color
    ) {

        LinearLayout card =
                makeCard();

        TextView titleText =
                text(
                        title,
                        15,
                        MUTED
                );

        TextView valueText =
                text(
                        value,
                        27,
                        color
                );

        titleText.setTypeface(
                null,
                Typeface.BOLD
        );

        valueText.setTypeface(
                null,
                Typeface.BOLD
        );

        valueText.setGravity(
                Gravity.CENTER
        );

        card.addView(
                titleText
        );

        card.addView(
                valueText
        );

        content.addView(
                card
        );

        space(7);
    }

    // ---------------------------------------------------------
    // توزيع الدرجات
    // ---------------------------------------------------------

    private LinearLayout createGradeDistributionCard() {

        LinearLayout card =
                makeCard();

        TextView title =
                text(
                        "توزيع مستويات الطلاب",
                        19,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title
        );

        int excellent =
                0;

        int veryGood =
                0;

        int good =
                0;

        int acceptable =
                0;

        int weak =
                0;

        for (int i = 0;
             i < students.size();
             i++) {

            double average =
                    getFinalStudentAverage(
                            i
                    );

            if (average >= 90) {

                excellent++;

            } else if (average >= 80) {

                veryGood++;

            } else if (average >= 70) {

                good++;

            } else if (average >= 50) {

                acceptable++;

            } else if (average > 0) {

                weak++;
            }
        }

        addResultLine(
                card,
                "ممتاز جدًا (90+)",
                excellent
        );

        addResultLine(
                card,
                "ممتاز (80-89)",
                veryGood
        );

        addResultLine(
                card,
                "جيد جدًا (70-79)",
                good
        );

        addResultLine(
                card,
                "مقبول (50-69)",
                acceptable
        );

        addResultLine(
                card,
                "أقل من النجاح",
                weak
        );

        return card;
    }

    // ---------------------------------------------------------
    // شاشة تقارير الصف
    // ---------------------------------------------------------

    private void showClassReport() {

        createBase();

        TextView title =
                text(
                        "تقرير الصف",
                        27,
                        TEXT
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                title
        );

        content.addView(
                text(
                        "ملخص شامل للطلاب والمواد والنتائج",
                        15,
                        MUTED
                )
        );

        space(14);

        int studentsCount =
                students.size();

        int subjectsCount =
                subjects.size();

        double average =
                0;

        int count =
                0;

        for (int i = 0;
             i < studentsCount;
             i++) {

            double a =
                    getFinalStudentAverage(
                            i
                    );

            if (a > 0) {

                average +=
                        a;

                count++;
            }
        }

        if (count > 0) {

            average /=
                    count;
        }

        LinearLayout summary =
                makeCard();

        TextView summaryTitle =
                text(
                        "ملخص التقرير",
                        19,
                        TEXT
                );

        summaryTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        summary.addView(
                summaryTitle
        );

        addResultLine(
                summary,
                "عدد الطلاب",
                studentsCount
        );

        addResultLine(
                summary,
                "عدد المواد",
                subjectsCount
        );

        addResultLine(
                summary,
                "متوسط الصف",
                average
        );

        content.addView(
                summary
        );

        space(10);

        Button dashboard =
                actionButton(
                        "لوحة التحليل",
                        PRIMARY
                );

        content.addView(
                dashboard
        );

        dashboard.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        showAdvancedDashboard();
                    }
                }
        );

        Button back =
                actionButton(
                        "العودة",
                        MUTED
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(
                            View v
                    ) {

                        showReports();
                    }
                }
        );
    }

    // =========================================================
    // نهاية القسم 14
    // =========================================================
        // ==================== SECTION 15 ====================

@Override
protected void onStart() {
    super.onStart();

    try {
        grades.clear();
        loadGradeData();
        recalculateAllGrades();
    } catch (Exception e) {
        Toast.makeText(this, "تم فتح السجل", Toast.LENGTH_SHORT).show();
    }
}

private double safeGrade15(double value) {
    if (Double.isNaN(value) || Double.isInfinite(value)) {
        return 0;
    }

    if (value < 0) {
        return 0;
    }

    if (value > 100) {
        return 100;
    }

    return value;
}

private String safeText15(String value, String fallback) {
    if (value == null || value.trim().isEmpty()) {
        return fallback;
    }

    return value.trim();
}

private boolean validStudent15(int index) {
    return index >= 0 && index < students.size();
}

private boolean validSubject15(int index) {
    return index >= 0 && index < subjects.size();
}

private String studentName15(int index) {
    if (!validStudent15(index)) {
        return "طالب غير معروف";
    }

    return safeText15(students.get(index).name, "بدون اسم");
}

private String studentNumber15(int index) {
    if (!validStudent15(index)) {
        return "";
    }

    return safeText15(students.get(index).number, "");
}

private String subjectName15(int index) {
    if (!validSubject15(index)) {
        return "مادة غير معروفة";
    }

    return safeText15(subjects.get(index).name, "بدون مادة");
}

private boolean hasAcademicData15(GradeRecord r) {
    if (r == null) {
        return false;
    }

    return r.daily1 != 0 ||
            r.daily2 != 0 ||
            r.daily3 != 0 ||
            r.activity != 0 ||
            r.behavior != 0 ||
            r.month != 0 ||
            r.firstMonth != 0 ||
            r.secondMonth != 0 ||
            r.midYear != 0;
}

private int completedSubjects15(int studentIndex) {
    int count = 0;

    if (!validStudent15(studentIndex)) {
        return count;
    }

    for (int s = 0; s < subjects.size(); s++) {
        GradeRecord r = getGradeRecord(studentIndex, s);

        if (hasAcademicData15(r)) {
            count++;
        }
    }

    return count;
}

private double studentAverage15(int studentIndex) {
    if (!validStudent15(studentIndex)) {
        return 0;
    }

    double total = 0;
    int count = 0;

    for (int s = 0; s < subjects.size(); s++) {
        GradeRecord r = getGradeRecord(studentIndex, s);

        if (hasAcademicData15(r)) {
            updateAcademicRecord(r);
            total += safeGrade15(r.annual);
            count++;
        }
    }

    if (count == 0) {
        return 0;
    }

    return total / count;
}

private double classAverage15() {
    double total = 0;
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
        if (completedSubjects15(i) > 0) {
            total += studentAverage15(i);
            count++;
        }
    }

    if (count == 0) {
        return 0;
    }

    return total / count;
}

private double subjectAverage15(int subjectIndex) {
    if (!validSubject15(subjectIndex)) {
        return 0;
    }

    double total = 0;
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
        GradeRecord r = getGradeRecord(i, subjectIndex);

        if (hasAcademicData15(r)) {
            updateAcademicRecord(r);
            total += safeGrade15(r.annual);
            count++;
        }
    }

    if (count == 0) {
        return 0;
    }

    return total / count;
}

private int excellentCount15() {
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
        if (studentAverage15(i) >= 90) {
            count++;
        }
    }

    return count;
}

private int goodCount15() {
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
        double avg = studentAverage15(i);

        if (avg >= 70 && avg < 90) {
            count++;
        }
    }

    return count;
}

private int weakCount15() {
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
        double avg = studentAverage15(i);

        if (avg > 0 && avg < 50) {
            count++;
        }
    }

    return count;
}

private String statusText15(double average) {
    if (average >= 90) {
        return "ممتاز";
    }

    if (average >= 80) {
        return "جيد جداً";
    }

    if (average >= 70) {
        return "جيد";
    }

    if (average >= 50) {
        return "مقبول";
    }

    if (average > 0) {
        return "ضعيف";
    }

    return "لا توجد درجات";
}

private int statusColor15(double average) {
    if (average >= 90) {
        return SUCCESS;
    }

    if (average >= 70) {
        return PRIMARY;
    }

    if (average >= 50) {
        return WARNING;
    }

    return DANGER;
}

private void saveCurrentData15() {
    try {
        saveEverything();
    } catch (Exception e) {
        Toast.makeText(
                this,
                "تعذر حفظ البيانات",
                Toast.LENGTH_SHORT
        ).show();
    }
}

private void refreshAcademicData15() {
    try {
        recalculateAllGrades();
        saveCurrentData15();
    } catch (Exception ignored) {
    }
}

private void resetSelection15() {
    selectedStudent = -1;
    selectedSubject = -1;
}

private String buildStudentSummary15(int index) {
    if (!validStudent15(index)) {
        return "";
    }

    double average = studentAverage15(index);
    int completed = completedSubjects15(index);

    return studentName15(index)
            + " • "
            + "المعدل: "
            + formatNumber(average)
            + " • "
            + "المواد: "
            + completed
            + "/"
            + subjects.size();
}

private String buildClassSummary15() {
    return "الطلاب: "
            + students.size()
            + "   |   المواد: "
            + subjects.size()
            + "   |   معدل الصف: "
            + formatNumber(classAverage15());
}

private boolean studentHasAnyData15(int index) {
    return completedSubjects15(index) > 0;
}

private void showDataCheck15() {
    LinearLayout box = createBase("فحص السجل");

    addWelcome(
            box,
            "فحص البيانات",
            "ملخص سريع لحالة سجل الدرجات"
    );

    addInfoLine(
            box,
            "عدد الطلاب",
            String.valueOf(students.size())
    );

    addInfoLine(
            box,
            "عدد المواد",
            String.valueOf(subjects.size())
    );

    addInfoLine(
            box,
            "عدد السجلات",
            String.valueOf(getGradeRecordsCount())
    );

    addInfoLine(
            box,
            "معدل الصف",
            formatNumber(classAverage15())
    );

    addInfoLine(
            box,
            "الطلاب الممتازون",
            String.valueOf(excellentCount15())
    );

    addInfoLine(
            box,
            "الطلاب الجيدون",
            String.valueOf(goodCount15())
    );

    addInfoLine(
            box,
            "الطلاب الضعفاء",
            String.valueOf(weakCount15())
    );

    content.addView(box);

    content.addView(
            actionButton(
                    "العودة للرئيسية",
                    PRIMARY,
                    v -> showHome()
            )
    );
}

// ===== END SECTION 15 =====
            // ==================== SECTION 16 ====================

private void showSubjectsAnalysis16() {
    LinearLayout box = createBase("تحليل المواد");

    addWelcome(
            box,
            "تحليل المواد الدراسية",
            "نظرة شاملة على أداء الطلاب في كل مادة"
    );

    if (subjects.isEmpty()) {
        TextView empty = text(
                "لا توجد مواد مضافة حالياً",
                16,
                MUTED
        );

        empty.setGravity(Gravity.CENTER);
        box.addView(empty);

        box.addView(
                actionButton(
                        "إضافة مادة",
                        PRIMARY,
                        v -> showAddSubject()
                )
        );

        content.addView(box);
        return;
    }

    for (int s = 0; s < subjects.size(); s++) {
        addSubjectAnalysisCard16(box, s);
    }

    content.addView(box);

    content.addView(
            actionButton(
                    "العودة للرئيسية",
                    PRIMARY,
                    v -> showHome()
            )
    );
}

private void addSubjectAnalysisCard16(
        LinearLayout parent,
        int subjectIndex
) {
    if (!validSubject15(subjectIndex)) {
        return;
    }

    double average = subjectAverage15(subjectIndex);
    int studentsCount = 0;
    double highest = 0;
    double lowest = 101;
    String highestName = "";
    String lowestName = "";

    for (int i = 0; i < students.size(); i++) {
        GradeRecord r = getGradeRecord(i, subjectIndex);

        if (!hasAcademicData15(r)) {
            continue;
        }

        double value = safeGrade15(r.annual);

        studentsCount++;

        if (value > highest) {
            highest = value;
            highestName = studentName15(i);
        }

        if (value < lowest) {
            lowest = value;
            lowestName = studentName15(i);
        }
    }

    if (lowest == 101) {
        lowest = 0;
    }

    LinearLayout card = makeCard();

    TextView title = text(
            subjectName15(subjectIndex),
            19,
            TEXT
    );

    title.setTypeface(null, Typeface.BOLD);
    card.addView(title);

    card.addView(
            space(6)
    );

    addInfoLine(
            card,
            "متوسط المادة",
            formatNumber(average)
    );

    addInfoLine(
            card,
            "عدد الطلاب المسجلين",
            String.valueOf(studentsCount)
    );

    addInfoLine(
            card,
            "أعلى معدل",
            formatNumber(highest)
    );

    if (!highestName.isEmpty()) {
        addInfoLine(
                card,
                "صاحب أعلى معدل",
                highestName
        );
    }

    addInfoLine(
            card,
            "أدنى معدل",
            formatNumber(lowest)
    );

    if (!lowestName.isEmpty()) {
        addInfoLine(
                card,
                "صاحب أدنى معدل",
                lowestName
        );
    }

    TextView status = text(
            "حالة المادة: " + statusText15(average),
            14,
            statusColor15(average)
    );

    status.setTypeface(null, Typeface.BOLD);

    card.addView(
            space(4)
    );

    card.addView(status);

    Button open = actionButton(
            "عرض نتائج المادة",
            PRIMARY,
            v -> {
                selectedSubject = subjectIndex;
                showSubjectResults(subjectIndex);
            }
    );

    card.addView(
            space(6)
    );

    card.addView(open);

    parent.addView(card);

    parent.addView(
            space(8)
    );
}

private int countPassingStudents16(int subjectIndex) {
    int count = 0;

    if (!validSubject15(subjectIndex)) {
        return count;
    }

    for (int i = 0; i < students.size(); i++) {
        GradeRecord r = getGradeRecord(i, subjectIndex);

        if (!hasAcademicData15(r)) {
            continue;
        }

        updateAcademicRecord(r);

        if (r.annual >= 50) {
            count++;
        }
    }

    return count;
}

private int countFailingStudents16(int subjectIndex) {
    int count = 0;

    if (!validSubject15(subjectIndex)) {
        return count;
    }

    for (int i = 0; i < students.size(); i++) {
        GradeRecord r = getGradeRecord(i, subjectIndex);

        if (!hasAcademicData15(r)) {
            continue;
        }

        updateAcademicRecord(r);

        if (r.annual < 50) {
            count++;
        }
    }

    return count;
}

private double subjectPassRate16(int subjectIndex) {
    int total = 0;
    int passed = 0;

    if (!validSubject15(subjectIndex)) {
        return 0;
    }

    for (int i = 0; i < students.size(); i++) {
        GradeRecord r = getGradeRecord(i, subjectIndex);

        if (!hasAcademicData15(r)) {
            continue;
        }

        total++;
        updateAcademicRecord(r);

        if (r.annual >= 50) {
            passed++;
        }
    }

    if (total == 0) {
        return 0;
    }

    return (passed * 100.0) / total;
}

private String subjectPerformance16(int subjectIndex) {
    double average = subjectAverage15(subjectIndex);

    if (average >= 90) {
        return "أداء ممتاز";
    }

    if (average >= 80) {
        return "أداء مرتفع";
    }

    if (average >= 70) {
        return "أداء جيد";
    }

    if (average >= 50) {
        return "أداء مقبول";
    }

    if (average > 0) {
        return "تحتاج إلى متابعة";
    }

    return "لا توجد بيانات كافية";
}

private void showSubjectStatistics16(int subjectIndex) {
    if (!validSubject15(subjectIndex)) {
        return;
    }

    LinearLayout box = createBase(
            "إحصائيات " + subjectName15(subjectIndex)
    );

    addWelcome(
            box,
            subjectName15(subjectIndex),
            "إحصائيات تفصيلية للمادة"
    );

    double average = subjectAverage15(subjectIndex);
    double passRate = subjectPassRate16(subjectIndex);

    addInfoLine(
            box,
            "المتوسط",
            formatNumber(average)
    );

    addInfoLine(
            box,
            "الناجحون",
            String.valueOf(
                    countPassingStudents16(subjectIndex)
            )
    );

    addInfoLine(
            box,
            "الراسبون",
            String.valueOf(
                    countFailingStudents16(subjectIndex)
            )
    );

    addInfoLine(
            box,
            "نسبة النجاح",
            formatNumber(passRate) + "%"
    );

    addInfoLine(
            box,
            "التقييم العام",
            subjectPerformance16(subjectIndex)
    );

    content.addView(box);

    content.addView(
            actionButton(
                    "عرض الطلاب",
                    PRIMARY,
                    v -> showSubjectResults(subjectIndex)
            )
    );

    content.addView(
            actionButton(
                    "العودة لتحليل المواد",
                    SUCCESS,
                    v -> showSubjectsAnalysis16()
            )
    );
}

// ==================== END SECTION 16 ====================
            // ==================== SECTION 17 ====================

private void showStudentRanking17() {
    LinearLayout box = createBase("ترتيب الطلاب");

    addWelcome(
            box,
            "الترتيب العام",
            "ترتيب الطلاب حسب المعدل السنوي"
    );

    ArrayList<Integer> ranking = getSortedStudentIndexes();

    if (ranking.isEmpty()) {
        TextView empty = text(
                "لا توجد درجات كافية لعرض الترتيب",
                16,
                MUTED
        );

        empty.setGravity(Gravity.CENTER);
        box.addView(empty);
        content.addView(box);
        return;
    }

    int position = 1;

    for (Integer index : ranking) {
        if (!validStudent15(index)) {
            continue;
        }

        double average = studentAverage15(index);

        if (average <= 0) {
            continue;
        }

        LinearLayout card = makeCard();

        TextView rank = text(
                "المركز " + position,
                17,
                PRIMARY
        );

        rank.setTypeface(null, Typeface.BOLD);
        card.addView(rank);

        card.addView(space(4));

        TextView name = text(
                studentName15(index),
                18,
                TEXT
        );

        name.setTypeface(null, Typeface.BOLD);
        card.addView(name);

        addInfoLine(
                card,
                "الرقم",
                studentNumber15(index)
        );

        addInfoLine(
                card,
                "المعدل",
                formatNumber(average)
        );

        addInfoLine(
                card,
                "التقدير",
                statusText15(average)
        );

        addInfoLine(
                card,
                "المواد المكتملة",
                completedSubjects15(index)
                        + "/"
                        + subjects.size()
        );

        Button details = actionButton(
                "عرض ملف الطالب",
                PRIMARY,
                v -> showStudentDetails(index)
        );

        card.addView(space(6));
        card.addView(details);

        box.addView(card);
        box.addView(space(8));

        position++;
    }

    content.addView(box);

    content.addView(
            actionButton(
                    "العودة للرئيسية",
                    PRIMARY,
                    v -> showHome()
            )
    );
}

private void showStudentProgress17(int studentIndex) {
    if (!validStudent15(studentIndex)) {
        return;
    }

    LinearLayout box = createBase(
            "تقدم الطالب"
    );

    double average = studentAverage15(studentIndex);

    addWelcome(
            box,
            studentName15(studentIndex),
            "متابعة الأداء الأكاديمي"
    );

    addInfoLine(
            box,
            "المعدل الحالي",
            formatNumber(average)
    );

    addInfoLine(
            box,
            "التقدير",
            statusText15(average)
    );

    addInfoLine(
            box,
            "المواد المكتملة",
            completedSubjects15(studentIndex)
                    + "/" + subjects.size()
    );

    for (int s = 0; s < subjects.size(); s++) {
        GradeRecord r = getGradeRecord(
                studentIndex,
                s
        );

        if (!hasAcademicData15(r)) {
            continue;
        }

        updateAcademicRecord(r);

        LinearLayout subjectCard = makeCard();

        TextView title = text(
                subjectName15(s),
                16,
                TEXT
        );

        title.setTypeface(null, Typeface.BOLD);
        subjectCard.addView(title);

        addInfoLine(
                subjectCard,
                "الدرجة السنوية",
                formatNumber(r.annual)
        );

        addInfoLine(
                subjectCard,
                "حالة المادة",
                statusText15(r.annual)
        );

        box.addView(subjectCard);
        box.addView(space(6));
    }

    content.addView(box);

    content.addView(
            actionButton(
                    "تعديل درجات الطالب",
                    PRIMARY,
                    v -> openStudentGradeEntry(
                            studentIndex
                    )
            )
    );

    content.addView(
            actionButton(
                    "العودة",
                    SUCCESS,
                    v -> showStudentRanking17()
            )
    );
}

private int findStudentRank17(int studentIndex) {
    ArrayList<Integer> ranking =
            getSortedStudentIndexes();

    int rank = 0;

    for (Integer index : ranking) {
        if (!validStudent15(index)) {
            continue;
        }

        if (studentAverage15(index) <= 0) {
            continue;
        }

        rank++;

        if (index == studentIndex) {
            return rank;
        }
    }

    return 0;
}

private String rankText17(int studentIndex) {
    int rank = findStudentRank17(studentIndex);

    if (rank == 0) {
        return "غير مصنف";
    }

    return "المركز " + rank;
}

private double bestStudentAverage17() {
    double best = 0;

    for (int i = 0; i < students.size(); i++) {
        double average = studentAverage15(i);

        if (average > best) {
            best = average;
        }
    }

    return best;
}

private double lowestStudentAverage17() {
    double lowest = 101;

    for (int i = 0; i < students.size(); i++) {
        double average = studentAverage15(i);

        if (average > 0 && average < lowest) {
            lowest = average;
        }
    }

    return lowest == 101 ? 0 : lowest;
}

private void showRankingSummary17() {
    LinearLayout box =
            createBase("ملخص الترتيب");

    addWelcome(
            box,
            "ملخص أداء الصف",
            "إحصائيات سريعة عن ترتيب الطلاب"
    );

    addInfoLine(
            box,
            "معدل الصف",
            formatNumber(classAverage15())
    );

    addInfoLine(
            box,
            "أعلى معدل",
            formatNumber(
                    bestStudentAverage17()
            )
    );

    addInfoLine(
            box,
            "أدنى معدل",
            formatNumber(
                    lowestStudentAverage17()
            )
    );

    addInfoLine(
            box,
            "عدد الممتازين",
            String.valueOf(
                    excellentCount15()
            )
    );

    addInfoLine(
            box,
            "عدد الضعفاء",
            String.valueOf(
                    weakCount15()
            )
    );

    content.addView(box);

    content.addView(
            actionButton(
                    "عرض الترتيب",
                    PRIMARY,
                    v -> showStudentRanking17()
            )
    );

    content.addView(
            actionButton(
                    "العودة للرئيسية",
                    SUCCESS,
                    v -> showHome()
            )
    );
}

// ==================== END SECTION 17 ====================
            // ==================== SECTION 18 ====================

private void showStudentPerformance18(int studentIndex) {
    if (!validStudent15(studentIndex)) {
        return;
    }

    LinearLayout box = createBase("أداء الطالب");

    double average = studentAverage15(studentIndex);
    int completed = completedSubjects15(studentIndex);
    int rank = findStudentRank17(studentIndex);

    addWelcome(
            box,
            studentName15(studentIndex),
            "لوحة الأداء الأكاديمي للطالب"
    );

    addInfoLine(
            box,
            "رقم الطالب",
            studentNumber15(studentIndex)
    );

    addInfoLine(
            box,
            "المعدل السنوي",
            formatNumber(average)
    );

    addInfoLine(
            box,
            "التقدير",
            statusText15(average)
    );

    addInfoLine(
            box,
            "الترتيب",
            rank == 0 ? "غير مصنف" : String.valueOf(rank)
    );

    addInfoLine(
            box,
            "المواد المكتملة",
            completed + "/" + subjects.size()
    );

    addInfoLine(
            box,
            "نسبة الإنجاز",
            subjects.isEmpty()
                    ? "0%"
                    : formatNumber(
                            completed * 100.0 / subjects.size()
                    ) + "%"
    );

    addPerformanceBar18(
            box,
            average
    );

    content.addView(box);

    addSubjectPerformance18(
            studentIndex
    );

    content.addView(
            actionButton(
                    "تعديل درجات الطالب",
                    PRIMARY,
                    v -> openStudentGradeEntry(
                            studentIndex
                    )
            )
    );

    content.addView(
            actionButton(
                    "العودة",
                    SUCCESS,
                    v -> showStudentRanking17()
            )
    );
}

private void addPerformanceBar18(
        LinearLayout parent,
        double average
) {
    TextView title = text(
            "مستوى الأداء",
            15,
            TEXT
    );

    title.setTypeface(
            null,
            Typeface.BOLD
    );

    parent.addView(title);
    parent.addView(space(5));

    ProgressBar progress =
            new ProgressBar(
                    this,
                    null,
                    android.R.attr.progressBarStyleHorizontal
            );

    progress.setMax(100);
    progress.setProgress(
            (int) Math.round(
                    Math.max(
                            0,
                            Math.min(100, average)
                    )
            )
    );

    LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                    -1,
                    24
            );

    params.setMargins(
            0,
            4,
            0,
            10
    );

    progress.setLayoutParams(params);
    parent.addView(progress);
}

private void addSubjectPerformance18(
        int studentIndex
) {
    LinearLayout box =
            createBase("تفاصيل المواد");

    for (int s = 0; s < subjects.size(); s++) {
        GradeRecord r =
                getGradeRecord(
                        studentIndex,
                        s
                );

        if (!hasAcademicData15(r)) {
            continue;
        }

        updateAcademicRecord(r);

        LinearLayout card =
                makeCard();

        TextView title = text(
                subjectName15(s),
                17,
                TEXT
        );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(title);
        card.addView(space(4));

        addInfoLine(
                card,
                "الشهر الأول",
                formatNumber(
                        r.firstMonth
                )
        );

        addInfoLine(
                card,
                "الشهر الثاني",
                formatNumber(
                        r.secondMonth
                )
        );

        addInfoLine(
                card,
                "السعي الأول",
                formatNumber(
                        r.firstTerm
                )
        );

        addInfoLine(
                card,
                "نصف السنة",
                formatNumber(
                        r.midYear
                )
        );

        addInfoLine(
                card,
                "السعي الثاني",
                formatNumber(
                        r.secondTerm
                )
        );

        addInfoLine(
                card,
                "السنوي",
                formatNumber(
                        r.annual
                )
        );

        TextView state = text(
                statusText15(r.annual),
                14,
                statusColor15(r.annual)
        );

        state.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(space(3));
        card.addView(state);

        box.addView(card);
        box.addView(space(7));
    }

    content.addView(box);
}

private int countExcellentSubjects18(
        int studentIndex
) {
    int count = 0;

    for (int s = 0; s < subjects.size(); s++) {
        GradeRecord r =
                getGradeRecord(
                        studentIndex,
                        s
                );

        if (!hasAcademicData15(r)) {
            continue;
        }

        updateAcademicRecord(r);

        if (r.annual >= 90) {
            count++;
        }
    }

    return count;
}

private int countWeakSubjects18(
        int studentIndex
) {
    int count = 0;

    for (int s = 0; s < subjects.size(); s++) {
        GradeRecord r =
                getGradeRecord(
                        studentIndex,
                        s
                );

        if (!hasAcademicData15(r)) {
            continue;
        }

        updateAcademicRecord(r);

        if (r.annual > 0 && r.annual < 50) {
            count++;
        }
    }

    return count;
}

private String performanceMessage18(
        double average
) {
    if (average >= 90) {
        return "أداء ممتاز جداً";
    }

    if (average >= 80) {
        return "أداء ممتاز";
    }

    if (average >= 70) {
        return "أداء جيد ومستقر";
    }

    if (average >= 50) {
        return "يحتاج إلى تحسين";
    }

    if (average > 0) {
        return "يحتاج إلى متابعة مستمرة";
    }

    return "لا توجد درجات كافية";
}

private void showStudentQuickStats18(
        int studentIndex
) {
    if (!validStudent15(studentIndex)) {
        return;
    }

    LinearLayout box =
            createBase("إحصائيات الطالب");

    double average =
            studentAverage15(studentIndex);

    addWelcome(
            box,
            "إحصائيات الأداء",
            studentName15(studentIndex)
    );

    addInfoLine(
            box,
            "المعدل",
            formatNumber(average)
    );

    addInfoLine(
            box,
            "التقييم",
            performanceMessage18(
                    average
            )
    );

    addInfoLine(
            box,
            "المواد الممتازة",
            String.valueOf(
                    countExcellentSubjects18(
                            studentIndex
                    )
            )
    );

    addInfoLine(
            box,
            "المواد الضعيفة",
            String.valueOf(
                    countWeakSubjects18(
                            studentIndex
                    )
            )
    );

    addInfoLine(
            box,
            "الترتيب",
            rankText17(studentIndex)
    );

    content.addView(box);

    content.addView(
            actionButton(
                    "عرض الأداء الكامل",
                    PRIMARY,
                    v -> showStudentPerformance18(
                            studentIndex
                    )
            )
    );

    content.addView(
            actionButton(
                    "العودة",
                    SUCCESS,
                    v -> showHome()
            )
    );
}

// ==================== END SECTION 18 ====================
            // ==================== SECTION 19 ====================

private void showComparison19() {
    LinearLayout box = createBase("المقارنة");

    addWelcome(
            box,
            "مقارنة الأداء",
            "مقارنة الطلاب والمواد داخل الصف"
    );

    if (students.isEmpty()) {
        addInfoLine(
                box,
                "الحالة",
                "لا يوجد طلاب"
        );

        content.addView(box);
        return;
    }

    addComparisonSummary19(box);
    addStudentComparison19(box);
    addSubjectComparison19(box);

    content.addView(box);

    content.addView(
            actionButton(
                    "العودة للرئيسية",
                    PRIMARY,
                    v -> showHome()
            )
    );
}

private void addComparisonSummary19(
        LinearLayout parent
) {
    double classAverage =
            classAverage15();

    double highest =
            bestStudentAverage17();

    double lowest =
            lowestStudentAverage17();

    LinearLayout card = makeCard();

    TextView title = text(
            "ملخص الصف",
            18,
            TEXT
    );

    title.setTypeface(
            null,
            Typeface.BOLD
    );

    card.addView(title);
    card.addView(space(5));

    addInfoLine(
            card,
            "متوسط الصف",
            formatNumber(classAverage)
    );

    addInfoLine(
            card,
            "أعلى معدل",
            formatNumber(highest)
    );

    addInfoLine(
            card,
            "أدنى معدل",
            formatNumber(lowest)
    );

    addInfoLine(
            card,
            "الفارق",
            formatNumber(
                    Math.max(0, highest - lowest)
            )
    );

    parent.addView(card);
    parent.addView(space(8));
}

private void addStudentComparison19(
        LinearLayout parent
) {
    LinearLayout card = makeCard();

    TextView title = text(
            "مقارنة الطلاب",
            18,
            TEXT
    );

    title.setTypeface(
            null,
            Typeface.BOLD
    );

    card.addView(title);
    card.addView(space(5));

    ArrayList<Integer> ranking =
            getSortedStudentIndexes();

    int shown = 0;

    for (Integer index : ranking) {
        if (!validStudent15(index)) {
            continue;
        }

        double average =
                studentAverage15(index);

        if (average <= 0) {
            continue;
        }

        TextView row = text(
                (shown + 1)
                        + ". "
                        + studentName15(index)
                        + " — "
                        + formatNumber(average),
                15,
                statusColor15(average)
        );

        row.setPadding(
                8,
                10,
                8,
                10
        );

        card.addView(row);

        shown++;

        if (shown >= 10) {
            break;
        }
    }

    if (shown == 0) {
        card.addView(
                text(
                        "لا توجد درجات كافية",
                        14,
                        MUTED
                )
        );
    }

    parent.addView(card);
    parent.addView(space(8));
}

private void addSubjectComparison19(
        LinearLayout parent
) {
    LinearLayout card = makeCard();

    TextView title = text(
            "مقارنة المواد",
            18,
            TEXT
    );

    title.setTypeface(
            null,
            Typeface.BOLD
    );

    card.addView(title);
    card.addView(space(5));

    for (int s = 0; s < subjects.size(); s++) {
        double average =
                subjectAverage15(s);

        TextView row = text(
                subjectName15(s)
                        + " — "
                        + formatNumber(average),
                15,
                statusColor15(average)
        );

        row.setPadding(
                8,
                10,
                8,
                10
        );

        card.addView(row);
    }

    if (subjects.isEmpty()) {
        card.addView(
                text(
                        "لا توجد مواد",
                        14,
                        MUTED
                )
        );
    }

    parent.addView(card);
}

private int bestStudentIndex19() {
    int bestIndex = -1;
    double bestAverage = 0;

    for (int i = 0; i < students.size(); i++) {
        double average =
                studentAverage15(i);

        if (average > bestAverage) {
            bestAverage = average;
            bestIndex = i;
        }
    }

    return bestIndex;
}

private int weakestStudentIndex19() {
    int weakIndex = -1;
    double lowest = 101;

    for (int i = 0; i < students.size(); i++) {
        double average =
                studentAverage15(i);

        if (average > 0 && average < lowest) {
            lowest = average;
            weakIndex = i;
        }
    }

    return weakIndex;
}

private int bestSubjectIndex19() {
    int bestIndex = -1;
    double bestAverage = 0;

    for (int s = 0; s < subjects.size(); s++) {
        double average =
                subjectAverage15(s);

        if (average > bestAverage) {
            bestAverage = average;
            bestIndex = s;
        }
    }

    return bestIndex;
}

private int weakestSubjectIndex19() {
    int weakIndex = -1;
    double lowest = 101;

    for (int s = 0; s < subjects.size(); s++) {
        double average =
                subjectAverage15(s);

        if (average > 0 && average < lowest) {
            lowest = average;
            weakIndex = s;
        }
    }

    return weakIndex;
}

private void showBestAndWeakest19() {
    LinearLayout box =
            createBase("الأفضل والأضعف");

    addWelcome(
            box,
            "تحليل الأداء",
            "أعلى وأدنى نتائج مسجلة"
    );

    int bestStudent =
            bestStudentIndex19();

    int weakStudent =
            weakestStudentIndex19();

    int bestSubject =
            bestSubjectIndex19();

    int weakSubject =
            weakestSubjectIndex19();

    if (bestStudent >= 0) {
        addInfoLine(
                box,
                "أفضل طالب",
                studentName15(bestStudent)
                        + " — "
                        + formatNumber(
                                studentAverage15(
                                        bestStudent
                                )
                        )
        );
    }

    if (weakStudent >= 0) {
        addInfoLine(
                box,
                "أضعف طالب",
                studentName15(weakStudent)
                        + " — "
                        + formatNumber(
                                studentAverage15(
                                        weakStudent
                                )
                        )
        );
    }

    if (bestSubject >= 0) {
        addInfoLine(
                box,
                "أفضل مادة",
                subjectName15(bestSubject)
                        + " — "
                        + formatNumber(
                                subjectAverage15(
                                        bestSubject
                                )
                        )
        );
    }

    if (weakSubject >= 0) {
        addInfoLine(
                box,
                "أضعف مادة",
                subjectName15(weakSubject)
                        + " — "
                        + formatNumber(
                                subjectAverage15(
                                        weakSubject
                                )
                        )
        );
    }

    content.addView(box);

    content.addView(
            actionButton(
                    "عرض المقارنة",
                    PRIMARY,
                    v -> showComparison19()
            )
    );

    content.addView(
            actionButton(
                    "الرئيسية",
                    SUCCESS,
                    v -> showHome()
            )
    );
}

// ==================== END SECTION 19 ====================

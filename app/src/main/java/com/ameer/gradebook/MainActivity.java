package com.ameer.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    // =====================================================
    // ألوان التطبيق
    // =====================================================

    private final int NAVY = Color.rgb(8, 29, 58);
    private final int BLUE = Color.rgb(25, 103, 210);
    private final int GREEN = Color.rgb(25, 166, 110);
    private final int GOLD = Color.rgb(218, 169, 55);
    private final int PURPLE = Color.rgb(128, 83, 190);

    private final int WHITE = Color.WHITE;
    private final int TEXT = Color.rgb(25, 38, 58);
    private final int GRAY = Color.rgb(105, 117, 135);
    private final int BACKGROUND = Color.rgb(246, 248, 252);

    // =====================================================
    // قائمة الطلاب
    // =====================================================

    private ArrayList<String> students =
            new ArrayList<String>();

    // =====================================================
    // بداية التطبيق
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);

        createHomeScreen();
    }

    // =====================================================
    // الشاشة الرئيسية
    // =====================================================

    private void createHomeScreen() {

        LinearLayout root =
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

        ScrollView scrollView =
                new ScrollView(this);

        scrollView.setFillViewport(true);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(30)
        );

        content.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        // =================================================
        // رأس التطبيق
        // =================================================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        header.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable headerBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{
                                NAVY,
                                Color.rgb(17, 55, 103)
                        }
                );

        headerBg.setCornerRadius(
                dp(24)
        );

        header.setBackground(
                headerBg
        );

        TextView logo =
                createText(
                        "🎓",
                        30,
                        WHITE,
                        false
                );

        logo.setGravity(
                Gravity.CENTER
        );

        GradientDrawable logoBg =
                new GradientDrawable();

        logoBg.setColor(
                Color.rgb(35, 76, 125)
        );

        logoBg.setCornerRadius(
                dp(17)
        );

        logo.setBackground(
                logoBg
        );

        header.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(58),
                        dp(58)
                )
        );

        LinearLayout titleBox =
                new LinearLayout(this);

        titleBox.setOrientation(
                LinearLayout.VERTICAL
        );

        titleBox.setGravity(
                Gravity.RIGHT
        );

        TextView title =
                createText(
                        "سجل الدرجات",
                        24,
                        WHITE,
                        true
                );

        TextView year =
                createText(
                        "العام الدراسي 2026 - 2027",
                        13,
                        Color.rgb(
                                205,
                                220,
                                240
                        ),
                        false
                );

        titleBox.addView(title);
        titleBox.addView(verticalSpace(4));
        titleBox.addView(year);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        titleParams.setMargins(
                dp(12),
                0,
                0,
                0
        );

        header.addView(
                titleBox,
                titleParams
        );

        content.addView(header);

        content.addView(
                verticalSpace(18)
        );

        // =================================================
        // بطاقة الترحيب
        // =================================================

        LinearLayout welcomeCard =
                createCard();

        TextView welcomeTitle =
                createText(
                        "مرحبًا بك 👋",
                        20,
                        TEXT,
                        true
                );

        TextView welcomeText =
                createText(
                        "إدارة درجات الطلاب بطريقة سهلة ومنظمة",
                        14,
                        GRAY,
                        false
                );

        welcomeCard.addView(
                welcomeTitle
        );

        welcomeCard.addView(
                verticalSpace(5)
        );

        welcomeCard.addView(
                welcomeText
        );

        content.addView(
                welcomeCard
        );

        content.addView(
                verticalSpace(18)
        );

        // =================================================
        // الوصول السريع
        // =================================================

        TextView quickTitle =
                createText(
                        "الوصول السريع",
                        18,
                        TEXT,
                        true
                );

        content.addView(
                quickTitle
        );

        content.addView(
                verticalSpace(10)
        );

        LinearLayout row1 =
                new LinearLayout(this);

        row1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row1.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        // بطاقة الطلاب

        TextView studentsCard =
                createDashboardCard(
                        "👨‍🎓",
                        "الطلاب",
                        "إدارة الطلاب والشعب",
                        BLUE
                );

        studentsCard.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        createStudentsScreen();
                    }
                }
        );

        row1.addView(
                studentsCard,
                cardParams()
        );

        row1.addView(
                horizontalSpace(10)
        );

        // بطاقة الدرجات

        TextView gradesCard =
                createDashboardCard(
                        "📝",
                        "الدرجات",
                        "إدخال وتعديل الدرجات",
                        GREEN
                );

        gradesCard.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showMessage(
                                "قسم الدرجات قيد التطوير"
                        );
                    }
                }
        );

        row1.addView(
                gradesCard,
                cardParams()
        );

        content.addView(row1);

        content.addView(
                verticalSpace(10)
        );

        // =================================================
        // الصف الثاني
        // =================================================

        LinearLayout row2 =
                new LinearLayout(this);

        row2.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row2.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        TextView reportsCard =
                createDashboardCard(
                        "📊",
                        "التقارير",
                        "عرض وطباعة النتائج",
                        GOLD
                );

        reportsCard.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showMessage(
                                "قسم التقارير قيد التطوير"
                        );
                    }
                }
        );

        row2.addView(
                reportsCard,
                cardParams()
        );

        row2.addView(
                horizontalSpace(10)
        );

        TextView resultsCard =
                createDashboardCard(
                        "🏆",
                        "النتائج",
                        "المتفوقون والنتائج",
                        PURPLE
                );

        resultsCard.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showMessage(
                                "قسم النتائج قيد التطوير"
                        );
                    }
                }
        );

        row2.addView(
                resultsCard,
                cardParams()
        );

        content.addView(row2);

        content.addView(
                verticalSpace(18)
        );

        // =================================================
        // آخر العمليات
        // =================================================

        TextView activityTitle =
                createText(
                        "آخر العمليات",
                        18,
                        TEXT,
                        true
                );

        content.addView(
                activityTitle
        );

        content.addView(
                verticalSpace(10)
        );

        LinearLayout activityCard =
                createCard();

        addActivity(
                activityCard,
                "➕",
                "إضافة طالب جديد",
                "جاهز لإضافة الطلاب",
                "الآن"
        );

        addDivider(
                activityCard
        );

        addActivity(
                activityCard,
                "📝",
                "إدخال درجات الفصل الأول",
                "سجل الدرجات",
                "قريبًا"
        );

        addDivider(
                activityCard
        );

        addActivity(
                activityCard,
                "📊",
                "إعداد التقارير",
                "تقارير الطلاب",
                "قريبًا"
        );

        content.addView(
                activityCard
        );

        // =================================================
        // نهاية القسم الأول
        // =================================================
            // =================================================
        // زر الإعدادات
        // =================================================

        content.addView(
                verticalSpace(18)
        );

        TextView settingsButton =
                createButton(
                        "⚙️   الإعدادات والنسخ الاحتياطي",
                        NAVY
                );

        settingsButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showMessage(
                                "الإعدادات قيد التطوير"
                        );
                    }
                }
        );

        content.addView(
                settingsButton
        );

        // =================================================
        // إضافة المحتوى
        // =================================================

        scrollView.addView(
                content
        );

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(
                root
        );
    }

    // =====================================================
    // شاشة الطلاب
    // =====================================================

    private void createStudentsScreen() {

        LinearLayout root =
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

        // =================================================
        // رأس شاشة الطلاب
        // =================================================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(15)
        );

        header.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable headerBg =
                new GradientDrawable();

        headerBg.setColor(
                NAVY
        );

        headerBg.setCornerRadius(
                dp(22)
        );

        header.setBackground(
                headerBg
        );

        // زر الرجوع

        TextView back =
                createText(
                        "‹",
                        35,
                        WHITE,
                        false
                );

        back.setGravity(
                Gravity.CENTER
        );

        back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        createHomeScreen();
                    }
                }
        );

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(50),
                        dp(50)
                )
        );

        // العنوان

        LinearLayout headerTitle =
                new LinearLayout(this);

        headerTitle.setOrientation(
                LinearLayout.VERTICAL
        );

        headerTitle.setGravity(
                Gravity.RIGHT
        );

        TextView pageTitle =
                createText(
                        "إدارة الطلاب",
                        22,
                        WHITE,
                        true
                );

        TextView pageSubtitle =
                createText(
                        "إضافة وتنظيم بيانات الطلاب",
                        12,
                        Color.rgb(
                                205,
                                220,
                                240
                        ),
                        false
                );

        headerTitle.addView(
                pageTitle
        );

        headerTitle.addView(
                verticalSpace(3)
        );

        headerTitle.addView(
                pageSubtitle
        );

        header.addView(
                headerTitle,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        TextView studentIcon =
                createText(
                        "👨‍🎓",
                        25,
                        WHITE,
                        false
                );

        studentIcon.setGravity(
                Gravity.CENTER
        );

        header.addView(
                studentIcon,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(55)
                )
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        // =================================================
        // محتوى شاشة الطلاب
        // =================================================

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(30)
        );

        content.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        // =================================================
        // الإحصائيات
        // =================================================

        LinearLayout stats =
                createCard();

        TextView statsTitle =
                createText(
                        "ملخص الطلاب",
                        17,
                        TEXT,
                        true
                );

        stats.addView(
                statsTitle
        );

        stats.addView(
                verticalSpace(12)
        );

        LinearLayout statsRow =
                new LinearLayout(this);

        statsRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        statsRow.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        TextView totalStat =
                createStat(
                        String.valueOf(
                                students.size()
                        ),
                        "إجمالي الطلاب",
                        BLUE
                );

        statsRow.addView(
                totalStat,
                new LinearLayout.LayoutParams(
                        0,
                        dp(85),
                        1
                )
        );

        statsRow.addView(
                horizontalSpace(8)
        );

        TextView classesStat =
                createStat(
                        "1",
                        "الشعب",
                        GREEN
                );

        statsRow.addView(
                classesStat,
                new LinearLayout.LayoutParams(
                        0,
                        dp(85),
                        1
                )
        );

        stats.addView(
                statsRow
        );

        content.addView(
                stats
        );

        content.addView(
                verticalSpace(18)
        );

        // =================================================
        // زر إضافة طالب
        // =================================================

        TextView addStudent =
                createButton(
                        "＋   إضافة طالب جديد",
                        BLUE
                );

        addStudent.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showAddStudentDialog();
                    }
                }
        );

        content.addView(
                addStudent
        );

        content.addView(
                verticalSpace(18)
        );

        // =================================================
        // عنوان القائمة
        // =================================================

        TextView listTitle =
                createText(
                        "قائمة الطلاب",
                        18,
                        TEXT,
                        true
                );

        content.addView(
                listTitle
        );

        content.addView(
                verticalSpace(10)
        );

        // =================================================
        // قائمة الطلاب
        // =================================================

        if (students.size() == 0) {

            LinearLayout empty =
                    createCard();

            empty.setGravity(
                    Gravity.CENTER
            );

            TextView emptyIcon =
                    createText(
                            "👨‍🎓",
                            45,
                            BLUE,
                            false
                    );

            emptyIcon.setGravity(
                    Gravity.CENTER
            );

            TextView emptyTitle =
                    createText(
                            "لا يوجد طلاب حتى الآن",
                            17,
                            TEXT,
                            true
                    );

            emptyTitle.setGravity(
                    Gravity.CENTER
            );

            TextView emptyText =
                    createText(
                            "اضغط على «إضافة طالب جديد» لبدء إدخال الطلاب",
                            13,
                            GRAY,
                            false
                    );

            emptyText.setGravity(
                    Gravity.CENTER
            );

            empty.addView(
                    emptyIcon
            );

            empty.addView(
                    verticalSpace(8)
            );

            empty.addView(
                    emptyTitle
            );

            empty.addView(
                    verticalSpace(5)
            );

            empty.addView(
                    emptyText
            );

            content.addView(
                    empty
            );

        } else {

            for (
                    int i = 0;
                    i < students.size();
                    i++
            ) {

                addStudentRow(
                        content,
                        i
                );

                if (
                        i <
                        students.size() - 1
                ) {

                    content.addView(
                            verticalSpace(8)
                    );
                }
            }
        }

        // =================================================
        // إنهاء الشاشة
        // =================================================

        scroll.addView(
                content
        );

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(
                root
        );

        // =================================================
        // نهاية القسم الثاني
        // =================================================
            // =================================================
        // نهاية القسم الثالث
        // =================================================

    }

    // =====================================================
    // نافذة إضافة طالب
    // =====================================================

    private void showAddStudentDialog() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(24),
                dp(10),
                dp(24),
                dp(5)
        );

        box.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        TextView nameLabel =
                createText(
                        "اسم الطالب",
                        14,
                        TEXT,
                        true
                );

        box.addView(
                nameLabel
        );

        box.addView(
                verticalSpace(6)
        );

        final EditText name =
                new EditText(this);

        name.setHint(
                "اكتب اسم الطالب"
        );

        name.setTextSize(
                15
        );

        name.setSingleLine(
                true
        );

        name.setInputType(
                InputType.TYPE_CLASS_TEXT
        );

        name.setGravity(
                Gravity.RIGHT
        );

        name.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        box.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        box.addView(
                verticalSpace(12)
        );

        TextView classLabel =
                createText(
                        "الصف والشعبة",
                        14,
                        TEXT,
                        true
                );

        box.addView(
                classLabel
        );

        box.addView(
                verticalSpace(6)
        );

        final EditText className =
                new EditText(this);

        className.setHint(
                "مثال: الثالث المتوسط / أ"
        );

        className.setTextSize(
                15
        );

        className.setSingleLine(
                true
        );

        className.setGravity(
                Gravity.RIGHT
        );

        className.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        box.addView(
                className,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "إضافة طالب جديد"
                        )
                        .setView(box)
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
                new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(
                            DialogInterface d
                    ) {

                        dialog.getButton(
                                AlertDialog.BUTTON_POSITIVE
                        ).setOnClickListener(
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            View v
                                    ) {

                                        String studentName =
                                                name.getText()
                                                        .toString()
                                                        .trim();

                                        String studentClass =
                                                className.getText()
                                                        .toString()
                                                        .trim();

                                        if (
                                                studentName.length()
                                                == 0
                                        ) {

                                            name.setError(
                                                    "أدخل اسم الطالب"
                                            );

                                            return;
                                        }

                                        if (
                                                studentClass.length()
                                                == 0
                                        ) {

                                            className.setError(
                                                    "أدخل الصف والشعبة"
                                            );

                                            return;
                                        }

                                        String student =
                                                studentName
                                                + "  •  "
                                                + studentClass;

                                        students.add(
                                                student
                                        );

                                        dialog.dismiss();

                                        createStudentsScreen();

                                        showMessage(
                                                "تمت إضافة الطالب بنجاح"
                                        );
                                    }
                                }
                        );
                    }
                }
        );

        dialog.show();
    }

    // =====================================================
    // إنشاء صف الطالب
    // =====================================================

    private void addStudentRow(
            LinearLayout parent,
            final int position
    ) {

        LinearLayout card =
                createCard();

        card.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        // الرقم

        TextView number =
                createText(
                        String.valueOf(
                                position + 1
                        ),
                        17,
                        BLUE,
                        true
                );

        number.setGravity(
                Gravity.CENTER
        );

        GradientDrawable numberBg =
                new GradientDrawable();

        numberBg.setColor(
                Color.rgb(
                        235,
                        243,
                        255
                )
        );

        numberBg.setCornerRadius(
                dp(14)
        );

        number.setBackground(
                numberBg
        );

        row.addView(
                number,
                new LinearLayout.LayoutParams(
                        dp(48),
                        dp(48)
                )
        );

        row.addView(
                horizontalSpace(10)
        );

        // معلومات الطالب

        LinearLayout info =
                new LinearLayout(this);

        info.setOrientation(
                LinearLayout.VERTICAL
        );

        info.setGravity(
                Gravity.RIGHT
        );

        TextView studentName =
                createText(
                        students.get(position),
                        15,
                        TEXT,
                        true
                );

        TextView studentStatus =
                createText(
                        "طالب مسجل",
                        11,
                        GRAY,
                        false
                );

        info.addView(
                studentName
        );

        info.addView(
                verticalSpace(3)
        );

        info.addView(
                studentStatus
        );

        row.addView(
                info,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        // زر الحذف

        TextView delete =
                createText(
                        "🗑",
                        19,
                        Color.rgb(
                                210,
                                70,
                                70
                        ),
                        false
                );

        delete.setGravity(
                Gravity.CENTER
        );

        delete.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
        );

        delete.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        confirmDelete(
                                position
                        );
                    }
                }
        );

        row.addView(
                delete,
                new LinearLayout.LayoutParams(
                        dp(50),
                        dp(50)
                )
        );

        card.addView(
                row
        );

        parent.addView(
                card
        );
    }

    // =====================================================
    // تأكيد حذف الطالب
    // =====================================================

    private void confirmDelete(
            final int position
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "حذف الطالب"
                )
                .setMessage(
                        "هل أنت متأكد من حذف:\n"
                        + students.get(position)
                )
                .setNegativeButton(
                        "إلغاء",
                        null
                )
                .setPositiveButton(
                        "حذف",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which
                            ) {

                                students.remove(
                                        position
                                );

                                createStudentsScreen();

                                showMessage(
                                        "تم حذف الطالب"
                                );
                            }
                        }
                )
                .show();
    }

    // =====================================================
    // بطاقة لوحة التحكم
    // =====================================================

    private TextView createDashboardCard(
            String icon,
            String title,
            String subtitle,
            int color
    ) {

        TextView view =
                new TextView(this);

        view.setText(
                icon
                + "\n\n"
                + title
                + "\n"
                + subtitle
        );

        view.setTextColor(
                TEXT
        );

        view.setTextSize(
                15
        );

        view.setGravity(
                Gravity.RIGHT |
                Gravity.CENTER_VERTICAL
        );

        view.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(15)
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                WHITE
        );

        bg.setCornerRadius(
                dp(20)
        );

        bg.setStroke(
                dp(1),
                Color.rgb(
                        225,
                        231,
                        240
                )
        );

        view.setBackground(
                bg
        );

        view.setElevation(
                dp(3)
        );

        return view;
    }

    // =====================================================
    // بطاقة عادية
    // =====================================================

    private LinearLayout createCard() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        card.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                WHITE
        );

        bg.setCornerRadius(
                dp(20)
        );

        bg.setStroke(
                dp(1),
                Color.rgb(
                        225,
                        231,
                        240
                )
        );

        card.setBackground(
                bg
        );

        card.setElevation(
                dp(2)
        );

        return card;
    }

    // =====================================================
    // بطاقة إحصائية
    // =====================================================

    private TextView createStat(
            String number,
            String label,
            int color
    ) {

        TextView view =
                createText(
                        number
                        + "\n"
                        + label,
                        14,
                        color,
                        true
                );

        view.setGravity(
                Gravity.CENTER
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                Color.rgb(
                        245,
                        248,
                        253
                )
        );

        bg.setCornerRadius(
                dp(15)
        );

        view.setBackground(
                bg
        );

        return view;
    }

    // =====================================================
    // إنشاء زر
    // =====================================================

    private TextView createButton(
            String text,
            int color
    ) {

        TextView button =
                createText(
                        text,
                        15,
                        WHITE,
                        true
                );

        button.setGravity(
                Gravity.CENTER
        );

        button.setPadding(
                dp(10),
                dp(14),
                dp(10),
                dp(14)
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                color
        );

        bg.setCornerRadius(
                dp(16)
        );

        button.setBackground(
                bg
        );

        button.setElevation(
                dp(3)
        );

        return button;
    }

    // =====================================================
    // آخر العمليات
    // =====================================================

    private void addActivity(
            LinearLayout parent,
            String icon,
            String title,
            String subtitle,
            String time
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        TextView iconView =
                createText(
                        icon,
                        20,
                        BLUE,
                        false
                );

        iconView.setGravity(
                Gravity.CENTER
        );

        GradientDrawable iconBg =
                new GradientDrawable();

        iconBg.setColor(
                Color.rgb(
                        235,
                        243,
                        255
                )
        );

        iconBg.setCornerRadius(
                dp(13)
        );

        iconView.setBackground(
                iconBg
        );

        row.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(45)
                )
        );

        LinearLayout details =
                new LinearLayout(this);

        details.setOrientation(
                LinearLayout.VERTICAL
        );

        details.setGravity(
                Gravity.RIGHT
        );

        TextView titleView =
                createText(
                        title,
                        14,
                        TEXT,
                        true
                );

        TextView subtitleView =
                createText(
                        subtitle,
                        11,
                        GRAY,
                        false
                );

        details.addView(
                titleView
        );

        details.addView(
                verticalSpace(3)
        );

        details.addView(
                subtitleView
        );

        LinearLayout.LayoutParams detailParams =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        detailParams.setMargins(
                dp(10),
                0,
                0,
                0
        );

        row.addView(
                details,
                detailParams
        );

        TextView timeView =
                createText(
                        time,
                        10,
                        GRAY,
                        false
                );

        row.addView(
                timeView
        );

        parent.addView(
                row
        );
    }

    // =====================================================
    // فاصل
    // =====================================================

    private void addDivider(
            LinearLayout parent
    ) {

        View divider =
                new View(this);

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(
                Color.rgb(
                        235,
                        238,
                        243
                )
        );

        divider.setBackground(
                bg
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(1)
                );

        params.setMargins(
                0,
                dp(12),
                0,
                dp(12)
        );

        parent.addView(
                divider,
                params
        );
    }

    // =====================================================
    // إنشاء TextView
    // =====================================================

    private TextView createText(
            String text,
            float size,
            int color,
            boolean bold
    ) {

        TextView view =
                new TextView(this);

        view.setText(
                text
        );

        view.setTextSize(
                size
        );

        view.setTextColor(
                color
        );

        view.setGravity(
                Gravity.RIGHT
        );

        view.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        if (bold) {

            view.setTypeface(
                    Typeface.create(
                            "sans-serif",
                            Typeface.BOLD
                    )
            );
        }

        return view;
    }

    // =====================================================
    // أبعاد البطاقات
    // =====================================================

    private LinearLayout.LayoutParams cardParams() {

        return new LinearLayout.LayoutParams(
                0,
                dp(150),
                1
        );
    }

    // =====================================================
    // مسافة عمودية
    // =====================================================

    private View verticalSpace(
            int height
    ) {

        View view =
                new View(this);

        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );

        return view;
    }

    // =====================================================
    // مسافة أفقية
    // =====================================================

    private View horizontalSpace(
            int width
    ) {

        View view =
                new View(this);

        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        dp(width),
                        1
                )
        );

        return view;
    }

    // =====================================================
    // تحويل dp
    // =====================================================

    private int dp(
            int value
    ) {

        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    // =====================================================
    // رسالة
    // =====================================================

    private void showMessage(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}

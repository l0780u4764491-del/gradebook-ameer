package com.ameer.gradebook;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // =========================================
    // ألوان التطبيق
    // =========================================

    private final int NAVY = Color.rgb(8, 29, 58);
    private final int BLUE = Color.rgb(25, 103, 210);
    private final int GREEN = Color.rgb(25, 166, 110);
    private final int GOLD = Color.rgb(218, 169, 55);
    private final int PURPLE = Color.rgb(128, 83, 190);

    private final int WHITE = Color.WHITE;
    private final int TEXT = Color.rgb(25, 38, 58);
    private final int GRAY = Color.rgb(105, 117, 135);
    private final int BACKGROUND = Color.rgb(246, 248, 252);

    // =========================================
    // بدء التطبيق
    // =========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);

        createHomeScreen();
    }

    // =========================================
    // إنشاء الشاشة الرئيسية
    // =========================================

    private void createHomeScreen() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BACKGROUND);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ScrollView scrollView = new ScrollView(this);

        scrollView.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);

        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(24)
        );

        content.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        // =====================================
        // رأس التطبيق
        // =====================================

        LinearLayout header = new LinearLayout(this);

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

        GradientDrawable headerBackground =
                new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{
                                NAVY,
                                Color.rgb(17, 55, 103)
                        }
                );

        headerBackground.setCornerRadius(
                dp(24)
        );

        header.setBackground(
                headerBackground
        );

        // الشعار

        TextView logo = createText(
                "🎓",
                32,
                WHITE,
                false
        );

        logo.setGravity(
                Gravity.CENTER
        );

        GradientDrawable logoBackground =
                new GradientDrawable();

        logoBackground.setColor(
                Color.rgb(35, 76, 125)
        );

        logoBackground.setCornerRadius(
                dp(17)
        );

        logo.setBackground(
                logoBackground
        );

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(
                        dp(58),
                        dp(58)
                );

        logoParams.setMargins(
                0,
                0,
                dp(12),
                0
        );

        header.addView(
                logo,
                logoParams
        );

        // عنوان التطبيق

        LinearLayout titleBox =
                new LinearLayout(this);

        titleBox.setOrientation(
                LinearLayout.VERTICAL
        );

        titleBox.setGravity(
                Gravity.RIGHT
        );

        TextView title = createText(
                "سجل الدرجات",
                24,
                WHITE,
                true
        );

        TextView year = createText(
                "العام الدراسي 2026 - 2027",
                13,
                Color.rgb(205, 220, 240),
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

        header.addView(
                titleBox,
                titleParams
        );

        content.addView(header);

        content.addView(
                verticalSpace(18)
        );

        // =====================================
        // بطاقة الترحيب
        // =====================================

        LinearLayout welcomeCard =
                createCard();

        TextView welcomeTitle = createText(
                "مرحبًا بك 👋",
                20,
                TEXT,
                true
        );

        TextView welcomeText = createText(
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

        // =====================================
        // عنوان الوصول السريع
        // =====================================

        TextView quickTitle = createText(
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

        // =====================================
        // بطاقات الطلاب والدرجات
        // =====================================

        LinearLayout row1 =
                new LinearLayout(this);

        row1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row1.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        TextView students =
                createDashboardCard(
                        "👨‍🎓",
                        "الطلاب",
                        "إدارة الطلاب والشعب",
                        BLUE
                );

        students.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage(
                                "قسم الطلاب سيتم تطويره لاحقًا"
                        );
                    }
                }
        );

        row1.addView(
                students,
                cardParams()
        );

        row1.addView(
                horizontalSpace(10)
        );

        TextView grades =
                createDashboardCard(
                        "📝",
                        "الدرجات",
                        "إدخال وتعديل الدرجات",
                        GREEN
                );

        grades.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage(
                                "قسم الدرجات سيتم تطويره لاحقًا"
                        );
                    }
                }
        );

        row1.addView(
                grades,
                cardParams()
        );

        content.addView(row1);

        content.addView(
                verticalSpace(10)
        );

        // =====================================
        // بطاقات التقارير والنتائج
        // =====================================

        LinearLayout row2 =
                new LinearLayout(this);

        row2.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row2.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        TextView reports =
                createDashboardCard(
                        "📊",
                        "التقارير",
                        "عرض وطباعة النتائج",
                        GOLD
                );

        reports.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage(
                                "قسم التقارير سيتم تطويره لاحقًا"
                        );
                    }
                }
        );

        row2.addView(
                reports,
                cardParams()
        );

        row2.addView(
                horizontalSpace(10)
        );

        TextView results =
                createDashboardCard(
                        "🏆",
                        "النتائج",
                        "المتفوقون والنتائج",
                        PURPLE
                );

        results.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage(
                                "قسم النتائج سيتم تطويره لاحقًا"
                        );
                    }
                }
        );

        row2.addView(
                results,
                cardParams()
        );

        content.addView(row2);

        content.addView(
                verticalSpace(18)
        );

        // =====================================
        // هنا ينتهي القسم الأول
        // =====================================
            // =====================================
        // آخر العمليات
        // =====================================

        TextView activityTitle = createText(
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

        content.addView(
                verticalSpace(18)
        );

        // =====================================
        // زر الإعدادات
        // =====================================

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
                                "الإعدادات سيتم تطويرها لاحقًا"
                        );
                    }
                }
        );

        content.addView(
                settingsButton
        );

        content.addView(
                verticalSpace(20)
        );

        // =====================================
        // شريط التنقل السفلي
        // =====================================

        LinearLayout navigation =
                new LinearLayout(this);

        navigation.setOrientation(
                LinearLayout.HORIZONTAL
        );

        navigation.setGravity(
                Gravity.CENTER
        );

        navigation.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(8)
        );

        navigation.setLayoutDirection(
                View.LAYOUT_DIRECTION_RTL
        );

        GradientDrawable navigationBackground =
                new GradientDrawable();

        navigationBackground.setColor(
                NAVY
        );

        navigationBackground.setCornerRadius(
                dp(22)
        );

        navigation.setBackground(
                navigationBackground
        );

        navigation.addView(
                createNavigationItem(
                        "⌂",
                        "الرئيسية",
                        true
                ),
                navigationParams()
        );

        navigation.addView(
                createNavigationItem(
                        "👥",
                        "الطلاب",
                        false
                ),
                navigationParams()
        );

        navigation.addView(
                createNavigationItem(
                        "📝",
                        "الدرجات",
                        false
                ),
                navigationParams()
        );

        navigation.addView(
                createNavigationItem(
                        "📊",
                        "التقارير",
                        false
                ),
                navigationParams()
        );

        navigation.addView(
                createNavigationItem(
                        "•••",
                        "المزيد",
                        false
                ),
                navigationParams()
        );

        content.addView(
                navigation
        );

        // =====================================
        // إضافة المحتوى
        // =====================================

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

        // =====================================
        // هنا ينتهي القسم الثاني
        // =====================================
            // =====================================
        // نهاية القسم الثالث
        // =====================================

    }

    // =====================================================
    // إنشاء بطاقة لوحة التحكم
    // =====================================================

    private TextView createDashboardCard(
            String icon,
            String title,
            String subtitle,
            int accentColor
    ) {

        TextView view = new TextView(this);

        String content =
                icon +
                "\n\n" +
                title +
                "\n" +
                subtitle;

        view.setText(content);

        view.setTextColor(TEXT);

        view.setTextSize(15);

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

        view.setTypeface(
                Typeface.create(
                        "sans-serif",
                        Typeface.NORMAL
                )
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                WHITE
        );

        background.setCornerRadius(
                dp(20)
        );

        background.setStroke(
                dp(1),
                Color.rgb(225, 231, 240)
        );

        view.setBackground(
                background
        );

        view.setElevation(
                dp(3)
        );

        return view;
    }

    // =====================================================
    // إنشاء بطاقة عادية
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

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                WHITE
        );

        background.setCornerRadius(
                dp(20)
        );

        background.setStroke(
                dp(1),
                Color.rgb(225, 231, 240)
        );

        card.setBackground(
                background
        );

        card.setElevation(
                dp(2)
        );

        return card;
    }

    // =====================================================
    // إضافة عملية إلى قائمة آخر العمليات
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

        // الأيقونة

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

        GradientDrawable iconBackground =
                new GradientDrawable();

        iconBackground.setColor(
                Color.rgb(235, 243, 255)
        );

        iconBackground.setCornerRadius(
                dp(13)
        );

        iconView.setBackground(
                iconBackground
        );

        row.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(45)
                )
        );

        // التفاصيل

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

        // الوقت

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
    // إنشاء فاصل
    // =====================================================

    private void addDivider(
            LinearLayout parent
    ) {

        View divider =
                new View(this);

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                Color.rgb(235, 238, 243)
        );

        divider.setBackground(
                background
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
    // إنشاء زر
    // =====================================================

    private TextView createButton(
            String title,
            int color
    ) {

        TextView button =
                createText(
                        title,
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

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                color
        );

        background.setCornerRadius(
                dp(16)
        );

        button.setBackground(
                background
        );

        button.setElevation(
                dp(3)
        );

        return button;
    }

    // =====================================================
    // عنصر شريط التنقل
    // =====================================================

    private LinearLayout createNavigationItem(
            String icon,
            String title,
            boolean selected
    ) {

        LinearLayout item =
                new LinearLayout(this);

        item.setOrientation(
                LinearLayout.VERTICAL
        );

        item.setGravity(
                Gravity.CENTER
        );

        TextView iconView =
                createText(
                        icon,
                        19,
                        selected
                                ? WHITE
                                : Color.rgb(
                                        165,
                                        185,
                                        210
                                ),
                        false
                );

        iconView.setGravity(
                Gravity.CENTER
        );

        TextView titleView =
                createText(
                        title,
                        10,
                        selected
                                ? WHITE
                                : Color.rgb(
                                        165,
                                        185,
                                        210
                                ),
                        selected
                );

        titleView.setGravity(
                Gravity.CENTER
        );

        item.addView(
                iconView
        );

        item.addView(
                verticalSpace(2)
        );

        item.addView(
                titleView
        );

        return item;
    }

    // =====================================================
    // إنشاء TextView
    // =====================================================

    private TextView createText(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView view =
                new TextView(this);

        view.setText(
                value
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
    // حجم بطاقات لوحة التحكم
    // =====================================================

    private LinearLayout.LayoutParams cardParams() {

        return new LinearLayout.LayoutParams(
                0,
                dp(150),
                1
        );
    }

    // =====================================================
    // حجم عناصر التنقل
    // =====================================================

    private LinearLayout.LayoutParams navigationParams() {

        return new LinearLayout.LayoutParams(
                0,
                dp(58),
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
    // تحويل dp إلى pixels
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
    // رسالة مؤقتة
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

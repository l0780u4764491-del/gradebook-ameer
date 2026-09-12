package com.ameer.gradebook;

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

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // ألوان التطبيق
    private final int NAVY = Color.rgb(8, 29, 58);
    private final int BLUE = Color.rgb(25, 103, 210);
    private final int LIGHT_BLUE = Color.rgb(235, 243, 255);
    private final int WHITE = Color.WHITE;
    private final int TEXT = Color.rgb(25, 38, 58);
    private final int GRAY = Color.rgb(105, 117, 135);
    private final int GREEN = Color.rgb(25, 166, 110);
    private final int GOLD = Color.rgb(218, 169, 55);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(NAVY);
        getWindow().setNavigationBarColor(NAVY);

        buildHomeScreen();
    }

    private void buildHomeScreen() {

        // الصفحة الرئيسية
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(246, 248, 252));

        // اتجاه عربي RTL
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Scroll
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(18), dp(18), dp(24));
        content.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // =========================
        // رأس التطبيق
        // =========================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(18), dp(18), dp(18), dp(18));

        GradientDrawable headerBg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{NAVY, Color.rgb(17, 55, 103)}
        );
        headerBg.setCornerRadius(dp(24));
        header.setBackground(headerBg);

        LinearLayout titleBox = new LinearLayout(this);
        titleBox.setOrientation(LinearLayout.VERTICAL);
        titleBox.setGravity(Gravity.RIGHT);

        TextView appTitle = text(
                "سجل الدرجات",
                24,
                WHITE,
                true
        );

        TextView schoolYear = text(
                "العام الدراسي 2026 - 2027",
                13,
                Color.rgb(205, 220, 240),
                false
        );

        titleBox.addView(appTitle);
        titleBox.addView(space(4));
        titleBox.addView(schoolYear);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(0, -2, 1);

        header.addView(titleBox, titleParams);

        TextView logo = text("🎓", 38, WHITE, false);
        logo.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(dp(58), dp(58));
        logoParams.setMargins(dp(10), 0, 0, 0);

        header.addView(logo, logoParams);

        content.addView(header);

        content.addView(space(18));

        // =========================
        // بطاقة الترحيب
        // =========================

        LinearLayout welcome = card();

        TextView welcomeTitle = text(
                "مرحبًا بك 👋",
                20,
                TEXT,
                true
        );

        TextView welcomeText = text(
                "إدارة درجات الطلاب بطريقة سهلة ومنظمة",
                14,
                GRAY,
                false
        );

        welcome.addView(welcomeTitle);
        welcome.addView(space(5));
        welcome.addView(welcomeText);

        content.addView(welcome);

        content.addView(space(16));

        // =========================
        // عنوان الاختصارات
        // =========================

        TextView quickTitle = text(
                "الوصول السريع",
                18,
                TEXT,
                true
        );

        content.addView(quickTitle);
        content.addView(space(10));

        // =========================
        // الصف الأول
        // =========================

        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        row1.addView(
                dashboardCard(
                        "👨‍🎓",
                        "الطلاب",
                        "إدارة الطلاب والشعب",
                        BLUE,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "قسم الطلاب - سيتم تطويره في القسم القادم",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                ),
                weightParams()
        );

        row1.addView(spaceHorizontal(10));

        row1.addView(
                dashboardCard(
                        "📝",
                        "الدرجات",
                        "إدخال وتعديل الدرجات",
                        GREEN,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "قسم الدرجات - سيتم تطويره لاحقًا",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                ),
                weightParams()
        );

        content.addView(row1);

        content.addView(space(10));

        // =========================
        // الصف الثاني
        // =========================

        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        row2.addView(
                dashboardCard(
                        "📊",
                        "التقارير",
                        "عرض وطباعة النتائج",
                        GOLD,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "قسم التقارير - سيتم تطويره لاحقًا",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                ),
                weightParams()
        );

        row2.addView(spaceHorizontal(10));

        row2.addView(
                dashboardCard(
                        "🏆",
                        "النتائج",
                        "المتفوقون والنتائج",
                        Color.rgb(128, 83, 190),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "قسم النتائج - سيتم تطويره لاحقًا",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                ),
                weightParams()
        );

        content.addView(row2);

        content.addView(space(16));

        // =========================
        // آخر العمليات
        // =========================

        TextView activityTitle = text(
                "آخر العمليات",
                18,
                TEXT,
                true
        );

        content.addView(activityTitle);
        content.addView(space(10));

        LinearLayout activityCard = card();

        addActivity(
                activityCard,
                "➕",
                "إضافة طالب جديد",
                "جاهز لإضافة الطلاب",
                "منذ لحظات"
        );

        addDivider(activityCard);

        addActivity(
                activityCard,
                "📝",
                "إدخال درجات الفصل الأول",
                "سجل الدرجات",
                "لم يبدأ بعد"
        );

        addDivider(activityCard);

        addActivity(
                activityCard,
                "📊",
                "إعداد التقارير",
                "تقارير الطلاب",
                "متاح لاحقًا"
        );

        content.addView(activityCard);

        content.addView(space(16));

        // =========================
        // الإعدادات
        // =========================

        TextView settings = button(
                "⚙️   الإعدادات والنسخ الاحتياطي",
                NAVY
        );

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        MainActivity.this,
                        "الإعدادات سيتم تطويرها لاحقًا",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        content.addView(settings);

        content.addView(space(20));

        // =========================
        // شريط التنقل السفلي
        // =========================

        LinearLayout navigation = new LinearLayout(this);
        navigation.setOrientation(LinearLayout.HORIZONTAL);
        navigation.setGravity(Gravity.CENTER);
        navigation.setPadding(dp(8), dp(8), dp(8), dp(8));

        GradientDrawable navBg = new GradientDrawable();
        navBg.setColor(NAVY);
        navBg.setCornerRadius(dp(22));
        navigation.setBackground(navBg);

        navigation.addView(
                navItem("⌂", "الرئيسية", true),
                weightParams()
        );

        navigation.addView(
                navItem("👥", "الطلاب", false),
                weightParams()
        );

        navigation.addView(
                navItem("📝", "الدرجات", false),
                weightParams()
        );

        navigation.addView(
                navItem("📊", "التقارير", false),
                weightParams()
        );

        navigation.addView(
                navItem("•••", "المزيد", false),
                weightParams()
        );

        content.addView(navigation);

        scrollView.addView(content);

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(-1, 0, 1)
        );

        setContentView(root);
    }

    // ==========================================
    // إنشاء بطاقة لوحة التحكم
    // ==========================================

    private LinearLayout dashboardCard(
            String icon,
            String title,
            String subtitle,
            int accent,
            View.OnClickListener listener
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.RIGHT);
        card.setPadding(dp(15), dp(15), dp(15), dp(15));
        card.setClickable(true);
        card.setFocusable(true);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(WHITE);
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(1), Color.rgb(225, 231, 240));

        card.setBackground(bg);
        card.setElevation(dp(3));

        TextView iconView = text(
                icon,
                30,
                accent,
                false
        );

        iconView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(dp(48), dp(48));

        iconParams.gravity = Gravity.RIGHT;

        GradientDrawable iconBg = new GradientDrawable();
        iconBg.setColor(lighten(accent));
        iconBg.setCornerRadius(dp(15));

        iconView.setBackground(iconBg);

        card.addView(iconView, iconParams);

        card.addView(space(10));

        TextView titleView = text(
                title,
                17,
                TEXT,
                true
        );

        card.addView(titleView);

        card.addView(space(3));

        TextView subtitleView = text(
                subtitle,
                11,
                GRAY,
                false
        );

        card.addView(subtitleView);

        card.setOnClickListener(listener);

        return card;
    }

    // ==========================================
    // بطاقة عامة
    // ==========================================

    private LinearLayout card() {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(WHITE);
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(1), Color.rgb(225, 231, 240));

        card.setBackground(bg);
        card.setElevation(dp(2));

        return card;
    }

    // ==========================================
    // العمليات الأخيرة
    // ==========================================

    private void addActivity(
            LinearLayout parent,
            String icon,
            String title,
            String subtitle,
            String time
    ) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView iconView = text(
                icon,
                22,
                BLUE,
                false
        );

        iconView.setGravity(Gravity.CENTER);

        GradientDrawable iconBg = new GradientDrawable();
        iconBg.setColor(LIGHT_BLUE);
        iconBg.setCornerRadius(dp(13));

        iconView.setBackground(iconBg);

        row.addView(
                iconView,
                new LinearLayout.LayoutParams(dp(45), dp(45))
        );

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setGravity(Gravity.RIGHT);

        TextView titleView = text(
                title,
                14,
                TEXT,
                true
        );

        TextView subtitleView = text(
                subtitle,
                11,
                GRAY,
                false
        );

        details.addView(titleView);
        details.addView(space(3));
        details.addView(subtitleView);

        LinearLayout.LayoutParams detailParams =
                new LinearLayout.LayoutParams(0, -2, 1);

        detailParams.setMargins(dp(10), 0, 0, 0);

        row.addView(details, detailParams);

        TextView timeView = text(
                time,
                10,
                GRAY,
                false
        );

        row.addView(timeView);

        parent.addView(row);
    }

    // ==========================================
    // فاصل
    // ==========================================

    private void addDivider(LinearLayout parent) {

        View divider = new View(this);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(235, 238, 243));

        divider.setBackground(bg);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(1)
                );

        params.setMargins(0, dp(12), 0, dp(12));

        parent.addView(divider, params);
    }

    // ==========================================
    // زر
    // ==========================================

    private TextView button(String title, int color) {

        TextView view = text(
                title,
                15,
                WHITE,
                true
        );

        view.setGravity(Gravity.CENTER);
        view.setPadding(dp(10), dp(14), dp(10), dp(14));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(16));

        view.setBackground(bg);
        view.setElevation(dp(3));

        return view;
    }

    // ==========================================
    // شريط التنقل
    // ==========================================

    private LinearLayout navItem(
            String icon,
            String title,
            boolean selected
    ) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView iconView = text(
                icon,
                19,
                selected ? Color.WHITE : Color.rgb(165, 185, 210),
                false
        );

        iconView.setGravity(Gravity.CENTER);

        TextView titleView = text(
                title,
                10,
                selected ? Color.WHITE : Color.rgb(165, 185, 210),
                selected
        );

        titleView.setGravity(Gravity.CENTER);

        item.addView(iconView);
        item.addView(space(2));
        item.addView(titleView);

        return item;
    }

    // ==========================================
    // أدوات
    // ==========================================

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {

        TextView view = new TextView(this);

        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.RIGHT);
        view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

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

    private LinearLayout.LayoutParams weightParams() {

        return new LinearLayout.LayoutParams(
                0,
                -2,
                1
        );
    }

    private View space(int height) {

        View view = new View(this);

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

    private int dp(int value) {

        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    private int lighten(int color) {

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        r = (r + 255) / 2;
        g = (g + 255) / 2;
        b = (b + 255) / 2;

        return Color.rgb(r, g, b);
    }
}

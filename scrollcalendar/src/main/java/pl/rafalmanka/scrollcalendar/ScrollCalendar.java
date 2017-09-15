package pl.rafalmanka.scrollcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import pl.rafalmanka.scrollcalendar.adapter.LegendItem;
import pl.rafalmanka.scrollcalendar.adapter.ResProvider;
import pl.rafalmanka.scrollcalendar.adapter.ScrollCalendarAdapter;
import pl.rafalmanka.scrollcalendar.contract.ScrollCalendarCallback;

/**
 * Created by rafal.manka on 10/09/2017
 */
public class ScrollCalendar extends LinearLayoutCompat implements ResProvider {

    @ColorRes
    private int fontColor;
    @ColorRes
    private int backgroundColor;
    @ColorRes
    private int disabledTextColor;
    @ColorRes
    private int unavailableTextColor;
    @ColorRes
    private int selectedTextColor;
    @ColorRes
    private int todayTextColor;

    @DrawableRes
    private int unavailableBackground;
    @DrawableRes
    private int selectedBackground;
    @DrawableRes
    private int currentDayBackground;

    @ColorRes
    private int disabledBackgroundColor;

    @Nullable
    private String customFont;

    private final LegendItem[] legend = new LegendItem[7];

    @Nullable
    private ScrollCalendarAdapter adapter;

    public ScrollCalendar(Context context) {
        super(context);
        init(context);
    }

    public ScrollCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyle(context, attrs);
        init(context);
    }

    public ScrollCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs);
        init(context);
    }

    private void init(@NonNull Context context) {
        setOrientation(VERTICAL);
        inflate(context, R.layout.scrollcalendar_calendar, this);
        for (int i = 0; i < legend.length; i++) {
            legend[i] = new LegendItem(i + 1);
        }
    }

    private void initStyle(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.ScrollCalendar);
        currentDayBackground = typedArray.getResourceId(Keys.CURRENT_DAY_BG, Defaults.CURRENT_DAY_BG);
        fontColor = typedArray.getResourceId(Keys.FONT_COLOR, Defaults.FONT_COLOR);
        backgroundColor = typedArray.getResourceId(Keys.BACKGROUND_COLOR, Defaults.BACKGROUND_COLOR);
        disabledTextColor = typedArray.getResourceId(Keys.DISABLED_TEXT_COLOR, Defaults.DISABLED_TEXT_COLOR);
        disabledBackgroundColor = typedArray.getResourceId(Keys.DISABLED_BACKGROUND_COLOR, Defaults.DISABLED_BACKGROUND_COLOR);
        unavailableTextColor = typedArray.getResourceId(Keys.UNAVAILABLE_TEXT_COLOR, Defaults.UNAVAILABLE_TEXT_COLOR);
        selectedTextColor = typedArray.getResourceId(Keys.SELECTED_TEXT_COLOR, Defaults.SELECTED_TEXT_COLOR);
        unavailableBackground = typedArray.getResourceId(Keys.UNAVAILABLE_BACKGROUND, Defaults.UNAVAILABLE_BACKGROUND);
        selectedBackground = typedArray.getResourceId(Keys.SELECTED_BACKGROUND, Defaults.SELECTED_BACKGROUND);
        todayTextColor = typedArray.getResourceId(Keys.TODAY_TEXT_COLOR, Defaults.TODAY_TEXT_COLOR);
        customFont = typedArray.getString(Keys.CUSTOM_FONT);
        typedArray.recycle();
    }

    public void setCallback(@Nullable final ScrollCalendarCallback calendarCallback) {
        getAdapter().setCallback(calendarCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // Legend
        LinearLayout legendHolder = findViewById(R.id.legend);
        for (LegendItem legendItem : legend) {
            legendHolder.addView(legendItem.layout(legendHolder, this));
            legendItem.display();
        }
        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
        recyclerView.setAdapter(getAdapter());
    }

    @NonNull
    private ScrollCalendarAdapter getAdapter() {
        if (adapter == null) {
            adapter = new ScrollCalendarAdapter(this);
        }
        return adapter;
    }

    // Colors

    @ColorInt
    @Override
    public int defaultFontColor() {
        return ContextCompat.getColor(getContext(), fontColor);
    }

    @ColorInt
    @Override
    public int defaultBackgroundColor() {
        return ContextCompat.getColor(getContext(), backgroundColor);
    }

    @ColorInt
    @Override
    public int disabledTextColor() {
        return ContextCompat.getColor(getContext(), disabledTextColor);
    }

    @ColorInt
    @Override
    public int disabledBackgroundColor() {
        return ContextCompat.getColor(getContext(), disabledBackgroundColor);
    }

    @ColorInt
    @Override
    public int todayTextColor() {
        return ContextCompat.getColor(getContext(), todayTextColor);
    }

    @ColorInt
    @Override
    public int unavailableTextColor() {
        return ContextCompat.getColor(getContext(), unavailableTextColor);
    }


    @Override
    public int selectedTextColor() {
        return ContextCompat.getColor(getContext(), selectedTextColor);
    }

    // Drawables

    @Override
    @DrawableRes
    public int todayBackground() {
        return currentDayBackground;
    }

    @Override
    @DrawableRes
    public int unavailableBackgroundColor() {
        return unavailableBackground;
    }

    @Override
    public int selectedBackgroundColor() {
        return selectedBackground;
    }

    // Other

    @Override
    @Nullable
    public Typeface getCustomFont() {
        if (customFont == null) {
            return null;
        }
        try {
            return Typeface.createFromAsset(getContext().getAssets(), customFont);
        } catch (Exception e) {
            return null;
        }
    }
}
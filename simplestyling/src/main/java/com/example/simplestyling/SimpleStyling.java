package com.example.simplestyling;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.EditText;

public class SimpleStyling {

    /**
     * Applies formatting to the selected text in an EditText.
     *
     * @param TypeOrSize        -1: Bold, -2: Highlight, -3: Underline, -4: Italic, 0: Step Size, >0: Fixed Size
     * @param SelectionStart    Start of the selection range
     * @param SelectionEnd      End of the selection range
     * @param EditTextName      The target EditText
     * @param TextSizeIncrement Step value for size change
     * @param HighLightColor    Color integer for highlighting
     */
    public static void Format(int TypeOrSize, int SelectionStart, int SelectionEnd, final EditText EditTextName, int TextSizeIncrement, int HighLightColor) {
        if (SelectionStart < 0 || SelectionEnd > EditTextName.length() || SelectionStart >= SelectionEnd) return;

        int cursorStart = EditTextName.getSelectionStart();
        int cursorEnd = EditTextName.getSelectionEnd();

        // Use a builder for efficient, single-pass editing
        SpannableStringBuilder builder = new SpannableStringBuilder(EditTextName.getText());

        switch (TypeOrSize) {
            case -1: toggleStyle(builder, SelectionStart, SelectionEnd, Typeface.BOLD); break;
            case -4: toggleStyle(builder, SelectionStart, SelectionEnd, Typeface.ITALIC); break;
            case -3: toggleSimpleSpan(builder, SelectionStart, SelectionEnd, UnderlineSpan.class); break;
            case -2: toggleColorSpan(builder, SelectionStart, SelectionEnd, HighLightColor); break;
            case 0:
                int currentSize = getExistingSize(builder, SelectionStart, SelectionEnd, (int) EditTextName.getTextSize());
                applySizeSpan(builder, SelectionStart, SelectionEnd, Math.max(1, currentSize + TextSizeIncrement));
                break;
            default:
                if (TypeOrSize > 0) applySizeSpan(builder, SelectionStart, SelectionEnd, TypeOrSize);
                break;
        }

        EditTextName.setText(builder);
        EditTextName.setSelection(cursorStart, cursorEnd);
    }

    private static void toggleStyle(SpannableStringBuilder builder, int start, int end, int styleBit) {
        boolean isAppliedEverywhere = true;
        for (int i = start; i < end; i++) {
            StyleSpan[] spans = builder.getSpans(i, i + 1, StyleSpan.class);
            boolean found = false;
            for (StyleSpan s : spans) if ((s.getStyle() & styleBit) != 0) found = true;
            if (!found) { isAppliedEverywhere = false; break; }
        }

        StyleSpan[] spans = builder.getSpans(start, end, StyleSpan.class);
        for (StyleSpan span : spans) {
            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            int currentStyle = span.getStyle();
            builder.removeSpan(span);
            if (s < start) builder.setSpan(new StyleSpan(currentStyle), s, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (e > end) builder.setSpan(new StyleSpan(currentStyle), end, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            int newInnerStart = Math.max(s, start);
            int newInnerEnd = Math.min(e, end);
            int newStyle = isAppliedEverywhere ? (currentStyle & ~styleBit) : (currentStyle | styleBit);
            if (newStyle != 0) builder.setSpan(new StyleSpan(newStyle), newInnerStart, newInnerEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (!isAppliedEverywhere) { // Fill gaps
            for (int i = start; i < end; ) {
                StyleSpan[] current = builder.getSpans(i, i + 1, StyleSpan.class);
                int next = end;
                if (current.length == 0) {
                    StyleSpan[] nextSpans = builder.getSpans(i, end, StyleSpan.class);
                    for (StyleSpan ns : nextSpans) next = Math.min(next, builder.getSpanStart(ns));
                    builder.setSpan(new StyleSpan(styleBit), i, next, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    for (StyleSpan s : current) next = Math.min(next, builder.getSpanEnd(s));
                }
                i = next;
            }
        }
    }

    private static void toggleSimpleSpan(SpannableStringBuilder builder, int start, int end, Class<? extends CharacterStyle> spanClass) {
        boolean isAppliedEverywhere = true;
        for (int i = start; i < end; i++) {
            if (builder.getSpans(i, i + 1, spanClass).length == 0) { isAppliedEverywhere = false; break; }
        }

        Object[] spans = builder.getSpans(start, end, spanClass);
        for (Object span : spans) {
            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            builder.removeSpan(span);
            if (s < start) trySetSpan(builder, spanClass, s, start);
            if (e > end) trySetSpan(builder, spanClass, end, e);
        }
        if (!isAppliedEverywhere) trySetSpan(builder, spanClass, start, end);
    }

    private static void toggleColorSpan(SpannableStringBuilder builder, int start, int end, int color) {
        boolean isAppliedEverywhere = true;
        for (int i = start; i < end; i++) {
            BackgroundColorSpan[] spans = builder.getSpans(i, i + 1, BackgroundColorSpan.class);
            boolean found = false;
            for (BackgroundColorSpan s : spans) if (s.getBackgroundColor() == color) found = true;
            if (!found) { isAppliedEverywhere = false; break; }
        }

        BackgroundColorSpan[] spans = builder.getSpans(start, end, BackgroundColorSpan.class);
        for (BackgroundColorSpan span : spans) {
            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            int c = span.getBackgroundColor();
            builder.removeSpan(span);
            if (s < start) builder.setSpan(new BackgroundColorSpan(c), s, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (e > end) builder.setSpan(new BackgroundColorSpan(c), end, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!isAppliedEverywhere) builder.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void applySizeSpan(SpannableStringBuilder builder, int start, int end, int size) {
        AbsoluteSizeSpan[] spans = builder.getSpans(start, end, AbsoluteSizeSpan.class);
        for (AbsoluteSizeSpan span : spans) {
            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            int oldSize = span.getSize();
            builder.removeSpan(span);
            if (s < start) builder.setSpan(new AbsoluteSizeSpan(oldSize), s, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (e > end) builder.setSpan(new AbsoluteSizeSpan(oldSize), end, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        builder.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static int getExistingSize(SpannableStringBuilder builder, int start, int end, int fallback) {
        AbsoluteSizeSpan[] spans = builder.getSpans(start, end, AbsoluteSizeSpan.class);
        return spans.length > 0 ? spans[0].getSize() : fallback;
    }

    private static void trySetSpan(SpannableStringBuilder b, Class<?> clazz, int s, int e) {
        try { b.setSpan(clazz.newInstance(), s, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); } catch (Exception ignored) {}
    }

    public static int[] autoselection(EditText editText) {
        int s = editText.getSelectionStart(), e = editText.getSelectionEnd();
        if (s < e) return new int[]{s, e};
        CharSequence t = editText.getText();
        if (t == null || t.length() == 0) return new int[]{0, 0};
        int start = s, end = s, len = t.length();
        while (start > 0 && !Character.isWhitespace(t.charAt(start - 1))) start--;
        while (end < len && !Character.isWhitespace(t.charAt(end))) end++;
        return new int[]{start, end};
    }
}

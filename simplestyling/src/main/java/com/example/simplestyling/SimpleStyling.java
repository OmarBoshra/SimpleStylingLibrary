package com.example.simplestyling;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.EditText;
import android.widget.TextView;

public class SimpleStyling {

    public enum FormattingType {
        BOLD, ITALIC, UNDERLINE, HIGHLIGHT, INCREMENTAL_SIZE, FIXED_SIZE
    }

    /**
     * Applies formatting to the selected text in an EditText.
     *
     * @param type              The type of formatting to apply
     * @param SelectionStart    Start of the selection range
     * @param SelectionEnd      End of the selection range
     * @param EditTextName      The target EditText
     * @param sizeValue         Value for size change (increment for INCREMENTAL_SIZE, absolute for FIXED_SIZE)
     * @param HighLightColor    Color integer for highlighting
     */
    public static void format(FormattingType type, int SelectionStart, int SelectionEnd, final EditText EditTextName, int sizeValue, int HighLightColor) {
        if (SelectionStart < 0 || SelectionEnd > EditTextName.length() || SelectionStart >= SelectionEnd) return;

        Editable editable = EditTextName.getText();
        if (editable == null) return;

        int selectionStart = EditTextName.getSelectionStart();
        int selectionEnd = EditTextName.getSelectionEnd();

        switch (type) {
            case BOLD:
                toggleStyle(editable, SelectionStart, SelectionEnd, Typeface.BOLD);
                break;
            case ITALIC:
                toggleStyle(editable, SelectionStart, SelectionEnd, Typeface.ITALIC);
                break;
            case UNDERLINE:
                toggleSimpleSpan(editable, SelectionStart, SelectionEnd, UnderlineSpan.class);
                break;
            case HIGHLIGHT:
                toggleColorSpan(editable, SelectionStart, SelectionEnd, HighLightColor);
                break;
            case INCREMENTAL_SIZE:
                int currentSize = getExistingSize(editable, SelectionStart, SelectionEnd, (int) EditTextName.getTextSize());
                applySizeSpan(editable, SelectionStart, SelectionEnd, Math.max(1, currentSize + sizeValue));
                break;
            case FIXED_SIZE:
                if (sizeValue > 0) {
                    applySizeSpan(editable, SelectionStart, SelectionEnd, sizeValue);
                }
                break;
        }

        // Force a full layout rebuild by re-setting the text as a SPANNABLE buffer.
        // This fixes the issue where lines overlap because DynamicLayout caches height incorrectly.
        EditTextName.setText(editable, TextView.BufferType.SPANNABLE);
        
        // Restore selection to prevent the cursor from jumping to the start
        EditTextName.setSelection(selectionStart, selectionEnd);
    }

    private static void toggleStyle(Spannable builder, int start, int end, int styleBit) {
        boolean isAppliedEverywhere = true;
        for (int i = start; i < end; i++) {
            StyleSpan[] spans = builder.getSpans(i, i + 1, StyleSpan.class);
            boolean found = false;
            for (StyleSpan s : spans) {
                if ((s.getStyle() & styleBit) != 0 && (builder.getSpanFlags(s) & Spanned.SPAN_COMPOSING) == 0) {
                    found = true;
                    break;
                }
            }
            if (!found) { isAppliedEverywhere = false; break; }
        }

        StyleSpan[] spans = builder.getSpans(start, end, StyleSpan.class);
        for (StyleSpan span : spans) {
            if ((builder.getSpanFlags(span) & Spanned.SPAN_COMPOSING) != 0) continue;

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

        if (!isAppliedEverywhere) {
            for (int i = start; i < end; ) {
                StyleSpan[] current = builder.getSpans(i, i + 1, StyleSpan.class);
                int next = end;
                boolean alreadyStyled = false;
                for (StyleSpan s : current) {
                    if ((s.getStyle() & styleBit) != 0 && (builder.getSpanFlags(s) & Spanned.SPAN_COMPOSING) == 0) {
                        alreadyStyled = true;
                        next = builder.getSpanEnd(s);
                        break;
                    }
                }

                if (!alreadyStyled) {
                    StyleSpan[] nextSpans = builder.getSpans(i, end, StyleSpan.class);
                    for (StyleSpan ns : nextSpans) {
                        if ((ns.getStyle() & styleBit) != 0 && (builder.getSpanFlags(ns) & Spanned.SPAN_COMPOSING) == 0) {
                            next = Math.min(next, builder.getSpanStart(ns));
                        }
                    }
                    builder.setSpan(new StyleSpan(styleBit), i, Math.min(next, end), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    i = next;
                } else {
                    i = next;
                }
            }
        }
    }

    private static void toggleSimpleSpan(Spannable builder, int start, int end, Class<? extends CharacterStyle> spanClass) {
        boolean isAppliedEverywhere = true;
        for (int i = start; i < end; i++) {
            boolean found = false;
            for (Object s : builder.getSpans(i, i + 1, spanClass)) {
                if ((builder.getSpanFlags(s) & Spanned.SPAN_COMPOSING) == 0) {
                    found = true;
                    break;
                }
            }
            if (!found) { isAppliedEverywhere = false; break; }
        }

        Object[] spans = builder.getSpans(start, end, spanClass);
        for (Object span : spans) {
            if ((builder.getSpanFlags(span) & Spanned.SPAN_COMPOSING) != 0) continue;

            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            builder.removeSpan(span);
            if (s < start) applyNewSpan(builder, spanClass, s, start);
            if (e > end) applyNewSpan(builder, spanClass, end, e);
        }
        if (!isAppliedEverywhere) applyNewSpan(builder, spanClass, start, end);
    }

    private static void applyNewSpan(Spannable builder, Class<?> spanClass, int start, int end) {
        if (spanClass == UnderlineSpan.class) {
            builder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static void toggleColorSpan(Spannable builder, int start, int end, int color) {
        boolean isAppliedEverywhere = true;
        for (int i = start; i < end; i++) {
            BackgroundColorSpan[] spans = builder.getSpans(i, i + 1, BackgroundColorSpan.class);
            boolean found = false;
            for (BackgroundColorSpan s : spans) {
                if (s.getBackgroundColor() == color && (builder.getSpanFlags(s) & Spanned.SPAN_COMPOSING) == 0) {
                    found = true;
                    break;
                }
            }
            if (!found) { isAppliedEverywhere = false; break; }
        }

        BackgroundColorSpan[] spans = builder.getSpans(start, end, BackgroundColorSpan.class);
        for (BackgroundColorSpan span : spans) {
            if ((builder.getSpanFlags(span) & Spanned.SPAN_COMPOSING) != 0) continue;

            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            int c = span.getBackgroundColor();
            builder.removeSpan(span);
            if (s < start) builder.setSpan(new BackgroundColorSpan(c), s, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (e > end) builder.setSpan(new BackgroundColorSpan(c), end, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!isAppliedEverywhere) builder.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void applySizeSpan(Spannable builder, int start, int end, int size) {
        AbsoluteSizeSpan[] spans = builder.getSpans(start, end, AbsoluteSizeSpan.class);
        for (AbsoluteSizeSpan span : spans) {
            if ((builder.getSpanFlags(span) & Spanned.SPAN_COMPOSING) != 0) continue;

            int s = builder.getSpanStart(span);
            int e = builder.getSpanEnd(span);
            int oldSize = span.getSize();
            builder.removeSpan(span);
            if (s < start) builder.setSpan(new AbsoluteSizeSpan(oldSize), s, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (e > end) builder.setSpan(new AbsoluteSizeSpan(oldSize), end, e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        builder.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static int getExistingSize(Spannable builder, int start, int end, int fallback) {
        AbsoluteSizeSpan[] spans = builder.getSpans(start, end, AbsoluteSizeSpan.class);
        for (AbsoluteSizeSpan s : spans) {
            if ((builder.getSpanFlags(s) & Spanned.SPAN_COMPOSING) == 0) return s.getSize();
        }
        return fallback;
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

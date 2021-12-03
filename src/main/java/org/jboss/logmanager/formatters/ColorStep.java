package org.jboss.logmanager.formatters;

import org.jboss.logmanager.ExtLogRecord;

import java.util.logging.Formatter;

final class ColorStep implements FormatStep {
    private final int r, g, b;
    private final FormatStep delegate;

    ColorStep(final FormatStep delegate, final int r, final int g, final int b, final int darken) {
        this.r = r >>> darken;
        this.g = g >>> darken;
        this.b = b >>> darken;
        this.delegate = delegate;
    }

    public void render(final Formatter formatter, final StringBuilder builder, final ExtLogRecord record) {
        ColorUtil.startFgColor(builder, ColorPatternFormatter.isTrueColor(), r, g, b);
        delegate.render(formatter, builder, record);
        ColorUtil.endFgColor(builder);
    }

    public void render(final StringBuilder builder, final ExtLogRecord record) {
        render(null, builder, record);
    }

    public int estimateLength() {
        return delegate.estimateLength() + 30;
    }

    public boolean isCallerInformationRequired() {
        return delegate.isCallerInformationRequired();
    }

    public FormatStep[] childSteps() {
        return delegate.childSteps();
    }

    public int childStepCount() {
        return delegate.childStepCount();
    }

    public FormatStep getChildStep(final int idx) {
        return delegate.getChildStep(idx);
    }

    public ItemType getItemType() {
        return delegate.getItemType();
    }
}

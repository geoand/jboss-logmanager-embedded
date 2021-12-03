package org.jboss.logmanager.formatters;

import org.jboss.logmanager.ExtFormatter;
import org.jboss.logmanager.ExtLogRecord;

import static java.lang.Math.max;

public class ImmutableMultistepFormatter extends ExtFormatter {

    private final FormatStep[] steps;
    private final int builderLength;
    private final boolean callerCalculationRequired;

    public ImmutableMultistepFormatter(final FormatStep[] steps) {
        this.steps = steps.clone();

        boolean callerCalculatedRequired = false;
        int builderLength = 0;
        for (FormatStep step : steps) {
            builderLength += step.estimateLength();
            if (step.isCallerInformationRequired()) {
                callerCalculatedRequired = true;
            }
        }
        this.builderLength = max(32, builderLength);
        this.callerCalculationRequired = callerCalculatedRequired;
    }

    public String format(final ExtLogRecord record) {
        final StringBuilder builder = new StringBuilder(builderLength);
        for (FormatStep step : steps) {
            step.render(this, builder, record);
        }
        return builder.toString();
    }

    @Override
    public boolean isCallerCalculationRequired() {
        return callerCalculationRequired;
    }
}

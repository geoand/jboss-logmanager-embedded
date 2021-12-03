/*
 * Copyright 2018 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.logmanager.formatters;

import static java.lang.Math.abs;
import static org.jboss.logmanager.formatters.ColorPatternUtil.*;

import java.util.logging.LogRecord;

import com.oracle.svm.core.annotate.Delete;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import org.jboss.logmanager.ExtLogRecord;
import org.wildfly.common.format.Printf;

/**
 * A pattern formatter that colorizes the pattern in a fixed manner.
 */
public class ColorPatternFormatter extends PatternFormatter {

    private final Printf printf;
    private final int darken;

    public ColorPatternFormatter() {
        this(0);
    }

    public ColorPatternFormatter(final String pattern) {
        this(0, pattern);
    }

    public ColorPatternFormatter(int darken) {
        this.darken = darken;
        printf = new ColorPrintf(darken);
    }

    public ColorPatternFormatter(int darken, final String pattern) {
        this(darken);
        setPattern(pattern);
    }

    static final boolean trueColor = determineTrueColor();

    static boolean isTrueColor() {
        return trueColor;
    }

    public void setSteps(final FormatStep[] steps) {
        FormatStep[] colorSteps = new FormatStep[steps.length];
        for (int i = 0; i < steps.length; i++) {
            colorSteps[i] = colorize(steps[i], darken);
        }
        super.setSteps(colorSteps);
    }

    private String colorizePlain(final String str) {
        return str;
    }

    public String formatMessage(final LogRecord logRecord) {
        if (logRecord instanceof ExtLogRecord) {
            final ExtLogRecord record = (ExtLogRecord) logRecord;
            if (record.getFormatStyle() != ExtLogRecord.FormatStyle.PRINTF || record.getParameters() == null || record.getParameters().length == 0) {
                return colorizePlain(super.formatMessage(record));
            }
            return printf.format(record.getMessage(), record.getParameters());
        } else {
            return colorizePlain(super.formatMessage(logRecord));
        }
    }

}

@TargetClass(ColorPatternFormatter.class)
final class Target_ColorPatternFormatter {

    @Delete
    static boolean trueColor = false;

    @Substitute
    static boolean isTrueColor() {
        return TrueColorHolder.trueColor;
    }
}

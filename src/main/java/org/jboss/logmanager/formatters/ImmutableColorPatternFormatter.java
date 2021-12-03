package org.jboss.logmanager.formatters;

import com.oracle.svm.core.annotate.Delete;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import org.jboss.logmanager.ExtLogRecord;
import org.wildfly.common.format.Printf;

import java.util.logging.LogRecord;

import static org.jboss.logmanager.formatters.ColorPatternUtil.*;

public class ImmutableColorPatternFormatter extends ImmutablePatternFormatter {

    private final Printf printf;

    public ImmutableColorPatternFormatter(int darken, final String pattern) {
        super(pattern);
        this.printf = new ColorPrintf(darken);
    }

    static final boolean trueColor = determineTrueColor();

    static boolean isTrueColor() {
        return trueColor;
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

@TargetClass(ImmutableColorPatternFormatter.class)
final class Target_ImmutableColorPatternFormatter {

    @Delete
    static boolean trueColor = false;

    @Substitute
    static boolean isTrueColor() {
        return TrueColorHolder.trueColor;
    }
}

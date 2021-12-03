package org.jboss.logmanager.formatters;

import org.jboss.logmanager.ExtLogRecord;
import org.jboss.logmanager.Level;

import java.util.logging.Formatter;

import static java.lang.Math.abs;

final class ColorPatternUtil {
    private ColorPatternUtil() {}


    static FormatStep colorize(final FormatStep step, int darken) {
        switch (step.getItemType()) {
            case LEVEL:
                return new LevelColorStep(step, darken);
            case SOURCE_CLASS_NAME:
                return new ColorStep(step, 0xff, 0xff, 0x44, darken);
            case DATE:
                return new ColorStep(step, 0xc0, 0xc0, 0xc0, darken);
            case SOURCE_FILE_NAME:
                return new ColorStep(step, 0xff, 0xff, 0x44, darken);
            case HOST_NAME:
                return new ColorStep(step, 0x44, 0xff, 0x44, darken);
            case SOURCE_LINE_NUMBER:
                return new ColorStep(step, 0xff, 0xff, 0x44, darken);
            case LINE_SEPARATOR:
                return step;
            case CATEGORY:
                return new ColorStep(step, 0x44, 0x88, 0xff, darken);
            case MDC:
                return new ColorStep(step, 0x44, 0xff, 0xaa, darken);
            case MESSAGE:
                return new ColorStep(step, 0xff, 0xff, 0xff, darken);
            case EXCEPTION_TRACE:
                return new ColorStep(step, 0xff, 0x44, 0x44, darken);
            case SOURCE_METHOD_NAME:
                return new ColorStep(step, 0xff, 0xff, 0x44, darken);
            case SOURCE_MODULE_NAME:
                return new ColorStep(step, 0x88, 0xff, 0x44, darken);
            case SOURCE_MODULE_VERSION:
                return new ColorStep(step, 0x44, 0xff, 0x44, darken);
            case NDC:
                return new ColorStep(step, 0x44, 0xff, 0xaa, darken);
            case PROCESS_ID:
                return new ColorStep(step, 0xdd, 0xbb, 0x77, darken);
            case PROCESS_NAME:
                return new ColorStep(step, 0xdd, 0xdd, 0x77, darken);
            case RELATIVE_TIME:
                return new ColorStep(step, 0xc0, 0xc0, 0xc0, darken);
            case RESOURCE_KEY:
                return new ColorStep(step, 0x44, 0xff, 0x44, darken);
            case SYSTEM_PROPERTY:
                return new ColorStep(step, 0x88, 0x88, 0x00, darken);
            case TEXT:
                return new ColorStep(step, 0xd0, 0xd0, 0xd0, darken);
            case THREAD_ID:
                return new ColorStep(step, 0x44, 0xaa, 0x44, darken);
            case THREAD_NAME:
                return new ColorStep(step, 0x44, 0xaa, 0x44, darken);
            case COMPOUND:
            case GENERIC:
            default:
                return new ColorStep(step, 0xb0, 0xd0, 0xb0, darken);
        }
    }

    static boolean determineTrueColor() {
        final String colorterm = System.getenv("COLORTERM");
        return (colorterm != null && (colorterm.contains("truecolor") || colorterm.contains("24bit")));
    }

    private static final class LevelColorStep implements FormatStep {
        private static final int LARGEST_LEVEL = Level.ERROR.intValue();
        private static final int SMALLEST_LEVEL = Level.TRACE.intValue();
        private static final int SATURATION = 66;
        private final FormatStep delegate;
        private final int darken;

        LevelColorStep(final FormatStep delegate, final int darken) {
            this.delegate = delegate;
            this.darken = darken;
        }

        public void render(final Formatter formatter, final StringBuilder builder, final ExtLogRecord record) {
            final int level = Math.max(Math.min(record.getLevel().intValue(), LARGEST_LEVEL), SMALLEST_LEVEL) - SMALLEST_LEVEL;
            // really crappy linear interpolation
            int r = ((level < 300 ? 0 : (level - 300) * (255 - SATURATION) / 300) + SATURATION) >>> darken;
            int g = ((300 - abs(level - 300)) * (255 - SATURATION) / 300 + SATURATION) >>> darken;
            int b = ((level > 300 ? 0 : level * (255 - SATURATION) / 300) + SATURATION) >>> darken;
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
            return false;
        }
    }
}

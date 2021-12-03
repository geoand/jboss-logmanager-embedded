package org.jboss.logmanager.formatters;

public class ImmutablePatternFormatter extends ImmutableMultistepFormatter {

    private final String pattern;

    public ImmutablePatternFormatter(String pattern) {
        super(FormatStringParser.getSteps(pattern, ColorMap.DEFAULT_COLOR_MAP));
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}

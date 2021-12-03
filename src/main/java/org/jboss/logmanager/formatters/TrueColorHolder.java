package org.jboss.logmanager.formatters;

// delayed init on native image
final class TrueColorHolder {
    private TrueColorHolder() {
    }

    static final boolean trueColor = ColorPatternUtil.determineTrueColor();
}

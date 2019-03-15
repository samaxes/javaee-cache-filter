package com.samaxes.filter.util;

public class StringUtil
{
    /**
     * In abscence of {@link String#join} in 1.6
     * @param  delimiter the delimiter that separates each element
     * @param  elements the elements to join together.
     *
     * @return a new {@code String} that is composed of the {@code elements}
     *         separated by the {@code delimiter}
     */
    public static String join(CharSequence delimiter, CharSequence... elements) {
        boolean first = true;
        StringBuilder str = new StringBuilder();
        for (CharSequence s : elements) {
            if (!first) str.append(delimiter);
            str.append(s);
            first = false;
        }
        return str.toString();
    }
}

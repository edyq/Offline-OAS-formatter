package org.ntu.apiconverter.common.formatter;

/**
 *
 */
public class StringApiDocFormatter extends ApiDocFormatter{
    @Override
    public String format(String key, Object object, int level, String padding) {
        if (!object.getClass().isAssignableFrom(String.class)){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        indentation(level, sb);
        sb.append(padding);
        sb.append(key + ": "+(String) object);
        newLine(sb);

        return sb.toString();

    }
}

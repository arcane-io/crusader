package crusader.commons;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for String manipulation.
 * Extends functionality from {@link org.apache.commons.lang3.StringUtils}
 * 
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public final class StringUtil extends org.apache.commons.lang3.StringUtils {
    /**
     * Default values separator for string array.
     */
    public static final String STRING_ARRAY_SEPARATOR = ",";
    /**
     * Separator for query params.
     */
    public static final String QUERY_PARAMS_SEPARATOR = "&";
    /**
     * Separator for key/value pair.
     */
    public static final String KEY_VALUE_SEPARATOR = "=";

    /**
     * Convenience method to concatenate string values.
     *
     * @param separator Separator for values
     * @param values    String values to be added
     * @return Resulted string value
     */
    public static String concat(String separator, String... values) {
        StringBuilder builder = new StringBuilder();

        if (values != null) {
            for (String value : values) {
                if (!isEmpty(value)) {
                    if (builder.length() == 0) {
                        builder.append(separator);
                    }
                    builder.append(value);
                }
            }
        }

        return builder.toString();
    }

    /**
     * Convert string value to string array.
     *
     * @param source    String value to convert
     * @param separator Value separator. Default ","
     * @return String array
     */
    public static String[] toStringArray(String source, String separator) {
        if (isEmpty(source)) {
            //return empty array
            return new String[0];
        }
        
        separator = stripToNull(separator);
        String[] stringArray = source.split(separator == null ? STRING_ARRAY_SEPARATOR : separator);
        String[] stringArrayCopy = new String[stringArray.length];

        int index = 0;
        for (String value : stringArray) {
            value = stripToNull(value);

            if (value != null) {
                stringArrayCopy[index++] = value;
            }
        }
        
        //remove null values and return
        return Arrays.copyOf(stringArrayCopy, index);
    }

    /**
     * Create a map from a key/value string.
     *
     * @param source    source string
     * @param separator key/value separator
     * @return Map created from source string
     */
    public static Map<String, String> getKeyValuePair(String source, String separator) {
        String[] sourceArray = toStringArray(source, separator);
        return getKeyValuePair(sourceArray);
    }

    /**
     * Create a map from a key/value string.
     *
     * @param source source string array
     * @return Map created from source string array
     */
    public static Map<String, String> getKeyValuePair(String[] source) {
        Map<String, String> map = new HashMap<String, String>();

        if (source != null) {
            for (String part : source) {
                //split in only 2 parts
                String[] parts = part.split(KEY_VALUE_SEPARATOR, 2);
                String key = parts[0].trim();

                //add only valid key/value pairs
                if (!isEmpty(key) && parts.length == 2) {
                    //set null if value is empty string
                    map.put(key, stripToNull(parts[1]));
                }
            }
        }

        return map;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private StringUtil() {
    }
}

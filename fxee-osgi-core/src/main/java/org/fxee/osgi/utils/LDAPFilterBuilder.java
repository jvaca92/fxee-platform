package org.fxee.osgi.utils;

/**
 * Helper for building LDAP filter
 * @author Jan Vaca <jan.vaca92@gmail.com> on 12/1/2018
 */
public class LDAPFilterUtil {

    private static final String AND_PATTERN= "(&%s %s)";

    private static final String OR_PATTERN= "(|%s %s)";

    private static final String EQUAL_PATTERN = "(%s=%s)";

    private static final String NOT_EQUAL_PATTERN = "(%s=%s)";

    public static  String equal(String key, String value) {
        return String.format(EQUAL_PATTERN, key, value);
    }

    public static String notEqual(String key, String value) {
        return String.format(NOT_EQUAL_PATTERN, key, value);
    }

    public static String and(String equal1, String equal2) {
        return String.format(AND_PATTERN, equal1, equal2);
    }

    public static String or(String equal1, String equal2) {
        return String.format(OR_PATTERN, equal1, equal2);
    }



}

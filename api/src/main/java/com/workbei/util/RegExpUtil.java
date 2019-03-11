package com.workbei.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wallace Mao
 * Date: 2018-12-09 19:28
 */
public class RegExpUtil {
    private static Pattern PATTERN_PATH_VARIABLE = Pattern.compile("\\{\\w+}");

    public static String replacePathVariable(String originUrl, Map json) {
        Matcher matcher = PATTERN_PATH_VARIABLE.matcher(originUrl);
        String  result = originUrl;
        while (matcher.find()) {
            String groupString = matcher.group();
            String key = groupString.substring(1, groupString.length() - 1);
            if (json.containsKey(key)) {
                result = result.replace(matcher.group(), String.valueOf(json.get(key)));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("-------------------");
        Map<String, Object> json = new HashMap<>();
        json.put("abc", "123");
        json.put("xyz", "456");
        String result = replacePathVariable("aaa.com/{abc}/ddd/{xyz}", json);
        System.out.println(result);
    }
}

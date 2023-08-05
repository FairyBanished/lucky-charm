package com.fairy.lucky.charm.common.util;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class CommonUtils {

    /**
     * 转换为字符串，补齐0
     *
     * @param set
     * @return
     */
    public static String completingZero(TreeSet<Long> set) {
        return set.stream().map(num -> String.format("%02d", num)).collect(Collectors.joining(" "));
    }

}

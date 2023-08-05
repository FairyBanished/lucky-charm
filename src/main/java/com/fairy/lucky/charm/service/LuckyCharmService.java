package com.fairy.lucky.charm.service;

import java.util.Set;

/**
 * 幸运锦鲤
 */
public interface LuckyCharmService {

    /**
     *
     * 体彩超级大乐透：前区35选5，后区12选2
     *
     * @param excludeFront 前区排除号码
     * @param excludeBack  后区排除号码
     * @param count        生成注数
     */
    default void superLotto(Set<String> excludeFront, Set<String> excludeBack, int count) {
        lucky(35, 12, 5, 2, count, excludeFront, excludeBack);
    }

    /**
     * 福彩双色球：前区33选6，后区16选1
     *
     * @param excludeFront 前区排除号码
     * @param excludeBack  后区排除号码
     * @param count        生成注数
     */
    default void doubleColorBall(Set<String> excludeFront, Set<String> excludeBack, int count) {
        lucky(33, 16, 6, 1, count, excludeFront, excludeBack);
    }

    /**
     * @param frontLimit   前区最大值
     * @param backLimit    后区最大值
     * @param frontNumber  前区球个数
     * @param backNumber   后区球个数
     * @param count        生成注数
     * @param excludeFront 前区排除号码
     * @param excludeBack  后区排除号码
     */
    void lucky(int frontLimit, int backLimit, int frontNumber, int backNumber, int count, Set<String> excludeFront, Set<String> excludeBack);
}

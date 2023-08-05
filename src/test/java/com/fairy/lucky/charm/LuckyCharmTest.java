package com.fairy.lucky.charm;

import com.fairy.lucky.charm.service.LuckyCharmService;
import com.fairy.lucky.charm.service.impl.MutualExclusionServiceImpl;
import com.fairy.lucky.charm.service.impl.PureRandomServiceImpl;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 幸运锦鲤
 */
public class LuckyCharmTest {

    public static void main(String[] args) {
        Set<String> excludeFront = Sets.newConcurrentHashSet();
        excludeFront.addAll(Set.of());

        Set<String> excludeBack = Sets.newConcurrentHashSet();
        excludeBack.addAll(Set.of());

        LuckyCharmService service = new MutualExclusionServiceImpl();
        LuckyCharmService ps = new PureRandomServiceImpl();
//        System.out.println("互斥(本文方法)：");
//        service.superLotto(excludeFront, excludeBack, 7);
////        System.out.println("----------------------------------");
//        System.out.println("随机：");
//        ps.superLotto(excludeFront, excludeBack, 7);

        System.out.println("互斥(本文方法)：");
        service.doubleColorBall(excludeFront, excludeBack, 6);
        System.out.println("----------------------------------");
        System.out.println("随机：");
         ps.doubleColorBall(excludeFront, excludeBack,6);


    }


}

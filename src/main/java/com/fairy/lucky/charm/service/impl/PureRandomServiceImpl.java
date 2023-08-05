package com.fairy.lucky.charm.service.impl;

import com.fairy.lucky.charm.service.LuckyCharmService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static com.fairy.lucky.charm.common.util.CommonUtils.completingZero;

/**
 * 随机锦鲤
 */
@Service("PureRandomServiceImpl")
public class PureRandomServiceImpl implements LuckyCharmService {

    /**
     * @param frontLimit   前区最大值
     * @param backLimit    后区最大值
     * @param frontNumber  前区球个数
     * @param backNumber   后区球个数
     * @param count        生成注数
     * @param excludeFront 前区排除号码
     * @param excludeBack  后区排除号码
     */
    @Override
    public void lucky(int frontLimit, int backLimit, int frontNumber, int backNumber, int count, Set<String> excludeFront, Set<String> excludeBack) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(20);
            long maxFront = 1;
            long maxBack = 1;
            for (int i = frontLimit; i > frontLimit - frontNumber; i--) {
                maxFront = maxFront * i;
            }
            for (int i = backLimit; i > backLimit - backNumber; i--) {
                maxBack = maxBack * i;
            }
            List<TreeSet<Long>> frontArea = Lists.newCopyOnWriteArrayList();
            List<TreeSet<Long>> backArea = Lists.newCopyOnWriteArrayList();
            CountDownLatch cdl = new CountDownLatch(2 * count);
            long MAX_FRONT = maxFront;
            long MAX_BACK = maxBack;
            executor.submit(() -> {
                for (int i = 0; i < count; i++) {
                    executor.submit(() -> {
                        TreeSet<Long> generatedNum = Sets.newTreeSet();
                        while (true) {
                            long random = ThreadLocalRandom.current().nextLong(1, MAX_FRONT);
                            long num = random % (frontLimit + 1);
                            if (num != 0 && !excludeFront.contains(String.valueOf(num))) {
                                generatedNum.add(num);
                            }
                            if (generatedNum.size() == frontNumber) {
                                break;
                            }
                        }
                        if (generatedNum.size() == frontNumber) {
                            frontArea.add(generatedNum);
                        }
                        cdl.countDown();
                    });
                }
            });
            executor.submit(() -> {
                for (int i = 0; i < count; i++) {
                    executor.submit(() -> {
                        TreeSet<Long> generatedNum = Sets.newTreeSet();
                        while (true) {
                            long random = ThreadLocalRandom.current().nextLong(1, MAX_BACK);
                            long num = random % (backLimit + 1);
                            if (num != 0 && !excludeBack.contains(String.valueOf(num))) {
                                generatedNum.add(num);
                            }
                            if (generatedNum.size() == backNumber) {
                                break;
                            }
                        }
                        backArea.add(generatedNum);
                        cdl.countDown();
                    });
                }
            });
            cdl.await();
            executor.shutdown();
            Set<String> fUsed = Sets.newConcurrentHashSet();
            Set<String> bUsed = Sets.newConcurrentHashSet();
            for (int i = 0; i < count; i++) {
                while (true) {
                    int frontIndex = (int) (ThreadLocalRandom.current().nextLong(1, MAX_FRONT) % count);
                    int backIndex = (int) (ThreadLocalRandom.current().nextLong(1, MAX_BACK) % count);
                    String fi = String.valueOf(frontIndex);
                    String bi = String.valueOf(backIndex);
                    if (!fUsed.contains(fi) && !bUsed.contains(bi)) {
                        fUsed.add(fi);
                        bUsed.add(bi);
                        System.out.println(String.format("%s", completingZero(frontArea.get(frontIndex)) + "  +  " + completingZero(backArea.get(backIndex))));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

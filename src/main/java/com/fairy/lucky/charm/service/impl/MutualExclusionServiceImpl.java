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
 * 互斥锦鲤
 *
 */
@Service("MutualExclusionServiceImpl")
public class MutualExclusionServiceImpl implements LuckyCharmService {

    /**
     * 大家自己选号，一个球都不中的概率很大，本方法模拟自己选号，已选中的不重复使用(双色球除外，双色球必有3个重复的号，因为33/6不是整数)，假设选出的一组号码都是不中的(概率比较大)，则剩下的就是全中的，前区有Math.ceil(frontLimit * 1.0 / frontNumber)组，后区有backLimit / backNumber组
     * 那么这些组中，前区必有一组全中，后区必有一组全中，通过随机筛选，假设选中的前区必中+后区必中就是当期中奖号码
     *
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
            final Set<String> frontUsed = Sets.newConcurrentHashSet(excludeFront);
            final Set<String> backUsed = Sets.newConcurrentHashSet(excludeBack);
            List<TreeSet<Long>> frontArea = Lists.newCopyOnWriteArrayList();
            List<TreeSet<Long>> backArea = Lists.newCopyOnWriteArrayList();
            int generateCount = (int) Math.ceil((frontLimit - excludeFront.size()) * 1.0 / frontNumber);
            int countReality = count > generateCount ? generateCount : count;
            CountDownLatch cdl = new CountDownLatch(2 * countReality);
            long MAX_FRONT = maxFront;
            long MAX_BACK = maxBack;
            executor.submit(() -> {
                for (int i = 0; i < countReality; i++) {
                    executor.submit(() -> {
                        TreeSet<Long> generatedNum = Sets.newTreeSet();
                        outerLoop:
                        while (true) {
                            long random = ThreadLocalRandom.current().nextLong(1, MAX_FRONT);
                            long num = random % (frontLimit + 1);
                            if (num != 0 && frontUsed.add(String.valueOf(num))) {
                                generatedNum.add(num);
                            }
                            if (generatedNum.size() == frontNumber) {
                                break;
                            }
                            if (generatedNum.size() < frontNumber && frontUsed.size() == frontLimit) {
                                Set<String> used = Sets.newTreeSet(excludeFront);
                                while (true) {
                                    long randomIgnore = ThreadLocalRandom.current().nextLong(1, MAX_FRONT);
                                    long numIgnore = randomIgnore % (frontLimit + 1);
                                    String ni = String.valueOf(numIgnore);
                                    if (numIgnore != 0 && !used.contains(ni)) {
                                        used.add(ni);
                                        generatedNum.add(numIgnore);
                                    }
                                    if (generatedNum.size() == frontNumber) {
                                        break outerLoop;
                                    }
                                }
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
                for (int i = 0; i < countReality; i++) {
                    executor.submit(() -> {
                        TreeSet<Long> generatedNum = Sets.newTreeSet();
                        outerLoop:
                        while (true) {
                            long random = ThreadLocalRandom.current().nextLong(1, MAX_BACK);
                            long num = random % (backLimit + 1);
                            if (num != 0 && backUsed.add(String.valueOf(num))) {
                                generatedNum.add(num);
                            }
                            if (generatedNum.size() == backNumber) {
                                break;
                            }
                            if (generatedNum.size() < backNumber && backUsed.size() == backLimit) {
                                Set<String> used = Sets.newTreeSet(excludeBack);
                                while (true) {
                                    long randomIgnore = ThreadLocalRandom.current().nextLong(1, MAX_BACK);
                                    long numIgnore = randomIgnore % (backLimit + 1);
                                    String ni = String.valueOf(numIgnore);
                                    if (numIgnore != 0 && !used.contains(ni)) {
                                        used.add(ni);
                                        generatedNum.add(numIgnore);
                                    }
                                    if (generatedNum.size() == backNumber) {
                                        break outerLoop;
                                    }
                                }
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
            for (int i = 0; i < countReality; i++) {
                while (true) {
                    int frontIndex = (int) (ThreadLocalRandom.current().nextLong(1, MAX_FRONT) % countReality);
                    int backIndex = (int) (ThreadLocalRandom.current().nextLong(1, MAX_BACK) % countReality);
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

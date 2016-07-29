package com.jaf.examples.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.assertj.core.util.Lists;
import org.junit.Test;

/**
 * 并行流
 * 
 * @author liaozhicheng
 * @date 2016年7月29日
 * @since 1.0
 */
public class ParallelStreamTests {
	
	@Test
	public void parallelismTest() {
		// 并行流默认采用的 ForkJoinPool 实现
		ForkJoinPool commonPool = ForkJoinPool.commonPool();
		System.out.println(commonPool.getParallelism());    // 7
		
		// 默认的并行级别等于机器可用的cpu数量
		// 可以用下面的方式修改默认的并行级别
		// -Djava.util.concurrent.ForkJoinPool.common.parallelism=3
		Lists.newArrayList("a1", "a2", "b1", "c2", "c1", "d1", "f1", "c3", "h1")
		    .parallelStream()
		    .filter(s -> {
		        System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
		        return true;
		    })
		    .map(s -> {
		        System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
	}
	
	@Test
	public void parallelStreamStortTest() {
		// filter,map 方法可以并行执行，但是 sort 方法却只在一个线程中串行执行
		
		// 并行流的 sort 采用的是 java8 里面的 Arrays.parallelSort() 方法实现
		// If the length of the specified array is less than the minimum
	    // granularity, then it is sorted using the appropriate Arrays.sort() method.
		// 默认的阈值 1 << 13 （8192），即当排序元素的个数小于 8192 个时，默认采用的是串行排序
		Lists.newArrayList("a1", "a2", "b1", "c2", "c1")
		    .parallelStream()
		    .filter(s -> {
		        System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
		        return true;
		    })
		    .map(s -> {
		        System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
		        return s.toUpperCase();
		    })
		    .sorted((s1, s2) -> {
		        System.out.format("sort: %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
		        return s1.compareTo(s2);
		    })
		    .forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
	}
	
	@Test
	public void reduceTest() {
		// reduce 方法可以并行执行，类似的 max,min 方法，底层采用的是 reduce 方法实现，也可以并行实现
		List<Person> persons = Arrays.asList(
		    new Person("Max", 18),
		    new Person("Peter", 23),
		    new Person("Pamela", 23),
		    new Person("David", 12));

		int ageSum = persons
		    .parallelStream()
		    .reduce(0,
		        (sum, p) -> {
		            System.out.format("accumulator: sum=%s; person=%s [%s]\n",
		                sum, p, Thread.currentThread().getName());
		            return sum += p.age;
		        },
		        (sum1, sum2) -> {
		            System.out.format("combiner: sum1=%s; sum2=%s [%s]\n",
		                sum1, sum2, Thread.currentThread().getName());
		            return sum1 + sum2;
		        });
		System.out.println(ageSum);
	}
	
	@Test
	public void maxTest() {
		List<Integer> nums = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		nums.parallelStream().max((x, y) -> {
			System.out.format("comparate: x=%s; y=%s [%s]\n", x, y, Thread.currentThread().getName());
			return x - y;
		});
	}
	
}

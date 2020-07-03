package com.cnnc.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class SumTask extends RecursiveTask<Integer> {

    private Integer start = 0;
    private Integer end = 0;

    public SumTask(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start < 100) {
            int sumResult = 0;
            for (int i = start; i <= end; i++) {
                sumResult += i;
            }
            return sumResult;
        } else {
            int middle = (end + start) / 2;
            SumTask leftSum = new SumTask(this.start, middle);
            SumTask rightSum = new SumTask(middle, this.end);
            leftSum.fork();
            rightSum.fork();
            return leftSum.join() + rightSum.join();
        }
    }

    public static void main(String[] args) {
////        ForkJoinPool forkJoinPool = new ForkJoinPool(100);
//
//        SumTask task = new SumTask(1, 99999);
////        forkJoinPool.submit(task);
//        task.fork();
//        System.out.println("result: " + task.join());

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        list.stream().forEach(System.out::print);
        System.out.println();
        list.stream().map(x -> Integer.toString(++x)).collect(Collectors.toList());

        list.parallelStream().forEach(x -> System.out.println(Thread.currentThread().getName() + ">>" + x));
    }
}

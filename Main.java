import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

public class Main {
    private static final int ITERATIONS = 20; // 반복 실행 횟수 증가

    public static void main(String[] args) {
        int[] sizes = {100, 10_000, 1_000_000};

        for (int size : sizes) {
            int[] numbers = generateNumbers(size);

            System.out.println("Data Size: " + size);

            // JVM Warm-up 실행 (첫 번째 실행은 무시)
            warmUpJVM(numbers);

            // For Loop 실행 시간 평균 계산
            long avgForLoop = calculateAverageTime(numbers, Main::sumUsingForLoop);
            System.out.println("For Loop Avg Execution Time: " + avgForLoop + " ns");

            // Stream 실행 시간 평균 계산
            long avgStream = calculateAverageTime(numbers, Main::sumUsingStream);
            System.out.println("Stream Avg Execution Time: " + avgStream + " ns");

            // Parallel Stream 실행 시간 평균 계산
            long avgParallelStream = calculateAverageTime(numbers, Main::sumUsingParallelStream);
            System.out.println("Parallel Stream Avg Execution Time: " + avgParallelStream + " ns");

            System.out.println("----------------------------------");
        }
    }

    // 특정 연산을 여러 번 실행한 후 평균 실행 시간을 반환
    private static long calculateAverageTime(int[] numbers, ToIntFunction<int[]> method) {
        long totalTime = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            System.gc(); // Garbage Collector 실행으로 메모리 영향 최소화
            long startTime = System.nanoTime();
            method.applyAsInt(numbers);
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        return totalTime / ITERATIONS;
    }

    // JVM Warm-up을 위한 더미 실행 (결과는 무시)
    private static void warmUpJVM(int[] numbers) {
        sumUsingForLoop(numbers);
        sumUsingStream(numbers);
        sumUsingParallelStream(numbers);
    }

    // 주어진 크기의 숫자 배열 생성
    public static int[] generateNumbers(int size) {
        return IntStream.rangeClosed(1, size).toArray();
    }

    // For Loop을 사용한 짝수 합산
    public static int sumUsingForLoop(int[] numbers) {
        int sum = 0;
        for (int num : numbers) {
            if (num % 3 == 0) {
                sum += num;
            }
        }
        return sum;
    }

    // Stream을 사용한 짝수 합산
    public static int sumUsingStream(int[] numbers) {
        return IntStream.of(numbers)
                .filter(num -> num % 2 == 0)
                .sum();
    }

    // Parallel Stream을 사용한 짝수 합산
    public static int sumUsingParallelStream(int[] numbers) {
        return IntStream.of(numbers)
                .parallel()
                .filter(num -> num % 2 == 0)
                .sum();
    }
}

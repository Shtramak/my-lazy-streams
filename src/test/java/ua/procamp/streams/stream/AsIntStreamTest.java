package ua.procamp.streams.stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import ua.procamp.streams.function.IntBinaryOperator;
import ua.procamp.streams.function.IntPredicate;
import ua.procamp.streams.function.IntToIntStreamFunction;
import ua.procamp.streams.function.IntUnaryOperator;

public class AsIntStreamTest {

    private IntStream intStream;
    private IntStream emptyStream;

    @Before
    public void init() {
        int[] intArr = {0, -1, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
        emptyStream = AsIntStream.of();
    }

    @Test
    public void testSum() {
        System.out.println("sum");
        Integer expResult = 5;
        Integer result = intStream.sum();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testSumWhenStreamIsEmpty() {
        System.out.println("sum when stream is empty");
        emptyStream.sum();
    }

    @Test
    public void testAverage() {
        System.out.println("average");
        Double expResult = 1.0;
        Double result = intStream.average();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAverageWhenStreamIsEmpty() {
        System.out.println("average when stream is empty");
        emptyStream.average();
    }

    @Test
    public void testMax() {
        System.out.println("max");
        Integer expResult = 3;
        Integer result = intStream.max();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxWhenStreamIsEmpty() {
        System.out.println("max when stream is empty");
        emptyStream.max();
    }

    @Test
    public void testMin() {
        System.out.println("min");
        Integer expResult = -1;
        Integer result = intStream.min();
        assertEquals(expResult, result);
    }


    @Test(expected = IllegalStateException.class)
    public void testMinWhenStreamIsEmpty() {
        System.out.println("min when stream is empty");
        emptyStream.min();
    }

    @Test
    public void testCount() {
        System.out.println("count");
        long expResult = 5;
        long result = intStream.count();
        assertEquals(expResult, result);
    }

    @Test
    public void testCountWhenStreamIsEmpty() {
        System.out.println("count");
        long expResult = 0;
        long result = emptyStream.count();
        assertEquals(expResult, result);
    }

    @Test
    public void testToArray() {
        int[] expResult = {0, -1, 1, 2, 3};
        int[] result = intStream.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testToArrayWhenStreamIsEmpty() {
        int[] expResult = new int[0];
        int[] result = emptyStream.toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testFilter() {
        IntPredicate predicate = x -> x > 0;
        int[] expResult = {1, 2, 3};
        int[] result = intStream
                .filter(predicate)
                .toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testFilterWhenEmptyStream() {
        IntPredicate predicate = x -> x > 0;
        int[] expResult = new int[0];
        int[] result = emptyStream
                .filter(predicate)
                .toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testForEach() {
        StringBuilder builder = new StringBuilder();
        intStream.forEach(builder::append);
        String expResult = "0-1123";
        String result = builder.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testMap() {
        IntUnaryOperator unaryOperator = x -> x * 2;
        int[] expResult = new int[]{0, -2, 2, 4, 6};
        int[] result = intStream.map(unaryOperator).toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testFlatMap() {
        IntToIntStreamFunction streamFunction = x -> AsIntStream.of(x, 2 * x, x * x);
        int[] expResult = new int[]{0, 0, 0, -1, -2, 1, 1, 2, 1, 2, 4, 4, 3, 6, 9};
        int[] result = intStream.flatMap(streamFunction).toArray();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testReduce() {
        IntBinaryOperator binaryOperator = (a, b) -> a + b;
        int result = intStream.reduce(0, binaryOperator);
        int expResult = 5;
        assertEquals(expResult, result);
    }
}

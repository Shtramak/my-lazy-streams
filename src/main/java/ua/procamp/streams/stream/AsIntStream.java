package ua.procamp.streams.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ua.procamp.streams.function.IntBinaryOperator;
import ua.procamp.streams.function.IntConsumer;
import ua.procamp.streams.function.IntPredicate;
import ua.procamp.streams.function.IntToIntStreamFunction;
import ua.procamp.streams.function.IntUnaryOperator;

public class AsIntStream implements IntStream {

    private List<Integer> elements;

    private AsIntStream(List<Integer> elements) {
        this.elements = elements;
    }

    public static IntStream of(int... values) {
        List<Integer> elements = new ArrayList<>();
        for (int value : values) {
            elements.add(value);
        }
        return new AsIntStream(elements);
    }

    @Override
    public Double average() {
        verifyStream();
        int sum = 0;
        for (Integer element : elements) {
            sum += element;
        }
        return (double) sum / size();
    }

    @Override
    public Integer max() {
        verifyStream();
        Integer max = elements.get(0);
        for (Integer element : elements) {
            if (element > max) {
                max = element;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        verifyStream();
        Integer min = elements.get(0);
        for (Integer element : elements) {
            if (element < min) {
                min = element;
            }
        }
        return min;
    }

    @Override
    public long count() {
        return size();
    }

    @Override
    public Integer sum() {
        verifyStream();
        int sum = 0;
        for (Integer element : elements) {
            sum += element;
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        for (int i = 0; i < size(); i++) {
            Integer currentElement = elements.get(i);
            boolean isFalsePredicate = !predicate.test(currentElement);
            if (isFalsePredicate) {
                elements.remove(currentElement);
                i--;
            }
        }
        return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        for (Integer element : elements) {
            action.accept(element);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        for (int i = 0; i < elements.size(); i++) {
            Integer currentElement = elements.get(i);
            int resultElement = mapper.apply(currentElement);
            elements.set(i, resultElement);
        }
        return this;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        Objects.requireNonNull(func);
        List<Integer> result = new ArrayList<>();
        for (Integer element : elements) {
            AsIntStream stream = (AsIntStream) func.applyAsIntStream(element);
            result.addAll(stream.elements);
        }
        elements = result;
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator operator) {
        Objects.requireNonNull(operator);
        int result = identity;
        for (Integer element : elements) {
            result = operator.apply(result, element);
        }
        return result;
    }

    @Override
    public int[] toArray() {
        int size = size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements.get(i);
        }
        return result;
    }

    private void verifyStream() {
        if (size() == 0) {
            throw new IllegalStateException("No elements found to process");
        }
    }

    private int size() {
        return elements.size();
    }

}

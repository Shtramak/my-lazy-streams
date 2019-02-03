package ua.procamp.streams.stream;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ua.procamp.streams.function.IntBinaryOperator;
import ua.procamp.streams.function.IntConsumer;
import ua.procamp.streams.function.IntPredicate;
import ua.procamp.streams.function.IntToIntStreamFunction;
import ua.procamp.streams.function.IntUnaryOperator;

public class AsIntStream implements IntStream {

    private Iterable<Integer> storage;
    private int size;

    private AsIntStream(List<Integer> list) {
        storage = list;
        size = list.size();
    }

    public static IntStream of(int... values) {
        List<Integer> result = new LinkedList<>();
        for (int value : values) {
            result.add(value);
        }
        return new AsIntStream(result);
    }

    @Override
    public Double average() {
        Integer sum = sum();
        return (double) sum / size;
    }

    @Override
    public Integer max() {
        verifyStorage(storage);
        Iterator<Integer> iterator = storage.iterator();
        Integer max = iterator.next();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            if (element > max) {
                max = element;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        verifyStorage(storage);
        Iterator<Integer> iterator = storage.iterator();
        Integer min = iterator.next();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            if (element < min) {
                min = element;
            }
        }
        return min;
    }

    @Override
    public long count() {
        return size;
    }

    @Override
    public Integer sum() {
        verifyStorage(storage);
        Iterator<Integer> iterator = storage.iterator();
        Integer sum = 0;
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            sum += element;
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        storage = filteredStorage(storage, predicate);
        return this;
    }

    private Iterable<Integer> filteredStorage(Iterable<Integer> storage, IntPredicate predicate) {
        return () -> {
            Iterator<Integer> iterator = storage.iterator();
            while (iterator.hasNext()) {
                if (!predicate.test(iterator.next())) {
                    iterator.remove();
                    size--;
                }
            }
            return storage.iterator();
        };
    }

    @Override
    public void forEach(IntConsumer action) {
        for (Integer element : storage) {
            action.accept(element);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        storage = mappedStorage(storage, mapper);
        return this;
    }

    private Iterable<Integer> mappedStorage(Iterable<Integer> storage, IntUnaryOperator mapper) {
        return () -> {
            Iterator<Integer> iterator = storage.iterator();
            List<Integer> newStorage = new LinkedList<>();
            while (iterator.hasNext()) {
                Integer element = iterator.next();
                int mappedElement = mapper.apply(element);
                newStorage.add(mappedElement);
            }
            return newStorage.iterator();
        };
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction function) {
        Objects.requireNonNull(function);
        storage = flatMappedStorage(storage, function);
        return this;
    }

    private Iterable<Integer> flatMappedStorage(Iterable<Integer> storage, IntToIntStreamFunction function) {
        return () -> {
            Iterator<Integer> iterator = storage.iterator();
            List<Integer> result = new LinkedList<>();
            while (iterator.hasNext()) {
                Integer element = iterator.next();
                AsIntStream stream = (AsIntStream) function.applyAsIntStream(element);
                List<Integer> streamStorage = (LinkedList<Integer>) stream.storage;
                result.addAll(streamStorage);
            }
            size = result.size();
            return result.iterator();
        };
    }

    @Override
    public int reduce(int identity, IntBinaryOperator operator) {
        Objects.requireNonNull(operator);
        int result = identity;
        for (Integer element : storage) {
            result = operator.apply(result, element);
        }
        return result;
    }

    @Override
    public int[] toArray() {
        Iterator<Integer> iterator = storage.iterator();
        int[] result = new int[size];
        int index = 0;
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            result[index] = element;
            index++;
        }
        return result;
    }

    private void verifyStorage(Iterable<Integer> storage) {
        if (!storage.iterator().hasNext()) {
            throw new IllegalStateException("No elements found to process");
        }
    }
}

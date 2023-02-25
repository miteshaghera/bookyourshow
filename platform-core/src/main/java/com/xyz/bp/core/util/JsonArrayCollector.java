package com.xyz.bp.core.util;

import io.vertx.core.json.JsonArray;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class JsonArrayCollector<T> implements Collector<T, JsonArray, JsonArray> {
    @Override
    public Supplier<JsonArray> supplier() {
        return JsonArray::new;
    }

    @Override
    public BiConsumer<JsonArray, T> accumulator() {
        return JsonArray::add;
    }

    @Override
    public BinaryOperator<JsonArray> combiner() {
        return (left, right) -> {
            if (left.size() >= right.size()) {
                left.addAll(right);
                return left;
            } else {
                right.addAll(left);
                return right;
            }
        };
    }

    @Override
    public Function<JsonArray, JsonArray> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.IDENTITY_FINISH);
    }
}

package io.github.lyr2000.common.dto;

import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HashMap 的 put 方法 不能链式编程
 * 这里做一层封装
 *
 * @Author lyr
 * @create 2021/2/11 18:10
 */
@Data
public class Maps<K, V> {
    private Map<K, V> map;

    private Maps(Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> Maps<K, V> build(Map<K, V> map) {
        return new Maps<>(map);
    }

    public static <K, V> Maps<K, V> hashMap() {
        return build(new HashMap<>());
    }

    public static <K, V> Maps<K, V> linkedHashMap() {

        return new Maps<>(new LinkedHashMap<>());
    }

    public static <K, V> Maps<K, V> treeMap() {
        return build(new TreeMap<>());
    }

    public static <K, V> Maps<K, V> treeMap(Comparator<Object> comparator) {
        return build(new TreeMap<>(comparator));
    }

    public static <K, V> Maps<K, V> concurrentHashMap() {
        return build(new ConcurrentHashMap<>());
    }


    public Maps<K, V> put(K key, V v) {
        this.map.put(key, v);
        return this;
    }


}

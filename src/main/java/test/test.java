package test;

/**
 * @author 李东
 * @version 1.0
 * @date 2020/1/9 13:51
 */
@FunctionalInterface
public interface test<T> {
    T apply(T t);
}

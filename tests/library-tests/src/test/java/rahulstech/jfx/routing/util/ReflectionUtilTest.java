package rahulstech.jfx.routing.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilTest {

    @ParameterizedTest
    @MethodSource("newInstanceClassAndArgumentsTestSource")
    void newInstance(Callable<Object> callable) {
        assertDoesNotThrow(()->{
            Object instance = callable.call();
        });
    }

    static Stream<Callable<Object>> newInstanceClassAndArgumentsTestSource() {
        return Stream.of(
                ()->ReflectionUtil.newInstance(C.class,new Object[]{new B()}),
                ()->ReflectionUtil.newInstance(D.class,new Object[0])
        );
    }

    static class A {}

    static class B extends A {}

    static class C {

        C(A a) {}
    }

    static class D {}
}
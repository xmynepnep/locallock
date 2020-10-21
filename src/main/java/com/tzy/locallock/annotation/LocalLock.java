package com.tzy.locallock.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LocalLock {
    String key() default "defaultKey1";

    String key2() default "defaultKey2";

    String key3() default "defaultKey3";
}

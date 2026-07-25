package at.yedel.yedelmod.config;



import cc.polyfrost.oneconfig.config.annotations.CustomOption;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomOption(id = "empty")
public @interface Empty {
    String name();

    String description();

    String category();

    String subcategory();

    int size() default 2;
}

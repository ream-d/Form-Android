package ream.form.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 2017/1/12 0012.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface FormField {
    String beanFiled() default "";
}

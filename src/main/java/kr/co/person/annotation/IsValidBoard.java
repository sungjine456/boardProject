package kr.co.person.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidBoard {
	int idx() default 0;
	String title() default "";
	String content() default "";
	int hitCount() default 0;
}

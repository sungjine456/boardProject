package kr.co.person.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidUser {
	int idx() default 0;
	String id() default "";
	String name() default "";
	String email() default "";
	String password() default "";
	String img() default "";
}

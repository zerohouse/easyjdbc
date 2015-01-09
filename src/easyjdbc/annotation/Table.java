package easyjdbc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	String value();

	String defaultCondition() default "";

	int pageSize() default 10;

	String joinWith() default "";
	
	String joinType() default "inner";
	
	String on() default "";
	
}

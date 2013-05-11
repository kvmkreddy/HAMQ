package org.os.javaee.jms.core.mapper.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Title: Mapper.java</p>
 * <p>Description: A program element annotated &#64;Mapper is one that acts as MessageMapper. This is a class level annotation.</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Open Source Development.</p>
 * @author Murali Reddy
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
	/**
	 * The operation description.
	 * @return The operation description.
	 */
	String description() default "Mapper Object"; 
	
	/**
	 * The operation name.
	 * @return The operation name.
	 */
	String name() default "Mapper";
}

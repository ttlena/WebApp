package de.hsrm.mi.web.projekt.validierung;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=BuntValidator.class)

public @interface Bunt {
    String message() default "Diese Farbe enthält zwei gleiche R/G/B-Anteileund ist daher nicht bunt genug";
    Class<? extends Payload>[] payload() default { };
    Class<?>[] groups() default { };
}

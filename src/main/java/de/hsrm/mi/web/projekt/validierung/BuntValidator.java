package de.hsrm.mi.web.projekt.validierung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BuntValidator implements ConstraintValidator<Bunt, String>{
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")){
            return true;

        }else if (value.matches("^#[A-Fa-f0-9]{3}$")){
            char[] rgb = value.toCharArray();
            return (rgb[1] != rgb[2] && rgb[2] != rgb[3] && rgb[1] != rgb[3]);

        }else if (value.matches("^#[A-Fa-f0-9]{6}$")){
            String r = value.substring(1,3);
            String g = value.substring(3, 5);
            String b = value.substring(5);
            return (!r.equals(g) && !g.equals(b) && !r.equals(b));
        }

        return false;
    }
    
}

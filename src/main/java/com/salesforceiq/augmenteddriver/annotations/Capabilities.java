package com.salesforceiq.augmenteddriver.annotations;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Capabilities {

    String value();

}

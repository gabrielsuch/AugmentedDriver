package com.salesforceiq.augmenteddriver.annotations;

import com.google.inject.AbstractModule;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiceModules {

    /**
     * The Guice Modules classes needed by the class under test.
     */
    Class<? extends AbstractModule>[] value();

}
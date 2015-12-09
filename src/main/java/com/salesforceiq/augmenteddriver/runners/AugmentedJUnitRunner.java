package com.salesforceiq.augmenteddriver.runners;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.salesforceiq.augmenteddriver.annotations.ExtraModules;
import com.salesforceiq.augmenteddriver.annotations.GuiceModules;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class AugmentedJUnitRunner extends BlockJUnit4ClassRunner {

    private final transient Injector injector;

    public AugmentedJUnitRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        List<Class<? extends AbstractModule>> modules = getGuiceModulesFor(klass);
        modules.addAll(getExtraModulesFor(klass));
        this.injector = this.createInjectorFor(modules);
    }

    @Override
    public final Object createTest() throws Exception {
        final Object obj = super.createTest();
        this.injector.injectMembers(obj);
        return obj;
    }

    private Injector createInjectorFor(final List<Class<? extends AbstractModule>> classes) throws InitializationError {
        List<AbstractModule> modules = Lists.newArrayList();

        for (Class<? extends AbstractModule> clazz : classes) {
            try {
                modules.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        return Guice.createInjector(modules);
    }

    private List<Class<? extends AbstractModule>> getGuiceModulesFor(final Class<?> klass) throws InitializationError {
        final GuiceModules annotation = klass.getAnnotation(GuiceModules.class);

        if (annotation == null) {
            final String message = String.format("Missing @GuiceModules annotation for unit test '%s'", klass.getName());
            throw new InitializationError(message);
        }

        return Lists.newArrayList(annotation.value());
    }

    private List<Class<? extends AbstractModule>> getExtraModulesFor(final Class<?> klass) throws InitializationError {
        final ExtraModules annotation = klass.getAnnotation(ExtraModules.class);
        return annotation == null ? Lists.newArrayList() : Lists.newArrayList(annotation.value());
    }

}

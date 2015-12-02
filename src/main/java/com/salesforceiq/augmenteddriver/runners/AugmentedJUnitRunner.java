package com.salesforceiq.augmenteddriver.runners;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.salesforceiq.augmenteddriver.annotations.Capabilities;
import com.salesforceiq.augmenteddriver.annotations.GuiceModules;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.util.CommandLineArguments;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.Arrays;
import java.util.List;

public class AugmentedJUnitRunner extends BlockJUnit4ClassRunner {

    private final Injector injector;
    private final Class<?> klass;

    public AugmentedJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.klass = klass;
        this.loadArguments();
        this.injector = createInjector(getExtraModules(klass));
    }

    @Override
    public final Object createTest() throws Exception {
        final Object obj = super.createTest();
        injector.injectMembers(obj);
        return obj;
    }

    private Class[] defaultModules() {
        return new Class[] { PropertiesModule.class };
    }

    private Injector createInjector(Class<?>[] classes) throws InitializationError {
        final List<Module> modules = Lists.newArrayList();
        final List<Class> moduleClasses = Lists.newArrayList(defaultModules());
        moduleClasses.addAll(Arrays.asList(classes));

        for (final Class<?> module : moduleClasses) {
            try {
                modules.add((Module) module.newInstance());
            } catch (final ReflectiveOperationException exception) {
                throw new InitializationError(exception);
            }
        }

        return Guice.createInjector(modules);
    }

    private void loadArguments() {
        if (CommandLineArguments.ARGUMENTS != null) {
            return;
        }

        final Capabilities annotation = klass.getAnnotation(Capabilities.class);

        if (annotation != null) {
            CommandLineArguments.initializeForCapabilities(annotation.value());
        }
    }

    private Class<?>[] getExtraModules(Class<?> klass) throws InitializationError {
        final GuiceModules annotation = klass.getAnnotation(GuiceModules.class);
        return annotation == null ? new Class<?>[] {} : annotation.value();
    }

}

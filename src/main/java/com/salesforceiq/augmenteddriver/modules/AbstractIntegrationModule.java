package com.salesforceiq.augmenteddriver.modules;

import com.google.inject.AbstractModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.salesforceiq.augmenteddriver.integrations.Integration;
import com.salesforceiq.augmenteddriver.integrations.ReportIntegration;

public abstract class AbstractIntegrationModule extends AbstractModule {

    private Multibinder<Integration> integrationsBinder;
    private Multibinder<ReportIntegration> reportersBinder;

    @Override
    protected void configure() {
        integrationsBinder = Multibinder.newSetBinder(binder(), Integration.class);
        reportersBinder = Multibinder.newSetBinder(binder(), ReportIntegration.class);
        configureActions();
    }

    protected abstract void configureActions();

    protected final LinkedBindingBuilder<Integration> bindIntegration() {
        return integrationsBinder.addBinding();
    }

    protected final LinkedBindingBuilder<ReportIntegration> bindReportIntegration() {
        return reportersBinder.addBinding();
    }

}

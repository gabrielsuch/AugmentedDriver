package com.salesforceiq.augmenteddriver.modules;

import com.salesforceiq.augmenteddriver.integrations.TeamCityIntegration;


public class TeamCityModule extends AbstractIntegrationModule {

    @Override
    protected void configureActions() {
        bindIntegration().to(TeamCityIntegration.class);
        bindReportIntegration().to(TeamCityIntegration.class);
    }

}

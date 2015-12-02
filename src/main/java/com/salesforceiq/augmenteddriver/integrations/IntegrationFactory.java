package com.salesforceiq.augmenteddriver.integrations;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Factory for all the different implementations.
 */
@Singleton
public class IntegrationFactory {

    @Inject(optional = true)
    private SauceLabsIntegration sauceLabsIntegration;

    @Inject(optional = true)
    private TeamCityIntegration teamCityIntegration;

    public boolean isSauceLabsEnabled() {
        return sauceLabs() != null && sauceLabs().isEnabled();
    }

    public boolean isTeamCityEnabled() {
        return teamCity() != null && teamCity().isEnabled();
    }

    /**
     * Saucelabs Integration.
     */
    public SauceLabsIntegration sauceLabs() {
        return sauceLabsIntegration;
    }

    /**
     * TeamCity Integration.
     */
    public TeamCityIntegration teamCity() {
        return teamCityIntegration;
    }

}

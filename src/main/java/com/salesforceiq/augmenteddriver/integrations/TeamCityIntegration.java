package com.salesforceiq.augmenteddriver.integrations;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.salesforceiq.augmenteddriver.modules.PropertiesModule;
import com.salesforceiq.augmenteddriver.reporters.TeamCityReporter;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

/**
 * Integration for TeamCity, used to write the output so Team City understands.
 */
@Singleton
public class TeamCityIntegration implements Integration, ReportIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(TeamCityIntegration.class);

    private final boolean teamCityIntegration;

    @Inject
    public TeamCityIntegration(@Named(PropertiesModule.TEAM_CITY_INTEGRATION) String teamCityIntegration) {
        this.teamCityIntegration = Preconditions.checkNotNull(Boolean.valueOf(teamCityIntegration));
    }

    @Override
    public boolean isEnabled() {
        return teamCityIntegration;
    }

    @Override
    public void testPassed(boolean testPassed, String sessionId) {
        String result = testPassed ? "SUCCESS" : "FAILURE";
        LOG.info("TEST RESULT: " + result + " - Session: " + sessionId);
    }

    @Override
    public void jobName(String jobName, String sessionId) {
        LOG.info(String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", sessionId, jobName));
    }

    @Override
    public void buildName(String testName, String sessionId) {
        LOG.info("Test: " + testName + " Session: " + sessionId);
    }

    public RunListener getReporter(ByteArrayOutputStream outputStream, String nameAppender) {
        return new TeamCityReporter(outputStream, nameAppender);
    }

}

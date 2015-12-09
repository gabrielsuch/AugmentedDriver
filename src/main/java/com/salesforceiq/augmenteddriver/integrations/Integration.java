package com.salesforceiq.augmenteddriver.integrations;

/**
 * Markup interface for all integrations.
 */
public interface Integration {

    /**
     * Feature toggle
     * @return if the integration is currently enabled
     */
    boolean isEnabled();

    void testPassed(boolean testPassed, String sessionId);

    void jobName(String testName, String sessionId);

    void buildName(String testName, String sessionId);

}

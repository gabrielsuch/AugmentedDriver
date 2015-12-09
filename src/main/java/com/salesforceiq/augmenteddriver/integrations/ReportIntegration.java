package com.salesforceiq.augmenteddriver.integrations;

import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;


public interface ReportIntegration {

    boolean isEnabled();

    RunListener getReporter(ByteArrayOutputStream outputStream, String nameAppender);

}

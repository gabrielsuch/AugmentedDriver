package com.salesforceiq.augmenteddriver.runners;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.salesforceiq.augmenteddriver.integrations.ReportIntegration;
import com.salesforceiq.augmenteddriver.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Knows how to run one test.
 */
public class TestRunner implements Callable<AugmentedResult> {

    private static final Log LOG = LogFactory.getLog(TestRunner.class);

    private final Method test;
    private final ByteArrayOutputStream outputStream;
    private final String nameAppender;

    @Inject(optional = true)
    private Set<ReportIntegration> reports = new HashSet<>();

    @Inject
    public TestRunner(@Assisted Method test,
                      @Assisted String nameAppender,
                      ByteArrayOutputStream outputStream) {
        this.test = Preconditions.checkNotNull(test);
        this.nameAppender = Preconditions.checkNotNull(nameAppender);
        this.outputStream = Preconditions.checkNotNull(outputStream);
    }

    @Override
    public AugmentedResult call() throws Exception {
        JUnitCore jUnitCore = getJUnitCore();
        String testName = String.format("%s#%s", test.getDeclaringClass().getCanonicalName(), test.getName());
        long start = System.currentTimeMillis();

        try {
            LOG.info(String.format("STARTING Test %s", testName));
            Result result = jUnitCore.run(Request.method(test.getDeclaringClass(), test.getName()));
            LOG.info(String.format("FINSHED Test %s in %s", testName, Util.TO_PRETTY_FORNAT.apply(System.currentTimeMillis() - start)));

            return new AugmentedResult(result, outputStream);
        } finally {
            outputStream.close();
        }
    }

    private JUnitCore getJUnitCore() {
        JUnitCore jUnitCore = new JUnitCore();

        reports
                .stream()
                .filter(each -> each.isEnabled())
                .forEach(each -> jUnitCore.addListener(each.getReporter(outputStream, nameAppender)));

        return jUnitCore;
    }

}

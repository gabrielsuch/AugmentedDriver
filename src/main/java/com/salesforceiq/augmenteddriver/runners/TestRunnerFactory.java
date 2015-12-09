package com.salesforceiq.augmenteddriver.runners;

import java.lang.reflect.Method;

public interface TestRunnerFactory {

    TestRunner create(Method test, String nameAppender);

}
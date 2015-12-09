package com.salesforceiq.augmenteddriver.util.saucelabs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.salesforceiq.augmenteddriver.modules.SaucelabsModule;
import com.salesforceiq.augmenteddriver.util.Util;
import com.saucelabs.saucerest.SauceREST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.util.List;

public class SauceLabsUploader {
    private static final Logger LOG = LoggerFactory.getLogger(SauceLabsUploader.class);


    private static void checkArguments(SauceCommandLineArguments arguments) {
        Preconditions.checkNotNull(arguments.file(), "You should specify a file to upload");
        Preconditions.checkArgument(Files.exists(arguments.file()), "The file should exist " + arguments.file().getFileName());
    }

    public static void main(String[] args) throws Exception {
        SauceCommandLineArguments arguments = SauceCommandLineArguments.initialize(args);
        checkArguments(arguments);

        List<Module> modules = Lists.newArrayList(new SaucelabsModule());
        Injector injector = Guice.createInjector(modules);
        SauceREST sauceREST = injector.getInstance(SauceREST.class);
        long start = System.currentTimeMillis();
        LOG.info(String.format("Uploading file %s to SauceLabs, overwriting %s",
                arguments.file().getFileName().toString(), arguments.overwrite()));
        sauceREST.uploadFile(arguments.file().toFile(), arguments.file().getFileName().toString(), arguments.overwrite());
        LOG.info(String.format("Finishing uploading file %s in %s", arguments.file().getFileName().toString(),
                Util.TO_PRETTY_FORNAT.apply(System.currentTimeMillis() - start)));
    }
}

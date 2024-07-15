package com.lu.gademo.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deidentifier.arx.*;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.criteria.DistinctLDiversity;
import org.deidentifier.arx.criteria.EntropyLDiversity;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.deidentifier.arx.metric.Metric;
import org.springframework.stereotype.Component;


public class KAnonymityUtil {

    public static Data createData(final String dataset, String dir) throws IOException {

        Data data = Data.create(dir + File.separator + dataset + ".csv", StandardCharsets.UTF_8, ';');

        // Read generalization hierarchies
        FilenameFilter hierarchyFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(dataset + "_hierarchy_(.)+.csv");
            }
        };

        // Create definition
        File testDir = new File(dir + File.separator);
        File[] genHierFiles = testDir.listFiles(hierarchyFilter);
        Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
        if (genHierFiles != null) {
            for (File file : genHierFiles) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
                    String attributeName = matcher.group(1);
                    data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
                }
            }
        }
        return data;
    }

    public String lEntropyDiversity(final String dataset, String dir, String params, String attribute) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 0;
        switch (params) {
            case "1": {
                level = 4;
                break;
            }
            case "2": {
                level = 8;
                break;
            }
        }
        config.addPrivacyModel(new EntropyLDiversity(attribute, level));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lRecursiveCDiversity(final String dataset, String dir, String params, String attribute) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 0;
        switch (params) {
            case "1": {
                level = 4;
                break;
            }
            case "2": {
                level = 8;
                break;
            }
        }
        config.addPrivacyModel(new RecursiveCLDiversity(attribute, level, 2));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }
}

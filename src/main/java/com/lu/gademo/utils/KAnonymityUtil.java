package com.lu.gademo.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.deidentifier.arx.*;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.criteria.*;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.deidentifier.arx.metric.Metric;

@Slf4j
public class KAnonymityUtil {

    public static Data createData(final String dataset, String dir) throws IOException {

        Data data = Data.create(dir + File.separator + dataset + ".csv", StandardCharsets.UTF_8, ';');

        // Read generalization hierarchies
        FilenameFilter hierarchyFilter = (dir1, name) -> name.matches(dataset + "_hierarchy_(.)+.csv");

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

    public String kAnonymity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        // data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        int level = 2;
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

        int k = (level * length / 10) <= 1 ? 2 : level * length / 10;
        log.info("k = {}", k);
        config.addPrivacyModel(new KAnonymity(k));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lDistinctDiversity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        int level = 8;
        switch (params) {
            case "1": {
                level = 10;
                break;
            }
            case "2": {
                level = 16;
                break;
            }
        }

        int l = length * level / 10;
        log.info("l = {}", l);
        config.addPrivacyModel(new DistinctLDiversity(attribute, l));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lEntropyDiversity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 8;
        switch (params) {
            case "1": {
                level = 10;
                break;
            }
            case "2": {
                level = 16;
                break;
            }
        }
        double l = length * level / 10;
        log.info("l = {}", l);
        config.addPrivacyModel(new EntropyLDiversity(attribute, l));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lRecursiveCDiversity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 8;
        switch (params) {
            case "1": {
                level = 10;
                break;
            }
            case "2": {
                level = 16;
                break;
            }
        }
        double c = length * level / 10;
        log.info("c = {}, l = 2", c);
        config.addPrivacyModel(new RecursiveCLDiversity(attribute, c, 2));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String tCloseness(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 0.4;
        switch (params) {
            case "1": {
                level = 0.2;
                break;
            }
            case "2": {
                level = 0.1;
                break;
            }
        }
//        double t = length * (1 - level / 10.0);
        double t = level;
        log.info("t = {}", t);
        config.addPrivacyModel(new EqualDistanceTCloseness(attribute, t));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }
}

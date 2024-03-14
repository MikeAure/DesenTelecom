package com.lu.gademo.utils;

import java.io.IOException;

public interface AudioUtil {
    double cosineSimilarity(String file1, String file2) throws IOException;

    double euclideanDistance(String file1, String file2) throws IOException;
}

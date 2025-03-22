package com.lu.gademo.entity.ga;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlgorithmMapping {
    int id;
    String attributeName;
    int algorithmId;
}

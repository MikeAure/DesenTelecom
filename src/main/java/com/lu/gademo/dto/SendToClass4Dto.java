package com.lu.gademo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendToClass4Dto {
    @JsonProperty("data")
    Class4Data data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Class4Data {
        @JsonProperty("globalID")
        String globalID;
        int minDesenLevel;
        int maxCategoryLevel;
    }

}

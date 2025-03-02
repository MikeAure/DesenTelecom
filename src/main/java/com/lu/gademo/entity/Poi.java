package com.lu.gademo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Poi {
    String id;
    String attribute;
    String name;
    double lon;
    double lat;
}

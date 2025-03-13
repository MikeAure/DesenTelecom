package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.format.DateTimeFormatter;

public class BaseDateFormatter {
    @JsonIgnore
    final protected DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}

package com.lu.gademo.dto.officeComment;

import com.lu.gademo.dto.OFDMessage;
import com.lu.gademo.entity.LogCollectResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentLogAggregate {
    LogCollectResult logCollectResult;
    OFDMessage ofdMessage;
}

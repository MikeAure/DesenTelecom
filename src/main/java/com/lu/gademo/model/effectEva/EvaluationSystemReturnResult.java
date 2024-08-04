package com.lu.gademo.model.effectEva;

import com.lu.gademo.entity.effectEva.RecEvaReqReceipt;
import com.lu.gademo.entity.effectEva.RecEvaResult;
import com.lu.gademo.entity.effectEva.RecEvaResultInv;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class EvaluationSystemReturnResult {
    private RecEvaReqReceipt recEvaReqReceipt;
    private RecEvaResult recEvaResult;
    private RecEvaResultInv recEvaResultInv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EvaluationSystemReturnResult)) return false;
        EvaluationSystemReturnResult that = (EvaluationSystemReturnResult) o;
        return Objects.equals(getRecEvaReqReceipt(), that.getRecEvaReqReceipt()) && Objects.equals(getRecEvaResult(), that.getRecEvaResult()) && Objects.equals(getRecEvaResultInv(), that.getRecEvaResultInv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecEvaReqReceipt(), getRecEvaResult(), getRecEvaResultInv());
    }
}

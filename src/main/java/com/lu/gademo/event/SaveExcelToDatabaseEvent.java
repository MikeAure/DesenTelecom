package com.lu.gademo.event;

import com.lu.gademo.entity.FileStorageDetails;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 将Excel文件保存到数据库的事件
 */
@Getter
public class SaveExcelToDatabaseEvent extends ApplicationEvent {
    String entityClassName;
    FileStorageDetails fileStorageDetails;
    CompletableFuture<ResponseEntity<byte[]>> futureResult;
    ResponseEntity<byte[]> responseEntity;

    public SaveExcelToDatabaseEvent(Object source, String entityClassName,
                                    FileStorageDetails fileStorageDetails,
                                    CompletableFuture<ResponseEntity<byte[]>> futureResult,
                                    ResponseEntity<byte[]> responseEntity) {
        super(source);
        this.entityClassName = entityClassName;
        this.fileStorageDetails = fileStorageDetails;
        this.futureResult = futureResult;
        this.responseEntity = responseEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaveExcelToDatabaseEvent)) return false;
        SaveExcelToDatabaseEvent that = (SaveExcelToDatabaseEvent) o;
        return Objects.equals(getEntityClassName(), that.getEntityClassName()) && Objects.equals(getFileStorageDetails(), that.getFileStorageDetails()) && Objects.equals(getFutureResult(), that.getFutureResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityClassName(), getFileStorageDetails(), getFutureResult());
    }
}



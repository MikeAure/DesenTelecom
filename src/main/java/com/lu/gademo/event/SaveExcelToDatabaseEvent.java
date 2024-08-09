package com.lu.gademo.event;

import com.lu.gademo.entity.FileStorageDetails;
import javafx.application.Application;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

@Getter
public class SaveExcelToDatabaseEvent extends ApplicationEvent {
    String entityClassName;
    FileStorageDetails fileStorageDetails;

    public SaveExcelToDatabaseEvent(Object source, String entityClassName, FileStorageDetails fileStorageDetails) {
        super(source);
        this.entityClassName = entityClassName;
        this.fileStorageDetails = fileStorageDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaveExcelToDatabaseEvent)) return false;
        SaveExcelToDatabaseEvent that = (SaveExcelToDatabaseEvent) o;
        return Objects.equals(getEntityClassName(), that.getEntityClassName()) && Objects.equals(getFileStorageDetails(), that.getFileStorageDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntityClassName(), getFileStorageDetails());
    }
}



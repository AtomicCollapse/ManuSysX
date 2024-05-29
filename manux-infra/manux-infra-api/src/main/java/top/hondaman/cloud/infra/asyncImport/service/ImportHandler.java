package top.hondaman.cloud.infra.asyncImport.service;

public interface ImportHandler {

    void checkData();

    void persistData();
}

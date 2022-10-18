package com.example.ownablebackend.services.file;

import com.example.ownablebackend.domain.Files;

public interface FileService {

    void saveFile(Files files);

    byte[] loadFile(String hash);
}

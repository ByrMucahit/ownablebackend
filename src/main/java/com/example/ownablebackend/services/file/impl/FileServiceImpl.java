package com.example.ownablebackend.services.file.impl;

import com.example.ownablebackend.config.IPFSConfiguration;
import com.example.ownablebackend.domain.Files;
import com.example.ownablebackend.repositories.FilesRepository;
import com.example.ownablebackend.services.file.FileService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {


    IPFSConfiguration ipfsConfiguration;

    private final FilesRepository filesRepository;

    public FileServiceImpl(IPFSConfiguration ipfsConfig, FilesRepository filesRepository) {
        this.ipfsConfiguration = ipfsConfig;
        this.filesRepository = filesRepository;
    }

    @Override
    public byte[] loadFile(String hash) {

        try {

            Multihash filePointer = Multihash.fromBase58(hash);
            return ipfsConfiguration.ipfsConfig().cat(filePointer);
        } catch (Exception e) {
            throw new RuntimeException("Error whilist communicating with the IPFS node", e);
        }
    }

    @Override
    public void saveFile(Files files) {
        this.filesRepository.save(files);
    }
}

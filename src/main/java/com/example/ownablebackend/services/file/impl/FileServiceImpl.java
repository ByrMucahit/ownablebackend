package com.example.ownablebackend.services.file.impl;

import com.example.ownablebackend.config.IPFSConfig;
import com.example.ownablebackend.services.file.FileService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class FileServiceImpl implements FileService {


    IPFSConfig ipfsConfig;

    public FileServiceImpl(IPFSConfig ipfsConfig) {
        this.ipfsConfig = ipfsConfig;
    }

    @Override
    public byte[] loadFile(String hash) {

        try {
            IPFS ipfs =new IPFS("/ip4/127.0.0.1/tcp/5001");

            Multihash filePointer = Multihash.fromBase58(hash);
            return ipfs.cat(filePointer);
        } catch (Exception e) {
            throw new RuntimeException("Error whilist communicating with the IPFS node", e);
        }
    }

    @Override
    public String saveFile(MultipartFile file) {

        try {
            InputStream stream = new ByteArrayInputStream(file.getBytes());
            NamedStreamable.InputStreamWrapper inputStreamWrapper = new NamedStreamable.InputStreamWrapper(stream);

            IPFS ipfs =new IPFS("/ip4/127.0.0.1/tcp/5001");

            MerkleNode merkleNode = ipfs.add(inputStreamWrapper).get(0);

            return merkleNode.hash.toBase58();

        } catch (Exception e) {
            throw new RuntimeException("Error whilist communicating with the IPFS node", e);
        }
    }
}

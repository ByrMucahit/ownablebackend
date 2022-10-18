package com.example.ownablebackend.client.ipfs;

import com.example.ownablebackend.config.IPFSConfiguration;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class IpfsClient {

    private final IPFSConfiguration ipfsConfiguration;

    public byte[] loadFile(String hash) {
        try {

            Multihash filePointer = Multihash.fromBase58(hash);
            return ipfsConfiguration.ipfsConfig().cat(filePointer);
        } catch (Exception e) {
            throw new RuntimeException("Error whilist communicating with the IPFS node", e);
        }
    }

    public String saveFile(MultipartFile file) {

        try {
            InputStream stream = new ByteArrayInputStream(file.getBytes());
            NamedStreamable.InputStreamWrapper inputStreamWrapper = new NamedStreamable.InputStreamWrapper(stream);

            MerkleNode merkleNode = ipfsConfiguration.ipfsConfig().add(inputStreamWrapper).get(0);

            return merkleNode.hash.toBase58();

        } catch (Exception e) {
            throw new RuntimeException(String.format("The has is not found %d", file.getSize()));
        }
    }
}

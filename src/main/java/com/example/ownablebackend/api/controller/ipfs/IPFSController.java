package com.example.ownablebackend.api.controller.ipfs;

import com.example.ownablebackend.client.ipfs.IpfsClient;
import com.example.ownablebackend.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/nft")
public class IPFSController {

    @Autowired
    private IpfsClient ipfsClient;

    @PostMapping(value = "upload")
    public String saveFile(@RequestParam("file")MultipartFile multipartFile) {
        return ipfsClient.saveFile(multipartFile);
    }

    @GetMapping(value = "file/{hash}")
    public ResponseEntity<byte[]> loadFile(@PathVariable("hash") String hash) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", MediaType.ALL_VALUE);
        byte[] bytes = ipfsClient.loadFile(hash);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(bytes);
    }
}

package com.example.ownablebackend.config;

import io.ipfs.api.IPFS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IPFSConfiguration {

    @Value("${ipfs.config}")
    String ipfsConfig;

    IPFS ipfs;

    @Bean
    public IPFS ipfsConfig() throws IOException {

         this.ipfs = new IPFS(ipfsConfig);

         return ipfs;

    }
}

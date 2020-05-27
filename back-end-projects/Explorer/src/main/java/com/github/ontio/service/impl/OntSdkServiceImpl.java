package com.github.ontio.service.impl;

import com.github.ontio.OntSdk;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.network.exception.ConnectorException;
import com.github.ontio.sdk.exception.SDKException;
import com.github.ontio.service.IOntSdkService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
@Service
public class OntSdkServiceImpl implements IOntSdkService {

    private OntSdk sdk;

    private int nodeCount;

    private AtomicInteger currentNodeIndex;

    private ParamsConfig paramsConfig;

    @Autowired
    public OntSdkServiceImpl(ParamsConfig paramsConfig) {
        this.paramsConfig = paramsConfig;
        this.nodeCount = paramsConfig.getHosts().size();
        this.currentNodeIndex = new AtomicInteger(0);
        this.sdk = OntSdk.getInstance();
        try {
            sdk.getRestful();
        } catch (SDKException e) {
            sdk.setRestful(paramsConfig.getHosts().get(this.currentNodeIndex.get()));
        }
    }

    @Override
    public int getGovernanceView() {
        for (int i = 0; i < 4; i++) {
            try {
                return sdk.nativevm().governance().getGovernanceView().view;
            } catch (ConnectorException | IOException | SDKException e) {
                log.warn("Getting governance view failed: {}", e.getMessage());
                switchSyncNode();
            }
        }
        return 0;
    }

    private void switchSyncNode() {
        if (currentNodeIndex.get() >= nodeCount) {
            currentNodeIndex.set(0);
        }
        sdk.setRestful(paramsConfig.getHosts().get(currentNodeIndex.getAndAdd(1)));
        try {
            log.warn("Using node: {}", sdk.getRestful().toString());
        } catch (SDKException e) {
            log.warn("Getting REST URL failed: {}", e.getMessage());
        }
    }

}

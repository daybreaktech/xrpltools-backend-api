package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.Sample;
import com.daybreaktech.xrpltools.backendapi.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    public List<Sample> getAllSamples() {
        return (List<Sample>) sampleRepository.findAll();
    }

    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id).get();
    }

    public Sample saveSample(Sample sample) {
        return sampleRepository.save(sample);
    }

}

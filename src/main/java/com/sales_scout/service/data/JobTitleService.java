package com.sales_scout.service.data;


import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.repository.data.JobTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobTitleService {

    @Autowired
    private JobTitleRepository jobTitleRepository;

    public List<JobTitle> getAllJobTitles() {
        return jobTitleRepository.findAll();
    }

    public Optional<JobTitle> getJobTitleById(Long id) {
        return jobTitleRepository.findById(id);
    }

    public JobTitle createJobTitle(JobTitle jobTitle) {
        return jobTitleRepository.save(jobTitle);
    }

    public JobTitle updateJobTitle(Long id, JobTitle jobTitleDetails) {
        Optional<JobTitle> optionalJobTitle = jobTitleRepository.findById(id);
        if (optionalJobTitle.isPresent()) {
            JobTitle jobTitle = optionalJobTitle.get();
            jobTitle.setName(jobTitleDetails.getName());
            return jobTitleRepository.save(jobTitle);
        }
        return null; // or throw an exception
    }

    public void deleteJobTitle(Long id) {
        jobTitleRepository.deleteById(id);
    }
}


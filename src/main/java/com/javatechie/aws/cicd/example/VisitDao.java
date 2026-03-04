package com.javatechie.aws.cicd.example;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class VisitDao {
    // NEW: introduce a repository for the Visit entity
    public List<Visit> getVisits(String fromDate) {
        // NEW: implement filtering logic at the repository or database query level
        if (fromDate != null) {
            return Stream.of(
                    new Visit(1, "Pet1", "2024-01-02"),
                    new Visit(2, "Pet2", "2024-01-03")
            ).filter(visit -> visit.getVisitDate().compareTo(fromDate) > 0).collect(Collectors.toList());
        } else {
            return Stream.of(
                    new Visit(1, "Pet1", "2024-01-01"),
                    new Visit(2, "Pet2", "2024-01-02"),
                    new Visit(3, "Pet3", "2024-01-03")
            ).collect(Collectors.toList());
        }
    }
}
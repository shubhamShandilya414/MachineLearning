package com.javatechie.aws.cicd.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testFetchVisits() {
        // NEW: test the endpoint with and without the 'fromDate' parameter
        ResponseEntity<List<Visit>> response = testRestTemplate.getForEntity("/visits", List.class);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().size() == 3;

        response = testRestTemplate.getForEntity("/visits?fromDate=2024-01-02", List.class);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().size() == 1;
    }
}
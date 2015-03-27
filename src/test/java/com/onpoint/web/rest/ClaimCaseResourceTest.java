package com.onpoint.web.rest;

import com.onpoint.Application;
import com.onpoint.domain.ClaimCase;
import com.onpoint.repository.ClaimCaseRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClaimCaseResource REST controller.
 *
 * @see ClaimCaseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClaimCaseResourceTest {

    private static final String DEFAULT_LEGACY_CASE_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_LEGACY_CASE_NUMBER = "UPDATED_TEXT";

    @Inject
    private ClaimCaseRepository claimCaseRepository;

    private MockMvc restClaimCaseMockMvc;

    private ClaimCase claimCase;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClaimCaseResource claimCaseResource = new ClaimCaseResource();
        ReflectionTestUtils.setField(claimCaseResource, "claimCaseRepository", claimCaseRepository);
        this.restClaimCaseMockMvc = MockMvcBuilders.standaloneSetup(claimCaseResource).build();
    }

    @Before
    public void initTest() {
        claimCase = new ClaimCase();
        claimCase.setLegacyCaseNumber(DEFAULT_LEGACY_CASE_NUMBER);
    }

    @Test
    @Transactional
    public void createClaimCase() throws Exception {
        // Validate the database is empty
        assertThat(claimCaseRepository.findAll()).hasSize(0);

        // Create the ClaimCase
        restClaimCaseMockMvc.perform(post("/api/claimCases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claimCase)))
                .andExpect(status().isCreated());

        // Validate the ClaimCase in the database
        List<ClaimCase> claimCases = claimCaseRepository.findAll();
        assertThat(claimCases).hasSize(1);
        ClaimCase testClaimCase = claimCases.iterator().next();
        assertThat(testClaimCase.getLegacyCaseNumber()).isEqualTo(DEFAULT_LEGACY_CASE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllClaimCases() throws Exception {
        // Initialize the database
        claimCaseRepository.saveAndFlush(claimCase);

        // Get all the claimCases
        restClaimCaseMockMvc.perform(get("/api/claimCases"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(claimCase.getId().intValue()))
                .andExpect(jsonPath("$.[0].legacyCaseNumber").value(DEFAULT_LEGACY_CASE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getClaimCase() throws Exception {
        // Initialize the database
        claimCaseRepository.saveAndFlush(claimCase);

        // Get the claimCase
        restClaimCaseMockMvc.perform(get("/api/claimCases/{id}", claimCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(claimCase.getId().intValue()))
            .andExpect(jsonPath("$.legacyCaseNumber").value(DEFAULT_LEGACY_CASE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClaimCase() throws Exception {
        // Get the claimCase
        restClaimCaseMockMvc.perform(get("/api/claimCases/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClaimCase() throws Exception {
        // Initialize the database
        claimCaseRepository.saveAndFlush(claimCase);

        // Update the claimCase
        claimCase.setLegacyCaseNumber(UPDATED_LEGACY_CASE_NUMBER);
        restClaimCaseMockMvc.perform(put("/api/claimCases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(claimCase)))
                .andExpect(status().isOk());

        // Validate the ClaimCase in the database
        List<ClaimCase> claimCases = claimCaseRepository.findAll();
        assertThat(claimCases).hasSize(1);
        ClaimCase testClaimCase = claimCases.iterator().next();
        assertThat(testClaimCase.getLegacyCaseNumber()).isEqualTo(UPDATED_LEGACY_CASE_NUMBER);
    }

    @Test
    @Transactional
    public void deleteClaimCase() throws Exception {
        // Initialize the database
        claimCaseRepository.saveAndFlush(claimCase);

        // Get the claimCase
        restClaimCaseMockMvc.perform(delete("/api/claimCases/{id}", claimCase.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ClaimCase> claimCases = claimCaseRepository.findAll();
        assertThat(claimCases).hasSize(0);
    }
}

package com.onpoint.web.rest;

import com.onpoint.Application;
import com.onpoint.domain.Employer;
import com.onpoint.repository.EmployerRepository;

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
 * Test class for the EmployerResource REST controller.
 *
 * @see EmployerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployerResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_CONTACT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_CONTACT_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_CONTACT_PHONE = "SAMPLE_TEXT";
    private static final String UPDATED_CONTACT_PHONE = "UPDATED_TEXT";

    @Inject
    private EmployerRepository employerRepository;

    private MockMvc restEmployerMockMvc;

    private Employer employer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployerResource employerResource = new EmployerResource();
        ReflectionTestUtils.setField(employerResource, "employerRepository", employerRepository);
        this.restEmployerMockMvc = MockMvcBuilders.standaloneSetup(employerResource).build();
    }

    @Before
    public void initTest() {
        employer = new Employer();
        employer.setName(DEFAULT_NAME);
        employer.setAddress(DEFAULT_ADDRESS);
        employer.setContactName(DEFAULT_CONTACT_NAME);
        employer.setContactPhone(DEFAULT_CONTACT_PHONE);
    }

    @Test
    @Transactional
    public void createEmployer() throws Exception {
        // Validate the database is empty
        assertThat(employerRepository.findAll()).hasSize(0);

        // Create the Employer
        restEmployerMockMvc.perform(post("/api/employers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employer)))
                .andExpect(status().isCreated());

        // Validate the Employer in the database
        List<Employer> employers = employerRepository.findAll();
        assertThat(employers).hasSize(1);
        Employer testEmployer = employers.iterator().next();
        assertThat(testEmployer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployer.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testEmployer.getContactPhone()).isEqualTo(DEFAULT_CONTACT_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployers() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get all the employers
        restEmployerMockMvc.perform(get("/api/employers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(employer.getId().intValue()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].address").value(DEFAULT_ADDRESS.toString()))
                .andExpect(jsonPath("$.[0].contactName").value(DEFAULT_CONTACT_NAME.toString()))
                .andExpect(jsonPath("$.[0].contactPhone").value(DEFAULT_CONTACT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME.toString()))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployer() throws Exception {
        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Update the employer
        employer.setName(UPDATED_NAME);
        employer.setAddress(UPDATED_ADDRESS);
        employer.setContactName(UPDATED_CONTACT_NAME);
        employer.setContactPhone(UPDATED_CONTACT_PHONE);
        restEmployerMockMvc.perform(put("/api/employers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employer)))
                .andExpect(status().isOk());

        // Validate the Employer in the database
        List<Employer> employers = employerRepository.findAll();
        assertThat(employers).hasSize(1);
        Employer testEmployer = employers.iterator().next();
        assertThat(testEmployer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployer.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testEmployer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    public void deleteEmployer() throws Exception {
        // Initialize the database
        employerRepository.saveAndFlush(employer);

        // Get the employer
        restEmployerMockMvc.perform(delete("/api/employers/{id}", employer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employer> employers = employerRepository.findAll();
        assertThat(employers).hasSize(0);
    }
}

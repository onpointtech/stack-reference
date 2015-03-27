package com.onpoint.web.rest;

import com.onpoint.Application;
import com.onpoint.domain.Hearing;
import com.onpoint.repository.HearingRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HearingResource REST controller.
 *
 * @see HearingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HearingResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_HEARING_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_HEARING_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_HEARING_DATE_STR = dateTimeFormatter.print(DEFAULT_HEARING_DATE);
    private static final String DEFAULT_LOCATION = "SAMPLE_TEXT";
    private static final String UPDATED_LOCATION = "UPDATED_TEXT";

    @Inject
    private HearingRepository hearingRepository;

    private MockMvc restHearingMockMvc;

    private Hearing hearing;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HearingResource hearingResource = new HearingResource();
        ReflectionTestUtils.setField(hearingResource, "hearingRepository", hearingRepository);
        this.restHearingMockMvc = MockMvcBuilders.standaloneSetup(hearingResource).build();
    }

    @Before
    public void initTest() {
        hearing = new Hearing();
        hearing.setHearingDate(DEFAULT_HEARING_DATE);
        hearing.setLocation(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createHearing() throws Exception {
        // Validate the database is empty
        assertThat(hearingRepository.findAll()).hasSize(0);

        // Create the Hearing
        restHearingMockMvc.perform(post("/api/hearings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hearing)))
                .andExpect(status().isCreated());

        // Validate the Hearing in the database
        List<Hearing> hearings = hearingRepository.findAll();
        assertThat(hearings).hasSize(1);
        Hearing testHearing = hearings.iterator().next();
        assertThat(testHearing.getHearingDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_HEARING_DATE);
        assertThat(testHearing.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void getAllHearings() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

        // Get all the hearings
        restHearingMockMvc.perform(get("/api/hearings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(hearing.getId().intValue()))
                .andExpect(jsonPath("$.[0].hearingDate").value(DEFAULT_HEARING_DATE_STR))
                .andExpect(jsonPath("$.[0].location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getHearing() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

        // Get the hearing
        restHearingMockMvc.perform(get("/api/hearings/{id}", hearing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hearing.getId().intValue()))
            .andExpect(jsonPath("$.hearingDate").value(DEFAULT_HEARING_DATE_STR))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHearing() throws Exception {
        // Get the hearing
        restHearingMockMvc.perform(get("/api/hearings/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHearing() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

        // Update the hearing
        hearing.setHearingDate(UPDATED_HEARING_DATE);
        hearing.setLocation(UPDATED_LOCATION);
        restHearingMockMvc.perform(put("/api/hearings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hearing)))
                .andExpect(status().isOk());

        // Validate the Hearing in the database
        List<Hearing> hearings = hearingRepository.findAll();
        assertThat(hearings).hasSize(1);
        Hearing testHearing = hearings.iterator().next();
        assertThat(testHearing.getHearingDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_HEARING_DATE);
        assertThat(testHearing.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void deleteHearing() throws Exception {
        // Initialize the database
        hearingRepository.saveAndFlush(hearing);

        // Get the hearing
        restHearingMockMvc.perform(delete("/api/hearings/{id}", hearing.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hearing> hearings = hearingRepository.findAll();
        assertThat(hearings).hasSize(0);
    }
}

package com.onpoint.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.onpoint.domain.Hearing;
import com.onpoint.repository.HearingRepository;
import com.onpoint.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Hearing.
 */
@RestController
@RequestMapping("/api")
public class HearingResource {

    private final Logger log = LoggerFactory.getLogger(HearingResource.class);

    @Inject
    private HearingRepository hearingRepository;

    /**
     * POST  /hearings -> Create a new hearing.
     */
    @RequestMapping(value = "/hearings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Hearing hearing) throws URISyntaxException {
        log.debug("REST request to save Hearing : {}", hearing);
        if (hearing.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hearing cannot already have an ID").build();
        }
        hearingRepository.save(hearing);
        return ResponseEntity.created(new URI("/api/hearings/" + hearing.getId())).build();
    }

    /**
     * PUT  /hearings -> Updates an existing hearing.
     */
    @RequestMapping(value = "/hearings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Hearing hearing) throws URISyntaxException {
        log.debug("REST request to update Hearing : {}", hearing);
        if (hearing.getId() == null) {
            return create(hearing);
        }
        hearingRepository.save(hearing);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /hearings -> get all the hearings.
     */
    @RequestMapping(value = "/hearings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Hearing>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Hearing> page = hearingRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hearings", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hearings/:id -> get the "id" hearing.
     */
    @RequestMapping(value = "/hearings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Hearing> get(@PathVariable Long id) {
        log.debug("REST request to get Hearing : {}", id);
        return Optional.ofNullable(hearingRepository.findOne(id))
            .map(hearing -> new ResponseEntity<>(
                hearing,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hearings/:id -> delete the "id" hearing.
     */
    @RequestMapping(value = "/hearings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Hearing : {}", id);
        hearingRepository.delete(id);
    }
}

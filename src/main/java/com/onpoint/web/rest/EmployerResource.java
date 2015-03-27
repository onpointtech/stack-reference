package com.onpoint.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.onpoint.domain.Employer;
import com.onpoint.repository.EmployerRepository;
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
 * REST controller for managing Employer.
 */
@RestController
@RequestMapping("/api")
public class EmployerResource {

    private final Logger log = LoggerFactory.getLogger(EmployerResource.class);

    @Inject
    private EmployerRepository employerRepository;

    /**
     * POST  /employers -> Create a new employer.
     */
    @RequestMapping(value = "/employers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to save Employer : {}", employer);
        if (employer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new employer cannot already have an ID").build();
        }
        employerRepository.save(employer);
        return ResponseEntity.created(new URI("/api/employers/" + employer.getId())).build();
    }

    /**
     * PUT  /employers -> Updates an existing employer.
     */
    @RequestMapping(value = "/employers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to update Employer : {}", employer);
        if (employer.getId() == null) {
            return create(employer);
        }
        employerRepository.save(employer);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /employers -> get all the employers.
     */
    @RequestMapping(value = "/employers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Employer>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Employer> page = employerRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employers/:id -> get the "id" employer.
     */
    @RequestMapping(value = "/employers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Employer> get(@PathVariable Long id) {
        log.debug("REST request to get Employer : {}", id);
        return Optional.ofNullable(employerRepository.findOne(id))
            .map(employer -> new ResponseEntity<>(
                employer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employers/:id -> delete the "id" employer.
     */
    @RequestMapping(value = "/employers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Employer : {}", id);
        employerRepository.delete(id);
    }
}

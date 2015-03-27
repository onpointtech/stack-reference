package com.onpoint.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.onpoint.domain.ClaimCase;
import com.onpoint.repository.ClaimCaseRepository;
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
 * REST controller for managing ClaimCase.
 */
@RestController
@RequestMapping("/api")
public class ClaimCaseResource {

    private final Logger log = LoggerFactory.getLogger(ClaimCaseResource.class);

    @Inject
    private ClaimCaseRepository claimCaseRepository;

    /**
     * POST  /claimCases -> Create a new claimCase.
     */
    @RequestMapping(value = "/claimCases",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ClaimCase claimCase) throws URISyntaxException {
        log.debug("REST request to save ClaimCase : {}", claimCase);
        if (claimCase.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new claimCase cannot already have an ID").build();
        }
        claimCaseRepository.save(claimCase);
        return ResponseEntity.created(new URI("/api/claimCases/" + claimCase.getId())).build();
    }

    /**
     * PUT  /claimCases -> Updates an existing claimCase.
     */
    @RequestMapping(value = "/claimCases",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ClaimCase claimCase) throws URISyntaxException {
        log.debug("REST request to update ClaimCase : {}", claimCase);
        if (claimCase.getId() == null) {
            return create(claimCase);
        }
        claimCaseRepository.save(claimCase);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /claimCases -> get all the claimCases.
     */
    @RequestMapping(value = "/claimCases",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClaimCase>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ClaimCase> page = claimCaseRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/claimCases", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /claimCases/:id -> get the "id" claimCase.
     */
    @RequestMapping(value = "/claimCases/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClaimCase> get(@PathVariable Long id) {
        log.debug("REST request to get ClaimCase : {}", id);
        return Optional.ofNullable(claimCaseRepository.findOne(id))
            .map(claimCase -> new ResponseEntity<>(
                claimCase,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /claimCases/:id -> delete the "id" claimCase.
     */
    @RequestMapping(value = "/claimCases/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ClaimCase : {}", id);
        claimCaseRepository.delete(id);
    }
}

package io.sumac.demo.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.MediaTypes.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.MediaType.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value="/api/v1/members",produces = APPLICATION_JSON_VALUE)
@Slf4j
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class MemberController {

    private final MemberDataService members;

    @Autowired
    public MemberController(MemberDataService members) {
        this.members = members;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    public ResponseEntity<?> create(@Valid @RequestBody MemberData memberData) {
        var member = members.create(memberData);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(member.getId())
                .toUri();
        EntityModel<Member> result = EntityModel.of(member, linkTo(MemberController.class).slash(member.getId()).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(result);
    }

    @GetMapping(value="/{id}", produces = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        EntityModel<Member> result = EntityModel.of(members.getById(id).orElseThrow(ApplicationException::notFound), linkTo(MemberController.class).slash(id).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping(value="/{id}", consumes = APPLICATION_JSON_VALUE, produces = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody Member member) {
        EntityModel<Member> result = EntityModel.of(members.update(id, member), linkTo(MemberController.class).slash(id).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        return ResponseEntity.status(members.delete(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(produces = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    public ResponseEntity<?> findByAll() {
        final var membersResults = members.findByAll();
        membersResults.forEach(member ->
            member.add(linkTo(MemberController.class).slash(member.getId()).withSelfRel())
        );
        CollectionModel<Member> result = CollectionModel.of(membersResults, linkTo(MemberController.class).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

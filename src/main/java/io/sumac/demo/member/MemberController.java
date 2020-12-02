package io.sumac.demo.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
@Slf4j
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class MemberController {

    private final MemberDataService members;

    @Autowired
    public MemberController(MemberDataService members) {
        this.members = members;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MemberData member) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(members.create(member))
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        return members.getById(id).map(result -> ResponseEntity.status(HttpStatus.OK).body(result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @Valid @RequestBody Member member) {
        return ResponseEntity.status(members.update(id, member) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        return ResponseEntity.status(members.delete(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<?> findByAll() {
        final var membersResults = members.findByAll();
        membersResults.forEach(member ->
            member.add(linkTo(MemberController.class).slash(member.getId()).withSelfRel())
        );
        CollectionModel<Member> result = CollectionModel.of(membersResults, linkTo(MemberController.class).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

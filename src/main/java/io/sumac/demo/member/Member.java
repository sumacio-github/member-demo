package io.sumac.demo.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "members")
public class Member extends RepresentationModel<Member> {
    @NotNull(message="'id' is required")
    private Integer id;
    @NotBlank(message="'name' is required")
    private String name;
    private String aliasName;
    @NotBlank(message="'login' is required")
    private String login;
    @NotNull(message="'rating' is required")
    @Range(min=0, max=5, message="Rating must be between 0 and 5 inclusive")
    private Float rating;
    @NotBlank(message="'status' is required")
    @Pattern(regexp="^(ACTIVE|CANCELLED)$", message="'status' must be 'ACTIVE' or 'CANCELLED'")
    private String status;
    private LocalDateTime joined;
}

package io.sumac.demo.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.Optional;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberData {
    @NotBlank(message="'name' is required")
    private String name;
    private String aliasName;
    @NotBlank(message="'login' is required")
    private String login;
}

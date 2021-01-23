package io.sumac.demo.member;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "application.sql.member")
public class MemberQueries {
    private final String insert;
    private final String update;
    private final String delete;
    private final String select;
    private final String selectAll;
}

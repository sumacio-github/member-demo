package io.sumac.demo.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Optional;

@Repository
public class MemberRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (row, num) -> new Member(
            row.getInt("id"),
            row.getString("name"),
            row.getString("alias_name"),
            row.getString("login"),
            row.getFloat("rating"),
            row.getString("status"),
            row.getTimestamp("joined").toLocalDateTime()
    );

    public int create(MemberData member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("insert into members (name, alias_name, login) values (?, ?, ?)");
            ps.setString(1, member.getName());
            ps.setString(2, member.getAliasName());
            ps.setString(3, member.getLogin());
            return ps;
        }, keyHolder);

        return (int) keyHolder.getKey();
    }

    public Optional<Member> getById(int id) {
        try {
            return Optional.of(jdbc.queryForObject("select id, name, alias_name, login, rating, status, joined from members where id=?", MEMBER_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean update(int id, Member member) {
        return jdbc.update("update members set name=?, alias_name=?, login=?, rating=?, status=?, joined=? where id=?",
                member.getName(), member.getAliasName(), member.getLogin(), member.getRating(), member.getStatus(), member.getJoined(), id) > 0;
    }

    public boolean delete(int id) {
        return jdbc.update("delete from members where id=?", id) > 0;
    }

    public Collection<Member> findByAll() {
        return jdbc.query("select id, name, alias_name, login, rating, status, joined from members", MEMBER_ROW_MAPPER);
    }

}

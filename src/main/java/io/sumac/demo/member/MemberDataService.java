package io.sumac.demo.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class MemberDataService {

    private final MemberRepository repository;

    @Autowired
    public MemberDataService(MemberRepository repository) {
        this.repository = repository;
    }

    public int create(MemberData member) {
        if (isLoginExists(member.getLogin())) {
            throw ApplicationException.duplicateLogin(member.getLogin());
        }
        return repository.create(member);
    }

    public Optional<Member> getById(int id) {
        return repository.getById(id);
    }

    public Collection<Member> findByAll() {
        return repository.findByAll();
    }

    public boolean update(int id, Member member) {
        var storedMember = repository.getById(id);
        if (storedMember.isEmpty()) {
            return false;
        }
        if (!storedMember.get().getLogin().equals(member.getLogin()) && isLoginExists(member.getLogin())) {
            throw ApplicationException.duplicateLogin(member.getLogin());
        }
        return repository.update(id, member);
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    private boolean isLoginExists(String login) {
        return repository.findByAll().stream().filter(member -> member.getLogin().equals(login)).count() > 0;
    }
}

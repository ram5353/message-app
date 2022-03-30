package com.example.messageapp.emailslist;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface EmailsListRepository extends CassandraRepository<EmailsList, EmailsListPrimaryKey>  {
    List<EmailsList> findAllByKey_UserIdAndKey_Label(String userId, String label);
}

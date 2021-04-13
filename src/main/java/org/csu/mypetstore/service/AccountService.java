package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Account;

public interface AccountService {

    Account getAccount(String username);

    Account getAccount(Account account);

    void insertAccount(Account account);

    void updateAccount(Account account);
}

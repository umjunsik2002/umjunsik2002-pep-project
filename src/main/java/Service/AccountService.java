package Service;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Message> getMessageForAccount(int accountId) {
        return accountDAO.getMessageForAccount(accountId);
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }

    public Account register(Account account) {
        return accountDAO.register(account);
    }
}

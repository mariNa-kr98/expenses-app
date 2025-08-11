package gr.aueb.cf.expensesApp.service;

import gr.aueb.cf.expensesApp.core.enums.ErrorCode;
import gr.aueb.cf.expensesApp.core.exceptions.AppException;
import gr.aueb.cf.expensesApp.model.User;

public interface IUserService {

    User saveUser(User user) throws AppException;
    User deleteUser(Long userId) throws AppException;

}

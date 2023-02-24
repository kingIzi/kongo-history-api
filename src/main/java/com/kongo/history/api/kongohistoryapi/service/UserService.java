package com.kongo.history.api.kongohistoryapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kongo.history.api.kongohistoryapi.model.entity.User;
import com.kongo.history.api.kongohistoryapi.model.form.RegisterForm;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.repository.UserRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addNewUser(final RegisterForm registerForm, final LoginResponse loginResponse) {
        final var user = new User(registerForm.getFullName(), registerForm.getPhoneNumber(),
                loginResponse.getEmail(), loginResponse.getLocalId());
        try {
            final var saved = this.userRepository.save(user)
                    .orElseThrow(
                            AppUtilities.supplyException("Failed to upload user", AppConst._KEY_CODE_INTERNAL_ERROR));
        } catch (ValueDataException e) {
            e.printStackTrace();
        }
    }

    public HttpDataResponse<List<User>> getUsersList(final Integer limit) {
        final var httpDataResponse = new HttpDataResponse<List<User>>();
        try {
            final var users = this.userRepository.searchByCriteria(limit).orElseThrow(
                    AppUtilities.supplyException("Failed to get users", AppConst._KEY_CODE_INTERNAL_ERROR));
            httpDataResponse.setResponse(users);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<User> findUser(final String userId) {
        final var httpDataResponse = new HttpDataResponse<User>();
        try {
            final var user = this.userRepository.get(userId)
                    .orElseThrow(AppUtilities.supplyException("Failed to get user", AppConst._KEY_CODE_PARAMS_ERROR));
            httpDataResponse.setResponse(user);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }
}

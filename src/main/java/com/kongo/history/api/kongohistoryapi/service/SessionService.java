package com.kongo.history.api.kongohistoryapi.service;

import com.kongo.history.api.kongohistoryapi.model.entity.User;
import com.kongo.history.api.kongohistoryapi.model.form.LoginForm;
import com.kongo.history.api.kongohistoryapi.model.form.RegisterUserForm;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.repository.UserRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;

//spring
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.validation.BindingResult;

@Service
public class SessionService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private static final String SIGN_IN_WITH_EMAIL_PASSWORD = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyCE4py7YtsILZHix12wW-LroXgth7jvDNQ";
    private static final String SIGN_UP_WITH_EMAIL_PASSWORD = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyCE4py7YtsILZHix12wW-LroXgth7jvDNQ";

    public HttpDataResponse<LoginResponse> login(final LoginForm loginForm, final BindingResult bindingResult) {
        final var httpDataResponse = new HttpDataResponse<LoginResponse>();
        try {
            AppUtilities.controlForm(bindingResult);
            final var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            final var request = new HttpEntity<>(loginForm, headers);
            final var response = this.restTemplate.postForEntity(SessionService.SIGN_IN_WITH_EMAIL_PASSWORD, request,
                    LoginResponse.class);
            httpDataResponse.setResponse(response.getBody());
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e.getMessage(), e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<LoginResponse> register(final RegisterUserForm registerForm, BindingResult bindingResult) {
        final var httpDataResponse = new HttpDataResponse<LoginResponse>();
        try {
            AppUtilities.controlForm(bindingResult);
            final var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            final var request = new HttpEntity<>(registerForm, headers);
            final var response = this.restTemplate.postForEntity(SessionService.SIGN_UP_WITH_EMAIL_PASSWORD, request,
                    LoginResponse.class);
            registerForm.setPassword("");
            final var body = response.getBody();
            httpDataResponse.setResponse(body);
            final var signUp = new Thread(new Runnable() {
                @Override
                public void run() {
                    userService.addNewUser(registerForm, body);
                }
            });
            signUp.start();
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    // public HttpDataResponse<LoginResponse> register(final RegisterAdminForm
    // registerForm) {
    // final var httpDataResponse = new HttpDataResponse<LoginResponse>();
    // try {
    // final var headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_JSON);
    // final var request = new HttpEntity<>(registerForm, headers);
    // final var response =
    // this.restTemplate.postForEntity(SessionService.SIGN_UP_WITH_EMAIL_PASSWORD,
    // request,
    // LoginResponse.class);
    // registerForm.setPassword("");
    // final var body = response.getBody();
    // httpDataResponse.setResponse(body);
    // final var signUp = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // userService.addNewAdmin(registerForm, body);
    // }
    // });
    // signUp.start();
    // } catch (Exception e) {
    // e.printStackTrace();
    // UtilityFormatter.formatMessagesParamsError(httpDataResponse);
    // }
    // return httpDataResponse;
    // }
}
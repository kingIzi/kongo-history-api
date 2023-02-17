package com.kongo.history.api.kongohistoryapi.service;


//app imports
import com.kongo.history.api.kongohistoryapi.model.form.LoginForm;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;


//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AuthService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String SIGN_IN_WITH_EMAIL_PASSWORD = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyCE4py7YtsILZHix12wW-LroXgth7jvDNQ";


    public HttpDataResponse<LoginResponse> loginUser(final LoginForm loginForm){
        final var httpDataResponse = new HttpDataResponse<LoginResponse>();
        try{
            final var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            final var request = new HttpEntity<>(loginForm,headers);
            final var response = this.restTemplate.postForEntity(AuthService.SIGN_IN_WITH_EMAIL_PASSWORD,request,LoginResponse.class);
            httpDataResponse.setResponse(response.getBody());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return httpDataResponse;
    }
}
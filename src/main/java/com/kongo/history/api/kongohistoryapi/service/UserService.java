package com.kongo.history.api.kongohistoryapi.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.kongo.history.api.kongohistoryapi.model.form.FindUserForm;
import com.kongo.history.api.kongohistoryapi.model.form.UpdateUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kongo.history.api.kongohistoryapi.model.entity.User;
import com.kongo.history.api.kongohistoryapi.model.form.RegisterUserForm;
import com.kongo.history.api.kongohistoryapi.model.response.LoginResponse;
import com.kongo.history.api.kongohistoryapi.repository.UserRepository;
import com.kongo.history.api.kongohistoryapi.utils.AppUtilities;
import com.kongo.history.api.kongohistoryapi.utils.HttpDataResponse;
import com.kongo.history.api.kongohistoryapi.utils.UtilityFormatter;
import com.kongo.history.api.kongohistoryapi.utils.ValueDataException;
import com.kongo.history.api.kongohistoryapi.utils.AppConst;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addNewUser(final RegisterUserForm registerForm, final LoginResponse loginResponse) {
        final var user = new User(registerForm.getFullName(), registerForm.getPhoneNumber(),
                loginResponse.getEmail().trim(), loginResponse.getLocalId().trim(), registerForm.getRole().trim(),
                new Date(), new Date());
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
                    AppUtilities.supplyException("Failed to get users",
                            AppConst._KEY_CODE_INTERNAL_ERROR));
            httpDataResponse.setResponse(users);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<List<User>> getUsersList(final Integer limit, final FindUserForm findUserForm) {
        final var httpDataResponse = new HttpDataResponse<List<User>>();
        try {
            if (limit != null) {
                final var users = this.userRepository.searchByCriteria(limit, findUserForm)
                        .orElseThrow(AppUtilities.supplyException("Failed to retrieve users. Please contact support.",
                                AppConst._KEY_CODE_INTERNAL_ERROR));
                return new HttpDataResponse<List<User>>(users);
            } else {
                final var users = this.userRepository.searchByCriteria(findUserForm)
                        .orElseThrow(AppUtilities.supplyException("Failed to retrieve users. Please contact support.",
                                AppConst._KEY_CODE_INTERNAL_ERROR));
                return new HttpDataResponse<List<User>>(users);
            }
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

    private Map<String, Object> updateUserValues(final User user, final UpdateUserForm updateUserForm) {
        final var values = new HashMap<String, Object>();
        if (AppUtilities.modifiableValue(user.getEmail(), updateUserForm.getEmail()))
            values.put(User.EMAIL, updateUserForm.getEmail());
        if (AppUtilities.modifiableValue(user.getPhotoUrl(), updateUserForm.getPhotoUrl()))
            values.put(User.PHOTO_URL, updateUserForm.getPhotoUrl());
        if (AppUtilities.modifiableValue(user.getPhotoFileName(), updateUserForm.getPhotoFileName()))
            values.put(User.PHOTO_FILENAME, updateUserForm.getPhotoFileName());
        if (AppUtilities.modifiableValue(user.getPhoneNumber(), updateUserForm.getPhoneNumber()))
            values.put(User.PHONE_NUMBER, updateUserForm.getPhoneNumber());
        if (updateUserForm.getFavorites() != null)
            values.put(User.FAVORITES, updateUserForm.getFavorites());
        if (updateUserForm.getRole() != null)
            values.put(User.ROLE, updateUserForm.getRole());
        if (!values.isEmpty())
            values.put(User.DATE_UPDATED, new Date());
        return values;
    }

    public HttpDataResponse<User> updateUser(final String userId, final MultipartFile photo,
            final UpdateUserForm updateUserForm) {
        final var httpDataResponse = new HttpDataResponse<User>();
        try {
            final var user = this.userRepository.get(userId).orElseThrow(AppUtilities
                    .supplyException("Failed to find user userId = " + userId, AppConst._KEY_CODE_PARAMS_ERROR));
            if (photo != null && !photo.isEmpty()) {
                final var uploadedFile = this.userRepository.uploadFile(photo).orElseThrow(
                        AppUtilities.supplyException("Failed to upload user photo", AppConst._KEY_CODE_INTERNAL_ERROR));
                updateUserForm.setPhotoUrl(uploadedFile.getSecond());
                updateUserForm.setPhotoFileName(uploadedFile.getFirst());
            }
            final var updatedUser = this.updateUserValues(user, updateUserForm);
            if (this.userRepository.save(userId, updatedUser))
                return this.findUser(userId);
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, "No items found to update",
                    AppConst._KEY_MSG_SUCCESS);
        } catch (ValueDataException e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            UtilityFormatter.formatMessagesParamsError(httpDataResponse);
        }
        return httpDataResponse;
    }

    public HttpDataResponse<User> favorites(final String userId, final String comidId) {
        final var httpDataResponse = new HttpDataResponse<User>();
        try {
            final var user = this.userRepository.get(userId)
                    .orElseThrow(AppUtilities.supplyException("Failed to find user", AppConst._KEY_CODE_PARAMS_ERROR));
            if (user.getFavorites() == null) {
                user.setFavorites(new ArrayList<>());
                user.getFavorites().add(userId);
            }
            if (user.getFavorites().contains(userId))
                user.getFavorites().remove(userId);
            if (!user.getFavorites().contains(userId))
                user.getFavorites().add(userId);

            Map<String, Object> update = new HashMap<>();
            update.put(User.FAVORITES, user.getFavorites());
            this.userRepository.save(userId, update);
            return this.findUser(userId);
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

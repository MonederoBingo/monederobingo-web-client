package com.lealpoints.service;

import javax.mail.MessagingException;
import com.lealpoints.context.ThreadContextService;
import com.lealpoints.model.CompanyUser;
import com.lealpoints.model.NotificationEmail;
import com.lealpoints.repository.CompanyRepository;
import com.lealpoints.repository.CompanyUserRepository;
import com.lealpoints.service.base.BaseService;
import com.lealpoints.service.model.CompanyLoginResult;
import com.lealpoints.service.model.CompanyUserLogin;
import com.lealpoints.service.model.CompanyUserPasswordChanging;
import com.lealpoints.service.model.ServiceResult;
import com.lealpoints.service.model.ValidationResult;
import com.lealpoints.util.EmailUtil;
import com.lealpoints.util.Translations;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyUserService extends BaseService {
    private static final Logger logger = LogManager.getLogger(CompanyUserService.class.getName());
    private final CompanyUserRepository _companyUserRepository;
    private final CompanyRepository _companyRepository;
    private final ThreadContextService _threadContextService;
    private final CompanyService _companyService;

    @Autowired
    public CompanyUserService(CompanyUserRepository companyUserRepository, ThreadContextService threadContextService, Translations translations,
        CompanyRepository companyRepository, CompanyService companyService) {
        super(translations, threadContextService);
        _companyUserRepository = companyUserRepository;
        _threadContextService = threadContextService;
        _companyRepository = companyRepository;
        _companyService = companyService;
    }

    public ServiceResult<CompanyLoginResult> loginUser(CompanyUserLogin companyUserLogin) {
        CompanyLoginResult loginResult = new CompanyLoginResult();
        try {
            if (StringUtils.isEmpty(companyUserLogin.getEmail())) {
                logger.error("The company user email is empty");
                return new ServiceResult<>(false, getTranslation(Translations.Message.EMAIL_IS_EMPTY));
            }
            if (StringUtils.isEmpty(companyUserLogin.getPassword())) {
                logger.error("The company user password is empty");
                return new ServiceResult<>(false, getTranslation(Translations.Message.PASSWORD_IS_EMPTY));
            }
            CompanyUser companyUser = _companyUserRepository.getByEmailAndPassword(companyUserLogin.getEmail(), companyUserLogin.getPassword());
            if (companyUser == null) {
                return new ServiceResult<>(false, getTranslation(Translations.Message.LOGIN_FAILED));
            }
            if (!companyUser.isActive()) {
                loginResult.setActive(false);
                return new ServiceResult<>(false, getTranslation(Translations.Message.YOUR_USER_IS_NOT_ACTIVE), loginResult);
            }
            String apiKey = RandomStringUtils.random(20, true, true) + "com";
            final int updatedRows = _companyUserRepository.updateApiKeyByEmail(companyUser.getEmail(), apiKey);
            if (updatedRows != 1) {
                logger.error("The company user api key could not be updated. updatedRows: " + updatedRows);
                return new ServiceResult<>(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
            }
            loginResult.setEmail(companyUser.getEmail());
            loginResult.setMustChangePassword(companyUser.getMustChangePassword());
            loginResult.setActive(true);
            loginResult.setCompanyId(companyUser.getCompanyId());
            loginResult.setCompanyUserId(companyUser.getCompanyUserId());
            loginResult.setLanguage(companyUser.getLanguage());
            loginResult.setCompanyName(_companyRepository.getByCompanyId(companyUser.getCompanyId()).getName());
            loginResult.setApiKey(apiKey);
            return new ServiceResult<>(true, "", loginResult);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ServiceResult<>(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
        }
    }

    public ServiceResult activateUser(String activationKey) throws Exception {
        try {
            _threadContextService.getQueryAgent().beginTransaction();
            int updatedRows = _companyUserRepository.updateActivateByActivationKey(activationKey);
            if (updatedRows > 0) {
                _companyUserRepository.clearActivationKey(activationKey);
                _threadContextService.getQueryAgent().commitTransaction();
                return new ServiceResult(true, getTranslation(Translations.Message.YOUR_USER_IS_ACTIVE_NOW));
            } else {
                _threadContextService.getQueryAgent().rollbackTransaction();
                return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
        }
    }

    public ServiceResult sendActivationEmail(String email) {
        try {
            final CompanyUser companyUser = _companyUserRepository.getByEmail(email);
            if (companyUser == null) {
                return new ServiceResult(false, getTranslation(Translations.Message.THIS_EMAIL_DOES_NOT_EXIST));
            }
            _companyService.sendActivationEmail(email, companyUser.getActivationKey());
            return new ServiceResult(true, getTranslation(Translations.Message.WE_HAVE_SENT_YOU_AND_ACTIVATION_LINK));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
        }
    }

    public ServiceResult sendTempPasswordEmail(String email) {
        try {
            final CompanyUser companyUser = _companyUserRepository.getByEmail(email);
            if (companyUser == null) {
                return new ServiceResult(false, getTranslation(Translations.Message.THIS_EMAIL_DOES_NOT_EXIST));
            }
            final String tempPassword = RandomStringUtils.random(8, true, true);
            final int updatedRows = _companyUserRepository.updatePasswordByEmail(email, tempPassword, true);
            if (updatedRows > 0) {
                sendTempPasswordEmail(email, tempPassword);
            } else {
                return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
            }
            return new ServiceResult(true, getTranslation(Translations.Message.WE_HAVE_SENT_YOU_A_NEW_PASSWORD_TO_YOUR_EMAIL));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
        }
    }

    public ServiceResult changePassword(CompanyUserPasswordChanging passwordChanging) {
        try {
            final ValidationResult validationResult = validatePassword(passwordChanging);
            if (validationResult.isValid()) {
                final int updatedRows =
                    _companyUserRepository.updatePasswordByEmail(passwordChanging.getEmail(), passwordChanging.getNewPassword(), false);
                if (updatedRows > 0) {
                    return new ServiceResult<>(true, getTranslation(Translations.Message.YOUR_PASSWORD_HAS_BEEN_CHANGED));
                } else {
                    return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
                }
            } else {
                return new ServiceResult<>(false, validationResult.getMessage());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ServiceResult(false, getTranslation(Translations.Message.COMMON_USER_ERROR));
        }
    }

    void sendTempPasswordEmail(String email, String tempPassword) throws MessagingException {
        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setSubject(getTranslation(Translations.Message.NEW_PASSWORD_EMAIL_SUBJECT));
        notificationEmail.setBody(getTranslation(Translations.Message.NEW_PASSWORD_EMAIL_BODY) + " " + tempPassword);
        notificationEmail.setEmailTo(email);
        EmailUtil.sendEmail(notificationEmail);
    }

    private ValidationResult validatePassword(CompanyUserPasswordChanging passwordChanging) {
        if (passwordChanging.getNewPassword() == null || passwordChanging.getPasswordConfirmation() == null) {
            return new ValidationResult(false, getTranslation(Translations.Message.PASSWORD_MUST_HAVE_AT_LEAST_6_CHARACTERS));
        }
        if (passwordChanging.getNewPassword().length() < 6) {
            return new ValidationResult(false, getTranslation(Translations.Message.PASSWORD_MUST_HAVE_AT_LEAST_6_CHARACTERS));
        }
        if (!passwordChanging.getNewPassword().equals(passwordChanging.getPasswordConfirmation())) {
            return new ValidationResult(false, getTranslation(Translations.Message.PASSWORD_AND_CONFIRMATION_ARE_DIFFERENT));
        }
        return new ValidationResult(true, "");
    }
}
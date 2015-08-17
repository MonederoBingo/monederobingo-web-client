package com.lealpoints.service.validation;

import com.lealpoints.context.ThreadContextService;
import com.lealpoints.service.base.BaseService;
import com.lealpoints.service.model.ValidationResult;
import com.lealpoints.util.Translations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidatorService extends BaseService {

    @Autowired
    public PhoneValidatorService(Translations translations, ThreadContextService threadContextService) {
        super(translations, threadContextService);
    }

    public ValidationResult validate(String phone) {
        if (phone == null || phone.length() != 10) {
            return new ValidationResult(false, getTranslation(Translations.Message.PHONE_MUST_HAVE_10_DIGITS));
        }
        return new ValidationResult(true);
    }
}

package dev.ta2khu75.shopapp.component;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import dev.ta2khu75.shopapp.utils.WebUtil;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class LocalizationUtil {
  private final MessageSource messageSource;
  private final LocaleResolver localeResolver;

  public String getLocalizedMessage(String message, Object ...params){
    Locale locale=localeResolver.resolveLocale(WebUtil.getCurrentRequest());
    return messageSource.getMessage(message, params, locale);
  }
}
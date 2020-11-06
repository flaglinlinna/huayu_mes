package provider;

import org.apache.camel.ValidationException;

public interface CommonI18nService {
	public void isRequired(String value, Object...args) throws ValidationException;
	public void isRequired(String value, String message, Object...args) throws ValidationException;
}


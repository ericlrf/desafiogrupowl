package com.apress.todo;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

public class ValidationUtils {
	
	public static Validator buildValidator() {
	PlatformResourceBundleLocator platform = new PlatformResourceBundleLocator("messages");
	MessageInterpolator interpolator = new ResourceBundleMessageInterpolator(platform);
	
	return Validation
			.byDefaultProvider()
			.configure()
			.messageInterpolator(interpolator)
			.buildValidatorFactory()
			.getValidator();
	}
}

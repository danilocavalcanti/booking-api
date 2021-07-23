/**
 * 
 */
package com.alten.bookingapi.i18n;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Danilo Cavalcanti
 *
 */
@Component
@Getter
@Setter
public class MessageBundle {
	
	private static final String DEFAULT_LANG = "en";
	
	@Autowired
	private HttpServletRequest request; 
	
	public String get(String key) {
		
		String lang = Objects.isNull(request.getParameter("lang")) ? DEFAULT_LANG : request.getParameter("lang");
		
		Locale locale = new Locale(lang);
		
		try {
			
			ResourceBundle messages = ResourceBundle.getBundle("static/messages", locale);
			
			return messages.getString(key);
			
		} catch (Exception e) {
			
			locale = new Locale(DEFAULT_LANG);
			
			ResourceBundle messages = ResourceBundle.getBundle("static/messages", locale);
			
			return messages.getString(key);
			
		}
		
	}

}

/**
 * 
 */
package com.alten.bookingapi.body.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Danilo Cavalcanti
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseBody {
	
	private Set<String> messages;
}

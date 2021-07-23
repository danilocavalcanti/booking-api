/**
 * 
 */
package com.alten.bookingapi.body.response;

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
public class SuccessResponseBody<T> {

	private T result;

	public SuccessResponseBody<T> create(T result) {
		
		setResult(result);

		return this;
	}
}

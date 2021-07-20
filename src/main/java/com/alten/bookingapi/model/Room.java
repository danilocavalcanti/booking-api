/**
 * 
 */
package com.alten.bookingapi.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Danilo Cavalcanti
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false)
	private Integer number;

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private BigDecimal price;
	
	@Column(nullable = true)
	private Short guestsLimit;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "room", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Booking> bookings = new ArrayList<>();

	public Room(Integer id) {
		
		setId(id);
	}
}

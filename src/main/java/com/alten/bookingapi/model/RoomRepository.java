package com.alten.bookingapi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Danilo Cavalcanti
 *
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>{

}

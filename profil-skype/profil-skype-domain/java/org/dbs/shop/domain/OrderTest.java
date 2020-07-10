package org.dbs.shop.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.dbs.shop.domain.customer.Customer;
import org.dbs.shop.domain.order.Order;
import org.junit.jupiter.api.Test;

public class OrderTest {

	@Test
	public void add_items_test() {
		// Given
		final Customer customer = new Customer("John Doe", "password");

		// When
		final Order order = new Order(customer, 1, LocalDate.now());
		order.addItem(new Item("Banana", BigDecimal.valueOf(1.5), 10));
		order.addItem(new Item("Pinaple", BigDecimal.valueOf(5.99), 1));
		order.addItem(new Item("Mango", BigDecimal.valueOf(3.01), 4));

		// Then
		//assertThat(order.getTotal()).isEqual(BigDecimal.valueOf(3));
		assertEquals(BigDecimal.valueOf(33.03), order.getTotal());
	}

}

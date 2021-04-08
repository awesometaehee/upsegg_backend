package com.example.upsegg.order.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SalesOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name; // 구매자 이름
	private String address; // 주소
	private String tel; // 번호
	private String orderDate; // 주문일
	private String note;
	private String pay;

	@Column(columnDefinition = "CHAR(2)")
	@ColumnDefault("'00'")
	private String orderState; // 주문 상태

	@OneToMany(cascade = CascadeType.PERSIST) // 영속성 전이로 저장
	@JoinColumn(name = "salesOrderId")
	private List<SalesOrderDetail> salesOrderDetail; // 다른 클래스로 삽입 -> 참조관계
}

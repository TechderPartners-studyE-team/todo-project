package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
// @Table을 추가하지 않거나, 추가해도 name을 명시하지 않는다면 -> @Entity의 이름을 테이블 이름으로 간주
// 만약 @Entity에 이름을 지정하지 않는 경우 클래스의 이름을 테이블 이름으로 간주
@Table(name = "Todo")		//테이블 이름을 지정하기 위함
public class TodoEntity {
	@Id	// 기본키가 될 필드에 지정
	@GeneratedValue(generator="system-uuid")	// 이 어노테이션을 이용해 ID를 자동으로 생성 가능
	@GenericGenerator(name="system-uuid", strategy="uuid")
	private String id;		// 이 오브젝트의 아이
	private String userId;	// 이 오브젝트를 생성한 유저의 아이디
	private String title;	// Todo 타이틀 예) 운동하
	private boolean done;	// true - todo를 완료한 경우(checked)
}

package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo")
public class TodoEntity {
    //primary key 지정
    @Id
    //id 자동생성
    @GeneratedValue(generator = "system-uuid")
    //커스텀 Generator
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;
    private String userId;
    private String title;
    private boolean done;
}

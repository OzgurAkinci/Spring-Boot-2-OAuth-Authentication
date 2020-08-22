package com.project.app.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import lombok.Data;

@Entity
@Table(name = "s_role")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Data
@Audited
@NoArgsConstructor
@AllArgsConstructor
public class SRole extends Auditable<String> implements Serializable {
	@Id
	@SequenceGenerator(name = "s_role_seq", sequenceName = "s_role_seq", initialValue = 5, allocationSize = 1)
	@GeneratedValue(generator = "s_role_seq", strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "role_name")
	private String roleName;
}

package com.app.base.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class IdEntity implements Serializable {
	private static final long serialVersionUID = 5391836388143717010L;
	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_gen")
	@TableGenerator(name = "id_gen", table = "id_gen", initialValue = 5000, allocationSize=50)
	protected Long id;

	// @Id
	// @SequenceGenerator(name="sequenceGenerator",sequenceName="ACTIVITIESSCOPE_SEQ")
	// @GeneratedValue(generator="sequenceGenerator",strategy=GenerationType.SEQUENCE)
	// protected Long id;

	@Version
	protected Integer version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}

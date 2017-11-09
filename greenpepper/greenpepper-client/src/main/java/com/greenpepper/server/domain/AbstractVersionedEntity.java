package com.greenpepper.server.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractVersionedEntity extends AbstractEntity
{
    private Integer version;
    
    @Version
    @Column(name="VERSION")
    public Integer getVersion()
    {
        return this.version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }
}

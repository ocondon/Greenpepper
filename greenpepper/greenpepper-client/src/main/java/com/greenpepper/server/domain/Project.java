package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.PROJECT_NAME_IDX;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;

/**
 * Project Class.
 * Definition of a project.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * @author JCHUET
 */

@Entity
@Table(name="PROJECT")
@SuppressWarnings("serial")
public class Project extends AbstractVersionedEntity implements Comparable
{
    private String name;
    private Set<Repository> repositories = new HashSet<Repository>();
    private Set<SystemUnderTest> systemUnderTests = new HashSet<SystemUnderTest>();

    public static Project newInstance(String name)
    {
        Project project = new Project();
        project.setName(name);
        return project;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length=255)
    public String getName()
    {
        return name;
    }

    @OneToMany(mappedBy="project", cascade=CascadeType.ALL)
    public Set<Repository> getRepositories()
    {
        return repositories;
    }

    @OneToMany(mappedBy="project", cascade=CascadeType.ALL)
    public Set<SystemUnderTest> getSystemUnderTests()
    {
        return systemUnderTests;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRepositories(Set<Repository> repositories)
    {
        this.repositories = repositories;
    }

    public void setSystemUnderTests(Set<SystemUnderTest> systemUnderTests)
    {
        this.systemUnderTests = systemUnderTests;
    }

    public void addRepository(Repository repo) throws GreenPepperServerException
    {
        if (findRepositoryByName(repo.getName()) != null)
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REPOSITORY_ALREADY_EXISTS, "Repository already exists");
        }

        repo.setProject(this);
        repositories.add(repo);
    }

    public void removeRepository(Repository repo) throws GreenPepperServerException
    {
        if(!repositories.contains(repo))
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REPOSITORY_NOT_FOUND, "Repository not found");
        }

        repositories.remove(repo);
        repo.setProject(null);
    }

    public void addSystemUnderTest(SystemUnderTest sut) throws GreenPepperServerException
    {
        if (findSystemUnderTestByName(sut.getName()) != null)
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SUT_ALREADY_EXISTS, "Sut name already exists");
        }

        if(systemUnderTests.isEmpty()){ sut.setIsDefault(true); }
        systemUnderTests.add(sut);
        sut.setProject(this);
    }

    public void removeSystemUnderTest(SystemUnderTest sut) throws GreenPepperServerException
    {
        if(!systemUnderTests.contains(sut))
        {
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SUT_NOT_FOUND, "Sut not found");
        }

        systemUnderTests.remove(sut);
        if(sut.isDefault() && !systemUnderTests.isEmpty())
        {
            systemUnderTests.iterator().next().setIsDefault(true);
        }

        sut.setProject(null);
    }

    @Transient
    public SystemUnderTest getDefaultSystemUnderTest()
    {
        for(SystemUnderTest sut : systemUnderTests)
        {
            if(sut.isDefault())
            {
                return sut;
            }
        }

        return null;
    }

    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(PROJECT_NAME_IDX, name);
        return parameters;
    }

    public int compareTo(Object o)
    {
        return getName().compareTo(((Project)o).getName());
    }

    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Project))
        {
            return false;
        }

        Project projectCompared = (Project)o;
        if(getName().equals(projectCompared.getName()))
        {
            return true;
        }

        return false;
    }

    public int hashCode()
    {
        return getName().hashCode();
    }

    private Repository findRepositoryByName(String repoName)
    {
        for (Repository repository : repositories)
        {
            if (repository.getName().equalsIgnoreCase(repoName))
            {
                return repository;
            }
        }
        return null;
    }

    private SystemUnderTest findSystemUnderTestByName(String sutName)
    {
        for (SystemUnderTest sut : systemUnderTests)
        {
            if (sut.getName().equalsIgnoreCase(sutName))
            {
                return sut;
            }
        }
        return null;
    }
}

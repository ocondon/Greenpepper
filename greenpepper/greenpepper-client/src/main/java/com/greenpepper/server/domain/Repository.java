package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_BASEREPO_URL_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_BASETEST_URL_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_BASE_URL_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_CONTENTTYPE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_MAX_USERS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_NAME_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_PASSWORD_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_PROJECT_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_UID_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_USERNAME_IDX;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.runner.FactoryConverter;
import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.component.ContentType;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;


/**
 * Repository Class.
 * Definition of the repository.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * @author JCHUET
 */

@Entity
@Table(name="REPOSITORY", uniqueConstraints = {@UniqueConstraint(columnNames={"NAME", "PROJECT_ID"})})
@SuppressWarnings("serial")
public class Repository extends AbstractVersionedEntity implements Comparable
{
    private String name;
    private String uid;
    private String baseUrl;
    private String repositoryBaseUrl;
    private String testBaseUrl;
    private ContentType contentType = ContentType.UNKNOWN;
    private String username;
    private String password;

    private Project project;
    private RepositoryType type;
    private Set<Requirement> requirements = new HashSet<Requirement>();
    private Set<Specification> specifications = new HashSet<Specification>();
    private int maxUsers;

    public static Repository newInstance(String uid)
    {
        Repository repository = new Repository();
        repository.setUid(uid);
        return repository;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length=255)
    public String getName()
    {
        return this.name;
    }

    @Basic
    @Column(name = "UIDENT", unique = true, nullable = false, length=255)
    public String getUid()
    {
        return this.uid;
    }

    @Basic
    @Column(name = "BASE_URL", nullable = false, length=255)
    public String getBaseUrl()
    {
        return this.baseUrl;
    }

    @Basic
    @Column(name = "BASE_REPOSITORY_URL", nullable = false, length=255)
    public String getBaseRepositoryUrl()
    {
        return this.repositoryBaseUrl;
    }

    @Basic
    @Column(name = "BASE_TEST_URL", nullable = false, length=255)
    public String getBaseTestUrl()
    {
        return this.testBaseUrl;
    }

    @Basic
    @Column(name = "USERNAME", nullable = true, length=15)
	public String getUsername()
	{
		return username;
	}

    @Basic
    @Column(name = "PASSWORD", nullable = true, length=15)
	public String getPassword()
	{
		return password;
	}

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="PROJECT_ID")
    public Project getProject()
    {
        return project;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="REPOSITORY_TYPE_ID")
    public RepositoryType getType()
    {
        return this.type;
    }

    @OneToMany(mappedBy="repository", cascade=CascadeType.ALL)
    public Set<Requirement> getRequirements()
    {
        return this.requirements;
    }

    @OneToMany(mappedBy="repository", cascade=CascadeType.ALL)
    public Set<Specification> getSpecifications()
    {
        return specifications;
    }

    public ContentType getContentType()
    {
        return contentType;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public void setBaseRepositoryUrl(String repositoryBaseUrl)
    {
        this.repositoryBaseUrl = repositoryBaseUrl;
    }

    public void setBaseTestUrl(String testBaseUrl)
    {
        this.testBaseUrl = testBaseUrl;
    }

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

    public void setContentType(ContentType contentType)
    {
        this.contentType = contentType;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public void setType(RepositoryType type)
    {
        this.type = type;
    }

    public void setRequirements(Set<Requirement> requirements)
    {
        this.requirements = requirements;
    }

    public void setSpecifications(Set<Specification> specifications)
    {
        this.specifications = specifications;
    }

    public void addRequirement(Requirement requirement) throws GreenPepperServerException
    {
        if(requirements.contains(requirement) || requirementNameExists(requirement.getName()))
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REQUIREMENT_ALREADY_EXISTS, "Requirement already exists");

        requirement.setRepository(this);
        requirements.add(requirement);
    }

    public void removeRequirement(Requirement requirement) throws GreenPepperServerException
    {
        if(!requirements.contains(requirement) )
            throw new GreenPepperServerException( GreenPepperServerErrorKey.REQUIREMENT_NOT_FOUND, "Requirement not found");

        requirements.remove(requirement);
        requirement.setRepository(null);
    }

    public void addSpecification(Specification specification) throws GreenPepperServerException
    {
        if(specifications.contains(specification) || specificationNameExists(specification.getName()))
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SPECIFICATION_ALREADY_EXISTS, "Specification already exists");

        specification.setRepository(this);
        specifications.add(specification);
    }

    public void removeSpecification(Specification specification) throws GreenPepperServerException
    {
        if(!specifications.contains(specification))
            throw new GreenPepperServerException( GreenPepperServerErrorKey.SPECIFICATION_NOT_FOUND, "Specification not found");

        specifications.remove(specification);
        specification.setRepository(null);
    }

    @Transient
    public int getMaxUsers()
    {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers)
    {
        this.maxUsers = maxUsers;
    }

    public String resolveName(Document document) throws GreenPepperServerException
    {
        return type.resolveName(document);
    }

	public String asCmdLineOption(EnvironmentType env) 
	{
		return type.asFactoryArguments(this, env, false, null, null);
	}

	public DocumentRepository asDocumentRepository(EnvironmentType env) throws Exception 
	{
	    return asDocumentRepository(env, null, null);
	}

	public DocumentRepository asDocumentRepository(EnvironmentType env, String user, String pwd) throws Exception 
	{
	    return (DocumentRepository)new FactoryConverter().convert(type.asFactoryArguments(this, env, true, user, pwd));
	}

    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(REPOSITORY_NAME_IDX, XmlRpcDataMarshaller.padNull(name));
        parameters.add(REPOSITORY_UID_IDX, XmlRpcDataMarshaller.padNull(uid));
        parameters.add(REPOSITORY_PROJECT_IDX, project != null ? project.marshallize() : Project.newInstance("").marshallize());
        parameters.add(REPOSITORY_TYPE_IDX, type != null ? type.marshallize() : RepositoryType.newInstance("").marshallize());
        parameters.add(REPOSITORY_CONTENTTYPE_IDX, contentType.toString());
        parameters.add(REPOSITORY_BASE_URL_IDX, XmlRpcDataMarshaller.padNull(getBaseUrl()));
        parameters.add(REPOSITORY_BASEREPO_URL_IDX, XmlRpcDataMarshaller.padNull(getBaseRepositoryUrl()));
        parameters.add(REPOSITORY_BASETEST_URL_IDX, XmlRpcDataMarshaller.padNull(getBaseTestUrl()));
        parameters.add(REPOSITORY_USERNAME_IDX, XmlRpcDataMarshaller.padNull(username));
        parameters.add(REPOSITORY_PASSWORD_IDX, XmlRpcDataMarshaller.padNull(password));
        parameters.add(REPOSITORY_MAX_USERS_IDX, maxUsers);
        return parameters;
    }

    public int compareTo(Object o)
    {
        return getName().compareTo(((Repository)o).getName());
    }

    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Repository))
        {
            return false;
        }

        Repository repoCompared = (Repository)o;
        return getUid().equals(repoCompared.getUid());
    }

    public int hashCode()
    {
        return getUid() == null ? 0 : getUid().hashCode();
    }

    private boolean requirementNameExists(String requirementName)
    {
        for (Requirement requirement : requirements)
            if (requirement.getName().equalsIgnoreCase(requirementName))
                return true;

        return false;
    }

    private boolean specificationNameExists(String specificationName)
    {
        for (Specification specification : specifications)
            if (specification.getName().equalsIgnoreCase(specificationName))
                return true;

        return false;
    }
}
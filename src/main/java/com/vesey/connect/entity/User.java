package com.vesey.connect.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vesey.connect.utils.PasswordUtils;

/**
 *
 * @author Richard
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u ORDER BY u.surname DESC"), @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"), @NamedQuery(name = User.FIND_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :username"), @NamedQuery(name = User.COUNT_ALL, query = "SELECT COUNT(u) FROM User u") })
public class User implements Serializable {
	public static final String FIND_ALL = "User.findAll";
	public static final String COUNT_ALL = "User.countAll";
	public static final String FIND_BY_LOGIN_PASSWORD = "User.findByUsernameAndPassword";
	public static final String FIND_BY_USERNAME = "User.findByUsername";

	@Basic(optional = false)
	@NotNull
	@Column(name = "attachments")
	private boolean attachments;

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "userid")
	private Integer userid;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 45)
	@Column(name = "title")
	private String title;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 45)
	@Column(name = "username")
	private String username;
	@Size(min = 1, max = 45)
	@Column(name = "password")
	private String password;
	@Size(min = 1, max = 200)
	@Column(name = "hashedpassword")
	private String hashedpassword;
	@Size(min = 1, max = 45)
	@Column(name = "token")
	private String token;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "forename")
	private String forename;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "surname")
	private String surname;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "email")
	private String email;
	@Size(max = 45)
	@Column(name = "telephone")
	private String telephone;
	@Size(max = 500)
	@Column(name = "comments")
	private String comments;
	@Basic(optional = false)
	@NotNull
	@Column(name = "administrator")
	private boolean administrator;
	@Basic(optional = false)
	@NotNull
	@Column(name = "disabled")
	private boolean disabled;
	@Size(max = 200)
	@Column(name = "updatesemail")
	private String updatesemail;
	@Basic(optional = false)
	@NotNull
	@Column(name = "sendupdates")
	private boolean sendupdates;
	@Basic(optional = false)
	@NotNull
	@Column(name = "accounts")
	private boolean accounts;
	@Basic(optional = false)
	@NotNull
	@Column(name = "primarycontact")
	private boolean primarycontact;
	@Size(max = 80)
	@Column(name = "position")
	private String position;
	@Size(max = 200)
	@Column(name = "updatesemail2")
	private String updatesemail2;
	@Size(max = 45)
	@Column(name = "idkey")
	private String idkey;
	@Column(name = "registrationdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationdate;
	@Column(name = "lastloggedindate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastloggedindate;


	@JoinTable(name = "userrole", 
		joinColumns = { @JoinColumn(name = "userid", referencedColumnName = "userid") },
		inverseJoinColumns = { @JoinColumn(name = "roleid", referencedColumnName = "roleid") }
	)
	@ManyToMany
	private Collection<Role> roleCollection;

	public String getAuthtoken() {
		return authtoken;
	}

	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}

	@Transient
	private String authtoken;

	public User() {
	}

	@PrePersist
	private void setUUID() {
		if (token == null) {
			token = UUID.randomUUID().toString().replace("-", "");
		}
		password = PasswordUtils.digestPassword(password);
	}

	public User(Integer userid) {
		this.userid = userid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean getAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getUpdatesemail() {
		return updatesemail;
	}

	public void setUpdatesemail(String updatesemail) {
		this.updatesemail = updatesemail;
	}

	public boolean getSendupdates() {
		return sendupdates;
	}

	public void setSendupdates(boolean sendupdates) {
		this.sendupdates = sendupdates;
	}

	public boolean getAccounts() {
		return accounts;
	}

	public void setAccounts(boolean accounts) {
		this.accounts = accounts;
	}

	public boolean getPrimarycontact() {
		return primarycontact;
	}

	public void setPrimarycontact(boolean primarycontact) {
		this.primarycontact = primarycontact;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUpdatesemail2() {
		return updatesemail2;
	}

	public void setUpdatesemail2(String updatesemail2) {
		this.updatesemail2 = updatesemail2;
	}

	public String getIdkey() {
		return idkey;
	}

	public void setIdkey(String idkey) {
		this.idkey = idkey;
	}

	public Date getRegistrationdate() {
		return registrationdate;
	}

	public void setRegistrationdate(Date registrationdate) {
		this.registrationdate = registrationdate;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userid != null ? userid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof User)) {
			return false;
		}
		User other = (User) object;
		if ((this.getUserid() == null && other.getUserid() != null) || (this.getUserid() != null && !this.getUserid().equals(other.getUserid()))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.vesey.searchpoint.database.generated.entity.Users[ userid=" + userid + " ] ";
	}

	public boolean getAttachments() {
		return attachments;
	}

	public void setAttachments(boolean attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the hashedpassword
	 */
	public String getHashedpassword() {
		return hashedpassword;
	}

	/**
	 * @param hashedpassword
	 *            the hashedpassword to set
	 */
	public void setHashedpassword(String hashedpassword) {
		this.hashedpassword = hashedpassword;
	}

	/**
	 * @return the lastloggedindate
	 */
	public Date getLastloggedindate() {
		return lastloggedindate;
	}

	/**
	 * @param lastloggedindate
	 *            the lastloggedindate to set
	 */
	public void setLastloggedindate(Date lastloggedindate) {
		this.lastloggedindate = lastloggedindate;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	@XmlTransient
	@JsonIgnore
	public Collection<Role> getRoleCollection() {
		return roleCollection;
	}

	public void setRoleCollection(Collection<Role> roleCollection) {
		this.roleCollection = roleCollection;
	}

}

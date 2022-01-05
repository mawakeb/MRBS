package nl.tudelft.sem.user.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nl.tudelft.sem.user.object.Type;

/**
 * The User Entity.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "net_id")
    private String netId;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    /**
     * No-args constructor for Spring.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param netId          the net id
     * @param name           the name
     * @param hashedPassword the hashed password
     * @param type           the type
     */
    public User(String netId, String name, String hashedPassword, Type type) {
        this.netId = netId;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.type = type;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets net id.
     *
     * @return the netId
     */
    public String getNetId() {
        return netId;
    }

    /**
     * Sets net id.
     *
     * @param netId the netId
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets hashed password.
     *
     * @return the hashed password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets hashed password.
     *
     * @param hashedPassword the hashed password
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            User user = (User) o;
            return Objects.equals(id, user.id)
                    && Objects.equals(netId, user.netId)
                    && Objects.equals(name, user.name)
                    && Objects.equals(hashedPassword, user.hashedPassword);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, netId, name, hashedPassword);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", netId='" + netId + '\''
                + ", name='" + name + '\''
                + ", hashedPassword='" + hashedPassword + '\''
                + '}';
    }
}

package org.expertojava.jbibrest.modelo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ClaseDominio {
    @Id
    @GeneratedValue
    private Long id;

    protected void setId(Long id) { this.id = id; }
    public Long getId() { return id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClaseDominio that = (ClaseDominio) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

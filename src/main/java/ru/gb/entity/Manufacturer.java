package ru.gb.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.gb.entity.common.InfoEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "MANUFACTURER")
public class Manufacturer extends InfoEntity {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.MERGE)
    private Set<Product> products;

    public boolean addProduct(Product product) {
        if (products == null) {
            products = new HashSet<>();
        }
        return products.add(product);
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }

    @Builder
    public Manufacturer(Long id, int version, String createdBy, LocalDateTime createdDate, String lastModifiedBy,
                        LocalDateTime lastModifiedDate, String name, Set<Product> products) {
        super(id, version, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.name = name;
        this.products = products;
    }

}

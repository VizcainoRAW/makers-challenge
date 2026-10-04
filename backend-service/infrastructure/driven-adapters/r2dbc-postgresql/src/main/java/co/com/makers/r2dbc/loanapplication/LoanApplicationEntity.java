package co.com.makers.r2dbc.loanapplication;

import co.com.makers.model.loanapplication.valueobject.LoanStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table("loan_applications")
public class LoanApplicationEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("status")
    private String status;

    @Column("amount")
    private BigDecimal amount;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

    @Transient
    private boolean isNew = false;

    public LoanApplicationEntity() {
    }

    public LoanApplicationEntity(UUID id, UUID userId, LoanStatus status, BigDecimal amount, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.status = status != null ? status.name() : null;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    @Override
    public boolean isNew() {
        return this.isNew || this.id == null;
    }
}
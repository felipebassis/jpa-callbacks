package br.com.iadtec.demo.entity.auditory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "auditory_value")
public class AuditoryValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "column_name")
    private String columnName;

    @Column(name = "previous_value")
    private String previousValue;

    @Column(name = "current_value")
    private String currentValue;

    public AuditoryValue(String columnName, String currentValue) {
        this.columnName = columnName;
        this.currentValue = currentValue;
    }

    public AuditoryValue(String columnName, String previousValue, String currentValue) {
        this.columnName = columnName;
        this.previousValue = previousValue;
        this.currentValue = currentValue;
    }
}

package com.quizplus.demo.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "data_in_sneak_case", nullable = false)
    private String data_in_sneak_case;

    @Column(name = "dataInCamelCase", nullable = false)
    private String dataInCamelCase;

    @Override
    public String toString() {
        return "Description{" +
                "id=" + id +
                ", dataInSneakCase='" + data_in_sneak_case + '\'' +
                ", dataInCamelCase='" + dataInCamelCase + '\'' +
                '}';
    }
}

package com.netomass.main_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * this class intended to control who gets to access which folder
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Path {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private String name;

    @ManyToOne
    private User user;

}

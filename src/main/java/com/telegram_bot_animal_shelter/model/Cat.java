package com.telegram_bot_animal_shelter.model;

import lombok.*;

import javax.persistence.*;

/**
 * Entity for shelter pet: cat.
 * @author: Kucherenko V.V.
 * @version 0.0.1
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cat {

    /**
     * Autogenerated primary key for entity cat
     * @param id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cat name
     * @param name
     */
    @Column(name = "name")
    private String name;

    /**
     * Cat breed. Like: Britain, Norwegian forest
     * @param breed
     */
    @Column(name = "breed")
    private String breed;

    /**
     * Cat age
     * @param age
     */
    @Column(name = "age")
    private int age;

    /**
     * Additional information about the pet: habbits, health, features
     * @param description
     */
    @Column(name = "description")
    private String description;


}

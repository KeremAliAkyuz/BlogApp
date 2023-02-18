package dev.aliak.blogapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Id
    private Long id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "content",nullable = false)
    private String content;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    //orphan removal yani parent silindiğinde commentler de silinir
    //mapped by bi-directional relationshiplerde kullanılır.Post commentin atası olduğu için post'a göre map yapılır

    private Set<Comment> comments = new HashSet<>();
    //List yerine set kullanıyoruz çünkü Set duplicatelere izin vermez list verir.

    @ManyToOne(fetch = FetchType.LAZY)
    //Whenever we load the post entity,then its category wont load immediately
    //we can get this category object on demand by calling getter Category method
    @JoinColumn(name = "category_id")
    private Category category;

}

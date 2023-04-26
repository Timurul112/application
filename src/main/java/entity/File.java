package entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "events")
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "file_path")
    private String filePath;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();

}

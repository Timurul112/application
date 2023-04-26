package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {})
    @JoinColumn(name = "file_id")
    private File file;

    public void setUser(User user) {
        this.user = user;
        this.user.getEvents().add(this);
    }

    public void setFile(File file) {
        this.file = file;
        this.file.getEvents().add(this);
    }
}

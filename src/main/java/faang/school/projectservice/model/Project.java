package faang.school.projectservice.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import faang.school.projectservice.model.stage.Stage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "storage_size")
    private BigInteger storageSize;

    @Column(name = "max_storage_size")
    private BigInteger maxStorageSize;

    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_project_id")
    private Project parentProject;

    @OneToMany(mappedBy = "parentProject", fetch = FetchType.EAGER)
    private List<Project> children;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @OneToMany(mappedBy = "project")
    private List<Resource> resources;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectVisibility visibility;

    @Column(name = "cover_image_id")
    private String coverImageId;

    @OneToMany(mappedBy = "project")
    private List<Team> teams;

    @OneToOne(mappedBy = "project")
    private CalendarToken calendarToken;

    @OneToOne(mappedBy = "project")
    private Schedule schedule;

    @OneToMany(mappedBy = "project")
    private List<Stage> stages;

    @OneToMany(mappedBy = "project")
    private List<Vacancy> vacancies;

    @ManyToMany(mappedBy = "projects")
    private List<Moment> moments;
    
    public void addResource(Resource resource) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }
        this.resources.add(resource);
    }
    
    public boolean isStatusFinished() {
        return this.status == ProjectStatus.CANCELLED || this.status == ProjectStatus.COMPLETED;
    }
    
    public boolean isMaximumStorageSizeExceed(Long fileSize) {
        BigInteger sizeAfterAddingFile = this.getStorageSize().add(BigInteger.valueOf(fileSize));
        
        return sizeAfterAddingFile.compareTo(this.getMaxStorageSize()) > 0;
    }
    
    public BigInteger addStorageSize(BigInteger fileSize) {
        return this.getStorageSize().add(fileSize);
    }
    
    public boolean hasCover() {
        return !StringUtils.isBlank(this.coverImageId);
    }
}

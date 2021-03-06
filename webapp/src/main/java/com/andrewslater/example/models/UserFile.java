package com.andrewslater.example.models;

import com.andrewslater.example.api.APIView;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_files")
public class UserFile implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(UserFile.class);

    public enum Status {UPLOADING, SAVING, AVAILABLE, SOFT_DELETED, HARD_DELETED;}

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false, updatable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "volume_id", nullable = false)
    @JsonIgnore
    private Volume volume;

    @Column(name = "path")
    @JsonIgnore
    private String path;

    @Column(name = "name")
    private String name;

    @Column(name = "size_in_bytes", nullable = false)
    private Long sizeInBytes;

    @Column(name = "mimetype", nullable = false)
    private String mimeType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = true)
    private LocalDateTime updatedAt;

    @Column(name = "is_public", nullable = false)
    @JsonView(APIView.Internal.class)
    private Boolean isPublic;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return "/user-file/" + getFileId();
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getStoragePath() {
        String extension = FilenameUtils.getExtension(getName());
        String fullPath = String.format("%s/%d", getPath(), getFileId());

        if (!StringUtils.isEmpty(extension)) {
            fullPath += "." + extension;
        }

        return fullPath;
    }

    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UserFile rhs = (UserFile) obj;
        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(fileId, rhs.fileId)
            //                TODO: Uncomment when upgrading to commons-lang:3.5 (https://issues.apache.org/jira/browse/LANG-456)
            //            .append(user, rhs.user)
            .append(volume, rhs.volume)
            .append(path, rhs.path)
            .append(name, rhs.name)
            .append(sizeInBytes, rhs.sizeInBytes)
            .append(mimeType, rhs.mimeType)
            .append(status, rhs.status)
            .append(createdAt, rhs.createdAt)
            .append(updatedAt, rhs.updatedAt)
            .append(isPublic, rhs.isPublic)
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(577, 127)
            .append(fileId)
            //                TODO: Uncomment when upgrading to commons-lang:3.5 (https://issues.apache.org/jira/browse/LANG-456)
            //            .append(user)
            .append(volume)
            .append(path)
            .append(name)
            .append(sizeInBytes)
            .append(mimeType)
            .append(status)
            .append(createdAt)
            .append(updatedAt)
            .append(isPublic)
            .toHashCode();
    }
}

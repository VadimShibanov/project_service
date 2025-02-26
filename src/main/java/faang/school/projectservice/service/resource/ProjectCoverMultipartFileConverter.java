package faang.school.projectservice.service.resource;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import faang.school.projectservice.dto.image.ProjectImage;
import faang.school.projectservice.dto.resource.FileResourceDto;
import faang.school.projectservice.model.Project;
import lombok.SneakyThrows;

@Component
public class ProjectCoverMultipartFileConverter extends MultipartFileResourceConverter {
    @SneakyThrows
    @Override
    public FileResourceDto convertToFileResourceDto(MultipartFile file, Project project) {
        ProjectImage projectImage = new ProjectImage(file.getInputStream());
        return FileResourceDto.builder()
            .size(projectImage.getResizedImageSize())
            .contentType(file.getContentType())
            .fileName(file.getOriginalFilename())
            .folderName(getDefaultProjectFolderName(project))
            .resourceInputStream(projectImage.getCoverInputStream())
            .build();
    }
    
    @Override
    public ProjectResourceType getProjectResourceSupportedType() {
        return ProjectResourceType.PROJECT_COVER;
    }
}

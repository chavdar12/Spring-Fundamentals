package bg.softuni.demo.web;

import bg.softuni.demo.model.entity.PictureEntity;
import bg.softuni.demo.repository.PictureRepository;
import bg.softuni.demo.service.CloudinaryImage;
import bg.softuni.demo.service.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PicturesController.class)
class PicturesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @MockitoBean
    private PictureRepository pictureRepository;

    @Test
    void addUploadsAndSavesPicture() throws Exception {
        when(cloudinaryService.upload(any()))
                .thenReturn(new CloudinaryImage().setUrl("http://img/1.png").setPublicId("abc"));

        mockMvc.perform(multipart("/pictures/add")
                        .file(new MockMultipartFile("picture", "pic.png", "image/png", new byte[]{1}))
                        .param("title", "Sunset"))
                .andExpect(redirectedUrl("/pictures/all"));

        verify(pictureRepository).save(argThat(p ->
                p.getTitle().equals("Sunset") && p.getPublicId().equals("abc") && p.getUrl().equals("http://img/1.png")));
    }

    @Test
    void allListsPictures() throws Exception {
        when(pictureRepository.findAll()).thenReturn(List.of(new PictureEntity().setTitle("a").setUrl("u").setPublicId("p")));

        mockMvc.perform(get("/pictures/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all"))
                .andExpect(model().attribute("pictures", hasSize(1)));
    }

    @Test
    void deleteRemovesFromRepositoryOnlyWhenCloudinaryDeleteSucceeds() throws Exception {
        when(cloudinaryService.delete("ok")).thenReturn(true);
        when(cloudinaryService.delete("fail")).thenReturn(false);

        mockMvc.perform(delete("/pictures/delete").param("public_id", "ok"))
                .andExpect(redirectedUrl("/pictures/all"));
        mockMvc.perform(delete("/pictures/delete").param("public_id", "fail"))
                .andExpect(redirectedUrl("/pictures/all"));

        verify(pictureRepository).deleteAllByPublicId("ok");
        verify(pictureRepository, never()).deleteAllByPublicId("fail");
    }
}

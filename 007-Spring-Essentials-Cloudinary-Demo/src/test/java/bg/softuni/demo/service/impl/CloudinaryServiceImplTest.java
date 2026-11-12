package bg.softuni.demo.service.impl;

import bg.softuni.demo.service.CloudinaryImage;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CloudinaryServiceImplTest {

    private Uploader uploader;
    private CloudinaryServiceImpl service;

    @BeforeEach
    void setUp() {
        Cloudinary cloudinary = mock(Cloudinary.class);
        uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        service = new CloudinaryServiceImpl(cloudinary);
    }

    @Test
    void uploadReturnsUrlAndPublicIdAndDeletesTempFile() throws IOException {
        File[] uploaded = new File[1];
        when(uploader.upload(any(File.class), anyMap())).thenAnswer(inv -> {
            uploaded[0] = inv.getArgument(0);
            assertTrue(uploaded[0].exists());
            return Map.of("url", "http://img/1.png", "public_id", "abc");
        });

        CloudinaryImage image = service.upload(new MockMultipartFile("picture", "pic.png", "image/png", new byte[]{1, 2}));

        assertEquals("http://img/1.png", image.getUrl());
        assertEquals("abc", image.getPublicId());
        assertFalse(uploaded[0].exists());
    }

    @Test
    void uploadFallsBackToDefaultsWhenResultIsEmpty() throws IOException {
        when(uploader.upload(any(File.class), anyMap())).thenReturn(Map.of());

        CloudinaryImage image = service.upload(new MockMultipartFile("picture", "pic.png", "image/png", new byte[]{1}));

        assertEquals("", image.getPublicId());
        assertNotNull(image.getUrl());
    }

    @Test
    void deleteReturnsTrueOnSuccess() throws IOException {
        assertTrue(service.delete("abc"));
        verify(uploader).destroy(eq("abc"), anyMap());
    }

    @Test
    void deleteReturnsFalseOnIOException() throws IOException {
        when(uploader.destroy(eq("abc"), anyMap())).thenThrow(new IOException("boom"));

        assertFalse(service.delete("abc"));
    }
}

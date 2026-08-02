package com.notesapp.notesapp;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.notesapp.notesapp.controller.NoteController;
import com.notesapp.notesapp.entity.Note;
import com.notesapp.notesapp.service.NoteService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllNotes_ShouldReturnListOfNotes() throws Exception {
        // Arrange
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("First Note");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Second Note");

        when(noteService.getAllNotes()).thenReturn(Arrays.asList(note1, note2));

        // Act & Assert
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("First Note"));
    }

    @Test
    void createNote_ShouldReturnCreatedNote() throws Exception {
        // Arrange
        Note note = new Note();
        note.setTitle("New Note");
        note.setContent("New Content");

        Note savedNote = new Note();
        savedNote.setId(1L);
        savedNote.setTitle("New Note");
        savedNote.setContent("New Content");

        when(noteService.createNote(any(Note.class))).thenReturn(savedNote);

        // Act & Assert
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Note"));
    }

    @Test
    void getNoteById_ShouldReturnNote() throws Exception {
        // Arrange
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");

        when(noteService.getNoteById(1L)).thenReturn(note);

        // Act & Assert
        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Note"));
    }

    @Test
    void updateNote_ShouldReturnUpdatedNote() throws Exception {
        // Arrange
        Note updatedNote = new Note();
        updatedNote.setId(1L);
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");

        when(noteService.updateNote(eq(1L), any(Note.class))).thenReturn(updatedNote);

        // Act & Assert
        mockMvc.perform(put("/api/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void deleteNote_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        doNothing().when(noteService).deleteNote(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isOk());

        verify(noteService, times(1)).deleteNote(1L);
    }
}
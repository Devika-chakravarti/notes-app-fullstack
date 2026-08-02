package com.notesapp.notesapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.notesapp.notesapp.entity.Note;
import com.notesapp.notesapp.service.NoteService;

@SpringBootTest
class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Test
    void createNote_ShouldSaveNoteSuccessfully() {
        // Arrange
        Note note = new Note();
        note.setTitle("Test Note");
        note.setContent("This is a test note content");

        // Act
        Note savedNote = noteService.createNote(note);

        // Assert
        assertNotNull(savedNote.getId());
        assertEquals("Test Note", savedNote.getTitle());
        assertNotNull(savedNote.getCreatedDate());
    }

    @Test
    void getAllNotes_ShouldReturnNonNullList() {
        // Act
        List<Note> notes = noteService.getAllNotes();

        // Assert
        assertNotNull(notes);
    }

    @Test
    void getNoteById_WhenNoteExists_ShouldReturnCorrectNote() {
        // Arrange
        Note note = new Note();
        note.setTitle("Find Me");
        note.setContent("Content here");
        Note saved = noteService.createNote(note);

        // Act
        Note found = noteService.getNoteById(saved.getId());

        // Assert
        assertEquals("Find Me", found.getTitle());
    }

    @Test
    void getNoteById_WhenNoteDoesNotExist_ShouldThrowException() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            noteService.getNoteById(999999L);
        });
    }

    @Test
    void updateNote_ShouldChangeTitleAndContent() {
        // Arrange
        Note note = new Note();
        note.setTitle("Old Title");
        note.setContent("Old Content");
        Note saved = noteService.createNote(note);

        Note updatedDetails = new Note();
        updatedDetails.setTitle("New Title");
        updatedDetails.setContent("New Content");

        // Act
        Note result = noteService.updateNote(saved.getId(), updatedDetails);

        // Assert
        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
    }

    @Test
    void deleteNote_ShouldRemoveNoteFromDatabase() {
        // Arrange
        Note note = new Note();
        note.setTitle("To Be Deleted");
        note.setContent("Delete me");
        Note saved = noteService.createNote(note);

        // Act
        noteService.deleteNote(saved.getId());

        // Assert
        assertThrows(RuntimeException.class, () -> {
            noteService.getNoteById(saved.getId());
        });
    }
}
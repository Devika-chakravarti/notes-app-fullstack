package com.notesapp.notesapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notesapp.notesapp.entity.Note;
import com.notesapp.notesapp.repository.NoteRepository;

@Service
public class NoteService {

	@Autowired
	private NoteRepository noteRepository;

	// Create a new note
	public Note createNote(Note note) {
		return noteRepository.save(note);
	}

	// Get all notes
	public List<Note> getAllNotes() {
		return noteRepository.findAll();
	}

	// Get a single note by id
	public Note getNoteById(Long id) {
		return noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
	}

	// Update an existing note
	public Note updateNote(Long id, Note updatedNote) {
		Note existingNote = getNoteById(id);
		existingNote.setTitle(updatedNote.getTitle());
		existingNote.setContent(updatedNote.getContent());
		return noteRepository.save(existingNote);
	}

	// Delete a note
	public void deleteNote(Long id) {
		Note note = getNoteById(id);
		noteRepository.delete(note);
	}
}
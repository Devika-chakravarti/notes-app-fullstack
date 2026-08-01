package com.notesapp.notesapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notesapp.notesapp.entity.Note;
import com.notesapp.notesapp.service.NoteService;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:5173")
public class NoteController {

	@Autowired
	private NoteService noteService;

	// CREATE - Add a new note
	@PostMapping
	public Note createNote(@RequestBody Note note) {
		return noteService.createNote(note);
	}

	// READ - Get all notes
	@GetMapping
	public List<Note> getAllNotes() {
		return noteService.getAllNotes();
	}

	// READ - Get a single note by id
	@GetMapping("/{id}")
	public Note getNoteById(@PathVariable Long id) {
		return noteService.getNoteById(id);
	}

	// UPDATE - Update an existing note
	@PutMapping("/{id}")
	public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
		return noteService.updateNote(id, note);
	}

	// DELETE - Delete a note
	@DeleteMapping("/{id}")
	public String deleteNote(@PathVariable Long id) {
		noteService.deleteNote(id);
		return "Note deleted successfully with id: " + id;
	}
}
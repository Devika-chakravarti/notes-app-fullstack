package com.notesapp.notesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notesapp.notesapp.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
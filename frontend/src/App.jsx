import { useState, useEffect } from 'react';
import axios from 'axios';

const API_URL = 'https://notes-app-fullstack-ymed.onrender.com/api/notes';

function App() {
  const [notes, setNotes] = useState([]);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchNotes();
  }, []);

  const fetchNotes = async () => {
    try {
      const response = await axios.get(API_URL);
      setNotes(response.data);
    } catch (error) {
      console.error('Error fetching notes:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title.trim() || !content.trim()) return;

    try {
      if (editingId) {
        await axios.put(`${API_URL}/${editingId}`, { title, content });
        setEditingId(null);
      } else {
        await axios.post(API_URL, { title, content });
      }
      setTitle('');
      setContent('');
      fetchNotes();
    } catch (error) {
      console.error('Error saving note:', error);
    }
  };

  const handleEdit = (note) => {
    setEditingId(note.id);
    setTitle(note.title);
    setContent(note.content);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm('Delete this note? This cannot be undone.');
    if (!confirmed) return;

    try {
      await axios.delete(`${API_URL}/${id}`);
      fetchNotes();
    } catch (error) {
      console.error('Error deleting note:', error);
    }
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setTitle('');
    setContent('');
  };

  const isFormValid = title.trim() && content.trim();

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 px-4 py-14 relative overflow-hidden">

      {/* Background glow effects */}
      <div className="absolute top-[-10%] left-[15%] w-[500px] h-[500px] bg-indigo-600/20 rounded-full blur-[120px] pointer-events-none"></div>
      <div className="absolute top-[20%] right-[10%] w-[400px] h-[400px] bg-purple-600/15 rounded-full blur-[120px] pointer-events-none"></div>
      <div className="absolute bottom-[-10%] left-[30%] w-[450px] h-[450px] bg-blue-600/15 rounded-full blur-[120px] pointer-events-none"></div>

      {/* Grid pattern overlay */}
      <div
        className="absolute inset-0 opacity-[0.04] pointer-events-none"
        style={{
          backgroundImage: 'linear-gradient(to right, white 1px, transparent 1px), linear-gradient(to bottom, white 1px, transparent 1px)',
          backgroundSize: '48px 48px'
        }}
      ></div>

      <div className="max-w-5xl mx-auto relative z-10">

        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-5xl font-bold tracking-tight text-white">
            NOTES
          </h1>
          <p className="text-slate-500 mt-3 text-sm">
            Capture your thoughts, one note at a time.
          </p>
        </div>

        {/* Form */}
        <form
          onSubmit={handleSubmit}
          className="bg-slate-900/60 backdrop-blur-xl border border-slate-800/60 rounded-2xl p-6 shadow-2xl mb-12 max-w-xl mx-auto"
        >
          <input
            type="text"
            placeholder="Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full bg-slate-800/60 text-slate-100 placeholder-slate-500 rounded-lg px-4 py-2.5 mb-3 outline-none border border-transparent focus:border-indigo-500 transition-colors"
          />
          <textarea
            placeholder="Write your note here..."
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows="4"
            className="w-full bg-slate-800/60 text-slate-100 placeholder-slate-500 rounded-lg px-4 py-2.5 mb-4 outline-none border border-transparent focus:border-indigo-500 transition-colors resize-none"
          />
          <div className="flex gap-3">
            <button
              type="submit"
              disabled={!isFormValid}
              className="bg-indigo-600 hover:bg-indigo-500 disabled:bg-slate-800 disabled:text-slate-600 disabled:cursor-not-allowed transition-colors text-white font-medium px-5 py-2.5 rounded-lg"
            >
              {editingId ? 'Update Note' : 'Add Note'}
            </button>
            {editingId && (
              <button
                type="button"
                onClick={handleCancelEdit}
                className="bg-transparent border border-slate-700 hover:border-slate-500 transition-colors text-slate-300 font-medium px-5 py-2.5 rounded-lg"
              >
                Cancel
              </button>
            )}
          </div>
        </form>

        {/* Notes Grid */}
        {loading ? (
          <p className="text-center text-slate-600">Loading notes...</p>
        ) : notes.length === 0 ? (
          <div className="text-center text-slate-600 mt-20">
            <div className="text-5xl mb-4">—</div>
            <p className="text-slate-500">No notes yet. Add your first one above.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
            {notes.map((note) => (
              <div
                key={note.id}
                className="group bg-slate-900/60 backdrop-blur-xl border border-slate-800/60 rounded-xl p-5 hover:border-slate-700 hover:-translate-y-0.5 transition-all"
              >
                <h2 className="text-lg font-semibold mb-2 break-words text-white">
                  {note.title}
                </h2>
                <p className="text-slate-400 text-sm mb-4 break-words whitespace-pre-wrap leading-relaxed">
                  {note.content}
                </p>
                <div className="flex items-center justify-between">
                  <p className="text-xs text-slate-600">
                    {new Date(note.createdDate).toLocaleDateString('en-US', {
                      month: 'short',
                      day: 'numeric',
                      year: 'numeric',
                    })}
                  </p>
                  <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                    <button
                      onClick={() => handleEdit(note)}
                      className="text-xs font-medium text-indigo-400 hover:text-indigo-300 transition-colors"
                    >
                      Edit
                    </button>
                    <span className="text-slate-700">·</span>
                    <button
                      onClick={() => handleDelete(note.id)}
                      className="text-xs font-medium text-red-400 hover:text-red-300 transition-colors"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
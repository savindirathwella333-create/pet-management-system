import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client.js'
import { useAuth } from '../context/AuthContext.jsx'
import ServiceStamp from '../components/ServiceStamp.jsx'

const SPECIES_OPTIONS = ['Dog', 'Cat', 'Bird', 'Rabbit', 'Other']

export default function DashboardPage() {
  const { auth } = useAuth()
  const [pets, setPets] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [formOpen, setFormOpen] = useState(false)
  const [form, setForm] = useState({ name: '', species: 'Dog', breed: '', age: '' })
  const [submitting, setSubmitting] = useState(false)

  async function loadPets() {
    setLoading(true)
    try {
      const data = await api.getPetsByOwner(auth.ownerId, auth.token)
      setPets(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadPets()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  function update(field) {
    return (e) => setForm({ ...form, [field]: e.target.value })
  }

  async function handleAddPet(e) {
    e.preventDefault()
    setSubmitting(true)
    setError('')
    try {
      await api.createPet(
        { ...form, age: Number(form.age) || 0, ownerId: auth.ownerId },
        auth.token
      )
      setForm({ name: '', species: 'Dog', breed: '', age: '' })
      setFormOpen(false)
      loadPets()
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="eyebrow">Function 1 → 2 · Owner → Pet</p>
          <h1>Your pets</h1>
          <p className="subtitle">
            Every pet below was validated against your owner record before it could be created.
          </p>
        </div>
        <button className="btn btn-primary" onClick={() => setFormOpen((v) => !v)}>
          {formOpen ? 'Cancel' : '+ Add a pet'}
        </button>
      </div>

      {formOpen && (
        <form onSubmit={handleAddPet} className="form form-inline card">
          <label>
            Name
            <input value={form.name} onChange={update('name')} required />
          </label>
          <label>
            Species
            <select value={form.species} onChange={update('species')}>
              {SPECIES_OPTIONS.map((s) => (
                <option key={s} value={s}>{s}</option>
              ))}
            </select>
          </label>
          <label>
            Breed
            <input value={form.breed} onChange={update('breed')} />
          </label>
          <label>
            Age
            <input type="number" min="0" value={form.age} onChange={update('age')} />
          </label>
          <button className="btn btn-primary" type="submit" disabled={submitting}>
            {submitting ? 'Saving…' : 'Save pet'}
          </button>
        </form>
      )}

      {error && <p className="form-error">{error}</p>}

      {loading ? (
        <p className="muted">Loading pets…</p>
      ) : pets.length === 0 ? (
        <div className="empty-state card">
          <p>No pets yet. Add your first one to start the chain: pet → appointment → medical record.</p>
        </div>
      ) : (
        <div className="card-grid">
          {pets.map((pet) => (
            <Link to={`/pets/${pet.id}`} key={pet.id} className="record-card">
              <div className="record-card-top">
                <h3>{pet.name}</h3>
                <span className="pill">{pet.species}</span>
              </div>
              <p className="muted">{pet.breed || 'Breed not specified'} · {pet.age ?? '—'} yrs old</p>
              <ServiceStamp service="PET-SERVICE" port="8082" />
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}

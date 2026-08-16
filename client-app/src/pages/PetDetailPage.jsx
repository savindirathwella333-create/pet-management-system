import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client.js'
import { useAuth } from '../context/AuthContext.jsx'
import ServiceStamp from '../components/ServiceStamp.jsx'

export default function PetDetailPage() {
  const { petId } = useParams()
  const { auth } = useAuth()
  const [pet, setPet] = useState(null)
  const [appointments, setAppointments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [formOpen, setFormOpen] = useState(false)
  const [form, setForm] = useState({ vetName: '', appointmentDate: '', reason: '' })
  const [submitting, setSubmitting] = useState(false)

  async function loadData() {
    setLoading(true)
    try {
      const [petData, appts] = await Promise.all([
        api.getPet(petId, auth.token),
        api.getAppointmentsByPet(petId, auth.token),
      ])
      setPet(petData)
      setAppointments(appts)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [petId])

  function update(field) {
    return (e) => setForm({ ...form, [field]: e.target.value })
  }

  async function handleAddAppointment(e) {
    e.preventDefault()
    setSubmitting(true)
    setError('')
    try {
      await api.createAppointment(
        { ...form, petId: Number(petId) },
        auth.token
      )
      setForm({ vetName: '', appointmentDate: '', reason: '' })
      setFormOpen(false)
      loadData()
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return <p className="muted page">Loading pet…</p>

  return (
    <div className="page">
      <Link to="/" className="back-link">← All pets</Link>

      {pet && (
        <div className="detail-header card">
          <div>
            <p className="eyebrow">Function 2 → 3 · Pet → Appointment</p>
            <h1>{pet.name}</h1>
            <p className="subtitle">{pet.species} · {pet.breed || 'Breed not specified'} · {pet.age ?? '—'} yrs old</p>
          </div>
          <ServiceStamp service="PET-SERVICE" port="8082" />
        </div>
      )}

      <div className="page-header">
        <h2>Vet appointments</h2>
        <button className="btn btn-primary" onClick={() => setFormOpen((v) => !v)}>
          {formOpen ? 'Cancel' : '+ Book appointment'}
        </button>
      </div>

      {formOpen && (
        <form onSubmit={handleAddAppointment} className="form form-inline card">
          <label>
            Vet name
            <input value={form.vetName} onChange={update('vetName')} required />
          </label>
          <label>
            Date &amp; time
            <input type="datetime-local" value={form.appointmentDate} onChange={update('appointmentDate')} required />
          </label>
          <label>
            Reason
            <input value={form.reason} onChange={update('reason')} placeholder="Annual checkup" />
          </label>
          <button className="btn btn-primary" type="submit" disabled={submitting}>
            {submitting ? 'Booking…' : 'Book'}
          </button>
        </form>
      )}

      {error && <p className="form-error">{error}</p>}

      {appointments.length === 0 ? (
        <div className="empty-state card">
          <p>No appointments booked yet for {pet?.name}.</p>
        </div>
      ) : (
        <div className="card-grid">
          {appointments.map((appt) => (
            <Link to={`/appointments/${appt.id}`} key={appt.id} className="record-card">
              <div className="record-card-top">
                <h3>{appt.vetName}</h3>
                <span className={`pill pill-${appt.status.toLowerCase()}`}>{appt.status}</span>
              </div>
              <p className="muted">{new Date(appt.appointmentDate).toLocaleString()}</p>
              <p className="muted">{appt.reason}</p>
              <ServiceStamp service="APPOINTMENT-SERVICE" port="8083" />
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}

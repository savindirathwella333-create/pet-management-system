import React, { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client.js'
import { useAuth } from '../context/AuthContext.jsx'
import ServiceStamp from '../components/ServiceStamp.jsx'

export default function AppointmentDetailPage() {
  const { appointmentId } = useParams()
  const { auth } = useAuth()
  const [appointment, setAppointment] = useState(null)
  const [records, setRecords] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [formOpen, setFormOpen] = useState(false)
  const [form, setForm] = useState({ diagnosis: '', treatment: '' })
  const [submitting, setSubmitting] = useState(false)
  const [statusSaving, setStatusSaving] = useState(false)

  async function loadData() {
    setLoading(true)
    try {
      const appt = await api.getAppointment(appointmentId, auth.token)
      setAppointment(appt)
      const recs = await api.getRecordsByPet(appt.petId, auth.token)
      setRecords(recs.filter((r) => r.appointmentId === Number(appointmentId)))
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [appointmentId])

  function update(field) {
    return (e) => setForm({ ...form, [field]: e.target.value })
  }

  async function handleAddRecord(e) {
    e.preventDefault()
    setSubmitting(true)
    setError('')
    try {
      await api.createRecord(
        { ...form, petId: appointment.petId, appointmentId: Number(appointmentId) },
        auth.token
      )
      setForm({ diagnosis: '', treatment: '' })
      setFormOpen(false)
      loadData()
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  async function handleStatusChange(status) {
    setStatusSaving(true)
    try {
      const updated = await api.updateAppointmentStatus(appointmentId, status, auth.token)
      setAppointment(updated)
    } catch (err) {
      setError(err.message)
    } finally {
      setStatusSaving(false)
    }
  }

  if (loading) return <p className="muted page">Loading appointment…</p>

  return (
    <div className="page">
      {appointment && (
        <Link to={`/pets/${appointment.petId}`} className="back-link">← Back to pet</Link>
      )}

      {appointment && (
        <div className="detail-header card">
          <div>
            <p className="eyebrow">Function 3 → 4 · Appointment → Medical record</p>
            <h1>{appointment.vetName}</h1>
            <p className="subtitle">
              {new Date(appointment.appointmentDate).toLocaleString()} · {appointment.reason}
            </p>
            <div className="status-row">
              {['SCHEDULED', 'COMPLETED', 'CANCELLED'].map((s) => (
                <button
                  key={s}
                  className={`pill pill-${s.toLowerCase()} ${appointment.status === s ? 'pill-active' : ''}`}
                  disabled={statusSaving}
                  onClick={() => handleStatusChange(s)}
                >
                  {s}
                </button>
              ))}
            </div>
          </div>
          <ServiceStamp service="APPOINTMENT-SERVICE" port="8083" />
        </div>
      )}

      <div className="page-header">
        <h2>Medical records</h2>
        <button className="btn btn-primary" onClick={() => setFormOpen((v) => !v)}>
          {formOpen ? 'Cancel' : '+ Add record'}
        </button>
      </div>

      {formOpen && (
        <form onSubmit={handleAddRecord} className="form form-inline card">
          <label>
            Diagnosis
            <input value={form.diagnosis} onChange={update('diagnosis')} required />
          </label>
          <label>
            Treatment
            <input value={form.treatment} onChange={update('treatment')} placeholder="Optional" />
          </label>
          <button className="btn btn-primary" type="submit" disabled={submitting}>
            {submitting ? 'Saving…' : 'Save record'}
          </button>
        </form>
      )}

      {error && <p className="form-error">{error}</p>}

      {records.length === 0 ? (
        <div className="empty-state card">
          <p>No medical records logged for this appointment yet.</p>
        </div>
      ) : (
        <div className="card-grid">
          {records.map((rec) => (
            <div key={rec.id} className="record-card record-card-static">
              <div className="record-card-top">
                <h3>{rec.diagnosis}</h3>
              </div>
              <p className="muted">{rec.treatment || 'No treatment noted'}</p>
              <p className="muted">{new Date(rec.recordDate).toLocaleDateString()}</p>
              <ServiceStamp service="MEDICAL-RECORD-SERVICE" port="8084" />
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

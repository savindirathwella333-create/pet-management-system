const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

/**
 * Every request goes through the API Gateway (port 8080) ONLY. The client
 * never talks to a microservice directly and never sees any X-API-KEY -
 * the Gateway attaches those itself. Protected requests carry the JWT
 * issued by owner-auth-service's /api/auth/login.
 */
async function request(path, { method = 'GET', body, token } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  if (token) headers['Authorization'] = `Bearer ${token}`

  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  })

  const contentType = res.headers.get('content-type') || ''
  const data = contentType.includes('application/json') ? await res.json() : null

  if (!res.ok) {
    const message = data?.message || `Request failed with status ${res.status}`
    throw new Error(message)
  }
  return data
}

export const api = {
  register: (payload) => request('/api/auth/register', { method: 'POST', body: payload }),
  login: (payload) => request('/api/auth/login', { method: 'POST', body: payload }),

  getPetsByOwner: (ownerId, token) => request(`/api/pets/owner/${ownerId}`, { token }),
  getPet: (id, token) => request(`/api/pets/${id}`, { token }),
  createPet: (payload, token) => request('/api/pets', { method: 'POST', body: payload, token }),

  getAppointmentsByPet: (petId, token) => request(`/api/appointments/pet/${petId}`, { token }),
  getAppointment: (id, token) => request(`/api/appointments/${id}`, { token }),
  createAppointment: (payload, token) => request('/api/appointments', { method: 'POST', body: payload, token }),
  updateAppointmentStatus: (id, status, token) =>
    request(`/api/appointments/${id}/status`, { method: 'PUT', body: { status }, token }),

  getRecordsByPet: (petId, token) => request(`/api/records/pet/${petId}`, { token }),
  createRecord: (payload, token) => request('/api/records', { method: 'POST', body: payload, token }),
}

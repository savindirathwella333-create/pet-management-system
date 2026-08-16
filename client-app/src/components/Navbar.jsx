import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

export default function Navbar() {
  const { auth, logout } = useAuth()
  const navigate = useNavigate()

  return (
    <header className="navbar">
      <Link to="/" className="navbar-brand">
        <span className="brand-mark">PMS</span>
        <span className="brand-text">Pet Management System</span>
      </Link>
      {auth && (
        <div className="navbar-user">
          <span className="owner-name">{auth.name}</span>
          <button
            className="btn btn-ghost"
            onClick={() => {
              logout()
              navigate('/login')
            }}
          >
            Sign out
          </button>
        </div>
      )}
    </header>
  )
}

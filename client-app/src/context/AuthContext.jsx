import React, { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => {
    const stored = localStorage.getItem('pms_auth')
    return stored ? JSON.parse(stored) : null
  })

  const login = (authResponse) => {
    setAuth(authResponse)
    localStorage.setItem('pms_auth', JSON.stringify(authResponse))
  }

  const logout = () => {
    setAuth(null)
    localStorage.removeItem('pms_auth')
  }

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
